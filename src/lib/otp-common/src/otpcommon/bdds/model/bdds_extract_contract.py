from dataclasses import dataclass, field
from typing import Optional, List, Dict
from dataclasses_json import dataclass_json

from otpcommon import default_json_config


@dataclass_json
@dataclass
class BddsExtractContractRequest:
    nir: Optional[str] = field(default=None, metadata=default_json_config)
    birthDate: Optional[str] = field(default=None, metadata=default_json_config)
    searchDate: Optional[str] = field(default=None, metadata=default_json_config)
    amc: Optional[str] = field(default=None, metadata=default_json_config)
    type: Optional[str] = field(default=None, metadata=default_json_config)
    hash: Optional[str] = field(default=None, metadata=default_json_config)

    def __hash__(self):
        return hash(self.hash)

    def __eq__(self, other):
        return self.hash == other.hash


@dataclass_json
@dataclass
class BddsDomainResponse:
    codeDomaine: Optional[str] = field(default=None, metadata=default_json_config)
    typePeriode: Optional[str] = field(default=None, metadata=default_json_config)
    periodeDebut: Optional[str] = field(default=None, metadata=default_json_config)
    periodeFin: Optional[str] = field(default=None, metadata=default_json_config)
    periodeFinFermeture: Optional[str] = field(default=None, metadata=default_json_config)
    codeGarantie: Optional[str] = field(default=None, metadata=default_json_config)
    libelleGarantie: Optional[str] = field(default=None, metadata=default_json_config)
    codeProduit: Optional[str] = field(default=None, metadata=default_json_config)
    libelleProduit: Optional[str] = field(default=None, metadata=default_json_config)
    referenceCouverture: Optional[str] = field(default=None, metadata=default_json_config)
    tauxRemboursement: Optional[str] = field(default=None, metadata=default_json_config)
    uniteTauxRemboursement: Optional[str] = field(default=None, metadata=default_json_config)
    codeFormule: Optional[str] = field(default=None, metadata=default_json_config)
    libelleFormule: Optional[str] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class BddsContractResponse:
    idDeclarant: Optional[str] = field(default=None, metadata=default_json_config)
    numeroContrat: Optional[str] = field(default=None, metadata=default_json_config)
    numeroAdherent: Optional[str] = field(default=None, metadata=default_json_config)
    dateSouscription: Optional[str] = field(default=None, metadata=default_json_config)
    dateResiliation: Optional[str] = field(default=None, metadata=default_json_config)
    dateRestitution: Optional[str] = field(default=None, metadata=default_json_config)
    dateCreation: Optional[str] = field(default=None, metadata=default_json_config)
    dateModification: Optional[str] = field(default=None, metadata=default_json_config)
    dateNaissance: Optional[str] = field(default=None, metadata=default_json_config)
    rangNaissance: Optional[str] = field(default=None, metadata=default_json_config)
    nirBeneficiaire: Optional[str] = field(default=None, metadata=default_json_config)
    cleNirBeneficiaire: Optional[str] = field(default=None, metadata=default_json_config)
    nirOd1: Optional[str] = field(default=None, metadata=default_json_config)
    cleNirOd1: Optional[str] = field(default=None, metadata=default_json_config)
    nirOd2: Optional[str] = field(default=None, metadata=default_json_config)
    cleNirOd2: Optional[str] = field(default=None, metadata=default_json_config)
    domains: List[BddsDomainResponse] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class BddsExtractContractResponseError:
    code: Optional[str] = field(default=None, metadata=default_json_config)
    msg: Optional[str] = field(default=None, metadata=default_json_config)
    request: Optional[dict] = field(default=dict, metadata=default_json_config)


@dataclass_json
@dataclass
class BddsExtractContractResponse:
    content: Optional[Dict[str, List[BddsContractResponse]]] = field(default=dict, metadata=default_json_config)
    errors: Optional[Dict[str, BddsExtractContractResponseError]] = field(default=dict, metadata=default_json_config)
