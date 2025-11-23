from commonomuhelper.omuhelper import produce_output_parameters

from bobbpurgejob.common.common_constants import (PURGED_CONTRACT_ELEMENTS_COUNT, UPDATED_VERSIONS_COUNT, PRE_PURGE, POST_PURGE)

class Report:
    _instance = None

    def __init__(self):
        self.post_purge_statistics = {}
        self.pre_purge_statistics = {}
        self.updated_versions = 0
        self.purged_contract_elements = 0

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super(Report, cls).__new__(cls)
            cls._instance.purged_contract_elements = 0
            cls._instance.updated_versions = 0
            cls._instance.pre_purge_statistics = {}
            cls._instance.post_purge_statistics = {}
        return cls._instance

    def generate_crex(self):
        output_parameters = {
            str(PURGED_CONTRACT_ELEMENTS_COUNT): str(self.purged_contract_elements),
            str(UPDATED_VERSIONS_COUNT): str(self.updated_versions),
            str(PRE_PURGE): str(self.pre_purge_statistics) if self.pre_purge_statistics else '{}',
            str(POST_PURGE): str(self.post_purge_statistics) if self.post_purge_statistics else '{}'
        }
        produce_output_parameters(output_parameters)


    def generate_parameter_crex(self, deleted_count, updated_count, pre_purge_stats, post_purge_stats):
        if not isinstance(pre_purge_stats, dict) or not isinstance(post_purge_stats, dict):
            raise ValueError("pre_purge_stats and post_purge_stats must be dictionaries")

        if deleted_count < 0 or updated_count < 0:
            raise ValueError("Counts cannot be negative")

        self.purged_contract_elements = deleted_count
        self.updated_versions = updated_count
        self.pre_purge_statistics = {
            "total_contract_elements": pre_purge_stats.get('total_contract_elements', 0),
            "total_versions": pre_purge_stats.get('total_versions', 0),
            "active_versions": pre_purge_stats.get('active_versions', 0),
            "updated_versions": pre_purge_stats.get('updated_versions', 0)
        }

        self.post_purge_statistics = {
            "total_contract_elements": post_purge_stats.get('total_contract_elements', 0),
            "total_versions": post_purge_stats.get('total_versions', 0),
            "active_versions": post_purge_stats.get('active_versions', 0),
            "updated_versions": post_purge_stats.get('updated_versions', 0)
        }