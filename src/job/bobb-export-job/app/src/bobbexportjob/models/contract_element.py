

class ContractElement:
    def __init__(self, code_contract_element="", code_insurer="", code_amc="", product_elements=None, ignored=""):
        self.code_contract_element = code_contract_element
        self.code_insurer = code_insurer
        self.code_amc = code_amc
        self.product_elements = product_elements if product_elements else []
        self.ignored = ignored
