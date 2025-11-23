from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import dataclass_json
from otpcommon import default_json_config


@dataclass_json
@dataclass
class ValidationFail:
    code_erreur: Optional[str] = field(default=None, metadata=default_json_config)
    libelle_erreur: Optional[str] = field(default=None, metadata=default_json_config)
