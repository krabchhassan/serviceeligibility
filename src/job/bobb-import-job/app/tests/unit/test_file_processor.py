from pathlib import Path
from unittest.mock import patch, MagicMock, mock_open

from bobbimportjob.exceptions.file_exception import InputFileException
from bobbimportjob.services.file_processor import FileProcessor
import unittest


class TestFileProcessor(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.input_files = [
            'import_test_1.json',
            'import_test_2.json',
            'import_test_2.json.meta',
            'import_test_3.xlsx.meta',
            'import_test_1.json.meta',
            'import_test_3.xlsx'
        ]
        cls.file_name_to_delete = 'import_test_1.json'

    def setUp(self):
        self.mock_env_patcher = patch('os.environ.get', return_value='/mocked/path')
        self.mock_env_get = self.mock_env_patcher.start()
        self.addCleanup(self.mock_env_patcher.stop)

    def _mock_file_open(self, read_data):
        return patch('builtins.open', new_callable=mock_open, read_data=read_data)

    def _mock_path_exists(self, return_value):
        return patch('pathlib.Path.exists', return_value=return_value)

    def _mock_common_omu_helper(self):
        return patch('commonomuhelper.omuhelper.get_workdir_input_path', return_value='/mocked/path')

    def test_get_workdir_folder_env_var(self):
        result = FileProcessor.get_workdir_folder()
        self.assertEqual(result, '/mocked/path')
        self.mock_env_get.assert_called_once_with("INPUT_FOLDER")

    def test_check_input_file_format_valid(self):
        try:
            FileProcessor.check_input_file_format('valid_file.json')
        except InputFileException:
            self.fail("check_input_file_format raised InputFileException unexpectedly!")

    @patch('commonomuhelper.omuhelper.get_workdir_input_path', return_value='/mocked/path')
    def test_get_count_of_product_element_valid_json(self, mock_get_workdir_input_path):
        with self._mock_path_exists(True), \
                self._mock_file_open('[{"codeContractElement":"FSOC72066A","codeInsurer":"ASSU00060","codeAMC":"",'
                                     '"productElements":[{"codeOC":"ASSU00060","codeOffer":"FSC72065",'
                                     '"codeProduct":"FSOC72066A","codeBenefitNature":"LOCASSS","from":"2023-03-01",'
                                     '"effectiveDate":"2023-03-01","to":""},{"codeOC":"ASSU00060",'
                                     '"codeOffer":"FSC720652","codeProduct":"FSOC72066A2",'
                                     '"codeBenefitNature":"LOCASSS2",'
                                     '"from":"2023-03-01","effectiveDate":"2023-03-01","to":""}],"ignored":"false"}]'):
            result = FileProcessor.get_count_of_product_element('valid_file.json')
            self.assertEqual(result, 2)

    def test_check_input_file_format_invalid_extension(self):
        with self.assertRaises(InputFileException):
            FileProcessor.check_input_file_format('invalid_file.txt')

    @patch('commonomuhelper.omuhelper.get_workdir_input_path', return_value='/mocked/path')
    def test_get_count_of_product_element_file_not_found(self, mock_get_workdir_input_path):
        with self._mock_path_exists(False):
            with self.assertRaises(InputFileException):
                FileProcessor.get_count_of_product_element('non_existent_file.json')

    @patch('commonomuhelper.omuhelper.get_workdir_input_path', return_value='/mocked/path')
    def test_get_count_of_product_element_empty_json(self, mock_get_workdir_input_path):
        with self._mock_path_exists(True), \
                self._mock_file_open('{}'):
            result = FileProcessor.get_count_of_product_element('empty_file.json')
            self.assertEqual(result, 0)

    def test_get_input_file_content_file_not_found(self):
        with self._mock_path_exists(False):
            with self.assertRaises(InputFileException):
                list(FileProcessor.get_input_file_content('non_existent_file.json', 10))



    @patch('bobbimportjob.services.file_processor.logger')
    def test_delete_files_from_input_folder_no_match(self, mock_logger):
        # Mock delete_file_if_exists method
        FileProcessor.delete_file_if_exists = MagicMock(return_value=False)

        # Call the method under test with different file name to delete
        FileProcessor.delete_files_from_input_folder(self.input_files, 'non_existing_file.json')

        # Assertions based on mocked behavior
        mock_logger.info.assert_called_once_with('No files matching non_existing_file were found in input_files')

    def test_get_base_name(self):
        file_name = 'import_test_1.json'
        base_name = FileProcessor.get_base_name(file_name)
        self.assertEqual(base_name, 'import_test_1')

    def test_is_match(self):
        file_path = Path('C:/PEFB/BOBB/INPUT/import_test_1.json')
        self.assertTrue(FileProcessor.is_match(file_path, 'import_test_1'))
        self.assertFalse(FileProcessor.is_match(file_path, 'import_test_2'))

    @patch('bobbimportjob.services.file_processor.FileProcessor.delete_file_if_exists')
    def test_deletes_files_matching_base_name(self, mock_delete_file_if_exists):
        # Mock the delete_file_if_exists method to return True
        mock_delete_file_if_exists.return_value = True

        input_files = [
            'pefb/bobb/import/import_test_1.json',
            'pefb/bobb/import/import_test_2.json',
            'pefb/bobb/import/import_test_3.xlsx'
        ]
        file_name_to_delete = 'import_test_1.json'

        # Call the method under test
        FileProcessor.delete_files_from_input_folder(input_files, file_name_to_delete)

        # Assertions based on mocked behavior
        self.assertEqual(mock_delete_file_if_exists.call_count, 2)

    @patch('bobbimportjob.services.file_processor.FileProcessor.delete_file_if_exists')
    @patch('bobbimportjob.services.file_processor.logger')
    def test_no_files_matching_base_name(self, mock_logger, mock_delete_file_if_exists):
        # Mock the delete_file_if_exists method to return False
        mock_delete_file_if_exists.return_value = False

        input_files = [
            'pefb/bobb/import/import_test_2.json',
            'pefb/bobb/import/import_test_3.json',
            'pefb/bobb/import/import_test_4.json',
            'pefb/bobb/import/import_test_5.json'
        ]
        file_name_to_delete = 'import_test_1.json'

        # Call the method under test
        FileProcessor.delete_files_from_input_folder(input_files, file_name_to_delete)

        # Assertions based on mocked behavior
        mock_logger.info.assert_called_once_with('No files matching import_test_1 were found in input_files')
