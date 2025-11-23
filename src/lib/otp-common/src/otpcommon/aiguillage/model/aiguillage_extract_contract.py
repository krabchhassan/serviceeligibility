from dataclasses import dataclass, field
from typing import Optional, List, Dict

from dataclasses_json import dataclass_json

from otpcommon import default_json_config

from .aiguillage_dto import AiguillageResponse


@dataclass_json
@dataclass
class AiguillageError:
    code: Optional[str] = field(default=None, metadata=default_json_config)
    msg: Optional[str] = field(default=None, metadata=default_json_config)
    request: Optional[dict] = field(default=dict, metadata=default_json_config)


@dataclass_json
@dataclass
class AiguillageExtractContractResponse:
    content: Dict[str, List[AiguillageResponse]]
    errors: Dict[str, AiguillageError]


@dataclass_json
@dataclass
class AiguillageExtractContractRequest:
    nir: Optional[str] = field(default=None, metadata=default_json_config)
    birthDate: Optional[str] = field(default=None, metadata=default_json_config)
    searchDate: Optional[str] = field(default=None, metadata=default_json_config)
    amc: Optional[str] = field(default=None, metadata=default_json_config)
    hash: Optional[str] = field(default=None, metadata=default_json_config)

    def __hash__(self):
        return hash(self.hash)

    def __eq__(self, other):
        return self.hash == other.hash
