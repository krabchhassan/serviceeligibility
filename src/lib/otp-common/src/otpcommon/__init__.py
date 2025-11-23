from dataclasses_json import LetterCase, config, Undefined


# --------------------
# JSON CONFIG
# --------------------
def exclude_if_none(value):
    """Do not include field for None values"""
    return value is None


default_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none)
excluce_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=lambda x: True)

contratCMUC2S_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="contratCMUC2S")
numAMCEchange_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="numAMCEchange")
regimeOD1_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="regimeOD1")
caisseOD1_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="caisseOD1")
centreOD1_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="centreOD1")
regimeOD2_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="regimeOD2")
caisseOD2_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="caisseOD2")
centreOD2_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="centreOD2")
isContratCMU_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="isContratCMU")
periodesContratCMU_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="periodesContratCMU")
rattachementRO_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="rattachementRO")
prioriteBO_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="prioriteBO")
affiliationsRO_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="affiliationsRO")
isBeneficiaireACS_json_config = config(letter_case=LetterCase.CAMEL, undefined=Undefined.EXCLUDE, exclude=exclude_if_none, field_name="isBeneficiaireACS")
