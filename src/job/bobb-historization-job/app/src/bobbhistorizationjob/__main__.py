import logging
from .services.historization_service import HistorizationService
from .common.common_constants import JOB_NAME
from .common.environment_validator import EnvironmentValidator

from bobbhistorizationjob.logging import init_logging
LOGGER_NAME = "bobbhistorization"
init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


def main():
    EnvironmentValidator.validate_environment()
    logger.info("============ Le JOB bobb-historization-job est exécuté ============")
    HistorizationService().historize_files()
    logger.info("============ Le JOB bobb-historization-job est terminé ============")


if __name__ == '__main__':
    from beyondpythonframework.tracing import init_tracing, get_tracer, get_trace_id
    # Configuration du tracing
    init_tracing()
    tracer = get_tracer()

    # Start a new trace and set it as the current trace
    with tracer.start_as_current_span(JOB_NAME):
        job_trace_id = get_trace_id()
        logger.info(f"Job's trace ID : {job_trace_id}")
        main()
