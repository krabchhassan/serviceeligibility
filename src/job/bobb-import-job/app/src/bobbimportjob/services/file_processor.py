import json
import os
import logging
from pathlib import Path
import shutil

import ijson

from bobbimportjob.exceptions.file_exception import InputFileException
from commonomuhelper.omuhelper import get_workdir_input_path, get_workdir_output_path
from bobbimportjob.common.common_constants import (JSON_FILE_EXTENSION)
from bobbimportjob.logging import init_logging

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class FileProcessor:
    @staticmethod
    def get_workdir_folder():
        input_folder = os.environ.get("INPUT_FOLDER")
        return input_folder if input_folder else get_workdir_input_path()

    @staticmethod
    def get_workdir_output_folder():
        input_folder = os.environ.get("OUTPUT_FOLDER")
        return input_folder if input_folder else get_workdir_output_path()

    @staticmethod
    def move_file(file_name):
        """
        Copies the specified file from input folder to output folder.

        :param file_name: The name of the file to copy.
        """
        try:
            workdir_input_folder = FileProcessor.get_workdir_folder()
            workdir_output_folder = FileProcessor.get_workdir_output_folder()
            input_file_path = Path(os.path.join(workdir_input_folder, file_name))
            output_file_path = Path(os.path.join(workdir_output_folder, file_name))
            shutil.copy(input_file_path, output_file_path)
            logger.info(f"{file_name} copied from input to output folder")
        except Exception as e:
            logger.error(str(e))

    @staticmethod
    def check_input_file_format(filename):
        """
        Checks if the input file format is valid JSON.

        :param filename: The name of the file to check.
        :raises InputFileException: If the file format is invalid.
        """
        if not filename.endswith(JSON_FILE_EXTENSION):
            raise InputFileException("Le format du fichier d'entrée est invalide")

    @staticmethod
    def get_count_of_product_element(input_file_name):
        workdir_folder = FileProcessor.get_workdir_folder()
        input_path = Path(os.path.join(workdir_folder, input_file_name))

        FileProcessor.check_input_file_format(input_file_name)

        if not input_path.exists():
            raise InputFileException(f"File {input_path} not found.")

        try:
            total_product_elements = 0
            with open(input_path, 'r', encoding='utf-8') as json_file:
                logger.debug("File opened successfully.")
                parser = ijson.parse(json_file)
                for prefix, event, value in parser:
                    if prefix.endswith('.productElements.item') and event == 'start_map':
                        total_product_elements += 1
                return total_product_elements
        except Exception as e:
            logger.error(f"Error reading file {input_path}: {e}")
            raise InputFileException(f"Error reading file: {e}")

    @staticmethod
    def get_input_file_content(input_file_name, batch_size):
        """
        Reads the content of the input file and returns it in batches.

        :param input_file_name: The name of the input file.
        :param batch_size: The size of the batches to return.
        :return: A generator yielding batches of JSON objects.
        :raises InputFileException: If the input file is not found or is invalid JSON.
        """
        workdir_folder = FileProcessor.get_workdir_folder()
        input_path = Path(os.path.join(workdir_folder, input_file_name))
        FileProcessor.check_input_file_format(input_file_name)
        if not input_path.exists():
            raise InputFileException(f"File {input_path} not found.")

        try:
            with open(input_path, 'r', encoding='utf-8') as file_stream:
                logger.debug("File opened successfully.")

                parser = ijson.items(file_stream, 'item')
                batch = []
                for item in parser:
                    batch.append(item)
                    if len(batch) >= batch_size:
                        yield batch
                        batch = []

                if batch:
                    yield batch

        except Exception as e:
            logger.error(f"Error reading file {input_path}: {e}")
            raise InputFileException(f"Error reading file: {e}")

    @staticmethod
    def delete_file(file_name):
        """
       Deletes the specified file.

       :param file_name: The name of the file to delete.
       """
        try:
            workdir_folder = FileProcessor.get_workdir_folder()
            file_path = Path(os.path.join(workdir_folder, file_name))
            os.remove(file_path)
            logger.info(file_name + " deleted")
        except Exception as e:
            logger.error(str(e))

    @staticmethod
    def delete_files_from_input_folder(input_files, file_name):
        try:
            base_name_to_delete = FileProcessor.get_base_name(file_name)
            input_paths = [Path(file) for file in input_files]
            files_deleted = False

            for file_path in input_paths:
                if FileProcessor.is_match(file_path, base_name_to_delete):
                    FileProcessor.delete_file_if_exists(file_path)
                    # Check and delete files with .meta extension
                    meta_file_path = Path(str(file_path) + '.meta')
                    FileProcessor.delete_file_if_exists(meta_file_path)
                    files_deleted = True

            if not files_deleted:
                logger.info(f"No files matching {base_name_to_delete} were found in input_files")

        except Exception as e:
            logger.error(f"Error in delete_files_from_input_folder: {e}")

    @staticmethod
    def get_base_name(file_name):
        return file_name.split('.')[0]

    @staticmethod
    def is_match(file_path, base_name_to_delete):
        return file_path.name.split('.')[0] == base_name_to_delete

    @staticmethod
    def delete_file_if_exists(file_path):
        try:
            if file_path.exists():
                os.remove(file_path)
                logger.info(f"{file_path} deleted")
                return True
            else:
                logger.warning(f"{file_path} does not exist")
                return False
        except PermissionError as e:
            logger.error(f"PermissionError: Could not delete {file_path} - {e}")
            return False
        except FileNotFoundError as e:
            logger.error(f"FileNotFoundError: {file_path} not found - {e}")
            return False
        except Exception as e:
            logger.error(f"Error deleting {file_path}: {e}")
            return False
