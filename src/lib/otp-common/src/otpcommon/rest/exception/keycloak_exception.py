class AuthException(Exception):
    """
    Exception raised when a problem occurs during the Identity Provider call
    """

    def __init__(self, error):
        super().__init__(f'Keycloak error: {error}')


class UnableToGetHttpAuthorizationHeader(AuthException):
    pass


class DecodedTokenNotInTheContext(AuthException):
    pass
