from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import dataclass_json
from otpcommon import default_json_config

from otpextractrights.model import AiguillageOutputSuccess


@dataclass_json
@dataclass
class MlsAmcFail:
    amc: Optional[str] = field(default=None, metadata=default_json_config)
    libelle_erreur: Optional[str] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class MlsOutputSuccess:
    origin_benef: Optional[AiguillageOutputSuccess] = field(default=None, metadata=default_json_config)
    hash: Optional[str] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class MlsOutputFail:
    code_erreur: Optional[str] = field(default=None, metadata=default_json_config)
    libelle_erreur: Optional[str] = field(default=None, metadata=default_json_config)
