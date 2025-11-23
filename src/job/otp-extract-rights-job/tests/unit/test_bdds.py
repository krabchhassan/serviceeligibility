import os
import json
import sys
from unittest.mock import Mock

sys.modules['organisationsettings.services.get_organisation_by_amc'] = Mock()
from otpextractrights.model import MlsAmcFail
from otpextractrights.configuration import Logger

from otpcommon.bdds.model.bdds_extract_contract import BddsExtractContractRequest
from otpcommon.mls.model.server_info import ServerInfo
import logging
from unittest import TestCase
import asyncio

logger = logging.getLogger()
logger.level = logging.DEBUG
stream_handler = logging.StreamHandler(sys.stdout)
os.environ['BEYOND_INSTANCE_TYPE'] = 'DEDICATED'


def load_file(filename: str):
    with open(f"data/bdds/{filename}", "r") as f:
        return json.load(f)


class TestMain(TestCase):

    @classmethod
    def setUpClass(cls) -> None:
        Logger.init_logger()
        loop = asyncio.new_event_loop()
        asyncio.set_event_loop(loop)

    def test_populate_queue(self):
        from otpextractrights.service import BddsWorker
        worker = BddsWorker()
        bdds_requests = [
            BddsExtractContractRequest(nir="1", birthDate="1", searchDate="1", amc="1", type="1", hash="1-1-1-1"),
            BddsExtractContractRequest(nir="1", birthDate="2", searchDate="1", amc="1", type="1", hash="1-2-1-1"),
            BddsExtractContractRequest(nir="1", birthDate="3", searchDate="1", amc="1", type="1", hash="1-3-1-1"),
            BddsExtractContractRequest(nir="1", birthDate="1", searchDate="1", amc="2", type="1", hash="1-1-1-2"),
            BddsExtractContractRequest(nir="1", birthDate="2", searchDate="1", amc="2", type="1", hash="1-1-1-3"),
            BddsExtractContractRequest(nir="1", birthDate="1", searchDate="1", amc="3", type="1", hash="1-1-1-3")
        ]
        server_infos = {"1": ServerInfo(base_url="1", amc="1"), "2": ServerInfo(base_url="2", amc="2"), "3": MlsAmcFail(amc="3")}
        queue = asyncio.Queue()

        worker._populate_queue(
            beneficiaries=bdds_requests,
            servers=server_infos,
            queue=queue
        )

        self.assertEqual(queue.qsize(), 2)
