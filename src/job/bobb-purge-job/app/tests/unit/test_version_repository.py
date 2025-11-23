import unittest
from unittest.mock import patch, MagicMock
from datetime import datetime, timezone
from bson import ObjectId

from bobbpurgejob.repositories.version_repository import VersionRepository
from bobbpurgejob.common.common_constants import COLLECTION_VERSIONS, STATUS_ACTIVE

class TestVersionRepository(unittest.TestCase):
    def setUp(self):
        # Patch the database connection
        self.db_manager_patcher = patch('bobbpurgejob.repositories.version_repository.DatabaseManager')
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
        self.repository = VersionRepository()

    def tearDown(self):
        self.db_manager_patcher.stop()

    def test_initialization(self):
        """Test that repository initializes correctly with database connections"""
        self.mock_db_manager_class.assert_called_once()
        self.mock_db_manager.get_db.assert_called_once()
        self.mock_db.__getitem__.assert_called_once_with(COLLECTION_VERSIONS)
        self.assertEqual(self.repository.db, self.mock_db)
        self.assertEqual(self.repository.collection, self.mock_collection)

    def test_get_last_keep_count_versions(self):
        """Test retrieval of last N active versions"""
        # Mock data
        mock_versions = [
            {"_id": ObjectId(), "creationDate": datetime.now(timezone.utc), "purgeDate": None},
            {"_id": ObjectId(), "creationDate": datetime.now(timezone.utc), "purgeDate": None}
        ]
        
        # Configure the mock
        self.mock_collection.find.return_value.sort.return_value.limit.return_value = mock_versions
        
        # Call the method
        result = self.repository.get_last_keep_count_versions(2)
        
        # Verify the result
        self.assertEqual(result, mock_versions)
        
        # Verify the query
        self.mock_collection.find.assert_called_once_with({
            "purgeDate": None
        })
        self.mock_collection.find.return_value.sort.assert_called_once_with("creationDate", -1)
        self.mock_collection.find.return_value.sort.return_value.limit.assert_called_once_with(2)

    def test_get_versions_to_purge(self):
        """Test retrieval of versions to be purged"""
        # Mock data
        last_versions = [{"_id": ObjectId()}, {"_id": ObjectId()}]
        mock_versions_to_purge = [
            {"_id": ObjectId(), "purgeDate": None},
            {"_id": ObjectId(), "purgeDate": None}
        ]
        
        # Configure the mock
        self.mock_collection.find.return_value = mock_versions_to_purge
        
        # Call the method
        result = self.repository.get_versions_to_purge(last_versions)
        
        # Verify the result
        self.assertEqual(result, mock_versions_to_purge)
        
        # Verify the query
        self.mock_collection.find.assert_called_once_with({
            "_id": {"$nin": [v["_id"] for v in last_versions]},
            "purgeDate": None
        })

    def test_mark_versions_for_purge(self):
        """Test marking versions for purge"""
        # Test data
        versions_to_purge = [{"_id": ObjectId()}, {"_id": ObjectId()}]
        purged_by = "test-user"
        
        # Configure the mock
        self.mock_collection.update_many.return_value.modified_count = 2
        
        # Call the method
        result = self.repository.mark_versions_for_purge(versions_to_purge, purged_by)
        
        # Verify the result
        self.assertEqual(result, 2)
        
        # Verify the update operation
        self.mock_collection.update_many.assert_called_once()
        args, kwargs = self.mock_collection.update_many.call_args
        
        # Verify the query
        self.assertEqual(args[0], {"_id": {"$in": [v["_id"] for v in versions_to_purge]}})
        
        # Verify the update
        self.assertIn("$set", args[1])
        self.assertIsNotNone(args[1]["$set"]["purgeDate"])
        self.assertEqual(args[1]["$set"]["purgedBy"], purged_by)

    def test_get_cutoff_version(self):
        """Test getting the cutoff version"""
        # Mock data
        keep_count = 5
        mock_version = {"_id": ObjectId(), "creationDate": datetime.now(timezone.utc)}
        
        # Configure the mock
        self.mock_collection.find_one.return_value = mock_version
        
        # Call the method
        result = self.repository.get_cutoff_version(keep_count)
        
        # Verify the result
        self.assertEqual(result, mock_version)
        
        # Verify the query
        self.mock_collection.find_one.assert_called_once_with(
            {"status": STATUS_ACTIVE, "purgeDate": None},
            sort=[("creationDate", -1)],
            skip=keep_count - 1
        )

    def test_get_versions_to_purge_cursor(self):
        cutoff_date = datetime.now(timezone.utc)
        mock_cursor = MagicMock()

        mock_find_result = MagicMock()
        mock_find_result.batch_size.return_value = mock_cursor
        self.mock_collection.find.return_value = mock_find_result

        result = self.repository.get_versions_to_purge_cursor(cutoff_date)

        self.assertEqual(result, mock_cursor)

        self.mock_collection.find.assert_called_once_with({
            "status": STATUS_ACTIVE,
            "purgeDate": None,
            "creationDate": {"$lt": cutoff_date}
        })

        mock_find_result.batch_size.assert_called_once_with(1000)

    def test_mark_versions_batch(self):
        """Test marking a batch of versions for purge"""
        # Test data
        version_ids = [ObjectId() for _ in range(3)]
        purged_by = "test-user"
        
        # Configure the mock
        self.mock_collection.update_many.return_value.modified_count = 3
        
        # Call the method
        result = self.repository.mark_versions_batch(version_ids, purged_by)
        
        # Verify the result
        self.assertEqual(result, 3)
        
        # Verify the update operation
        self.mock_collection.update_many.assert_called_once()
        args, _ = self.mock_collection.update_many.call_args
        
        # Verify the query
        self.assertEqual(args[0], {"_id": {"$in": version_ids}})
        
        # Verify the update
        self.assertIn("$set", args[1])
        self.assertIsNotNone(args[1]["$set"]["purgeDate"])
        self.assertEqual(args[1]["$set"]["purgedBy"], purged_by)

    def test_mark_versions_batch_empty(self):
        """Test marking an empty batch of versions"""
        # Call the method with empty list
        result = self.repository.mark_versions_batch([], "test-user")
        
        # Verify the result
        self.assertEqual(result, 0)
        
        # Verify no update was performed
        self.mock_collection.update_many.assert_not_called()

    def test_get_versions_to_purge_optimized(self):
        """Test optimized retrieval of versions to purge"""
        # Mock data
        last_versions = [{"_id": ObjectId()}, {"_id": ObjectId()}]
        mock_versions_to_purge = [
            {"_id": ObjectId(), "purgeDate": None},
            {"_id": ObjectId(), "purgeDate": None}
        ]
        
        # Configure the mock cursor
        mock_cursor = MagicMock()
        mock_cursor.__iter__.return_value = mock_versions_to_purge
        self.mock_collection.find.return_value.batch_size.return_value = mock_cursor
        
        # Call the method
        result = self.repository.get_versions_to_purge_optimized(last_versions)
        
        # Verify the result
        self.assertEqual(result, mock_versions_to_purge)
        
        # Verify the query
        self.mock_collection.find.assert_called_once_with(
            {"purgeDate": None},
            {"_id": 1}
        )
        self.mock_collection.find.return_value.batch_size.assert_called_once_with(5000)

    def test_mark_versions_for_purge_optimized(self):
        versions_to_purge = [{"_id": i} for i in range(2500)]  # 2500 versions to test batching
        purged_by = "test-user"
        update_counts = [1000, 1000, 500]
        self.mock_collection.update_many.side_effect = [
             MagicMock(modified_count=update_counts[0]),
             MagicMock(modified_count=update_counts[1]),
             MagicMock(modified_count=update_counts[2])
        ]

        result = self.repository.mark_versions_for_purge_optimized(versions_to_purge, purged_by)

        self.assertEqual(result, 2500)

        self.assertEqual(self.mock_collection.update_many.call_count, 3)

    def test_mark_versions_for_purge_optimized_empty(self):
        """Test optimized marking with empty list"""
        result = self.repository.mark_versions_for_purge_optimized([], "test-user")
        self.assertEqual(result, 0)
        self.mock_collection.update_many.assert_not_called()

if __name__ == '__main__':
    unittest.main()
