from otpextractrights.exceptions.general_exception import GeneralException


class ValidationException(GeneralException):
    """
    Exception raised when an error occurs during input data validation
    """

    def __init__(self, message: str):
        self.message = message
        super().__init__(self.message)
