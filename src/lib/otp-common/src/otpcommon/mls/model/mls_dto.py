from dataclasses import dataclass, field
from typing import Optional, List

from dataclasses_json import dataclass_json

from otpcommon import default_json_config


@dataclass_json
@dataclass
class MlsCatalogConfig:
    url: Optional[str] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class MlsCatalog:
    resource_type: Optional[str] = field(default=None, metadata=default_json_config)
    configuration: Optional[str] = field(default=None, metadata=default_json_config)
    integration_point_name: Optional[str] = field(default=None, metadata=default_json_config)
    integration_point_code: Optional[str] = field(default=None, metadata=default_json_config)


@dataclass_json
@dataclass
class MlsResponse:
    code: Optional[str] = field(default=None, metadata=default_json_config)
    secret_id: Optional[str] = field(default=None, metadata=default_json_config)
    catalog_json_response: Optional[List[MlsCatalog]] = field(default=None, metadata=default_json_config)
