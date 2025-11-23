from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import dataclass_json
from otpcommon import default_json_config


@dataclass_json
@dataclass
class AiguillageOutputSuccess:
    nir_benef: Optional[str] = field(default=None, metadata=default_json_config)
    date_naissance: Optional[str] = field(default=None, metadata=default_json_config)
    date_soins: Optional[str] = field(default=None, metadata=default_json_config)
    amc: Optional[str] = field(default=None, metadata=default_json_config)

    def __hash__(self):
        return hash((self.nir_benef, self.date_naissance, self.date_soins, self.amc))

    def __eq__(self, other):
        if not isinstance(other, AiguillageOutputSuccess):
            return False
        return (
                self.nir_benef == other.nir_benef and
                self.date_naissance == other.date_naissance and
                self.date_soins == other.date_soins and
                self.amc == other.amc
        )


@dataclass_json
@dataclass
class AiguillageOutputFail:
    code_erreur: Optional[str] = field(default=None, metadata=default_json_config)
    libelle_erreur: Optional[str] = field(default=None, metadata=default_json_config)
