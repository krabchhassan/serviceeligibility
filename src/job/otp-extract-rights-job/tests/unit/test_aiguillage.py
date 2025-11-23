import os
import json
import sys
from unittest.mock import Mock
sys.modules['organisationsettings.services.get_organisation_by_amc'] = Mock()

import logging
from typing import List
from unittest import TestCase
from unittest.mock import MagicMock, AsyncMock
import asyncio
from otpcommon.aiguillage.model.aiguillage_extract_contract import AiguillageExtractContractRequest, \
    AiguillageExtractContractResponse


logger = logging.getLogger()
logger.level = logging.DEBUG
stream_handler = logging.StreamHandler(sys.stdout)
os.environ['BEYOND_INSTANCE_TYPE'] = 'DEDICATED'

def load_file(filename: str):
    with open(f"data/aiguillage/{filename}", "r") as f:
        return json.load(f)


class TestMain(TestCase):

    @classmethod
    def setUpClass(cls) -> None:
        from otpextractrights.configuration import Logger
        Logger.init_logger()

    async def mock_aiguillage_multitasking(self, worker_id: int, requests: List[AiguillageExtractContractRequest]):
        data = load_file("multitasking_responses.json")
        await asyncio.sleep(0.2)
        return AiguillageExtractContractResponse.from_dict(data[requests[0].hash])

    def test_multitasking(self):
        from otpextractrights.service.aiguillage_worker import AiguillageWorker
        worker = AiguillageWorker()
        worker.AIGUILLAGE_BATCH_SIZE = 1
        worker.aiguillage_service.find_all_extract = MagicMock(side_effect=self.mock_aiguillage_multitasking)

        input_data: List[AiguillageExtractContractRequest] = [
            AiguillageExtractContractRequest.from_dict(d) for d in load_file("multitasking_data.json")]
        with self.assertLogs(logger="otpextractrights") as cm:
            output_df = worker.run(input_data, 2)
            # Assert order of the output log to ensure multitasking
            """

                self.assertEqual(cm.output, [
                "INFO:otpextractrights:aiguillage worker 0 starting",
                "INFO:otpextractrights:aiguillage worker 0 consuming queue",
                "INFO:otpextractrights:aiguillage worker 1 starting",
                "INFO:otpextractrights:aiguillage worker 1 consuming queue",
                "INFO:otpextractrights:aiguillage worker 0 consuming queue",
                "INFO:otpextractrights:aiguillage worker 1 consuming queue",
                'INFO:otpextractrights:aiguillage worker 0 shut down - queue empty',
                'INFO:otpextractrights:aiguillage worker 1 shut down - queue empty'
            ])
            # Output have been succefully merged
            self.assertEqual(len(output_df), 4)"""

    def test_simple_success_and_fail(self):
        from otpextractrights.service.aiguillage_worker import AiguillageWorker
        worker = AiguillageWorker()
        aiguillage_response = AiguillageExtractContractResponse.from_dict(load_file("simple_response.json"))
        worker.aiguillage_service.find_all_extract = AsyncMock(return_value=aiguillage_response)

        input_data: List[AiguillageExtractContractRequest] = [
            AiguillageExtractContractRequest.from_dict(d) for d in load_file("simple_input.json")]
        output_data: List[AiguillageExtractContractResponse] = worker.run(input_data)
        # Only One request made (bulked)
        self.assertEqual(len(output_data), 1)
        self.assertEqual(len(output_data[0].content), 1)
        self.assertEqual(len(output_data[0].errors), 1)

    def test_simple_only_success(self):
        from otpextractrights.service.aiguillage_worker import AiguillageWorker
        worker = AiguillageWorker()
        aiguillage_response = AiguillageExtractContractResponse.from_dict(load_file("simple_no_fail_response.json"))
        worker.aiguillage_service.find_all_extract = AsyncMock(return_value=aiguillage_response)

        input_data: List[AiguillageExtractContractRequest] = [
            AiguillageExtractContractRequest.from_dict(d) for d in load_file("simple_input.json")]
        output_data = worker.run(input_data)
        # Only One request made (bulked)
        self.assertEqual(len(output_data), 1)
        self.assertEqual(len(output_data[0].content), 2)
        self.assertEqual(len(output_data[0].errors), 0)

    def test_simple_only_fails(self):
        from otpextractrights.service.aiguillage_worker import AiguillageWorker
        worker = AiguillageWorker()
        aiguillage_response = AiguillageExtractContractResponse.from_dict(load_file("simple_no_success_response.json"))
        worker.aiguillage_service.find_all_extract = AsyncMock(return_value=aiguillage_response)

        input_data: List[AiguillageExtractContractRequest] = [
            AiguillageExtractContractRequest.from_dict(d) for d in load_file("simple_input.json")]
        output_data = worker.run(input_data)
        # Only One request made (bulked)
        self.assertEqual(len(output_data), 1)
        self.assertEqual(len(output_data[0].content), 0)
        self.assertEqual(len(output_data[0].errors), 2)
