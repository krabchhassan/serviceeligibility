from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import dataclass_json

from otpcommon import default_json_config


@dataclass_json
@dataclass
class AiguillageResponse:
    lastname: Optional[str] = field(default=None, metadata=default_json_config)
    firstname: Optional[str] = field(default=None, metadata=default_json_config)
    insee_code: Optional[str] = field(default=None, metadata=default_json_config)
    birth_date: Optional[str] = field(default=None, metadata=default_json_config)
    birth_rank: Optional[str] = field(default=None, metadata=default_json_config)
    insurer_id: Optional[str] = field(default=None, metadata=default_json_config)
    start_date: Optional[str] = field(default=None, metadata=default_json_config)
    end_date: Optional[str] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class AiguillageRequest:
    lastname: Optional[str] = field(default=None, metadata=default_json_config)
    firstname: Optional[str] = field(default=None, metadata=default_json_config)
    insee_code: Optional[str] = field(default=None, metadata=default_json_config)
    birth_date: Optional[str] = field(default=None, metadata=default_json_config)
    birth_rank: Optional[str] = field(default=None, metadata=default_json_config)
    insurer_id: Optional[str] = field(default=None, metadata=default_json_config)
    subscriber_id: Optional[str] = field(default=None, metadata=default_json_config)
