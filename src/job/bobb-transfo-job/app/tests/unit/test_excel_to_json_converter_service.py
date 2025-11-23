import os
import unittest
from unittest import mock
from unittest.mock import patch, MagicMock

from bobbtransfojob.common.common_constants import CODE_CONTRACT_ELEMENT, CODE_INSURER, CODE_AMC, CODE_OC, CODE_OFFER, \
    CODE_PRODUCT, CODE_BENEFIT_NATURE, FROM_DATE, TO_DATE, EFFECTIVE_DATE, IGNORED, ASC
from bobbtransfojob.services.excel_to_json_converter_service import ExcelToJsonConverterService


class TestExcelToJsonConverterService(unittest.TestCase):
    def setUp(self):
        self.input_folder = "input_folder"
        self.output_folder = "output_folder"
        self.service = ExcelToJsonConverterService(self.input_folder, self.output_folder)

    def test_converts_valid_excel_to_json_successfully(self):
        import os
        from unittest.mock import patch, mock_open, MagicMock
        from bobbtransfojob.services.excel_to_json_converter_service import ExcelToJsonConverterService

        input_folder = "input_files"
        output_folder = "output_files"
        service = ExcelToJsonConverterService(input_folder, output_folder)

        input_file_path = "input_files/test.xlsx"
        filename = "test.xlsx"

        mock_data = [
            {CODE_CONTRACT_ELEMENT: "CE1", CODE_INSURER: "INS1", CODE_AMC: "AMC1", CODE_OC: "OC1", CODE_OFFER: "OFF1", CODE_PRODUCT: "PROD1", CODE_BENEFIT_NATURE: "BN1", FROM_DATE: "2023-01-01", TO_DATE: "2023-12-31", EFFECTIVE_DATE: "2023-01-01", IGNORED: ""},
            {CODE_CONTRACT_ELEMENT: "CE1", CODE_INSURER: "INS1", CODE_AMC: "AMC1", CODE_OC: "OC2", CODE_OFFER: "OFF2", CODE_PRODUCT: "PROD2", CODE_BENEFIT_NATURE: "BN2", FROM_DATE: "2023-01-01", TO_DATE: "2023-12-31", EFFECTIVE_DATE: "2023-01-01", IGNORED: ""}
        ]

        with patch("builtins.open", mock_open()) as mocked_file, \
                patch("openpyxl.load_workbook") as mocked_load_workbook, \
                patch("os.path.exists") as mocked_exists, \
                patch("os.makedirs") as mocked_makedirs:

            mocked_exists.return_value = True
            mocked_workbook = MagicMock()
            mocked_sheet = MagicMock()
            mocked_load_workbook.return_value = mocked_workbook
            mocked_workbook.active = mocked_sheet
            mocked_sheet.iter_rows.return_value = iter([mock_data[0].keys()] + [list(row.values()) for row in mock_data])

            service.excel_to_json(input_file_path, filename)

            mocked_file.assert_called_with(os.path.join(output_folder, "test.json"), 'w')
            handle = mocked_file()
            handle.write.assert_any_call('[')
            handle.write.assert_any_call(']')
            self.assertTrue(mocked_file().write.called)

    @patch('bobbtransfojob.services.excel_to_json_converter_service.EventPublisherService')
    @patch('bobbtransfojob.services.excel_to_json_converter_service.ExcelToJsonConverterService.process_excel_file')
    @patch('bobbtransfojob.services.excel_to_json_converter_service.ExcelToJsonConverterService.process_json_file')
    @patch('os.listdir')
    def test_process_files_in_folder(self, mock_listdir, mock_process_json_file, mock_process_excel_file, MockEventPublisherService):
        # Prepare the mock values
        mock_listdir.return_value = ["test.xlsx", "test.json"]
        mock_is_excel_file_empty = MagicMock(return_value=False)
        mock_is_json_file_empty = MagicMock(return_value=False)
        self.service.is_excel_file_empty = mock_is_excel_file_empty
        self.service.is_json_file_empty = mock_is_json_file_empty

        # Call the method under test
        self.service.process_files_in_folder(self.input_folder)

        # Assertions
        mock_listdir.assert_called_once_with(self.input_folder)



    @patch('bobbtransfojob.services.excel_to_json_converter_service.EventPublisherService')
    @patch('builtins.open', new_callable=unittest.mock.mock_open)
    @patch('bobbtransfojob.services.excel_to_json_converter_service.json.dump')
    def test_process_excel_file_success(self, mock_json_dump, mock_open, MockEventPublisherService):
        input_folder = '/bobb/input'
        output_folder = '/bobb/output'
        excel_filename = 'test.xlsx'
        excel_file_path = os.path.join(input_folder, excel_filename)
        excel_data = [("CONTRACT1", "INSURER1", "AMC1", "amc1", "OFFRE1", "PRODUCT1", "BenefitNature1", "2024-01-01",
                       "2024-12-31", "2024-01-01", "False")]
        mock_logger = MagicMock()
        mock_event_publisher = MagicMock()
        mock_excel_to_json = MagicMock(return_value=excel_data)
        mock_check_excel_columns = MagicMock(return_value=True)
        mock_is_excel_sorted = MagicMock(return_value=True)

        instance_under_test = ExcelToJsonConverterService(input_folder, output_folder)
        instance_under_test.excel_to_json = mock_excel_to_json
        instance_under_test.check_excel_columns = mock_check_excel_columns
        instance_under_test.is_excel_sorted = mock_is_excel_sorted

        instance_under_test.process_excel_file(excel_filename)

        mock_excel_to_json.assert_called_once_with(excel_file_path, excel_filename)
        # mock_json_dump.assert_called_once_with(excel_data, mock_open(), indent=4)
        MockEventPublisherService.return_value.publish_success_event.assert_called_once_with(excel_filename)

    @patch('bobbtransfojob.services.excel_to_json_converter_service.EventPublisherService')
    @patch('builtins.open', new_callable=unittest.mock.mock_open)
    @patch('bobbtransfojob.services.excel_to_json_converter_service.json.dump')
    def test_process_excel_file_failure(self, mock_json_dump, mock_open, MockEventPublisherService):
        # Mocking necessary objects and methods
        input_folder = '/bobb/input'
        output_folder = '/bobb/output'
        excel_filename = 'test.xlsx'
        excel_file_path = os.path.join(input_folder, excel_filename)
        mock_event_publisher = MagicMock()
        mock_excel_to_json = MagicMock(side_effect=Exception('Test Exception'))
        mock_check_excel_columns = MagicMock(return_value=True)
        mock_is_excel_sorted = MagicMock(return_value=True)

        instance_under_test = ExcelToJsonConverterService(input_folder, output_folder)
        instance_under_test.excel_to_json = mock_excel_to_json
        instance_under_test.check_excel_columns = mock_check_excel_columns
        instance_under_test.is_excel_sorted = mock_is_excel_sorted
        instance_under_test.process_excel_file(excel_filename)

        mock_excel_to_json.assert_called_once_with(excel_file_path, excel_filename)
        MockEventPublisherService.return_value.publish_failed_event.assert_called_once()

    @patch('bobbtransfojob.services.excel_to_json_converter_service.shutil.copy')
    @patch('bobbtransfojob.services.excel_to_json_converter_service.logger.info')
    def test_process_json_file_success(self, mock_logger_info, mock_shutil_copy):
        input_folder = '/bobb/input'
        output_folder = '/bobb/output'
        json_filename = 'test.json'
        json_file_path = os.path.join(input_folder, json_filename)
        mock_instance = MagicMock()
        mock_instance.input_folder = input_folder
        mock_instance.output_folder = output_folder
        mock_instance.list_json_file = []
        mock_is_excel_sorted = MagicMock(return_value=True)

        instance_under_test = ExcelToJsonConverterService(input_folder, output_folder)
        instance_under_test.list_json_file = mock_instance.list_json_file
        instance_under_test.is_excel_sorted = mock_is_excel_sorted

        instance_under_test.process_json_file(json_filename)

        mock_shutil_copy.assert_called_once_with(json_file_path, os.path.join(output_folder, json_filename))
        mock_logger_info.assert_called_once_with(
            f"Copied JSON file to output folder: {os.path.join(output_folder, json_filename)}")
        self.assertIn(json_filename, instance_under_test.list_json_file)

    @patch('bobbtransfojob.services.excel_to_json_converter_service.shutil.copy',
           side_effect=Exception('Test Exception'))
    @patch('bobbtransfojob.services.excel_to_json_converter_service.logger.error')
    def test_process_json_file_failure(self, mock_logger_error, mock_shutil_copy_exception):
        input_folder = '/bobb/input'
        output_folder = '/bobb/output'
        json_filename = 'test.json'
        json_file_path = os.path.join(input_folder, json_filename)
        mock_instance = MagicMock()
        mock_instance.input_folder = input_folder
        mock_instance.output_folder = output_folder
        mock_instance.list_json_file = []

        instance_under_test = ExcelToJsonConverterService(input_folder, output_folder)
        instance_under_test.list_json_file = mock_instance.list_json_file

        instance_under_test.process_json_file(json_filename)

        mock_shutil_copy_exception.assert_called_once_with(json_file_path, os.path.join(output_folder, json_filename))
        mock_logger_error.assert_called_once()
        self.assertNotIn(json_filename, instance_under_test.list_json_file)

    def test_delete_file(self):
        input_folder = "input_folder"
        output_folder = "output_folder"
        file_path = "test_file.xlsx"

        service = ExcelToJsonConverterService(input_folder, output_folder)

        with mock.patch('os.remove') as mock_remove:
            service.delete_file(file_path)
            mock_remove.assert_called_once_with(file_path)

    @patch("bobbtransfojob.services.excel_to_json_converter_service.load_workbook")
    def test_is_excel_file_empty_empty_file(self, mock_load_workbook):
        # Mock the workbook and the active sheet
        mock_workbook = MagicMock()
        mock_sheet = MagicMock()
        mock_workbook.active = mock_sheet
        mock_load_workbook.return_value = mock_workbook

        # Simulate the sheet having only 1 row (empty or header)
        mock_sheet.iter_rows.return_value = iter([
            (None,)  # First row, which could be empty or just a header
        ])

        file_path = "test.xlsx"
        is_empty = self.service.is_excel_file_empty(file_path)

        self.assertTrue(is_empty)
        mock_load_workbook.assert_called_once_with(filename=file_path, read_only=True)

    @patch("bobbtransfojob.services.excel_to_json_converter_service.load_workbook")
    def test_is_excel_file_empty_non_empty_file(self, mock_load_workbook):
        # Mock the workbook and the active sheet
        mock_workbook = MagicMock()
        mock_sheet = MagicMock()
        mock_workbook.active = mock_sheet
        mock_load_workbook.return_value = mock_workbook

        # Simulate the sheet having exactly 2 rows
        mock_sheet.iter_rows.return_value = iter([
            (None,),  # First row, could be header or empty
            ("Some data",)  # Second row with actual data
        ])

        file_path = "test.xlsx"
        is_empty = self.service.is_excel_file_empty(file_path)

        self.assertFalse(is_empty)
        mock_load_workbook.assert_called_once_with(filename=file_path, read_only=True)

    @patch("bobbtransfojob.services.excel_to_json_converter_service.open")
    def test_is_json_file_empty_empty_file(self, mock_open):
        mock_file = MagicMock()
        mock_file.__enter__.return_value = mock_file
        mock_file.read.return_value = ""

        mock_open.return_value = mock_file

        file_path = "test.json"
        is_empty = self.service.is_json_file_empty(file_path)

        self.assertTrue(is_empty)
        mock_open.assert_called_once_with(file_path, 'r')

    @patch("bobbtransfojob.services.excel_to_json_converter_service.open")
    def test_is_json_file_empty_non_empty_file(self, mock_open):
        mock_file = MagicMock()
        mock_file.__enter__.return_value = mock_file

        non_empty_json_object = '{"key": "value"}'
        mock_file.read.return_value = non_empty_json_object

        mock_open.return_value = mock_file

        file_path = "test.json"
        is_empty = self.service.is_json_file_empty(file_path)

        self.assertFalse(is_empty)
        mock_open.assert_called_once_with(file_path, 'r')


    def test_is_out_of_order_asc(self):
        self.assertFalse(
            self.service.is_out_of_order(None, None, "123abc", "insurer001", ASC),
            "La première ligne ne devrait jamais être considérée comme désordonnée."
        )
        self.assertFalse(
            self.service.is_out_of_order("123abc", "insurer001", "123def", "insurer002", ASC),
            "Les chaînes triées correctement ne devraient pas être considérées comme désordonnées."
        )
        self.assertTrue(
            self.service.is_out_of_order("123abc", "insurer002", "122abc", "insurer001", ASC),
            "Un contrat trié en sens inverse doit être considéré comme désordonné."
        )
        self.assertFalse(
            self.service.is_out_of_order("00123xyz", "insurer1", "00124xyz", "insurer2", ASC),
            "Les contrats avec des zéros initiaux triés correctement ne devraient pas être désordonnés."
        )
        self.assertTrue(
            self.service.is_out_of_order("a123", "insurer2", "123xyz", "insurer1", ASC),
            "Un mélange de contrats et d'assureurs triés en sens inverse doit être considéré comme désordonné."
        )

