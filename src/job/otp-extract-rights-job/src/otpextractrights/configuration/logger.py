import logging
import traceback
from typing import Union

CONFIGURATION_PATH = "otpextractrights.configuration"
LOGGER_NAME = "otpextractrights"


class Logger:
    _logger: logging = None

    @staticmethod
    def init_logger():
        Logger._logger = logging.getLogger(LOGGER_NAME)

    @staticmethod
    def info(message: str):
        Logger._logger.info(message)

    @staticmethod
    def error(message: Union[str, Exception]):
        Logger._logger.error(traceback.format_exc())
        Logger._logger.error(message)

    @staticmethod
    def warning(message: str):
        Logger._logger.warning(message)

    @staticmethod
    def debug(message: str):
        Logger._logger.debug(message)
