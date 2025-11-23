from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import dataclass_json
from otpcommon import default_json_config


@dataclass_json
@dataclass
class InputBeneficiary:
    ligne: Optional[int] = field(default=0, metadata=default_json_config)
    nir: Optional[str] = field(default=None, metadata=default_json_config)
    date_naissance: Optional[str] = field(default=None, metadata=default_json_config)
    date_soins: Optional[str] = field(default=None, metadata=default_json_config)
    amc: Optional[str] = field(default=None, metadata=default_json_config)


class InputFields:
    NIR = "nir"
    DATE_NAISSANCE = "date_naissance"
    DATE_SOINS = "date_soins"
    AMC = "amc"
