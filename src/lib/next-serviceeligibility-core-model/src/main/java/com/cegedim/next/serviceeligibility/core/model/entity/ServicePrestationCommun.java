package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicePrestation")
@Data
@EqualsAndHashCode(callSuper = true)
public class ServicePrestationCommun extends DocumentEntity {
  private static final long serialVersionUID = 1L;

  private String idDeclarant;
  private String societeEmettrice;
  private String numero;
  private String numeroExterne;
  private String numeroAdherent;
  private String numeroAdherentComplet;
  private String dateSouscription;
  private String dateResiliation;
  private String apporteurAffaire;
  private List<Periode> periodesContratResponsableOuvert; // NOSONAR
  private String critereSecondaireDetaille;
  private String critereSecondaire;
  private Boolean isContratIndividuel;
  private String gestionnaire;
  private String qualification;
  private String ordrePriorisation;

  @Override
  @JsonIgnore
  public String get_id() {
    return null;
  }
}
