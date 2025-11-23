package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.CodePeriodeDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.NirRattachementRODeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.TeletransmissionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode
public class BeneficiaireContractTPCommun implements GenericDomain<BeneficiaireContractTPCommun> {

  private Boolean isCarteTPaEditer;

  private String dateRadiation;

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime dateCreation;

  @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second)
  private LocalDateTime dateModification;

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
  private String rangAdministratif;
  private String categorieSociale;
  private String situationParticuliere;
  private String modePaiementPrestations;
  private String dateAdhesionMutuelle;
  private String dateDebutAdhesionIndividuelle;
  private String numeroAdhesionIndividuelle;
  private String dernierMouvementRecu;

  private Affiliation affiliation;
  private List<Adresse> adresses;

  private List<NirRattachementRODeclaration> affiliationsRO;
  private List<PeriodeComparable> periodesMedecinTraitant;
  private List<TeletransmissionDeclaration> teletransmissions;
  private List<CodePeriodeDeclaration> regimesParticuliers;
  private List<CodePeriodeDeclaration> situationsParticulieres;

  public BeneficiaireContractTPCommun() {
    /* empty constructor */ }

  public BeneficiaireContractTPCommun(BeneficiaireContractTPCommun source) {
    this.dateRadiation = source.getDateRadiation();
    this.dateCreation = source.getDateCreation();
    this.dateModification = source.getDateModification();
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
    this.rangAdministratif = source.getRangAdministratif();
    this.categorieSociale = source.getCategorieSociale();
    this.situationParticuliere = source.getSituationParticuliere();
    this.modePaiementPrestations = source.getModePaiementPrestations();
    this.dateAdhesionMutuelle = source.getDateAdhesionMutuelle();
    this.dateDebutAdhesionIndividuelle = source.getDateDebutAdhesionIndividuelle();
    this.numeroAdhesionIndividuelle = source.getNumeroAdhesionIndividuelle();
    this.dernierMouvementRecu = source.getDernierMouvementRecu();

    if (source.affiliation != null) {
      this.affiliation = new Affiliation(source.getAffiliation());
    }
    if (!CollectionUtils.isEmpty(source.getAdresses())) {
      this.adresses = new ArrayList<>();
      for (Adresse adress : source.getAdresses()) {
        this.adresses.add(new Adresse(adress));
      }
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
  public int compareTo(BeneficiaireContractTPCommun beneficiaireContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.dateRadiation, beneficiaireContract.dateRadiation);
    compareToBuilder.append(this.dateCreation, beneficiaireContract.dateCreation);
    compareToBuilder.append(this.dateModification, beneficiaireContract.dateModification);
    compareToBuilder.append(this.dateNaissance, beneficiaireContract.dateNaissance);
    compareToBuilder.append(this.rangNaissance, beneficiaireContract.rangNaissance);
    compareToBuilder.append(this.nirBeneficiaire, beneficiaireContract.nirBeneficiaire);
    compareToBuilder.append(this.cleNirBeneficiaire, beneficiaireContract.cleNirBeneficiaire);
    compareToBuilder.append(this.nirOd1, beneficiaireContract.nirOd1);
    compareToBuilder.append(this.cleNirOd1, beneficiaireContract.cleNirOd1);
    compareToBuilder.append(this.nirOd2, beneficiaireContract.nirOd2);
    compareToBuilder.append(this.cleNirOd2, beneficiaireContract.cleNirOd2);
    compareToBuilder.append(this.insc, beneficiaireContract.insc);
    compareToBuilder.append(this.numeroPersonne, beneficiaireContract.numeroPersonne);
    compareToBuilder.append(this.refExternePersonne, beneficiaireContract.refExternePersonne);
    compareToBuilder.append(this.rangAdministratif, beneficiaireContract.rangAdministratif);
    compareToBuilder.append(this.categorieSociale, beneficiaireContract.categorieSociale);
    compareToBuilder.append(this.situationParticuliere, beneficiaireContract.situationParticuliere);
    compareToBuilder.append(
        this.modePaiementPrestations, beneficiaireContract.modePaiementPrestations);
    compareToBuilder.append(this.dateAdhesionMutuelle, beneficiaireContract.dateAdhesionMutuelle);
    compareToBuilder.append(
        this.dateDebutAdhesionIndividuelle, beneficiaireContract.dateDebutAdhesionIndividuelle);
    compareToBuilder.append(
        this.numeroAdhesionIndividuelle, beneficiaireContract.numeroAdhesionIndividuelle);
    compareToBuilder.append(this.affiliation, beneficiaireContract.affiliation);
    compareToBuilder.append(this.adresses, beneficiaireContract.adresses);
    compareToBuilder.append(this.dernierMouvementRecu, beneficiaireContract.dernierMouvementRecu);
    compareToBuilder.append(this.affiliationsRO, beneficiaireContract.affiliationsRO);
    compareToBuilder.append(
        this.periodesMedecinTraitant, beneficiaireContract.periodesMedecinTraitant);
    compareToBuilder.append(this.teletransmissions, beneficiaireContract.teletransmissions);
    compareToBuilder.append(this.regimesParticuliers, beneficiaireContract.regimesParticuliers);
    compareToBuilder.append(
        this.situationsParticulieres, beneficiaireContract.situationsParticulieres);
    return compareToBuilder.toComparison();
  }
}
