package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.mapper.MapperContractTPMaille;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.BeneficiaireMailleDomaine;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.ContractTPMailleDomaine;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.MailleDomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.BeneficiaireMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.ContractTPMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.DomaineDroitMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.MailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.*;
import com.cegedim.next.serviceeligibility.core.utils.ContractTPAgregationUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContractTPAgregationService {

  private final MapperContractTPMaille mapperMaille;

  /**
   * Agrège les périodes d'un contrat TP à la maille référence couverture
   *
   * @param contractTP
   * @return le contrat TP à la maille référence couverture (les natures prestations ont été
   *     agrégées)
   */
  public ContractTPMailleRefCouv agregationMailleReferenceCouverture(ContractTP contractTP) {
    ContractTP copyContract = new ContractTP(contractTP);
    ContractTPMailleRefCouv contractTPMailleRefCouv =
        mapperMaille.contractTPToContractTPMailleRefCouv(copyContract);

    List<BeneficiaireMailleRefCouv> beneficiaireMailleRefCouvs = new ArrayList<>();
    for (BeneficiaireContractTP beneficiaireContractTP :
        ListUtils.emptyIfNull(copyContract.getBeneficiaires())) {
      BeneficiaireMailleRefCouv beneficiaireMailleRefCouv =
          mapperMaille.beneficiaireToBeneficiaireMailleRefCouv(beneficiaireContractTP);
      for (DomaineDroitContractTP domaineDroitContractTP :
          ListUtils.emptyIfNull(beneficiaireContractTP.getDomaineDroits())) {
        DomaineDroitMailleRefCouv domaineDroitMailleRefCouv =
            mapperMaille.domaineDroitToDomaineDroitMailleRefCouv(domaineDroitContractTP);
        for (Garantie garantie : ListUtils.emptyIfNull(domaineDroitContractTP.getGaranties())) {
          GarantieMailleRefCouv garantieMailleRefCouv =
              mapperMaille.garantieToGarantieMailleRefCouv(garantie);
          for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
            ProduitMailleRefCouv produitMailleRefCouv =
                mapperMaille.produitToProduitMailleRefCouv(produit);
            for (ReferenceCouverture referenceCouverture :
                ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
              MailleReferenceCouverture mailleReferenceCouverture =
                  aggregateNaturesPrestations(referenceCouverture);
              produitMailleRefCouv.getReferencesCouverture().add(mailleReferenceCouverture);
            }
            garantieMailleRefCouv.getProduits().add(produitMailleRefCouv);
          }
          domaineDroitMailleRefCouv.getGaranties().add(garantieMailleRefCouv);
        }
        beneficiaireMailleRefCouv.getDomaineDroits().add(domaineDroitMailleRefCouv);
      }
      beneficiaireMailleRefCouvs.add(beneficiaireMailleRefCouv);
    }
    contractTPMailleRefCouv.setBeneficiaires(beneficiaireMailleRefCouvs);
    return contractTPMailleRefCouv;
  }

  /**
   * Agrège les périodes d'un contrat TP à la maille produit
   *
   * @param contractTP
   * @return le contrat TP à la maille produit (les natures prestations et les références
   *     couvertures ont été agrégées)
   */
  public ContractTPMailleProduit agregationMailleProduit(ContractTP contractTP) {
    ContractTP copyContract = new ContractTP(contractTP);
    ContractTPMailleProduit contractTPMailleProduit =
        mapperMaille.contractTPToContractTPMailleProduit(copyContract);

    List<BeneficiaireMailleProduit> beneficiaireMailleProduits = new ArrayList<>();
    for (BeneficiaireContractTP beneficiaireContractTP :
        ListUtils.emptyIfNull(copyContract.getBeneficiaires())) {
      BeneficiaireMailleProduit beneficiaireMailleProduit =
          mapperMaille.beneficiaireToBeneficiaireMailleProduit(beneficiaireContractTP);
      for (DomaineDroitContractTP domaineDroitContractTP :
          ListUtils.emptyIfNull(beneficiaireContractTP.getDomaineDroits())) {
        DomaineDroitMailleProduit domaineDroitMailleProduit =
            mapperMaille.domaineDroitToDomaineDroitMailleProduit(domaineDroitContractTP);
        for (Garantie garantie : ListUtils.emptyIfNull(domaineDroitContractTP.getGaranties())) {
          GarantieMailleProduit garantieMailleProduit =
              mapperMaille.garantieToGarantieMailleProduit(garantie);
          for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
            List<MailleReferenceCouverture> mailleReferenceCouvertures = new ArrayList<>();
            for (ReferenceCouverture referenceCouverture :
                ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
              mailleReferenceCouvertures.add(aggregateNaturesPrestations(referenceCouverture));
            }
            garantieMailleProduit
                .getProduits()
                .add(aggregateReferencesCouvertures(mailleReferenceCouvertures, produit));
          }
          domaineDroitMailleProduit.getGaranties().add(garantieMailleProduit);
        }
        beneficiaireMailleProduit.getDomaineDroits().add(domaineDroitMailleProduit);
      }
      beneficiaireMailleProduits.add(beneficiaireMailleProduit);
    }
    contractTPMailleProduit.setBeneficiaires(beneficiaireMailleProduits);
    return contractTPMailleProduit;
  }

  /**
   * Agrège les périodes d'un contrat TP à la maille garantie
   *
   * @param contractTP
   * @return le contrat TP à la maille garantie (les natures prestations, les références couvertures
   *     et les produits ont été agrégés)
   */
  public ContractTPMailleGarantie agregationMailleGarantie(ContractTP contractTP) {
    ContractTP copyContract = new ContractTP(contractTP);
    ContractTPMailleGarantie contractTPMailleGarantie =
        mapperMaille.contractTPToContractTPMailleGarantie(copyContract);

    List<BeneficiaireMailleGarantie> beneficiaireMailleGaranties = new ArrayList<>();
    for (BeneficiaireContractTP beneficiaireContractTP :
        ListUtils.emptyIfNull(copyContract.getBeneficiaires())) {
      BeneficiaireMailleGarantie beneficiaireMailleGarantie =
          mapperMaille.beneficiaireToBeneficiaireMailleGarantie(beneficiaireContractTP);
      for (DomaineDroitContractTP domaineDroitContractTP :
          ListUtils.emptyIfNull(beneficiaireContractTP.getDomaineDroits())) {
        DomaineDroitMailleGarantie domaineDroitMailleGarantie =
            mapperMaille.domaineDroitToDomaineDroitMailleGarantie(domaineDroitContractTP);
        for (Garantie garantie : ListUtils.emptyIfNull(domaineDroitContractTP.getGaranties())) {
          List<MailleProduit> mailleProduits = new ArrayList<>();
          for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
            List<MailleReferenceCouverture> mailleReferenceCouvertures = new ArrayList<>();
            for (ReferenceCouverture referenceCouverture :
                ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
              mailleReferenceCouvertures.add(aggregateNaturesPrestations(referenceCouverture));
            }
            mailleProduits.add(aggregateReferencesCouvertures(mailleReferenceCouvertures, produit));
          }
          domaineDroitMailleGarantie
              .getGaranties()
              .add(aggregateProduits(mailleProduits, garantie));
        }
        beneficiaireMailleGarantie.getDomaineDroits().add(domaineDroitMailleGarantie);
      }
      beneficiaireMailleGaranties.add(beneficiaireMailleGarantie);
    }
    contractTPMailleGarantie.setBeneficiaires(beneficiaireMailleGaranties);
    return contractTPMailleGarantie;
  }

  /**
   * Agrège les périodes d'un contrat TP à la maille domaine
   *
   * @param contractTP
   * @return le contrat TP à la maille domaine (les natures prestations, les références couvertures,
   *     les produits et les garanties ont été agrégés)
   */
  public ContractTPMailleDomaine agregationMailleDomaine(ContractTP contractTP) {
    ContractTP copyContract = new ContractTP(contractTP);
    ContractTPMailleDomaine contractTPMailleDomaine =
        mapperMaille.contractTPToContractTPMailleDomaine(contractTP);

    List<BeneficiaireMailleDomaine> beneficiaireMailleDomaines = new ArrayList<>();
    for (BeneficiaireContractTP beneficiaireContractTP :
        ListUtils.emptyIfNull(copyContract.getBeneficiaires())) {
      BeneficiaireMailleDomaine beneficiaireMailleDomaine =
          mapperMaille.beneficiaireToBeneficiaireMailleDomaine(beneficiaireContractTP);
      for (DomaineDroitContractTP domaineDroitContractTP :
          ListUtils.emptyIfNull(beneficiaireContractTP.getDomaineDroits())) {
        List<MailleGarantie> mailleGaranties = new ArrayList<>();
        for (Garantie garantie : ListUtils.emptyIfNull(domaineDroitContractTP.getGaranties())) {
          List<MailleProduit> mailleProduits = new ArrayList<>();
          for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
            List<MailleReferenceCouverture> mailleReferenceCouvertures = new ArrayList<>();
            for (ReferenceCouverture referenceCouverture :
                ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
              mailleReferenceCouvertures.add(aggregateNaturesPrestations(referenceCouverture));
            }
            mailleProduits.add(aggregateReferencesCouvertures(mailleReferenceCouvertures, produit));
          }
          mailleGaranties.add(aggregateProduits(mailleProduits, garantie));
        }
        beneficiaireMailleDomaine
            .getDomaineDroits()
            .add(aggregateGaranties(mailleGaranties, domaineDroitContractTP));
      }
      beneficiaireMailleDomaines.add(beneficiaireMailleDomaine);
    }
    contractTPMailleDomaine.setBeneficiaires(beneficiaireMailleDomaines);
    return contractTPMailleDomaine;
  }

  /**
   * Agrège les périodes d'une liste de garanties
   *
   * @param mailleGaranties
   * @param domaine
   * @return un domaineDroit avec les périodes des garanties agrégées
   */
  private MailleDomaineDroit aggregateGaranties(
      List<MailleGarantie> mailleGaranties, DomaineDroitContractTP domaine) {
    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        ContractTPAgregationUtil.mergePeriodeDroitContractTP(
            mailleGaranties.stream()
                .flatMap(mailleGarantie -> mailleGarantie.getPeriodesDroit().stream())
                .collect(Collectors.toList()));
    List<ConventionnementContrat> conventionnementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleGaranties,
            MailleGarantie::getConventionnements,
            ConventionnementContrat::getPeriodes);
    List<PrestationContrat> prestationContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleGaranties, MailleGarantie::getPrestations, PrestationContrat::getPeriodes);
    List<RemboursementContrat> remboursementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleGaranties, MailleGarantie::getRemboursements, RemboursementContrat::getPeriodes);
    List<PrioriteDroitContrat> prioriteDroitContratList =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleGaranties, MailleGarantie::getPrioritesDroit, PrioriteDroitContrat::getPeriodes);
    ContractTPAgregationUtil.aggregatePeriodesLists(
        conventionnementContrats,
        prestationContrats,
        remboursementContrats,
        prioriteDroitContratList);
    MailleDomaineDroit mailleDomaineDroit = mapperMaille.domaineDroitToMailleDomaineDroit(domaine);
    mailleDomaineDroit.setPeriodesDroit(periodeDroitContractTPS);
    mailleDomaineDroit.setConventionnements(conventionnementContrats);
    mailleDomaineDroit.setPrestations(prestationContrats);
    mailleDomaineDroit.setPrioritesDroit(prioriteDroitContratList);
    mailleDomaineDroit.setRemboursements(remboursementContrats);
    return mailleDomaineDroit;
  }

  /**
   * Agrège les périodes d'une liste de produits
   *
   * @param mailleProduits
   * @param garantie
   * @return une garantie avec les periodes des produits agrégées
   */
  private MailleGarantie aggregateProduits(List<MailleProduit> mailleProduits, Garantie garantie) {
    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        ContractTPAgregationUtil.mergePeriodeDroitContractTP(
            mailleProduits.stream()
                .flatMap(produitMaille -> produitMaille.getPeriodesDroit().stream())
                .collect(Collectors.toList()));
    List<ConventionnementContrat> conventionnementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleProduits,
            MailleProduit::getConventionnements,
            ConventionnementContrat::getPeriodes);
    List<PrestationContrat> prestationContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleProduits, MailleProduit::getPrestations, PrestationContrat::getPeriodes);
    List<RemboursementContrat> remboursementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleProduits, MailleProduit::getRemboursements, RemboursementContrat::getPeriodes);
    List<PrioriteDroitContrat> prioriteDroitContratList =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleProduits, MailleProduit::getPrioritesDroit, PrioriteDroitContrat::getPeriodes);
    ContractTPAgregationUtil.aggregatePeriodesLists(
        conventionnementContrats,
        prestationContrats,
        remboursementContrats,
        prioriteDroitContratList);
    MailleGarantie mailleGarantie = mapperMaille.garantieToMailleGarantie(garantie);
    mailleGarantie.setPeriodesDroit(periodeDroitContractTPS);
    mailleGarantie.setConventionnements(conventionnementContrats);
    mailleGarantie.setPrestations(prestationContrats);
    mailleGarantie.setPrioritesDroit(prioriteDroitContratList);
    mailleGarantie.setRemboursements(remboursementContrats);
    return mailleGarantie;
  }

  /**
   * Agrège les périodes d'une liste de références couvertures
   *
   * @param mailleReferenceCouvertures
   * @param produit
   * @return un produit avec les périodes des références couvertures agrégées
   */
  private MailleProduit aggregateReferencesCouvertures(
      List<MailleReferenceCouverture> mailleReferenceCouvertures, Produit produit) {
    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        ContractTPAgregationUtil.mergePeriodeDroitContractTP(
            mailleReferenceCouvertures.stream()
                .flatMap(
                    referenceCouvertureMaille ->
                        referenceCouvertureMaille.getPeriodesDroit().stream())
                .collect(Collectors.toList()));
    List<ConventionnementContrat> conventionnementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleReferenceCouvertures,
            MailleReferenceCouverture::getConventionnements,
            ConventionnementContrat::getPeriodes);
    List<PrestationContrat> prestationContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleReferenceCouvertures,
            MailleReferenceCouverture::getPrestations,
            PrestationContrat::getPeriodes);
    List<RemboursementContrat> remboursementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleReferenceCouvertures,
            MailleReferenceCouverture::getRemboursements,
            RemboursementContrat::getPeriodes);
    List<PrioriteDroitContrat> prioriteDroitContratList =
        ContractTPAgregationUtil.mergeSubObjectsList(
            mailleReferenceCouvertures,
            MailleReferenceCouverture::getPrioritesDroit,
            PrioriteDroitContrat::getPeriodes);
    ContractTPAgregationUtil.aggregatePeriodesLists(
        conventionnementContrats,
        prestationContrats,
        remboursementContrats,
        prioriteDroitContratList);
    MailleProduit mailleProduit = mapperMaille.produitToProduitMaille(produit);
    mailleProduit.setPeriodesDroit(periodeDroitContractTPS);
    mailleProduit.setConventionnements(conventionnementContrats);
    mailleProduit.setPrestations(prestationContrats);
    mailleProduit.setPrioritesDroit(prioriteDroitContratList);
    mailleProduit.setRemboursements(remboursementContrats);
    return mailleProduit;
  }

  /**
   * Agrège les périodes d'une liste de natures prestations (contenue dans un objet
   * referenceCouverture)
   *
   * @param referenceCouverture
   * @return une référence couverture avec les périodes des natures prestations agrégées
   */
  private MailleReferenceCouverture aggregateNaturesPrestations(
      ReferenceCouverture referenceCouverture) {
    List<PeriodeDroitContractTP> periodeDroitContractTPS =
        ContractTPAgregationUtil.mergePeriodeDroitContractTP(
            referenceCouverture.getNaturesPrestation().stream()
                .flatMap(naturePrestation -> naturePrestation.getPeriodesDroit().stream())
                .collect(Collectors.toList()));
    List<ConventionnementContrat> conventionnementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            referenceCouverture.getNaturesPrestation(),
            NaturePrestation::getConventionnements,
            ConventionnementContrat::getPeriodes);
    List<PrestationContrat> prestationContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            referenceCouverture.getNaturesPrestation(),
            NaturePrestation::getPrestations,
            PrestationContrat::getPeriodes);
    List<RemboursementContrat> remboursementContrats =
        ContractTPAgregationUtil.mergeSubObjectsList(
            referenceCouverture.getNaturesPrestation(),
            NaturePrestation::getRemboursements,
            RemboursementContrat::getPeriodes);
    List<PrioriteDroitContrat> prioriteDroitContratList =
        ContractTPAgregationUtil.mergeSubObjectsList(
            referenceCouverture.getNaturesPrestation(),
            NaturePrestation::getPrioritesDroit,
            PrioriteDroitContrat::getPeriodes);
    ContractTPAgregationUtil.aggregatePeriodesLists(
        conventionnementContrats,
        prestationContrats,
        remboursementContrats,
        prioriteDroitContratList);
    return mapReferenceCouvertureMaille(
        referenceCouverture,
        periodeDroitContractTPS,
        prioriteDroitContratList,
        prestationContrats,
        remboursementContrats,
        conventionnementContrats);
  }

  private MailleReferenceCouverture mapReferenceCouvertureMaille(
      ReferenceCouverture referenceCouverture,
      List<PeriodeDroitContractTP> periodeDroitContractTPList,
      List<PrioriteDroitContrat> prioriteDroitContratList,
      List<PrestationContrat> prestationContratList,
      List<RemboursementContrat> remboursementContratList,
      List<ConventionnementContrat> conventionnementContratList) {
    MailleReferenceCouverture mailleReferenceCouverture = new MailleReferenceCouverture();
    mailleReferenceCouverture.setReferenceCouverture(referenceCouverture.getReferenceCouverture());
    mailleReferenceCouverture.setFormulaMask(referenceCouverture.getFormulaMask());
    mailleReferenceCouverture.setPeriodesDroit(periodeDroitContractTPList);
    mailleReferenceCouverture.setPrestations(prestationContratList);
    mailleReferenceCouverture.setConventionnements(conventionnementContratList);
    mailleReferenceCouverture.setPrioritesDroit(prioriteDroitContratList);
    mailleReferenceCouverture.setRemboursements(remboursementContratList);
    return mailleReferenceCouverture;
  }
}
