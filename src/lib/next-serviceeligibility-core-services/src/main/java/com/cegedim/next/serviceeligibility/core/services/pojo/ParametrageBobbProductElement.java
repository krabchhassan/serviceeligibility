package com.cegedim.next.serviceeligibility.core.services.pojo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ParametrageBobbProductElement {

  String codeOc;
  String codeOffre;
  String codeProduit;
  List<ParametrageBobbNaturePrestation> naturePrestation = new ArrayList<>();
}
