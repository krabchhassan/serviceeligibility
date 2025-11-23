package com.cegedim.next.serviceeligibility.extractpopulation.model;

public record AffiliationRO(
    String nir,
    String regimeCode,
    String healthInsuranceCompanyCode,
    String centerCode,
    String startDate,
    String endDate) {}
