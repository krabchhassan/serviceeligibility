package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.util.List;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Data
public abstract class UniqueAccessPointRequest {
  String nirCode;
  String birthDate;
  String birthRank;
  String startDate;
  String endDate;

  String startDateForMongoContract;
  String endDateForMongoContract;

  String insurerId;
  String subscriberId;
  String contractNumber;
  String context;
  List<String> domains;

  // Pas de constructeur par défaut, c'est voulu

  protected UniqueAccessPointRequest(
      String nirCode,
      String birthDate,
      String birthRank,
      String startDate,
      String endDate,
      String insurerId,
      String subscriberId,
      String contractNumber,
      String context,
      String listDomain) {
    this.nirCode = nirCode;
    this.birthDate = birthDate;
    this.birthRank = birthRank;
    this.contractNumber = contractNumber;

    this.startDate = startDate;

    if (StringUtils.isBlank(endDate)) {
      // LOT-1054
      this.endDate = null;
    } else {
      this.endDate = endDate;
    }

    this.insurerId = insurerId;
    this.subscriberId = subscriberId;
    this.context = context;
    this.domains = Util.stringToList(listDomain);
  }

  protected String commonToString() {
    String item = "";

    item += String.format("nircode %s%n", this.nirCode);
    item += String.format("birthDate %s%n", this.birthDate);
    item += String.format("birthRank %s%n", this.birthRank);
    item += String.format("startDate %s%n", this.startDate);
    item += String.format("endDate %s%n", this.endDate);
    item += String.format("insurerId %s%n", this.insurerId);

    if (StringUtils.isNotBlank(this.subscriberId)) {
      item += String.format("subscriberId %s%n", this.subscriberId);
    }

    if (StringUtils.isNotBlank(this.contractNumber)) {
      item += String.format("contractNumber %s%n", this.contractNumber);
    }

    item += String.format("context %s%n", this.context);

    if (CollectionUtils.isNotEmpty(this.domains)) {
      item += String.format("domains %s%n", this.domains);
    }

    return item;
  }

  public abstract String toString();
}
