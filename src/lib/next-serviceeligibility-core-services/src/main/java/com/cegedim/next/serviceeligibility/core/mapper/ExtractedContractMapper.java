package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.BeneficiaireMailleRefCouv;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.ContractTPMailleRefCouv;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.DomaineDroitMailleRefCouv;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.GarantieMailleRefCouv;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.MailleReferenceCouverture;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.ProduitMailleRefCouv;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ExtractedContractMapper {

  @NotNull
  public List<ExtractedContract> fromContract(@NotNull final ContractTPMailleRefCouv contractTP) {
    final var base =
        ExtractedContract.builder()
            .idDeclarant(contractTP.getIdDeclarant())
            .numeroContrat(contractTP.getNumeroContrat())
            .numeroAdherent(contractTP.getNumeroAdherent())
            .dateSouscription(contractTP.getDateSouscription())
            .dateResiliation(contractTP.getDateResiliation())
            .dateRestitution(contractTP.getDateRestitution())
            .build();
    return contractTP.getBeneficiaires().stream()
        .map(benef -> this.fromBeneficiaire(base, benef))
        .toList();
  }

  // --------------------
  // UTILS
  // --------------------
  private ExtractedContract fromBeneficiaire(
      final ExtractedContract base, final BeneficiaireMailleRefCouv benef) {
    final var domains =
        benef.getDomaineDroits().stream()
            .flatMap(domain -> this.fromDomain(domain).stream())
            .toList();

    return base.toBuilder()
        .dateCreation(benef.getDateCreation())
        .dateModification(benef.getDateModification())
        .dateNaissance(benef.getDateNaissance())
        .rangNaissance(benef.getRangNaissance())
        .nirBeneficiaire(benef.getNirBeneficiaire())
        .cleNirBeneficiaire(benef.getCleNirBeneficiaire())
        .nirOd1(benef.getNirOd1())
        .cleNirOd1(benef.getCleNirOd1())
        .nirOd2(benef.getNirOd2())
        .cleNirOd2(benef.getCleNirOd2())
        .domains(domains)
        .build();
  }

  private List<ExtractedDomain> fromDomain(final DomaineDroitMailleRefCouv domain) {
    ExtractedDomain extractedDomain =
        ExtractedDomain.builder().codeDomaine(domain.getCode()).build();
    return domain.getGaranties().stream()
        .flatMap(garantie -> this.fromGarantie(extractedDomain, garantie).stream())
        .toList();
  }

  private List<ExtractedDomain> fromGarantie(
      final ExtractedDomain extractedDomain, final GarantieMailleRefCouv garantie) {
    extractedDomain.setCodeGarantie(garantie.getCodeGarantie());
    extractedDomain.setLibelleGarantie(garantie.getLibelleGarantie());

    return garantie.getProduits().stream()
        .flatMap(produit -> this.fromProduit(extractedDomain, produit).stream())
        .toList();
  }

  private List<ExtractedDomain> fromProduit(
      final ExtractedDomain extractedDomain, final ProduitMailleRefCouv produit) {
    extractedDomain.setCodeProduit(produit.getCodeProduit());
    extractedDomain.setLibelleProduit(produit.getLibelleProduit());

    return produit.getReferencesCouverture().stream()
        .flatMap(couverture -> this.fromCouverture(extractedDomain, couverture).stream())
        .toList();
  }

  private List<ExtractedDomain> fromCouverture(
      final ExtractedDomain extractedDomain, final MailleReferenceCouverture couverture) {
    return couverture.getPeriodesDroit().stream()
        .flatMap(
            period ->
                couverture.getRemboursements().stream()
                    .flatMap(
                        remboursement ->
                            couverture.getPrestations().stream()
                                .map(
                                    prestation ->
                                        buildExtractedDomain(
                                            extractedDomain,
                                            couverture,
                                            period,
                                            remboursement,
                                            prestation))))
        .toList();
  }

  private ExtractedDomain buildExtractedDomain(
      ExtractedDomain extractedDomain,
      MailleReferenceCouverture couverture,
      PeriodeDroitContractTP period,
      RemboursementContrat remboursement,
      PrestationContrat prestation) {
    return ExtractedDomain.builder()
        .codeDomaine(extractedDomain.getCodeDomaine())
        .typePeriode(period.getTypePeriode())
        .periodeDebut(period.getPeriodeDebut())
        .periodeFin(period.getPeriodeFin())
        .periodeFinFermeture(period.getPeriodeFinFermeture())
        .codeGarantie(extractedDomain.getCodeGarantie())
        .libelleGarantie(extractedDomain.getLibelleGarantie())
        .codeProduit(extractedDomain.getCodeProduit())
        .libelleProduit(extractedDomain.getLibelleProduit())
        .referenceCouverture(couverture.getReferenceCouverture())
        .tauxRemboursement(remboursement.getTauxRemboursement())
        .uniteTauxRemboursement(remboursement.getUniteTauxRemboursement())
        .codeFormule(prestation.getFormule().getNumero())
        .libelleFormule(prestation.getFormule().getLibelle())
        .build();
  }
}
