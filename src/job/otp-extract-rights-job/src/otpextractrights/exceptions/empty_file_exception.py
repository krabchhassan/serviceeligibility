from otpextractrights.exceptions.general_exception import GeneralException
from otpextractrights.utils.errors import EMPTY_FILE


class EmptyFileException(GeneralException):
    """
    Exception raised when the input file is empty
    """
    EXCEPTION_MESSAGE = EMPTY_FILE

    def __init__(self):
        self.message = self.EXCEPTION_MESSAGE
        super().__init__(self.message)
