import argparse
import os
import sys
from typing import List, Set, Union, Dict

from beyondpythonframework.securitycontext import init_execution_context
from otpcommon.aiguillage.model.aiguillage_extract_contract import AiguillageExtractContractRequest, \
    AiguillageExtractContractResponse
from otpcommon.bdds.model.bdds_extract_contract import BddsExtractContractRequest, BddsExtractContractResponse
from otpcommon.mls.model.server_info import ServerInfo

from otpextractrights.configuration import Logger
from otpextractrights.exceptions.general_exception import GeneralException
from otpextractrights.model import InputBeneficiary, OutputBeneficiary, MlsAmcFail, AiguillageOutputSuccess, \
    AiguillageOutputFail, BddsOutputSuccess, BddsOutputFail
from otpextractrights.model.mls import MlsOutputFail
from otpextractrights.service import ValidationService, InputOutputService, AiguillageWorker, BddsWorker, MlsWorker
from otpextractrights.service.mapper import ExtractContractMapper
from otpextractrights.utils import CrexUtils
from otpextractrights.utils.common_utils import dict_of_list_to_list
from otpextractrights.utils.kafka_utils import KafkaUtils

USER_ID_ENV_KEY = "USER_ID"  # Workflow label positioned by the launcher since this job usePersonalWorkdir = true.
SYSTEM_ERROR_CODE = 1


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--file_path", type=str, help="Path entry file", required=True)
    job_args = parser.parse_args()
    user_id = os.getenv(USER_ID_ENV_KEY, None)
    if user_id is None:
        Logger.error("USER_ID label was not provided by the launcher")
        sys.exit(SYSTEM_ERROR_CODE)  # Exit the program with an error signal in the workflow.
    file_path = job_args.file_path
    run(
        user_id=user_id,
        file_path=file_path,
        input_output_service=InputOutputService(),
        validation_service=ValidationService(),
        aiguillage_service=AiguillageWorker(),
        mls_service=MlsWorker(),
        bdds_service=BddsWorker(),
        mapper_service=ExtractContractMapper()
    )


def run(
        user_id: str,
        file_path: str,
        input_output_service: InputOutputService,
        validation_service: ValidationService,
        aiguillage_service: AiguillageWorker,
        mls_service: MlsWorker,
        bdds_service: BddsWorker,
        mapper_service: ExtractContractMapper
):
    try:
        Logger.info("Initialiser le contexte d'exécution")
        init_execution_context()
        Logger.info("Start OMU otp-extract-rights")
        KafkaUtils.publish_start_event(user_hash=user_id, filename=file_path)
        # GET DATA
        Logger.info("Load data from input file")
        input_beneficiaries: List[InputBeneficiary] = input_output_service.get_input(user_id, file_path)
        CrexUtils.set_nb_input_lines(len(input_beneficiaries))

        # VALIDATE DATA
        Logger.info("Data Validation")
        validated_successes, validation_fails = validation_service.validate(input_beneficiaries)

        # GET AMC/BENEF FROM AIGUILLAGE
        Logger.info("Convert aiguillage requests")
        aiguillage_requests: List[AiguillageExtractContractRequest] = mapper_service.to_aiguillage_requests(
            input_beneficiaries=validated_successes
        )
        Logger.info("Aiguillage calls")
        aiguillage_responses: List[AiguillageExtractContractResponse] = aiguillage_service.run(
            requests=aiguillage_requests
        )
        Logger.info("Convert aiguillage outputs")
        aiguillages_output = mapper_service.to_aiguillage_outputs(
            aiguillage_requests=aiguillage_requests,
            aiguillage_responses=aiguillage_responses
        )
        aiguillages_successes: Dict[str, List[AiguillageOutputSuccess]] = aiguillages_output[0]
        aiguillages_fails: Dict[str, AiguillageOutputFail] = aiguillages_output[1]

        # GET INTEGRATION POINT FROM MLS
        Logger.info("Resolve AMC from MLS")
        aiguillages_successes_list: List[AiguillageOutputSuccess] = dict_of_list_to_list(aiguillages_successes)
        amcs: Set[str] = set([benef.amc for benef in aiguillages_successes_list])
        server_infos: Dict[str, Union[ServerInfo, MlsAmcFail]] = mls_service.resolve_amc(amcs=amcs)
        Logger.info("Convert MLS outputs")
        mls_output = mapper_service.to_mls_outputs(
            aiguillage_successes=aiguillages_successes_list,
            server_infos=server_infos
        )
        bdds_requests: Dict[str, BddsExtractContractRequest] = mls_output[0]
        mls_fails: Dict[str, MlsOutputFail] = mls_output[1]

        # GET TP OFFLINE RIGHTS FROM BDDS
        Logger.info("Bdds calls")
        bdds_requests_list: List[BddsExtractContractRequest] = list(bdds_requests.values())
        bdds_responses: List[BddsExtractContractResponse] = bdds_service.run(
            beneficiaries=bdds_requests_list,
            servers=server_infos
        )
        bdds_outputs = mapper_service.to_bdds_outputs(bdds_response=bdds_responses)
        bdds_successes: Dict[str, List[BddsOutputSuccess]] = bdds_outputs[0]
        bdds_fails: Dict[str, BddsOutputFail] = bdds_outputs[1]

        # GENERATE OUTPUT
        Logger.info("Convert output beneficiaries")
        output_beneficiaries: List[OutputBeneficiary] = mapper_service.to_output_beneficiaries(
            input_beneficiaries=input_beneficiaries,
            validation_fails=validation_fails,
            aiguillage_fails=aiguillages_fails,
            aiguillage_success=aiguillages_successes,
            mls_fails=mls_fails,
            bdds_fails=bdds_fails,
            bdds_successes=bdds_successes
        )
        Logger.info("Save output to csv file")
        input_output_service.save_output(output_beneficiaries, user_id, file_path)
        Logger.info("Count CREX success and fails")
        CrexUtils.count_success_fails(inputs=input_beneficiaries, outputs=output_beneficiaries)

    except GeneralException as e:
        Logger.error(e)

    finally:
        Logger.info("Produce CREX")
        CrexUtils.produce_crex()
        Logger.info("Publish end event")
        KafkaUtils.publish_end_event()
