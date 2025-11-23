package com.cegedim.next.serviceeligibility.core.model.entity;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CARTE_DEMATERIALISEE;

import com.cegedim.next.serviceeligibility.core.job.utils.Constants;
import com.cegedim.next.serviceeligibility.core.model.domain.Beneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.audit.Audit;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Document(collection = Constants.COLLECTION_CONSOLIDATION_CARTES)
public class DeclarationConsolide extends DocumentEntity
    implements GenericDomain<DeclarationConsolide>, Audit {
  private String AMC_contrat;
  private Beneficiaire beneficiaire;
  // Valeur par defaut dans le cas d une ancienne declaration conso
  private List<String> codeServices = new ArrayList<>(List.of(CARTE_DEMATERIALISEE));
  private Contrat contrat;
  private Date dateConsolidation;
  private boolean declarationValide;
  private List<DomaineDroit> domaineDroits;
  private Date effetDebut;
  private String idDeclarant;
  private String idDeclarations;
  private String periodeDebut;
  private String periodeFin;

  private List<DomaineConvention> listeDomainesConventions;

  private DomaineDroit produit;

  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;

  private String idOrigine;
  private String numAMCEchange;

  private String identifiant;

  public DeclarationConsolide(DeclarationConsolide source) {
    this.set_id(source.get_id());
    this.AMC_contrat = source.AMC_contrat;
    this.beneficiaire = new Beneficiaire(source.beneficiaire);
    this.codeServices = source.codeServices;
    this.contrat = new Contrat(source.contrat);
    this.dateConsolidation = source.dateConsolidation;
    this.declarationValide = source.declarationValide;
    this.effetDebut = source.effetDebut;
    this.idDeclarant = source.idDeclarant;
    this.idDeclarations = source.idDeclarations;
    this.periodeDebut = source.periodeDebut;
    this.periodeFin = source.periodeFin;
    if (source.produit != null) {
      this.produit = new DomaineDroit(source.produit);
    }
    this.dateCreation = source.dateCreation;
    this.userCreation = source.userCreation;
    this.dateModification = source.dateModification;
    this.userModification = source.userModification;
    this.idOrigine = source.idOrigine;
    this.identifiant = source.identifiant;
    this.listeDomainesConventions = source.listeDomainesConventions;
    this.domaineDroits = new ArrayList<>();
    for (DomaineDroit domaineD : source.domaineDroits) {
      domaineDroits.add(new DomaineDroit(domaineD));
    }
  }

  @Override
  public int compareTo(DeclarationConsolide o) {
    return 0;
  }

  @JsonProperty("AMC_contrat")
  public void setAMC_contrat(String AMC_contrat) {
    this.AMC_contrat = AMC_contrat;
  }

  public List<DomaineDroit> getDomaineDroits() {
    if (this.domaineDroits == null) {
      this.domaineDroits = new ArrayList<>();
    }
    return this.domaineDroits;
  }
}
