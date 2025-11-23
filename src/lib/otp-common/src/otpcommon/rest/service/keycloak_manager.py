import json
import logging
import os
from typing import Optional

import requests
from cachetools import cached, TTLCache
from requests import RequestException

from otpcommon.rest.exception.keycloak_exception import UnableToGetHttpAuthorizationHeader, AuthException
from otpcommon.rest.model.http_status import HttpStatus

AUTHORIZATION_HEADER_KEY = "authorization"
GRANT_TYPE_VALUE = "client_credentials"
GRANT_TYPE_KEY = "grant_type"
CLIENT_ID_KEY = "client_id"
CLIENT_SECRET_KEY = "client_secret"
CONTENT_TYPE_KEY = "Content-Type"
CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded"
ERROR_MESSAGE = "Auth error"

BEARER_VALUE = "Bearer "
ACCESS_TOKEN = "access_token"
TOKEN_URL = "{}/protocol/openid-connect/token"


class KeycloakAuthManager:
    def client_id(self) -> str:
        return os.getenv('CLIENT_ID')

    def client_secret(self) -> str:
        return os.getenv('CLIENT_SECRET')

    def issuer(self) -> str:
        keycloak_url = os.getenv('keycloak_url')
        keycloak_realm = os.getenv('keycloak_realm')
        return "{}/realms/{}".format(keycloak_url, keycloak_realm)

    def token_uri(self, issuer_uri: str) -> str:
        return TOKEN_URL.format(issuer_uri)

    @cached(cache=TTLCache(maxsize=1024, ttl=240))
    def get_access_token(self, token_uri: str, client_id: str, client_secret: str) -> str:
        """
        Get the authorization endpoint and retrieve the access token
        :return: access token
        :rtype: str
        :raise: UnableToGetHttpAuthorizationHeader
        """

        try:
            logging.debug('-- get access token from : {url: %s, client_id: %s}', token_uri, client_id)
            payload = {
                GRANT_TYPE_KEY: GRANT_TYPE_VALUE,
                CLIENT_ID_KEY: client_id,
                CLIENT_SECRET_KEY: client_secret
            }
            header = {CONTENT_TYPE_KEY: CONTENT_TYPE_VALUE}

            response = requests.post(url=token_uri, headers=header, data=payload)

            status = HttpStatus.from_code(response.status_code)
            if status != HttpStatus.OK:
                raise AuthException(response.content)

            content = json.loads(response.content)
            access_token = BEARER_VALUE + content.get(ACCESS_TOKEN)
        except (ConnectionError, RequestException):
            raise UnableToGetHttpAuthorizationHeader(ERROR_MESSAGE)
        return access_token

    def get_http_authorization_header(self, token_uri: str, client_id: str, client_secret: str) -> dict:
        """
        Get the access token and wrap it in an authorization header dictionary
        :return: authorization header
        :rtype: dict
        :raise: UnableToGetHttpAuthorizationHeader
        """

        access_token = self.get_access_token(token_uri=token_uri, client_id=client_id, client_secret=client_secret)
        return self.get_auth_header(access_token=access_token)

    def get_auth_header(self, access_token: Optional[str]) -> dict:
        """
        Wrap access token in an authorization header dictionary
        :return: authorization header
        :rtype: dict
        """
        if access_token is None:
            return {}  # early exit

        return {AUTHORIZATION_HEADER_KEY: access_token}
