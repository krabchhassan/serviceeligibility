from beyondpythonframework.config import Configuration
from bobbpurgejob.common.common_constants import KEEP_COUNT, DEFAULT_KEEP_COUNT

def get_configuration():
    configuration = Configuration()
    return configuration

def get_keep_count():
    return get_configuration().get_value(key=KEEP_COUNT, default_value=DEFAULT_KEEP_COUNT)