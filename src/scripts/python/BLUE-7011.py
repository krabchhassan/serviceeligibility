#!/usr/bin/env python

###########
# IMPORTS #
###########
from typing import Optional, Tuple

##############
# ATTRIBUTES #
##############

#############
# CONSTANTS #
#############
AUTHORIZATION_HEADER_KEY = "authorization"
GRANT_TYPE_VALUE = "client_credentials"
GRANT_TYPE_KEY = "grant_type"
USERNAME_KEY = "username"
PASSWORD_KEY = "password"
CLIENT_ID_KEY = "client_id"
CONTENT_TYPE_KEY = "Content-Type"
CONTENT_TYPE_VALUE = "application/x-www-form-urlencoded"
USER_ID_ENV_KEY = 'USER_ID'
BEARER_VALUE = "Bearer "
ACCESS_TOKEN = "access_token"
SYSTEM_ERROR_CODE = 1
DEFAULT_TIMEOUT_VALUE = 15

# info pour accéder à la consolidation, attention l'utilisateur doit avoir le droit SE_P_SUPPORT_BDDS
# changer les infos suivantes pour l'utilisateur et l'environnement :
API_URL = "https://url/"
user = "email"
password = "password"
keycloak_url = "https://idp.dev.beyond.cegedim.cloud/auth/"
keycloak_realm = "htp"

#############
# FUNCTIONS #
#############

# --------------------
# KEYCLOAK
# --------------------
"""
Get the access token and wrap it in an authorization header dictionary
:return: authorization header
:rtype: dict
"""

def get_http_authorization_header(token_uri: str) -> dict:
    access_token = get_access_token(token_uri)
    return get_auth_header(access_token)

"""
Get the authorization endpoint and retrieve the access token
:return: access token
:rtype: str
"""
def get_access_token(token_uri: str) -> str:
    import json
    import requests

    payload = {
        GRANT_TYPE_KEY: "password",
        USERNAME_KEY: user,
        PASSWORD_KEY: password,
        CLIENT_ID_KEY: "postman-es15"
    }
    header = {CONTENT_TYPE_KEY: CONTENT_TYPE_VALUE}

    response = requests.post(url=token_uri, headers=header, data=payload)

    status = response.status_code
    content = json.loads(response.content)

    if status != 200:
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

def post(endpoint: str, headers=None, timeout=DEFAULT_TIMEOUT_VALUE) -> Tuple:
    if headers is None:
        headers = {}
    import requests

    requests.post(endpoint, headers=headers, timeout=timeout)

def consolidate(access_token, id_declarant, numero_contrat, numero_adherent):
    full_url = API_URL + "/v1/consolidation/" + id_declarant + "/" + numero_contrat + "/" + numero_adherent
    print(access_token)
    post(full_url, access_token)

def process():
    complete_keycloak_url = keycloak_url + "/realms/" + keycloak_realm + "/protocol/openid-connect/token"
    access_token = None
    csv_stream = open("c:/tmp/contrats.csv", "r") # fichier à créer en local
    # Read each line of the input file
    for counter, line in enumerate(csv_stream, start=1):
        info = line.split(",")
        numero_contrat = info[1]
        numero_adherent = info[2]
        id_declarant = info[0]
        if ((counter - 1) % 50) == 0:
            access_token = get_http_authorization_header(complete_keycloak_url)
            if access_token is None:
                exit("error while calling Keycloak")
        consolidate(access_token, id_declarant, numero_contrat, numero_adherent)
##########
# SCRIPT #
##########
process()
