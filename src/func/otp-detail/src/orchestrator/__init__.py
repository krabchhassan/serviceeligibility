import logging
import traceback
from typing import Optional, Callable, Tuple

from flask import request
from otpcommon.bdds.exception.bdds_exception import BddsException
from otpcommon.bdds.service.bdds_service import BddsService
from otpcommon.mls.model.server_info import ServerInfo
from otpcommon.mls.service.mls_service import MlsService

QUERY_PARAM_PERSON_NUMBER = 'personNumber'
QUERY_PARAM_INSURER_ID = 'insurerId'
QUERY_PARAM_DECLARATIONS_IDS = 'declarationsIds'
QUERY_PARAM_CONTRACT_NUMBER = 'contractNumber'
QUERY_PARAM_ENV_ORIGIN = 'environment'
QUERY_PARAM_ID_DECLARANT = 'idDeclarant'
QUERY_PARAM_AMC_ID = 'amcId'
QUERY_PARAM_REQUEST_ID = 'requestId'

ENDPOINT_DETAIL = 'beneficiaryTpDetails'
ENDPOINT_AFFICHER_PLUS_DECLARATIONS = f'{ENDPOINT_DETAIL}/nextDeclarations'
ENDPOINT_AFFICHER_PLUS_ATTESTATIONS = f'{ENDPOINT_DETAIL}/nextCertifications'
ENDPOINT_AFFICHER_PLUS_CONSOLIDATIONS = f'{ENDPOINT_DETAIL}/nextConsolidatedContratsTP'

logger = logging.getLogger("orchestrator")


def detail(data) -> dict:
    request_id = request.args.get(QUERY_PARAM_REQUEST_ID)
    logger.debug(f'Request_id : {request_id}')

    if request_id == '1':
        return _next_attestations_tp()
    elif request_id == '2':
        return _next_declarations()
    elif request_id == '3':
        return _next_consolidations()
    else:
        logger.info('=== Detail des bénéficiaires orchestration ===')
        params, bdds_service, insurer_id = _get_settings_ochestrator(QUERY_PARAM_INSURER_ID)
        return _bdds_to_bdds_orchestration(bdds_service.get, ENDPOINT_DETAIL, params, insurer_id,
                                           "Detail des bénéficiaires")


def _next_declarations() -> dict:
    logger.info('=== Afficher plus declarations orchestration ===')
    params, bdds_service, insurer_id = _get_settings_ochestrator(QUERY_PARAM_ID_DECLARANT)
    return _bdds_to_bdds_orchestration(bdds_service.get, ENDPOINT_AFFICHER_PLUS_DECLARATIONS, params, insurer_id,
                                       "Afficher plus declarations")


def _next_attestations_tp() -> dict:
    logger.info('=== Afficher plus attestations TP orchestration ===')
    params, bdds_service, insurer_id = _get_settings_ochestrator(QUERY_PARAM_ID_DECLARANT)
    return _bdds_to_bdds_orchestration(bdds_service.get, ENDPOINT_AFFICHER_PLUS_ATTESTATIONS, params, insurer_id,
                                       "Afficher plus attestations TP")


def _next_consolidations() -> dict:
    logger.info('=== Afficher plus consolidations orchestration ===')
    params, bdds_service, insurer_id = _get_settings_ochestrator(QUERY_PARAM_AMC_ID)
    return _bdds_to_bdds_orchestration(bdds_service.get, ENDPOINT_AFFICHER_PLUS_CONSOLIDATIONS, params, insurer_id,
                                       "Afficher plus consolidations")


def _get_settings_ochestrator(insurer_id: str) -> Tuple[dict, BddsService, str]:
    ###############
    # GET PARAM IN FAAS CALL URL
    ###############
    logger.info('1. GET PATH PARAM IN URL')
    params_query = request.args
    insurer_id = params_query.get(insurer_id)
    params = params_query.to_dict()
    bdds_service: BddsService = BddsService()
    logger.debug(f'query param : {params}')
    return params, bdds_service, insurer_id


def _bdds_to_bdds_orchestration(call_bdds_endpoint_func: Callable[[ServerInfo, dict, str], dict], endpoint_bdds: str,
                                endpoint_params: dict,
                                insurer_id: str, endpoint_name_for_log: str) -> dict:
    try:
        local_result: Optional[dict] = None
        remote_result: Optional[dict] = None

        mls_service: MlsService = MlsService()

        environment_origin = endpoint_params[QUERY_PARAM_ENV_ORIGIN]
        if environment_origin is not None:
            if environment_origin == 'internal':
                # appel UI HTP -> bénéficiaire sur HTP ou appel UI OTP -> bénéficiaire sur OTP
                try:
                    logger.info('2. GET LOCAL SERVER')
                    local_server: ServerInfo = mls_service.get_local_server()
                    logger.info(f'-- {endpoint_name_for_log} only on local server: {local_server}')

                    logger.info('3. CALL BDDS')
                    local_result = call_bdds_endpoint_func(local_server, endpoint_params, endpoint_bdds)
                    logger.debug(f'-- local {endpoint_name_for_log}: {local_result}')
                    return local_result
                except BddsException as e:
                    logger.error(f'LOCAL {endpoint_name_for_log} fail: {e}')
            else:
                # appel UI OTP -> bénéficiaire sur HTP
                try:
                    ###############
                    # CALL MLS SERVER TO RETRIEVE REMOTE ADRESS BY AMC
                    ###############
                    logger.info('2. CALL MLS')
                    remote_server: ServerInfo = mls_service.get_server(insurer_id)
                    logger.info(f'-- {endpoint_name_for_log} only on remote server: {remote_server}')

                    logger.info('3. CALL BDDS')
                    remote_result = call_bdds_endpoint_func(remote_server, endpoint_params, endpoint_bdds)
                    logger.debug(f'-- remote {endpoint_name_for_log}: {remote_result}')
                    return remote_result
                except BddsException as ex:
                    logger.error(f'Remote {endpoint_name_for_log} fail: {ex}')

    except Exception as ex:
        logger.error(f'Orchestration {endpoint_name_for_log} fail: {ex}')
        logger.error(traceback.format_exc())
        raise ex
