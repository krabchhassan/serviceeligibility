import json
import os
import logging
from datetime import datetime

from commonomuhelper.omuhelper import produce_output_parameters, get_workdir_output_path
from commonpersonalworkdir.common_personal_workdir import build_personal_workdir_path
from openpyxl import Workbook

from bobbexportjob.common.common_constants import CODE_CONTRACT_ELEMENT, CODE_INSURER, CODE_AMC, IGNORED, CODE_OFFER, \
    CODE_PRODUCT, CODE_BENEFIT_NATURE, FROM_DATE, TO_DATE, EFFECTIVE_DATE, PRODUCT_ELEMENTS, CODE_Amc, BEYOND_COULOIR, \
    DATE_TIME_FORMAT, PREFIX, OUTPUT_FOLDER, LIST_EXPORTED_FILE, NUMBER_EXPORTED_FILES, TOTAL_EXPORTED_ROWS, \
    EMPTY_STRING, EXCEL_FILE_EXTENSION, USER_ID, DATE_FORMAT, OMU_ID_ENV_VAR
from bobbexportjob.exceptions.exception_handler import ExceptionHandler
from bobbexportjob.models.contract_element import ContractElement
from bobbexportjob.models.excel_columns import ExcelColumns
from bobbexportjob.models.product_element import ProductElement
from bobbexportjob.services.event_publisher_service import EventPublisherService
from bobbexportjob.logging import init_logging

LOGGER_NAME = "bobbexport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)



class ProcessContractElementService:
    def __init__(self, max_rows_per_file, input_file_name):
        self.output_folder = os.environ.get(OUTPUT_FOLDER)
        self.input_file_name = input_file_name
        self.max_rows_per_file = max_rows_per_file
        self.row_count = 0
        self.file_count = 1
        self.total_row_count = 0
        self.list_file = []
        self.wb = Workbook()
        self.ws = self.wb.active
        self.ws.append(ExcelColumns.CONTRACT_ELEMENT)

    def add_to_list_file(self, output_file_path):
        file_entry = f"{output_file_path} ({str(self.row_count)} lines)"
        self.list_file.append(file_entry)
        self.total_row_count += self.row_count

    def process_contract_data(self, contracts_elements):
        for contract_element_data in contracts_elements:
            contract_element = self.create_contract_element(contract_element_data)
            self.process_contract_element(contract_element)

        output_file_path = self.generate_output_file_path()
        self.wb.save(output_file_path)
        logger.info(f"Data written to {output_file_path}")
        self.add_to_list_file(output_file_path)
        filename = os.path.basename(output_file_path)
        event_publisher = EventPublisherService()
        event_publisher.publish_success_event(filename)

    def get_input_file_path(self):
        return os.path.join(get_workdir_output_path(), self.input_file_name)

    def create_contract_element(self, contract_element):
        product_elements_data = contract_element.get(PRODUCT_ELEMENTS, [])
        product_elements = [self.create_product_element(product_data) for product_data in product_elements_data]
        return ContractElement(
            code_contract_element=contract_element.get(CODE_CONTRACT_ELEMENT, EMPTY_STRING),
            code_insurer=contract_element.get(CODE_INSURER, EMPTY_STRING),
            code_amc=contract_element.get(CODE_AMC, EMPTY_STRING),
            product_elements=product_elements,
            ignored=contract_element.get(IGNORED, EMPTY_STRING)
        )

    def create_product_element(self, product_element):
        return ProductElement(
            code_amc=product_element.get(CODE_Amc, EMPTY_STRING),
            code_offer=product_element.get(CODE_OFFER, EMPTY_STRING),
            code_product=product_element.get(CODE_PRODUCT, EMPTY_STRING),
            code_benefit_nature=product_element.get(CODE_BENEFIT_NATURE, EMPTY_STRING),
            from_date=product_element.get(FROM_DATE, EMPTY_STRING),
            to_date=product_element.get(TO_DATE, EMPTY_STRING),
            effective_date=product_element.get(EFFECTIVE_DATE, EMPTY_STRING)
        )

    def process_contract_element(self, contract_element):
        if self.row_count >= self.max_rows_per_file:
            self.create_new_excel_file()
        if contract_element.product_elements:
            self.process_contract_with_product_elements(contract_element)
        else:
            self.process_contract_without_product_elements(contract_element)

    def create_new_excel_file(self):
        output_file_path = self.generate_output_file_path()
        self.wb.save(output_file_path)
        logger.info(f"Data written to {output_file_path}")
        self.add_to_list_file(output_file_path)
        self.publish_success_event_for_previous_file(output_file_path)
        self.row_count = 0
        self.file_count += 1
        self.wb = Workbook()
        self.ws = self.wb.active
        self.ws.append(ExcelColumns.CONTRACT_ELEMENT)

    def publish_success_event_for_previous_file(self, file_path):
        filename = os.path.basename(file_path)
        event_publisher = EventPublisherService()
        event_publisher.publish_success_event(filename)

    def process_contract_with_product_elements(self, contract_element):
        for product_element in contract_element.product_elements:
            row_data = [
                contract_element.code_contract_element,
                contract_element.code_insurer,
                contract_element.code_amc,
                product_element.code_amc,
                product_element.code_offer,
                product_element.code_product,
                product_element.code_benefit_nature,
                product_element.from_date.strftime(DATE_FORMAT) if isinstance(product_element.from_date, datetime) else product_element.from_date,
                product_element.to_date.strftime(DATE_FORMAT) if isinstance(product_element.to_date, datetime) else product_element.to_date,
                product_element.effective_date.strftime(DATE_FORMAT) if isinstance(product_element.effective_date, datetime) else product_element.effective_date,
                str(contract_element.ignored).lower()
            ]
            self.ws.append(row_data)
            self.row_count += 1

    def process_contract_without_product_elements(self, contract_element):
        row_data = [
            contract_element.code_contract_element,
            contract_element.code_insurer,
            contract_element.code_amc,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            EMPTY_STRING,
            str(contract_element.ignored).lower()
        ]
        self.ws.append(row_data)
        self.row_count += 1

    def generate_crex(self):
        produce_output_parameters({
            LIST_EXPORTED_FILE: self.list_file,
            NUMBER_EXPORTED_FILES: self.file_count,
            TOTAL_EXPORTED_ROWS: self.total_row_count
        })
        logger.info(f"list_file : {self.list_file}")
        logger.info(f"file_count : {self.file_count}")
        logger.info(f"total_row_count : {self.total_row_count}")

    def generate_filename(self):
        current_datetime = datetime.now().strftime(DATE_TIME_FORMAT)
        postfix = f"{self.file_count}{EXCEL_FILE_EXTENSION}"
        env = os.environ.get(BEYOND_COULOIR)
        if not env:
            raise ExceptionHandler("BEYOND_COULOIR environment variable is not set.")
        filename = f"{PREFIX}_{current_datetime}_{env}_{postfix}"
        return filename

    def generate_output_file_path(self):
        filename = os.getenv(OMU_ID_ENV_VAR) + "_" + self.generate_filename()
        personal_workdir_path = build_personal_workdir_path(os.getenv(USER_ID))
        if not os.path.exists(personal_workdir_path):
            os.makedirs(personal_workdir_path)
        output_file_path = os.path.join(personal_workdir_path, filename)
        return output_file_path
