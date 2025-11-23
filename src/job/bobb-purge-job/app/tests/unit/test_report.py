import unittest
from unittest.mock import patch, call
import logging
from bobbpurgejob.common.report import Report
from bobbpurgejob.common.common_constants import (
    PURGED_CONTRACT_ELEMENTS_COUNT, 
    UPDATED_VERSIONS_COUNT,
    PRE_PURGE,
	POST_PURGE
)
class TestReport(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        """Set up test fixtures before any tests are run."""
        # Set up logging for tests
        logging.basicConfig(level=logging.DEBUG)
        cls.logger = logging.getLogger(__name__)

    def setUp(self):
        """Set up test fixtures before each test method is called."""
        # Reset the singleton instance before each test
        Report._instance = None
        self.report = Report()
        
        # Patch the produce_output_parameters function
        self.produce_output_patcher = patch('bobbpurgejob.common.report.produce_output_parameters')
        self.mock_produce_output = self.produce_output_patcher.start()
        
    def tearDown(self):
        """Tear down test fixtures after each test method has been called."""
        self.produce_output_patcher.stop()
        
    def test_singleton_pattern(self):
        """Test that Report follows the singleton pattern."""
        # Create two instances
        report1 = Report()
        report2 = Report()
        
        # Both should be the same instance
        self.assertIs(report1, report2)
        
    def test_initial_values(self):
        """Test that counters are initialized to zero."""
        self.assertEqual(self.report.purged_contract_elements, 0)
        self.assertEqual(self.report.updated_versions, 0)
        self.assertEqual(self.report.pre_purge_statistics, {})
        self.assertEqual(self.report.post_purge_statistics, {})
        
    def test_generate_crex(self):
        """Test that generate_crex produces the correct output parameters."""
        # Set test values
        self.report.purged_contract_elements = 5
        self.report.updated_versions = 3
        self.report.pre_purge_statistics = {'total_contract_elements':5, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 1}
        self.report.post_purge_statistics = {'total_contract_elements':2, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}
        
        # Call the method
        self.report.generate_crex()
        
        # Verify the output parameters - all values should be strings
        expected_output = {
            str(PURGED_CONTRACT_ELEMENTS_COUNT): '5',
            str(UPDATED_VERSIONS_COUNT): '3',
            str(PRE_PURGE): "{'total_contract_elements': 5, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 1}",
            str(POST_PURGE): "{'total_contract_elements': 2, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}"
        }
        self.mock_produce_output.assert_called_once_with(expected_output)
        
    def test_counters_independence(self):
        """Test that counters are independent between test cases."""
        # In this test, we're creating a new Report instance, which should have counters at 0
        self.assertEqual(self.report.purged_contract_elements, 0)
        self.assertEqual(self.report.updated_versions, 0)
        self.assertEqual(self.report.pre_purge_statistics, {})
        self.assertEqual(self.report.post_purge_statistics, {})
        
        # Modify the counters
        self.report.purged_contract_elements = 10
        self.report.updated_versions = 7
        self.report.pre_purge_statistics = {'total_contract_elements':10, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 2}
        self.report.post_purge_statistics = {'total_contract_elements':4, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 2.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}
        
        # Verify the changes
        self.assertEqual(self.report.purged_contract_elements, 10)
        self.assertEqual(self.report.updated_versions, 7)
        self.assertEqual(self.report.pre_purge_statistics, {'total_contract_elements':10, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 2})
        self.assertEqual(self.report.post_purge_statistics, {'total_contract_elements':4, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 2.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0})
    
    def test_generate_crex_with_zero_values(self):
        """Test generate_crex with zero values."""
        # Ensure counters are zero
        self.report.purged_contract_elements = 0
        self.report.updated_versions = 0
        self.report.pre_purge_statistics = {}
        self.report.post_purge_statistics = {}
        
        # Call the method
        self.report.generate_crex()
        
        # Verify the output parameters - all values should be strings
        expected_output = {
            str(PURGED_CONTRACT_ELEMENTS_COUNT): '0',
            str(UPDATED_VERSIONS_COUNT): '0',
            str(PRE_PURGE): '{}',
            str(POST_PURGE): '{}'
        }
        self.mock_produce_output.assert_called_once_with(expected_output)
    
    def test_generate_crex_with_high_values(self):
        """Test generate_crex with high values."""
        # Set high values
        self.report.purged_contract_elements = 1000000
        self.report.updated_versions = 500000
        self.report.pre_purge_statistics = {'total_contract_elements':1000000, '_id': None, 'total_groups': 400000, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 400000}
        self.report.post_purge_statistics = {'total_contract_elements':400000, '_id': None, 'total_groups': 400000, 'avg_versions_per_group': 2.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}
        
        # Call the method
        self.report.generate_crex()
        
        # Verify the output parameters - all values should be strings
        expected_output = {
            str(PURGED_CONTRACT_ELEMENTS_COUNT): '1000000',
            str(UPDATED_VERSIONS_COUNT): '500000',
            str(PRE_PURGE): "{'total_contract_elements': 1000000, '_id': None, 'total_groups': 400000, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 400000}",
            str(POST_PURGE): "{'total_contract_elements': 400000, '_id': None, 'total_groups': 400000, 'avg_versions_per_group': 2.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}"
        }
        self.mock_produce_output.assert_called_once_with(expected_output)
    
    def test_generate_crex_multiple_calls(self):
        """Test multiple calls to generate_crex with different values."""
        # First call
        self.report.purged_contract_elements = 5
        self.report.updated_versions = 3
        self.report.pre_purge_statistics = {'total_contract_elements':5, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 1}
        self.report.post_purge_statistics = {'total_contract_elements':2, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}
        self.report.generate_crex()
        
        # Second call with different values
        self.report.purged_contract_elements = 10
        self.report.updated_versions = 6
        self.report.pre_purge_statistics = {'total_contract_elements':10, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 2}
        self.report.post_purge_statistics = {'total_contract_elements':4, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 2.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}
        self.report.generate_crex()
        
        # Verify both calls with correct parameters - all values should be strings
        expected_calls = [
            call({
                str(PURGED_CONTRACT_ELEMENTS_COUNT): '5',
                str(UPDATED_VERSIONS_COUNT): '3',
                str(PRE_PURGE): "{'total_contract_elements': 5, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 1}",
                str(POST_PURGE): "{'total_contract_elements': 2, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}"
            }),
            call({
                str(PURGED_CONTRACT_ELEMENTS_COUNT): '10',
                str(UPDATED_VERSIONS_COUNT): '6',
                str(PRE_PURGE): "{'total_contract_elements': 10, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 5.0, 'max_versions_in_group': 5, 'groups_with_3plus_versions': 2}",
                str(POST_PURGE): "{'total_contract_elements': 4, '_id': None, 'total_groups': 2, 'avg_versions_per_group': 2.0, 'max_versions_in_group': 2, 'groups_with_3plus_versions': 0}"
            })
        ]
        self.mock_produce_output.assert_has_calls(expected_calls)
    
    def test_report_instance_across_tests(self):
        """Test that the Report instance is properly reset between tests."""
        # This test verifies that the singleton is properly reset between tests
        self.assertEqual(self.report.purged_contract_elements, 0)
        self.assertEqual(self.report.updated_versions, 0)
        self.assertEqual(self.report.pre_purge_statistics, {})
        self.assertEqual(self.report.post_purge_statistics, {})
        
        # Modify the counters
        self.report.purged_contract_elements = 1
        self.report.updated_versions = 1
        self.report.pre_purge_statistics = {'total_contract_elements':1, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 1.0, 'max_versions_in_group': 1, 'groups_with_3plus_versions': 1}
        self.report.post_purge_statistics = {'total_contract_elements':1, '_id': None, 'total_groups': 1, 'avg_versions_per_group': 1.0, 'max_versions_in_group': 1, 'groups_with_3plus_versions': 0}

if __name__ == '__main__':
    unittest.main()
