from dataclasses import dataclass, field
from typing import Optional

from dataclasses_json import LetterCase, dataclass_json

from otpcommon import default_json_config


@dataclass_json(letter_case=LetterCase.CAMEL)
@dataclass
class ServerInfo:
    base_url: Optional[str] = field(default=None, metadata=default_json_config)
    client_id: Optional[str] = field(default=None, metadata=default_json_config)
    client_secret: Optional[str] = field(default=None, metadata=default_json_config)
    issuer_uri: Optional[str] = field(default=None, metadata=default_json_config)
    token_uri: Optional[str] = field(default=None, metadata=default_json_config)
    auth_header: Optional[str] = field(default=None, metadata=default_json_config)
    is_local: Optional[bool] = field(default=False, metadata=default_json_config)
    amc: Optional[str] = field(default=False, metadata=default_json_config)

    # methods (for comparator)
    def __eq__(self, other):
        if isinstance(other, ServerInfo):
            return (self.base_url == other.base_url) \
                and (self.token_uri == other.token_uri) \
                and (self.client_id == other.client_id)
        else:
            return False

    def __ne__(self, other):
        return not self.__eq__(other)

    def __hash__(self):
        return hash(self.base_url)

    # override methods
    def __repr__(self):
        return "ServerInfo('base_url': {}, 'client_id': {})".format(self.base_url, self.client_id)
