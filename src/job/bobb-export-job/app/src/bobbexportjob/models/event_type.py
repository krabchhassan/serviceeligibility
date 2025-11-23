from enum import Enum


class EventType(Enum):
    BOBB_EXPORT_FILE_SUCCEEDED_EVENT = 'bobb-export-file-succeeded-event'
    BOBB_EXPORT_FILE_FAILED_EVENT = 'bobb-export-file-failed-event'
