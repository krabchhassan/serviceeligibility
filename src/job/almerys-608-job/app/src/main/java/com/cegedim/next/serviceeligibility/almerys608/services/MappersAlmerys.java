package com.cegedim.next.serviceeligibility.almerys608.services;

public record MappersAlmerys(
    MapperAlmerysContrat mapperAlmerysContrat,
    MapperAlmerysEntreprise mapperAlmerysEntreprise,
    MapperAlmerysServiceTP mapperAlmerysServiceTP,
    MapperAlmerysMembreContrat mapperAlmerysMembreContrat,
    MapperAlmerysProduit mapperAlmerysProduit,
    MapperAlmerysCarence mapperAlmerysCarence,
    MapperAlmerysAdresse mapperAlmerysAdresse,
    MapperAlmerysRattachement mapperAlmerysRattachement,
    MapperAlmerysBeneficiaire mapperAlmerysBeneficiaire) {}
