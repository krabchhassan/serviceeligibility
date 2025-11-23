import os
import sys
from unittest.mock import Mock
from unittest import TestCase

sys.modules['organisationsettings.services.get_organisation_by_amc'] = Mock()
from otpextractrights.exceptions.validation_exception import ValidationException
from otpextractrights.service import ValidationService

from otpextractrights.model import  InputBeneficiary

os.environ['BEYOND_INSTANCE_TYPE'] = 'DEDICATED'

class TestMain(TestCase):

    def test_check_nir_not_null(self):
        # RAISE VALIDATION ERROR
        benef = InputBeneficiary(
            nir=None
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_nir_not_null(benef)

        # VALID
        benef = InputBeneficiary(
            nir="notNone"
        )
        ValidationService.check_nir_not_null(benef)

    def test_check_nir_is_13_char(self):
        # RAISE VALIDATION ERROR
        benef = InputBeneficiary(
            nir="TooShort"
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_nir_is_13_char(benef)
        benef = InputBeneficiary(
            nir="ThisNirIsReallyTooLong"
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_nir_is_13_char(benef)

        # VALID
        benef = InputBeneficiary(
            nir="_"*13
        )
        ValidationService.check_nir_is_13_char(benef)

    def test_check_search_date_not_null(self):
        # RAISE VALIDATION ERROR
        benef = InputBeneficiary(
            date_soins=None
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_search_date_not_null(benef)

        # VALID
        benef = InputBeneficiary(
            date_soins="notNone"
        )
        ValidationService.check_search_date_not_null(benef)

    def test_check_search_date_is_date(self):
        # RAISE VALIDATION ERROR
        invalid_list = [
            "not_valid",
            "1"*9,
            "1234567A",
            "99999999",
            "20230144"
        ]
        for invalid in invalid_list:
            with self.assertRaises(ValidationException) as ctx:
                ValidationService.check_search_format(
                    benef=InputBeneficiary(date_soins=invalid)
                )

        # VALID
        valid_list = [
            "20230101",
            "19990101",
            "01231231"
        ]
        for valid in valid_list:
            ValidationService.check_search_format(benef=InputBeneficiary(date_soins=valid))

    def test_birth_date(self):
        # RAISE VALIDATION ERROR
        benef = InputBeneficiary(
            date_naissance="1"
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_birth_date(benef)
        benef = InputBeneficiary(
            date_naissance=""
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_birth_date(benef)
        benef = InputBeneficiary(
            date_naissance="1111111"
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_birth_date(benef)
        benef = InputBeneficiary(
            date_naissance="11111"
        )
        with self.assertRaises(ValidationException) as ctx:
            ValidationService.check_birth_date(benef)

        # VALID
        benef = InputBeneficiary(
            date_naissance="111111"
        )
        ValidationService.check_birth_date(benef)
        benef = InputBeneficiary(
            date_naissance="000000"
        )
        ValidationService.check_birth_date(benef)
        benef = InputBeneficiary(
            date_naissance=None
        )
        ValidationService.check_birth_date(benef)
