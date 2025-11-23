import json
import logging
import unittest
from datetime import datetime
from unittest.mock import patch

from bobbimportjob.common.common_constants import COMPLET_MODE, NON
from bobbimportjob.common.common_constants import PRODUCT_ELEMENTS
from bobbimportjob.exceptions.volumetry_exception import VolumetryException
from bobbimportjob.models.contract_element import ContractElement
from bobbimportjob.models.product_element import ProductElement
from bobbimportjob.services.data_processor import DataProcessor
from bobbimportjob.logging import init_logging

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class TestDataProcessor(unittest.TestCase):
    def setUp(self):
        self.mock_env_patcher = patch('os.environ.get', return_value='unit/ressources/')
        self.mock_env_get = self.mock_env_patcher.start()
        self.addCleanup(self.mock_env_patcher.stop)

    @patch('bobbimportjob.services.data_processor.read_output_parameters')
    @patch('bobbimportjob.services.data_processor.datetime')  # Ajoutez cette ligne pour mocker datetime.now()
    def test_prepare_data(self, mock_datetime, mock_read_output_parameters):
        # Définissez la date de test
        datetime_test = datetime.strptime("2024-03-16", "%Y-%m-%d")
        # Définissez la date actuelle que vous voulez utiliser dans le test
        mock_datetime.now.return_value = datetime(2024, 6, 12, 17, 5, 48, 46692)

        contract_elements_map = {
            '1': ContractElement(
                _id='1',
                code_insurer='insurer1',
                code_contract_element='contract1',
                code_amc='amc1',
                label='label1',
                ignored=False,
                origine='origin1',
                user='user1',
                deadline=datetime_test,
                product_elements=[
                    ProductElement(
                        code_offer='offer1',
                        code_product='product1',
                        code_benefit_nature='benefit1',
                        code_oc='oc1',
                        from_date="2024-03-16",
                        to="2024-03-16",
                        effective_date="2024-03-16"
                    )
                ]
            )
        }

        mock_read_output_parameters.return_value = 'user1'

        expected_result = [
            {
                '_id': '1',
                'codeInsurer': 'insurer1',
                'codeContractElement': 'contract1',
                'codeAMC': 'amc1',
                'label': 'label1',
                'ignored': False,
                'origine': 'origin1',
                'user': 'user1',
                'deadline': datetime_test,
                'status': 'PENDING',
                'productElements': [
                    {
                        'codeOffer': 'offer1',
                        'codeProduct': 'product1',
                        'codeBenefitNature': 'benefit1',
                        'codeAmc': 'oc1',
                        'from': datetime_test,
                        'to': datetime_test,
                        'effectiveDate': datetime_test
                    }
                ],
                'insertDate': datetime(2024, 6, 12, 17, 5, 48, 46692),
                'fileName': 'file.json'
                # Ajoutez également la valeur attendue pour insertDate
            }
        ]

        result = DataProcessor.prepare_data(contract_elements_map, "file.json")
        self.assertEqual(result, expected_result)

    @patch('bobbimportjob.services.data_processor.RedisCacheService')
    @patch('bobbimportjob.services.file_processor.FileProcessor.get_input_file_content',
           return_value=json.load(open("unit/ressources/test_volumetry_success.json", encoding="utf8")))
    @patch('bobbimportjob.repositories.contract_element_repository.ContractElementRepository'
           '.contract_elements_count',
           return_value=100)
    @patch('bobbimportjob.services.event_publisher_service.EventPublisher.publish_failed_import_file_event')
    def test_check_volumetry_successfull(self, mock_publish_failed_import_file_event, mock_contract_elements_count,
                                         mock_get_input_file_content, mock_redis_cache_service):
        list_files_names = ["test_volumetry_success.json"]
        mode = COMPLET_MODE
        forcage = "NON"

        DataProcessor().check_volumetry(list_files_names, mode, forcage)

        mock_publish_failed_import_file_event.assert_not_called()

    # the default value of volumetry rate are 95%
    @patch('bobbimportjob.services.data_processor.RedisCacheService')
    @patch('bobbimportjob.repositories.contract_element_repository.ContractElementRepository'
           '.contract_elements_count',
           return_value=100)
    @patch('bobbimportjob.services.file_processor.FileProcessor.get_input_file_content',
           return_value=json.load(open("unit/ressources/test_volumetry_failed.json", encoding="utf8")))
    @patch('bobbimportjob.services.event_publisher_service.EventPublisher.publish_failed_import_file_event')
    def test_check_volumetry_failed(self, mock_publish_failed_import_file_event, mock_get_input_file_content,
                                    mock_contract_elements_count, mock_redis_cache_service):
        list_files_names = ["test_volumetry_failed.json"]
        mode = COMPLET_MODE
        forcage = "NON"
        with self.assertRaises(VolumetryException):
            DataProcessor().check_volumetry(list_files_names, mode, forcage)
        mock_publish_failed_import_file_event.assert_called_once_with(filename="test_volumetry_failed.json", mode=COMPLET_MODE, forcage=NON,
                                                                      error="La volumétrie n'est pas respectée : le "
                                                                            "nombre de lignes dans le fichier est "
                                                                            "inférieur"
                                                                            " au seuil minimum attendu.")

    @patch('bobbimportjob.services.data_processor.RedisCacheService')
    def test_find_added_elements(self, mock_redis_cache_service):
        contract_element_in_file = {
            "codeContractElement": "FAKE3145",
            "codeInsurer": "FAKE3145",
            "codeAMC": None,
            "productElements": [
                {
                    "codeAmc": "FAKE3145",
                    "codeOffer": "FAKE3145",
                    "codeProduct": "FAKE3145",
                    "codeBenefitNature": "",
                    "from": "2020-01-01",
                    "to": None,
                    "effectiveDate": "2020-01-01"
                }, {
                    "codeAmc": "FAKE3122",
                    "codeOffer": "FAKE3122",
                    "codeProduct": "FAKE3122",
                    "codeBenefitNature": "",
                    "from": "2020-01-01",
                    "to": None,
                    "effectiveDate": "2020-01-01"
                }, {
                    "codeAmc": "FAKE3344",
                    "codeOffer": "FAKE3344",
                    "codeProduct": "FAKE3344",
                    "codeBenefitNature": "",
                    "from": "2020-01-01",
                    "to": None,
                    "effectiveDate": "2020-01-01"
                }
            ],
            "ignored": "false"
        }
        stored_product_elements = {PRODUCT_ELEMENTS: []}

        elements = DataProcessor().find_added_elements(contract_element_in_file, stored_product_elements)

        self.assertEqual(len(elements["ADDED"]), 3)
        self.assertEqual(elements["ADDED"], contract_element_in_file.get(PRODUCT_ELEMENTS))

    @patch('bobbimportjob.services.data_processor.RedisCacheService')
    def test_find_updated_elements(self, mock_redis_cache_service):
        contract_element_in_file = {
            "codeContractElement": "FAKE3145",
            "codeInsurer": "FAKE3145",
            "codeAMC": None,
            "productElements": [
                {
                    "codeAmc": "123",
                    "codeOffer": "123",
                    "codeProduct": "123",
                    "codeBenefitNature": "",
                    "from": "2023-01-01",
                    "to": None,
                    "effectiveDate": "2023-01-01"
                }, {
                    "codeAmc": "456",
                    "codeOffer": "456",
                    "codeProduct": "456",
                    "codeBenefitNature": "",
                    "from": "2023-01-01",
                    "to": None,
                    "effectiveDate": "2023-01-01"
                }
            ],
            "ignored": "false"
        }
        contract_element_stored = {
            "codeContractElement": "FAKE3145",
            "codeInsurer": "FAKE3145",
            "codeAMC": None,
            "productElements": [
                {
                    "codeAmc": "123",
                    "codeOffer": "123",
                    "codeProduct": "123",
                    "codeBenefitNature": "",
                    "from": "2023-01-01",
                    "to": None,
                    "effectiveDate": "2023-01-01"
                }, {
                    "codeAmc": "456",
                    "codeOffer": "456",
                    "codeProduct": "456",
                    "codeBenefitNature": "",
                    "from": "2023-01-01",
                    "to": None,
                    "effectiveDate": "2023-01-01"
                }, {
                    "codeAmc": "789",
                    "codeOffer": "789",
                    "codeProduct": "789",
                    "codeBenefitNature": "",
                    "from": "2023-01-01",
                    "to": None,
                    "effectiveDate": "2023-01-01"
                }
            ],
            "ignored": "false"
        }

        elements = DataProcessor().find_updated_elements(contract_element_in_file, contract_element_stored)
        self.assertEqual(len(elements["UPDATED"]) + len(elements["NO CHANGES"]), 2)

    @patch('bobbimportjob.services.data_processor.RedisCacheService')
    def test_find_deleted_elements(self, mock_redis_cache_service):
        file_product_elements = [("456", datetime(2023, 1, 1), "456")]
        contract_element_stored = {
            "codeContractElement": "FAKE3145",
            "codeInsurer": "FAKE3145",
            "codeAMC": None,
            "productElements": [
                {
                    "codeAmc": "123",
                    "codeOffer": "123",
                    "codeProduct": "123",
                    "codeBenefitNature": "",
                    "from": datetime(2023, 1, 1),
                    "to": None,
                    "effectiveDate": datetime(2023, 1, 1)
                }, {
                    "codeAmc": "456",
                    "codeOffer": "456",
                    "codeProduct": "456",
                    "codeBenefitNature": "BenefitNature1",
                    "from": datetime(2023, 1, 1),
                    "to": None,
                    "effectiveDate": datetime(2023, 1, 1)
                }
            ],
            "ignored": "false"
        }

        elements = DataProcessor().find_deleted_elements(file_product_elements, contract_element_stored)
        self.assertEqual(len(elements["DEPRECIATE"]), 1)
