from bobbtransfojob.common.common_constants import CODE_OC, CODE_OFFER, CODE_PRODUCT, CODE_BENEFIT_NATURE, FROM_DATE, \
    TO_DATE, EFFECTIVE_DATE


class ProductElement:
    def __init__(self, code_oc, code_offer, code_product, code_benefit_nature, from_date, to_date, effective_date):
        self.code_oc = code_oc
        self.code_offer = code_offer
        self.code_product = code_product
        self.code_benefit_nature = code_benefit_nature
        self.from_date = from_date
        self.to_date = to_date
        self.effective_date = effective_date

    def to_dict(self):
        return {
            CODE_OC: self.code_oc,
            CODE_OFFER: self.code_offer,
            CODE_PRODUCT: self.code_product,
            CODE_BENEFIT_NATURE: self.code_benefit_nature,
            FROM_DATE: self.from_date,
            TO_DATE: self.to_date,
            EFFECTIVE_DATE: self.effective_date
        }
