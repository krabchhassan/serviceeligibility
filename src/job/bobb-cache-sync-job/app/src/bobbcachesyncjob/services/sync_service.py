import logging
import os

from bobbcachesyncjob.logging import init_logging
from bobbcachesyncjob.repositories.contract_element_repository import ContractElementRepository
from .redis_cache_service import RedisCacheService

LOGGER_NAME = "bobbcachesync"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

CHUNK_SIZE = int(os.environ.get("CHUNK_SIZE", "1000"))


class SyncService:
    def __init__(self):
        self.redis_cache_service = RedisCacheService()

    def synchronize_cache(self):
        collection_cursor = ContractElementRepository.get_active_contracts_elements()
        chunk = []
        nb_documents_added = 0
        for document in collection_cursor:
            chunk.append(document)
            if len(chunk) >= CHUNK_SIZE:
                self.redis_cache_service.insert_chunk_in_redis_cache(chunk)
                nb_documents_added += len(chunk)
                chunk = []

        if chunk:
            self.redis_cache_service.insert_chunk_in_redis_cache(chunk)
            nb_documents_added += len(chunk)

        return nb_documents_added

    @classmethod
    def get_contracts_elements(cls):
        return ContractElementRepository.get_active_contracts_elements()

    def clean_cache(self):
        return self.redis_cache_service.delete_all_contract_elements_in_redis_cache()
