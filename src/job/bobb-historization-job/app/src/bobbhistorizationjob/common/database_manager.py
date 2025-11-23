from pymongo import MongoClient
from pymongo.errors import ConnectionFailure
import logging
import os
from bobbhistorizationjob.common.common_constants import MONGODB_URL_ENV_VAR

logger = logging.getLogger()
logger.setLevel("INFO")
MONGODB_URL = os.getenv(MONGODB_URL_ENV_VAR,
                        "mongodb://localhost:27017/bobb")


class DatabaseManager:
    _instance = None

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super(DatabaseManager, cls).__new__(cls)
            cls._instance.client = None
            cls._instance.db = None
            cls._instance.connect()
            cls._instance.session = None
        return cls._instance

    def connect(self):
        if self.client is None:
            try:
                self._instance.client = MongoClient(MONGODB_URL)
                self._instance.db = self._instance.client.get_default_database()
                logging.info('Connection to the database established.')
            except ConnectionFailure:
                logging.error('Failed to establish a connection to the database.')

    def get_db(self):
        return self._instance.db

    def close_connection(self):
        if self._instance.session:
            self._instance.session.end_session()
        if self._instance.client:
            self._instance.client.close()
            logging.info('Closing database connection.')
