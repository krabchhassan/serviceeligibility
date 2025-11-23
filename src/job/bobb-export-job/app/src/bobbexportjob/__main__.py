import os
import logging

from argparse import ArgumentParser

from bobbexportjob.services.event_publisher_service import init_context
from commonomuhelper.omuhelper import omu_parse_args, produce_output_parameters

from bobbexportjob.common.common_constants import JOB_NAME, INPUT_ARG_MAX_ROWS_PER_FILE, OUTPUT_FILENAME, \
    LIMIT_LINES_PER_FILE, DEFAULT_LIMIT_LINES_PER_FILE, LIST_EXPORTED_FILE, NUMBER_EXPORTED_FILES, TOTAL_EXPORTED_ROWS
from bobbexportjob.configuration.database_manager import DatabaseManager
from bobbexportjob.services.event_publisher_service import EventPublisherService
from bobbexportjob.services.extract_contract_element_service import ExtractContractElementService
from bobbexportjob.services.process_contract_element_service import ProcessContractElementService
from bobbexportjob.logging import init_logging

LOGGER_NAME = "bobbexport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


def handle_error_and_publish_event(error_msg):
    logger.error(error_msg)
    event_publisher = EventPublisherService()
    event_publisher.publish_failed_event(error_msg)


def fetch_data():
    try:
            db_manager = DatabaseManager()
            extract_contract_element_service = ExtractContractElementService(db_manager)
            contract_elements = extract_contract_element_service.get_all_contract_elements()
            return contract_elements ,db_manager

    except Exception as e:
        error_msg = f"An error occurred during data export: {str(e)}"
        handle_error_and_publish_event(error_msg)



def process_exported_data(max_rows, filename, contracts_elements):
    try:
        logger.info(f"Max rows per file : {max_rows}")
        logger.info("Begin writing data into excel files.")
        process_contract_element_service = ProcessContractElementService(max_rows, filename)
        process_contract_element_service.process_contract_data(contracts_elements)
        logger.info("End writing data into excel files.")
        logger.info("Begin generate CREX.")
        process_contract_element_service.generate_crex()
        logger.info("End generate CREX.")

    except Exception as e:
        error_msg = f"An error occurred while processing exported data: {str(e)}"
        handle_error_and_publish_event(error_msg)


def parse_arg():
    parser = ArgumentParser()
    parser.add_argument('--max_rows_per_file', type=str, required=True)
    return omu_parse_args(parser, None)


def export(max_rows):
    init_context()
    logger.info("============ Le JOB bobb-export-job est exécuté ============")
    max_rows = int(max_rows)
    limit_lines = int(os.environ.get(LIMIT_LINES_PER_FILE, DEFAULT_LIMIT_LINES_PER_FILE))
    if max_rows <= limit_lines:
        contracts_elements,db_manager = fetch_data()
        process_exported_data(max_rows, OUTPUT_FILENAME, contracts_elements)
        db_manager.close_connection()
    else:
        generate_empty_crex()
        error_msg = f"The number of rows {max_rows} exceeds the maximum limit allowed per file {limit_lines}."
        handle_error_and_publish_event(error_msg)

    logger.info("============ Le JOB bobb-export-job est terminé ============")


def generate_empty_crex():
    produce_output_parameters({
        LIST_EXPORTED_FILE: [],
        NUMBER_EXPORTED_FILES: 0,
        TOTAL_EXPORTED_ROWS: 0
    })


if __name__ == '__main__':
    from beyondpythonframework.tracing import init_tracing, get_tracer, get_trace_id
    # Configuration du tracing
    init_tracing()
    tracer = get_tracer()

    # Start a new trace and set it as the current trace
    with tracer.start_as_current_span(JOB_NAME):
        job_trace_id = get_trace_id()
        logger.info(f"Job's trace ID : {job_trace_id}")
        args = parse_arg()
        max_rows_per_file = args.get(INPUT_ARG_MAX_ROWS_PER_FILE)
        export(max_rows_per_file)
