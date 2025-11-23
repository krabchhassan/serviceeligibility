from datetime import datetime
from typing import List, Tuple, Dict

from otpextractrights.exceptions.validation_exception import ValidationException
from otpextractrights.model import InputBeneficiary, ValidationFail
from otpextractrights.service.mapper import ExtractContractMapper
from otpextractrights.utils.errors import EMPTY_NIR, NIR_NOT_13, EMPTY_SEARCH_DATE, BIRTH_DATE_NOT_6, VALIDATION_ERROR, \
    SEARCH_DATE_NOT_DATE


class ValidationService:

    @staticmethod
    def validate(input_beneficiaries: List[InputBeneficiary]) -> Tuple[
        List[InputBeneficiary], Dict[str, ValidationFail]]:
        successes: List[InputBeneficiary] = []
        fails: Dict[str, ValidationFail] = {}
        for benef in input_beneficiaries:
            try:
                ValidationService.check_nir_not_null(benef)
                ValidationService.check_nir_is_13_char(benef)
                ValidationService.check_search_date_not_null(benef)
                ValidationService.check_search_format(benef)
                ValidationService.check_birth_date(benef)
                successes.append(benef)
            except ValidationException as e:
                fails[ExtractContractMapper.get_input_hash(benef)] = ExtractContractMapper.to_validation_fail(
                    error_code=VALIDATION_ERROR,
                    label_error=e.message
                )
        return successes, fails

    @staticmethod
    def check_nir_not_null(benef: InputBeneficiary):
        if (benef.nir is None) or (benef.nir == ""):
            raise ValidationException(EMPTY_NIR)

    @staticmethod
    def check_nir_is_13_char(benef: InputBeneficiary):
        if len(benef.nir) != 13:
            raise ValidationException(NIR_NOT_13)

    @staticmethod
    def check_search_date_not_null(benef: InputBeneficiary):
        if (benef.date_soins is None) or (benef.date_soins == ""):
            raise ValidationException(EMPTY_SEARCH_DATE)

    @staticmethod
    def check_search_format(benef: InputBeneficiary):
        date_format = "%Y%m%d"
        try:
            datetime.strptime(benef.date_soins, date_format)
        except Exception:
            raise ValidationException(SEARCH_DATE_NOT_DATE)

    @staticmethod
    def check_birth_date(benef: InputBeneficiary):
        if benef.date_naissance is None:
            return
        if len(benef.date_naissance) != 6:
            raise ValidationException(BIRTH_DATE_NOT_6)
