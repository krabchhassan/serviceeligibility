from pymongo import MongoClient
from pymongo.errors import ConnectionFailure
import logging
import os
from bobbimportjob.common.common_constants import MONGODB_URL_ENV_VAR, TRANSACTION_FAILED
from bobbimportjob.logging import init_logging
from contextlib import contextmanager

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

MONGODB_URL = os.getenv(MONGODB_URL_ENV_VAR,
                        "mongodb://bdd-next-serviceeligibility-core-api-dev-mono:elvNDc8NszKC@detcisedt0110.hosting.cegedim.cloud:27017/bdd-next-serviceeligibility-core-api-dev")


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
                self._instance.client = MongoClient(
                    MONGODB_URL,
                    socketTimeoutMS=1800000,
                    connectTimeoutMS=1800000
                )
                self._instance.db = self._instance.client.get_default_database()
                logging.info('Connection to the database established.')
            except ConnectionFailure:
                logging.error('Failed to establish a connection to the database.')

    def get_db(self):
        return self._instance.db

    @contextmanager
    def start_transaction_context(self):
        if not self.client:
            raise RuntimeError(TRANSACTION_FAILED)

        session = self.client.start_session()
        session.start_transaction()
        try:
            yield session
            session.commit_transaction()
            logger.debug('Transaction committed successfully.')
        except Exception as e:
            session.abort_transaction()
            logger.error(f'Transaction aborted due to error: {str(e)}')
            raise
        finally:
            session.end_session()

    def abort_transaction(self):
        if self._instance.session:
            self._instance.session.abort_transaction()
            logging.info('Database transaction aborted')
        else:
            logging.info(TRANSACTION_FAILED)

    def close_connection(self):
        if self._instance.session:
            self._instance.session.end_session()
        if self._instance.client:
            self._instance.client.close()
            logging.info('Closing database connection.')
