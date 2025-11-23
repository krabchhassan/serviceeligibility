from otpextractrights.exceptions.general_exception import GeneralException
from otpextractrights.utils.errors import MISSING_FILE


class MissingFileException(GeneralException):
    """
    Exception raised when the input file is missing
    """
    EXCEPTION_MESSAGE = MISSING_FILE

    def __init__(self, file_path: str):
        self.message = self.EXCEPTION_MESSAGE.format(file_path)
        super().__init__(self.message)
