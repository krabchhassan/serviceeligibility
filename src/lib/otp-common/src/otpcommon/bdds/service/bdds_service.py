import logging
import os
import posixpath
from multiprocessing import current_process
from typing import List, TypeVar, Any

from otpcommon.bdds.exception.bdds_exception import BddsException
from otpcommon.bdds.model.bdds_extract_contract import BddsExtractContractResponse, BddsExtractContractRequest
from otpcommon.bdds.model.bdds_search_dto import BddsSearchRequest, BddsSearchResponse
from otpcommon.mls.model.server_info import ServerInfo
from otpcommon.rest.model.http_status import HttpStatus
from otpcommon.rest.service.keycloak_manager import KeycloakAuthManager
from otpcommon.rest.service.rest_client import RestClient

T = TypeVar('T')
ENDPOINT_SEARCH = "searchBeneficiaries"
ENDPOINT_EXTRACT_CONTRACT = os.environ.get("BDDS_EXTRACT_CONTRACT_ENDPOINT", "contrats/extract")

logger = logging.getLogger("otpcommon")


class BddsService:

    # --------------------
    # CONSTRUCTOR
    # --------------------
    def __init__(self,
                 rest_client: RestClient = RestClient(),
                 keycloak_manager: KeycloakAuthManager = KeycloakAuthManager()):
        self.rest_client = rest_client
        self.keycloak_manager = keycloak_manager

    # --------------------
    # UTILS
    # --------------------
    def __get_authorization_header(self, server: ServerInfo) -> dict:
        # Token access already here
        if server.auth_header is not None:
            return self.keycloak_manager.get_auth_header(access_token=server.auth_header)
        # No token
        if server.client_id is None or server.client_secret is None or server.token_uri is None:
            return {}
        # Retrieve token with client_id & client_secret
        return self.keycloak_manager.get_http_authorization_header(
            token_uri=server.token_uri,
            client_id=server.client_id,
            client_secret=server.client_secret)

    def __post(self, server: ServerInfo, payload: dict) -> List[BddsSearchResponse]:
        api_call_status, full_url, pid, response, response_json_data = self.__call_bdds_beneficiary_search_endpoint(
            payload, server)
        if api_call_status == HttpStatus.OK:
            data = response_json_data.get('data')  # get content on paging result
            return BddsSearchResponse.schema().load(data, many=True)

        logger.error('-- bdds fail: {pid: %s, url: %s, response: %s}', pid, full_url, response)
        raise BddsException(status=api_call_status, error=response_json_data)

    def __post_local(self, server: ServerInfo, payload: dict) -> dict:
        api_call_status, full_url, pid, response, response_json_data = self.__call_bdds_beneficiary_search_endpoint(
            payload, server)
        if api_call_status == HttpStatus.OK:
            return response_json_data

        logger.error('-- bdds fail: {pid: %s, url: %s, response: %s}', pid, full_url, response)
        raise BddsException(status=api_call_status, error=response_json_data)

    def __call_bdds_beneficiary_search_endpoint(self, payload, server, headers=None):
        pid = current_process().pid
        full_url = posixpath.join(server.base_url, ENDPOINT_SEARCH)
        logger.debug('-- call bdds with: {pid: %s, url: %s, payload: %s}', pid, full_url, payload)

        if headers is None:
            headers = self.__get_authorization_header(server)
        response = self.rest_client.post(endpoint=full_url, payload=payload, headers=headers, is_local=server.is_local)

        (api_call_status, response_json_data) = response  # Tuple(HttpStatus, dict)

        logger.debug('-- bdds response: {pid: %s, status: %s, content: %s}', pid, api_call_status, response_json_data)
        return api_call_status, full_url, pid, response, response_json_data

    async def __async_post(self, server: ServerInfo, payload: Any, worker_id: int) -> List[BddsSearchResponse]:
        full_url = posixpath.join(server.base_url, ENDPOINT_EXTRACT_CONTRACT)

        logger.debug('-- worker-%s : call bdds with url: %s}', worker_id, full_url)
        headers = self.__get_authorization_header(server)
        response = await self.rest_client.async_post(url=full_url, payload=payload, headers=headers)

        (api_call_status, response_json_data) = response  # Tuple(HttpStatus, dict)
        logger.debug('-- worker-%s : bdds response status: %s}', worker_id, api_call_status)
        if api_call_status == HttpStatus.OK:
            return response_json_data

        logger.error('-- worker-%s : bdds fail: {url: %s, response: %s}', worker_id, full_url, response)
        raise BddsException(status=api_call_status, error=response_json_data)

    def page_of(self, items: List[T]) -> dict:
        return {
            "data": items,
            "paging": {
                "page": 0,
                "totalPages": None,
                "totalElements": len(items),
                "perPage": 10
            }
        }

    def merge(self, results: List[List[BddsSearchResponse]]) -> dict:
        logger.debug('-- bdds results: merge')
        # join results
        join_set = set()
        for one_result in results:
            join_set.update(one_result)
        # Order by (contrats.data.nom.prenom, contrats.data.nom.nomUsage)
        logger.debug('-- bdds results: sort')
        flatten_list = [i.to_dict() for i in sorted(join_set)]
        # map to pageable result
        logger.debug('-- bdds results: paging')
        return self.page_of(flatten_list)

    # --------------------
    # METHODS
    # --------------------
    def find_all_search(self, server: ServerInfo, request: BddsSearchRequest) -> List[BddsSearchResponse]:
        if not server.is_local:
            # On ne récupère que les bénéficiaires avec TP
            request.service_metier = 'Service_TP'

        return self.__post(server=server, payload=request.to_dict())

    def find_all_search_local(self, server: ServerInfo, request: BddsSearchRequest) -> dict:
        return self.__post_local(server=server, payload=request.to_dict())

    async def find_all_extract(
            self,
            worker_id: int,
            server_info: ServerInfo,
            request: List[BddsExtractContractRequest]
    ) -> BddsExtractContractResponse:
        response = await self.__async_post(
            server=server_info,
            payload=[req.to_dict() for req in request],
            worker_id=worker_id
        )
        return BddsExtractContractResponse.from_dict(response)

    def get(self, server: ServerInfo, params: dict, url: str) -> dict:
        pid = current_process().pid
        full_url = posixpath.join(server.base_url, url)

        logger.info(f'-- call bdds with: {{pid: {pid}, url: {full_url}, params: {params}}}')
        headers = self.__get_authorization_header(server)
        response = self.rest_client.get(endpoint=full_url, qparams=params, headers=headers, is_local=server.is_local)

        (api_call_status, response_json_data) = response  # Tuple(HttpStatus, dict)
        logger.debug(f'-- bdds response: {{pid: {pid}, status: {api_call_status}, content: {response_json_data}}}')
        if api_call_status == HttpStatus.OK:
            return response_json_data

        logger.error(f'-- bdds fail: {{pid: {pid}, url: {full_url}, response: {response}}}')
        raise BddsException(status=api_call_status, error=response_json_data)
