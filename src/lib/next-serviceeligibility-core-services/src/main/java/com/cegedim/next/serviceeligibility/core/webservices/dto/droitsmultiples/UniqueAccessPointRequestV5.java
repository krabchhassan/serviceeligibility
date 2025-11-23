package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

public class UniqueAccessPointRequestV5 extends UniqueAccessPointRequest {

  @Getter private final String beneficiaryId;
  @Getter private final String issuingCompanyCode;
  @Getter private final Boolean isForced;
  @Getter private final Boolean requirePW;

  @Getter private final String clientType;

  // Pas de constructeur par défaut, c'est voulu

  public UniqueAccessPointRequestV5(
      String nirCode,
      String birthDate,
      String birthRank,
      String startDate,
      String endDate,
      String insurerId,
      String subscriberId,
      String contractNumber,
      String context,
      String listDomain,
      String beneficiaryId,
      String issuingCompanyCode,
      String clientType,
      Boolean isForced) {
    super(
        nirCode,
        birthDate,
        birthRank,
        startDate,
        endDate,
        insurerId,
        subscriberId,
        contractNumber,
        context,
        listDomain);
    this.beneficiaryId = beneficiaryId;
    this.issuingCompanyCode = issuingCompanyCode;
    this.clientType = clientType;
    this.isForced = isForced;
    this.requirePW = true;
  }

  public UniqueAccessPointRequestV5(
      String nirCode,
      String birthDate,
      String birthRank,
      String startDate,
      String endDate,
      String insurerId,
      String subscriberId,
      String contractNumber,
      String context,
      String listDomain,
      String beneficiaryId,
      String issuingCompanyCode,
      String clientType,
      Boolean isForced,
      Boolean requirePW) {
    super(
        nirCode,
        birthDate,
        birthRank,
        startDate,
        endDate,
        insurerId,
        subscriberId,
        contractNumber,
        context,
        listDomain);
    this.beneficiaryId = beneficiaryId;
    this.issuingCompanyCode = issuingCompanyCode;
    this.clientType = clientType;
    this.isForced = isForced;
    this.requirePW = requirePW;
  }

  public String toString() {
    String item = commonToString();

    if (StringUtils.isNotBlank(this.beneficiaryId)) {
      item += String.format("beneficiaryId %s%n", this.beneficiaryId);
    }

    if (StringUtils.isNotBlank(this.issuingCompanyCode)) {
      item += String.format("issuingCompanyCode %s%n", this.issuingCompanyCode);
    }

    if (this.isForced != null) {
      item += String.format("isForced %b%n", this.isForced);
    }

    return item;
  }
}
