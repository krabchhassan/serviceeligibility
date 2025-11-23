package com.cegedim.next.serviceeligibility.core.services.cartedemat.ws;

import static com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDematExceptionCode.*;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.dao.CardDao;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CardRequest;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse.*;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarteDematException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService {
  @Autowired private CardDao cardDao;

  @Autowired private DeclarantService declarantService;

  @Autowired private ParametreBddService parametreBddService;

  @Autowired private LimiteDomaineRestitutionService limiteDomaineRestitService;

  private static final Logger LOGGER = LoggerFactory.getLogger(CardService.class);

  @ContinueSpan(log = "build card response")
  public List<CardResponse> buildResponse(CardRequest request) {
    List<CardResponse> responseCards = new ArrayList<>();

    validateRequest(request);
    List<CarteDemat> cards = cardDao.findCartesDematFromRequest(request);

    // If we didn't find any card, raise an error
    if (CollectionUtils.isEmpty(cards)) {
      throw new CarteDematException(CARTE_DEMAT_NON_TROUVEE);
    }

    // Domain restitution limit
    limiteDomaineRestitService.limiteDomainListeCarteByParam(cards, request.getDateReference());

    // If we found any card, map and return them
    for (CarteDemat card : cards) {
      responseCards.add(mapCard(card));
    }

    validateCardsWithNumeroAdherent(cards, request);
    return responseCards;
  }

  @ContinueSpan(log = "validate cards with numero adherent")
  public void validateCardsWithNumeroAdherent(List<CarteDemat> cards, CardRequest request) {
    // Run this process only if numeroAdherent is empty
    if (StringUtils.isNotBlank(request.getNumeroAdherent())) {
      return;
    }

    // Cards list is not empty because we checked it before
    String firstNumeroAdherentFound = "";

    for (CarteDemat card : cards) {
      String cardNumeroAdherent = card.getContrat().getNumeroAdherent();

      // Reassign only once
      if (StringUtils.isBlank(firstNumeroAdherentFound)) {
        firstNumeroAdherentFound = cardNumeroAdherent;
      }

      // If one card's numeroAdherent is different from another, raise an error
      if (!firstNumeroAdherentFound.equals(cardNumeroAdherent)) {
        logAndThrow(
            new CarteDematException(
                ERREUR_MULTIPLES_ADHERENTS,
                "Précisez le numéro d'adhérent recherché dans la requête."));
      }
    }
  }

  @ContinueSpan(log = "validate card request")
  public void validateRequest(CardRequest request) throws CarteDematException {
    if (request == null) {
      logAndThrow(
          new CarteDematException(
              PARAM_RECHERCHE_INCORRECTS, "Le corps de la requête ne doit pas être vide."));
    } else { // request != null
      if (StringUtils.isBlank(request.getNumeroAmc())) {
        logAndThrow(
            new CarteDematException(
                PARAM_RECHERCHE_INCORRECTS, "Le champ 'numeroAmc' est obligatoire."));
      }

      if (StringUtils.isBlank(request.getDateReference())) {
        logAndThrow(
            new CarteDematException(
                PARAM_RECHERCHE_INCORRECTS, "Le champ 'dateReference' est obligatoire."));
      } else { // dateReference is not an empty String
        // Try to parse the date to validate format
        if (!DateUtils.isValidDate(request.getDateReference(), DateUtils.YYYY_MM_DD)) {
          logAndThrow(
              new CarteDematException(
                  PARAM_RECHERCHE_INCORRECTS,
                  "Le format du champ 'dateReference' doit être 'yyyy-MM-dd'."));
        }

        // Check that dateReference is greater or equal than now
        LocalDate dateReference =
            DateUtils.parseLocalDate(request.getDateReference(), DateUtils.YYYY_MM_DD);
        LocalDate now = LocalDate.now();

        if (dateReference == null || now.isAfter(dateReference)) {
          logAndThrow(
              new CarteDematException(
                  PARAM_RECHERCHE_INCORRECTS_DATE_REFERENCE,
                  "'dateReference' doit être supérieure à la date du jour."));
        }
      }

      if (StringUtils.isBlank(request.getNumeroContrat())) {
        logAndThrow(
            new CarteDematException(
                PARAM_RECHERCHE_INCORRECTS, "Le champ 'numeroContrat' est obligatoire."));
      }
    }
  }

  private void logAndThrow(CarteDematException e) throws CarteDematException {
    // No stacktrace because it only catches functional errors
    LOGGER.error("Error during card request validation : {}", e.getCommentaire());
    throw e;
  }

  @ContinueSpan(log = "map card")
  public CardResponse mapCard(CarteDemat card) {
    CardResponse cardResponse = new CardResponse();

    cardResponse.setNumeroAMC(card.getIdDeclarant());
    Declarant declarant = declarantService.findById(card.getIdDeclarant());
    if (declarant != null) {
      cardResponse.setNomAMC(declarant.getNom());
      cardResponse.setLibelleAMC(declarant.getLibelle());
    }
    cardResponse.setPeriodeDebut(card.getPeriodeDebut());
    cardResponse.setPeriodeFin(card.getPeriodeFin());

    Contrat contratCarteDemat = card.getContrat();
    if (contratCarteDemat != null) {
      cardResponse.setSocieteEmettrice(contratCarteDemat.getGestionnaire());

      cardResponse.setCodeConvention(contratCarteDemat.getTypeConvention());
      ParametresDto parametresDto =
          parametreBddService.findOneByType(
              Constants.CONVENTIONNEMENT, cardResponse.getCodeConvention());
      if (parametresDto != null) {
        cardResponse.setLibelleConvention(parametresDto.getLibelle());
      }

      cardResponse.setCodeRenvoi(contratCarteDemat.getCodeRenvoi());
      cardResponse.setLibelleRenvoi(contratCarteDemat.getLibelleCodeRenvoi());
      cardResponse.setFondCarte(contratCarteDemat.getFondCarte());
      cardResponse.setAnnexe1Carte(contratCarteDemat.getAnnexe1Carte());
      cardResponse.setAnnexe2Carte(contratCarteDemat.getAnnexe2Carte());
      cardResponse.setCodeItelis(contratCarteDemat.getCodeItelis());
      cardResponse.setNumeroAMCEchange(contratCarteDemat.getNumAMCEchange());
    }
    cardResponse.setContrat(mapContrat(card));
    cardResponse.setAdresseContrat(card.getAdresse());
    cardResponse.setDomaines(mapDomains(card.getDomainesConventions()));
    cardResponse.setBeneficiaires(mapBeneficiaries(card.getBeneficiaires()));

    return cardResponse;
  }

  @ContinueSpan(log = "map contrat carte")
  public CardResponseContrat mapContrat(CarteDemat card) {
    CardResponseContrat contrat = new CardResponseContrat();
    Contrat contratCarteDemat = card.getContrat();
    if (contratCarteDemat != null) {
      contrat.setNumero(contratCarteDemat.getNumero());
      contrat.setNumeroAdherent(contratCarteDemat.getNumeroAdherent());
      contrat.setNumeroAdherentComplet(contratCarteDemat.getNumeroAdherentComplet());
      contrat.setNomPorteur(contratCarteDemat.getNomPorteur());
      contrat.setPrenomPorteur(contratCarteDemat.getPrenomPorteur());
      contrat.setCivilitePorteur(contratCarteDemat.getCivilitePorteur());
      contrat.setNumeroContratCollectif(contratCarteDemat.getNumeroContratCollectif());
      contrat.setNumeroExterneContratIndividuel(
          contratCarteDemat.getNumeroExterneContratIndividuel());
      contrat.setNumeroExterneContratCollectif(
          contratCarteDemat.getNumeroExterneContratCollectif());
      contrat.setIsContratResponsable(contratCarteDemat.getIsContratResponsable());
      contrat.setIsContratCMU(contratCarteDemat.getIsContratCMU());
      contrat.setContratCMUC2S(contratCarteDemat.getContratCMUC2S());
      contrat.setIndividuelOuCollectif(contratCarteDemat.getIndividuelOuCollectif());
      contrat.setCritereSecondaire(contratCarteDemat.getCritereSecondaire());
      contrat.setCritereSecondaireDetaille(contratCarteDemat.getCritereSecondaireDetaille());
      contrat.setGestionnaire(contratCarteDemat.getGestionnaire());
      contrat.setGroupeAssures(contratCarteDemat.getGroupeAssures());
      contrat.setNumeroCarte(contratCarteDemat.getNumeroCarte());
      contrat.setEditeurCarte(contratCarteDemat.getEditeurCarte());
      contrat.setOrdrePriorisation(contratCarteDemat.getOrdrePriorisation());
      contrat.setIdentifiantCollectivite(contratCarteDemat.getIdentifiantCollectivite());
      contrat.setRaisonSociale(contratCarteDemat.getRaisonSociale());
      contrat.setSiret(contratCarteDemat.getSiret());
      contrat.setGroupePopulation(contratCarteDemat.getGroupePopulation());
    }

    return contrat;
  }

  @ContinueSpan(log = "map domaines contrat carte")
  public List<CardResponseDomain> mapDomains(List<DomaineConvention> domainesCarte) {
    List<CardResponseDomain> responseDomains = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(domainesCarte)) {
      domainesCarte.sort(Comparator.comparing(DomaineConvention::getRang));
      for (DomaineConvention domaine : domainesCarte) {
        CardResponseDomain mappedDomain = new CardResponseDomain();

        mappedDomain.setCode(domaine.getCode());
        ParametresDto parametresDto =
            parametreBddService.findOneByType(Constants.DOMAINE, domaine.getCode());
        if (parametresDto != null) {
          mappedDomain.setLibelleDomaine(parametresDto.getLibelle());
        }
        mappedDomain.setRang(domaine.getRang());
        mappedDomain.setConventions(mapConventions(domaine));

        responseDomains.add(mappedDomain);
      }
    }

    return responseDomains;
  }

  private List<CardResponseConvention> mapConventions(DomaineConvention domaine) {
    List<CardResponseConvention> conventions = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(domaine.getConventions())) {
      for (Convention convention : domaine.getConventions()) {
        CardResponseConvention mappedConvention = new CardResponseConvention();

        mappedConvention.setCodeConvention(convention.getCode());
        ParametresDto parametre =
            parametreBddService.findOneByType(Constants.CONVENTIONNEMENT, convention.getCode());
        if (parametre != null) {
          mappedConvention.setLibelleConvention(parametre.getLibelle());
        }
        mappedConvention.setPrioriteConvention(convention.getPriorite());

        conventions.add(mappedConvention);
      }
    }

    return conventions;
  }

  @ContinueSpan(log = "map beneficiaires carte")
  public List<CardResponseBeneficiary> mapBeneficiaries(List<BenefCarteDemat> beneficiairesCarte) {
    List<CardResponseBeneficiary> beneficiaries = new ArrayList<>();

    if (CollectionUtils.isNotEmpty(beneficiairesCarte)) {
      for (BenefCarteDemat benefCarte : beneficiairesCarte) {
        CardResponseBeneficiary beneficiary = new CardResponseBeneficiary();

        // Map beneficiary
        beneficiary = mapBeneficiariesCore(benefCarte, beneficiary);

        // Map couvertures
        mapBeneficiariesDomains(benefCarte, beneficiary);

        beneficiaries.add(beneficiary);
      }
    }

    return beneficiaries;
  }

  @ContinueSpan(log = "map beneficiaires carte - core")
  public CardResponseBeneficiary mapBeneficiariesCore(
      BenefCarteDemat benefCarte, CardResponseBeneficiary beneficiary) {
    Beneficiaire benefCarteBenef = benefCarte.getBeneficiaire();
    LienContrat lienContrat = benefCarte.getLienContrat();

    if (benefCarteBenef != null) {
      Affiliation affiliation = benefCarteBenef.getAffiliation();

      if (affiliation != null) {
        beneficiary.setNomBeneficiaire(affiliation.getNom());
        beneficiary.setNomPatronymique(affiliation.getNomPatronymique());
        beneficiary.setNomMarital(affiliation.getNomMarital());
        beneficiary.setPrenom(affiliation.getPrenom());
        beneficiary.setQualite(affiliation.getQualite());
        beneficiary.setTypeAssure(affiliation.getTypeAssure());

        beneficiary.setRegimeParticulier(affiliation.getRegimeParticulier());
        beneficiary.setBeneficiaireACS(affiliation.getIsBeneficiaireACS());
        beneficiary.setTeleTransmission(affiliation.getIsTeleTransmission());
        beneficiary.setDebutAffiliation(affiliation.getPeriodeDebut());
      }

      beneficiary.setNirOd1(benefCarteBenef.getNirOd1());
      beneficiary.setCleNirOd1(benefCarteBenef.getCleNirOd1());
      beneficiary.setNirOd2(benefCarteBenef.getNirOd2());
      beneficiary.setCleNirOd2(benefCarteBenef.getCleNirOd2());
      beneficiary.setNirBeneficiaire(benefCarteBenef.getNirBeneficiaire());
      beneficiary.setCleNirBeneficiaire(benefCarteBenef.getCleNirBeneficiaire());
      beneficiary.setDateNaissance(benefCarteBenef.getDateNaissance());
      beneficiary.setRangNaissance(benefCarteBenef.getRangNaissance());
      beneficiary.setNumeroPersonne(benefCarteBenef.getNumeroPersonne());
      beneficiary.setRefExternePersonne(benefCarteBenef.getRefExternePersonne());
    }

    if (lienContrat != null) {
      beneficiary.setLienFamilial(lienContrat.getLienFamilial());
      beneficiary.setRangAdministratif(lienContrat.getRangAdministratif());
    }

    return beneficiary;
  }

  @ContinueSpan(log = "map beneficiaires carte - domains")
  public void mapBeneficiariesDomains(
      BenefCarteDemat benefCarte, CardResponseBeneficiary beneficiary) {
    List<CardResponseCouverture> couvertures = new ArrayList<>();
    if (!CollectionUtils.isEmpty(benefCarte.getDomainesRegroup())) {
      for (DomaineCarte domaine : benefCarte.getDomainesRegroup()) {
        CardResponseCouverture mappedDomain = new CardResponseCouverture();

        ParametresDto parametresDto =
            parametreBddService.findOneByType(Constants.DOMAINE, domaine.getCode());
        if (parametresDto != null) {
          mappedDomain.setLibelleDomaine(parametresDto.getLibelle());
        }
        mapDomaineCarte(mappedDomain, domaine);
        couvertures.add(mappedDomain);
      }
    } else if (!CollectionUtils.isEmpty(benefCarte.getDomainesCouverture())) {
      for (DomaineDroit domaine : benefCarte.getDomainesCouverture()) {
        CardResponseCouverture mappedDomain = new CardResponseCouverture();

        ParametresDto parametresDto =
            parametreBddService.findOneByType(Constants.DOMAINE, domaine.getCode());
        if (parametresDto != null) {
          mappedDomain.setLibelleDomaine(parametresDto.getLibelle());
        }
        mapDomaineDroit(mappedDomain, domaine);
        couvertures.add(mappedDomain);
      }
    }
    beneficiary.setCouvertures(couvertures);
  }

  @ContinueSpan(log = "map beneficiaires carte - priorités droits")
  public CardResponsePrioriteDroit mapPrioriteDroits(PrioriteDroit prioriteDroit) {
    CardResponsePrioriteDroit prioriteDroitCartePapier = new CardResponsePrioriteDroit();
    prioriteDroitCartePapier.setCode(prioriteDroit.getCode());
    prioriteDroitCartePapier.setLibelle(prioriteDroit.getLibelle());
    prioriteDroitCartePapier.setTypeDroit(prioriteDroit.getTypeDroit());
    prioriteDroitCartePapier.setPrioriteBO(prioriteDroit.getPrioriteBO());
    return prioriteDroitCartePapier;
  }

  private void mapDomaineCarte(CardResponseCouverture mappedDomain, DomaineCarte domaine) {
    mappedDomain.setCodeDomaine(domaine.getCode());
    mappedDomain.setCodeExterneProduit(domaine.getCodeExterneProduit());
    mappedDomain.setCodeOptionMutualiste(domaine.getCodeOptionMutualiste());
    mappedDomain.setLibelleOptionMutualiste(domaine.getLibelleOptionMutualiste());
    mappedDomain.setCodeProduit(domaine.getCodeProduit());
    mappedDomain.setLibelleProduit(domaine.getLibelleProduit());
    mappedDomain.setCodeGarantie(domaine.getCodeGarantie());
    mappedDomain.setLibelleGarantie(domaine.getLibelleGarantie());
    mappedDomain.setCodeRenvoi(domaine.getCodeRenvoi());
    mappedDomain.setLibelleCodeRenvoi(domaine.getLibelleCodeRenvoi());
    mappedDomain.setCodeRenvoiAdditionnel(domaine.getCodeRenvoiAdditionnel());
    mappedDomain.setLibelleCodeRenvoiAdditionnel(domaine.getLibelleCodeRenvoiAdditionnel());
    mappedDomain.setTauxRemboursement(domaine.getTaux());
    mappedDomain.setUniteTauxRemboursement(domaine.getUnite());
    mappedDomain.setCategorieDomaine(domaine.getCategorieDomaine());
    mappedDomain.setPeriodeDebut(domaine.getPeriodeDebut());
    mappedDomain.setPeriodeFin(domaine.getPeriodeFin());
    if (domaine.getPrioriteDroits() != null) {
      mappedDomain.setPrioriteDroits(mapPrioriteDroits(domaine.getPrioriteDroits()));
    }
  }

  private void mapDomaineDroit(CardResponseCouverture mappedDomain, DomaineDroit domaine) {
    mappedDomain.setCodeDomaine(domaine.getCode());
    mappedDomain.setCodeExterneProduit(domaine.getCodeExterneProduit());
    mappedDomain.setCodeOptionMutualiste(domaine.getCodeOptionMutualiste());
    mappedDomain.setLibelleOptionMutualiste(domaine.getLibelleOptionMutualiste());
    mappedDomain.setCodeProduit(domaine.getCodeProduit());
    mappedDomain.setLibelleProduit(domaine.getLibelleProduit());
    mappedDomain.setCodeGarantie(domaine.getCodeGarantie());
    mappedDomain.setLibelleGarantie(domaine.getLibelleGarantie());
    mappedDomain.setCodeRenvoi(domaine.getCodeRenvoi());
    mappedDomain.setLibelleCodeRenvoi(domaine.getLibelleCodeRenvoi());
    mappedDomain.setCodeRenvoiAdditionnel(domaine.getCodeRenvoiAdditionnel());
    mappedDomain.setLibelleCodeRenvoiAdditionnel(domaine.getLibelleCodeRenvoiAdditionnel());
    mappedDomain.setTauxRemboursement(domaine.getTauxRemboursement());
    mappedDomain.setUniteTauxRemboursement(domaine.getUniteTauxRemboursement());
    mappedDomain.setCategorieDomaine(domaine.getCategorie());
    if (domaine.getPeriodeDroit() != null) {
      mappedDomain.setPeriodeDebut(domaine.getPeriodeDroit().getPeriodeDebut());
      mappedDomain.setPeriodeFin(domaine.getPeriodeDroit().getPeriodeFin());
    }
    if (domaine.getPrioriteDroit() != null) {
      mappedDomain.setPrioriteDroits(mapPrioriteDroits(domaine.getPrioriteDroit()));
    }
  }
}
