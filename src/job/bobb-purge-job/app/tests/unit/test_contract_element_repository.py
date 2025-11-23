import unittest
from unittest.mock import patch, MagicMock
import uuid
from bson.binary import Binary, UuidRepresentation

from bobbpurgejob.repositories.contract_element_repository import ContractElementRepository
from bobbpurgejob.common.common_constants import COLLECTION_CONTRACT_ELEMENT


class TestContractElementRepository(unittest.TestCase):

    def setUp(self):
        # Patch the database connection
        self.db_manager_patcher = patch('bobbpurgejob.repositories.contract_element_repository.DatabaseManager')
        self.mock_db_manager_class = self.db_manager_patcher.start()

        # Create a mock database manager instance
        self.mock_db_manager = MagicMock()
        self.mock_db_manager_class.return_value = self.mock_db_manager

        # Create mock database and collection
        self.mock_db = MagicMock()
        self.mock_collection = MagicMock()

        # Set up the mock database manager to return our mock database
        self.mock_db_manager.get_db.return_value = self.mock_db
        self.mock_db.__getitem__.return_value = self.mock_collection

        # Create repository instance
        self.repository = ContractElementRepository()

        # Reset mocks for each test
        self.mock_collection.reset_mock()

    def tearDown(self):
        self.db_manager_patcher.stop()

    def test_initialization(self):
        """Test that repository initializes correctly with database connections"""
        self.mock_db_manager_class.assert_called_once()
        self.mock_db_manager.get_db.assert_called_once()
        self.mock_db.__getitem__.assert_called_once_with(COLLECTION_CONTRACT_ELEMENT)
        self.assertEqual(self.repository.db, self.mock_db)
        self.assertEqual(self.repository.collection, self.mock_collection)

    def test_convert_to_binary_uuid(self):
        """Test UUID conversion to binary format"""
        test_uuid = uuid.uuid4()
        result = self.repository._convert_to_binary_uuid(test_uuid)
        self.assertIsInstance(result, Binary)
        self.assertEqual(result, Binary(test_uuid.bytes, UuidRepresentation.STANDARD))

    def test_convert_to_binary_uuid_non_uuid(self):
        """Test that non-UUID values are returned as-is"""
        test_value = "not-a-uuid"
        result = self.repository._convert_to_binary_uuid(test_value)
        self.assertEqual(result, test_value)


    def test_delete_contract_elements_by_versions_optimized_empty(self):
        """Test optimized deletion with empty version IDs list"""
        result = self.repository.delete_contract_elements_by_versions_optimized([])
        self.assertEqual(result, 0)
        self.mock_collection.delete_many.assert_not_called()
    def test_delete_contract_elements_by_versions_optimized(self):
        version_ids = [uuid.uuid4() for _ in range(1500)]  # 1500 versions to test batching

        mock_bulk_result = MagicMock()
        mock_bulk_result.deleted_count = 1000

        self.mock_collection.bulk_write.return_value = mock_bulk_result

        result = self.repository.delete_contract_elements_by_versions_optimized(version_ids)

        self.assertEqual(result, 3000)

        self.mock_collection.bulk_write.assert_called()

    def test_delete_contract_elements_by_versions_optimized_with_error(self):
        test_uuids = [uuid.uuid4() for _ in range(5)]

        self.mock_collection.bulk_write.side_effect = Exception("Database error")

        mock_fallback_result = MagicMock()
        mock_fallback_result.deleted_count = 3  # Quelques documents supprimés
        self.mock_collection.delete_many.return_value = mock_fallback_result

        result = self.repository.delete_contract_elements_by_versions_optimized(test_uuids)

        self.assertEqual(result, 3)

        self.mock_collection.bulk_write.assert_called()
        self.mock_collection.delete_many.assert_called()

    def test_delete_contract_elements_by_versions_optimized_complete_failure(self):
        test_uuids = [uuid.uuid4() for _ in range(5)]

        self.mock_collection.bulk_write.side_effect = Exception("Bulk write error")
        self.mock_collection.delete_many.side_effect = Exception("Fallback also failed")

        result = self.repository.delete_contract_elements_by_versions_optimized(test_uuids)

        self.assertEqual(result, 0)

        self.mock_collection.bulk_write.assert_called()
        self.mock_collection.delete_many.assert_called()

if __name__ == '__main__':
    unittest.main()