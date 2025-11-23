from dataclasses import dataclass, field
from typing import List
from datetime import date
from typing import Optional


from bobbimportjob.common.common_constants import CODE_INSURER, CODE_CONTRACT_ELEMENT, CODE_AMC, LABEL, IGNORED, ORIGINE, USER, \
    ALERT_ID, DEADLINE, PRODUCT_ELEMENTS

from bobbimportjob.exceptions.invalid_parameter_exception import InvalidParameterException


@dataclass
class ContractElement:
    _id: str
    code_insurer: str
    code_contract_element: str
    code_amc: str
    label: str
    ignored: bool
    origine: str
    user: str = ""
    alert_id: Optional[str] = None
    deadline: Optional[date] = None
    product_elements: List['ProductElement'] = field(default_factory=list)

    def __init__(
            self,
            _id: str,
            code_insurer: str,
            code_contract_element: str,
            code_amc: str,
            label: str,
            ignored: bool,
            origine: str = "",
            user: str = "",
            alert_id: Optional[str] = None,
            deadline: Optional[date] = None,
            product_elements: List['ProductElement'] = None
    ):
        parameters = {
            CODE_INSURER: code_insurer,
            CODE_CONTRACT_ELEMENT: code_contract_element,
            CODE_AMC: code_amc,
            IGNORED: ignored
        }
        if product_elements is None:
            product_elements = []
        if not code_insurer or not code_contract_element:
            missing_fields = []
            if not code_insurer:
                missing_fields.append(CODE_INSURER)
            if not code_contract_element:
                missing_fields.append(CODE_CONTRACT_ELEMENT)
            param_details = ', '.join(f"{k}: {v}" for k, v in parameters.items() if v is not None)
            error_message = (
                f"Erreur de validation : Champs obligatoires manquants - {', '.join(missing_fields)}."
                f" Paramètres fournis : {param_details}."
            )
            raise InvalidParameterException(error_message)
        if isinstance(ignored, str):
            ignored_lower = ignored.lower()
            if ignored_lower == "true":
                ignored = True
            elif ignored_lower == "false":
                ignored = False
            else:
                raise InvalidParameterException(f"Le champ 'ignored' doit être un booléen ou une chaîne de caractères "
                                                f"contenant 'true' ou 'false' pour contractElement : {parameters}")
        elif not isinstance(ignored, bool):
            raise InvalidParameterException(f"Le champ 'ignored' doit être un booléen pour contractElement : {parameters}.")
        self._id = _id
        self.code_insurer = code_insurer
        self.code_contract_element = code_contract_element
        self.code_amc = code_amc
        self.label = label
        self.ignored = ignored
        self.origine = origine
        self.user = user
        self.alert_id = alert_id
        self.deadline = deadline
        self.product_elements = product_elements

    def to_dict(self):
        data = {}
        def clean(value):
            return value.strip() if isinstance(value, str) else value
        if self._id is not None:
            data["_id"] = self._id
        if self.code_insurer is not None:
            data[CODE_INSURER] = clean(self.code_insurer)
        if self.code_contract_element is not None:
            data[CODE_CONTRACT_ELEMENT] = clean(self.code_contract_element)
        if self.code_amc is not None:
            data[CODE_AMC] = self.code_amc
        if self.label is not None:
            data[LABEL] = self.label
        if self.ignored is not None:
            data[IGNORED] = self.ignored
        if self.origine is not None:
            data[ORIGINE] = self.origine
        if self.user is not None:
            data[USER] = self.user
        if self.alert_id is not None:
            data[ALERT_ID] = self.alert_id
        if self.deadline is not None:
            data[DEADLINE] = self.deadline
        if self.product_elements is not None:
            data[PRODUCT_ELEMENTS] = self.product_elements
        return data
