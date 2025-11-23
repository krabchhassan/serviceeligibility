package com.cegedim.next.serviceeligibility.core.model.entity.card;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CARTE_DEMATERIALISEE;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.CollectionUtils;

/** Classe qui mappe la collection carteDemat dans la base de donnees. */
@Document(collection = "cartesDemat")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CarteDemat extends DocumentEntity implements GenericDomain<CarteDemat>, Audit {

  private static final long serialVersionUID = 1L;

  ////////////////
  // Attributes //
  ////////////////

  // Properties
  private String idDeclarant;
  private String periodeDebut;
  private String periodeFin;

  @JsonProperty("AMC_contrat")
  private String AMC_contrat;

  private boolean isLastCarteDemat;

  private String codeClient;

  // Trace
  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;
  private Date dateConsolidation;

  // Embedded documents
  private Contrat contrat;
  private Adresse adresse;
  private List<BenefCarteDemat> beneficiaires;
  private List<DomaineConvention> domainesConventions;

  private List<String> idDeclarations;
  private List<String> idDeclarationsConsolides;
  // Valeur par defaut dans le cas d une ancienne carte demat
  private List<String> codeServices = new ArrayList<>(List.of(CARTE_DEMATERIALISEE));

  private String identifiant;

  //////////////////////
  // Custom Accessors //
  //////////////////////
  public boolean getIsLastCarteDemat() {
    return isLastCarteDemat;
  }

  public void setIsLastCarteDemat(boolean b) {
    this.isLastCarteDemat = b;
  }

  public List<DomaineConvention> getDomainesConventions() {
    if (this.domainesConventions == null) {
      this.domainesConventions = new ArrayList<>();
    }
    return this.domainesConventions;
  }

  public List<BenefCarteDemat> getBeneficiaires() {
    if (this.beneficiaires == null) {
      this.beneficiaires = new ArrayList<>();
    }
    return this.beneficiaires;
  }

  /////////////
  // Methods //
  /////////////
  public CarteDemat(CarteDemat source) {
    this.idDeclarant = source.getIdDeclarant();
    this.periodeDebut = source.getPeriodeDebut();
    this.periodeFin = source.getPeriodeFin();
    this.isLastCarteDemat = source.getIsLastCarteDemat();
    this.AMC_contrat = source.getAMC_contrat();
    this.dateCreation = source.getDateCreation();
    this.dateConsolidation = source.getDateConsolidation();
    this.userCreation = source.getUserCreation();
    this.dateModification = source.getDateModification();
    this.userModification = source.getUserModification();
    /* DOCUMENTS EMBEDDED */
    if (source.getContrat() != null) {
      this.contrat = new Contrat(source.getContrat());
    }
    if (source.getAdresse() != null) {
      this.adresse = new Adresse(source.getAdresse());
    }
    if (!CollectionUtils.isEmpty(source.getBeneficiaires())) {
      this.beneficiaires = new ArrayList<>();
      for (BenefCarteDemat benef : source.getBeneficiaires()) {
        this.beneficiaires.add(new BenefCarteDemat(benef));
      }
    }
  }

  @Override
  public int compareTo(final CarteDemat carte) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.idDeclarant, carte.idDeclarant);
    compareToBuilder.append(this.periodeDebut, carte.periodeDebut);
    compareToBuilder.append(this.periodeFin, carte.periodeFin);
    compareToBuilder.append(this.domainesConventions, carte.domainesConventions);
    compareToBuilder.append(this.beneficiaires, carte.beneficiaires);
    compareToBuilder.append(this.contrat, carte.contrat);
    compareToBuilder.append(this.adresse, carte.adresse);
    return compareToBuilder.toComparison();
  }
}
