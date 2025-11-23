import logging
from .exceptions.invalid_parameter_exception import InvalidParameterException
from .common.common_constants import MODE_ARG, FORCAGE_ARG, COMPLET_MODE, DIFFERENTIEL_MODE, OUI, NON, JOB_NAME
from .services.import_service import ImportService
from .services.event_publisher_service import init_context
from bobbimportjob.logging import init_logging
from commonomuhelper.omuhelper import omu_parse_args
from argparse import ArgumentParser

LOGGER_NAME = "bobbimport"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)

IMPORT_MODE_OPTIONS = {COMPLET_MODE, DIFFERENTIEL_MODE}
FORCING_MODE_OPTIONS = {OUI, NON}


def parse_arg():
    parser = ArgumentParser()
    parser.add_argument('--mode', type=str, required=True)
    parser.add_argument('--forcage', type=str, required=True)
    return omu_parse_args(parser, None)


def main(mode, forcage):
    init_context()
    check_args(mode, forcage)
    ImportService().import_bobb(mode.upper(), forcage.upper())


def check_args(mode, forcage):
    if mode.upper() not in IMPORT_MODE_OPTIONS or forcage.upper() not in FORCING_MODE_OPTIONS:
        raise InvalidParameterException("La préconfiguration est invalide.")


if __name__ == '__main__':
    from beyondpythonframework.tracing import init_tracing, get_tracer, get_trace_id
    init_tracing()
    tracer = get_tracer()

    with tracer.start_as_current_span(JOB_NAME):
        job_trace_id = get_trace_id()
        logger.info(f"Job's trace ID : {job_trace_id}")
        args = parse_arg()
        mode = args.get(MODE_ARG)
        forcage = args.get(FORCAGE_ARG)
        main(mode, forcage)
