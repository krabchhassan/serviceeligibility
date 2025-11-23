import logging
from bson.binary import Binary, UuidRepresentation
import uuid
from typing import List, Any
from pymongo import DeleteMany

from bobbpurgejob.common.common_constants import (LOGGER_NAME, COLLECTION_CONTRACT_ELEMENT, VERSION_ID_FIELD, BATCH_LIMIT)
from bobbpurgejob.common.database_manager import DatabaseManager
from bobbpurgejob.logging import init_logging


init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class ContractElementRepository:

    def __init__(self):
        self.db_manager = DatabaseManager()
        self.db = self.db_manager.get_db()
        self.collection = self.db[COLLECTION_CONTRACT_ELEMENT]
        self.optimized_index_created = False
        self.BATCH_LIMIT = BATCH_LIMIT

    @staticmethod
    def _convert_to_binary_uuid(doc_id: Any) -> Any:
        if isinstance(doc_id, uuid.UUID):
            return Binary(doc_id.bytes, UuidRepresentation.STANDARD)
        return doc_id

    def delete_contract_elements_by_versions_optimized(self, version_ids: List[Any]) -> int:
        if not version_ids:
            return 0

        total_deleted = 0
        binary_version_ids = [self._convert_to_binary_uuid(vid) for vid in version_ids]

        batch_size = self._optimize_batch_size(len(binary_version_ids))

        for i in range(0, len(binary_version_ids), batch_size):
            batch_ids = binary_version_ids[i:i + batch_size]
            try:
                bulk_ops = [
                    DeleteMany({VERSION_ID_FIELD: {"$in": batch_ids}})
                    for __ in batch_ids
                ]

                result = self.collection.bulk_write(bulk_ops, ordered=False)
                total_deleted += result.deleted_count
                logger.info(f"result.deleted_count for bulk_write: {str(result.deleted_count)}")
            except Exception as e:
                logger.error(f"Bulk delete failure at batch {i//batch_size + 1}: {str(e)}")
                try:
                    result = self.collection.delete_many({
                        VERSION_ID_FIELD: {"$in": batch_ids}
                    })
                    total_deleted += result.deleted_count
                    logger.info(f"result.deleted_count for delete_many: {str(result.deleted_count)}")
                except Exception as fallback_error:
                    logger.error(f"Fallback delete also failed: {str(fallback_error)}")
                    continue

        return total_deleted


    def _optimize_batch_size(self, total_ids):
        if total_ids <= 1000:
            return 100
        elif total_ids <= 10000:
            return 500
        elif total_ids <= 100000:
            return 1000
        else:
            return 5000