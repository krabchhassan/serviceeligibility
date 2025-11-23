package com.cegedim.next.serviceeligibility.core.mapper;

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
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface MapperContractTPMaille {

  // Mapper à la maille reference couverture
  @Mapping(target = "beneficiaires", ignore = true)
  ContractTPMailleRefCouv contractTPToContractTPMailleRefCouv(ContractTP contractTP);

  @Mapping(target = "domaineDroits", ignore = true)
  BeneficiaireMailleRefCouv beneficiaireToBeneficiaireMailleRefCouv(
      BeneficiaireContractTP beneficiaireContractTP);

  @Mapping(target = "garanties", ignore = true)
  DomaineDroitMailleRefCouv domaineDroitToDomaineDroitMailleRefCouv(
      DomaineDroitContractTP domaineDroitContractTP);

  @Mapping(target = "produits", ignore = true)
  GarantieMailleRefCouv garantieToGarantieMailleRefCouv(Garantie garantie);

  @Mapping(target = "referencesCouverture", ignore = true)
  ProduitMailleRefCouv produitToProduitMailleRefCouv(Produit produit);

  // Mapper à la maille produit
  @Mapping(target = "beneficiaires", ignore = true)
  ContractTPMailleProduit contractTPToContractTPMailleProduit(ContractTP contractTP);

  @Mapping(target = "domaineDroits", ignore = true)
  BeneficiaireMailleProduit beneficiaireToBeneficiaireMailleProduit(
      BeneficiaireContractTP beneficiaireContractTP);

  @Mapping(target = "garanties", ignore = true)
  DomaineDroitMailleProduit domaineDroitToDomaineDroitMailleProduit(
      DomaineDroitContractTP domaineDroitContractTP);

  @Mapping(target = "produits", ignore = true)
  GarantieMailleProduit garantieToGarantieMailleProduit(Garantie garantie);

  @Mapping(target = "prestations", ignore = true)
  @Mapping(target = "prioritesDroit", ignore = true)
  @Mapping(target = "remboursements", ignore = true)
  @Mapping(target = "periodesDroit", ignore = true)
  @Mapping(target = "conventionnements", ignore = true)
  MailleProduit produitToProduitMaille(Produit produit);

  // Mapper à la maille garantie
  @Mapping(target = "beneficiaires", ignore = true)
  ContractTPMailleGarantie contractTPToContractTPMailleGarantie(ContractTP contractTP);

  @Mapping(target = "domaineDroits", ignore = true)
  BeneficiaireMailleGarantie beneficiaireToBeneficiaireMailleGarantie(
      BeneficiaireContractTP beneficiaireContractTP);

  @Mapping(target = "garanties", ignore = true)
  DomaineDroitMailleGarantie domaineDroitToDomaineDroitMailleGarantie(
      DomaineDroitContractTP domaineDroitContractTP);

  @Mapping(target = "prestations", ignore = true)
  @Mapping(target = "prioritesDroit", ignore = true)
  @Mapping(target = "remboursements", ignore = true)
  @Mapping(target = "periodesDroit", ignore = true)
  @Mapping(target = "conventionnements", ignore = true)
  MailleGarantie garantieToMailleGarantie(Garantie garantie);

  // Mapper à la maille domaine droit
  @Mapping(target = "beneficiaires", ignore = true)
  ContractTPMailleDomaine contractTPToContractTPMailleDomaine(ContractTP contractTP);

  @Mapping(target = "domaineDroits", ignore = true)
  BeneficiaireMailleDomaine beneficiaireToBeneficiaireMailleDomaine(
      BeneficiaireContractTP beneficiaireContractTP);

  @Mapping(target = "prestations", ignore = true)
  @Mapping(target = "prioritesDroit", ignore = true)
  @Mapping(target = "remboursements", ignore = true)
  @Mapping(target = "periodesDroit", ignore = true)
  @Mapping(target = "conventionnements", ignore = true)
  MailleDomaineDroit domaineDroitToMailleDomaineDroit(
      DomaineDroitContractTP domaineDroitContractTP);
}
