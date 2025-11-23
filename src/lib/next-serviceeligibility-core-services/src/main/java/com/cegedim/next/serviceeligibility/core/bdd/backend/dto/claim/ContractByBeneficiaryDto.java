package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.claim;

import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;

public record ContractByBeneficiaryDto(
    String insurerId,
    String contractNumber,
    String subscriberId,
    String issuingCompanyCode,
    Period period,
    NirDto nir,
    NomDto subscriber,
    boolean isIndividualContract) {}
