import os
import logging

from bobbimportjob.common.common_constants import CODE_CONTRACT_ELEMENT, CODE_INSURER, STATUS, ACTIVE, PENDING, FILENAME, CODE_AMC

from bobbimportjob.common.database_manager import DatabaseManager
from bobbimportjob.logging import init_logging
from pymongo import ASCENDING, UpdateOne, DeleteOne

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)
db_manager = DatabaseManager()
DB = db_manager.get_db()
CONTRACT_ELEMENT_COLLECTION = DB.contractElement
CHUNK_SIZE = int(os.getenv("CHUNK_SIZE", "1000"))


class ContractElementRepository:
    @classmethod
    def add_all_pending_contracts_elements(cls, contract_elements):
        try:
            with db_manager.start_transaction_context() as session:
                CONTRACT_ELEMENT_COLLECTION.insert_many(contract_elements, session=session)
                logger.info("MongoDB insertion successful")
        except Exception as e:
            logger.exception(e)
            raise

    @classmethod
    def contract_elements_count(cls):
        count_of_products_elements_query = [
            {
                "$project": {
                    "_id": 0,
                    "productElementCount": {"$size": "$productElements"}
                }
            },
            {
                "$group": {
                    "_id": None,
                    "totalProductElements": {"$sum": "$productElementCount"}
                }
            }
        ]
        result = list(CONTRACT_ELEMENT_COLLECTION.aggregate(count_of_products_elements_query))
        return result[0]['totalProductElements'] if result else 0

    @classmethod
    def find_contract_element(cls, code_contract_element, code_insurer, code_amc, status):
        return CONTRACT_ELEMENT_COLLECTION.find_one({
            CODE_CONTRACT_ELEMENT: code_contract_element,
            CODE_INSURER: code_insurer,
            CODE_AMC: code_amc,
            STATUS: status
        })

    @classmethod
    def find_pending_contract_element(cls, code_contract_element, code_insurer, code_amc):
        return CONTRACT_ELEMENT_COLLECTION.find_one({
            CODE_CONTRACT_ELEMENT: code_contract_element,
            CODE_INSURER: code_insurer,
            CODE_AMC: code_amc,
            STATUS: PENDING
        })

    @classmethod
    def delete_pending_contract_element(cls, code_contract_element, code_insurer, code_amc):
        return CONTRACT_ELEMENT_COLLECTION.delete_one({
            CODE_CONTRACT_ELEMENT: code_contract_element,
            CODE_INSURER: code_insurer,
            CODE_AMC: code_amc,
            STATUS: PENDING
        })

    @classmethod
    def insert_new_contract_element(cls, contract_element):
        CONTRACT_ELEMENT_COLLECTION.insert_one(contract_element)

    @classmethod
    def update_existing_contract_element(cls, update_filter, updated_set):
        return CONTRACT_ELEMENT_COLLECTION.update_one(
            update_filter,
            {
                "$set": updated_set
            }
        )

    @classmethod
    def find_existing_contract_elements(cls):
        return CONTRACT_ELEMENT_COLLECTION.find()

    @classmethod
    def commit_validated_rows_in_db(cls, file_name):
        try:
            with db_manager.start_transaction_context() as session:
                CONTRACT_ELEMENT_COLLECTION.delete_many({STATUS: ACTIVE}, session=session)
                query = {
                    STATUS: PENDING,
                    FILENAME: file_name
                }
                new_values = {"$set": {STATUS: ACTIVE}}
                CONTRACT_ELEMENT_COLLECTION.update_many(query, new_values, session=session)
        except Exception as e:
            db_manager.abort_transaction()
            db_manager.close_connection()
            logger.exception(e)
            raise

    @classmethod
    def commit_differentiel_in_db(cls):
        try:
            bulk_operations = []
            counter = 0
            pending_cursor = CONTRACT_ELEMENT_COLLECTION.find({STATUS: PENDING}).batch_size(CHUNK_SIZE)

            for document in pending_cursor:
                code_contract_element = document.get(CODE_CONTRACT_ELEMENT, "")
                code_insurer = document.get(CODE_INSURER, "")
                code_amc = document.get(CODE_AMC, "")
                contract_filter = {
                    CODE_CONTRACT_ELEMENT: code_contract_element,
                    CODE_INSURER: code_insurer,
                    CODE_AMC: code_amc,
                    STATUS: ACTIVE
                }

                contract_element_stored = CONTRACT_ELEMENT_COLLECTION.find_one(contract_filter)
                if contract_element_stored is not None:
                    bulk_operations.append(DeleteOne(contract_filter))

                bulk_operations.append(UpdateOne(
                    {
                        CODE_CONTRACT_ELEMENT: code_contract_element,
                        CODE_INSURER: code_insurer,
                        CODE_AMC: code_amc,
                        STATUS: PENDING
                    },
                    {"$set": {STATUS: ACTIVE}}
                ))

                counter += 1

                # Execute bulk_write every 1000 operations
                if counter % CHUNK_SIZE == 0:
                    with db_manager.start_transaction_context() as session:
                        CONTRACT_ELEMENT_COLLECTION.bulk_write(bulk_operations, session=session)
                    bulk_operations = []

            # Execute any remaining operations
            if bulk_operations:
                with db_manager.start_transaction_context() as session:
                    CONTRACT_ELEMENT_COLLECTION.bulk_write(bulk_operations, session=session)

        except Exception as e:
            db_manager.abort_transaction()
            logger.exception(e)
            raise

    def ensure_index(self):
        index_name = "contractElementUnicity"
        indexes = CONTRACT_ELEMENT_COLLECTION.index_information()

        index_spec = [
            (CODE_CONTRACT_ELEMENT, ASCENDING),
            (CODE_INSURER, ASCENDING),
            (CODE_AMC, ASCENDING),
            (STATUS, ASCENDING)
        ]

        if index_name not in indexes:
            try:
                CONTRACT_ELEMENT_COLLECTION.create_index(
                    index_spec,
                    name=index_name,
                    unique=True
                )
                logger.debug(f"Index '{index_name}' created.")
            except Exception as e:
                logger.error(f"{str(e)}")
        else:
            logger.debug(f"Index '{index_name}' already exists.")

    def migrate_data(self):
        count_documents_without_status = CONTRACT_ELEMENT_COLLECTION.count_documents(
            {STATUS: {"$exists": False}}
        )
        if count_documents_without_status > 0:
            update_query = {"$set": {STATUS: "ACTIVE"}}
            CONTRACT_ELEMENT_COLLECTION.update_many({STATUS: {"$exists": False}}, update_query)
            logger.info("Migrate data")

    def delete_pending_contract(self, file_name):
        with db_manager.start_transaction_context() as session:
            CONTRACT_ELEMENT_COLLECTION.delete_many({STATUS: PENDING, FILENAME: file_name}, session=session)
