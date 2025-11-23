class BddsException(Exception):
    """
    Exception raised when a call bdds api fail
    """

    def __init__(self, status, error):
        super().__init__(f'BDDS API error: {status} - {error}')
