import os
import logging
from bobbhistorizationjob.common.database_manager import DatabaseManager
from bobbhistorizationjob.common.common_constants import STATUS, ACTIVE, FILENAME

from bobbhistorizationjob.logging import init_logging
LOGGER_NAME = "bobbhistorization"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

CHUNK_SIZE = int(os.getenv("CHUNK_SIZE", "1000"))


class ContractElementRepository:
    def __init__(self):
        self.db = DatabaseManager().get_db()
        self.collection = self.db.contractElement

    def get_active_contracts_elements(self, file_name):
        return self.collection.find({STATUS: ACTIVE, FILENAME: file_name}).batch_size(CHUNK_SIZE)
