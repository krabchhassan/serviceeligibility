import os
import logging

from commonomuhelper.omuhelper import get_workdir_output_path, omu_parse_args
from commonpersonalworkdir.common_personal_workdir import build_personal_workdir_path
from argparse import ArgumentParser

from bobbtransfojob.common.common_constants import JOB_NAME, OUTPUT_FOLDER, INPUT_FOLDER, USER_ID, INPUT_FILE
from bobbtransfojob.services.excel_to_json_converter_service import ExcelToJsonConverterService
from bobbtransfojob.services.event_publisher_service import init_context
from bobbtransfojob.logging import init_logging

LOGGER_NAME = "bobbtransfo"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


def parse_arg():
    parser = ArgumentParser()
    parser.add_argument('--input_file', type=str, required=True)
    return omu_parse_args(parser, None)


def main(input_file):
    init_context()
    logger.info("============ Le JOB bobb-transfo-job est exécuté ============")
    user_id = os.getenv(USER_ID)
    if user_id:
        input_folder = build_personal_workdir_path(user_id)
    else:
        input_folder = os.environ.get(INPUT_FOLDER)
    logger.info(f"Reading files from {input_folder}")
    output_folder = os.environ.get(OUTPUT_FOLDER)
    if not output_folder:
        output_folder = get_workdir_output_path()
    converter_service = ExcelToJsonConverterService(input_folder, output_folder)
    converter_service.process_files_in_folder(input_file)
    converter_service.generate_crex()

    logger.info("============ Le JOB bobb-transfo-job est terminé ============")


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
        input_file = args.get(INPUT_FILE)
        main(input_file)
