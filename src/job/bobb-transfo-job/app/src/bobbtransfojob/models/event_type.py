from enum import Enum


class EventType(Enum):
    BOBB_TRANSFORMATION_FILE_SUCCEEDED_EVENT = 'bobb-transformation-file-succeeded-event'
    BOBB_TRANSFORMATION_FILE_FAILED_EVENT = 'bobb-transformation-file-failed-event'
