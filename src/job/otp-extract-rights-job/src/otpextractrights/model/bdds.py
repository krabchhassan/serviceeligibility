from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import dataclass_json
from otpcommon import default_json_config
from otpcommon.bdds.model.bdds_extract_contract import BddsContractResponse


@dataclass_json
@dataclass
class BddsOutputSuccess:
    contract: BddsContractResponse = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class BddsOutputFail:
    code_erreur: Optional[str] = field(default=None, metadata=default_json_config)
    libelle_erreur: Optional[str] = field(default=None, metadata=default_json_config)
