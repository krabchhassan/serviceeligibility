import logging
from redis.sentinel import Sentinel

from bobbimportjob.common.common_constants import REDIS_AGENT_NODE_0, REDIS_AGENT_NODE_1, REDIS_AGENT_NODE_2, \
    REDIS_ADMIN_PASSWORD, REDIS_ADMIN_USER, REDIS_CRT_PATH, REDIS_PORT, REDIS_MASTER, REDIS_PASSWORD, REDIS_USER
from bobbimportjob.configuration.redis_configuration import RedisConfiguration
from bobbimportjob.logging import init_logging

REDIS_USERNAME_KEY = "username"
REDIS_PASSWORD_KEY = "password"
REDIS_SSL_KEY = "ssl"
REDIS_SSL_CA_CERT_KEY = "ssl_ca_certs"
REDIS_SSL_CERT_REQS_KEY = "ssl_cert_reqs"
REDIS_SSL_CERTFILE_KEY = "ssl_certfile"
REDIS_SSL_KEYFILE_KEY = "ssl_keyfile"
REDIS_SSL_CHECK_HOSTNAME_KEY = "ssl_check_hostname"
REDIS_SOCKET_TIMEOUT_KEY = "socket_connect_timeout"

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class RedisManager:
    """
    A class for managing interactions with a Redis database.

    This class provides methods for storing, retrieving, and deleting data in a Redis database.
    It offers functionalities to store single or multiple key-value pairs, retrieve entries by keys,
    and delete entries from the Redis cache.
    """

    _instance = None

    def __init__(self):
        self.__redis_config = RedisConfiguration.get_instance().get_config()

        __sentinel = Sentinel([
            (self.__redis_config.get(REDIS_AGENT_NODE_0), int(self.__redis_config.get(REDIS_PORT))),
            (self.__redis_config.get(REDIS_AGENT_NODE_1), int(self.__redis_config.get(REDIS_PORT))),
            (self.__redis_config.get(REDIS_AGENT_NODE_2), int(self.__redis_config.get(REDIS_PORT))),
        ], sentinel_kwargs={
            REDIS_PASSWORD_KEY: self.__redis_config.get(REDIS_ADMIN_PASSWORD),
            REDIS_USERNAME_KEY: self.__redis_config.get(REDIS_ADMIN_USER),
            REDIS_SSL_KEY: True,
            REDIS_SSL_CA_CERT_KEY: self.__redis_config.get(REDIS_CRT_PATH),
            REDIS_SSL_CERT_REQS_KEY: None,
            REDIS_SSL_CERTFILE_KEY: None,
            REDIS_SSL_KEYFILE_KEY: None,
            REDIS_SSL_CHECK_HOSTNAME_KEY: False,
            REDIS_SOCKET_TIMEOUT_KEY: 0.5
        })

        # Connect to the master
        self.master_connection = __sentinel.master_for(
            self.__redis_config.get(REDIS_MASTER),
            password=self.__redis_config.get(REDIS_PASSWORD),
            username=self.__redis_config.get(REDIS_USER),
            ssl=True,
            ssl_ca_certs=self.__redis_config.get(REDIS_CRT_PATH)
        )

    @classmethod
    def get_instance(cls):
        """
        Returns the singleton instance of the RedisManager class.
        If the instance does not exist, it is created.
        """
        if cls._instance is None:
            cls._instance = RedisManager()
        return cls._instance

    def delete_all_entries(self, pattern: str):
        """
        Deletes multiple keys and their associated values from the Redis cache.
        :param pattern: A pattern to match keys.
        """
        cursor = '0'
        while cursor != 0:
            cursor, keys = self.master_connection.scan(cursor=cursor, match=pattern, count=100000)
            if keys:
                self.master_connection.delete(*keys)
