from bobbtransfojob.common.common_constants import CODE_CONTRACT_ELEMENT, CODE_INSURER, CODE_AMC, PRODUCT_ELEMENTS, \
    IGNORED


class ContractElement:
    def __init__(self, code_contract_element, code_insurer, code_amc, product_elements, ignored):
        self.code_contract_element = code_contract_element
        self.code_insurer = code_insurer
        self.code_amc = code_amc
        self.product_elements = product_elements
        self.ignored = ignored

    def to_dict(self):
        return {
            CODE_CONTRACT_ELEMENT: self.code_contract_element,
            CODE_INSURER: self.code_insurer,
            CODE_AMC: self.code_amc,
            PRODUCT_ELEMENTS: [product_element.to_dict() for product_element in self.product_elements if product_element is not None],
            IGNORED: self.ignored
        }
