from unittest import TestCase, mock
from unittest.mock import MagicMock
from datetime import datetime
from bobbexportjob.services.extract_contract_element_service import ExtractContractElementService


class TestExtractContractElementService(TestCase):

    def setUp(self):
        self.db_manager_mock = MagicMock()
        self.contract_service = ExtractContractElementService(self.db_manager_mock)

    def test_get_all_contract_elements(self):
        mock_contract_elements = [
            {"codeAMC": "BALOO", "codeInsurer": "ASS_BALOO", "codeContractElement": "BAL_BASMOD010",
             "label": "BAL_BASMOD010", "productElements": [
                {"codeOffer": "BLUE_TP", "codeProduct": "SANTE_100", "codeBenefitNature": "OPTIQUE",
                 "codeAmc": "BALOO", "from": "2020-01-01", "effectiveDate": "2020-01-01"},
                {"codeOffer": "BLUE_TP", "codeProduct": "SANTE_100",
                 "codeBenefitNature": "PHARMACIE", "codeAmc": "BALOO", "from": "2022-07-01",
                 "effectiveDate": "2020-01-01"}], "ignored": "false", "origine": "IMPORT"}]
        self.contract_service.contract_repository.get_all_contract_elements = MagicMock(
            return_value=mock_contract_elements)

        contract_elements = self.contract_service.get_all_contract_elements()

        self.assertEqual(contract_elements, mock_contract_elements)
        self.contract_service.contract_repository.get_all_contract_elements.assert_called_once()

    def test_serialize_item(self):
        contract_elements = [
            {
                "codeAMC": "BALOO",
                "codeInsurer": "ASS_BALOO",
                "codeContractElement": "BAL_BASMOD010",
                "label": "BAL_BASMOD010",
                "productElements": [
                    {
                        "codeOffer": "BLUE_TP",
                        "codeProduct": "SANTE_100",
                        "codeBenefitNature": "OPTIQUE",
                        "codeAmc": "BALOO",
                        "from": datetime(2020, 1, 1),
                        "effectiveDate": datetime(2020, 1, 1)
                    },
                    {
                        "codeOffer": "BLUE_TP",
                        "codeProduct": "SANTE_100",
                        "codeBenefitNature": "PHARMACIE",
                        "codeAmc": "BALOO",
                        "from": datetime(2022, 7, 1),
                        "effectiveDate": datetime(2020, 1, 1)
                    }
                ],
                "ignored": False,
                "origine": "IMPORT"
            }
        ]

        expected_serialized_item = {
            "codeAMC": "BALOO",
            "codeInsurer": "ASS_BALOO",
            "codeContractElement": "BAL_BASMOD010",
            "label": "BAL_BASMOD010",
            "productElements": [
                {
                    "codeOffer": "BLUE_TP",
                    "codeProduct": "SANTE_100",
                    "codeBenefitNature": "OPTIQUE",
                    "codeAmc": "BALOO",
                    "from": "2020-01-01",
                    "effectiveDate": "2020-01-01"
                },
                {
                    "codeOffer": "BLUE_TP",
                    "codeProduct": "SANTE_100",
                    "codeBenefitNature": "PHARMACIE",
                    "codeAmc": "BALOO",
                    "from": "2022-07-01",
                    "effectiveDate": "2020-01-01"
                }
            ],
            "ignored": "false",
            "origine": "IMPORT"
        }

        serialized_item = self.contract_service.serialize_item(contract_elements[0])

        self.assertEqual(serialized_item, expected_serialized_item)
