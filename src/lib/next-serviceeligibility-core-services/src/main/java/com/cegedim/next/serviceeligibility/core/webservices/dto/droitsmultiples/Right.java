package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.List;
import lombok.Data;

@Data
public class Right {
  String code;
  String insurerCode;
  String originCode;
  String originInsurerCode;
  String waitingCode;
  String type;
  String prioritizationOrder;
  String guaranteeAgeDate;
  List<Product> products;
}
