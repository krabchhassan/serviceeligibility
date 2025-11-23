import os
import sys
from typing import List
from unittest.mock import Mock


sys.modules['organisationsettings.services.get_organisation_by_amc'] = Mock()
from otpextractrights.configuration import Logger
from otpextractrights.exceptions import EmptyFileException, MissingColumnsException, MissingFileException
from otpextractrights.model import InputBeneficiary, OutputBeneficiary


from unittest import TestCase

os.environ['BEYOND_INSTANCE_TYPE'] = 'DEDICATED'


class TestMain(TestCase):

    @classmethod
    def setUpClass(cls) -> None:
        Logger.init_logger()

    def test_load_input(self):
        from otpextractrights.service import InputOutputService
        inputs: List[InputBeneficiary] = InputOutputService._get_input("data/input_output/input.csv")
        self.assertEqual(len(inputs), 4)
        self.assertEqual(
            inputs[0],
            InputBeneficiary(ligne=1, nir='1041062498044', date_naissance='041223', date_soins='20230301', amc='0123456789')
        )
        self.assertEqual(
            inputs[1],
            InputBeneficiary(ligne=2, nir='1181075111121', date_naissance='180230', date_soins='20230301', amc=None)
        )
        self.assertEqual(
            inputs[2],
            InputBeneficiary(ligne=3, nir='1200531125129', date_naissance=None, date_soins='20230301', amc='0123456789')
        )
        self.assertEqual(
            inputs[3],
            InputBeneficiary(ligne=4, nir='1350966679569', date_naissance='AAAAA', date_soins='20230301', amc=None)
        )

    def test_output(self):
        from otpextractrights.service import InputOutputService
        output_path = "data/input_output/output.csv"
        if os.path.exists(output_path):
            os.remove(output_path)
        self.assertFalse(os.path.exists(output_path))
        outputs = [
            OutputBeneficiary(nir_demande='1', date_naissance_demande='1', date_soins_demande='1', amc_demande='1',
                              amc='1', nir_benef='nirBeneficiaire', nir_od1='nirOd1', nir_od2='nirOd2',
                              date_naissance='dateNaissance', rang_naissance='rangNaissance1',
                              adherent='numeroAdherent', contrat='numeroContrat1', produit='codeProduit1',
                              domaine='codeDomaine1', debut_droits='periodeDebut', fin_droits='periodeFin',
                              reference_couverture='referenceCouverture', numero_sts_formule='codeFormule',
                              formule_remboursement='libelleFormule', taux_remboursement='tauxRemboursement',
                              unite_taux_remboursement='uniteTauxRemboursement', date_donnees='dateModification',
                              code_erreur=None, libelle_erreur=None),
            OutputBeneficiary(nir_demande='1', date_naissance_demande='1', date_soins_demande='1', amc_demande='1',
                              code_erreur="error_code", libelle_erreur="error_label")
        ]
        InputOutputService._save_output(outputs, output_path)
        self.assertTrue(os.path.exists(output_path))

    def test_empty_input(self):
        from otpextractrights.service import InputOutputService
        with self.assertRaises(EmptyFileException) as context:
            InputOutputService._get_input("data/input_output/empty_1.csv")
        with self.assertRaises(EmptyFileException) as context:
            InputOutputService._get_input("data/input_output/empty_2.csv")

    def test_missing_column(self):
        from otpextractrights.service import InputOutputService
        with self.assertRaises(MissingColumnsException) as context:
            InputOutputService._get_input("data/input_output/missing_column.csv")

    def test_missing_file(self):
        from otpextractrights.service import InputOutputService
        with self.assertRaises(MissingFileException) as context:
            InputOutputService._get_input("data/input_output/missing_file.csv")
