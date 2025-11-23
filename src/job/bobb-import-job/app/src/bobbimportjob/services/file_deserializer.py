import logging
import uuid

from bobbimportjob.repositories.contract_element_repository import ContractElementRepository
from bobbimportjob.services.event_publisher_service import EventPublisher

from bobbimportjob.common.common_constants import (
    CODE_CONTRACT_ELEMENT, CODE_INSURER, CODE_AMC, IGNORED, PRODUCT_ELEMENTS,
    CODE_OFFER, CODE_PRODUCT, CODE_BENEFITE_NATURE, FROM, TO,
    EFFECTIVE_DATE, INPUT_DATETIME_FORMAT, COMPLET_MODE, IMPORT_ORIGINE, CODE_OC, DIFFERENTIEL_MODE, CODE_AMC_PRODUCT)
from bobbimportjob.common.report import Report
from bobbimportjob.exceptions.invalid_parameter_exception import InvalidParameterException
from bobbimportjob.models.contract_element import ContractElement
from bobbimportjob.models.product_element import ProductElement
from bobbimportjob.logging import init_logging

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

BOBB_CONTRACT_COLUMNS = {CODE_CONTRACT_ELEMENT, CODE_INSURER, CODE_AMC, IGNORED, PRODUCT_ELEMENTS}
BOBB_PRODUCT_COLUMNS = {CODE_OC, CODE_OFFER, CODE_PRODUCT, CODE_BENEFITE_NATURE, FROM}


class FileDeserializer:
    def __init__(self):
        self.report = Report()
        self.event_publisher = EventPublisher()
        self.contract_element_repository = ContractElementRepository()

    def deserialize_content(self, content, keycloak_user, mode, file_name):
        """
        :param content: Json content in input file
        :param keycloak_user: The keycloak user.
        :param mode: The mode of import
        :param file_name: The file name
        :return: A map containing contract elements with their keys.
        """
        keys_contract_elements_map = {}
        for contract_json in content:
            try:
                if FileDeserializer.check_bobb_contract_columns(contract_json):
                    key = contract_json.get(CODE_CONTRACT_ELEMENT) + "_" + contract_json.get(CODE_INSURER)
                    contract_element = self.deserialize_json_item(contract_json,
                                                                  keycloak_user, mode, file_name)
                    keys_contract_elements_map[key] = contract_element
                else:
                    raise InvalidParameterException("Erreur de correspondance des colonnes des éléments de contrat")
            except Exception as e:
                if mode == COMPLET_MODE:
                    for document in content:
                        self.report.invalid_lines.extend(document.get(PRODUCT_ELEMENTS, []))
                    raise
        return keys_contract_elements_map

    def deserialize_json_item(self, contract_json, keycloak_user, mode, file_name):
        """
         Deserialize a JSON contract item.
        :param contract_json: The json contract to deserialize.
        :param keycloak_user: The keycloak user.
        :param mode: The mode of import
        :param file_name: The file name
        :return: The deserialized contract element.
        """
        contract_element = self.try_deserialize_contract_element_fields(contract_json,
                                                                        keycloak_user, mode, file_name)
        list_products_elements = self.process_product_elements(contract_json, mode, file_name)
        self.check_duplication_of_product(contract_element, list_products_elements, contract_json, mode, file_name)
        return contract_element

    def try_deserialize_contract_element_fields(self, contract_json, keycloak_user,
                                                mode, file_name):
        try:
            return self.get_or_create_contract_element(contract_json, keycloak_user)
        except Exception as e:
            if mode == DIFFERENTIEL_MODE:
                self.handle_contract_element_error(contract_json, file_name, e)
            raise

    def handle_contract_element_error(self, contract_json, file_name, exception):
        self.event_publisher.publish_contract_integration_failed(contract_json, file_name, exception)
        product_elements = contract_json.get(PRODUCT_ELEMENTS, [])
        if not product_elements:
            self.report.invalid_lines.append({"line": "", "errorType": str(exception)})
        else:
            for product in product_elements:
                self.report.invalid_lines.append({"line": product, "errorType": str(exception)})

    def process_product_elements(self, contract_json, mode, file_name):
        list_products_elements = []
        for product in contract_json.get(PRODUCT_ELEMENTS, []):
            try:
                list_products_elements.append(self.create_product_element(product))
            except Exception as e:
                if mode == DIFFERENTIEL_MODE:
                    self.handle_product_element_error(product, contract_json, file_name, e)
                else:
                    raise
        return list_products_elements

    def handle_product_element_error(self, product, contract_json, file_name, exception):
        self.report.invalid_lines.append({"line": product, "errorType": str(exception)})
        self.event_publisher.publish_line_integration_failed(file_name,
                                                             contract_json.get(CODE_CONTRACT_ELEMENT, ""),
                                                             contract_json.get(CODE_INSURER, ""),
                                                             contract_json.get(IGNORED, ""),
                                                             contract_json.get(CODE_AMC, ""),
                                                             product.get(CODE_OC, ""),
                                                             product.get(CODE_OFFER, ""),
                                                             product.get(CODE_PRODUCT, ""),
                                                             product.get(CODE_BENEFITE_NATURE, ""),
                                                             product.get(EFFECTIVE_DATE, ""),
                                                             product.get(TO, ""),
                                                             product.get(FROM, ""),
                                                             exception
                                                             )

    def check_duplication_of_product(self, contract_element, list_products_elements, line, mode, file_name):
        """
        Check for duplication of products
        :param contract_element: The contract element.
        :param list_products_elements: The list of product elements.
        :param line: The line
        :param mode:  The mode.
        :param file_name: The file name.
        """
        for product_to_check in list_products_elements:
            duplicated = True if product_to_check in contract_element.product_elements else False
            if not duplicated:
                contract_element.product_elements.append(product_to_check)
            else:
                self.handle_duplicate_product(product_to_check, line, mode, file_name)

    def handle_duplicate_product(self, duplicate_product, line, mode, file_name):
        """
        Handle a duplicate product.
        :param duplicate_product: The duplicate product.
        :param line: The line
        :param mode: The mode of import
        :param file_name: The input file name
        """
        error_msg = f"Valeurs en double pour codeOffer : {duplicate_product.code_offer} et codeProduct : {duplicate_product.code_product}"
        if mode == COMPLET_MODE:
            raise InvalidParameterException(error_msg)
        else:
            logger.error(error_msg)
            self.report.invalid_lines.append({"line": duplicate_product, "errorType": error_msg})
            self.event_publisher.publish_line_integration_failed(file_name,
                                                                 line.get(CODE_CONTRACT_ELEMENT, ""),
                                                                 line.get(CODE_INSURER, ""),
                                                                 line.get(IGNORED, ""),
                                                                 line.get(CODE_AMC, ""),
                                                                 duplicate_product.code_oc,
                                                                 duplicate_product.code_offer,
                                                                 duplicate_product.code_product,
                                                                 duplicate_product.code_benefit_nature,
                                                                 duplicate_product.effective_date.strftime(
                                                                     INPUT_DATETIME_FORMAT) if duplicate_product.effective_date else "",
                                                                 duplicate_product.to.strftime(
                                                                     INPUT_DATETIME_FORMAT) if duplicate_product.to else "",
                                                                 duplicate_product.from_date.strftime(
                                                                     INPUT_DATETIME_FORMAT) if duplicate_product.from_date else "",
                                                                 error_msg
                                                                 )

    @staticmethod
    def create_product_element(product_element_json):
        """
        :param product_element_json: The JSON representation of the product element.
        :return: The created product element.
        """
        if FileDeserializer.check_bobb_product_columns(product_element_json):
            product_element = ProductElement(
                code_offer=product_element_json.get(CODE_OFFER),
                code_product=product_element_json.get(CODE_PRODUCT),
                code_benefit_nature=product_element_json.get(CODE_BENEFITE_NATURE),
                code_oc=product_element_json.get(CODE_OC),
                from_date=product_element_json.get(FROM),
                effective_date=product_element_json.get(EFFECTIVE_DATE),
                to=product_element_json.get(TO)
            )
            return product_element
        else:
            raise InvalidParameterException("Erreur de correspondance des colonnes de productElement.")

    def get_or_create_contract_element(self, item, keycloak_user):
        """
        :param item: Json item in input file equivalent to ContractElement
        :param keycloak_user: Keycloak user {not currently available}
        :return: Instance of ContractElement
        """
        contract_element_stored = ContractElementRepository.find_pending_contract_element(
            item.get(CODE_CONTRACT_ELEMENT),
            item.get(CODE_INSURER),
            item.get(CODE_AMC)
        )

        if contract_element_stored:
            ContractElementRepository.delete_pending_contract_element(
                item.get(CODE_CONTRACT_ELEMENT),
                item.get(CODE_INSURER),
                item.get(CODE_AMC)
            )
            self.report.total_elements -= 1
            self.report.total_guarantees_in_file -= 1

        return self._create_new_contract_element(item, keycloak_user)

    def _validate_ignored_field(self, item, contract_element, key):
        new_is_ignored = str(item.get(IGNORED)).lower()
        if new_is_ignored != str(contract_element.ignored).lower():
            raise InvalidParameterException(f"Valeur incohérente 'ignored' pour {key}")

    def _create_new_contract_element(self, item, keycloak_user):
        return ContractElement(
            _id=str(uuid.uuid4()),
            code_amc=item.get(CODE_AMC),
            code_contract_element=item.get(CODE_CONTRACT_ELEMENT),
            code_insurer=item.get(CODE_INSURER),
            label=item.get(CODE_CONTRACT_ELEMENT),
            ignored=item.get(IGNORED),
            origine=IMPORT_ORIGINE,
            user=keycloak_user
        )

    @staticmethod
    def check_bobb_contract_columns(json_line):
        for column in BOBB_CONTRACT_COLUMNS:
            if column not in json_line:
                logger.error(column + " not found")
                return False
        return True

    @staticmethod
    def check_bobb_product_columns(json_line):
        for column in BOBB_PRODUCT_COLUMNS:
            if column not in json_line:
                logger.error(column + " not found")
                return False
        return True
