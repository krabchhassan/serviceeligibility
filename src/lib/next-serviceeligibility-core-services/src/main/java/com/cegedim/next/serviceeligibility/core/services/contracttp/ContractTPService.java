package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.dao.ContractDao;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.benef.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.PeriodeComparable;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.services.pojo.DomaineDroitForConsolidation;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * Cette classe héberge les méthodes utilisées par le 634J et le consolidationcontract-worker. Pour
 * le worker, on utilise la collection contrat par défaut. Pour le 634J, le nom de la collection
 * peut être surchargé dans des cas de reprise.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContractTPService {

  private final ContractDao contractDao;

  private final DomaineTPService domaineTPService;

  /**
   * @param declaration déclaration à traiter
   * @return nbContratCree -2 : ano déclaration (pas de modif contrat) / -1 : contrat supprimé / 0 :
   *     maj de contrat / 1 : creation de contrat
   */
  @ContinueSpan(log = "processDeclaration (1 param)")
  public int processDeclaration(Declaration declaration) {
    return processDeclaration(declaration, Constants.CONTRATS_COLLECTION_NAME);
  }

  /**
   * @param declaration déclaration à traiter
   * @param collection collection contrat
   * @return nbContratCree -2 : ano déclaration (pas de modif contrat) / -1 : contrat supprimé / 0 :
   *     maj de contrat / 1 : creation de contrat
   */
  @NewSpan()
  public int processDeclaration(Declaration declaration, String collection) {
    int nbContratCree = 0;
    ContractTP contrat =
        contractDao.getContract(
            declaration.getIdDeclarant(),
            declaration.getContrat().getNumero(),
            declaration.getContrat().getNumeroAdherent(),
            collection);
    ContractTP contratToCreateOrUpdate;
    if (contrat == null) {
      // create contrat
      contratToCreateOrUpdate = toContrat(declaration);
      nbContratCree = 1;
    } else {
      // update contrat
      contratToCreateOrUpdate = updateContrat(contrat, declaration);
    }
    if (contratToCreateOrUpdate != null
        && CollectionUtils.isNotEmpty(contratToCreateOrUpdate.getBeneficiaires())) {
      contractDao.saveContract(contratToCreateOrUpdate, collection);
    } else {
      if (contrat != null && contratToCreateOrUpdate != null) {
        nbContratCree = -1;
        log.debug("Suppression du contrat {}", contrat.get_id());
        contractDao.deleteContract(contrat.get_id(), collection);
      } else {
        nbContratCree = -2;
        log.warn("Impossible de consolider la déclaration {}", declaration.get_id());
      }
    }
    return nbContratCree;
  }

  /**
   * @param declaration déclaration à traiter
   * @param collection collection contrat
   * @return nbContratCree -2 : ano déclaration (pas de modif contrat) / -1 : contrat supprimé / 0 :
   *     maj de contrat / 1 : creation de contrat
   */
  @NewSpan()
  public int processDeclarationJob(
      Declaration declaration, String collection, BulkContratTP bulkContratTP) {
    int nbContratCree = 0;
    ContractTP contrat =
        contractDao.getContract(
            declaration.getIdDeclarant(),
            declaration.getContrat().getNumero(),
            declaration.getContrat().getNumeroAdherent(),
            collection);
    ContractTP contratToCreateOrUpdate;
    if (contrat == null) {
      // create contrat
      contratToCreateOrUpdate = toContrat(declaration);
      nbContratCree = 1;
    } else {
      // update contrat
      contratToCreateOrUpdate = updateContrat(contrat, declaration);
    }
    if (contratToCreateOrUpdate != null
        && CollectionUtils.isNotEmpty(contratToCreateOrUpdate.getBeneficiaires())) {
      if (nbContratCree == 0) {
        bulkContratTP.toUpdate.add(contratToCreateOrUpdate);
      } else {
        bulkContratTP.toInsert.add(contratToCreateOrUpdate);
      }
    } else {
      if (contrat != null && contratToCreateOrUpdate != null) {
        nbContratCree = -1;
        log.debug("Suppression du contrat {}", contrat.get_id());
        bulkContratTP.toDelete.add(contrat);
      } else {
        nbContratCree = -2;
        log.warn("Impossible de consolider la déclaration {}", declaration.get_id());
      }
    }
    if (nbContratCree != -2) {
      bulkContratTP.contractIds.add(
          declaration.getIdDeclarant()
              + declaration.getContrat().getNumero()
              + declaration.getContrat().getNumeroAdherent());
      bulkContratTP.compteur++;
    }
    return nbContratCree;
  }

  /**
   * @param declaration Déclaration à empiler
   * @param contrat Contrat à enrichier ou null pour avoir un nouveau contrat
   * @return le contrat mis à jour
   */
  public ContractTP processDeclarationRDO(Declaration declaration, ContractTP contrat) {
    if (contrat == null) {
      // create contrat
      contrat = toContrat(declaration);
    } else {
      // update contrat
      contrat = updateContrat(contrat, declaration);
    }
    if (contrat == null) {
      log.warn("Impossible de consolider la déclaration {}", declaration.get_id());
    } else if (CollectionUtils.isEmpty(contrat.getBeneficiaires())) {
      // S'il n'y a pas de bénéficiaire on n'enregistre pas le contrat
      contrat = null;
    }
    return contrat;
  }

  ContractTP initContract(Declaration declaration, LocalDateTime now) {
    ContractTP contractTP = new ContractTP();
    mapDeclarationToContrat(declaration, now, contractTP, true);
    return contractTP;
  }

  private static void mapDeclarationToContrat(
      Declaration declaration, LocalDateTime now, ContractTP contractTP, boolean creation) {
    Contrat contratDeclaration = declaration.getContrat();
    if (creation) {
      contractTP.setOrigineDeclaration(TriggerUtils.getOrigineDeclaration(declaration));
      contractTP.setDateCreation(now);
      contractTP.setIdDeclarant(declaration.getIdDeclarant());
      contractTP.setNumeroContrat(contratDeclaration.getNumero());
      contractTP.setNumeroAdherent(contratDeclaration.getNumeroAdherent());
      contractTP.setNumeroAdherentComplet(contratDeclaration.getNumeroAdherentComplet());
      contractTP.setDateSouscription(contratDeclaration.getDateSouscription());
      contractTP.setType(contratDeclaration.getType());
      contractTP.setNumeroExterneContratIndividuel(
          contratDeclaration.getNumeroExterneContratIndividuel());
      contractTP.setNumeroExterneContratCollectif(
          contratDeclaration.getNumeroExterneContratCollectif());
    }
    contractTP.setDateResiliation(contratDeclaration.getDateResiliation());
    contractTP.setNomPorteur(contratDeclaration.getNomPorteur());
    contractTP.setPrenomPorteur(contratDeclaration.getPrenomPorteur());
    contractTP.setCivilitePorteur(contratDeclaration.getCivilitePorteur());
    contractTP.setQualification(contratDeclaration.getQualification());
    contractTP.setNumeroContratCollectif(contratDeclaration.getNumeroContratCollectif());
    contractTP.setIsContratResponsable(contratDeclaration.getIsContratResponsable());
    contractTP.setIsContratCMU(contratDeclaration.getIsContratCMU());
    contractTP.setDestinataire(contratDeclaration.getDestinataire());
    contractTP.setIndividuelOuCollectif(contratDeclaration.getIndividuelOuCollectif());
    contractTP.setSituationDebut(contratDeclaration.getSituationDebut());
    contractTP.setSituationFin(contratDeclaration.getSituationFin());
    contractTP.setMotifFinSituation(contratDeclaration.getMotifFinSituation());
    contractTP.setTypeConvention(contratDeclaration.getTypeConvention());
    contractTP.setCritereSecondaire(contratDeclaration.getCritereSecondaire());
    contractTP.setCritereSecondaireDetaille(contratDeclaration.getCritereSecondaireDetaille());
    contractTP.setSiret(contratDeclaration.getSiret());
    contractTP.setIdentifiantCollectivite(contratDeclaration.getIdentifiantCollectivite());
    contractTP.setRaisonSociale(contratDeclaration.getRaisonSociale());
    contractTP.setGroupePopulation(contratDeclaration.getGroupePopulation());
    contractTP.setGestionnaire(contratDeclaration.getGestionnaire());
    contractTP.setGroupeAssures(contratDeclaration.getGroupeAssures());
    contractTP.setNumAMCEchange(contratDeclaration.getNumAMCEchange());
    contractTP.setNumOperateur(contratDeclaration.getNumOperateur());

    contractTP.setOrdrePriorisation(contratDeclaration.getOrdrePriorisation());
    contractTP.setContratCMUC2S(contratDeclaration.getContratCMUC2S());
    contractTP.setDateRestitution(declaration.getDateRestitution());

    updatePeriodesSuspension(contractTP, declaration);
    updatePeriodeCMUOuverts(contractTP, declaration);
    updatePeriodeResponsableOuverts(contractTP, declaration);

    contractTP.setDateModification(now);
    contractTP.setDateConsolidation(now);

    contractTP.setCodeItelis(contratDeclaration.getCodeItelis());
    contractTP.setCarteTPaEditerOuDigitale(declaration.getCarteTPaEditerOuDigitale());
  }

  private static void updatePeriodeResponsableOuverts(
      ContractTP contractTP, Declaration declaration) {
    List<PeriodeComparable> periodeResponsableOuverts =
        declaration.getContrat().getPeriodeResponsableOuverts();
    contractTP.setPeriodeResponsableOuverts(
        deepCopy(periodeResponsableOuverts, PeriodeComparable::new));
  }

  private static void updatePeriodeCMUOuverts(ContractTP contractTP, Declaration declaration) {
    List<PeriodeCMUOuvert> periodeCMUOuverts = declaration.getContrat().getPeriodeCMUOuverts();
    contractTP.setPeriodeCMUOuverts(deepCopy(periodeCMUOuverts, PeriodeCMUOuvert::new));
  }

  static void updatePeriodesSuspension(ContractTP contract, Declaration declaration) {
    List<PeriodeSuspensionDeclaration> periodeSuspensionDeclarations =
        declaration.getContrat().getPeriodeSuspensions();
    if (!CollectionUtils.isEmpty(periodeSuspensionDeclarations)) {
      SuspensionContract suspensionContract = new SuspensionContract();
      List<PeriodeSuspensionContract> periodeSuspensionContracts = new ArrayList<>();
      for (PeriodeSuspensionDeclaration periodeSuspensionDeclaration :
          periodeSuspensionDeclarations) {
        PeriodeSuspensionContract periodeSuspensionContract = new PeriodeSuspensionContract();
        periodeSuspensionContract.setDateDebutSuspension(periodeSuspensionDeclaration.getDebut());
        periodeSuspensionContract.setDateFinSuspension(periodeSuspensionDeclaration.getFin());
        periodeSuspensionContracts.add(periodeSuspensionContract);
      }
      suspensionContract.setPeriodesSuspension(periodeSuspensionContracts);
      suspensionContract.setEtatSuspension(declaration.getEtatSuspension());
      contract.setSuspension(suspensionContract);
      Comparator<PeriodeSuspensionContract> startDateComparator =
          Comparator.comparing(PeriodeSuspensionContract::getDateDebutSuspension);
      contract.getSuspension().getPeriodesSuspension().sort(startDateComparator);
    } else {
      contract.setSuspension(null);
    }
  }

  private BeneficiaireContractTP initBeneficiaires(
      Declaration declaration,
      LocalDateTime now,
      Map<String, Set<DomaineDroitForConsolidation>> declMapDomain,
      boolean withDomain) {
    BeneficiaireV2 beneficiaireDeclaration = declaration.getBeneficiaire();
    Contrat contratDeclaration = declaration.getContrat();
    BeneficiaireContractTP beneficiaireContract = new BeneficiaireContractTP();
    beneficiaireContract.setDateRadiation(beneficiaireDeclaration.getDateRadiation());
    beneficiaireContract.setDateCreation(now);
    beneficiaireContract.setDateModification(now);
    beneficiaireContract.setDateNaissance(beneficiaireDeclaration.getDateNaissance());
    beneficiaireContract.setRangNaissance(beneficiaireDeclaration.getRangNaissance());
    beneficiaireContract.setNirBeneficiaire(beneficiaireDeclaration.getNirBeneficiaire());
    beneficiaireContract.setCleNirBeneficiaire(beneficiaireDeclaration.getCleNirBeneficiaire());
    beneficiaireContract.setNirOd1(beneficiaireDeclaration.getNirOd1());
    beneficiaireContract.setCleNirOd1(beneficiaireDeclaration.getCleNirOd1());
    beneficiaireContract.setNirOd2(beneficiaireDeclaration.getNirOd2());
    beneficiaireContract.setCleNirOd2(beneficiaireDeclaration.getCleNirOd2());
    beneficiaireContract.setInsc(beneficiaireDeclaration.getInsc());
    beneficiaireContract.setNumeroPersonne(beneficiaireDeclaration.getNumeroPersonne());
    beneficiaireContract.setRefExternePersonne(beneficiaireDeclaration.getRefExternePersonne());

    beneficiaireContract.setRangAdministratif(contratDeclaration.getRangAdministratif());
    beneficiaireContract.setCategorieSociale(contratDeclaration.getCategorieSociale());
    beneficiaireContract.setSituationParticuliere(contratDeclaration.getSituationParticuliere());
    beneficiaireContract.setModePaiementPrestations(
        contratDeclaration.getModePaiementPrestations());

    beneficiaireContract.setDateAdhesionMutuelle(beneficiaireDeclaration.getDateAdhesionMutuelle());
    beneficiaireContract.setDateDebutAdhesionIndividuelle(
        beneficiaireDeclaration.getDateDebutAdhesionIndividuelle());
    beneficiaireContract.setNumeroAdhesionIndividuelle(
        beneficiaireDeclaration.getNumeroAdhesionIndividuelle());

    beneficiaireContract.setAffiliation(beneficiaireDeclaration.getAffiliation());
    if (CollectionUtils.isNotEmpty(beneficiaireDeclaration.getAdresses())) {
      beneficiaireContract.setAdresses(mapAdress(beneficiaireDeclaration.getAdresses()));
    }
    beneficiaireContract.setDernierMouvementRecu(declaration.getCodeEtat());

    beneficiaireContract.setAffiliationsRO(
        deepCopy(beneficiaireDeclaration.getAffiliationsRO(), NirRattachementRODeclaration::new));
    beneficiaireContract.setPeriodesMedecinTraitant(
        deepCopy(beneficiaireDeclaration.getPeriodesMedecinTraitant(), PeriodeComparable::new));
    beneficiaireContract.setTeletransmissions(
        deepCopy(beneficiaireDeclaration.getTeletransmissions(), TeletransmissionDeclaration::new));
    beneficiaireContract.setRegimesParticuliers(
        deepCopy(beneficiaireDeclaration.getRegimesParticuliers(), CodePeriodeDeclaration::new));
    beneficiaireContract.setSituationsParticulieres(
        deepCopy(
            beneficiaireDeclaration.getSituationsParticulieres(), CodePeriodeDeclaration::new));
    List<DomaineDroitContractTP> contratDomains = new ArrayList<>();

    if (withDomain) {
      for (Set<DomaineDroitForConsolidation> declGarantiDomMap : declMapDomain.values()) {
        DomaineDroitContractTP contratDom = domaineTPService.createDomaineDroit(declGarantiDomMap);
        contratDomains.add(contratDom);
      }

      beneficiaireContract.setDomaineDroits(contratDomains);
    }

    return beneficiaireContract;
  }

  private List<Adresse> mapAdress(List<AdresseAvecFixe> adressesSource) {
    List<Adresse> adresses = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(adressesSource)) {
      adressesSource.forEach(adresseAvecFixe -> adresses.add(new Adresse(adresseAvecFixe)));
    }

    return adresses;
  }

  // Contrat from Declaration (new contrat)
  private ContractTP toContrat(Declaration declaration) {
    LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
    ContractTP contractTP = initContract(declaration, now);
    BeneficiaireContractTP beneficiaireContract =
        initBeneficiaires(declaration, now, domaineTPService.fillDomainMap(declaration), true);
    List<BeneficiaireContractTP> benefs = new ArrayList<>();
    benefs.add(beneficiaireContract);
    contractTP.setBeneficiaires(benefs);
    if (CollectionUtils.isEmpty(contractTP.getBeneficiaires())) {
      return null;
    }

    return contractTP;
  }

  // Step 3 - Update Contrat
  private ContractTP updateContrat(ContractTP contractTP, Declaration declaration) {
    LocalDateTime dateNow = LocalDateTime.now(ZoneOffset.UTC);
    mapDeclarationToContrat(declaration, dateNow, contractTP, false);
    Map<String, Set<DomaineDroitForConsolidation>> declMapDomain =
        domaineTPService.fillDomainMap(declaration);
    if (StringUtils.isBlank(declaration.getBeneficiaire().getNumeroPersonne())) {
      return null;
    }
    String newNumPersonne = declaration.getBeneficiaire().getNumeroPersonne();
    HashMap<String, BeneficiaireContractTP> mapOldBeneficiaries = new HashMap<>();
    List<BeneficiaireContractTP> listOldBeneficiaries = contractTP.getBeneficiaires();
    for (BeneficiaireContractTP oldBenef : listOldBeneficiaries) {
      String numPersonne = oldBenef.getNumeroPersonne();
      mapOldBeneficiaries.put(numPersonne, oldBenef);
    }

    // Step 4 - ADD/Update Beneficiaire
    BeneficiaireContractTP benToEdit = mapOldBeneficiaries.get(newNumPersonne);
    // vérifie si le benef existait déjà
    if (benToEdit == null) {
      listOldBeneficiaries.add(initBeneficiaires(declaration, dateNow, declMapDomain, true));
    } else {
      listOldBeneficiaries.remove(benToEdit);
      BeneficiaireContractTP newBenef =
          initBeneficiaires(declaration, dateNow, declMapDomain, false);
      updateBenef(benToEdit, newBenef, declaration, dateNow, declMapDomain);
      if (CollectionUtils.isNotEmpty(benToEdit.getDomaineDroits())) {
        listOldBeneficiaries.add(benToEdit);
      }
    }
    contractTP.setBeneficiaires(listOldBeneficiaries);
    return contractTP;
  }

  // Step 4 - Update Beneficiaire
  private void updateBenef(
      BeneficiaireContractTP benToEdit,
      BeneficiaireContractTP newBenef,
      Declaration declaration,
      LocalDateTime dateNow,
      Map<String, Set<DomaineDroitForConsolidation>> declMapDomain) {
    Contrat contratDeclaration = declaration.getContrat();
    benToEdit.setDateRadiation(newBenef.getDateRadiation());
    benToEdit.setDateModification(dateNow);
    benToEdit.setNirBeneficiaire(newBenef.getNirBeneficiaire());
    benToEdit.setCleNirBeneficiaire(newBenef.getCleNirBeneficiaire());
    benToEdit.setNirOd1(newBenef.getNirOd1());
    benToEdit.setCleNirOd1(newBenef.getCleNirOd1());
    benToEdit.setNirOd2(newBenef.getNirOd2());
    benToEdit.setCleNirOd2(newBenef.getCleNirOd2());
    benToEdit.setInsc(newBenef.getInsc());
    benToEdit.setNumeroPersonne(newBenef.getNumeroPersonne());
    benToEdit.setRefExternePersonne(newBenef.getRefExternePersonne());
    benToEdit.setDateNaissance(newBenef.getDateNaissance());
    benToEdit.setRangNaissance(newBenef.getRangNaissance());

    benToEdit.setRangAdministratif(newBenef.getRangAdministratif());
    benToEdit.setCategorieSociale(contratDeclaration.getCategorieSociale());
    benToEdit.setSituationParticuliere(contratDeclaration.getSituationParticuliere());
    benToEdit.setModePaiementPrestations(contratDeclaration.getModePaiementPrestations());

    benToEdit.setDateAdhesionMutuelle(newBenef.getDateAdhesionMutuelle());
    benToEdit.setDateDebutAdhesionIndividuelle(newBenef.getDateDebutAdhesionIndividuelle());
    benToEdit.setDernierMouvementRecu(newBenef.getDernierMouvementRecu());

    benToEdit.setAffiliation(newBenef.getAffiliation());
    benToEdit.setAdresses(newBenef.getAdresses());

    benToEdit.setAffiliationsRO(
        deepCopy(newBenef.getAffiliationsRO(), NirRattachementRODeclaration::new));
    benToEdit.setPeriodesMedecinTraitant(
        deepCopy(newBenef.getPeriodesMedecinTraitant(), PeriodeComparable::new));
    benToEdit.setTeletransmissions(
        deepCopy(newBenef.getTeletransmissions(), TeletransmissionDeclaration::new));
    benToEdit.setRegimesParticuliers(
        deepCopy(newBenef.getRegimesParticuliers(), CodePeriodeDeclaration::new));
    benToEdit.setSituationsParticulieres(
        deepCopy(newBenef.getSituationsParticulieres(), CodePeriodeDeclaration::new));

    // Step 5 - ADD/Update DomaineDroit

    Map<String, DomaineDroitContractTP> contratMapDomain = new HashMap<>();
    List<DomaineDroitContractTP> contratDomains = benToEdit.getDomaineDroits();
    for (DomaineDroitContractTP contratDom : contratDomains) {
      String contratCodeDom = contratDom.getCode();
      contratMapDomain.put(contratCodeDom, contratDom);
    }

    for (Map.Entry<String, Set<DomaineDroitForConsolidation>> entry : declMapDomain.entrySet()) {
      DomaineDroitContractTP contratDomToEdit = contratMapDomain.get(entry.getKey());
      if (contratDomToEdit != null) {
        contratDomains.remove(contratDomToEdit);
      }

      contratDomToEdit = domaineTPService.updateDomaineDroit(contratDomToEdit, entry.getValue());
      if (CollectionUtils.isNotEmpty(contratDomToEdit.getGaranties())) {
        contratDomains.add(contratDomToEdit);
      }
    }

    benToEdit.setDomaineDroits(contratDomains);
  }

  private static <T> List<T> deepCopy(List<T> list, Function<T, T> copyConstruct) {
    if (CollectionUtils.isEmpty(list)) return null;
    return list.stream().map(copyConstruct).collect(Collectors.toCollection(ArrayList::new));
  }
}
