import unittest
from unittest.mock import MagicMock, patch
from datetime import datetime
from bson import ObjectId

from bobbpurgejob.services.version_service import VersionService
from bobbpurgejob.services.event_publisher_service import EventPublisher
from bobbpurgejob.common.common_constants import STATUS_ACTIVE

class TestVersionService(unittest.TestCase):

    def setUp(self):
        self.mock_event_publisher = MagicMock(spec=EventPublisher)
        self.mock_event_publisher.context_initialized = True
        self.mock_event_publisher.omu_id = "test_omu"

        self.version_repo_patcher = patch('bobbpurgejob.services.version_service.VersionRepository')
        self.mock_version_repo = self.version_repo_patcher.start()
        self.mock_version_repo.return_value = self.mock_version_repo
        
        self.service = VersionService(event_publisher=self.mock_event_publisher)

        self.sample_versions = [
            {"_id": ObjectId(), "creationDate": datetime(2023, 1, 1)},
            {"_id": ObjectId(), "creationDate": datetime(2023, 2, 1)},
            {"_id": ObjectId(), "creationDate": datetime(2023, 3, 1)},
        ]

    def tearDown(self):
        self.version_repo_patcher.stop()

    def test_get_version_statistics(self):
        self.mock_version_repo.collection.find.return_value = self.sample_versions[:2]
        self.mock_version_repo.collection.count_documents.return_value = 3

        def count_documents_side_effect(query):
            if query == {}:  # Total count
                return 3
            elif query == {"purgeDate": {"$ne": None}}:  # Purged count
                return 0
            else:
                return 0

            self.mock_version_repo.collection.count_documents.side_effect = count_documents_side_effect

            stats = self.service.get_version_statistics()

            self.assertEqual(stats["total_versions"], 3)
            self.assertEqual(stats["active_versions"], 2)
            self.assertEqual(stats["updated_versions"], 0)

            self.mock_version_repo.collection.find.assert_called_once_with({
                "status": STATUS_ACTIVE,
                "purgeDate": None
            })

    def test_get_versions_to_purge(self):
        self.mock_version_repo.get_last_keep_count_versions.return_value = self.sample_versions[:1]
        self.mock_version_repo.get_versions_to_purge.return_value = self.sample_versions[1:2]

        versions_to_purge = self.service.get_versions_to_purge(keep_count=1)

        self.assertEqual(len(versions_to_purge), 1)
        self.mock_version_repo.get_last_keep_count_versions.assert_called_once_with(1)
        self.mock_version_repo.get_versions_to_purge.assert_called_once_with(self.sample_versions[:1])

    def test_mark_versions_for_purge(self):
        self.mock_version_repo.mark_versions_for_purge.return_value = 2

        updated_count = self.service.mark_versions_for_purge(self.sample_versions[:2])

        self.assertEqual(updated_count, 2)
        self.mock_version_repo.mark_versions_for_purge.assert_called_once_with(
            versions_to_purge=self.sample_versions[:2],
            purged_by="test_omu"
        )

    def test_get_version_ids(self):
        version_ids = self.service.get_version_ids(self.sample_versions)

        self.assertEqual(len(version_ids), 3)
        self.assertIsInstance(version_ids[0], ObjectId)

    def test_get_active_versions_count(self):
        self.mock_version_repo.collection.find.return_value = self.sample_versions[:2]

        count = self.service.get_active_versions_count()

        self.assertEqual(count, 2)
        self.mock_version_repo.collection.find.assert_called_once_with({
            "status": STATUS_ACTIVE,
            "purgeDate": None
        })


    def test_get_versions_to_purge_optimized(self):
        """Test the optimized version of get_versions_to_purge"""
        self.mock_version_repo.get_last_keep_count_versions.return_value = self.sample_versions[:1]
        self.mock_version_repo.get_versions_to_purge_optimized.return_value = self.sample_versions[1:2]

        versions_to_purge = self.service.get_versions_to_purge_optimized(keep_count=1)

        self.assertEqual(len(versions_to_purge), 1)
        self.mock_version_repo.get_last_keep_count_versions.assert_called_once_with(1)
        self.mock_version_repo.get_versions_to_purge_optimized.assert_called_once_with(self.sample_versions[:1])

    def test_mark_versions_for_purge_optimized(self):
        """Test the optimized version of mark_versions_for_purge"""
        self.mock_version_repo.mark_versions_for_purge_optimized.return_value = 2

        updated_count = self.service.mark_versions_for_purge_optimized(self.sample_versions[:2])

        self.assertEqual(updated_count, 2)
        self.mock_version_repo.mark_versions_for_purge_optimized.assert_called_once_with(
            versions_to_purge=self.sample_versions[:2],
            purged_by="test_omu"
        )

    def test_mark_versions_for_purge_optimized_empty(self):
        """Test the optimized version with empty list"""
        updated_count = self.service.mark_versions_for_purge_optimized([])
        self.assertEqual(updated_count, 0)
        self.mock_version_repo.mark_versions_for_purge_optimized.assert_not_called()

if __name__ == "__main__":
    unittest.main()
