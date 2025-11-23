import os
import unittest
from datetime import datetime
from unittest.mock import MagicMock, patch

from bobbexportjob.common.common_constants import OUTPUT_FILENAME, EXPORT_FILENAME
from bobbexportjob.models.contract_element import ContractElement
from bobbexportjob.models.product_element import ProductElement
from bobbexportjob.services.process_contract_element_service import ProcessContractElementService


class TestProcessContractElementService(unittest.TestCase):
    def setUp(self):
        self.max_rows_per_file = 50
        self.input_file_name = OUTPUT_FILENAME
        self.process_contract_element_service = ProcessContractElementService(self.max_rows_per_file,
                                                                              self.input_file_name)

    def test_get_input_file_path(self):
        expected_path = "/bobb/input/export_contract_element.json"
        with patch('bobbexportjob.services.process_contract_element_service.get_workdir_output_path',
                   return_value="/bobb/input/"):
            self.assertEqual(self.process_contract_element_service.get_input_file_path(), expected_path)

    @patch('os.environ.get')
    def test_generate_filename(self, mock_get_env):
        mock_get_env.return_value = 'dev-evol2'
        current_datetime = datetime(2024, 2, 8, 12, 0, 0)
        with patch('bobbexportjob.services.process_contract_element_service.datetime') as mock_datetime:
            mock_datetime.now.return_value = current_datetime
            filename = self.process_contract_element_service.generate_filename()
        expected_filename = "Export_bobb_08022024-12-00-00_dev-evol2_1.xlsx"
        self.assertEqual(filename, expected_filename)

    def test_create_contract_element(self):
        contract_data = {
            "codeAMC": "BALOO",
            "codeInsurer": "ASS_BALOO",
            "codeContractElement": "BAL_BASMOD010",
            "productElements": [
                {
                    "codeOffer": "BLUE_TP",
                    "codeProduct": "SANTE_100",
                    "codeBenefitNature": "OPTIQUE",
                    "codeAmc": "BALOO",
                    "from": "2020-01-01",
                    "effectiveDate": "2020-01-01"
                },
                {
                    "codeOffer": "BLUE_TP",
                    "codeProduct": "SANTE_100",
                    "codeBenefitNature": "PHARMACIE",
                    "codeAmc": "BALOO",
                    "from": "2022-07-01",
                    "effectiveDate": "2020-01-01"
                }
            ],
            "ignored": "false",
            "origine": "IMPORT"
        }

        contract_element = self.process_contract_element_service.create_contract_element(contract_data)

        self.assertEqual(contract_element.code_amc, "BALOO")
        self.assertEqual(contract_element.code_insurer, "ASS_BALOO")
        self.assertEqual(contract_element.code_contract_element, "BAL_BASMOD010")
        self.assertEqual(len(contract_element.product_elements), 2)
        self.assertEqual(contract_element.ignored, "false")

    def test_create_product_element(self):
        product_data = [
            {
                "codeOffer": "BLUE_TP",
                "codeProduct": "SANTE_100",
                "codeBenefitNature": "OPTIQUE",
                "codeAmc": "BALOO",
                "from": "2020-01-01",
                "effectiveDate": "2020-01-01"
            },
            {
                "codeOffer": "BLUE_TP",
                "codeProduct": "SANTE_100",
                "codeBenefitNature": "PHARMACIE",
                "codeAmc": "BALOO",
                "from": "2022-07-01",
                "effectiveDate": "2020-01-01"
            }
        ]

        for data in product_data:
            product_element = self.process_contract_element_service.create_product_element(data)

            self.assertEqual(product_element.code_offer, data["codeOffer"])
            self.assertEqual(product_element.code_product, data["codeProduct"])
            self.assertEqual(product_element.code_benefit_nature, data["codeBenefitNature"])
            self.assertEqual(product_element.code_amc, data["codeAmc"])
            self.assertEqual(product_element.from_date, data["from"])
            self.assertEqual(product_element.effective_date, data["effectiveDate"])

    def test_process_contract_with_product_elements(self):
        contract_element = ContractElement(
            code_contract_element="CONTRACT1",
            code_insurer="INSURER1",
            code_amc="AMC1",
            product_elements=[
                ProductElement(
                    code_amc="AMC1",
                    code_offer="OFFER1",
                    code_product="PRODUCT1",
                    code_benefit_nature="NATURE1",
                    from_date="2022-01-01",
                    to_date="2022-12-31",
                    effective_date="2022-01-01"
                ),
                ProductElement(
                    code_amc="AMC1",
                    code_offer="OFFER2",
                    code_product="PRODUCT2",
                    code_benefit_nature="NATURE2",
                    from_date="2022-01-01",
                    to_date="2022-12-31",
                    effective_date="2022-01-01"
                )
            ],
            ignored="false"
        )

        self.process_contract_element_service.ws.append = MagicMock()
        self.process_contract_element_service.process_contract_with_product_elements(contract_element)

        self.assertEqual(self.process_contract_element_service.ws.append.call_count, 2)

        # Define the expected row data for each product element
        expected_calls = [
            [
                "CONTRACT1", "INSURER1", "AMC1", "AMC1", "OFFER1", "PRODUCT1", "NATURE1",
                "2022-01-01", "2022-12-31", "2022-01-01", "false"
            ],
            [
                "CONTRACT1", "INSURER1", "AMC1", "AMC1", "OFFER2", "PRODUCT2", "NATURE2",
                "2022-01-01", "2022-12-31", "2022-01-01", "false"
            ]
        ]

        # Verify that the expected row data was appended to the worksheet
        for call_args, expected_row_data in zip(self.process_contract_element_service.ws.append.call_args_list,
                                                expected_calls):
            self.assertEqual(call_args[0][0], expected_row_data)

    def test_process_contract_without_product_elements(self):
        contract_element = ContractElement(
            code_contract_element="CONTRACT1",
            code_insurer="INSURER1",
            code_amc="AMC1",
            product_elements=[],  # No product elements
            ignored="false"
        )

        self.process_contract_element_service.ws.append = MagicMock()
        self.process_contract_element_service.process_contract_without_product_elements(contract_element)
        self.assertEqual(self.process_contract_element_service.row_count, 1)

        expected_row_data = [
            "CONTRACT1", "INSURER1", "AMC1", "", "", "", "", "", "", "", "false"
        ]
        self.process_contract_element_service.ws.append.assert_called_once_with(expected_row_data)

    @patch.object(ProcessContractElementService, 'generate_output_file_path')
    @patch('bobbexportjob.services.process_contract_element_service.EventPublisherService')
    def test_create_new_excel_file(self, mock_event_publisher, mock_generate_output_file_path):
        self.process_contract_element_service.wb = MagicMock()
        self.process_contract_element_service.ws = MagicMock()

        mock_generate_output_file_path.return_value = '/mocked/output/Export_bobb_06022024-14-45-28_dev-evol2_1.xlsx'

        self.process_contract_element_service.row_count = 50
        self.process_contract_element_service.file_count = 2

        with patch.object(self.process_contract_element_service.wb, 'save') as mock_save:
            self.process_contract_element_service.create_new_excel_file()
            self.assertEqual(self.process_contract_element_service.row_count, 0)
            self.assertEqual(self.process_contract_element_service.file_count, 3)
            mock_save.assert_called_once_with('/mocked/output/Export_bobb_06022024-14-45-28_dev-evol2_1.xlsx')
            mock_event_instance = mock_event_publisher.return_value
            mock_event_instance.publish_success_event.assert_called_once_with(EXPORT_FILENAME)

    @patch.object(ProcessContractElementService, 'get_input_file_path')
    @patch.object(ProcessContractElementService, 'generate_output_file_path')
    @patch('builtins.open', new_callable=MagicMock)
    @patch('json.load')
    @patch('bobbexportjob.services.process_contract_element_service.get_workdir_output_path')
    @patch('bobbexportjob.services.process_contract_element_service.EventPublisherService')
    @patch('bobbexportjob.services.process_contract_element_service.Workbook')
    def test_process_contract_data(self, mock_Workbook, mock_EventPublisherService,
                                   mock_get_workdir_output_path, mock_json_load, mock_open,
                                   mock_get_input_file_path, mock_generate_output_file_path):

        mock_get_input_file_path.return_value = '/mocked/input/export_contract_element.json'
        mock_get_workdir_output_path.return_value = "/bobb/input/"
        mock_generate_output_file_path.return_value = '/mocked/output/Export_bobb_06022024-14-45-28_dev-evol2_1.xlsx'

        json_data = [
            {
                "codeAMC": "BALOO",
                "codeInsurer": "ASS_BALOO",
                "codeContractElement": "BAL_BASMOD010",
                "productElements": [
                    {
                        "codeOffer": "BLUE_TP",
                        "codeProduct": "SANTE_100",
                        "codeBenefitNature": "OPTIQUE",
                        "codeAmc": "BALOO",
                        "from": "2020-01-01",
                        "effectiveDate": "2020-01-01"
                    }
                ],
                "ignored": "false",
                "origine": "IMPORT"
            }
        ]

        mock_json_load.return_value = json_data

        process_contract_element_service = ProcessContractElementService(max_rows_per_file=50,
                                                                         input_file_name="export_contract_element.json")
        process_contract_element_service.process_contract_data(json_data)

        mock_Workbook().save.assert_called_once()
        mock_EventPublisherService().publish_success_event.assert_called_once()
