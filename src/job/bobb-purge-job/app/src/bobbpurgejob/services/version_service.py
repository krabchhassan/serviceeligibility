from typing import Dict, List, Any, Optional

from bobbpurgejob.repositories.version_repository import VersionRepository
from bobbpurgejob.services.event_publisher_service import EventPublisher
from bobbpurgejob.common.common_constants import STATUS, STATUS_ACTIVE


class VersionService:

    def __init__(self, event_publisher: Optional[EventPublisher] = None):
        self.version_repo = VersionRepository()
        self.event_publisher = event_publisher or EventPublisher()
        if not hasattr(self.event_publisher, 'context_initialized') or not self.event_publisher.context_initialized:
            try:
                self.event_publisher.init_context()
            except Exception as e:
                raise RuntimeError(f"Event publisher context initialization failed: {str(e)}")
            self.event_publisher.context_initialized = True

    def get_version_statistics(self) -> Dict[str, int]:
        active_versions = list(self.version_repo.collection.find({
            "status": STATUS_ACTIVE,
            "purgeDate": None
        }))

        total_versions = self.version_repo.collection.count_documents({})

        updated_versions = self.version_repo.collection.count_documents({
            "purgeDate": {"$ne": None}
        })
        
        return {
            "total_versions": total_versions,
            "active_versions": len(active_versions),
            "updated_versions": updated_versions
        }
    
    def get_versions_to_purge(self, keep_count: int = 2) -> List[Dict]:
        versions_to_keep = self.version_repo.get_last_keep_count_versions(keep_count)
        return self.version_repo.get_versions_to_purge(versions_to_keep)

    def get_versions_to_purge_optimized(self, keep_count: int = 2) -> List[Dict]:
        versions_to_keep = self.version_repo.get_last_keep_count_versions(keep_count)
        return self.version_repo.get_versions_to_purge_optimized(versions_to_keep)
    
    def mark_versions_for_purge(self, versions_to_purge: List[Dict]) -> int:
        if not versions_to_purge:
            return 0
            
        return self.version_repo.mark_versions_for_purge(
            versions_to_purge=versions_to_purge,
            purged_by=self.event_publisher.omu_id
        )

    def mark_versions_for_purge_optimized(self, versions_to_purge: List[Dict]) -> int:
        if not versions_to_purge:
            return 0

        return self.version_repo.mark_versions_for_purge_optimized(
            versions_to_purge=versions_to_purge,
            purged_by=self.event_publisher.omu_id
        )
    
    @staticmethod
    def get_version_ids(versions: List[Dict]) -> List[Any]:
        return [v["_id"] for v in versions]
    
    def get_active_versions_count(self) -> int:
        return len(list(self.version_repo.collection.find({
            STATUS: STATUS_ACTIVE,
            "purgeDate": None
        })))
