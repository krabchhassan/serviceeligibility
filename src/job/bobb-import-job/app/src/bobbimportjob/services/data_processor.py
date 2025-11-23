import os
import logging
from copy import deepcopy
from datetime import timedelta, datetime

from commonomuhelper.omuhelper import read_output_parameters
from pymongo import InsertOne

from .event_publisher_service import EventPublisher
from .file_processor import FileProcessor
from .redis_cache_service import RedisCacheService
from bobbimportjob.common.common_constants import (
    CODE_CONTRACT_ELEMENT, CODE_INSURER, PRODUCT_ELEMENTS,
    VOLUMETRY_RATE_ENV_VAR, NON, OUI, CODE_PRODUCT, FROM, TO,
    DEPRECIATE, ADDED, UPDATED, NO_CHANGES, IGNORED, USER, USER_ID, COMPLET_MODE,
    STATUS, ACTIVE, PENDING, INSERT_DATE, FILENAME, CODE_AMC, CODE_OFFER
)
from bobbimportjob.common.report import Report
from bobbimportjob.exceptions.volumetry_exception import VolumetryException
from bobbimportjob.repositories.contract_element_repository import ContractElementRepository, CONTRACT_ELEMENT_COLLECTION
from bobbimportjob.logging import init_logging

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

WORKDIR_FOLDER = os.path.join("/workdir", "bobb/")
VOLUMETRY_RATE = int(os.getenv(VOLUMETRY_RATE_ENV_VAR, "95"))
report = Report()
MAX_WORKERS = int(os.environ.get("MAX_WORKERS", "10"))


class DataProcessor:
    def __init__(self):
        self.contract_element_repository = ContractElementRepository()
        self.report = Report()
        self.event_publisher = EventPublisher()
        self.file_processor = FileProcessor()
        self.redis_cache_service = RedisCacheService()

    @staticmethod
    def prepare_data(contract_elements_map, filename):
        def get_user_id():
            return read_output_parameters(USER_ID)

        def prepare_product_elements(contract_element):
            return [product_element.to_dict() for product_element in contract_element.product_elements]

        def prepare_contract_element_dict(contract_element, products_elements_list, user_id, filename):
            contract_element_dict = contract_element.to_dict()
            contract_element_dict.update({
                PRODUCT_ELEMENTS: products_elements_list,
                USER: user_id,
                STATUS: PENDING,
                INSERT_DATE: datetime.now(),
                FILENAME: filename
            })
            return contract_element_dict

        dict_list = []
        user_id = get_user_id()
        for contract_element in contract_elements_map.values():
            products_elements_list = prepare_product_elements(contract_element)
            contract_element_dict = prepare_contract_element_dict(contract_element, products_elements_list, user_id,
                                                                  filename)
            dict_list.append(contract_element_dict)

        return dict_list

    def check_volumetry(self, list_files_names, mode, forcage):
        """
        :param list_files_names: List of files name existing in input directory
        :param mode: Mode of import {COMPLET || DIFFERENTIEL}
        :param forcage: Mode of forcage {OUI || NON}
        :raise VolumetryException if volumetry rate is not respected and forage mode is NON
        """
        documents_db_count = self.contract_element_repository.contract_elements_count()
        minimum_number_lines = round((float(VOLUMETRY_RATE) / 100) * documents_db_count)
        first_file_name = list_files_names[0]
        total_product_elements = self.file_processor.get_count_of_product_element(first_file_name)
        logger.info("VOLUMETRY_RATE = {%s}, number of lines expected = {%s}, number of lines in file = {%s}",
                    VOLUMETRY_RATE, minimum_number_lines, total_product_elements)
        self.report.total_lines_pending = total_product_elements
        if total_product_elements < minimum_number_lines:
            if forcage == NON:
                error_msg = ("La volumétrie n'est pas respectée : le nombre de lignes dans le fichier est inférieur "
                             "au seuil minimum attendu.")
                self.event_publisher.publish_failed_import_file_event(
                    filename=first_file_name,
                    mode=mode,
                    forcage=forcage,
                    error=error_msg
                )
                self.report.list_files_ko.append(first_file_name)
                raise VolumetryException(error_msg)
            if forcage == OUI:
                logger.info("Forcing mode applied")
        else:
            logger.info("Volumetry validated")

    def process_contract_elements_existent_in_file(self, data):
        """
        :param data: Valid data extracted from file

        This function iterates through each contract element in the input data extracted from the file. For each contract
        element, it checks if it exists in the database. If the contract element does not exist in the database, it adds
        all product elements of the contract element from the file. If the contract element exists in the database, it
        extracts changes made to the product elements included in the contract element at the file level. These changes
        include added, updated, and deleted product elements. The function then updates the contract element in the database
        with the extracted product elements
        """
        insert_operations = []

        for line in data:
            contract_element = self.contract_element_repository.find_contract_element(
                line.get(CODE_CONTRACT_ELEMENT, ""),
                line.get(CODE_INSURER, ""),
                line.get(CODE_AMC, ""),
                ACTIVE
            )

            if contract_element is None:
                self.handle_new_contract_element(line, insert_operations)
            else:
                self.update_contract_element(contract_element, line, insert_operations)

        if insert_operations:
            CONTRACT_ELEMENT_COLLECTION.bulk_write(insert_operations)

    def handle_new_contract_element(self, line, insert_operations):
        insert_operations.append(InsertOne(line))
        product_elements = line.get(PRODUCT_ELEMENTS, [])
        if not product_elements:
            self.report.differential_operations[ADDED].append({})
        else:
            for product_element in product_elements:
                self.report.differential_operations[ADDED].append(product_element)
        self.update_report_counters(product_elements)

    def update_contract_element(self, contract_element, line, insert_operations):
        original_contract_element = deepcopy(contract_element)
        contract_element.update({
            STATUS: line.get(STATUS)
        })
        contract_element.update({
            IGNORED: line.get(IGNORED)
        })
        contract_element.update({
            USER: line.get(USER)
        })
        contract_element.update({
            FILENAME: line.get(FILENAME)
        })

        product_elements = line.get(PRODUCT_ELEMENTS, [])
        updated_product_elements = self.extract_product_element(original_contract_element, line)

        if self.should_update_contract_element(product_elements, contract_element, original_contract_element):
            self.update_contract_element_data(contract_element, line, updated_product_elements)
            insert_operations.append(InsertOne(contract_element))
        self.update_report_counters(product_elements)

    def should_update_contract_element(self, updated_product_elements, contract_element, original_contract_element):
        product_elements_set = {frozenset(element.items()) for element in original_contract_element.get(PRODUCT_ELEMENTS,[])}
        updated_product_elements_set = {frozenset(element.items()) for element in updated_product_elements}

        return product_elements_set != updated_product_elements_set or contract_element.get(IGNORED) != original_contract_element.get(IGNORED)

    def update_contract_element_data(self, contract_element, line, updated_product_elements):
        contract_element.update({
            PRODUCT_ELEMENTS: updated_product_elements
        })
        contract_element.update({
            "_id": line.get("_id")
        })

    def update_report_counters(self, product_elements):
        self.report.total_corresponsdence_in_file += max(len(product_elements), 1)
        self.report.total_guarantees_in_file += 1

    def add_all_products_of_contract_element(self, line):
        self.contract_element_repository.insert_new_contract_element(line)

        product_elements = line.get(PRODUCT_ELEMENTS, "")
        if not product_elements:
            self.report.differential_operations[ADDED].append({})
        for product_element in product_elements:
            self.report.differential_operations[ADDED].append(product_element)

    def extract_product_element(self, contract_element_stored, contract_element_in_file):
        """
        Extract product elements and their changes between the stored contract element in the database
        and the contract element in the input file.

        :param contract_element_stored: ContractElement stored in DB
        :param contract_element_in_file: ContractElement in the input file
        :return: List of ProductElement extracted from the difference between DB and input file
        """
        list_product_elements = []

        file_product_elements = {
            (element.get(CODE_PRODUCT, ""), element.get(FROM, ""), element.get(CODE_OFFER, "")) for element in
            contract_element_in_file.get(PRODUCT_ELEMENTS)
        }
        stored_product_elements = {
            (element.get(CODE_PRODUCT, ""), element.get(FROM, ""), element.get(CODE_OFFER, "")) for element in
            contract_element_stored.get(PRODUCT_ELEMENTS)
        }

        added_elements = self.find_added_elements(
            contract_element_in_file,
            stored_product_elements
        )

        updated_elements = self.find_updated_elements(contract_element_in_file, contract_element_stored)

        deleted_elements = self.find_deleted_elements(file_product_elements, contract_element_stored)
        list_product_elements += added_elements.get(ADDED, [])
        list_product_elements += updated_elements.get(UPDATED, [])
        list_product_elements += updated_elements.get(NO_CHANGES, [])
        list_product_elements += deleted_elements.get(DEPRECIATE, [])
        list_product_elements += deleted_elements.get(NO_CHANGES, [])

        return list_product_elements

    def find_added_elements(self, contract_element_in_file, stored_product_elements):
        """
        :param contract_element_in_file: ContractElement existing in input file
        :param stored_product_elements: Array of ProductElements existing in DB
        :return: List of productElements not existing DB
        """
        added_elements = {ADDED: []}

        for element in contract_element_in_file.get(PRODUCT_ELEMENTS, []):
            if (element.get(CODE_PRODUCT), element.get(FROM), element.get(CODE_OFFER)) \
                    not in stored_product_elements:
                added_elements[ADDED].append(element)
                self.report.differential_operations[ADDED].append(element)
        return added_elements

    def find_updated_elements(self, contract_element_in_file, contract_element_stored):
        """
        :param contract_element_in_file: ContractElement existing in input file
        :param contract_element_stored: ContractElement existing in DB
        :return: List of productElements existing in input file and DB
        """
        updated_elements = {UPDATED: [], NO_CHANGES: []}
        for product_element_stored in contract_element_stored.get(PRODUCT_ELEMENTS):
            code_product_stored = product_element_stored.get(CODE_PRODUCT)
            from_date_stored = product_element_stored.get(FROM)
            code_offer = product_element_stored.get(CODE_OFFER)
            for element in contract_element_in_file.get(PRODUCT_ELEMENTS):
                if element.get(CODE_PRODUCT) == code_product_stored and element.get(
                        FROM) == from_date_stored and element.get(CODE_OFFER) == code_offer:
                    if product_element_stored == element and contract_element_in_file.get(
                            IGNORED) == contract_element_stored.get(IGNORED):
                        self.report.differential_operations[NO_CHANGES].append(product_element_stored)
                        updated_elements[NO_CHANGES].append(product_element_stored)
                    else:
                        self.report.differential_operations[UPDATED].append(element)
                        updated_elements[UPDATED].append(element)
                    break
        return updated_elements

    def find_deleted_elements(self, product_elements_in_file, contract_element_stored):
        """
        :param product_elements_in_file: ProductElements existing in input file
        :param contract_element_stored: ContractElement existing in DB
        :return: List of productElements not existing in input file
        """
        depreciated_elements = {DEPRECIATE: [], NO_CHANGES: []}
        for product_element_stored in contract_element_stored.get(PRODUCT_ELEMENTS):
            code_product_stored = product_element_stored.get(CODE_PRODUCT)
            from_date_stored = product_element_stored.get(FROM)
            code_offer = product_element_stored.get(CODE_OFFER)
            if (code_product_stored, from_date_stored, code_offer) not in product_elements_in_file:
                if (not product_element_stored.get(TO) or product_element_stored[TO] !=
                        product_element_stored[FROM] - timedelta(days=1)):
                    product_element_stored[TO] = from_date_stored - timedelta(days=1)
                    self.report.differential_operations[DEPRECIATE].append(product_element_stored)
                    depreciated_elements[DEPRECIATE].append(product_element_stored)
                else:
                    depreciated_elements[NO_CHANGES].append(product_element_stored)
        return depreciated_elements

    def commit_validated_rows(self, file_name, mode):
        logger.info("Start commit for file")
        if mode == COMPLET_MODE:
            self.contract_element_repository.commit_validated_rows_in_db(file_name)
        else:
            self.contract_element_repository.commit_differentiel_in_db()
        logger.info("End commit for file")

    def clean_cache(self):
        self.redis_cache_service.delete_all_contract_elements_in_redis_cache()

    def process_all_contracts_elements(self, data):
        self.contract_element_repository.add_all_pending_contracts_elements(data)
        self.update_report_totals(data)

    def update_report_totals(self, data):
        total_elements = sum(len(document.get(PRODUCT_ELEMENTS, [])) or 1 for document in data)
        total_correspondence = total_elements
        total_guarantees = len(data)

        self.report.total_elements += total_elements
        self.report.total_corresponsdence_in_file += total_correspondence
        self.report.total_guarantees_in_file += total_guarantees

    def rollback_import(self, file_name):
        self.contract_element_repository.delete_pending_contract(file_name)

    def delete_pending_contracts(self, file_name):
        logger.info("Start deleting pending contractElements")
        self.contract_element_repository.delete_pending_contract(file_name)
        logger.info("End deleting pending contractElements")
