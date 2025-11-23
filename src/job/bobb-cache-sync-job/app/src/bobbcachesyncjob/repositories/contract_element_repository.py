import logging
from bobbcachesyncjob.common.database_manager import DatabaseManager
from bobbcachesyncjob.common.common_constants import STATUS, ACTIVE

from bobbcachesyncjob.logging import init_logging
LOGGER_NAME = "bobbcachesync"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)
db_manager = DatabaseManager()
DB = db_manager.get_db()
CONTRACT_ELEMENT_COLLECTION = DB.contractElement


class ContractElementRepository:
    @classmethod
    def get_active_contracts_elements(cls):
        return CONTRACT_ELEMENT_COLLECTION.find({STATUS: ACTIVE})
