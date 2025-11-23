import os
import unittest
from unittest.mock import Mock, patch
from datetime import datetime
from bobbhistorizationjob.common.common_constants import COMMON_INDEX_BASENAME_VAR_ENV
from bobbhistorizationjob.services.historization_service import HistorizationService


class TestHistorizationService(unittest.TestCase):

    @patch('bobbhistorizationjob.services.historization_service.OpenSearchConnection')
    @patch.dict(os.environ, {COMMON_INDEX_BASENAME_VAR_ENV: "test_basename"})
    def setUp(self, opensearch_connection_mock):
        self.service = HistorizationService()

    @patch('bobbhistorizationjob.services.historization_service.OpenSearchConnection')
    def test_insert_document(self, opensearch_connection_mock):
        self.service.client.index = Mock()

        document = {
            "_id": "123",
            "codeAMC": "",
            "codeInsurer": "ASSU00187",
            "codeContractElement": "FSSC46380A",
            "label": "FSSC46380A",
            "productElements": [
                {
                    "codeOffer": "EEE888222",
                    "codeProduct": "KKKK12956A",
                    "codeBenefitNature": "ajout 2",
                    "codeAmc": "ASSU00076",
                    "from": "2021-03-12",
                    "effectiveDate": "2019-12-26"
                }
            ],
            "ignored": True,
            "origine": "IMPORT",
            "user": "jlawling1@hud.gov"
        }

        self.service.historize_contract_element(document)
        self.service.client.index.assert_called_once_with(index=self.service.alias, body={
            "codeAMC": "",
            "codeInsurer": "ASSU00187",
            "codeContractElement": "FSSC46380A",
            "label": "FSSC46380A",
            "productElements": [
                {
                    "codeOffer": "EEE888222",
                    "codeProduct": "KKKK12956A",
                    "codeBenefitNature": "ajout 2",
                    "codeAmc": "ASSU00076",
                    "from": "2021-03-12",
                    "effectiveDate": "2019-12-26"
                }
            ],
            "ignored": True,
            "origine": "IMPORT",
            "user": "jlawling1@hud.gov",
            "indexingDate": datetime.now().strftime('%Y-%m-%dT%H:%M:%S')
        })

    @patch('bobbhistorizationjob.services.historization_service.OpenSearchConnection')
    def test_insert_documents(self, opensearch_connection_mock):
        self.service.client.bulk = Mock()

        documents = [
            {
                "_id": "123",
                "codeAMC": "",
                "codeInsurer": "ASSU00076",
                "codeContractElement": "FSOA18936A",
                "label": "FSOA18936A",
                "productElements": [
                    {
                        "codeOffer": "ZZZ18935",
                        "codeProduct": "RSOA18936A",
                        "codeBenefitNature": "modif",
                        "codeAmc": "ASSU11076",
                        "from": "2021-05-17",
                        "effectiveDate": "2017-03-12"
                    }
                ],
                "ignored": False,
                "origine": "IMPORT",
                "user": "jgorton0@blogs.com"
            },
            {
                "_id": "456",
                "codeAMC": "",
                "codeInsurer": "ASSU00187",
                "codeContractElement": "FSSC46380A",
                "label": "FSSC46380A",
                "productElements": [
                    {
                        "codeOffer": "EEE888222",
                        "codeProduct": "KKKK12956A",
                        "codeBenefitNature": "ajout 2",
                        "codeAmc": "ASSU00076",
                        "from": "2021-03-12",
                        "effectiveDate": "2019-12-26"
                    }
                ],
                "ignored": True,
                "origine": "IMPORT",
                "user": "jlawling1@hud.gov"
            }
        ]

        self.service.bulk_historize_contract_elements(documents)

        expected_bulk_data = [
            {"index": {"_index": self.service.alias}},
            {
                "codeAMC": "",
                "codeInsurer": "ASSU00076",
                "codeContractElement": "FSOA18936A",
                "label": "FSOA18936A",
                "productElements": [
                    {
                        "codeOffer": "ZZZ18935",
                        "codeProduct": "RSOA18936A",
                        "codeBenefitNature": "modif",
                        "codeAmc": "ASSU11076",
                        "from": "2021-05-17",
                        "effectiveDate": "2017-03-12"
                    }
                ],
                "ignored": False,
                "origine": "IMPORT",
                "user": "jgorton0@blogs.com",
                "indexingDate": datetime.now().strftime('%Y-%m-%dT%H:%M:%S')
            },
            {"index": {"_index": self.service.alias}},
            {
                "codeAMC": "",
                "codeInsurer": "ASSU00187",
                "codeContractElement": "FSSC46380A",
                "label": "FSSC46380A",
                "productElements": [
                    {
                        "codeOffer": "EEE888222",
                        "codeProduct": "KKKK12956A",
                        "codeBenefitNature": "ajout 2",
                        "codeAmc": "ASSU00076",
                        "from": "2021-03-12",
                        "effectiveDate": "2019-12-26"
                    }
                ],
                "ignored": True,
                "origine": "IMPORT",
                "user": "jlawling1@hud.gov",
                "indexingDate": datetime.now().strftime('%Y-%m-%dT%H:%M:%S')
            }
        ]
        self.service.client.bulk.assert_called_once_with(body=expected_bulk_data)
