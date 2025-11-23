import json
import sys
from unittest.mock import Mock


sys.modules['organisationsettings.services.get_organisation_by_amc'] = Mock()
from otpextractrights.utils.errors import AIGUILLAGE_BENEF_NOT_FOUND
from otpextractrights.model.mls import MlsOutputSuccess, MlsOutputFail

import logging
from typing import List, Dict
from unittest import TestCase

from otpcommon.bdds.model.bdds_extract_contract import BddsExtractContractRequest, BddsExtractContractResponse
from otpextractrights.model import AiguillageOutputSuccess, InputBeneficiary, AiguillageOutputFail, \
    BddsOutputSuccess, BddsOutputFail, OutputBeneficiary
from otpextractrights.service.mapper import ExtractContractMapper
from otpextractrights.utils import AIGUILLAGE_ERROR_CODE, BDDS_ERROR_CODE
from otpcommon.aiguillage.model.aiguillage_extract_contract import AiguillageExtractContractRequest, \
    AiguillageExtractContractResponse
from otpextractrights.configuration import Logger

logger = logging.getLogger()
logger.level = logging.DEBUG
stream_handler = logging.StreamHandler(sys.stdout)


def load_file(filename: str):
    with open(f"data/mapper/{filename}", "r") as f:
        return json.load(f)


class TestMain(TestCase):

    @classmethod
    def setUpClass(cls) -> None:
        Logger.init_logger()

    def test_to_aiguillage_request(self):
        input_beneficiaries: List[InputBeneficiary] = [
            InputBeneficiary.from_dict(d) for d in load_file("1_input_beneficiary.json")]
        aiguillage_requests: List[AiguillageExtractContractRequest] = ExtractContractMapper.to_aiguillage_requests(
            input_beneficiaries=input_beneficiaries)
        self.assertEqual(len(aiguillage_requests), 3)
        self.assertEqual(
            1,
            len(list(filter(
                lambda x: x == AiguillageExtractContractRequest(
                    nir="1", birthDate="1", searchDate="1", amc="1", hash="1-1-1-1"),
                aiguillage_requests)))
        )

        self.assertEqual(
            1,
            len(list(filter(
                lambda x: x == AiguillageExtractContractRequest(
                    nir="1", birthDate="1", searchDate="2", amc="1", hash="1-1-2-1"),
                aiguillage_requests)))
        )

        self.assertEqual(
            1,
            len(list(filter(
                lambda x: x == AiguillageExtractContractRequest(
                    nir="2", birthDate="2", searchDate="2", amc=None, hash="2-2-2"),
                aiguillage_requests)))
        )

    def test_to_aiguillage_output(self):
        requests: List[AiguillageExtractContractRequest] = [
            AiguillageExtractContractRequest.from_dict(d) for d in
            load_file("1_aiguillage_extract_contract_requests.json")]

        responses: List[AiguillageExtractContractResponse] = [
            AiguillageExtractContractResponse.from_dict(d) for d in
            load_file("1_aiguillage_extract_contract_responses.json")]

        successes, fails = ExtractContractMapper.to_aiguillage_outputs(
            aiguillage_requests=requests,
            aiguillage_responses=responses
        )

        self.assertEqual(len(successes), 2)

        self.assertEqual(1, len(successes["1-1-2-1"]))
        self.assertEqual(1, len(successes["1-1-1-1"]))

        self.assertEqual(
            successes["1-1-1-1"][0],
            AiguillageOutputSuccess(amc="1", nir_benef="1", date_soins="1", date_naissance="1")
        )

        self.assertEqual(
            successes["1-1-2-1"][0],
            AiguillageOutputSuccess(amc="1", nir_benef="1", date_soins="2", date_naissance="1")
        )

        self.assertEqual(len(fails), 1)
        self.assertEqual(
            fails["2-2-2"],
            AiguillageOutputFail(code_erreur=AIGUILLAGE_ERROR_CODE, libelle_erreur=AIGUILLAGE_BENEF_NOT_FOUND)
        )

    def test_to_bdds_outputs(self):
        responses: List[BddsExtractContractResponse] = [
            BddsExtractContractResponse.from_dict(d) for d in load_file("1_bdds_extract_contract_responses.json")]
        successes, fails = ExtractContractMapper.to_bdds_outputs(bdds_response=responses)

        self.assertEqual(1, len(successes))
        self.assertTrue("1-1-1-1" in successes.keys())
        self.assertEqual(2, len(successes["1-1-1-1"]))

        self.assertEqual(1, len(fails))
        self.assertTrue("1-1-2-1" in fails.keys())

    def test_to_beneficiary_output(self):
        input_beneficiaries: List[InputBeneficiary] = [InputBeneficiary.from_dict(item) for item in load_file("1_input_beneficiary.json")]
        validation_fails = {}
        aiguillage_successes: Dict[str, List[AiguillageOutputSuccess]] = {
            key: list(set([AiguillageOutputSuccess.from_dict(item) for item in value])) for key, value in load_file("1_aiguillage_output_success.json").items()
        }

        aiguillage_fails: Dict[str, AiguillageOutputFail] = {
            key: AiguillageOutputFail.from_dict(value) for key, value in load_file("1_aiguillage_output_fail.json").items()
        }

        bdds_successes: Dict[str, List[BddsOutputSuccess]] = {
            key: [BddsOutputSuccess.from_dict(item) for item in value] for key, value in load_file("1_bdds_output_success.json").items()
        }

        bdds_fails: Dict[str, BddsOutputFail] = {
            key: BddsOutputFail.from_dict(value) for key, value in load_file("1_bdds_output_fail.json").items()
        }
        mls_fails: Dict[str, MlsOutputFail] = {}

        outputs: List[OutputBeneficiary] = ExtractContractMapper.to_output_beneficiaries(
            input_beneficiaries=input_beneficiaries,
            validation_fails=validation_fails,
            aiguillage_fails=aiguillage_fails,
            aiguillage_success=aiguillage_successes,
            mls_fails=mls_fails,
            bdds_fails=bdds_fails,
            bdds_successes=bdds_successes,
        )

        self.assertEqual(11, len(outputs))

        # Assert one with all fields
        self.assertEqual(
            outputs[0],
            OutputBeneficiary(ligne=1, nir_demande='1', date_naissance_demande='1', date_soins_demande='1', amc_demande='1',
                              amc='1', nir_benef='nirBeneficiaire', nir_od1='nirOd1', nir_od2='nirOd2',
                              date_naissance='dateNaissance', rang_naissance='rangNaissance1',
                              adherent='numeroAdherent', contrat='numeroContrat1', produit='codeProduit1',
                              garantie='codeGarantie',domaine='codeDomaine1', debut_droits='periodeDebut',
                              fin_droits='periodeFin',reference_couverture='referenceCouverture',
                              numero_sts_formule='codeFormule',formule_remboursement='libelleFormule',
                              taux_remboursement='tauxRemboursement',unite_taux_remboursement='uniteTauxRemboursement',
                              date_donnees='dateModification', code_erreur=None, libelle_erreur=None)
        )

        self.assertEqual(outputs[1].rang_naissance, 'rangNaissance1')
        self.assertEqual(outputs[1].produit, 'codeProduit1')
        self.assertEqual(outputs[1].domaine, 'codeDomaine2')

        self.assertEqual(outputs[2].rang_naissance, 'rangNaissance1')
        self.assertEqual(outputs[2].produit, 'codeProduit2')
        self.assertEqual(outputs[2].domaine, 'codeDomaine1')

        self.assertEqual(outputs[3].rang_naissance, 'rangNaissance1')
        self.assertEqual(outputs[3].produit, 'codeProduit2')
        self.assertEqual(outputs[3].domaine, 'codeDomaine2')

        self.assertEqual(outputs[4].rang_naissance, 'rangNaissance2')
        self.assertEqual(outputs[4].produit, 'codeProduit1')
        self.assertEqual(outputs[4].domaine, 'codeDomaine1')

        self.assertEqual(outputs[5].rang_naissance, 'rangNaissance2')
        self.assertEqual(outputs[5].produit, 'codeProduit1')
        self.assertEqual(outputs[5].domaine, 'codeDomaine2')

        self.assertEqual(outputs[6].rang_naissance, 'rangNaissance2')
        self.assertEqual(outputs[6].produit, 'codeProduit2')
        self.assertEqual(outputs[6].domaine, 'codeDomaine1')

        self.assertEqual(outputs[7].rang_naissance, 'rangNaissance2')
        self.assertEqual(outputs[7].produit, 'codeProduit2')
        self.assertEqual(outputs[7].domaine, 'codeDomaine2')

        self.assertEqual(outputs[8].code_erreur, BDDS_ERROR_CODE)
        self.assertEqual(outputs[8].libelle_erreur, "NO_BENEF")

        self.assertEqual(outputs[9].code_erreur, BDDS_ERROR_CODE)
        self.assertEqual(outputs[9].libelle_erreur, "NO_BENEF")

        self.assertEqual(outputs[10].code_erreur, AIGUILLAGE_ERROR_CODE)
        self.assertEqual(outputs[10].libelle_erreur, "NO_BENEF")
