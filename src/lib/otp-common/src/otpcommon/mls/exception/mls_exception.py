from otpcommon.rest.model.http_status import HttpStatus


class MlsException(Exception):
    """
    Exception raised when a call MLS lib fail
    """

    def __init__(self, status, error):
        super().__init__(f'MLS lib error: {status} - {error}')


class MlsNotFoundException(MlsException):
    def __init__(self, amc: str):
        super().__init__(HttpStatus.NOT_FOUND, f'Could not find Organization location with Code {amc}')


class OcNotFoundException(MlsException):
    def __init__(self, amc: str):
        super().__init__(HttpStatus.NOT_FOUND, f'Amc {amc} not found in organisation')