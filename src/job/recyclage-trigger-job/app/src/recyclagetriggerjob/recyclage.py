#!/usr/bin/env python

# --------------------
# IMPORTS
# --------------------
import time
import sys
import os
import argparse
from os import environ
from typing import Optional, Tuple
from commonomuhelper.omuhelper import produce_output_parameters
from commonpersonalworkdir.common_personal_workdir import build_personal_workdir_path
from otpcommon.bdds.exception.bdds_exception import BddsException
import logging

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s - %(levelname)s - %(message)s",
    handlers=[logging.StreamHandler(sys.stdout)],
)

# --------------------
# CONSTANTS
# --------------------
AUTHORIZATION_HEADER_KEY = "authorization"
GRANT_TYPE_VALUE = "client_credentials"
GRANT_TYPE_KEY = "grant_type"
CLIENT_ID_KEY = "client_id"
CLIENT_SECRET_KEY = "client_secret"
CONTENT_TYPE_KEY = "Content-Type"
CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded"
USER_ID_ENV_KEY = 'USER_ID'
BEARER_VALUE = "Bearer "
ACCESS_TOKEN = "access_token"
SYSTEM_ERROR_CODE = 1
DEFAULT_TIMEOUT_VALUE = 15

PERSONAL_WORKDIR = "personal-workdir"
API_URL = "http://next-serviceeligibility-core-api:8080" #NOSONAR

# --------------------
# ARGUMENTS
# --------------------
client_id = environ.get('CLIENT_ID')
client_secret = environ.get("CLIENT_SECRET")
keycloak_url = environ.get("keycloak_url")
keycloak_realm = environ.get("keycloak_realm")

# --------------------
# KEYCLOAK
# --------------------
"""
Get the access token and wrap it in an authorization header dictionary
:return: authorization header
:rtype: dict
"""


def get_http_authorization_header(token_uri: str, client_id: str, client_secret: str) -> dict:
    access_token = get_access_token(token_uri, client_id, client_secret)
    return get_auth_header(access_token)


"""
Get the authorization endpoint and retrieve the access token
:return: access token
:rtype: str
"""
def get_access_token(token_uri: str, client_id: str, client_secret: str) -> str:
    import json
    import requests

    verbose_print('-- get access token from : {url: ' + token_uri + ', client_id: ' + client_id + '}')
    payload = {
        GRANT_TYPE_KEY: GRANT_TYPE_VALUE,
        CLIENT_ID_KEY: client_id,
        CLIENT_SECRET_KEY: client_secret
    }
    header = {CONTENT_TYPE_KEY: CONTENT_TYPE_VALUE}

    response = requests.post(url=token_uri, headers=header, data=payload)

    status = response.status_code
    logging.info(f"response : {response.content}")
    logging.info(f"status : {status}")
    content = json.loads(response.content)

    if status != 200:
        logging.info(f"error message : {content.error_description}")
        return ""

    access_token = BEARER_VALUE + content.get(ACCESS_TOKEN)

    return access_token


"""
Wrap access token in an authorization header dictionary
:return: authorization header
:rtype: dict
"""
def get_auth_header(access_token: Optional[str]) -> dict:
    if access_token == "":
        return {}  # early exit

    return {AUTHORIZATION_HEADER_KEY: access_token}

# --------------------
# HTTP FUNCTIONS
# --------------------
"""
Generic PUT method
:return: status of the request, and json response as dict
:rtype: Tuple : HttpStatus, dict
"""
def put(endpoint: str, headers=None, timeout=DEFAULT_TIMEOUT_VALUE) -> Tuple:
    if headers is None:
        headers = {}
    import requests

    verbose_print("-- posting on url : ", endpoint)

    verbose_print(requests.put(endpoint, headers=headers, timeout=timeout))
# --------------------
# FUNCTIONS
# --------------------
"""
Logs a line if the option --verbose is set
"""

def verbose_print(*messages):
    total_message = ""
    for message in messages:
        total_message += str(message)

    logging.info(total_message)

"""
Call to /v1/declencheursCarteTP/{id}/statut/{statut} to update the trigger
"""

def put_recycle(access_token, id_trigger):
    full_url = API_URL + "/v1/declencheursCarteTP/" + id_trigger + "/statut/ToProcess"
    put(full_url, access_token)

def os_list_is_not_empty(os_list: list) -> bool:
    os_list_not_empty = False
    for os in os_list:
        if len(os) > 0 and os != '':
            os_list_not_empty = True
    return os_list_not_empty

def get_personal_workdir_path():
    """
    Get personal workdir path
    :return: personal workdir path
    """
    return PERSONAL_WORKDIR

def get_mapping_filename(personal_workdir_path):
    from pathlib import Path
    personal_workdir_as_path = Path(personal_workdir_path)
    file_name = None
    for file_path in personal_workdir_as_path.glob("mapping_id_sp*"):
        if (file_name is not None):
            raise BddsException("personal workdir must contain only one mapping file")
        file_name = file_path.name
    return file_name


def generate_crex(total):
    produce_output_parameters({
        "number_triggers": total
    })
"""
Main process
"""
def main():
    logging.info("debut traitement")
    complete_keycloak_url = keycloak_url + "/realms/" + keycloak_realm + "/protocol/openid-connect/token"
    user_id = os.getenv(USER_ID_ENV_KEY)
    if user_id is None:
        logging.error("USER_ID label was not provided by the launcher")
        sys.exit(SYSTEM_ERROR_CODE)
    parser = argparse.ArgumentParser()
    parser.add_argument("--file_path", type=str, help="Path entry file", required=True)
    job_args = parser.parse_args()
    file_path = job_args.file_path
    # Read each line of the input file
    nombre_trigger = 1
    logging.info(f"file_path : {file_path}")
    csv_stream = open(f"{build_personal_workdir_path(user_id)}/{file_path}", "r")
    # Read each line of the input file
    for counter, line in enumerate(csv_stream, start=1):
        verbose_print("\n")
        # Get token every 50 lines
        if ((counter - 1) % 50) == 0:
            access_token = get_http_authorization_header(complete_keycloak_url, client_id, client_secret)
            verbose_print(access_token)
            if access_token is None:
                exit("error while calling Keycloak")

        # Split the line to extract insuredNumber and contractNumber
        id_trigger = line.strip("\n\r").strip(" ")
        put_recycle(access_token, id_trigger)
        time.sleep(0.5)
        nombre_trigger += 1
    generate_crex(nombre_trigger)
    logging.info("fin traitement")
    sys.exit()
