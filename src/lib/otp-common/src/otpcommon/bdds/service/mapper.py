from otpcommon.bdds.model.bdds_search_dto import BddsSearchRequest
from otpcommon.aiguillage.model.aiguillage_dto import AiguillageRequest


class Mapper:

    @staticmethod
    def bdds_to_aiguillage(bdds: BddsSearchRequest) -> AiguillageRequest:
        return AiguillageRequest(
            firstname=bdds.first_name,
            lastname=bdds.name,
            insee_code=bdds.nir,
            birth_date=bdds.birth_date,
            birth_rank=bdds.birth_rank,
            insurer_id=bdds.declarant_id,
            subscriber_id=bdds.subscriber_id_or_contract_number
        )
