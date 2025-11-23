import json
import logging
import os
from datetime import datetime

from bobbcachesyncjob.common.common_constants import PRODUCT_ELEMENTS, CODE_INSURER, CODE_CONTRACT_ELEMENT, \
    REDIS_DATABASE_BOBB, CACHE, DATE_FORMAT
from bobbcachesyncjob.common.redis_manager import RedisManager
from bobbcachesyncjob.logging import init_logging

LOGGER_NAME = "bobbcachesync"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class RedisCacheService:
    def __init__(self):
        self.redis_database = os.getenv(REDIS_DATABASE_BOBB)
        self.redis_manager = RedisManager()

    def generate_cache_key(self, data_item):
        return f"{self.redis_database}::{CACHE}:{data_item[CODE_INSURER]}:{data_item[CODE_CONTRACT_ELEMENT]}"

    @staticmethod
    def serialize_data(data_item):
        def convert_datetime_fields(data):
            for key, value in data.items():
                if isinstance(value, datetime):
                    data[key] = value.strftime(DATE_FORMAT)
            return data

        def process_product_elements(product_elements):
            for product_element in product_elements:
                product_element = convert_datetime_fields(product_element)
            return product_elements

        data_item[PRODUCT_ELEMENTS] = process_product_elements(data_item[PRODUCT_ELEMENTS])
        data_item = convert_datetime_fields(data_item)
        return json.dumps(data_item, default=str)

    def insert_chunk_in_redis_cache(self, documents):
        try:
            batch_size = len(documents)
            pipe = self.redis_manager.master_connection.pipeline(transaction=False)
            for document in documents:
                new_cache_key = self.generate_cache_key(document)
                serialized_data = self.serialize_data(document)
                pipe.set(new_cache_key, serialized_data)
                logger.debug(f"Inserted data into Redis cache: Key - {new_cache_key}")
            pipe.execute()
            logger.info(f"Batch insertion into Redis cache completed. Batch size: {batch_size}")
        except Exception as e:
            logger.error(f"Failed to insert batch of contract elements into Redis cache: {str(e)}")

    def delete_all_contract_elements_in_redis_cache(self):
        try:
            nb_deleted_values = self.redis_manager.get_instance().delete_all_entries(pattern=self.get_pattern())
            logger.info("Redis cache successfully cleared")
            return nb_deleted_values

        except Exception as e:
            logger.error("Error occurred while updating Redis cache: %s", str(e))
            raise

    def get_pattern(self):
        return f"{self.redis_database}::{CACHE}:*"