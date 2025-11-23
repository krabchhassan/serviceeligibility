from typing import List, Dict, Optional, Any
from pymongo import DESCENDING
from datetime import datetime, timezone

from bobbpurgejob.common.database_manager import DatabaseManager
from bobbpurgejob.common.common_constants import COLLECTION_VERSIONS, STATUS_ACTIVE

class VersionRepository:
    def __init__(self):
        self.db_manager = DatabaseManager()
        self.db = self.db_manager.get_db()
        self.collection = self.db[COLLECTION_VERSIONS]

    def get_last_keep_count_versions(self, keep_count: int = 2) -> List[Dict]:
        return list(self.collection.find(
            {
                "purgeDate": None
            }
        ).sort("creationDate", DESCENDING).limit(keep_count))

    def get_versions_to_purge(self, last_keep_count_versions: List[Dict]) -> List[Dict]:
        if not last_keep_count_versions:
            return []

        last_keep_count_ids = [version["_id"] for version in last_keep_count_versions]

        return list(self.collection.find(
            {
                "purgeDate": None,
                "_id": {"$nin": last_keep_count_ids}
            }
        ))

    def mark_versions_for_purge(self, versions_to_purge: List[Dict], purged_by: str) -> int:
        if not versions_to_purge:
            return 0

        version_ids = [version["_id"] for version in versions_to_purge]

        result = self.collection.update_many(
            {
                "_id": {"$in": version_ids}
            },
            {
                "$set": {
                    "purgeDate": datetime.now(timezone.utc),
                    "purgedBy": purged_by
                }
            }
        )

        return result.modified_count
        
    def get_cutoff_version(self, keep_count: int) -> Optional[Dict]:
        return self.collection.find_one(
            {"status": STATUS_ACTIVE, "purgeDate": None},
            sort=[("creationDate", -1)],
            skip=keep_count - 1
        )
        
    def get_versions_to_purge_cursor(self, cutoff_date: datetime):
        return self.collection.find({
            "status": STATUS_ACTIVE,
            "purgeDate": None,
            "creationDate": {"$lt": cutoff_date}
        }).batch_size(1000)
        
    def mark_versions_batch(self, version_ids: List[Any], purged_by: str) -> int:
        if not version_ids:
            return 0
            
        result = self.collection.update_many(
            {"_id": {"$in": version_ids}},
            {
                "$set": {
                    "purgeDate": datetime.now(timezone.utc),
                    "purgedBy": purged_by
                }
            }
        )
        return result.modified_count

    def get_versions_to_purge_optimized(self, last_keep_count_versions: List[Dict]) -> List[Dict]:
        if not last_keep_count_versions:
            return []

        last_keep_count_ids = {version["_id"] for version in last_keep_count_versions}

        cursor = self.collection.find(
            {
                "purgeDate": None
            },
           {"_id": 1}
        ).batch_size(5000)

        versions_to_purge = []
        for doc in cursor:
            if doc["_id"] not in last_keep_count_ids:
                versions_to_purge.append(doc)

        return versions_to_purge

    def mark_versions_for_purge_optimized(self, versions_to_purge: List[Dict], purged_by: str) -> int:
        if not versions_to_purge:
            return 0

        total_updated = 0
        batch_size = 1000  # Réduire la taille du batch pour les updates

        # Traitement par lots pour les updates
        for i in range(0, len(versions_to_purge), batch_size):
            batch = versions_to_purge[i:i + batch_size]
            version_ids = [version["_id"] for version in batch]

            result = self.collection.update_many(
                {"_id": {"$in": version_ids}},
                {
                    "$set": {
                        "purgeDate": datetime.now(timezone.utc),
                        "purgedBy": purged_by
                    }
                }
            )
            total_updated += result.modified_count

        return total_updated