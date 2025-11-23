import logging
import os
import posixpath
from operator import attrgetter
from typing import List, Set, Any

from otpcommon.aiguillage.exception.aiguillage_exception import AiguillageException
from otpcommon.aiguillage.model.aiguillage_dto import AiguillageRequest, AiguillageResponse
from otpcommon.aiguillage.model.aiguillage_extract_contract import AiguillageExtractContractResponse, \
    AiguillageExtractContractRequest
from otpcommon.rest.model.http_status import HttpStatus
from otpcommon.rest.service.keycloak_manager import KeycloakAuthManager
from otpcommon.rest.service.rest_client import RestClient

OTPBENEFAMC_ENDPOINT = "otpbenefamc"
OTPBULKBENEF_AMC = "bulkotpbenefamc"
AIGUILLAGE_URL = os.getenv('AIGUILLAGE_URL', 'http://next-aiguillage-core-api:8080')


class AiguillageService:

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
    def __get_authorization_header(self) -> dict:
        # Retrieve token with client_id & client_secret
        issuer = self.keycloak_manager.issuer()
        token_uri = self.keycloak_manager.token_uri(issuer)
        client_id = self.keycloak_manager.client_id()
        client_secret = self.keycloak_manager.client_secret()
        return self.keycloak_manager.get_http_authorization_header(token_uri, client_id, client_secret)

    def __post(self, payload: dict, endpoint: str) -> List[AiguillageResponse]:
        full_url = posixpath.join(AIGUILLAGE_URL, endpoint)

        logging.debug('-- call aiguillage with: {url: %s, payload: %s}', full_url, payload)
        response = self.rest_client.post(endpoint=full_url, payload=payload, is_local=True)

        (api_call_status, response_json_data) = response  # Tuple(HttpStatus, dict)
        logging.debug('-- aiguillage response: {status: %s, content: %s}', api_call_status, response_json_data)
        if api_call_status == HttpStatus.OK:
            return AiguillageResponse.schema().load(response_json_data, many=True)

        logging.error('-- aiguillage fail: {url: %s, response: %s}', full_url, response)
        raise AiguillageException(status=api_call_status, error=response_json_data)

    async def __async_post(self, worker_id: int, payload: Any, endpoint: str) -> dict:
        full_url = posixpath.join(AIGUILLAGE_URL, endpoint)

        logging.debug('-- worker-%s, call aiguillage with: {url: %s, payload: %s}', worker_id, full_url)
        headers = self.__get_authorization_header()
        response = await self.rest_client.async_post(url=full_url, payload=payload, headers=headers)
        (api_call_status, response_json_data) = response  # Tuple(HttpStatus, dict)
        logging.debug('-- worker-%s, aiguillage response status: %s', worker_id, api_call_status)
        if api_call_status == HttpStatus.OK:
            return response_json_data

        logging.error('-- worker-%s, aiguillage fail: {url: %s, response: %s}', worker_id, full_url, response)
        raise AiguillageException(status=api_call_status, error=response_json_data)

    def map_by_amc(self, response: List[AiguillageResponse]) -> Set[str]:
        return set(map(attrgetter('insurer_id'), response))

    # --------------------
    # METHODS
    # --------------------
    def find_all_search(self, request: AiguillageRequest) -> List[AiguillageResponse]:
        return self.__post(payload=request.to_dict(), endpoint=OTPBENEFAMC_ENDPOINT)

    async def find_all_extract(
            self,
            worker_id: int,
            requests: List[AiguillageExtractContractRequest]
    ) -> AiguillageExtractContractResponse:
        response = await self.__async_post(
                worker_id=worker_id,
                endpoint=OTPBULKBENEF_AMC,
                payload=[req.to_dict() for req in requests]
            )
        return AiguillageExtractContractResponse.from_dict(response)
