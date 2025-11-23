from enum import Enum

DEFAULT_STATUS_CODE = -1


class HttpStatus(Enum):
    """
    Enum to bind HTTP code with HTTP status as global variable
    """
    EXCEPTION = -1
    OK = 200
    NO_CONTENT = 204
    CREATED = 201
    PARTIAL_CONTENT = 206
    BAD_REQUEST = 400
    UNAUTHORIZED = 401
    FORBIDDEN = 403
    NOT_FOUND = 404
    CONFLICT = 409
    INTERNAL_SERVER = 500

    @classmethod
    def from_code(cls, status_code: int = DEFAULT_STATUS_CODE):
        """
        Return the ApiCallStatus enum corresponding to the given http status code
        :param status_code: http status code
        :type status_code: int
        :return: the enum corresponding to the given http status code, EXCEPTION ApiCallStatus otherwise
        """
        try:
            return cls(status_code)
        except ValueError:
            return cls.EXCEPTION
