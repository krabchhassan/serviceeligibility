package com.cegedim.next.serviceeligibility.core.services.bdd;

import com.cegedim.next.serviceeligibility.core.dao.BeneficiaryDao;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.kafka.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.HistoriqueDateRangNaissance;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.ContratV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.BenefInfosService;
import com.cegedim.next.serviceeligibility.core.utils.BusinessSortUtility;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {
  private final Logger logger = LoggerFactory.getLogger(BeneficiaryService.class);
  public static final String SERVICE_PRESTATION = "ServicePrestation";

  private final MongoTemplate template;

  private final TraceService traceService;

  private final BeneficiaryDao beneficiaryDao;

  private final BenefInfosService benefInfos;

  @ContinueSpan(log = "process benef from declaration")
  public BenefAIV5 process(
      Declaration declaration, String traceId, boolean isFirst, Source source) {
    BenefAIV5 benefToSave = returnCreatedOrUpdatedBeneficiary(declaration, traceId, source);

    // Setting key for connectors
    String key =
        benefToSave.getAmc().getIdDeclarant() + "-" + benefToSave.getIdentite().getNumeroPersonne();

    benefToSave.setKey(key);
    try {
      return beneficiaryDao.save(benefToSave);
    } catch (org.springframework.dao.DuplicateKeyException e) {
      if (isFirst) {
        return process(declaration, traceId, false, source);
      }
    }
    return null;
  }

  @ContinueSpan(log = "returnCreatedOrUpdatedBeneficiary")
  public BenefAIV5 returnCreatedOrUpdatedBeneficiary(
      Declaration declaration, String traceId, Source source) {
    BenefAIV5 existingBenef = benefExists(declaration);
    if (existingBenef == null) {
      return createBeneficiaire(declaration, traceId);
    } else {
      return updateBeneficiaire(declaration, existingBenef, traceId, source);
    }
  }

  // Find beneficiaire based on criteria : return it
  private BenefAIV5 benefExists(Declaration declaration) {
    if (declaration == null) {
      return null;
    }

    BeneficiaireV2 beneficiaire = declaration.getBeneficiaire();
    Contrat decContrat = declaration.getContrat();
    String idDeclarant = declaration.getIdDeclarant();
    if (beneficiaire == null || decContrat == null) {
      String id = declaration.get_id();
      logger.error(
          "Beneficiary or contract null for Declaration with ID declarant {} and _id {}",
          idDeclarant,
          id);
      return null;
    } else {
      // CRITERIA
      Criteria criteria =
          Criteria.where("amc.idDeclarant")
              .is(idDeclarant)
              .and("identite.numeroPersonne")
              .is(beneficiaire.getNumeroPersonne());

      Query query = new Query(criteria);

      return template.findOne(query, BenefAIV5.class, Constants.BENEFICIAIRE_COLLECTION_NAME);
    }
  }

  private BenefAIV5 updateBeneficiaire(
      Declaration declaration, BenefAIV5 existingBenef, String traceId, Source source) {

    existingBenef.setTraceId(traceId);
    // services
    List<String> serviceList = existingBenef.getServices();
    if (serviceList == null) {
      serviceList = new ArrayList<>();
    }
    if (!serviceList.contains(Constants.SERVICE_TP)) {
      serviceList.add(Constants.SERVICE_TP);
    }
    existingBenef.setServices(serviceList);

    if (Source.AUTRE.equals(source)) {
      // Si la source est AUTRE on ne met pas à jour le benef mis à part les services
      traceService.updateStatus(
          traceId, TraceStatus.Deserialized, Constants.BENEFICIAIRE_COLLECTION_NAME);
      return existingBenef;
    }

    // contract
    ContratV5 contrat = getContractFromDeclaration(declaration);
    if (contrat != null) {
      updateContracts(existingBenef, contrat);
    }

    BeneficiaireV2 declBeneficiaire = declaration.getBeneficiaire();
    if (declBeneficiaire != null) {
      IdentiteContrat existingIdentity = existingBenef.getIdentite();
      IdentiteContrat newIdentity = createIdentityFromDeclaration(declBeneficiaire);

      // Changement de NIR Principal
      Nir newNir = newIdentity.getNir();
      Nir oldNir = existingIdentity.getNir();
      if (newNir != null
          && oldNir != null
          && newNir.getCode() != null
          && oldNir.getCode() != null
          && !newNir.getCode().equals(oldNir.getCode())) {
        newIdentity.setNir(newNir);
      }

      updateBirthRankDateAndAffiliation(newIdentity, existingIdentity, source);
      BusinessSortUtility.updateNirs(newIdentity, existingIdentity, source);
      existingBenef.setIdentite(newIdentity);
    }

    traceService.updateStatus(
        traceId, TraceStatus.Deserialized, Constants.BENEFICIAIRE_COLLECTION_NAME);
    return existingBenef;
  }

  @ContinueSpan(log = "updateBirthRankDateAndAffiliation")
  public static void updateBirthRankDateAndAffiliation(
      IdentiteContrat newIdentity, IdentiteContrat oldIdentity, Source source) {
    if (oldIdentity == null) {
      return;
    }

    List<HistoriqueDateRangNaissance> historiqueDateRangNaissances =
        Util.assign(oldIdentity.getHistoriqueDateRangNaissance(), new ArrayList<>());

    // Changement de date/rang de naissance
    String newBirthDate = newIdentity.getDateNaissance();
    String oldBirthDate = oldIdentity.getDateNaissance();
    String newBirthRank = newIdentity.getRangNaissance();
    String oldBirthRank = oldIdentity.getRangNaissance();

    // Si on n'avait pas d'historique OU si il y a un changement de date/rang
    // naissance...
    if (oldIdentity.getHistoriqueDateRangNaissance() == null
        || (!newBirthDate.equals(oldBirthDate) || !newBirthRank.equals(oldBirthRank))) {
      boolean hasHistorique =
          historiqueDateRangNaissances.stream()
              .anyMatch(
                  historique ->
                      historique.getDateNaissance().equals(newBirthDate)
                          && historique.getRangNaissance().equals(newBirthRank));
      if (!hasHistorique) {
        HistoriqueDateRangNaissance historiqueDateRangNaissance = new HistoriqueDateRangNaissance();
        historiqueDateRangNaissance.setDateNaissance(newBirthDate);
        historiqueDateRangNaissance.setRangNaissance(newBirthRank);
        historiqueDateRangNaissances.add(historiqueDateRangNaissance);
      }
      newIdentity.setHistoriqueDateRangNaissance(historiqueDateRangNaissances);
    }
    // Si aucun changement détecté, on reprend l'ancien historique
    else {
      newIdentity.setHistoriqueDateRangNaissance(oldIdentity.getHistoriqueDateRangNaissance());
    }

    // Changement d'affiliation
    BusinessSortUtility.setAffiliationsRO(newIdentity, oldIdentity, source);
  }

  private void updateContracts(BenefAIV5 existingBenef, ContratV5 contrat) {
    boolean found = false;
    if (existingBenef.getContrats() != null && !existingBenef.getContrats().isEmpty()) {
      for (int i = 0; i < existingBenef.getContrats().size(); i++) {
        ContratV5 existingContract = existingBenef.getContrats().get(i);

        if (existingContract != null) {
          String existingContractNumeroContrat = existingContract.getNumeroContrat();
          if (StringUtils.isNotEmpty(existingContractNumeroContrat)
              && existingContractNumeroContrat.equals(contrat.getNumeroContrat())) {
            existingBenef.getContrats().set(i, contrat);
            found = true;
            break;
          }
        }
      }
      if (!found) {
        existingBenef.getContrats().add(contrat);
      }
    } else {
      List<ContratV5> newContracts = new ArrayList<>();
      newContracts.add(contrat);
      existingBenef.setContrats(newContracts);
    }
  }

  private ContratV5 getContractFromDeclaration(Declaration dec) {
    ContratV5 contractInBenef = new ContratV5();
    if (dec.getContrat() != null) {
      Contrat declContract = dec.getContrat();
      contractInBenef.setCodeEtat(dec.getCodeEtat());
      contractInBenef.setNumeroContrat(declContract.getNumero());
      contractInBenef.setData(createDataAssure(dec.getBeneficiaire()));
      contractInBenef.setNumeroAdherent(declContract.getNumeroAdherent());
      contractInBenef.setSocieteEmettrice(declContract.getGestionnaire());
      contractInBenef.setNumeroAMCEchange(declContract.getNumAMCEchange());
    } else {
      return null;
    }
    return contractInBenef;
  }

  private BenefAIV5 createBeneficiaire(Declaration declaration, String traceId) {
    BenefAIV5 beneficiary = new BenefAIV5();
    beneficiary.setTraceId(traceId);

    // AMC
    Amc amc = new Amc();
    amc.setIdDeclarant(declaration.getIdDeclarant());
    beneficiary.setAmc(amc);

    // services
    List<String> serviceList = new ArrayList<>();
    serviceList.add(Constants.SERVICE_TP);
    beneficiary.setServices(serviceList);

    // contracts
    ContratV5 contract = getContractFromDeclaration(declaration);
    if (contract != null) {
      List<ContratV5> contracts = new ArrayList<>();
      contracts.add(contract);
      beneficiary.setContrats(contracts);
      beneficiary.setNumeroAdherent(declaration.getContrat().getNumeroAdherent());
    }
    // identite
    BeneficiaireV2 declBeneficiaire = declaration.getBeneficiaire();
    if (declBeneficiaire != null) {
      beneficiary.setIdentite(createIdentityFromDeclaration(declBeneficiaire));
    }

    // data is null from declaration
    // audit
    Audit audit = new Audit();
    audit.setDateEmission(DateUtils.generateDate());
    beneficiary.setAudit(audit);

    traceService.updateStatus(
        traceId, TraceStatus.Deserialized, Constants.BENEFICIAIRE_COLLECTION_NAME);

    return beneficiary;
  }

  private IdentiteContrat createIdentityFromDeclaration(BeneficiaireV2 declBeneficiaire) {
    IdentiteContrat identite = new IdentiteContrat();

    identite.setNumeroPersonne(declBeneficiaire.getNumeroPersonne());
    identite.setDateNaissance(declBeneficiaire.getDateNaissance());
    identite.setRangNaissance(declBeneficiaire.getRangNaissance());

    List<HistoriqueDateRangNaissance> historiqueDateRangNaissances = new ArrayList<>();
    HistoriqueDateRangNaissance historiqueDateRangNaissance = new HistoriqueDateRangNaissance();
    historiqueDateRangNaissance.setDateNaissance(declBeneficiaire.getDateNaissance());
    historiqueDateRangNaissance.setRangNaissance(declBeneficiaire.getRangNaissance());
    historiqueDateRangNaissances.add(historiqueDateRangNaissance);
    identite.setHistoriqueDateRangNaissance(historiqueDateRangNaissances);

    if (StringUtils.isNotEmpty(declBeneficiaire.getNirBeneficiaire())) {
      identite.setNir(
          new Nir(declBeneficiaire.getNirBeneficiaire(), declBeneficiaire.getCleNirBeneficiaire()));
    }
    List<NirRattachementRO> affiliationsRO = new ArrayList<>();
    if (StringUtils.isNotEmpty(declBeneficiaire.getNirOd1())) {
      Nir nir = new Nir();
      nir.setCle(declBeneficiaire.getCleNirOd1());
      nir.setCode(declBeneficiaire.getNirOd1());
      NirRattachementRO affiliationROOd1 = new NirRattachementRO();

      affiliationROOd1.setNir(nir);
      affiliationROOd1.setRattachementRO(
          new RattachementRO(
              declBeneficiaire.getAffiliation().getRegimeOD1(),
              declBeneficiaire.getAffiliation().getCaisseOD1(),
              declBeneficiaire.getAffiliation().getCentreOD1()));
      affiliationROOd1.setPeriode(
          new Periode(
              StringUtils.replace(declBeneficiaire.getAffiliation().getPeriodeDebut(), "/", "-"),
              StringUtils.replace(declBeneficiaire.getAffiliation().getPeriodeFin(), "/", "-")));
      affiliationsRO.add(affiliationROOd1);
    }
    if (StringUtils.isNotEmpty(declBeneficiaire.getNirOd2())) {
      Nir nir = new Nir();
      nir.setCle(declBeneficiaire.getCleNirOd2());
      nir.setCode(declBeneficiaire.getNirOd2());
      NirRattachementRO affiliationROOd2 = new NirRattachementRO();

      affiliationROOd2.setNir(nir);
      affiliationROOd2.setRattachementRO(
          new RattachementRO(
              declBeneficiaire.getAffiliation().getRegimeOD2(),
              declBeneficiaire.getAffiliation().getCaisseOD2(),
              declBeneficiaire.getAffiliation().getCentreOD2()));
      affiliationROOd2.setPeriode(
          new Periode(
              StringUtils.replace(declBeneficiaire.getAffiliation().getPeriodeDebut(), "/", "-"),
              StringUtils.replace(declBeneficiaire.getAffiliation().getPeriodeFin(), "/", "-")));
      affiliationsRO.add(affiliationROOd2);
    }
    identite.setAffiliationsRO(affiliationsRO);
    return identite;
  }

  private DataAssure createDataAssure(BeneficiaireV2 declBeneficiaire) {
    DataAssure dataAssure = new DataAssure();
    NomAssure nomAssure = createNomAssure(declBeneficiaire);
    dataAssure.setNom(nomAssure);

    AdresseAssure adresseAssure = new AdresseAssure();
    Contact contact = new Contact();
    List<AdresseAvecFixe> adresseList = declBeneficiaire.getAdresses();
    if (adresseList != null && !adresseList.isEmpty()) {
      for (Adresse adresse : adresseList) {
        if (adresse != null
            && adresse.getTypeAdresse() != null
            && "AD".equals(adresse.getTypeAdresse().getType())) {
          setAdresseAssure(adresseAssure, adresse);
          setContact(contact, adresse);
          break;
        }
      }
    }

    dataAssure.setAdresse(adresseAssure);
    dataAssure.setContact(contact);

    return dataAssure;
  }

  private void setContact(Contact contact, Adresse adresse) {
    contact.setEmail(adresse.getEmail());
    contact.setFixe(adresse.getTelephone());
    contact.setMobile("");
  }

  private void setAdresseAssure(AdresseAssure adresseAssure, Adresse adresse) {
    adresseAssure.setLigne1(adresse.getLigne1());
    adresseAssure.setLigne2(adresse.getLigne2());
    adresseAssure.setLigne3(adresse.getLigne3());
    adresseAssure.setLigne4(adresse.getLigne4());
    adresseAssure.setLigne5(adresse.getLigne5());
    adresseAssure.setLigne6(adresse.getLigne6());
    adresseAssure.setLigne7(adresse.getLigne7());
    adresseAssure.setCodePostal(adresse.getCodePostal());
  }

  private NomAssure createNomAssure(BeneficiaireV2 declBeneficiaire) {
    NomAssure nomAssure = new NomAssure();
    Affiliation affiliation = declBeneficiaire.getAffiliation();
    if (affiliation != null) {
      nomAssure.setCivilite(affiliation.getCivilite());
      nomAssure.setNomFamille(affiliation.getNom());
      nomAssure.setNomUsage(affiliation.getNomMarital());
      nomAssure.setPrenom(affiliation.getPrenom());
    }
    return nomAssure;
  }

  @ContinueSpan(log = "extractBenefFromContratV5")
  public List<BenefAIV5> extractBenefFromContrat(
      List<Assure> contractBenefs, ContratAIV6 contract, String keycloakUsername, String traceId) {
    List<BenefAIV5> benefs = new ArrayList<>();

    if (contractBenefs != null) {
      for (Assure contractBenef : contractBenefs) {
        extractAssure(contract, keycloakUsername, traceId, benefs, contractBenef);
      }
    }
    return benefs;
  }

  private void extractAssure(
      ContratAICommun contract,
      String keycloakUsername,
      String traceId,
      List<BenefAIV5> benefs,
      Assure contractBenef) {
    String declarantId = contract.getIdDeclarant();
    String numero = contract.getNumero();
    String numeroAdherent = contract.getNumeroAdherent();
    String societeEmettrice = contract.getSocieteEmettrice();

    BenefAIV5 benef = new BenefAIV5();
    DataAssure data = contractBenef.getData();
    IdentiteContrat contractid = contractBenef.getIdentite();

    List<Periode> periodesContrat =
        benefInfos.handlePeriodesContratForBenef(contract, contractBenef);

    extractedV5(
        keycloakUsername,
        traceId,
        declarantId,
        numero,
        numeroAdherent,
        benef,
        data,
        contractid,
        societeEmettrice,
        periodesContrat);

    benefs.add(benef);
  }

  private void extractedV5(
      String keycloakUsername,
      String traceId,
      String declarantId,
      String numero,
      String numeroAdherent,
      BenefAIV5 benef,
      DataAssure data,
      IdentiteContrat contractid,
      String societeEmettrice,
      List<Periode> periodesContrat) {
    Amc amc = new Amc();
    amc.setIdDeclarant(declarantId);
    benef.setAmc(amc);
    benef.setIdClientBO(keycloakUsername);
    Audit audit = new Audit();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
    audit.setDateEmission(LocalDateTime.now(ZoneOffset.UTC).format(formatter));
    benef.setAudit(audit);

    DataAssure newDataAssure = new DataAssure();
    newDataAssure.setNom(data.getNom());
    newDataAssure.setAdresse(data.getAdresse());
    newDataAssure.setContact(data.getContact());

    if (!CollectionUtils.isEmpty(data.getDestinatairesPaiements())) {
      newDataAssure.setDestinatairesPaiements(data.getDestinatairesPaiements());
    }

    if (!CollectionUtils.isEmpty(data.getDestinatairesRelevePrestations())) {
      newDataAssure.setDestinatairesRelevePrestations(data.getDestinatairesRelevePrestations());
    }

    ContratV5 benefContrat = new ContratV5();
    benefContrat.setNumeroContrat(numero);
    benefContrat.setData(newDataAssure);
    benefContrat.setNumeroAdherent(numeroAdherent);
    benefContrat.setSocieteEmettrice(societeEmettrice);
    benefContrat.setPeriodes(periodesContrat);

    List<ContratV5> contractsBenef = new ArrayList<>();
    contractsBenef.add(benefContrat);
    benef.setContrats(contractsBenef);

    IdentiteContrat identite = new IdentiteContrat();

    if (contractid != null) {
      identite.setDateNaissance(contractid.getDateNaissance());
      identite.setNir(contractid.getNir());
      identite.setAffiliationsRO(contractid.getAffiliationsRO());
      identite.setNumeroPersonne(contractid.getNumeroPersonne());
      identite.setRangNaissance(contractid.getRangNaissance());
    } else {
      // BLUE-3370 : on affecte l'identite de l'assure sinon le
      // benef n'aura aucune infomation
      identite = contractid;
    }

    benef.setSocietesEmettrices(
        benefInfos.handlePeriodesSocieteEmettriceForBenef(List.of(benefContrat)));
    benef.setIdentite(identite);
    benef.setNumeroAdherent(numeroAdherent);
    List<String> services = new ArrayList<>();
    services.add(SERVICE_PRESTATION);
    benef.setServices(services);
    benef.setTraceId(traceId);
  }

  /**
   * Calcul de la clé unique d'un bénéficiaire
   *
   * @param idDeclarant
   * @param identite
   * @return La clé
   */
  @ContinueSpan(log = "calculateKey from IdentiteContrat")
  public String calculateKey(String idDeclarant, IdentiteContrat identite) {
    String numeroPersonne = identite != null ? identite.getNumeroPersonne() : "";
    // Setting key for connectors
    return calculateKey(idDeclarant, numeroPersonne);
  }

  @ContinueSpan(log = "calculateKey with String")
  public String calculateKey(String idDeclarant, String numeroPersonne) {
    // Setting key for connectors
    return idDeclarant + "-" + numeroPersonne;
  }

  @ContinueSpan(log = "getBeneficiaryByKey")
  public BenefAIV5 getBeneficiaryByKey(final String key) {
    return beneficiaryDao.getBeneficiaryByKey(key);
  }

  @ContinueSpan(log = "getBeneficiaryByKey")
  public List<BenefAIV5> getBeneficiaries(
      final String idDeclarant,
      String numeroAdherant,
      String numeroContrat,
      String numeroPersonne) {
    return beneficiaryDao.getBeneficiaries(
        idDeclarant, numeroAdherant, numeroContrat, numeroPersonne);
  }

  @ContinueSpan(log = "getBeneficiariesByDateReference")
  public List<BenefAIV5> getBeneficiariesByDateReference(
      final String idDeclarant, String numeroAdherent, String numeroContrat, String dateReference) {
    return beneficiaryDao.getBeneficiariesByDateReference(
        idDeclarant, numeroAdherent, numeroContrat, dateReference);
  }

  @ContinueSpan(log = "deleteBeneficiaryById")
  public long deleteBeneficiaryById(final String id) {
    return beneficiaryDao.deleteBeneficiaryById(id);
  }

  @ContinueSpan(log = "deleteBeneficiariesByAmc")
  public long deleteBeneficiariesByAmc(final String idDeclarant) {
    return beneficiaryDao.deleteBeneficiariesByAmc(idDeclarant);
  }

  public Iterator<BenefAIV5> getBenefMultiOS() {
    return beneficiaryDao.getBenefMultiOS();
  }

  @ContinueSpan(log = "save")
  public BenefAIV5 save(BenefAIV5 beneficiary) {
    return beneficiaryDao.save(beneficiary);
  }
}
