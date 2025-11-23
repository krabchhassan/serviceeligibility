from typing import Dict, List, Any, Optional

from bobbpurgejob.repositories.contract_element_repository import ContractElementRepository
from bobbpurgejob.services.event_publisher_service import EventPublisher
from bobbpurgejob.common.report import Report


class ContractElementService:

    def __init__(self, event_publisher: Optional[EventPublisher] = None):
        self.contract_element_repo = ContractElementRepository()
        self.event_publisher = event_publisher or EventPublisher()
        if not hasattr(self.event_publisher, 'context_initialized') or not self.event_publisher.context_initialized:
            try:
                self.event_publisher.init_context()
            except Exception as e:
                raise RuntimeError(f"Event publisher context initialization failed: {str(e)}")
            self.event_publisher.context_initialized = True

    def get_purge_statistics(self) -> Dict[str, int]:
        total_contract_elements = self.contract_element_repo.collection.count_documents({})
        return {
            "total_contract_elements": total_contract_elements
        }

    def purge_contract_elements(self, version_ids: List[Any]) -> int:
        if not version_ids:
            return 0
            
        return self.contract_element_repo.delete_contract_elements_by_versions_optimized(version_ids)
    
    def generate_purge_report(self, deleted_count: int , updated_count: int, pre_purge_stats: Dict,
                            post_purge_stats: Dict) -> Report:
        report = Report()

        report.generate_parameter_crex(
            deleted_count=deleted_count,
            updated_count=updated_count,
            pre_purge_stats=pre_purge_stats,
            post_purge_stats=post_purge_stats
        )
        report.generate_crex()
        return report

    def delete_contract_elements_by_versions_optimized(self, version_ids: List[Any]	):
        return self.contract_element_repo.delete_contract_elements_by_versions_optimized(version_ids)