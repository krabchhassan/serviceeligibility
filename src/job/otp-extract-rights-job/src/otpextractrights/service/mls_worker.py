from typing import Set, Union, Dict

import commonutils
from otpcommon.mls.exception.mls_exception import OcNotFoundException, MlsNotFoundException
from otpcommon.mls.model.server_info import ServerInfo
from otpcommon.mls.service.mls_service import MlsService

from otpextractrights.model import MlsAmcFail
from otpextractrights.utils.api_call_wrapper import call_api_get
from otpextractrights.utils.errors import OC_NOT_FOUND, MLS_NOT_FOUND


class MlsWorker:

    @staticmethod
    def resolve_amc(amcs: Set[str]) -> Dict[str, Union[ServerInfo, MlsAmcFail]]:

        mls_service = MlsService()
        servers: Dict[str, Union[ServerInfo, MlsAmcFail]] = dict()
        for amc in amcs:
            try:
                commonutils.utils.call_api_get = call_api_get
                server = mls_service.get_server_for_extract(amc)
            except OcNotFoundException:
                server = MlsAmcFail(amc=amc, libelle_erreur=OC_NOT_FOUND.format(amc))
            except MlsNotFoundException:
                server = MlsAmcFail(amc=amc, libelle_erreur=MLS_NOT_FOUND.format(amc))
            servers[amc] = server
        return servers
