def init_logging(logger_name):
    """
    Initialize logging from framework
    """
    from beyondpythonframework.config import Configuration

    from beyondpythonframework.utils import LOGGING_LEVEL_PATH_NATURE_DEPENDENT
    from beyondpythonframework.config.logging.logging_configuration import LoggingConfiguration
    framework_configuration = Configuration()
    level = framework_configuration.get_value(LOGGING_LEVEL_PATH_NATURE_DEPENDENT)
    LoggingConfiguration().add_additional_loggers_and_configure([logger_name],level)
