package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
public class DomaineDroit implements GenericDomain<DomaineDroit> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String codeExterne;
  private String libelle;
  private String codeExterneProduit;
  private String libelleExterne;
  private String codeOptionMutualiste;
  private String libelleOptionMutualiste;
  private String codeProduit;
  ModeAssemblage modeAssemblage;
  private String libelleProduit;
  private String codeGarantie;
  private String libelleGarantie;
  private String codeProfil;
  private String codeRenvoi;
  private String libelleCodeRenvoi;
  private String tauxRemboursement;
  private Boolean isSuspension = false;
  private String dateAdhesionCouverture;
  private String uniteTauxRemboursement;
  private String referenceCouverture;
  private String origineDroits;
  private Integer noOrdreDroit;
  private String categorie;

  private String formulaMask;
  private String naturePrestation;

  private String naturePrestationOnline;

  private String codeAssureurGarantie;
  private String codeOffre;

  private String versionOffre;

  private String codeOc;

  private Boolean isEditable = true;

  /* DOCUMENTS EMBEDDED */
  private PrioriteDroit prioriteDroit;
  private List<Prestation> prestations;
  private PeriodeDroit periodeDroit;
  private PeriodeDroit periodeOnline;
  private List<Conventionnement> conventionnements;

  private String codeCarence;
  private String codeOrigine;
  private String codeAssureurOrigine;

  private Periode periodeProductElement;
  private Periode periodePW;

  private PeriodeCarence periodeCarence;

  private String codeRenvoiAdditionnel;
  private String libelleCodeRenvoiAdditionnel;

  public DomaineDroit() {
    /* empty constructor */ }

  public DomaineDroit(DomaineDroit source) {
    this.code = source.getCode();
    this.codeExterne = source.getCodeExterne();
    this.libelle = source.getLibelle();
    this.codeExterneProduit = source.getCodeExterneProduit();
    this.libelleExterne = source.getLibelleExterne();
    this.codeOptionMutualiste = source.getCodeOptionMutualiste();
    this.libelleOptionMutualiste = source.getLibelleOptionMutualiste();
    this.codeProduit = source.getCodeProduit();
    this.modeAssemblage = source.getModeAssemblage();
    this.libelleProduit = source.getLibelleProduit();
    this.codeGarantie = source.getCodeGarantie();
    this.libelleGarantie = source.getLibelleGarantie();
    this.codeProfil = source.getCodeProfil();
    this.codeRenvoi = source.getCodeRenvoi();
    this.libelleCodeRenvoi = source.getLibelleCodeRenvoi();
    this.tauxRemboursement = source.getTauxRemboursement();
    this.isSuspension = source.getIsSuspension();
    this.dateAdhesionCouverture = source.getDateAdhesionCouverture();
    this.uniteTauxRemboursement = source.getUniteTauxRemboursement();
    this.referenceCouverture = source.getReferenceCouverture();
    this.origineDroits = source.getOrigineDroits();
    this.noOrdreDroit = source.getNoOrdreDroit();
    this.categorie = source.getCategorie();
    this.formulaMask = source.getFormulaMask();
    this.naturePrestation = source.getNaturePrestation();
    this.naturePrestationOnline = source.getNaturePrestationOnline();
    this.codeAssureurGarantie = source.getCodeAssureurGarantie();
    this.codeOffre = source.getCodeOffre();
    this.versionOffre = source.getVersionOffre();
    this.codeOc = source.getCodeOc();
    this.isEditable = source.getIsEditable();

    /* DOCUMENTS EMBEDDED */
    if (source.getPrioriteDroit() != null) {
      this.prioriteDroit = new PrioriteDroit(source.getPrioriteDroit());
    }
    if (!CollectionUtils.isEmpty(source.getPrestations())) {
      this.prestations = new ArrayList<>();
      for (Prestation prest : source.getPrestations()) {
        this.prestations.add(new Prestation(prest));
      }
    }
    if (source.getPeriodeDroit() != null) {
      this.periodeDroit = new PeriodeDroit(source.getPeriodeDroit());
    }
    if (source.getPeriodeOnline() != null) {
      this.periodeOnline = new PeriodeDroit(source.getPeriodeOnline());
    }
    if (!CollectionUtils.isEmpty(source.getConventionnements())) {
      this.conventionnements = new ArrayList<>();
      for (Conventionnement conv : source.getConventionnements()) {
        this.conventionnements.add(new Conventionnement(conv));
      }
    }

    this.codeCarence = source.getCodeCarence();
    this.codeOrigine = source.getCodeOrigine();
    this.codeAssureurOrigine = source.getCodeAssureurOrigine();
    if (source.getPeriodeProductElement() != null) {
      this.periodeProductElement = new Periode(source.getPeriodeProductElement());
    }
    if (source.getPeriodePW() != null) {
      this.periodePW = new Periode(source.getPeriodePW());
    }
    if (source.getPeriodeCarence() != null) {
      this.periodeCarence = new PeriodeCarence(source.getPeriodeCarence());
    }

    this.codeRenvoiAdditionnel = source.getCodeRenvoiAdditionnel();
    this.libelleCodeRenvoiAdditionnel = source.getLibelleCodeRenvoiAdditionnel();
  }

  /**
   * Affecte une liste de conventionnements. Si la liste en parametre est nulle, la propriete est
   * initialisee avec une liste vide.
   *
   * @param conventionnements La liste des conventionnements.
   */
  public void setConventionnements(final List<Conventionnement> conventionnements) {
    this.conventionnements = Objects.requireNonNullElseGet(conventionnements, ArrayList::new);
  }

  @Override
  public int compareTo(final DomaineDroit domaineDroit) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, domaineDroit.code);
    compareToBuilder.append(this.codeExterne, domaineDroit.codeExterne);
    compareToBuilder.append(this.codeExterneProduit, domaineDroit.codeExterneProduit);
    compareToBuilder.append(this.codeGarantie, domaineDroit.codeGarantie);
    compareToBuilder.append(this.codeOptionMutualiste, domaineDroit.codeOptionMutualiste);
    compareToBuilder.append(this.codeProduit, domaineDroit.codeProduit);
    compareToBuilder.append(this.modeAssemblage, domaineDroit.modeAssemblage);
    compareToBuilder.append(this.conventionnements, domaineDroit.conventionnements);
    compareToBuilder.append(this.periodeDroit, domaineDroit.periodeDroit);
    compareToBuilder.append(this.periodeOnline, domaineDroit.periodeOnline);
    compareToBuilder.append(this.libelle, domaineDroit.libelle);
    compareToBuilder.append(this.libelleExterne, domaineDroit.libelleExterne);
    compareToBuilder.append(this.libelleGarantie, domaineDroit.libelleGarantie);
    compareToBuilder.append(this.libelleOptionMutualiste, domaineDroit.libelleOptionMutualiste);
    compareToBuilder.append(this.libelleProduit, domaineDroit.libelleProduit);
    compareToBuilder.append(this.prestations, domaineDroit.prestations);
    compareToBuilder.append(this.prioriteDroit, domaineDroit.prioriteDroit);
    compareToBuilder.append(this.codeProfil, domaineDroit.codeProfil);
    compareToBuilder.append(this.codeRenvoi, domaineDroit.codeRenvoi);
    compareToBuilder.append(this.libelleCodeRenvoi, domaineDroit.libelleCodeRenvoi);
    compareToBuilder.append(this.tauxRemboursement, domaineDroit.tauxRemboursement);
    compareToBuilder.append(this.isSuspension, domaineDroit.isSuspension);
    compareToBuilder.append(this.isEditable, domaineDroit.isEditable);
    compareToBuilder.append(this.dateAdhesionCouverture, domaineDroit.dateAdhesionCouverture);
    compareToBuilder.append(this.uniteTauxRemboursement, domaineDroit.uniteTauxRemboursement);
    compareToBuilder.append(this.referenceCouverture, domaineDroit.referenceCouverture);
    compareToBuilder.append(this.noOrdreDroit, domaineDroit.noOrdreDroit);
    compareToBuilder.append(this.categorie, domaineDroit.categorie);
    compareToBuilder.append(this.formulaMask, domaineDroit.formulaMask);
    compareToBuilder.append(this.naturePrestation, domaineDroit.naturePrestation);
    compareToBuilder.append(this.naturePrestationOnline, domaineDroit.naturePrestationOnline);
    compareToBuilder.append(this.codeAssureurGarantie, domaineDroit.codeAssureurGarantie);
    compareToBuilder.append(this.codeOffre, domaineDroit.codeOffre);
    compareToBuilder.append(this.codeOc, domaineDroit.codeOc);
    compareToBuilder.append(this.codeRenvoiAdditionnel, domaineDroit.codeRenvoiAdditionnel);
    compareToBuilder.append(
        this.libelleCodeRenvoiAdditionnel, domaineDroit.libelleCodeRenvoiAdditionnel);
    return compareToBuilder.toComparison();
  }
}
