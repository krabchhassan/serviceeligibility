import os
import logging
from beyondpythonframework.securitycontext import init_execution_context

from commonomuhelper.omuhelper import read_output_parameters

from bobbimportjob.common.common_constants import (FILENAME, MODE, FORCAGE, ERROR_MESSAGE,
                                       IMPORT_FAILED_EVENT, IMPORT_SUCCEEDED_EVENT, OMU_ID,
                                       CODE_OFFER, CODE_PRODUCT, CODE_BENEFITE_NATURE, EFFECTIVE_DATE,
                                       TO, FROM, CODE_CONTRACT_ELEMENT, CODE_INSURER, CODE_AMC,
                                       CODE_OC, PRODUCT_ELEMENTS, IMPORT_LINES_SUCCEEDED_EVENT,
                                       IMPORT_LINE_FAILED_EVENT, OMU_CODE, AUDIT_TYPE, USER_ID, IGNORED,
                                       SUCCEEDED_LINES_COUNT, NUMBER_DISTINCT_GUARANTEES, FAILED_LINES_COUNT,
                                       DIFFERENTIEL_MODE, OMU_ID_ENV_VAR)
from bobbimportjob.common.report import Report
from bobbimportjob.logging import init_logging

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


def init_context():
    init_execution_context()


class EventPublisher:
    def __init__(self):
        self.omu_id = os.getenv(OMU_ID_ENV_VAR, "is-bdds-bobb-import-ccxkz-4238269182")
        self.report = Report()

    def publish_failed_import_file_event(self, filename, mode, forcage, error):
        from beyondpythonframework.messaging import get_business_event_producer
        event_data = {
            OMU_ID: self.omu_id,
            FILENAME: filename,
            MODE: mode,
            FORCAGE: forcage,
            ERROR_MESSAGE: error
        }
        messaging = get_business_event_producer()
        messaging.send_business_event(IMPORT_FAILED_EVENT, event_data)
        logger.info("=> publish import failed event => {0}".format(event_data))

    def publish_success_import_file_event(self, filename, mode, forcage):
        from beyondpythonframework.messaging import get_business_event_producer
        event_data = {
            OMU_ID: self.omu_id,
            FILENAME: filename,
            MODE: mode,
            FORCAGE: forcage,
            NUMBER_DISTINCT_GUARANTEES: self.report.total_guarantees_in_file
        }
        messaging = get_business_event_producer()
        messaging.send_business_event(IMPORT_SUCCEEDED_EVENT, event_data)
        logger.info("=> publish import succeeded event => {0}".format(event_data))

    def publish_lines_integration_succeeded(self, file_name):
        from beyondpythonframework.messaging import get_business_event_producer
        event_data = {
            FILENAME: file_name,
            MODE: self.report.mode,
            OMU_ID: os.getenv(OMU_ID_ENV_VAR, "is-bdds-bobb-import-ccxkz-4238269182"),
            SUCCEEDED_LINES_COUNT: self.report.total_corresponsdence_in_file
        }
        messaging = get_business_event_producer()
        messaging.send_business_event(IMPORT_LINES_SUCCEEDED_EVENT, event_data)
        logger.info(f"=> Publish {IMPORT_LINES_SUCCEEDED_EVENT} => {str(event_data)}")

    def publish_audit(self, file_name):
        total_invalid_lines = len(self.report.invalid_lines)
        user_id = read_output_parameters(USER_ID)
        event_data = {
            FILENAME: file_name,
            MODE: self.report.mode,
            OMU_ID: os.getenv(OMU_ID_ENV_VAR, "is-bdds-bobb-import-ccxkz-4238269182"),
            SUCCEEDED_LINES_COUNT: self.report.total_corresponsdence,
            FAILED_LINES_COUNT: total_invalid_lines if self.report.mode == DIFFERENTIEL_MODE else self.report.total_lines_pending
        }
        from beyondpythonframework.messaging import get_audit_event_producer
        audit_producer = get_audit_event_producer()
        audit_producer.send_audit_event(
            event_type=AUDIT_TYPE,
            event_data=event_data,
            subject=OMU_CODE,
            user_id=user_id
        )

    def publish_line_integration_failed(self, file_name, code_contract_element, code_insurer, ignored, code_amc,
                                        code_oc,
                                        code_offer, code_product, code_benefit_nature, effective_date, to, from_date,
                                        e):
        from beyondpythonframework.messaging import get_business_event_producer
        event_data = {
            FILENAME: file_name,
            MODE: self.report.mode,
            CODE_CONTRACT_ELEMENT: code_contract_element,
            CODE_INSURER: code_insurer,
            IGNORED: str(ignored),
            CODE_AMC: code_amc,
            CODE_OC: code_oc,
            CODE_OFFER: code_offer,
            CODE_PRODUCT: code_product,
            CODE_BENEFITE_NATURE: code_benefit_nature,
            EFFECTIVE_DATE: effective_date,
            TO: to,
            FROM: from_date,
            ERROR_MESSAGE: str(e),
            OMU_ID: os.getenv(OMU_ID_ENV_VAR, "is-bdds-bobb-import-ccxkz-4238269182")
        }
        messaging = get_business_event_producer()
        messaging.send_business_event(IMPORT_LINE_FAILED_EVENT, event_data)
        logger.info("=> Publish bobb-import-line-failed-event => " + str(event_data))

    def publish_contract_integration_failed(self, line, file_name, exception):
        common_fields = self.extract_common_fields(line)

        product_elements = line.get(PRODUCT_ELEMENTS, [])
        if not product_elements:
            self.publish_line_integration_failed(file_name, *common_fields, *([''] * 7), exception)
        else:
            for product in product_elements:
                product_fields = self.extract_product_fields(product)
                self.publish_line_integration_failed(file_name, *common_fields, *product_fields, exception)

    def extract_common_fields(self, line):
        return [
            line.get(CODE_CONTRACT_ELEMENT, ""),
            line.get(CODE_INSURER, ""),
            line.get(IGNORED, ""),
            line.get(CODE_AMC, "")
        ]

    def extract_product_fields(self, product):
        return [
            product.get(CODE_OC, ""),
            product.get(CODE_OFFER, ""),
            product.get(CODE_PRODUCT, ""),
            product.get(CODE_BENEFITE_NATURE, ""),
            product.get(EFFECTIVE_DATE, ""),
            product.get(TO, ""),
            product.get(FROM, "")
        ]
