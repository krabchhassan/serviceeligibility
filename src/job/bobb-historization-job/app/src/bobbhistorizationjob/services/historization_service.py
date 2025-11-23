import os
import logging
from datetime import datetime

from commonomuhelper.omuhelper import read_output_parameters

from bobbhistorizationjob.common.common_constants import (BOBB_IMPORT_INDEX_NAME, COMMON_INDEX_BASENAME_VAR_ENV, INDEXING_DATE, ITCARE_,
                                       INDEXING_DATE_FORMAT, PRODUCT_ELEMENTS, FROM, TO, EFFECTIVE_DATE, LIST_FILES_OK)
from bobbhistorizationjob.configuration.opensearch_connection import OpenSearchConnection
from bobbhistorizationjob.repositories.contract_element_repository import ContractElementRepository
from bobbhistorizationjob.logging import init_logging

LOGGER_NAME = "bobbhistorization"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

CHUNK_SIZE = int(os.environ.get("CHUNK_SIZE", "1000"))


class HistorizationService:
    def __init__(self):
        self.client = OpenSearchConnection().client
        self.index = ITCARE_ + os.environ.get(COMMON_INDEX_BASENAME_VAR_ENV) + "_" + BOBB_IMPORT_INDEX_NAME + "_index"
        self.alias = ITCARE_ + os.environ.get(COMMON_INDEX_BASENAME_VAR_ENV) + "_" + BOBB_IMPORT_INDEX_NAME
        self.contract_element_repository = ContractElementRepository()

    def historize_files(self):
        list_files_names = read_output_parameters(LIST_FILES_OK)
        for file_name in list_files_names:
            logger.info(f"Start historization for file {file_name}")
            contracts_elements = self.contract_element_repository.get_active_contracts_elements(file_name)
            chunk = []
            for contract_element in contracts_elements:
                chunk.append(contract_element)
                if len(chunk) >= CHUNK_SIZE:
                    self.bulk_historize_contract_elements(chunk)
                    chunk = []
            if chunk:
                self.bulk_historize_contract_elements(chunk)
            logger.info(f"End historization for file {file_name}")

    def historize_contract_element(self, document):
        doc = document.copy()
        doc.pop('_id', None)
        doc = self._add_creation_date(doc)
        self.client.index(index=self.alias, body=doc)

    def bulk_historize_contract_elements(self, contract_elements):
        """
        :param contract_elements: List of contractElements to insert in opensearch
        :return:
        """
        formatted_documents = self.format_documents_for_bulk_insertion(contract_elements)
        bulk_data = []
        for doc in formatted_documents:
            bulk_data.extend([
                {"index": {"_index": self.alias}},
                doc
            ])

        self.client.bulk(body=bulk_data)
        logger.info(f"Batch historization completed. Number of indexed documents: {len(formatted_documents)}")

    def format_documents_for_bulk_insertion(self, documents):
        formatted_documents = []
        for item in documents:
            doc = self._prepare_document(item)
            formatted_documents.append(doc)
            self._clean_product_elements(item)
        return formatted_documents

    def _prepare_document(self, item):
        doc = item.copy()
        doc.pop('_id', None)
        return self._add_creation_date(doc)

    def _clean_product_elements(self, item):
        for product in item.get(PRODUCT_ELEMENTS, []):
            self._remove_empty_fields(product, [TO, FROM, EFFECTIVE_DATE])

    def _remove_empty_fields(self, product, fields):
        for field in fields:
            if not product.get(field, ""):
                product.pop(field, None)

    @staticmethod
    def _add_creation_date(document):
        document[INDEXING_DATE] = datetime.now().strftime(INDEXING_DATE_FORMAT)
        return document
