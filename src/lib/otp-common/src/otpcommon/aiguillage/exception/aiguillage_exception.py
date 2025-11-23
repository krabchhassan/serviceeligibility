class AiguillageException(Exception):
    """
    Exception raised when a call aiguillage api fail
    """

    def __init__(self, status, error):
        super().__init__(f'Aiguillage API error: {status} - {error}')
