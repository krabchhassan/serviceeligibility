from dataclasses import dataclass
from datetime import date, datetime
from typing import Optional

from bobbimportjob.common.common_constants import FROM, CODE_OFFER, CODE_PRODUCT, CODE_BENEFITE_NATURE, TO, \
    EFFECTIVE_DATE, INPUT_DATETIME_FORMAT, CODE_AMC_PRODUCT, CODE_OC
from bobbimportjob.exceptions.invalid_parameter_exception import InvalidParameterException


@dataclass
class ProductElement:
    code_offer: str
    code_product: str
    code_benefit_nature: str
    code_oc: str
    from_date: Optional[date] = None
    to: Optional[date] = None
    effective_date: Optional[date] = None

    def __eq__(self, other):
        if isinstance(other, ProductElement):
            return (self.code_offer == other.code_offer and self.code_product == other.code_product
                    and self.code_benefit_nature == other.code_benefit_nature and self.code_oc == other.code_oc
                    and self.from_date == other.from_date
                    and self.to == other.to and self.effective_date == other.effective_date)
        return False

    def __init__(
            self,
            code_offer: str,
            code_product: str,
            code_benefit_nature: str,
            code_oc: str,
            from_date: str,
            to: str,
            effective_date: str
    ):
        parameters = {
            CODE_OFFER: code_offer,
            CODE_PRODUCT: code_product,
            CODE_BENEFITE_NATURE: code_benefit_nature,
            CODE_OC: code_oc,
            FROM: from_date,
            TO: to,
            EFFECTIVE_DATE: effective_date
        }
        if not code_offer or not code_product or not from_date or code_benefit_nature is None or code_oc is None:
            missing_fields = []
            if not code_offer:
                missing_fields.append(CODE_OFFER)
            if not code_product:
                missing_fields.append(CODE_PRODUCT)
            if not from_date:
                missing_fields.append(FROM)
            if code_benefit_nature is None:
                missing_fields.append(CODE_BENEFITE_NATURE)
            if code_oc is None:
                missing_fields.append(CODE_OC)

            param_details = ', '.join(f"{k}: {v}" for k, v in parameters.items() if v is not None)
            error_message = (
                f"Erreur de validation : Champs obligatoires manquants - {', '.join(missing_fields)}."
                f" Paramètres fournis : {param_details}."
            )
            raise InvalidParameterException(error_message)

        self.code_offer = code_offer
        self.code_product = code_product
        self.code_benefit_nature = code_benefit_nature
        self.code_oc = code_oc
        try:
            self.from_date = datetime.strptime(from_date, INPUT_DATETIME_FORMAT)
            self.to = datetime.strptime(to, INPUT_DATETIME_FORMAT) if to else None
            self.effective_date = datetime.strptime(effective_date, INPUT_DATETIME_FORMAT) if effective_date else None
        except ValueError as ve:
            raise InvalidParameterException(f"Format invalide pour les champs de date de productElement : {parameters}")

    def to_dict(self):
        data = {}
        def clean(value):
            return value.strip() if isinstance(value, str) else value
        if self.code_offer is not None:
            data[CODE_OFFER] = clean(self.code_offer)
        if self.code_product is not None:
            data[CODE_PRODUCT] = clean(self.code_product)
        if self.code_benefit_nature is not None:
            data[CODE_BENEFITE_NATURE] = clean(self.code_benefit_nature)
        if self.code_oc is not None:
            data[CODE_AMC_PRODUCT] = clean(self.code_oc)
        if self.from_date is not None:
            data[FROM] = self.from_date
        if self.to is not None:
            data[TO] = self.to
        if self.effective_date is not None:
            data[EFFECTIVE_DATE] = self.effective_date
        return data

