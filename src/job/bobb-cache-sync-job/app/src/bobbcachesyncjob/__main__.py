from beyondpythonframework.config.logging import get_beyond_logger

from .common.common_constants import JOB_NAME, CLEAR_MODE, SYNC_MODE
from .services.sync_service import SyncService

logger = get_beyond_logger()

MODE_OPTIONS = {CLEAR_MODE, SYNC_MODE}


def parse_arg():
    from commonomuhelper.omuhelper import omu_parse_args
    from argparse import ArgumentParser
    parser = ArgumentParser()
    parser.add_argument('--mode', type=str, required=True)
    return omu_parse_args(parser, None)


def check_args():
    if mode.upper() not in MODE_OPTIONS:
        raise Exception("La préconfiguration est invalide.")


def generate_crex(nb_deleted_values=0, nb_added_values=0):
    from commonomuhelper.omuhelper import produce_output_parameters
    produce_output_parameters({
        "nb_correspondance_bobb_supprimees": nb_deleted_values,
        "nb_correspondance_bobb_creees": nb_added_values
    })


def main():
    logger.info("============ Le JOB bobb-cache-sync-job est exécuté ============")
    if mode.upper() == SYNC_MODE:
        logger.info("Starting SYNC")
        SyncService().clean_cache()
        nb_added_values = SyncService().synchronize_cache()
        logger.info(f"Number of values added : {nb_added_values}")
        generate_crex(nb_added_values=nb_added_values)
    elif mode.upper() == CLEAR_MODE:
        logger.info("Starting CLEAR")
        nb_deleted_values = SyncService().clean_cache()
        logger.info(f"Number of values deleted : {nb_deleted_values}")
        generate_crex(nb_deleted_values=nb_deleted_values)
    else:
        logger.error("No mode " + mode.upper())
    logger.info("============ Le JOB bobb-cache-sync-job est terminé ============")


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
        mode = args.get("mode")
        check_args()
        main()
