package com.cegedim.next.serviceeligibility.core.model.domain.trigger;

import java.util.List;
import lombok.Data;

@Data
public class TriggerRequest {
  private List<String> amcs;
  private List<TriggerStatus> status;
  private List<TriggerEmitter> emitters;
  private String dateDebut;
  private String dateFin;
  private String owner;
  private Boolean isContratIndividuel;
  private String numeroContrat;
  private String nir;
  private int perPage;
  private int page;
  private String sortBy;
  private String direction;
}
