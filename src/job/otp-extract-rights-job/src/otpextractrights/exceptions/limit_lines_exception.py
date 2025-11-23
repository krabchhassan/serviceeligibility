from otpextractrights.exceptions.general_exception import GeneralException
from otpextractrights.utils.errors import LIMIT_EXCEEDED_ERROR


class LimitLinesException(GeneralException):
    """
    Exception raised when the input file is over 100 lines
    """
    EXCEPTION_MESSAGE = LIMIT_EXCEEDED_ERROR

    def __init__(self):
        self.message = self.EXCEPTION_MESSAGE
        super().__init__(self.message)
