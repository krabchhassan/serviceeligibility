import os
import logging

from bobbimportjob.common.common_constants import REDIS_DATABASE_BOBB, CACHE
from bobbimportjob.common.redis_manager import RedisManager
from bobbimportjob.logging import init_logging

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class RedisCacheService:
    def __init__(self):
        self.redis_database = os.getenv(REDIS_DATABASE_BOBB)
        self.redis_manager = RedisManager()

    def delete_all_contract_elements_in_redis_cache(self):
        try:
            self.redis_manager.get_instance().delete_all_entries(pattern=self.get_pattern())
            logger.info("Redis cache successfully cleared")

        except Exception as e:
            logger.error("Error occurred while updating Redis cache: %s", str(e))
            raise

    def get_pattern(self):
        return f"{self.redis_database}::{CACHE}:*"
