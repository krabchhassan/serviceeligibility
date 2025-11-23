from unittest import TestCase

from otpcommon.aiguillage.model.aiguillage_dto import AiguillageRequest
from otpcommon.bdds.model.bdds_search_dto import BddsSearchRequest
from otpcommon.bdds.service.mapper import Mapper
from utils import load_from_file


class TestModel(TestCase):

    def test_init_bdds_request(self):
        # INIT
        data: dict = load_from_file("bdds_request_test")
        request = BddsSearchRequest.from_dict(data)
        # VERIFY
        self.assertIsInstance(request, BddsSearchRequest)
        self.assertEqual(request.name, "THOMAS")
        self.assertEqual(request.first_name, None)

    def test_aiguillage_request(self):
        # INIT
        data: dict = load_from_file("aiguillage_request_test")
        request = AiguillageRequest.from_dict(data)
        # VERIFY
        self.assertIsInstance(request, AiguillageRequest)
        self.assertEqual(request.insee_code, "2810631555490")
        self.assertNotEqual(request.firstname, "laurent")
        self.assertEqual(request.firstname, None)

    def test_mapper_bdds_to_aiguillage(self):
        # INIT
        data: dict = load_from_file("bdds_request_test")
        bdds_request: BddsSearchRequest = BddsSearchRequest.from_dict(data)
        # RUN
        aiguillage_request = Mapper.bdds_to_aiguillage(bdds_request)
        # VERIFY
        self.assertIsInstance(aiguillage_request, AiguillageRequest)
        self.assertEqual(aiguillage_request.lastname, "THOMAS")
        self.assertEqual(aiguillage_request.insurer_id, None)
