package com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples;

import java.util.List;
import lombok.Data;

@Data
public class ReplacementRight {
  String code;
  String insurerCode;
  List<Product> products;
}
