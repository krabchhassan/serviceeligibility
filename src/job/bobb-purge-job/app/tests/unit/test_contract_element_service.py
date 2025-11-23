import unittest
from unittest.mock import patch
from bobbpurgejob.common.report import Report
from bobbpurgejob.common.common_constants import PURGED_CONTRACT_ELEMENTS_COUNT, UPDATED_VERSIONS_COUNT, PRE_PURGE, POST_PURGE


class TestReport(unittest.TestCase):

    def setUp(self):
        """Setup before each test"""
        # Réinitialiser l'instance singleton avant chaque test
        Report._instance = None
        self.report = Report()

    def tearDown(self):
        """Cleanup after each test"""
        Report._instance = None

    def test_generate_parameter_crex_should_set_correct_values(self):
        # Arrange
        deleted_count = 10
        updated_count = 5
        pre_purge_stats = {
            'total_contract_elements': 100,
            'total_versions': 50,
            'active_versions': 45,
            'updated_versions': 5
        }
        post_purge_stats = {
            'total_contract_elements': 90,
            'total_versions': 45,
            'active_versions': 40,
            'updated_versions': 10
        }

        # Act
        self.report.generate_parameter_crex(
            deleted_count=deleted_count,
            updated_count=updated_count,
            pre_purge_stats=pre_purge_stats,
            post_purge_stats=post_purge_stats
        )

        # Assert
        self.assertEqual(self.report.purged_contract_elements, deleted_count)
        self.assertEqual(self.report.updated_versions, updated_count)
        self.assertEqual(self.report.pre_purge_statistics, pre_purge_stats)
        self.assertEqual(self.report.post_purge_statistics, post_purge_stats)

    def test_generate_parameter_crex_should_handle_missing_stats(self):
        # Arrange
        deleted_count = 10
        updated_count = 5
        pre_purge_stats = {}  # Stats vides
        post_purge_stats = {'total_contract_elements': 90}  # Stats partielles

        # Act
        self.report.generate_parameter_crex(
            deleted_count=deleted_count,
            updated_count=updated_count,
            pre_purge_stats=pre_purge_stats,
            post_purge_stats=post_purge_stats
        )

        # Assert - devrait avoir des valeurs par défaut pour les champs manquants
        self.assertEqual(self.report.purged_contract_elements, deleted_count)
        self.assertEqual(self.report.updated_versions, updated_count)
        self.assertEqual(self.report.pre_purge_statistics['total_contract_elements'], 0)
        self.assertEqual(self.report.pre_purge_statistics['total_versions'], 0)
        self.assertEqual(self.report.pre_purge_statistics['active_versions'], 0)
        self.assertEqual(self.report.pre_purge_statistics['updated_versions'], 0)
        self.assertEqual(self.report.post_purge_statistics['total_contract_elements'], 90)
        self.assertEqual(self.report.post_purge_statistics['total_versions'], 0)
        self.assertEqual(self.report.post_purge_statistics['active_versions'], 0)
        self.assertEqual(self.report.post_purge_statistics['updated_versions'], 0)

    def test_generate_parameter_crex_should_handle_none_stats(self):
        deleted_count = 10
        updated_count = 5

        with self.assertRaises(ValueError) as context:
           self.report.generate_parameter_crex(
                deleted_count=deleted_count,
                updated_count=updated_count,
                pre_purge_stats=None,
                post_purge_stats=None
            )
           self.assertEqual(str(context.exception), "pre_purge_stats and post_purge_stats must be dictionaries")

    def test_generate_parameter_crex_should_raise_on_invalid_types(self):
        with self.assertRaises(ValueError) as context:
            self.report.generate_parameter_crex(
                deleted_count=10,
                updated_count=5,
                pre_purge_stats="not_a_dict",  # Type invalide
                post_purge_stats=123           # Type invalide
            )
        self.assertEqual(str(context.exception), "pre_purge_stats and post_purge_stats must be dictionaries")

    def test_generate_parameter_crex_should_raise_on_negative_counts(self):
        with self.assertRaises(ValueError) as context:
            self.report.generate_parameter_crex(
                deleted_count=-1,
                updated_count=5,
                pre_purge_stats={},
                post_purge_stats={}
            )
        self.assertEqual(str(context.exception), "Counts cannot be negative")

    @patch('bobbpurgejob.common.report.produce_output_parameters')
    def test_generate_crex_should_call_produce_output_parameters_with_correct_structure(self, mock_produce):
        # Arrange
        self.report.purged_contract_elements = 10
        self.report.updated_versions = 5
        self.report.pre_purge_statistics = {
            'total_contract_elements': 100,
            'total_versions': 50,
            'active_versions': 45,
            'updated_versions': 5
        }
        self.report.post_purge_statistics = {
            'total_contract_elements': 90,
            'total_versions': 45,
            'active_versions': 40,
            'updated_versions': 10
        }

        expected_output = {
            str(PURGED_CONTRACT_ELEMENTS_COUNT): '10',
            str(UPDATED_VERSIONS_COUNT): '5',
            str(PRE_PURGE): "{'total_contract_elements': 100, 'total_versions': 50, 'active_versions': 45, 'updated_versions': 5}",
            str(POST_PURGE): "{'total_contract_elements': 90, 'total_versions': 45, 'active_versions': 40, 'updated_versions': 10}"
        }

        # Act
        self.report.generate_crex()

        # Assert
        mock_produce.assert_called_once_with(expected_output)

    @patch('bobbpurgejob.common.report.produce_output_parameters')
    def test_generate_parameter_crex_followed_by_generate_crex_integration(self, mock_produce):
        # Arrange
        deleted_count = 15
        updated_count = 8
        pre_purge_stats = {
            'total_contract_elements': 200,
            'total_versions': 100,
            'active_versions': 90,
            'updated_versions': 10
        }
        post_purge_stats = {
            'total_contract_elements': 185,
            'total_versions': 92,
            'active_versions': 85,
            'updated_versions': 18
        }

        expected_output = {
            str(PURGED_CONTRACT_ELEMENTS_COUNT): '15',
            str(UPDATED_VERSIONS_COUNT): '8',
            str(PRE_PURGE): "{'total_contract_elements': 200, 'total_versions': 100, 'active_versions': 90, 'updated_versions': 10}",
            str(POST_PURGE): "{'total_contract_elements': 185, 'total_versions': 92, 'active_versions': 85, 'updated_versions': 18}"
        }

        # Act
        self.report.generate_parameter_crex(
            deleted_count=deleted_count,
            updated_count=updated_count,
            pre_purge_stats=pre_purge_stats,
            post_purge_stats=post_purge_stats
        )
        self.report.generate_crex()

        # Assert
        mock_produce.assert_called_once_with(expected_output)

    def test_singleton_pattern_maintained(self):
        # Arrange & Act
        report1 = Report()
        report2 = Report()

        # Assert
        self.assertIs(report1, report2)

    def test_generate_parameter_crex_should_override_previous_values(self):
        # Arrange
        self.report.purged_contract_elements = 100
        self.report.updated_versions = 50
        self.report.pre_purge_statistics = {'old': 'data'}
        self.report.post_purge_statistics = {'old': 'data'}

        new_pre_stats = {
            'total_contract_elements': 200,
            'total_versions': 100,
            'active_versions': 90,
            'updated_versions': 10
        }
        new_post_stats = {
            'total_contract_elements': 150,
            'total_versions': 80,
            'active_versions': 70,
            'updated_versions': 20
        }

        # Act
        self.report.generate_parameter_crex(
            deleted_count=25,
            updated_count=15,
            pre_purge_stats=new_pre_stats,
            post_purge_stats=new_post_stats
        )

        # Assert
        self.assertEqual(self.report.purged_contract_elements, 25)
        self.assertEqual(self.report.updated_versions, 15)
        self.assertEqual(self.report.pre_purge_statistics, new_pre_stats)
        self.assertEqual(self.report.post_purge_statistics, new_post_stats)

    def test_generate_parameter_crex_with_partial_statistics(self):
        # Arrange
        partial_pre_stats = {
            'total_contract_elements': 100,
            'active_versions': 80
        }
        partial_post_stats = {
            'total_versions': 90,
            'updated_versions': 10
            # total_contract_elements et active_versions manquants
        }

        # Act
        self.report.generate_parameter_crex(
            deleted_count=10,
            updated_count=5,
            pre_purge_stats=partial_pre_stats,
            post_purge_stats=partial_post_stats
        )

        # Assert - les champs manquants devraient avoir des valeurs par défaut
        self.assertEqual(self.report.pre_purge_statistics['total_contract_elements'], 100)
        self.assertEqual(self.report.pre_purge_statistics['total_versions'], 0)  # valeur par défaut
        self.assertEqual(self.report.pre_purge_statistics['active_versions'], 80)
        self.assertEqual(self.report.pre_purge_statistics['updated_versions'], 0)  # valeur par défaut

        self.assertEqual(self.report.post_purge_statistics['total_contract_elements'], 0)  # valeur par défaut
        self.assertEqual(self.report.post_purge_statistics['total_versions'], 90)
        self.assertEqual(self.report.post_purge_statistics['active_versions'], 0)  # valeur par défaut
        self.assertEqual(self.report.post_purge_statistics['updated_versions'], 10)

    def test_initial_state(self):
        """Test the initial state of Report instance"""
        report = Report()
        self.assertEqual(report.purged_contract_elements, 0)
        self.assertEqual(report.updated_versions, 0)
        self.assertEqual(report.pre_purge_statistics, {})
        self.assertEqual(report.post_purge_statistics, {})

    @patch('bobbpurgejob.common.report.produce_output_parameters')
    def test_generate_crex_with_default_values(self, mock_produce):
        """Test generate_crex with default values (no parameters set)"""
        # Act
        self.report.generate_crex()

        # Assert
        expected_output = {
            str(PURGED_CONTRACT_ELEMENTS_COUNT): '0',
            str(UPDATED_VERSIONS_COUNT): '0',
            str(PRE_PURGE): '{}',
            str(POST_PURGE): '{}'
        }
        mock_produce.assert_called_once_with(expected_output)


if __name__ == '__main__':
    unittest.main()