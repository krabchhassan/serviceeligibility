from datetime import datetime
from unittest.mock import patch

from bobbcachesyncjob.services.redis_cache_service import RedisCacheService

import pytest


class TestRedisCacheService:

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_generate_cache_key_creates_correct_key_format(self, mock_os_getenv, mock_redis_manager):
        redis_cache_service = RedisCacheService()
        data_item = {
            'codeInsurer': 'insurer1',
            'codeContractElement': 'element1'
        }
        expected_key = 'REDIS_DATABASE_BOBB::cache:insurer1:element1'
        generated_key = redis_cache_service.generate_cache_key(data_item)
        assert generated_key == expected_key

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_serialize_data_converts_datetime_fields_to_string(self, mock_os_getenv, mock_redis_manager):
        redis_cache_service = RedisCacheService()
        data_item = {
            'codeInsurer': 'insurer1',
            'codeContractElement': 'element1',
            'productElements': [],
            'from': datetime(2021, 1, 1)
        }
        serialized_data = redis_cache_service.serialize_data(data_item)
        assert '"from": "2021-01-01"' in serialized_data

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_serialize_data_processes_product_elements_correctly(self, mock_os_getenv, mock_redis_manager):
        redis_cache_service = RedisCacheService()
        data_item = {
            'codeInsurer': 'insurer1',
            'codeContractElement': 'element1',
            'productElements': [
                {'from': datetime(2021, 1, 1)}
            ]
        }
        serialized_data = redis_cache_service.serialize_data(data_item)
        assert '"from": "2021-01-01"' in serialized_data

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_insert_chunk_in_redis_cache_stores_multiple_documents_in_redis(self, mock_os_getenv, mock_redis_manager):
        redis_cache_service = RedisCacheService()
        documents = [
            {'codeInsurer': 'insurer1', 'codeContractElement': 'element1', 'productElements': []},
            {'codeInsurer': 'insurer2', 'codeContractElement': 'element2', 'productElements': []}
        ]
        redis_cache_service.insert_chunk_in_redis_cache(documents)
        assert mock_redis_manager.return_value.master_connection.pipeline().set.call_count == len(documents)

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_generate_cache_key_handles_missing_code_insurer_gracefully(self, mock_os_getenv, mock_redis_manager):
        redis_cache_service = RedisCacheService()
        data_item = {
            'codeContractElement': 'element1'
        }
        with pytest.raises(KeyError):
            redis_cache_service.generate_cache_key(data_item)

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_generate_cache_key_handles_missing_code_contract_element_gracefully(self, mock_os_getenv,
                                                                                 mock_redis_manager):
        redis_cache_service = RedisCacheService()
        data_item = {
            'codeInsurer': 'insurer1'
        }
        with pytest.raises(KeyError):
            redis_cache_service.generate_cache_key(data_item)

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_serialize_data_handles_empty_product_elements_list(self, mock_os_getenv, mock_redis_manager):
        redis_cache_service = RedisCacheService()
        data_item = {
            'codeInsurer': 'insurer1',
            'codeContractElement': 'element1',
            'productElements': []
        }
        serialized_data = redis_cache_service.serialize_data(data_item)
        assert '"productElements": []' in serialized_data

    @patch('bobbcachesyncjob.services.redis_cache_service.RedisManager')
    @patch('bobbcachesyncjob.services.redis_cache_service.os.getenv', return_value='REDIS_DATABASE_BOBB')
    def test_insert_chunk_in_redis_cache_handles_empty_documents_list(self, mock_os_getenv, mock_redis_manager):
        redis_cache_service = RedisCacheService()
        documents = []
        redis_cache_service.insert_chunk_in_redis_cache(documents)
        assert not mock_redis_manager.return_value.master_connection.pipeline().set.called
