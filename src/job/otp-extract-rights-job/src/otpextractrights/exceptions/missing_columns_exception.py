from typing import List

from otpextractrights.exceptions.general_exception import GeneralException
from otpextractrights.utils.errors import MISSING_COLUMNS


class MissingColumnsException(GeneralException):
    """
    Exception raised when a column is missing in the input file
    """
    EXCEPTION_MESSAGE = MISSING_COLUMNS

    def __init__(self, missing_columns: List[str]):
        self.message = self.EXCEPTION_MESSAGE.format(missing_columns)
        super().__init__(self.message)
