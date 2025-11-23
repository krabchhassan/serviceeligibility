

class RedisConfigurationException(Exception):
    """
    Exception remontée quand un élément de la configuration Redis est manquant.
    Indique la clé de l'élément de configuration manquant
    """
    MESSAGE = "Missing [{}] configuration for Redis Implementation"

    def __init__(self, missing_configuration: str):
        self.error_message = self.MESSAGE.format(missing_configuration)
        super().__init__(self.error_message)
