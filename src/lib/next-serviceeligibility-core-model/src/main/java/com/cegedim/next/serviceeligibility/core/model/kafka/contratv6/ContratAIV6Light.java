package com.cegedim.next.serviceeligibility.core.model.kafka.contratv6;

import java.util.List;
import lombok.Data;

@Data
public class ContratAIV6Light {
  String id;
  String traceId;
  List<String> listNumeroPersonne;
  String idDeclarant;
  String numero;
}
