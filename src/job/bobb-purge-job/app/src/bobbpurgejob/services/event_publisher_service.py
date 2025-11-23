import os
import logging
from beyondpythonframework.securitycontext import init_execution_context

from bobbpurgejob.common.common_constants import OMU_ID_ENV_VAR, LOGGER_NAME
from bobbpurgejob.common.report import Report
from bobbpurgejob.logging import init_logging

init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

class EventPublisher:
    def __init__(self):
        self.omu_id = os.getenv(OMU_ID_ENV_VAR, "is-bdds-bobb-purge-ccxkz-4238269182")
        logger.info(f"omu_id {self.omu_id}")
        self.report = Report()

    def init_context(self):
        init_execution_context()
