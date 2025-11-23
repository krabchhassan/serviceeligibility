package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.model.domain.TraceExtraction;
import com.cegedim.next.serviceeligibility.core.model.domain.TracePriorisation;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection profils dans la base de donnees. */
@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = Constants.TRACES)
public class Traces extends DocumentEntity implements GenericDomain<Traces> {

  private static final long serialVersionUID = -4312050886449724031L;

  private String idDeclaration;
  private String idDeclarant;
  private Date effetDebut;
  private String codeEtat;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private String nom;
  private String prenom;
  private String nirOd1;
  private String cleNirOd1;
  private String dateNaissance;
  private String rangNaissance;
  private String qualite;
  private String numeroContrat;
  private String numeroAdherent;
  private String numeroPersonne;
  private String nomFichierOrigine;
  private List<TraceConsolidation> listeConsolidations;
  private List<TraceExtraction> listeExtractions;
  private List<TracePriorisation> listePriorisation;

  @Override
  public int compareTo(Traces profil) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.get_id(), profil.get_id());
    return compareToBuilder.toComparison();
  }
}
