import csv
import os
from dataclasses import is_dataclass, fields
from pathlib import Path
from typing import List

from commonpersonalworkdir.common_personal_workdir import build_personal_workdir_path

from otpextractrights.configuration import Logger
from otpextractrights.exceptions.empty_file_exception import EmptyFileException
from otpextractrights.exceptions.limit_lines_exception import LimitLinesException
from otpextractrights.exceptions.missing_columns_exception import MissingColumnsException
from otpextractrights.exceptions.missing_file_exception import MissingFileException
from otpextractrights.model import InputBeneficiary, OutputBeneficiary
from otpextractrights.model.input_beneficiary import InputFields
from otpextractrights.utils import CrexUtils
from otpextractrights.utils.errors import EMPTY_FILE, MISSING_COLUMNS, MISSING_FILE, LIMIT_EXCEEDED_ERROR

CSV_DELIMITER = ";"
MAX_LINES = 100


class InputOutputService:

    @staticmethod
    def get_input(user_id: str, file_path: str):
        return InputOutputService._get_input(filepath=InputOutputService.get_input_path(user_id, file_path))

    @staticmethod
    def save_output(outputs: List[OutputBeneficiary], user_id: str, file_path: str):
        return InputOutputService._save_output(
            outputs=outputs,
            filepath=InputOutputService.get_output_path(user_id, file_path)
        )

    @staticmethod
    def _get_input(filepath: str) -> List[InputBeneficiary]:
        Logger.info(f"Loading data from {filepath}")
        InputOutputService.check_file_exist(file_path=filepath)
        with open(filepath) as csv_file:
            csv_reader = csv.DictReader(csv_file, delimiter=CSV_DELIMITER)
            rows: List[dict] = list(csv_reader)
            InputOutputService.check_file_empty(rows)
            InputOutputService.check_file_limit(rows)
            InputOutputService.check_columns(list(csv_reader.fieldnames))
            inputs_beneficiaries = [
                InputBeneficiary(
                    ligne=i,
                    nir=row.get(InputFields.NIR),
                    date_naissance=row.get(InputFields.DATE_NAISSANCE) if row.get(
                        InputFields.DATE_NAISSANCE) != '' else None,
                    date_soins=row.get(InputFields.DATE_SOINS),
                    amc=row.get(InputFields.AMC) if row.get(InputFields.AMC) is not '' else None,
                )
                for i, row in enumerate(rows, start=1)
            ]
        return inputs_beneficiaries

    @staticmethod
    def _save_output(outputs: List[OutputBeneficiary], filepath) -> None:
        """Saves a list of OutputBeneficiary instances to a CSV file."""
        filepath = Path(filepath)
        Logger.info(f"Saving CSV to {filepath}")

        if not outputs:
            Logger.warning("No data to save. Skipping CSV generation.")
            return

        try:
            filepath.parent.mkdir(parents=True, exist_ok=True)

            # Get field names dynamically
            if is_dataclass(outputs[0]):
                fieldnames = [field.name for field in fields(outputs[0])]
            else:
                fieldnames = list(vars(outputs[0]).keys())

            with filepath.open('w', newline='', encoding='utf-8-sig') as csvfile:
                writer = csv.DictWriter(csvfile, fieldnames=fieldnames, delimiter=CSV_DELIMITER)
                writer.writeheader()
                writer.writerows(output.to_dict() for output in outputs)
        except Exception as e:
            Logger.error(f"Failed to save CSV to {filepath}: {e}")
            raise

    @staticmethod
    def get_input_path(user_id: str, file_path: str) -> str:
        return f"{build_personal_workdir_path(user_id)}/{file_path}"

    @staticmethod
    def get_output_path(user_id: str, file_path: str) -> str:
        omu_id = os.getenv("OMU_ID")
        return f"{build_personal_workdir_path(user_id)}/{omu_id}_{file_path}"

    @staticmethod
    def check_file_empty(rows: List[dict]):
        if len(rows) == 0:
            CrexUtils.set_general_error(EMPTY_FILE)
            raise EmptyFileException()

    @staticmethod
    def check_columns(columns: List[str]) -> None:
        """Checks if all required columns are present in the given list."""
        if is_dataclass(InputBeneficiary):
            required_columns = [field.name for field in fields(InputBeneficiary) if field.name != "ligne"]
        else:
            required_columns = [key for key in vars(InputBeneficiary) if key != "ligne"]

        missing_columns = [col for col in required_columns if col not in columns]

        if missing_columns:
            CrexUtils.set_general_error(MISSING_COLUMNS.format(missing_columns))
            raise MissingColumnsException(missing_columns)

    @staticmethod
    def check_file_exist(file_path: str):
        if not os.path.isfile(file_path):
            CrexUtils.set_general_error(MISSING_FILE.format(file_path))
            raise MissingFileException(file_path)

    @staticmethod
    def check_file_limit(rows: List[dict]):
        if len(rows) > MAX_LINES:
            CrexUtils.set_general_error(LIMIT_EXCEEDED_ERROR)
            raise LimitLinesException()
