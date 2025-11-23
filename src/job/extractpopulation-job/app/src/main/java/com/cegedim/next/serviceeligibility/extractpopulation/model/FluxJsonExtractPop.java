package com.cegedim.next.serviceeligibility.extractpopulation.model;

import java.util.List;

public record FluxJsonExtractPop(
    String idDeclarant,
    String subscriberId,
    String contractNumber,
    String subscriptionDate,
    String terminationDate,
    String issuingCompanyCode,
    List<Assure> insureds) {}
