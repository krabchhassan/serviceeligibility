import logging
import os
from typing import Set, Optional

from cachetools import cached, TTLCache
from organisationmlssettings.helpers.organization_locator import get_organization_location_by_code
from organisationsettings.services.get_organisation_by_amc import get_organisation_by_amc

from otpcommon.mls.exception.mls_exception import MlsNotFoundException, OcNotFoundException
from otpcommon.mls.model.catalog_enum import CatalogKey
from otpcommon.mls.model.mls_dto import MlsResponse, MlsCatalogConfig
from otpcommon.mls.model.server_info import ServerInfo
from otpcommon.rest.service.keycloak_manager import KeycloakAuthManager

KEYCLOAK_SEPARATOR = "/"
PAU_URL = os.getenv('PAU_URL', 'http://next-serviceeligibility-core-api:8080/v1')
MLS_DEFAULT_TTL = '10800'  # 3h x 60min x 60sec
MLS_CACHE_TTL = float(os.getenv('MLS_CACHE_TTL', MLS_DEFAULT_TTL))

logger = logging.getLogger("mls")

class MlsService:
    # --------------------
    # CONSTRUCTOR
    # --------------------
    def __init__(self, keycloak_manager: KeycloakAuthManager = KeycloakAuthManager()):
        self.init_logging("mls")
        self.keycloak_manager = keycloak_manager

    def init_logging(self, logger_name):
        """
        Initialize logging from framework
        """
        from beyondpythonframework.config import Configuration

        from beyondpythonframework.utils import LOGGING_LEVEL_PATH_NATURE_DEPENDENT
        from beyondpythonframework.config.logging.logging_configuration import LoggingConfiguration
        framework_configuration = Configuration()
        level = framework_configuration.get_value(LOGGING_LEVEL_PATH_NATURE_DEPENDENT)
        LoggingConfiguration().add_additional_loggers_and_configure([logger_name],level)


    # --------------------
    # UTILS
    # --------------------
    def get_local_server(self):
        local_server = ServerInfo(
            base_url=PAU_URL,
            is_local=True
        )
        logger.debug(f'-- use local server: {local_server}')
        return local_server

    def __find_catalog(self, amc: str) -> Optional[MlsResponse]:
        code_os: str = get_organisation_by_amc(amc)[0]
        logger.debug('-- Code OS : {%s}', code_os)
        mls_response: dict = get_organization_location_by_code(code_os)
        logger.debug('-- mapping to MlsResponse : {%s}', mls_response)
        return MlsResponse.from_dict(mls_response)

    def __extract_url(self, mls_response: MlsResponse, key: CatalogKey) -> Optional[str]:
        logger.debug('-- extract url %s : {%s}', key.value, mls_response)
        for catalog in mls_response.catalog_json_response:
            if catalog.integration_point_code == key.value:
                config = MlsCatalogConfig.from_json(catalog.configuration)
                return config.url
        return None

    # --------------------
    # METHODS
    # --------------------
    @cached(cache=TTLCache(maxsize=1024, ttl=MLS_CACHE_TTL))
    def get_server(self, amc: str) -> ServerInfo:
        logger.debug('-- call MLS for amc : %s', amc)
        try:
            mls_response: Optional[MlsResponse] = self.__find_catalog(amc)
            if mls_response is None:
                raise MlsNotFoundException(amc)

            # -- PROXY server --
            keycloak = mls_response.secret_id.split(KEYCLOAK_SEPARATOR)
            client_id = keycloak[0]
            client_secret = keycloak[1]
            issuer_uri = self.__extract_url(mls_response=mls_response, key=CatalogKey.KEYCLOAK)
            proxy_uri = self.__extract_url(mls_response=mls_response, key=CatalogKey.BDDS_API)

            proxy_server = ServerInfo(
                base_url=proxy_uri,
                client_id=client_id,
                client_secret=client_secret,
                issuer_uri=issuer_uri,
                token_uri=self.keycloak_manager.token_uri(issuer_uri),
                amc=amc
            )
            logger.debug('-- use proxy server: %s', proxy_server)
            return proxy_server

        except Exception as ex:
            logger.error('-- MLS fail: {error: %s}', ex)
            # -- Default server --
            return self.get_local_server()

    def get_server_for_extract(self, amc: str) -> ServerInfo:
        logger.debug('-- call MLS for amc : %s', amc)
        try:
            code_os: str = get_organisation_by_amc(amc)[0]
            logger.debug('-- Code OS : {%s}', code_os)
        except Exception as e:
            logger.error('-- MLS fail resolving code os:  %s', e)
            raise OcNotFoundException(amc)
        try:
            mls_response: dict = get_organization_location_by_code(code_os)
            logger.debug('-- mapping to MlsResponse : {%s}', mls_response)
            mls_response: MlsResponse = MlsResponse.from_dict(mls_response)
        except Exception as e:
            logger.error('-- MLS fail resolving integration point: %s', e)
            raise MlsNotFoundException(code_os)

            # -- PROXY server --
        keycloak = mls_response.secret_id.split(KEYCLOAK_SEPARATOR)
        client_id = keycloak[0]
        client_secret = keycloak[1]
        issuer_uri = self.__extract_url(mls_response=mls_response, key=CatalogKey.KEYCLOAK)
        proxy_uri = self.__extract_url(mls_response=mls_response, key=CatalogKey.BDDS_API)

        proxy_server = ServerInfo(
            base_url=proxy_uri,
            client_id=client_id,
            client_secret=client_secret,
            issuer_uri=issuer_uri,
            token_uri=self.keycloak_manager.token_uri(issuer_uri),
            amc=amc
        )
        logger.debug('-- use proxy server: %s', proxy_server)
        return proxy_server

    def get_servers(self, amc_list: Set[str]) -> Set[ServerInfo]:
        if not amc_list:
            return {self.get_local_server()}  # early exit
        return set(map(self.get_server, amc_list))
