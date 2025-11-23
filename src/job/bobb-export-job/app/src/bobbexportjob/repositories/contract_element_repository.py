import  logging
from bobbexportjob.common.common_constants import CONTRACT_ELEMENT, ID
from bobbexportjob.logging import init_logging

LOGGER_NAME = "bobbexport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class ContractElementRepository:
    def __init__(self, db_manager):
        self.db_manager = db_manager

    def get_collection(self):
        return self.db_manager.get_db()[CONTRACT_ELEMENT]

    def get_all_contract_elements(self):
        try:
            collection = self.get_collection()
            cursor = collection.find({}, {ID: 0}).batch_size(1000)
            logger.info("Retrieved all contract elements from the database")
            return cursor
        except Exception as e:
            logger.error(f"Failed to retrieve contract elements: {e}")
            raise
