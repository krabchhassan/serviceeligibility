import os
import sys
import logging
from bobbhistorizationjob.common.common_constants import (
    COMMON_INDEX_BASENAME_VAR_ENV,
    MONGODB_URL_ENV_VAR,
    ELASTICSEARCH_URL_VAR_ENV,
    ELASTICSEARCH_LOGIN_VAR_ENV,
    ELASTICSEARCH_PASSWORD_VAR_ENV,
)
from bobbhistorizationjob.logging import init_logging

REQUIRED_ENV_VARS = [
    COMMON_INDEX_BASENAME_VAR_ENV,
    MONGODB_URL_ENV_VAR,
    ELASTICSEARCH_URL_VAR_ENV,
    ELASTICSEARCH_LOGIN_VAR_ENV,
    ELASTICSEARCH_PASSWORD_VAR_ENV,
]

LOGGER_NAME = "bobbhistorization"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class EnvironmentValidator:
    @classmethod
    def validate_environment(cls):
        missing_vars = [var for var in REQUIRED_ENV_VARS if not os.getenv(var)]

        if missing_vars:
            logger.error(f"Variables d'environnement manquantes : {', '.join(missing_vars)}")
            sys.exit(1)
