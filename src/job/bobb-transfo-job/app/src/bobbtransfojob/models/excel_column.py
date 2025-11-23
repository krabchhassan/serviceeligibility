from bobbtransfojob.common.common_constants import CODE_CONTRACT_ELEMENT, CODE_INSURER, CODE_AMC, CODE_OC, CODE_OFFER, \
    CODE_PRODUCT, CODE_BENEFIT_NATURE, FROM_DATE, TO_DATE, EFFECTIVE_DATE, IGNORED


class ExcelColumn:
    def __init__(self, name, index):
        self.name = name
        self.index = index

    def __str__(self):
        return f"Name: {self.name}, Index: {self.index}"

    def __repr__(self):
        return f"ExcelColumn('{self.name}', {self.index})"


# Define Excel columns
COLUMN_CODE_CONTRACT_ELEMENT = ExcelColumn(CODE_CONTRACT_ELEMENT, 0)
COLUMN_CODE_INSURER = ExcelColumn(CODE_INSURER, 1)
COLUMN_CODE_AMC = ExcelColumn(CODE_AMC, 2)
COLUMN_CODE_OC = ExcelColumn(CODE_OC, 3)
COLUMN_CODE_OFFER = ExcelColumn(CODE_OFFER, 4)
COLUMN_CODE_PRODUCT = ExcelColumn(CODE_PRODUCT, 5)
COLUMN_CODE_BENEFIT_NATURE = ExcelColumn(CODE_BENEFIT_NATURE, 6)
COLUMN_FROM_DATE = ExcelColumn(FROM_DATE, 7)
COLUMN_TO_DATE = ExcelColumn(TO_DATE, 8)
COLUMN_EFFECTIVE_DATE = ExcelColumn(EFFECTIVE_DATE, 9)
COLUMN_IGNORED = ExcelColumn(IGNORED, 10)
