import os
from pathlib import Path

from beyondpythonframework.config.logging import get_beyond_logger
from commonomuhelper.omuhelper import read_output_parameters

from bobbimportjob.common.common_constants import (COMPLET_MODE, DIFFERENTIEL_MODE, LIST_ORDERED_FILES,
                                                   LIST_INPUT_FILES)
from bobbimportjob.common.report import Report
from bobbimportjob.repositories.contract_element_repository import ContractElementRepository
from .data_processor import DataProcessor
from .event_publisher_service import EventPublisher
from .file_deserializer import FileDeserializer
from .file_processor import FileProcessor

logger = get_beyond_logger()

INPUT_FOLDER = os.environ.get("INPUT_FOLDER")

CHUNK_SIZE = int(os.environ.get("CHUNK_SIZE", "1000"))


class ImportService:

    def __init__(self):
        self.data_processor = DataProcessor()
        self.file_deserializer = FileDeserializer()
        self.report = Report()
        self.event_publisher = EventPublisher()
        self.contract_element_repository = ContractElementRepository()

    def import_bobb(self, mode, forcage):
        """
        Imports BOBB files based on the specified mode and forcage parameters.

        :param mode: The mode of import (COMPLET, DIFFERENTIEL)
        :param forcage: Flag indicating whether to force import
        """
        self.report.mode = mode
        self.report.forcage = forcage
        state = 0
        logger.info("============ Starting bobb-import-job ============")
        try:
            if self.check_workdir_folder():
                list_files_names = read_output_parameters(LIST_ORDERED_FILES)

                if mode == COMPLET_MODE:
                    self.data_processor.check_volumetry(list_files_names, mode, forcage)

                self.contract_element_repository.ensure_index()
                self.contract_element_repository.migrate_data()
                self.process_files(list_files_names, mode, forcage)

        except Exception as e:
            logger.exception(e)
            state = 2

        finally:
            self.report.generate_crex()
            logger.info("============ bobb-import-job completed . ============")
            exit(state)

    def process_files(self, list_files_names, mode, forcage):
        """
        :param list_files_names: List of files name existing in input directory
        :param mode: Mode of import {COMPLET || DIFFERENTIEL}
        :param forcage: Mode of forcage {OUI || NON}
        :return:
        """
        if mode == COMPLET_MODE:
            first_file_name = list_files_names[0]
            self.report.list_input_files.append(first_file_name)
            self.import_file(first_file_name, mode, forcage)
        if mode == DIFFERENTIEL_MODE:
            self.report.list_input_files = list_files_names
            for file_name in list_files_names:
                self.import_file(file_name, mode, forcage)

    def import_file(self, file_name, mode, forcage):
        """
        Imports the specified file using the given mode and forcage parameters.

        :param file_name: The name of the file to import
        :param mode: The mode of import
        :param forcage: Flag indicating whether to force import
        :return: None
        """
        try:
            self.report.init_file_report()
            logger.info("==== Start import " + file_name + " ====")
            self.data_processor.delete_pending_contracts(file_name)

            self.process_batches(CHUNK_SIZE, file_name, mode)

            self.data_processor.commit_validated_rows(file_name, mode)

            if mode == COMPLET_MODE:
                self.report.total_lines_pending = 0

            self.event_publisher.publish_lines_integration_succeeded(file_name)

            self.event_publisher.publish_success_import_file_event(
                filename=file_name,
                mode=mode,
                forcage=forcage
            )
            self.event_publisher.publish_audit(file_name)

            self.report.list_files_ok.append(file_name)
            FileProcessor.move_file(file_name)
            FileProcessor.delete_file(file_name)
            input_files = read_output_parameters(LIST_INPUT_FILES)
            FileProcessor.delete_files_from_input_folder(input_files, file_name)
            self.report.generate_business_report()
            logger.info("==== End import " + file_name + " ====")
        except Exception as e:
            self.report.list_files_ko.append(file_name)
            self.data_processor.rollback_import(file_name)
            self.event_publisher.publish_failed_import_file_event(
                filename=file_name,
                mode=mode,
                forcage=forcage,
                error=str(e)
            )
            self.event_publisher.publish_audit(file_name)
            raise

    def process_batches(self, chunk_size, file_name, mode):
        logger.info(f"Starting file split: {file_name}")
        file_content_batches = FileProcessor.get_input_file_content(file_name, chunk_size)
        for batch_number, batch in enumerate(file_content_batches, start=1):
            logger.info(f"Start processing batch {batch_number} with {len(batch)} records")
            self.process_bobb_batch(batch, mode, file_name)

    def process_bobb_batch(self, content, mode, file_name):
        """
       Processes the content of BOBB file based on the specified mode.

       :param content: The content of the BOBB file to process.
       :param mode: The mode of processing (COMPLET, DIFFERENTIEL)

       :return: None
       """
        keycloak_user = ""
        keys_contract_elements_map = self.file_deserializer.deserialize_content(content, keycloak_user, mode, file_name)
        valid_data = self.data_processor.prepare_data(keys_contract_elements_map, file_name)
        if mode == COMPLET_MODE:
            self.data_processor.process_all_contracts_elements(valid_data)
        if mode == DIFFERENTIEL_MODE:
            self.data_processor.process_contract_elements_existent_in_file(valid_data)

    @staticmethod
    def check_workdir_folder():
        """
        Checks the working directory (Workdir) folder for files to process.

        :return: True if there are files to process, False otherwise.
        """
        files_workdir_count = len(os.listdir(Path(FileProcessor.get_workdir_folder())))
        if files_workdir_count < 1:
            logger.info("The workdir folder contains " + str(files_workdir_count) + " files. No files to process.")
            return False
        return True

    @staticmethod
    def sort_files_by_date():
        """
        Sorts the files in the work directory folder by their modification date.

        :return: A list of file names sorted by modification date.
        """
        list_files_names = os.listdir(Path(FileProcessor.get_workdir_folder()))
        sorted_files = sorted(list_files_names,
                              key=lambda x: os.path.getctime(os.path.join(FileProcessor.get_workdir_folder(), x)))
        return sorted_files
