from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import config, Undefined
from dataclasses_json import dataclass_json
from otpcommon import exclude_if_none

json_config = config(undefined=Undefined.EXCLUDE, exclude=exclude_if_none)


@dataclass_json
@dataclass
class OutputBeneficiary:
    ligne: Optional[int] = field(default=0, metadata=json_config)
    nir_demande: Optional[str] = field(default=None, metadata=json_config)
    date_naissance_demande: Optional[str] = field(default=None, metadata=json_config)
    date_soins_demande: Optional[str] = field(default=None, metadata=json_config)
    amc_demande: Optional[str] = field(default=None, metadata=json_config)
    amc: Optional[str] = field(default=None, metadata=json_config)
    nir_benef: Optional[str] = field(default=None, metadata=json_config)
    nir_od1: Optional[str] = field(default=None, metadata=json_config)
    nir_od2: Optional[str] = field(default=None, metadata=json_config)
    date_naissance: Optional[str] = field(default=None, metadata=json_config)
    rang_naissance: Optional[str] = field(default=None, metadata=json_config)
    adherent: Optional[str] = field(default=None, metadata=json_config)
    contrat: Optional[str] = field(default=None, metadata=json_config)
    produit: Optional[str] = field(default=None, metadata=json_config)
    domaine: Optional[str] = field(default=None, metadata=json_config)
    garantie: Optional[str] = field(default=None, metadata=json_config)
    debut_droits: Optional[str] = field(default=None, metadata=json_config)
    fin_droits: Optional[str] = field(default=None, metadata=json_config)
    reference_couverture: Optional[str] = field(default=None, metadata=json_config)
    numero_sts_formule: Optional[str] = field(default=None, metadata=json_config)
    formule_remboursement: Optional[str] = field(default=None, metadata=json_config)
    taux_remboursement: Optional[str] = field(default=None, metadata=json_config)
    unite_taux_remboursement: Optional[str] = field(default=None, metadata=json_config)
    date_donnees: Optional[str] = field(default=None, metadata=json_config)
    code_erreur: Optional[str] = field(default=None, metadata=json_config)
    libelle_erreur: Optional[str] = field(default=None, metadata=json_config)
