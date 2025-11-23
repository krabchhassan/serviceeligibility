import os
import csv
import json
import sys
import logging
import os
from typing import List
from unittest.mock import Mock
from unittest import TestCase
from asynctest import MagicMock
import asyncio


sys.modules['organisationsettings.services.get_organisation_by_amc'] = Mock()

from otpextractrights.utils.kafka_utils import KafkaUtils
from otpextractrights.utils import CrexUtils
from otpcommon.bdds.model.bdds_extract_contract import BddsExtractContractResponse
from otpcommon.mls.model.server_info import ServerInfo
from otpextractrights.model import MlsAmcFail, AiguillageOutputSuccess, OutputBeneficiary
from otpextractrights.service import MlsWorker, ValidationService, InputOutputService, BddsWorker
from otpextractrights.service.mapper import ExtractContractMapper
from otpextractrights.utils.errors import OC_NOT_FOUND, EMPTY_NIR, NIR_NOT_13, EMPTY_SEARCH_DATE, SEARCH_DATE_NOT_DATE, \
    BIRTH_DATE_NOT_6, AIGUILLAGE_BENEF_NOT_FOUND, BDDS_BENEF_NOT_FOUND

from otpcommon.aiguillage.model.aiguillage_extract_contract import AiguillageExtractContractRequest, \
    AiguillageExtractContractResponse

logger = logging.getLogger()
logger.level = logging.DEBUG
stream_handler = logging.StreamHandler(sys.stdout)
os.environ['BEYOND_INSTANCE_TYPE'] = 'DEDICATED'
os.environ['BEYOND_CUSTOMER'] = 'editeur'
os.environ['BEYOND_INSTANCE'] = 'otp-es15'


def load_file(filename: str):
    with open(f"data/e2e/{filename}", "r") as f:
        return json.load(f)


def load_csv_output(file_path):
    with open(file_path, newline='') as csvfile:
        reader = csv.DictReader(csvfile, delimiter=";")
        outputs = []
        for row in reader:
            outputs.append(
                OutputBeneficiary.from_dict(dict(row))
            )
        return outputs


class TestMain(TestCase):

    @classmethod
    def setUpClass(cls) -> None:
        from otpextractrights.configuration import Logger
        Logger.init_logger()

    async def mock_aiguillage(self, worker_id: int, requests: List[AiguillageExtractContractRequest]):
        data = load_file("multitasking_responses.json")
        await asyncio.sleep(0.2)
        return AiguillageExtractContractResponse.from_dict(data[requests[0].hash])

    def test_e2e(self):
        from otpextractrights.execute_job import run
        from otpextractrights.service.aiguillage_worker import AiguillageWorker

        output_path = "data/e2e/output.csv"
        if os.path.exists(output_path):
            os.remove(output_path)
        self.assertFalse(os.path.exists(output_path))

        mapper = ExtractContractMapper()

        input_output = InputOutputService()
        InputOutputService.get_input_path = MagicMock(return_value="data/e2e/input.csv")
        InputOutputService.get_output_path = MagicMock(return_value=output_path)

        validation = ValidationService()

        aiguillage = AiguillageWorker()
        async def mock_aiguillage(worker_id, requests):
            return AiguillageExtractContractResponse.from_dict(load_file("aiguillage_response.json"))
        aiguillage.aiguillage_service.find_all_extract = MagicMock(side_effect=mock_aiguillage)

        mls = MlsWorker()
        servers = {
            "1": ServerInfo(amc="1"),
            "2": ServerInfo(amc="2"),
            "3": MlsAmcFail(amc="3", libelle_erreur=OC_NOT_FOUND)
        }
        MlsWorker.resolve_amc = MagicMock(return_value=servers)

        bdds = BddsWorker()
        async def mock_bdds(worker_id, server: ServerInfo, req):
            return BddsExtractContractResponse.from_dict(load_file(f"bdds_response_{server.amc}.json"))
        bdds.bdds_service.find_all_extract = MagicMock(side_effect=mock_bdds)

        CrexUtils.produce_crex = MagicMock()
        KafkaUtils.publish_end_event = MagicMock()
        KafkaUtils.publish_start_event = MagicMock

        run(
            user_id="",
            file_path="",
            input_output_service=input_output,
            validation_service=validation,
            aiguillage_service=aiguillage,
            mls_service=mls,
            bdds_service=bdds,
            mapper_service=mapper
        )

        self.assertTrue(os.path.exists(output_path))
        outputs: List[OutputBeneficiary] = load_csv_output(output_path)
        self.assertEqual(len(outputs), 13)
        self.assertEqual(len(list(filter(lambda x: x.code_erreur == "", outputs))), 3)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == OC_NOT_FOUND, outputs))), 1)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == EMPTY_NIR, outputs))), 1)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == NIR_NOT_13, outputs))), 1)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == EMPTY_SEARCH_DATE, outputs))), 1)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == SEARCH_DATE_NOT_DATE, outputs))), 1)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == BIRTH_DATE_NOT_6, outputs))), 1)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == AIGUILLAGE_BENEF_NOT_FOUND, outputs))), 2)
        self.assertEqual(len(list(filter(lambda x: x.libelle_erreur == BDDS_BENEF_NOT_FOUND, outputs))), 2)


