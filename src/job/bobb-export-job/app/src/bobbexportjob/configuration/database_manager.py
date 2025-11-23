import os
import logging
from pymongo import MongoClient
from pymongo.errors import ConnectionFailure

from bobbexportjob.common.common_constants import MONGODB_URL_KEY
from bobbexportjob.logging import init_logging

LOGGER_NAME = "bobbexport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

MONGODB_URL = os.getenv(
    MONGODB_URL_KEY,
    "mongodb://serviceeligibility-131e9cdfbadf14b6a7661c9b1d411b5b-dev-evol2:JSFirvPS3N97@detcisedt0110.hosting.cegedim.cloud:27017/bdd-next-serviceeligibility-core-api-dev-evol2"
)


class DatabaseManager:
    _instance = None

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super(DatabaseManager, cls).__new__(cls)
            cls._instance.client = None
            cls._instance.db = None
            cls._instance.connect()
        return cls._instance

    def connect(self):
        if self._instance.client is None:
            try:
                    self._instance.client = MongoClient(MONGODB_URL)
                    self._instance.db = self._instance.client.get_default_database()
                    logger.info('Connected to MongoDB successfully ...')
            except ConnectionFailure:
                logger.error('Failed to connect to MongoDB. Check the connection settings.')

    def get_db(self):
        return self._instance.db

    def close_connection(self):
        if self._instance.client:
            self._instance.client.close()
            logger.info('Closing connection to MongoDB !!!')
