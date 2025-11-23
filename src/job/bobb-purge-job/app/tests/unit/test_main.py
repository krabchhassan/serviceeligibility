import unittest
from unittest.mock import patch, MagicMock, call

from bobbpurgejob.__main__ import main
from bobbpurgejob.common.common_constants import JOB_NAME


class TestMainPurgeJob(unittest.TestCase):

    def setUp(self):
        # Mock exit to prevent tests from actually exiting - use builtins.exit
        self.exit_patcher = patch('builtins.exit')
        self.mock_exit = self.exit_patcher.start()

    def tearDown(self):
        self.exit_patcher.stop()

    @patch('bobbpurgejob.__main__.get_trace_id')
    @patch('bobbpurgejob.__main__.get_tracer')
    @patch('bobbpurgejob.__main__.init_tracing')
    @patch('bobbpurgejob.__main__.get_keep_count')
    @patch('bobbpurgejob.__main__.VersionService')
    @patch('bobbpurgejob.__main__.ContractElementService')
    @patch('bobbpurgejob.__main__.EventPublisher')
    @patch('bobbpurgejob.__main__.logger')
    def test_main_successful_execution(self, mock_logger, mock_event_publisher,
                                       mock_contract_service_cls, mock_version_service_cls, mock_get_keep_count,
                                       mock_init_tracing, mock_get_tracer,
                                       mock_get_trace_id):
        # Mock dependencies
        mock_event_publisher_instance = MagicMock()
        mock_event_publisher.return_value = mock_event_publisher_instance

        # Mock VersionService instance
        mock_version_service_instance = MagicMock()
        mock_version_service_instance.get_version_statistics.return_value = {
            'total_versions': 50,
            'active_versions': 30,
            'updated_versions': 0
        }
        mock_version_service_instance.get_versions_to_purge_optimized.return_value = ['version1', 'version2']
        mock_version_service_instance.mark_versions_for_purge_optimized.return_value = 10
        mock_version_service_instance.get_version_ids.return_value = ['id1', 'id2']
        mock_version_service_cls.return_value = mock_version_service_instance

        # Mock ContractElementService instance
        mock_contract_service_instance = MagicMock()
        mock_contract_service_instance.get_purge_statistics.return_value = {
            'total_contract_elements': 100
        }
        mock_contract_service_instance.purge_contract_elements.return_value = 5
        mock_contract_service_instance.generate_purge_report.return_value = None
        mock_contract_service_cls.return_value = mock_contract_service_instance

        mock_get_keep_count.return_value = 2

        mock_tracer = MagicMock()
        mock_span = MagicMock()
        mock_tracer.start_as_current_span.return_value.__enter__ = MagicMock(return_value=mock_span)
        mock_tracer.start_as_current_span.return_value.__exit__ = MagicMock(return_value=None)
        mock_get_tracer.return_value = mock_tracer

        mock_get_trace_id.return_value = "test-trace-id"

        # Execute main function
        main()

        # Verify logging calls
        expected_log_calls = [
            call.info("============ Démarrage du job de purge ============"),
            call.info("Job trace ID : test-trace-id"),
            call.info("Collecting statistics before purge..."),
            call.info("Pre-purge statistics: {'total_contract_elements': 100, 'total_versions': 50, 'active_versions': 30, 'updated_versions': 0}"),
            call.info("Starting purge operation..."),
            call.info("Keeping 2 most recent versions"),
            call.info("Purge completed. 5 documents deleted, 10 versions updated."),
            call.info("Collecting statistics after purge..."),
            call.info("Post-purge statistics: {'total_contract_elements': 100, 'total_versions': 50, 'active_versions': 30, 'updated_versions': 0}"),
            call.info("Generating reports..."),
            call.info("Purge completed successfully. 5 elements deleted and 10 versions updated."),
            call.info("============ Fin du job de purge ============")
        ]

        # Check that all expected log calls were made
        for log_call in expected_log_calls:
            self.assertIn(log_call, mock_logger.mock_calls)

        # Verify method calls
        mock_event_publisher_instance.init_context.assert_called_once()
        mock_init_tracing.assert_called_once()
        mock_get_tracer.assert_called_once()
        mock_tracer.start_as_current_span.assert_called_once_with(JOB_NAME)
        mock_get_trace_id.assert_called_once()

        # Verify service initialization
        mock_contract_service_cls.assert_called_once_with(event_publisher=mock_event_publisher_instance)
        mock_version_service_cls.assert_called_once_with(event_publisher=mock_event_publisher_instance)

        # Verify purge execution flow
        mock_version_service_instance.get_versions_to_purge_optimized.assert_called_once_with(2)
        mock_version_service_instance.mark_versions_for_purge_optimized.assert_called_once_with(['version1', 'version2'])
        mock_version_service_instance.get_version_ids.assert_called_once_with(['version1', 'version2'])
        mock_contract_service_instance.purge_contract_elements.assert_called_once_with(['id1', 'id2'])

        # Verify report generation
        mock_contract_service_instance.generate_purge_report.assert_called_once_with(
            deleted_count=5,
            updated_count=10,
            pre_purge_stats={'total_contract_elements': 100, 'total_versions': 50, 'active_versions': 30, 'updated_versions': 0},
            post_purge_stats={'total_contract_elements': 100, 'total_versions': 50, 'active_versions': 30, 'updated_versions': 0}
        )

        # Verify exit with success code
        self.mock_exit.assert_called_once_with(0)

    @patch('bobbpurgejob.__main__.get_trace_id')
    @patch('bobbpurgejob.__main__.get_tracer')
    @patch('bobbpurgejob.__main__.get_keep_count')
    @patch('bobbpurgejob.__main__.ContractElementService')
    @patch('bobbpurgejob.__main__.VersionService')
    @patch('bobbpurgejob.__main__.EventPublisher')
    @patch('bobbpurgejob.__main__.logger')
    def test_main_empty_purge(self, mock_logger, mock_event_publisher,
                              mock_version_service_cls, mock_contract_service_cls,
                              mock_get_keep_count,
                              mock_get_tracer, mock_get_trace_id):
        # Mock services and their instances
        mock_event_publisher_instance = MagicMock()
        mock_event_publisher.return_value = mock_event_publisher_instance

        # Mock ContractElementService
        mock_contract_service_instance = MagicMock()
        mock_contract_service_instance.get_purge_statistics.return_value = {
            'total_contract_elements': 10
        }
        mock_contract_service_instance.purge_contract_elements.return_value = 0
        mock_contract_service_instance.generate_purge_report.return_value = None
        mock_contract_service_cls.return_value = mock_contract_service_instance

        # Mock VersionService
        mock_version_service_instance = MagicMock()
        mock_version_service_instance.get_version_statistics.return_value = {
            'total_versions': 5,
            'active_versions': 2,
            'updated_versions': 0
        }
        mock_version_service_instance.get_versions_to_purge_optimized.return_value = []  # No versions to purge
        mock_version_service_cls.return_value = mock_version_service_instance

        # Mock keep count
        mock_get_keep_count.return_value = 2

        # Mock tracing
        mock_tracer = MagicMock()
        mock_span = MagicMock()
        mock_tracer.start_as_current_span.return_value.__enter__ = MagicMock(return_value=mock_span)
        mock_tracer.start_as_current_span.return_value.__exit__ = MagicMock(return_value=None)
        mock_get_tracer.return_value = mock_tracer
        mock_get_trace_id.return_value = "test-trace-id"

        # Execute main function
        main()

        # Verify the job completed successfully
        self.mock_exit.assert_called_once_with(0)

        # Verify service initialization
        mock_contract_service_cls.assert_called_once_with(event_publisher=mock_event_publisher_instance)
        mock_version_service_cls.assert_called_once_with(event_publisher=mock_event_publisher_instance)

        # Verify statistics collection
        mock_contract_service_instance.get_purge_statistics.assert_called()
        mock_version_service_instance.get_version_statistics.assert_called()

        # Verify version service calls
        mock_version_service_instance.get_versions_to_purge_optimized.assert_called_once_with(2)  # keep_count=2

        # Verify no versions to purge scenario
        mock_version_service_instance.mark_versions_for_purge_optimized.assert_not_called()
        mock_version_service_instance.get_version_ids.assert_not_called()
        mock_contract_service_instance.purge_contract_elements.assert_not_called()

        # Verify logging
        mock_logger.info.assert_any_call("No versions to purge")
        # Note: Les appels suivants ne se produisent pas quand il n'y a pas de versions à purger
        # car la fonction retourne prématurément

        # Verify report generation does NOT happen when no versions to purge
        mock_contract_service_instance.generate_purge_report.assert_not_called()

    @patch('bobbpurgejob.__main__.get_trace_id')
    @patch('bobbpurgejob.__main__.get_tracer')
    @patch('bobbpurgejob.__main__.get_keep_count')
    @patch('bobbpurgejob.__main__.VersionService')
    @patch('bobbpurgejob.__main__.ContractElementService')
    @patch('bobbpurgejob.__main__.EventPublisher')
    @patch('bobbpurgejob.__main__.logger')
    def test_main_exception_handling(self, mock_logger, mock_event_publisher,
                                     mock_contract_service_cls, mock_version_service_cls, mock_get_keep_count,
                                    mock_get_tracer, mock_get_trace_id):
        # Mock dependencies
        mock_event_publisher_instance = MagicMock()
        mock_event_publisher.return_value = mock_event_publisher_instance

        # Mock VersionService to raise an exception
        mock_version_service_instance = MagicMock()
        mock_version_service_instance.get_version_statistics.side_effect = Exception("Test error")
        mock_version_service_cls.return_value = mock_version_service_instance

        # Mock ContractElementService
        mock_contract_service_instance = MagicMock()
        mock_contract_service_cls.return_value = mock_contract_service_instance

        mock_get_keep_count.return_value = 2

        mock_tracer = MagicMock()
        mock_span = MagicMock()
        mock_tracer.start_as_current_span.return_value.__enter__ = MagicMock(return_value=mock_span)
        mock_tracer.start_as_current_span.return_value.__exit__ = MagicMock(return_value=None)
        mock_get_tracer.return_value = mock_tracer

        mock_get_trace_id.return_value = "test-trace-id"

        # Execute main function
        main()

        # Verify exception was logged
        mock_logger.exception.assert_called_once_with("Error during purge execution : Test error")

        # Verify exit with error code
        self.mock_exit.assert_called_once_with(1)


if __name__ == '__main__':
    unittest.main()