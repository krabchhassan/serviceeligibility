package com.cegedim.next.serviceeligibility.core.services.pojo;

import lombok.Data;

@Data
public class ParametrageCarence {
  String codeOffre;
  String codeProduit;
  String naturePrestation;
  String codeCarence;
  String debutParam;
  String finParam;
}
