import logging
from typing import Tuple, Union, Any

import requests
from aiohttp import ClientSession, ClientTimeout, ClientConnectionError
from asyncio import TimeoutError
from beyondpythonframework.http import get_http_forward_client
from otpcommon.rest.exception.keycloak_exception import UnableToGetHttpAuthorizationHeader
from otpcommon.rest.model.http_status import HttpStatus

DEFAULT_TIMEOUT_VALUE = 30
DEFAULT_ERROR_MESSAGE = "An error occured"
TIMEOUT_DETECTED_MESSAGE = "A timeout occured while connecting to {}"
ACCESS_TOKEN_ERROR_MESSAGE = "Unable to fetch access token for uri {}"
CONNECTION_ERROR_MESSAGE = "A connection error occured while connecting to {}"
HTTP_ERROR_MESSAGE = "An http error occured while connecting to {}"

logger = logging.getLogger("rest")

class RestClient:

    def __init__(self,
                 http_forward=get_http_forward_client()):
        self.http_forward = http_forward

    def __create_dict_containing_message(self, message: str):
        return dict(msg=message)

    def get(self, endpoint: str, qparams: dict = {}, headers: dict = {}, timeout=DEFAULT_TIMEOUT_VALUE, is_local: bool = False) -> Tuple:
        """
        Generic GET method
        :return: status of the request, and json response as dict
        :rtype: Tuple : HttpStatus, dict
        """
        response_json_data = self.__create_dict_containing_message(DEFAULT_ERROR_MESSAGE)
        api_call_status = HttpStatus.EXCEPTION

        try:
            if is_local:
                logger.debug(f'Performing GET request with framework {endpoint} with params {qparams}')
                response = self.http_forward.send_request(url=endpoint, method='GET', query_params=qparams, headers=headers, timeout=timeout)
            else:
                logger.debug(f'Performing GET request {endpoint} with params {qparams}')
                response = requests.get(endpoint, headers=headers, params=qparams, timeout=timeout)
            logger.debug(f'Response : {response}')
            api_call_status = HttpStatus.from_code(response.status_code)
            logger.debug(f'Status response : {api_call_status}')
            response_json_data = response.json()
        except requests.Timeout:
            response_json_data = self.__create_dict_containing_message(TIMEOUT_DETECTED_MESSAGE.format(endpoint))
        except UnableToGetHttpAuthorizationHeader:
            response_json_data = self.__create_dict_containing_message(ACCESS_TOKEN_ERROR_MESSAGE.format(endpoint))
        except requests.ConnectionError:
            response_json_data = self.__create_dict_containing_message(CONNECTION_ERROR_MESSAGE.format(endpoint))
        except requests.HTTPError as error:
            response = error.response
            api_call_status = HttpStatus.from_code(response.status_code)
            response_json_data = response.json()

        return api_call_status, response_json_data

    def post(self, endpoint: str, payload: dict, headers: dict = {},
             timeout=DEFAULT_TIMEOUT_VALUE, is_local: bool = False) -> Tuple:
        """
        Generic POST method
        :return: status of the request, and json response as dict
        :rtype: Tuple : HttpStatus, dict
        """
        response_json_data = self.__create_dict_containing_message(DEFAULT_ERROR_MESSAGE)
        api_call_status = HttpStatus.EXCEPTION

        try:
            if is_local:
                logger.debug(f'Performing POST request with framework {endpoint} with body {payload}')
                response = self.http_forward.send_request(url=endpoint, method='POST', body=payload, timeout=timeout, headers=headers)
            else:
                logger.debug(f'Performing POST request {endpoint} with body {payload}')
                response = requests.post(endpoint, headers=headers, timeout=timeout, json=payload)
            logger.debug(f'Response : {response}')
            api_call_status = HttpStatus.from_code(response.status_code)
            logger.debug(f'Status response : {api_call_status}')
            response_json_data = response.json()
        except requests.Timeout:
            response_json_data = self.__create_dict_containing_message(TIMEOUT_DETECTED_MESSAGE.format(endpoint))
        except UnableToGetHttpAuthorizationHeader:
            response_json_data = self.__create_dict_containing_message(ACCESS_TOKEN_ERROR_MESSAGE.format(endpoint))
        except requests.ConnectionError:
            response_json_data = self.__create_dict_containing_message(CONNECTION_ERROR_MESSAGE.format(endpoint))
        except requests.HTTPError as error:
            response = error.response
            api_call_status = HttpStatus.from_code(response.status_code)
            response_json_data = self.__create_dict_containing_message(HTTP_ERROR_MESSAGE.format(endpoint))

        return api_call_status, response_json_data

    async def async_get(self, url: str, qparams: dict = None, session: ClientSession = None,
                  headers: dict = None, timeout=DEFAULT_TIMEOUT_VALUE) -> Tuple:
        """
        Generic GET method
        :return: status of the request, and json response as dict
        :rtype: Tuple : HttpStatus, dict
        """
        if qparams is None:
            qparams = {}
        if headers is None:
            headers = {}

        if session is None:
            client_timeout = ClientTimeout(total=timeout)
            async with ClientSession(timeout=client_timeout) as new_session:
                return await self._async_get(url, new_session, qparams=qparams, headers=headers)
        else:
            return await self._async_get(url, session, qparams=qparams, headers=headers)

    async def async_post(self, url: str, payload: Any, session: ClientSession = None, headers: dict = None,
             timeout=DEFAULT_TIMEOUT_VALUE) -> Tuple:
        """
        Generic POST method
        :return: status of the request, and json response as dict
        :rtype: Tuple : HttpStatus, dict
        """
        if headers is None:
            headers = {}

        if session is None:
            client_timeout = ClientTimeout(total=timeout)
            async with ClientSession(timeout=client_timeout) as new_session:
                return await self._async_post(url, new_session, payload=payload, headers=headers)
        else:
            return await self._async_post(url, session, payload=payload, headers=headers)

    async def _async_get(self, url: str, session: ClientSession, qparams: dict = None,
                         headers: dict = None) -> Tuple:
        """
        Generic GET method
        :return: status of the request, and json response as dict
        :rtype: Tuple : HttpStatus, dict
        """

        try:
            async with session.get(url, params=qparams, headers=headers) as response:
                api_call_status = HttpStatus.from_code(response.status)
                response_json_data = await response.json()
        except ClientConnectionError:
            api_call_status = HttpStatus.EXCEPTION
            response_json_data = self.__create_dict_containing_message(CONNECTION_ERROR_MESSAGE.format(url))
        except TimeoutError:
            api_call_status = HttpStatus.EXCEPTION
            response_json_data = self.__create_dict_containing_message(TIMEOUT_DETECTED_MESSAGE.format(url))
        return api_call_status, response_json_data

    async def _async_post(self, url: str, session: ClientSession, payload: Any,
                          headers: dict = None) -> Tuple:
        """
        Generic GET method
        :return: status of the request, and json response as dict
        :rtype: Tuple : HttpStatus, dict
        """

        try:
            async with session.post(url, json=payload, headers=headers) as response:
                api_call_status = HttpStatus.from_code(response.status)
                response_json_data = await response.json()
        except ClientConnectionError as e:
            logging.error(e)
            api_call_status = HttpStatus.EXCEPTION
            response_json_data = self.__create_dict_containing_message(CONNECTION_ERROR_MESSAGE.format(url))
        except TimeoutError as e:
            logging.error(e)
            api_call_status = HttpStatus.EXCEPTION
            response_json_data = self.__create_dict_containing_message(TIMEOUT_DETECTED_MESSAGE.format(url))
        return api_call_status, response_json_data
