package com.cegedim.next.serviceeligibility.core.model.entity.card;

import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartepapier.DomaineConventionCartePapier;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;

/** Classe qui mappe l'objet CartePapier */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CartePapier implements GenericDomain<CartePapier> {

  private static final long serialVersionUID = 1L;

  ////////////////
  // Attributes //
  ////////////////

  // Properties
  private String numeroAMC;
  private String nomAMC;
  private String libelleAMC;
  private String periodeDebut;
  private String periodeFin;

  private String societeEmettrice;
  private String codeRenvoi;
  private String libelleRenvoi;
  private String codeConvention;
  private String libelleConvention;

  private String fondCarte;
  private String annexe1Carte;
  private String annexe2Carte;
  private String numAMCEchange;
  private String numOperateur;
  private String dateTraitement;

  // Embedded documents
  private Contrat contrat;
  private Adresse adresse;
  private List<BenefCarteDemat> beneficiaires;
  private List<DomaineConventionCartePapier> domainesConventions;

  private String contexte = Constants.ANNUEL;

  //////////////////////
  // Custom Accessors //
  //////////////////////

  public List<DomaineConventionCartePapier> getDomainesConventions() {
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

  @Override
  public int compareTo(final CartePapier carte) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.numeroAMC, carte.numeroAMC);
    compareToBuilder.append(this.periodeDebut, carte.periodeDebut);
    compareToBuilder.append(this.periodeFin, carte.periodeFin);
    compareToBuilder.append(this.domainesConventions, carte.domainesConventions);
    compareToBuilder.append(this.beneficiaires, carte.beneficiaires);
    compareToBuilder.append(this.contrat, carte.contrat);
    compareToBuilder.append(this.adresse, carte.adresse);
    return compareToBuilder.toComparison();
  }
}
