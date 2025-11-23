import logging

from bobbpurgejob.common.common_constants import JOB_NAME, LOGGER_NAME
from bobbpurgejob.services.contract_element_service import ContractElementService
from bobbpurgejob.services.version_service import VersionService
from bobbpurgejob.logging import init_logging
from bobbpurgejob.services.event_publisher_service import EventPublisher
from bobbpurgejob.configuration.configuration import get_keep_count
from beyondpythonframework.tracing import init_tracing, get_tracer, get_trace_id


init_logging(LOGGER_NAME)
logger = logging.getLogger(LOGGER_NAME)


def main():
    state = 0
    logger.info("============ Démarrage du job de purge ============")
    event_publisher = EventPublisher()
    event_publisher.init_context()
    try:
        init_tracing()
        tracer = get_tracer()

        with tracer.start_as_current_span(JOB_NAME):
            job_trace_id = get_trace_id()
            logger.info(f"Job trace ID : {job_trace_id}")

            contract_service = ContractElementService(event_publisher=event_publisher)
            version_service = VersionService(event_publisher=event_publisher)

            logger.info("Collecting statistics before purge...")
            pre_purge_contract_stats = contract_service.get_purge_statistics()
            pre_purge_version_stats = version_service.get_version_statistics()
            pre_purge_stats = {**pre_purge_contract_stats, **pre_purge_version_stats}
            logger.info(f"Pre-purge statistics: {pre_purge_stats}")

            logger.info("Starting purge operation...")

            keep_count = get_keep_count()
            logger.info(f"Keeping {keep_count} most recent versions")

            versions_to_purge = version_service.get_versions_to_purge_optimized(keep_count)
            
            if not versions_to_purge:
                logger.info("No versions to purge")
                return

            updated_count = version_service.mark_versions_for_purge_optimized(versions_to_purge)

            version_ids = version_service.get_version_ids(versions_to_purge)

            deleted_count = contract_service.purge_contract_elements(version_ids)
            
            logger.info(f"Purge completed. {deleted_count} documents deleted, {updated_count} versions updated.")

            logger.info("Collecting statistics after purge...")
            post_purge_contract_stats = contract_service.get_purge_statistics()
            post_purge_version_stats = version_service.get_version_statistics()
            post_purge_stats = {**post_purge_contract_stats, **post_purge_version_stats}
            logger.info(f"Post-purge statistics: {post_purge_stats}")

            logger.info("Generating reports...")
            contract_service.generate_purge_report(
                deleted_count=deleted_count,
                updated_count=updated_count,
                pre_purge_stats=pre_purge_stats,
                post_purge_stats=post_purge_stats
            )

            logger.info(f"Purge completed successfully. {deleted_count} elements deleted and "
                    f"{updated_count} versions updated.")

            logger.debug("=== SYNTHESE DE LA PURGE ===")
            logger.debug(f"contractElement documents before purge"
                         f": {pre_purge_stats.get('total_contract_elements', 'N/A')}")
            logger.debug(f"contractElement documents after purge"
                         f": {post_purge_stats.get('total_contract_elements', 'N/A')}")
            logger.debug("===========================================")

    except Exception as e:
        logger.exception(f"Error during purge execution : {str(e)}")
        state = 1

    finally:
        logger.info("============ Fin du job de purge ============")
        exit(state)


if __name__ == '__main__':
    main()
