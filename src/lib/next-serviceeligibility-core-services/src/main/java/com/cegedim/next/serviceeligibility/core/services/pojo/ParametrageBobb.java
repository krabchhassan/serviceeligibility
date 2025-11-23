package com.cegedim.next.serviceeligibility.core.services.pojo;

import java.util.List;
import lombok.Data;

@Data
public class ParametrageBobb {
  String codeAssureur;
  String codeGarantie;
  List<ParametrageBobbProductElement> parametrageBobbProductElements;
}
