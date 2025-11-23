import os
import logging
from beyondpythonframework.securitycontext import init_execution_context

from bobbtransfojob.common.common_constants import FILE_NAME, ERROR_MESSAGE, OMU_ID, POD_NAME_ENV_VAR
from bobbtransfojob.models.event_type import EventType as EventTypes
from bobbtransfojob.logging import init_logging

LOGGER_NAME = "bobbtransfo"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


def init_context():
    init_execution_context()


class EventPublisherService:

    def publish_success_event(self, filename):
        self.handle_publish_event(EventTypes.BOBB_TRANSFORMATION_FILE_SUCCEEDED_EVENT, filename, success=True)

    def publish_failed_event(self, error_message):
        self.handle_publish_event(EventTypes.BOBB_TRANSFORMATION_FILE_FAILED_EVENT, error_message, success=False)

    def handle_publish_event(self, event_type, data, success):
        event_data = self.initialize_event_data(data, success)
        self.publish_event(event_type, event_data)
        if success:
            logger.info(f"Published event: {event_type.value} with information: fileName: {event_data[FILE_NAME]},"
                        f"omuId: {event_data[OMU_ID]}")
        else:
            logger.error(
                f"Published event: {event_type.value} with information: errorMessage: {event_data[ERROR_MESSAGE]},"
                f"omuId: {event_data[OMU_ID]}")

    @staticmethod
    def get_omu_id():
        pod_name = os.getenv(POD_NAME_ENV_VAR, "is-bdds-bobb-import-ccxkz-4238269182")
        parts = pod_name.split("-")
        return "-".join(parts[:5])

    def initialize_event_data(self, data, success):
        if success:
            event_data = {FILE_NAME: data, OMU_ID: self.get_omu_id()}
        else:
            event_data = {ERROR_MESSAGE: data, OMU_ID: self.get_omu_id()}
        return event_data

    @staticmethod
    def publish_event(event_type, event_data):
        from beyondpythonframework.messaging import get_business_event_producer
        messaging = get_business_event_producer()
        messaging.send_business_event(event_type.value, event_data)
