import logging

from commonomuhelper.omuhelper import produce_output_parameters

from bobbimportjob.common.common_constants import (DEPRECIATE, ADDED, UPDATED, NO_CHANGES, DIFFERENTIEL_MODE,
                                       COUNT_FILES_OK, LIST_FILES_OK,
                                       COUNT_FILES_KO, LIST_FILES_KO, COUNT_LINES_OK, COUNT_LINES_KO)

from bobbimportjob.logging import init_logging
LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


class Report:
    _instance = None

    def __new__(cls, *args, **kwargs):
        if cls._instance is None:
            cls._instance = super(Report, cls).__new__(cls)
            cls._instance.list_input_files = []
            cls._instance.list_files_ok = []
            cls._instance.list_files_ko = []
            cls._instance.mode = None
            cls._instance.forcage = None
            cls._instance.volumetry_rate = None
            cls._instance.total_elements = 0
            cls._instance.total_lines_pending = 0
            cls._instance.total_guarantees_in_file = 0
            cls._instance.total_corresponsdence_in_file = 0
            cls._instance.total_corresponsdence = 0
            cls._instance.total_guarantees = 0
            cls._instance.invalid_lines = []
            cls._instance.total_invalid_lines = 0
            cls._instance.differential_operations = {
                DEPRECIATE: [],
                ADDED: [],
                UPDATED: [],
                NO_CHANGES: []
            }
        return cls._instance

    def init_file_report(self):
        self.total_corresponsdence += self.total_corresponsdence_in_file
        self.total_guarantees += self.total_guarantees_in_file
        self.total_invalid_lines += len(self.invalid_lines)
        self.total_guarantees_in_file = 0
        self.total_corresponsdence_in_file = 0
        self.invalid_lines = []
        self.differential_operations = {
            DEPRECIATE: [],
            ADDED: [],
            UPDATED: [],
            NO_CHANGES: []
        }

    def generate_business_report(self):
        if self.mode == DIFFERENTIEL_MODE:
            logger.info("Valid lines : " + str(self.total_corresponsdence_in_file))
            logger.info("Lines with errors : " + str(len(self.invalid_lines)))

    def generate_crex(self):
        self.total_invalid_lines += len(self.invalid_lines)
        self.total_corresponsdence += self.total_corresponsdence_in_file
        output_parameteres = {
            COUNT_FILES_OK: len(self.list_files_ok),
            LIST_FILES_OK: self.list_files_ok,
            COUNT_FILES_KO: len(self.list_files_ko),
            LIST_FILES_KO: self.list_files_ko,
            COUNT_LINES_OK: self.total_corresponsdence,
            COUNT_LINES_KO: self.total_invalid_lines if self.mode == DIFFERENTIEL_MODE else self.total_lines_pending
        }
        produce_output_parameters(output_parameteres)

