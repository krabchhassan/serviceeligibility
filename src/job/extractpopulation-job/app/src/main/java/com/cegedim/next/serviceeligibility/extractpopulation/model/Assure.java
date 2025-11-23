package com.cegedim.next.serviceeligibility.extractpopulation.model;

import java.util.List;

public record Assure(
    String personNumber,
    boolean isSubscriber,
    String nir,
    List<AffiliationRO> affiliationsRO,
    String birthDate,
    String birthRank,
    String quality,
    String administrativeRank,
    String lastName,
    String commonName,
    String firstName,
    String individualSubscriptionStartDate,
    String cancellationDate,
    List<Garantie> guarantees) {}
