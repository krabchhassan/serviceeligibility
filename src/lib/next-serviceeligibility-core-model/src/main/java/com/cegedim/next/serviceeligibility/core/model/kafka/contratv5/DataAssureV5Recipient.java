package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinataireRelevePrestations;
import java.util.List;
import lombok.Data;

@Data
public class DataAssureV5Recipient {
  private List<DestinataireRelevePrestations> destinatairesRelevePrestations;
  private List<DestinatairePaiementsV5> destinatairesPaiements;
}
