from copy import deepcopy
from typing import List, Tuple, Dict, Union

from otpcommon.aiguillage.model.aiguillage_extract_contract import AiguillageExtractContractRequest, \
    AiguillageExtractContractResponse
from otpcommon.bdds.model.bdds_extract_contract import BddsExtractContractRequest, BddsExtractContractResponse
from otpcommon.mls.model.server_info import ServerInfo

from otpextractrights.configuration import Logger
from otpextractrights.model import AiguillageOutputSuccess, BddsOutputSuccess, BddsOutputFail, OutputBeneficiary, \
    InputBeneficiary, AiguillageOutputFail, ValidationFail, MlsAmcFail
from otpextractrights.model.mls import MlsOutputFail
from otpextractrights.utils import AIGUILLAGE_ERROR_CODE, BDDS_ERROR_CODE
from otpextractrights.utils.errors import AIGUILLAGE_BENEF_NOT_FOUND, MLS_ERROR_CODE, BDDS_BENEF_NOT_FOUND, \
    BDDS_NO_CONTRACT, AIGUILLAGE_BENEF_NO_CONTRACT, TECHNICAL_ERROR


class ExtractContractMapper:

    @staticmethod
    def to_validation_fail(error_code, label_error) -> ValidationFail:
        return ValidationFail(
            code_erreur=error_code,
            libelle_erreur=label_error
        )

    @staticmethod
    def to_aiguillage_requests(input_beneficiaries: List[InputBeneficiary]) -> List[AiguillageExtractContractRequest]:
        aiguillage_requests: List[AiguillageExtractContractRequest] = [
            AiguillageExtractContractRequest(
                nir=benef.nir,
                birthDate=benef.date_naissance,
                searchDate=benef.date_soins,
                amc=benef.amc,
                hash=ExtractContractMapper.get_input_hash(benef),
            )
            for benef in input_beneficiaries
        ]
        return list(set(aiguillage_requests))

    @staticmethod
    def to_aiguillage_outputs(
            aiguillage_requests: List[AiguillageExtractContractRequest],
            aiguillage_responses: List[AiguillageExtractContractResponse]
    ) -> Tuple[
        Dict[str, List[AiguillageOutputSuccess]],
        Dict[str, AiguillageOutputFail]
    ]:
        requests_map: Dict[str, AiguillageExtractContractRequest] = {
            benef.hash: benef
            for benef in aiguillage_requests
        }
        successes = {}
        fails = {}
        for response in aiguillage_responses:
            for hash_, benef_values in response.content.items():
                req_benef = requests_map[hash_]
                successes[hash_] = list(set([AiguillageOutputSuccess(
                    amc=benef_response.insurer_id,
                    nir_benef=benef_response.insee_code,
                    date_soins=req_benef.searchDate,
                    date_naissance=benef_response.birth_date,
                ) for benef_response in benef_values]))
            for hash_, aiguillage_error in response.errors.items():
                Logger.warning(f"AIGUILLAGE ERROR: {aiguillage_error}")
                fails[hash_] = AiguillageOutputFail(
                    code_erreur=AIGUILLAGE_ERROR_CODE,
                    libelle_erreur=ExtractContractMapper.aiguillage_error_mapping(aiguillage_error.code),
                )
        return successes, fails

    @staticmethod
    def to_mls_outputs(
            aiguillage_successes: List[AiguillageOutputSuccess],
            server_infos: Dict[str, Union[ServerInfo, MlsAmcFail]]
    ) -> Tuple[Dict[str, BddsExtractContractRequest], Dict[str, MlsOutputFail]]:
        successes: Dict[str, BddsExtractContractRequest] = {}
        fails: Dict[str, MlsOutputFail] = {}
        for benef in aiguillage_successes:
            server_benef = server_infos[benef.amc]
            benef_hash = ExtractContractMapper.get_bdds_hash(benef)
            # amc not resolved properly by MLS
            if isinstance(server_benef, MlsAmcFail):
                fails[benef_hash] = MlsOutputFail(
                    code_erreur=MLS_ERROR_CODE,
                    libelle_erreur=server_benef.libelle_erreur
                )
            else:
                successes[benef_hash] = BddsExtractContractRequest(
                    nir=benef.nir_benef,
                    birthDate=benef.date_naissance,
                    searchDate=benef.date_soins,
                    amc=benef.amc,
                    hash=benef_hash
                )

        return successes, fails

    @staticmethod
    def to_bdds_outputs(
            bdds_response: List[BddsExtractContractResponse]
    ) -> Tuple[Dict[str, List[BddsOutputSuccess]], Dict[str, BddsOutputFail]]:
        successes = {}
        fails = {}
        for response in bdds_response:
            for benef_hash, benef_values in response.content.items():
                successes[benef_hash] = [BddsOutputSuccess(
                    contract=benef_response
                ) for benef_response in benef_values]
            for benef_hash, error_response in response.errors.items():
                fails[benef_hash] = BddsOutputFail(
                    code_erreur=BDDS_ERROR_CODE,
                    libelle_erreur=ExtractContractMapper.bdds_error_mapping(error_response.code)
                )
        return successes, fails

    @staticmethod
    def bdds_error_mapping(error_code: str):
        mapper = {
            "NEXT-SERVICEELIGIBILITY-CORE-EXTRACTED-404-1": BDDS_BENEF_NOT_FOUND,
            "NEXT-SERVICEELIGIBILITY-CORE-EXTRACTED-404-2": BDDS_NO_CONTRACT
        }
        return mapper.get(error_code, TECHNICAL_ERROR)

    @staticmethod
    def aiguillage_error_mapping(error_code: str):
        mapper = {
            "NEXT-AIGUILLAGE-CORE-EXTRACTED-404-1": AIGUILLAGE_BENEF_NOT_FOUND,
            "NEXT-AIGUILLAGE-CORE-EXTRACTED-404-2": AIGUILLAGE_BENEF_NO_CONTRACT
        }
        return mapper.get(error_code, TECHNICAL_ERROR)

    @staticmethod
    def to_output_beneficiaries(
            input_beneficiaries: List[InputBeneficiary],
            validation_fails: Dict[str, ValidationFail],
            aiguillage_fails: Dict[str, AiguillageOutputFail],
            aiguillage_success: Dict[str, List[AiguillageOutputSuccess]],
            mls_fails: Dict[str, MlsOutputFail],
            bdds_fails: Dict[str, BddsOutputFail],
            bdds_successes: Dict[str, List[BddsOutputSuccess]]
    ) -> List[OutputBeneficiary]:

        outputs_beneficiaries: List[OutputBeneficiary] = []

        for input_benef in input_beneficiaries:
            base_benef = OutputBeneficiary()
            base_benef.ligne = input_benef.ligne
            base_benef.nir_demande = input_benef.nir
            base_benef.date_naissance_demande = input_benef.date_naissance
            base_benef.date_soins_demande = input_benef.date_soins
            base_benef.amc_demande = input_benef.amc

            input_hash = ExtractContractMapper.get_input_hash(input_benef)

            if input_hash in validation_fails.keys():
                error = validation_fails[input_hash]
                base_benef.code_erreur = error.code_erreur
                base_benef.libelle_erreur = error.libelle_erreur
                outputs_beneficiaries.append(base_benef)
                continue

            if input_hash in aiguillage_fails.keys():
                error = aiguillage_fails[input_hash]
                base_benef.code_erreur = error.code_erreur
                base_benef.libelle_erreur = error.libelle_erreur
                outputs_beneficiaries.append(base_benef)
                continue

            try:
                aiguillage_success[input_hash]
            except KeyError:
                Logger.error(f"No aiguillage response mapping input benef {input_benef}")
                base_benef.code_erreur = AIGUILLAGE_ERROR_CODE
                base_benef.libelle_erreur = TECHNICAL_ERROR
                outputs_beneficiaries.append(base_benef)
                continue

            for aiguillage_response in aiguillage_success[input_hash]:
                benef_aiguillage = deepcopy(base_benef)

                benef_aiguillage.nir_benef = aiguillage_response.nir_benef
                benef_aiguillage.date_naissance = aiguillage_response.date_naissance
                benef_aiguillage.amc = aiguillage_response.amc

                bdds_hash = ExtractContractMapper.get_bdds_hash(aiguillage_response)

                if bdds_hash in mls_fails.keys():
                    error = mls_fails[bdds_hash]
                    benef_aiguillage.code_erreur = error.code_erreur
                    benef_aiguillage.libelle_erreur = error.libelle_erreur
                    outputs_beneficiaries.append(benef_aiguillage)
                    continue

                if bdds_hash in bdds_fails.keys():
                    error = bdds_fails[bdds_hash]
                    benef_aiguillage.code_erreur = error.code_erreur
                    benef_aiguillage.libelle_erreur = error.libelle_erreur
                    outputs_beneficiaries.append(benef_aiguillage)
                    continue

                try:
                    bdds_successes[bdds_hash]
                except KeyError:
                    Logger.error(f"No BDDS response mapping input benef {aiguillage_response}")
                    benef_aiguillage.code_erreur = BDDS_ERROR_CODE
                    benef_aiguillage.libelle_erreur = TECHNICAL_ERROR
                    outputs_beneficiaries.append(benef_aiguillage)
                    continue

                for bdds_response in bdds_successes[bdds_hash]:
                    benef_bdds = deepcopy(benef_aiguillage)

                    benef_bdds.nir_benef = bdds_response.contract.nirBeneficiaire
                    benef_bdds.nir_od1 = bdds_response.contract.nirOd1
                    benef_bdds.nir_od2 = bdds_response.contract.nirOd2
                    benef_bdds.date_naissance = bdds_response.contract.dateNaissance
                    benef_bdds.rang_naissance = bdds_response.contract.rangNaissance
                    benef_bdds.adherent = bdds_response.contract.numeroAdherent
                    benef_bdds.contrat = bdds_response.contract.numeroContrat
                    benef_bdds.date_donnees = bdds_response.contract.dateModification
                    for domain in bdds_response.contract.domains:
                        benef_domain = deepcopy(benef_bdds)

                        benef_domain.produit = domain.codeProduit
                        benef_domain.domaine = domain.codeDomaine
                        benef_domain.garantie = domain.codeGarantie
                        benef_domain.debut_droits = domain.periodeDebut
                        benef_domain.fin_droits = domain.periodeFin
                        benef_domain.reference_couverture = domain.referenceCouverture
                        benef_domain.numero_sts_formule = domain.codeFormule
                        benef_domain.formule_remboursement = domain.libelleFormule
                        benef_domain.taux_remboursement = domain.tauxRemboursement
                        benef_domain.unite_taux_remboursement = domain.uniteTauxRemboursement

                        outputs_beneficiaries.append(benef_domain)

        return outputs_beneficiaries

    @staticmethod
    def get_bdds_hash(aiguillage_output: AiguillageOutputSuccess) -> str:
        return f"{aiguillage_output.nir_benef}-{aiguillage_output.date_naissance}-{aiguillage_output.date_soins}-{aiguillage_output.amc}"

    @staticmethod
    def get_input_hash(beneficiary: InputBeneficiary) -> str:
        hash_ = f"{beneficiary.nir}-{beneficiary.date_naissance}-{beneficiary.date_soins}"
        if beneficiary.amc is not None:
            hash_ = f"{hash_}-{beneficiary.amc}"
        return hash_
