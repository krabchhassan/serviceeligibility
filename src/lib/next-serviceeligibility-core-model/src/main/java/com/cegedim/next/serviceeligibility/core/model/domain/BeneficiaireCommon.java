package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirRattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.TeletransmissionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe le document Beneficiaire */
@Data
public class BeneficiaireCommon implements GenericDomain<BeneficiaireCommon> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String idClientBO;
  private String dateNaissance;
  private String rangNaissance;
  private String nirBeneficiaire;
  private String cleNirBeneficiaire;
  private String nirOd1;
  private String cleNirOd1;
  private String nirOd2;
  private String cleNirOd2;
  private String insc;
  private String numeroPersonne;
  private String refExternePersonne;
  private String dateAdhesionMutuelle;
  private String dateDebutAdhesionIndividuelle;
  private String numeroAdhesionIndividuelle;
  private String dateRadiation;
  private boolean isSouscripteurAlmv3;

  /* DOCUMENTS EMBEDDED */

  private Affiliation affiliation;

  private List<NirRattachementRODeclaration> affiliationsRO;
  private List<PeriodeComparable> periodesMedecinTraitant;
  private List<TeletransmissionDeclaration> teletransmissions;
  private List<CodePeriodeDeclaration> regimesParticuliers;
  private List<CodePeriodeDeclaration> situationsParticulieres;

  public BeneficiaireCommon() {
    /* empty constructor */ }

  public BeneficiaireCommon(BeneficiaireCommon source) {
    this.idClientBO = source.getIdClientBO();
    this.dateNaissance = source.getDateNaissance();
    this.rangNaissance = source.getRangNaissance();
    this.nirBeneficiaire = source.getNirBeneficiaire();
    this.cleNirBeneficiaire = source.getCleNirBeneficiaire();
    this.nirOd1 = source.getNirOd1();
    this.cleNirOd1 = source.getCleNirOd1();
    this.nirOd2 = source.getNirOd2();
    this.cleNirOd2 = source.getCleNirOd2();
    this.insc = source.getInsc();
    this.numeroPersonne = source.getNumeroPersonne();
    this.refExternePersonne = source.getRefExternePersonne();
    this.dateAdhesionMutuelle = source.getDateAdhesionMutuelle();
    this.dateDebutAdhesionIndividuelle = source.getDateDebutAdhesionIndividuelle();
    this.numeroAdhesionIndividuelle = source.getNumeroAdhesionIndividuelle();
    this.dateRadiation = source.getDateRadiation();
    this.isSouscripteurAlmv3 = source.isSouscripteurAlmv3;

    if (source.getAffiliation() != null) {
      this.affiliation = new Affiliation(source.getAffiliation());
    }
    if (!CollectionUtils.isEmpty(source.getAffiliationsRO())) {
      this.affiliationsRO = new ArrayList<>();
      for (NirRattachementRODeclaration nirRattachementRO : source.getAffiliationsRO()) {
        this.affiliationsRO.add(new NirRattachementRODeclaration(nirRattachementRO));
      }
    }

    if (!CollectionUtils.isEmpty(source.getPeriodesMedecinTraitant())) {
      this.periodesMedecinTraitant = new ArrayList<>();
      for (PeriodeComparable periodeComparable : source.getPeriodesMedecinTraitant()) {
        this.periodesMedecinTraitant.add(new PeriodeComparable(periodeComparable));
      }
    }

    if (!CollectionUtils.isEmpty(source.getTeletransmissions())) {
      this.teletransmissions = new ArrayList<>();
      for (TeletransmissionDeclaration teletransmission : source.getTeletransmissions()) {
        this.teletransmissions.add(new TeletransmissionDeclaration(teletransmission));
      }
    }

    if (!CollectionUtils.isEmpty(source.getRegimesParticuliers())) {
      this.regimesParticuliers = new ArrayList<>();
      for (CodePeriodeDeclaration regimesParticulier : source.getRegimesParticuliers()) {
        this.regimesParticuliers.add(new CodePeriodeDeclaration(regimesParticulier));
      }
    }

    if (!CollectionUtils.isEmpty(source.getSituationsParticulieres())) {
      this.situationsParticulieres = new ArrayList<>();
      for (CodePeriodeDeclaration situationsParticuliere : source.getSituationsParticulieres()) {
        this.situationsParticulieres.add(new CodePeriodeDeclaration(situationsParticuliere));
      }
    }
  }

  @Override
  public int compareTo(BeneficiaireCommon beneficiaire) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.cleNirOd1, beneficiaire.cleNirOd1);
    compareToBuilder.append(this.dateNaissance, beneficiaire.dateNaissance);
    compareToBuilder.append(this.nirOd1, beneficiaire.nirOd1);
    compareToBuilder.append(this.rangNaissance, beneficiaire.rangNaissance);
    compareToBuilder.append(this.refExternePersonne, beneficiaire.refExternePersonne);
    compareToBuilder.append(this.affiliation, beneficiaire.affiliation);
    compareToBuilder.append(this.idClientBO, beneficiaire.idClientBO);
    compareToBuilder.append(this.nirBeneficiaire, beneficiaire.nirBeneficiaire);
    compareToBuilder.append(this.cleNirBeneficiaire, beneficiaire.cleNirBeneficiaire);
    compareToBuilder.append(this.dateAdhesionMutuelle, beneficiaire.dateAdhesionMutuelle);
    compareToBuilder.append(this.nirOd2, beneficiaire.nirOd2);
    compareToBuilder.append(this.cleNirOd2, beneficiaire.cleNirOd2);
    compareToBuilder.append(this.insc, beneficiaire.insc);
    compareToBuilder.append(this.numeroPersonne, beneficiaire.numeroPersonne);
    compareToBuilder.append(
        this.dateDebutAdhesionIndividuelle, beneficiaire.dateDebutAdhesionIndividuelle);
    compareToBuilder.append(
        this.numeroAdhesionIndividuelle, beneficiaire.numeroAdhesionIndividuelle);
    compareToBuilder.append(this.dateRadiation, beneficiaire.dateRadiation);
    compareToBuilder.append(this.affiliationsRO, beneficiaire.affiliationsRO);
    compareToBuilder.append(this.periodesMedecinTraitant, beneficiaire.periodesMedecinTraitant);
    compareToBuilder.append(this.teletransmissions, beneficiaire.teletransmissions);
    compareToBuilder.append(this.regimesParticuliers, beneficiaire.regimesParticuliers);
    compareToBuilder.append(this.situationsParticulieres, beneficiaire.situationsParticulieres);
    compareToBuilder.append(this.isSouscripteurAlmv3, beneficiaire.isSouscripteurAlmv3);
    return compareToBuilder.toComparison();
  }
}
