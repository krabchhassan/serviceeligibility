package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.io.Serializable;
import java.util.List;

@lombok.Data
public class Data implements Serializable {
  private List<DestinataireRelevePrestations> destinatairesRelevePrestations;
  private Adresse adresse;
  private Nom nom;
}
