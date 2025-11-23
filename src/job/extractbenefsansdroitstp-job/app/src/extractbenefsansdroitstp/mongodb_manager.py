from beyondpythonframework.config.logging import get_beyond_logger

logger = get_beyond_logger()


class MongoDBManager:
    _instance = None

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super(MongoDBManager, cls).__new__(cls)
            cls._instance.client = None
            cls._instance.db = None
            cls._instance.connect()
            cls._instance.session = None
        return cls._instance

    def connect(self):
        from pymongo import ReadPreference
        from pymongo.errors import ConnectionFailure
        from beyondpythonframework.database import get_mongodb_database_client

        if self.client is None:
            try:
                self._instance.client = get_mongodb_database_client()
                self._instance.db = self._instance.client.get_default_database(
                    read_preference=ReadPreference.SECONDARY_PREFERRED)
                logger.info('Connection to the database established.')
            except ConnectionFailure:
                logger.error('Failed to establish a connection to the database.')

    def get_db(self):
        return self._instance.db

    def close_connection(self):
        if self._instance.session:
            self._instance.session.end_session()
        if self._instance.client:
            self._instance.client.close()
            logger.info('Closing database connection.')
