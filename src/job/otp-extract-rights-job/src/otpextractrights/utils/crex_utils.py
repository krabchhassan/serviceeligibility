from typing import List

from commonomuhelper.omuhelper import produce_output_parameters

from otpextractrights.model import InputBeneficiary, OutputBeneficiary

NB_INPUT_LINES = "nbInputLines"
NB_LINES_OK = "nbLineOK"
NB_LINES_KO = "nbLineKO"
GENERAL_ERROR = "generalError"


class CrexUtils:
    DATA = {
        NB_INPUT_LINES: 0,
        NB_LINES_OK: 0,
        NB_LINES_KO: 0,
        GENERAL_ERROR: "Aucune",
    }

    @staticmethod
    def set_nb_input_lines(value: int):
        CrexUtils.DATA[NB_INPUT_LINES] = value

    @staticmethod
    def increment_lines_ok():
        CrexUtils.DATA[NB_LINES_OK] += 1

    @staticmethod
    def increment_lines_ko():
        CrexUtils.DATA[NB_LINES_KO] += 1

    @staticmethod
    def set_general_error(value: str):
        CrexUtils.DATA[GENERAL_ERROR] = value

    @staticmethod
    def produce_crex():
        produce_output_parameters(CrexUtils.DATA)

    @staticmethod
    def count_success_fails(inputs: List[InputBeneficiary], outputs: List[OutputBeneficiary]):
        for input_benef in inputs:
            # All outputs from this input
            matching_outputs = list(filter(lambda x: (
                    (x.nir_demande == input_benef.nir) and
                    (x.date_naissance_demande == input_benef.date_naissance) and
                    (x.date_soins_demande == input_benef.date_soins) and
                    (x.amc_demande == input_benef.amc)
            ), outputs))
            # if there is at least one right found
            if len(list(filter(lambda x: x.code_erreur is None, matching_outputs))) > 0:
                CrexUtils.increment_lines_ok()
            else:
                CrexUtils.increment_lines_ko()
