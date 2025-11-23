from otpextractrights.utils import CrexUtils


class KafkaUtils:

    @staticmethod
    def publish_start_event(user_hash: str, filename: str):
        event_data = {
            "userHash": user_hash,
            "file": filename
        }
        from beyondpythonframework.messaging import get_business_event_producer
        messaging = get_business_event_producer()
        messaging.send_business_event("extract-tpoffline-start-event", event_data)

    @staticmethod
    def publish_end_event():
        from beyondpythonframework.messaging import get_business_event_producer
        messaging = get_business_event_producer()
        messaging.send_business_event("extract-tpoffline-end-event", CrexUtils.DATA)
