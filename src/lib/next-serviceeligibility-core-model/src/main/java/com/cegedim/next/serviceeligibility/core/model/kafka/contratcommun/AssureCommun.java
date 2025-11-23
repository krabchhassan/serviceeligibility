package com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.QualiteAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * Classe contenant les champs communs à toutes les versions de l'assure d'interface pour l'event
 * Contrat
 *
 * @author RHERMEZ
 */
@Data
public abstract class AssureCommun {
  private Boolean isSouscripteur;
  private String rangAdministratif;
  private IdentiteContrat identite;
  private String dateAdhesionMutuelle;
  private String dateDebutAdhesionIndividuelle;
  private String numeroAdhesionIndividuelle;
  private String dateRadiation;
  private LocalDateTime dateCreation;
  private LocalDateTime dateModification;
  private List<Periode> periodes;
  private List<Periode> periodesMedecinTraitantOuvert;
  private List<CodePeriode> regimesParticuliers;
  private List<CodePeriode> situationsParticulieres;
  private QualiteAssure qualite;
}
