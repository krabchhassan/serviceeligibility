package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import java.util.List;
import lombok.Data;

@Data
public class ContratAIV5Light {
  String id;
  String traceId;
  List<String> listNumeroPersonne;
  String idDeclarant;
  String numero;
}
