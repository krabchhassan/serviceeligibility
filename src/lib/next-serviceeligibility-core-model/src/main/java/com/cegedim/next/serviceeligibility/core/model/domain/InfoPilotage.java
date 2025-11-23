package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
public class InfoPilotage implements GenericDomain<InfoPilotage> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private Integer numDebutFichier;
  private String numEmetteur;
  private String numClient;
  private String nomClient;
  private String versionNorme;
  private String typeFichier;
  private String codePerimetre;
  private String nomPerimetre;
  private String typeGestionnaireBO;
  private String libelleGestionnaireBO;
  private String codeClient;
  private Boolean numExterneContratIndividuel;
  private Boolean numExterneContratCollectif;
  private String codeComptable;
  private Date periodeReferenceDebut;
  private Date periodeReferenceFin;
  private Boolean filtreDomaine;
  private Boolean generateFichier;
  private Integer dureeValidite;
  private String periodeValidite;
  private List<ValiditeDomainesDroits> validitesDomainesDroits;

  public InfoPilotage() {
    /* empty constructor */ }

  public InfoPilotage(InfoPilotage source) {
    this.numDebutFichier = source.getNumDebutFichier();
    this.numEmetteur = source.getNumEmetteur();
    this.numClient = source.getNumClient();
    this.nomClient = source.getNomClient();
    this.versionNorme = source.getVersionNorme();
    this.typeFichier = source.getTypeFichier();
    this.codePerimetre = source.getCodePerimetre();
    this.nomPerimetre = source.getNomPerimetre();
    this.typeGestionnaireBO = source.getTypeGestionnaireBO();
    this.libelleGestionnaireBO = source.getLibelleGestionnaireBO();
    this.codeClient = source.getCodeClient();
    this.numExterneContratIndividuel = source.getNumExterneContratIndividuel();
    this.numExterneContratCollectif = source.getNumExterneContratCollectif();
    this.codeComptable = source.getCodeComptable();
    if (source.getPeriodeReferenceDebut() != null) {
      this.periodeReferenceDebut = new Date(source.getPeriodeReferenceDebut().getTime());
    }
    if (source.getPeriodeReferenceFin() != null) {
      this.periodeReferenceFin = new Date(source.getPeriodeReferenceFin().getTime());
    }
    this.filtreDomaine = source.getFiltreDomaine();
    this.generateFichier = source.getGenerateFichier();
    this.dureeValidite = source.getDureeValidite();
    this.periodeValidite = source.getPeriodeValidite();
    if (!CollectionUtils.isEmpty(source.getValiditesDomainesDroits())) {
      this.validitesDomainesDroits = new ArrayList<>();
      for (ValiditeDomainesDroits validite : source.getValiditesDomainesDroits()) {
        this.validitesDomainesDroits.add(new ValiditeDomainesDroits(validite));
      }
    }
  }

  @Override
  public int compareTo(final InfoPilotage info) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.numDebutFichier, info.numDebutFichier);
    compareToBuilder.append(this.numEmetteur, info.numEmetteur);
    compareToBuilder.append(this.numClient, info.numClient);
    compareToBuilder.append(this.nomClient, info.nomClient);
    compareToBuilder.append(this.versionNorme, info.versionNorme);
    compareToBuilder.append(this.typeFichier, info.typeFichier);
    compareToBuilder.append(this.codePerimetre, info.codePerimetre);
    compareToBuilder.append(this.nomPerimetre, info.nomPerimetre);
    compareToBuilder.append(this.typeGestionnaireBO, info.typeGestionnaireBO);
    compareToBuilder.append(this.libelleGestionnaireBO, info.libelleGestionnaireBO);
    compareToBuilder.append(this.codeClient, info.codeClient);
    compareToBuilder.append(this.numExterneContratIndividuel, info.numExterneContratIndividuel);
    compareToBuilder.append(this.numExterneContratCollectif, info.numExterneContratCollectif);
    compareToBuilder.append(this.codeComptable, info.codeComptable);
    compareToBuilder.append(this.periodeReferenceDebut, info.periodeReferenceDebut);
    compareToBuilder.append(this.periodeReferenceFin, info.periodeReferenceFin);
    compareToBuilder.append(this.dureeValidite, info.dureeValidite);
    compareToBuilder.append(this.periodeValidite, info.periodeValidite);
    return compareToBuilder.toComparison();
  }
}
