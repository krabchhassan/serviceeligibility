from opensearchpy import OpenSearch
import os
import logging
from bobbhistorizationjob.common.common_constants import (
    ELASTICSEARCH_URL_VAR_ENV,
    ELASTICSEARCH_LOGIN_VAR_ENV,
    ELASTICSEARCH_PASSWORD_VAR_ENV,
)

from bobbhistorizationjob.logging import init_logging
LOGGER_NAME = "bobbhistorization"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class OpenSearchConnection:
    _instance = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
            cls._instance.cluster_url = os.environ.get(ELASTICSEARCH_URL_VAR_ENV)
        return cls._instance

    def __init__(self):
        if not hasattr(self, 'client'):
            self.client = OpenSearch(
                [self.cluster_url],
                http_auth=(
                    os.environ.get(ELASTICSEARCH_LOGIN_VAR_ENV),
                    os.environ.get(ELASTICSEARCH_PASSWORD_VAR_ENV),
                ),
            )
