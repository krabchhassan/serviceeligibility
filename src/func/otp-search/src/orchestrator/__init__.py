import logging
import traceback
from os import environ
from itertools import cycle
from multiprocessing import Pool
from typing import List, Set

from otpcommon.aiguillage.model.aiguillage_dto import AiguillageRequest, AiguillageResponse
from otpcommon.aiguillage.service.aiguillage_service import AiguillageService
from otpcommon.bdds.model.bdds_search_dto import BddsSearchRequest, BddsSearchResponse
from otpcommon.bdds.service.bdds_service import BddsService
from otpcommon.bdds.service.mapper import Mapper
from otpcommon.mls.model.server_info import ServerInfo
from otpcommon.mls.service.mls_service import MlsService

client_type = environ.get('CLIENT_TYPE')
INSURER = 'INSURER'

logger = logging.getLogger("orchestrator")


def search(data) -> dict:
    logger.info('=== Search beneficiaries orchestration ===')
    try:
        # Init
        aiguillage_service: AiguillageService = AiguillageService()
        mls_service: MlsService = MlsService()
        bdds_service: BddsService = BddsService()

        logger.debug(data)
        bdds_request: BddsSearchRequest = BddsSearchRequest.from_dict(data)
        logger.debug(f'bdds_request : {bdds_request}')

        if client_type == INSURER:
            logger.info('1. GET LOCAL SERVER')
            local_server: ServerInfo = mls_service.get_local_server()  # add local server
            logger.info(f'-- local server found : {local_server}')

            logger.info('2. BDDS')
            local_result = bdds_service.find_all_search_local(local_server, bdds_request)
            logger.debug(f'-- bdds local result : {local_result}')
            return local_result
        else:
            logger.info('1. AIGUILLAGE')
            amc_list: Set[str] = set()
            if bdds_request.declarant_id:
                amc_list.add(bdds_request.declarant_id)
            else:
                aiguillage_request: AiguillageRequest = Mapper.bdds_to_aiguillage(bdds_request)
                aiguillage_response: List[AiguillageResponse] = aiguillage_service.find_all_search(aiguillage_request)
                amc_list = aiguillage_service.map_by_amc(aiguillage_response)
            logger.info(f'-- amc: {amc_list}')

            logger.info('2. MLS')
            server_list: Set[ServerInfo] = mls_service.get_servers(amc_list)
            server_list.add(mls_service.get_local_server())  # add local server
            logger.info(f'-- servers: {server_list}')

            logger.info('3. BDDS')
            bdds_response: List[List[BddsSearchResponse]] = []
            with Pool() as pool:
                # run bdds with map/reduce (multi-thread) and wait result
                request_list = [bdds_request]
                args_list = list(zip(server_list, cycle(request_list)))
                logger.debug(f'-- args_list: {args_list}')
                for result in pool.starmap(bdds_service.find_all_search, args_list):
                    logger.info(f'-- bdds {len(result)} results')
                    logger.debug(f'-- starmap: {result}')
                    bdds_response.append(result)

            logger.info('4. REDUCE')
            return bdds_service.merge(bdds_response)
    except Exception as ex:
        logger.error(f'Search beneficiaries fail: {ex}')
        logger.error(traceback.format_exc())
        raise ex
