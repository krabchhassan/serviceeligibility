import os

from bobbcachesyncjob.common.common_constants import REDIS_MASTER, REDIS_AGENT_NODE_0, REDIS_AGENT_NODE_1, \
    REDIS_AGENT_NODE_2, REDIS_PORT, REDIS_ADMIN_USER, REDIS_ADMIN_PASSWORD, REDIS_CRT_PATH, REDIS_USER, REDIS_PASSWORD
from bobbcachesyncjob.exceptions.redis_configuration_exception import RedisConfigurationException


class RedisConfiguration:
    """
    Représente la configuration nécessaire pour démarrer un client REDIS
    """
    _instance = None

    def __init__(self):
        """
        Constructeur, instancie un client REDIS
        """
        self.config = {}
        for key in [REDIS_MASTER, REDIS_AGENT_NODE_0, REDIS_AGENT_NODE_1, REDIS_AGENT_NODE_2, REDIS_PORT,
                    REDIS_ADMIN_USER, REDIS_ADMIN_PASSWORD, REDIS_USER, REDIS_PASSWORD, REDIS_CRT_PATH]:
            value = os.environ.get(key, None)
            if value is not None:
                self.config[key] = value
            else:
                raise RedisConfigurationException(missing_configuration=key)

    @classmethod
    def get_instance(cls):
        """
        Renvoie l'instance de la configuration REDIS, en créé une si elle n'existe pas
        :return: l'instance de la configuration REDIS
        """
        if cls._instance is None:
            cls._instance = cls()
        return cls._instance

    def get_config(self):
        """
        Renvoie le dictionnaire de configuration REDIS
        :return: le dictionnaire de configuration REDIS
        """
        return self.config


