from os import environ
import unittest
from unittest.mock import patch, MagicMock

from bobbimportjob.services.import_service import ImportService


class TestImportService(unittest.TestCase):

    def setUp(self):
        self.mock_env_patcher = patch('os.environ.get', return_value='unit/ressources/')
        self.mock_env_get = self.mock_env_patcher.start()
        self.addCleanup(self.mock_env_patcher.stop)

    @patch('bobbimportjob.services.import_service.read_output_parameters')
    @patch('bobbimportjob.services.import_service.DataProcessor')
    @patch(
        'bobbimportjob.services.import_service.ImportService.process_batches')
    @patch(
        'bobbimportjob.services.event_publisher_service.EventPublisher.publish_success_import_file_event')
    @patch(
        'bobbimportjob.services.event_publisher_service.EventPublisher.publish_lines_integration_succeeded')
    @patch(
        'bobbimportjob.services.event_publisher_service.EventPublisher.publish_audit')
    @patch('bobbimportjob.services.file_processor.FileProcessor.delete_file')
    def test_import_file_success(self, mock_delete_file, mock_publish_audit, mock_publish_lines_integration_succeeded,
                                 mock_publish_success_import_file_event,
                                 mock_process_bobb_batches, mock_data_processor,
                                 mock_read_output_parameters):
        mock_read_output_parameters.return_value = {"list_input_files": ["file1", "file2"]}
        mock_import_service = ImportService()
        mock_import_service.report = MagicMock()
        mock_import_service.data_processor = mock_data_processor
        mock_import_service.process_batches = MagicMock()
        mock_import_service.process_batches.return_value = None

        mock_import_service.import_file("file_name.json", "COMPLET", "OUI")

        mock_import_service.process_batches.assert_called_once_with(1000, "file_name.json", "COMPLET")
        mock_publish_success_import_file_event.assert_called_once_with(filename="file_name.json", mode="COMPLET",
                                                                       forcage="OUI")
        mock_publish_lines_integration_succeeded.assert_called_once_with("file_name.json")
        mock_publish_audit.assert_called_once_with("file_name.json")
        mock_delete_file.assert_called_once_with("file_name.json")

    @patch('bobbimportjob.services.import_service.DataProcessor')
    @patch('bobbimportjob.services.import_service.ImportService.process_batches',
           side_effect=Exception("File not found"))
    @patch('bobbimportjob.services.event_publisher_service.EventPublisher.publish_failed_import_file_event')
    def test_import_file_failed(self, mock_publish_failed_import_file_event, mock_process_batches,
                                mock_data_processor):
        file_name = "file1"
        mode = "mode1"
        forcage = False

        with self.assertRaises(Exception):
            ImportService().import_file(file_name, mode, forcage)

        mock_process_batches.assert_called_once_with(1000, file_name, mode)
        mock_publish_failed_import_file_event.assert_called_once_with(filename=file_name, mode=mode, forcage=forcage,
                                                                      error="File not found")

    @patch('os.listdir')
    def test_no_files(self, mock_listdir):
        mock_listdir.return_value = []

        result = ImportService.check_workdir_folder()

        self.assertFalse(result)

    @patch('os.listdir')
    def test_with_files(self, mock_listdir):
        mock_listdir.return_value = ['file1.json', 'file2.json']

        result = ImportService.check_workdir_folder()

        self.assertTrue(result)

