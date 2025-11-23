from unittest.mock import patch

from bobbimportjob.exceptions.invalid_parameter_exception import InvalidParameterException

from bobbimportjob.common.common_constants import COMPLET_MODE

from bobbimportjob.services.file_deserializer import FileDeserializer

import unittest


class TestFileDeserializer(unittest.TestCase):

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_deserialize_content_valid_json(self, mock_event_publisher, mock_contract_element_repository):
        deserializer = FileDeserializer()
        content = [
            {
                "codeContractElement": "123",
                "codeInsurer": "456",
                "codeAMC": "",
                "productElements": [
                    {
                        "codeOffer": "offer1",
                        "codeProduct": "product1",
                        "codeBenefitNature": "benefit1",
                        "codeOC": "oc1",
                        "from": "2023-01-01"
                    }
                ],
                "ignored": "false"
            }
        ]
        keycloak_user = "user"
        mode = COMPLET_MODE
        file_name = "test_file.json"

        result = deserializer.deserialize_content(content, keycloak_user, mode, file_name)

        self.assertIn("123_456", result)
        self.assertEqual(result["123_456"].code_contract_element, "123")
        self.assertEqual(result["123_456"].code_insurer, "456")

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_deserialize_json_item_valid_contract(self, mock_event_publisher, mock_contract_element_repository):
        deserializer = FileDeserializer()
        contract_json = {
            "codeContractElement": "123",
            "codeInsurer": "456",
            "codeAMC": "",
            "productElements": [
                {
                    "codeOffer": "offer1",
                    "codeProduct": "product1",
                    "codeBenefitNature": "benefit1",
                    "codeOC": "oc1",
                    "from": "2023-01-01"
                }
            ],
            "ignored": "false"
        }
        keycloak_user = "user"
        mode = COMPLET_MODE
        file_name = "test_file.json"

        result = deserializer.deserialize_json_item(contract_json, keycloak_user, mode, file_name)

        self.assertEqual(result.code_contract_element, "123")
        self.assertEqual(result.code_insurer, "456")

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_try_deserialize_contract_element_fields_valid(self, mock_event_publisher, mock_contract_element_repository):
        deserializer = FileDeserializer()
        contract_json = {
            "codeContractElement": "123",
            "codeInsurer": "456",
            "codeAMC": "",
            "ignored": "false"
        }
        keycloak_user = "user"
        mode = COMPLET_MODE
        file_name = "test_file.json"

        result = deserializer.try_deserialize_contract_element_fields(contract_json, keycloak_user, mode, file_name)

        self.assertEqual(result.code_contract_element, "123")
        self.assertEqual(result.code_insurer, "456")

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_process_product_elements_valid(self, mock_event_publisher, mock_contract_element_repository):
        deserializer = FileDeserializer()
        contract_json = {
            "productElements": [
                {
                    "codeOffer": "offer1",
                    "codeProduct": "product1",
                    "codeBenefitNature": "benefit1",
                    "codeOC": "oc1",
                    "from": "2023-01-01"
                }
            ]
        }
        mode = COMPLET_MODE
        file_name = "test_file.json"

        result = deserializer.process_product_elements(contract_json, mode, file_name)

        self.assertEqual(len(result), 1)
        self.assertEqual(result[0].code_offer, "offer1")
        self.assertEqual(result[0].code_product, "product1")

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_deserialize_content_missing_fields(self, mock_event_publisher, mock_contract_element_repository):
        deserializer = FileDeserializer()
        content = [
            {
                "codeContractElement": "",
                "codeInsurer": "",
                "productElements": []
            }
        ]
        keycloak_user = "user"
        mode = COMPLET_MODE
        file_name = "test_file.json"

        with self.assertRaises(InvalidParameterException):
            deserializer.deserialize_content(content, keycloak_user, mode, file_name)

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_deserialize_json_item_invalid_contract(self, mock_event_publisher, mock_contract_element_repository):
        deserializer = FileDeserializer()
        contract_json = {
            "codeContractElement": "",
            "codeInsurer": ""
        }
        keycloak_user = "user"
        mode = COMPLET_MODE
        file_name = "test_file.json"

        with self.assertRaises(InvalidParameterException):
            deserializer.deserialize_json_item(contract_json, keycloak_user, mode, file_name)

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_try_deserialize_contract_element_fields_exception(self, mock_event_publisher, mock_contract_element_repository):
        deserializer = FileDeserializer()
        contract_json = {
            "codeContractElement": "",
            "codeInsurer": ""
        }
        keycloak_user = "user"
        mode = COMPLET_MODE
        file_name = "test_file.json"

        with self.assertRaises(InvalidParameterException):
            deserializer.try_deserialize_contract_element_fields(contract_json, keycloak_user, mode, file_name)

    @patch('bobbimportjob.services.file_deserializer.ContractElementRepository')
    @patch('bobbimportjob.services.file_deserializer.EventPublisher')
    def test_process_product_elements_invalid(self, mock_contract_element_repository,
                                              mock_event_publisher):
        deserializer = FileDeserializer()
        contract_json = {
            "productElements": [
                {
                    "codeOffer": "",
                    "codeProduct": "",
                    "codeBenefitNature": "",
                    "codeOC": "",
                    "from": ""
                }
            ]
        }
        mode = COMPLET_MODE
        file_name = "test_file.json"

        with self.assertRaises(InvalidParameterException):
            deserializer.process_product_elements(contract_json, mode, file_name)
