package com.cegedim.next.serviceeligibility.core.mapper.carte;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ParametresDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.ParametreBddService;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartepapier.ConventionCartePapier;
import com.cegedim.next.serviceeligibility.core.model.domain.cartepapier.DomaineConventionCartePapier;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CartePapier;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.CartePapierEditique;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.DocumentType;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.EditingObject;
import com.cegedim.next.serviceeligibility.core.model.entity.card.cartepapiereditique.Internal;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MapperCartePapier {
  private final ParametreBddService parametreBddService;

  public CartePapierEditique mapCartePapierEditique(
      CarteDemat carteDemat,
      JsonNode insurerSettings,
      Declarant declarant,
      String contexte,
      Date today) {
    if (insurerSettings != null) {
      CartePapierEditique cartePapierEditique = new CartePapierEditique();
      DocumentType documentType = new DocumentType();
      documentType.setCode(
          Constants.CODE_CARTES_PAPIER); // by default because we manage only one documentType

      if (!insurerSettings.isEmpty()) {
        JsonNode defaultSetting = null;
        for (JsonNode jsonNode : insurerSettings) {
          if (Constants.DEFAUT.equals(jsonNode.get(Constants.SCOPE).asText())
              && jsonNode.has(Constants.COMMON)
              && Constants.CODE_CARTES_PAPIER.equals(
                  jsonNode.get(Constants.COMMON).get(Constants.DOCUMENT_TYPE).asText())) {
            defaultSetting = jsonNode;
            break;
          }
        }
        if (defaultSetting != null && defaultSetting.has(Constants.EDITING)) {
          documentType.setLabel(
              defaultSetting.get(Constants.EDITING).get(Constants.LABEL_DOCUMENT_TYPE).textValue());
          documentType.setCommunicationChannel(
              defaultSetting
                  .get(Constants.EDITING)
                  .get(Constants.COMMUNICATION_CHANNEL)
                  .textValue());
          documentType.setRecipientType(
              defaultSetting.get(Constants.EDITING).get(Constants.RECIPIENT_TYPE).textValue());
          documentType.setGedIndicator(
              Boolean.valueOf(
                  defaultSetting.get(Constants.EDITING).get(Constants.GED_INDICATOR).textValue()));
        }
      }

      EditingObject editingObject = new EditingObject();
      editingObject.setDocumentType(documentType);
      editingObject.setIdEditingObject(UUID.randomUUID().toString());
      editingObject.setStateObject(Constants.EMISSION); // by default

      cartePapierEditique.setEditingObject(editingObject);

      Internal internal = new Internal();
      internal.setStatus(Constants.TO_BE_ISSUED); // by default
      internal.setPortfolioCode(""); // optional
      internal.setMainOrganizationCode(""); // optional
      internal.setSecondaryOrganizationCode(""); // optional
      internal.setIdentifiant(carteDemat.getIdentifiant());

      cartePapierEditique.setInternal(internal);
      cartePapierEditique.setCartePapier(mapCartePapier(carteDemat, declarant, contexte, today));
      return cartePapierEditique;
    }
    return null;
  }

  public CartePapier mapCartePapier(
      CarteDemat carteDemat, Declarant declarant, String contexte, Date today) {
    CartePapier cartePapier = new CartePapier();
    cartePapier.setNumeroAMC(carteDemat.getIdDeclarant());
    cartePapier.setNomAMC(declarant.getNom());
    cartePapier.setLibelleAMC(declarant.getLibelle());
    cartePapier.setPeriodeDebut(carteDemat.getPeriodeDebut());
    cartePapier.setPeriodeFin(carteDemat.getPeriodeFin());
    cartePapier.setSocieteEmettrice(carteDemat.getContrat().getGestionnaire());
    cartePapier.setCodeRenvoi(carteDemat.getContrat().getCodeRenvoi());
    cartePapier.setLibelleRenvoi(carteDemat.getContrat().getLibelleCodeRenvoi());
    cartePapier.setCodeConvention(carteDemat.getContrat().getTypeConvention());
    ParametresDto parametresDto =
        parametreBddService.findOneByType(
            Constants.CONVENTIONNEMENT, cartePapier.getCodeConvention());
    if (parametresDto != null) {
      cartePapier.setLibelleConvention(parametresDto.getLibelle());
    }
    cartePapier.setFondCarte(carteDemat.getContrat().getFondCarte());
    cartePapier.setAnnexe1Carte(carteDemat.getContrat().getAnnexe1Carte());
    cartePapier.setAnnexe2Carte(carteDemat.getContrat().getAnnexe2Carte());
    cartePapier.setNumAMCEchange(carteDemat.getContrat().getNumAMCEchange());
    cartePapier.setNumOperateur(carteDemat.getContrat().getNumOperateur());
    String dateTraitement =
        DateUtils.formatDate(carteDemat.getDateConsolidation(), DateUtils.YYYY_MM_DD);
    cartePapier.setDateTraitement(
        dateTraitement != null ? dateTraitement : DateUtils.formatDate(today));
    cartePapier.setContrat(mapContratCartePapier(carteDemat.getContrat()));
    cartePapier.setAdresse(carteDemat.getAdresse());

    List<BenefCarteDemat> benefCartePapiers = new ArrayList<>();
    for (BenefCarteDemat benefCarteDemat : carteDemat.getBeneficiaires()) {
      benefCartePapiers.add(mapBenefCartePapier(benefCarteDemat));
    }

    List<DomaineConventionCartePapier> domaineConventionCartePapiers = new ArrayList<>();
    for (DomaineConvention domaineConvention : carteDemat.getDomainesConventions()) {
      domaineConventionCartePapiers.add(mapDomaineConventionCartePapier(domaineConvention));
    }
    cartePapier.setBeneficiaires(benefCartePapiers);
    domaineConventionCartePapiers.sort(
        Comparator.comparingInt(DomaineConventionCartePapier::getRang));
    cartePapier.setDomainesConventions(domaineConventionCartePapiers);
    cartePapier.setContexte(contexte);
    clean(cartePapier);
    return cartePapier;
  }

  private Contrat mapContratCartePapier(Contrat contrat) {
    Contrat contratCartePapier = new Contrat();
    contratCartePapier.setNumero(contrat.getNumero());
    contratCartePapier.setNumeroAdherent(contrat.getNumeroAdherent());
    contratCartePapier.setNumeroAdherentComplet(contrat.getNumeroAdherentComplet());
    contratCartePapier.setNomPorteur(contrat.getNomPorteur());
    contratCartePapier.setPrenomPorteur(contrat.getPrenomPorteur());
    contratCartePapier.setCivilitePorteur(contrat.getCivilitePorteur());
    contratCartePapier.setNumeroContratCollectif(contrat.getNumeroContratCollectif());
    contratCartePapier.setNumeroExterneContratIndividuel(
        contrat.getNumeroExterneContratIndividuel());
    contratCartePapier.setNumeroExterneContratCollectif(contrat.getNumeroExterneContratCollectif());
    contratCartePapier.setIsContratResponsable(contrat.getIsContratResponsable());
    contratCartePapier.setIsContratCMU(contrat.getIsContratCMU());
    contratCartePapier.setContratCMUC2S(contrat.getContratCMUC2S());
    contratCartePapier.setIndividuelOuCollectif(contrat.getIndividuelOuCollectif());
    contratCartePapier.setCategorieSociale(contrat.getCategorieSociale());
    contratCartePapier.setCritereSecondaireDetaille(contrat.getCritereSecondaireDetaille());
    contratCartePapier.setCritereSecondaire(contrat.getCritereSecondaire());
    contratCartePapier.setGestionnaire(contrat.getGestionnaire());
    contratCartePapier.setGroupeAssures(contrat.getGroupeAssures());
    contratCartePapier.setNumeroCarte(contrat.getNumeroCarte());
    contratCartePapier.setEditeurCarte(contrat.getEditeurCarte());
    contratCartePapier.setOrdrePriorisation(contrat.getOrdrePriorisation());
    contratCartePapier.setIdentifiantCollectivite(contrat.getIdentifiantCollectivite());
    contratCartePapier.setRaisonSociale(contrat.getRaisonSociale());
    contratCartePapier.setSiret(contrat.getSiret());
    contratCartePapier.setGroupePopulation(contrat.getGroupePopulation());
    contratCartePapier.setCodeItelis(contrat.getCodeItelis());

    return contratCartePapier;
  }

  public BenefCarteDemat mapBenefCartePapier(BenefCarteDemat benefCarteDemat) {
    BenefCarteDemat benefCartePapier = new BenefCarteDemat();

    LienContrat lienContrat = new LienContrat();
    lienContrat.setLienFamilial(benefCarteDemat.getLienContrat().getLienFamilial());
    lienContrat.setRangAdministratif(benefCarteDemat.getLienContrat().getRangAdministratif());

    Beneficiaire beneficiaire = mapBeneficiaireCartePapier(benefCarteDemat.getBeneficiaire());

    List<DomaineDroit> domaineDroitCartePapiers = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(benefCarteDemat.getDomainesRegroup())) {
      for (DomaineCarte domaineDroit : benefCarteDemat.getDomainesRegroup()) {
        domaineDroitCartePapiers.add(mapDomaineDroitCartePapier(domaineDroit));
      }
    } else if (CollectionUtils.isNotEmpty(benefCarteDemat.getDomainesCouverture())) {
      for (DomaineDroit domaineDroit : benefCarteDemat.getDomainesCouverture()) {
        domaineDroitCartePapiers.add(mapDomaineDroitCartePapier(domaineDroit));
      }
    }
    benefCartePapier.setLienContrat(lienContrat);
    benefCartePapier.setBeneficiaire(beneficiaire);
    domaineDroitCartePapiers.sort(Comparator.comparing(DomaineDroit::getNoOrdreDroit));
    benefCartePapier.setDomainesCouverture(domaineDroitCartePapiers);
    return benefCartePapier;
  }

  private Beneficiaire mapBeneficiaireCartePapier(Beneficiaire beneficiaire) {
    Beneficiaire beneficiaireCartePapier = new Beneficiaire();
    beneficiaireCartePapier.setDateNaissance(beneficiaire.getDateNaissance());
    beneficiaireCartePapier.setRangNaissance(beneficiaire.getRangNaissance());
    beneficiaireCartePapier.setNirBeneficiaire(beneficiaire.getNirBeneficiaire());
    beneficiaireCartePapier.setCleNirBeneficiaire(beneficiaire.getCleNirBeneficiaire());
    beneficiaireCartePapier.setNirOd1(beneficiaire.getNirOd1());
    beneficiaireCartePapier.setCleNirOd1(beneficiaire.getCleNirOd1());
    beneficiaireCartePapier.setNirOd2(beneficiaire.getNirOd2());
    beneficiaireCartePapier.setCleNirOd2(beneficiaire.getCleNirOd2());
    beneficiaireCartePapier.setNumeroPersonne(beneficiaire.getNumeroPersonne());
    beneficiaireCartePapier.setRefExternePersonne(beneficiaire.getRefExternePersonne());
    beneficiaireCartePapier.setDateAdhesionMutuelle(beneficiaire.getDateAdhesionMutuelle());
    beneficiaireCartePapier.setDateDebutAdhesionIndividuelle(
        beneficiaire.getDateDebutAdhesionIndividuelle());
    beneficiaireCartePapier.setNumeroAdhesionIndividuelle(
        beneficiaire.getNumeroAdhesionIndividuelle());
    beneficiaireCartePapier.setDateRadiation(beneficiaire.getDateRadiation());

    Affiliation affiliation = new Affiliation();
    affiliation.setNom(beneficiaire.getAffiliation().getNom());
    affiliation.setNomPatronymique(beneficiaire.getAffiliation().getNomPatronymique());
    affiliation.setNomMarital(beneficiaire.getAffiliation().getNomMarital());
    affiliation.setPrenom(beneficiaire.getAffiliation().getPrenom());
    affiliation.setCivilite(beneficiaire.getAffiliation().getCivilite());
    affiliation.setPeriodeDebut(beneficiaire.getAffiliation().getPeriodeDebut());
    affiliation.setPeriodeFin(beneficiaire.getAffiliation().getPeriodeFin());
    affiliation.setQualite(beneficiaire.getAffiliation().getQualite());
    affiliation.setRegimeParticulier(beneficiaire.getAffiliation().getRegimeParticulier());
    affiliation.setIsBeneficiaireACS(beneficiaire.getAffiliation().getIsBeneficiaireACS());
    affiliation.setIsTeleTransmission(beneficiaire.getAffiliation().getIsTeleTransmission());
    affiliation.setTypeAssure(beneficiaire.getAffiliation().getTypeAssure());
    beneficiaireCartePapier.setAffiliation(affiliation);
    return beneficiaireCartePapier;
  }

  private DomaineDroit mapDomaineDroitCartePapier(DomaineCarte domaineDroit) {
    DomaineDroit domaineDroitCartePapier = new DomaineDroit();
    domaineDroitCartePapier.setCode(domaineDroit.getCode());
    domaineDroitCartePapier.setCodeExterne(domaineDroit.getCodeExterne());
    domaineDroitCartePapier.setLibelle(domaineDroit.getLibelle());
    domaineDroitCartePapier.setCodeExterneProduit(domaineDroit.getCodeExterneProduit());
    domaineDroitCartePapier.setLibelleExterne(domaineDroit.getLibelleExterne());
    domaineDroitCartePapier.setCodeOptionMutualiste(domaineDroit.getCodeOptionMutualiste());
    domaineDroitCartePapier.setLibelleOptionMutualiste(domaineDroit.getLibelleOptionMutualiste());
    domaineDroitCartePapier.setCodeProduit(domaineDroit.getCodeProduit());
    domaineDroitCartePapier.setLibelleProduit(domaineDroit.getLibelleProduit());
    domaineDroitCartePapier.setCodeGarantie(domaineDroit.getCodeGarantie());
    domaineDroitCartePapier.setLibelleGarantie(domaineDroit.getLibelleGarantie());
    domaineDroitCartePapier.setCodeRenvoi(domaineDroit.getCodeRenvoi());
    domaineDroitCartePapier.setLibelleCodeRenvoi(domaineDroit.getLibelleCodeRenvoi());
    domaineDroitCartePapier.setTauxRemboursement(domaineDroit.getTaux());
    domaineDroitCartePapier.setUniteTauxRemboursement(domaineDroit.getUnite());
    domaineDroitCartePapier.setReferenceCouverture(domaineDroit.getReferenceCouverture());
    domaineDroitCartePapier.setNoOrdreDroit(domaineDroit.getNoOrdreDroit());
    domaineDroitCartePapier.setCategorie(domaineDroit.getCategorieDomaine());
    domaineDroitCartePapier.setFormulaMask(domaineDroit.getFormulaMask());
    domaineDroitCartePapier.setIsEditable(domaineDroit.getIsEditable());
    domaineDroitCartePapier.setCodeRenvoiAdditionnel(domaineDroit.getCodeRenvoiAdditionnel());
    domaineDroitCartePapier.setLibelleCodeRenvoiAdditionnel(
        domaineDroit.getLibelleCodeRenvoiAdditionnel());

    PrioriteDroit prioriteDroitCartePapier = new PrioriteDroit();
    prioriteDroitCartePapier.setCode(domaineDroit.getPrioriteDroits().getCode());
    prioriteDroitCartePapier.setLibelle(domaineDroit.getPrioriteDroits().getLibelle());
    prioriteDroitCartePapier.setTypeDroit(domaineDroit.getPrioriteDroits().getTypeDroit());
    prioriteDroitCartePapier.setPrioriteBO(domaineDroit.getPrioriteDroits().getPrioriteBO());

    List<Conventionnement> conventionnements = new ArrayList<>();
    for (Conventionnement conventionnement : domaineDroit.getConventionnements()) {
      conventionnements.add(new Conventionnement(conventionnement));
    }

    domaineDroitCartePapier.setPrioriteDroit(prioriteDroitCartePapier);
    domaineDroitCartePapier.setConventionnements(conventionnements);

    return domaineDroitCartePapier;
  }

  private DomaineDroit mapDomaineDroitCartePapier(DomaineDroit domaineDroit) {
    DomaineDroit domaineDroitCartePapier = new DomaineDroit();
    domaineDroitCartePapier.setCode(domaineDroit.getCode());
    domaineDroitCartePapier.setCodeExterne(domaineDroit.getCodeExterne());
    domaineDroitCartePapier.setLibelle(domaineDroit.getLibelle());
    domaineDroitCartePapier.setCodeExterneProduit(domaineDroit.getCodeExterneProduit());
    domaineDroitCartePapier.setLibelleExterne(domaineDroit.getLibelleExterne());
    domaineDroitCartePapier.setCodeOptionMutualiste(domaineDroit.getCodeOptionMutualiste());
    domaineDroitCartePapier.setLibelleOptionMutualiste(domaineDroit.getLibelleOptionMutualiste());
    domaineDroitCartePapier.setCodeProduit(domaineDroit.getCodeProduit());
    domaineDroitCartePapier.setLibelleProduit(domaineDroit.getLibelleProduit());
    domaineDroitCartePapier.setCodeGarantie(domaineDroit.getCodeGarantie());
    domaineDroitCartePapier.setLibelleGarantie(domaineDroit.getLibelleGarantie());
    domaineDroitCartePapier.setCodeRenvoi(domaineDroit.getCodeRenvoi());
    domaineDroitCartePapier.setLibelleCodeRenvoi(domaineDroit.getLibelleCodeRenvoi());
    domaineDroitCartePapier.setTauxRemboursement(domaineDroit.getTauxRemboursement());
    domaineDroitCartePapier.setUniteTauxRemboursement(domaineDroit.getUniteTauxRemboursement());
    domaineDroitCartePapier.setReferenceCouverture(domaineDroit.getReferenceCouverture());
    domaineDroitCartePapier.setNoOrdreDroit(domaineDroit.getNoOrdreDroit());
    domaineDroitCartePapier.setCategorie(domaineDroit.getCategorie());
    domaineDroitCartePapier.setFormulaMask(domaineDroit.getFormulaMask());
    domaineDroitCartePapier.setIsEditable(domaineDroit.getIsEditable());
    domaineDroitCartePapier.setCodeRenvoiAdditionnel(domaineDroit.getCodeRenvoiAdditionnel());
    domaineDroitCartePapier.setLibelleCodeRenvoiAdditionnel(
        domaineDroit.getLibelleCodeRenvoiAdditionnel());

    PrioriteDroit prioriteDroitCartePapier = new PrioriteDroit();
    prioriteDroitCartePapier.setCode(domaineDroit.getPrioriteDroit().getCode());
    prioriteDroitCartePapier.setLibelle(domaineDroit.getPrioriteDroit().getLibelle());
    prioriteDroitCartePapier.setTypeDroit(domaineDroit.getPrioriteDroit().getTypeDroit());
    prioriteDroitCartePapier.setPrioriteBO(domaineDroit.getPrioriteDroit().getPrioriteBO());

    List<Conventionnement> conventionnements = new ArrayList<>();
    for (Conventionnement conventionnement : domaineDroit.getConventionnements()) {
      conventionnements.add(new Conventionnement(conventionnement));
    }

    domaineDroitCartePapier.setPrioriteDroit(prioriteDroitCartePapier);
    domaineDroitCartePapier.setConventionnements(conventionnements);

    return domaineDroitCartePapier;
  }

  private DomaineConventionCartePapier mapDomaineConventionCartePapier(
      DomaineConvention domaineConvention) {
    DomaineConventionCartePapier domaineConventionCartePapier = new DomaineConventionCartePapier();
    domaineConventionCartePapier.setCode(domaineConvention.getCode());
    ParametresDto parametresDto =
        parametreBddService.findOneByType(Constants.DOMAINE, domaineConvention.getCode());
    if (parametresDto != null) {
      domaineConventionCartePapier.setLibelle(parametresDto.getLibelle());
    }
    domaineConventionCartePapier.setRang(domaineConvention.getRang());

    List<ConventionCartePapier> conventionCartePapiers = new ArrayList<>();
    for (Convention convention : domaineConvention.getConventions()) {
      ConventionCartePapier conventionCartePapier = new ConventionCartePapier();
      conventionCartePapier.setCode(convention.getCode());
      ParametresDto parametre =
          parametreBddService.findOneByType(Constants.CONVENTIONNEMENT, convention.getCode());
      if (parametre != null) {
        conventionCartePapier.setLibelle(parametre.getLibelle());
      }
      conventionCartePapier.setPriorite(convention.getPriorite());
      conventionCartePapiers.add(conventionCartePapier);
    }
    domaineConventionCartePapier.setConventions(conventionCartePapiers);
    return domaineConventionCartePapier;
  }

  private void clean(CartePapier cartePapier) {
    // Clean des infos du contrat
    cartePapier.getContrat().setDateSouscription(null);
    cartePapier.getContrat().setDateResiliation(null);
    cartePapier.getContrat().setType(null);
    cartePapier.getContrat().setQualification(null);
    cartePapier.getContrat().setRangAdministratif(null);
    cartePapier.getContrat().setDestinataire(null);
    cartePapier.getContrat().setSituationDebut(null);
    cartePapier.getContrat().setSituationFin(null);
    cartePapier.getContrat().setMotifFinSituation(null);
    cartePapier.getContrat().setLienFamilial(null);
    cartePapier.getContrat().setSituationParticuliere(null);
    cartePapier.getContrat().setTypeConvention(null);
    cartePapier.getContrat().setModePaiementPrestations(null);
    cartePapier.getContrat().setFondCarte(null);
    cartePapier.getContrat().setAnnexe1Carte(null);
    cartePapier.getContrat().setAnnexe2Carte(null);
    cartePapier.getContrat().setNumAMCEchange(null);
    cartePapier.getContrat().setNumOperateur(null);
    cartePapier.getContrat().setCodeRenvoi(null);
    cartePapier.getContrat().setLibelleCodeRenvoi(null);

    // Clean des infos des beneficiaires
    for (BenefCarteDemat benefToClean : cartePapier.getBeneficiaires()) {
      // Clean des infos du lienContrat
      benefToClean.getLienContrat().setModePaiementPrestations(null);
      // Clean des infos du beneficiaire
      benefToClean.getBeneficiaire().setIdClientBO(null);
      benefToClean.getBeneficiaire().setInsc(null);
      benefToClean.getBeneficiaire().getAffiliation().setRegimeOD1(null);
      benefToClean.getBeneficiaire().getAffiliation().setCaisseOD1(null);
      benefToClean.getBeneficiaire().getAffiliation().setCentreOD1(null);
      benefToClean.getBeneficiaire().getAffiliation().setRegimeOD2(null);
      benefToClean.getBeneficiaire().getAffiliation().setCaisseOD2(null);
      benefToClean.getBeneficiaire().getAffiliation().setCentreOD2(null);
      benefToClean.getBeneficiaire().getAffiliation().setHasMedecinTraitant(null);
      benefToClean.getBeneficiaire().setAdresses(null);
      // Clean des infos des domainesCouverture
      for (DomaineDroit domaineDroitToClean : benefToClean.getDomainesCouverture()) {
        // Clean du domaineDroit
        domaineDroitToClean.setCodeProfil(null);
        domaineDroitToClean.setOrigineDroits(null);
        domaineDroitToClean.setNaturePrestation(null);
        domaineDroitToClean.setNaturePrestationOnline(null);
        domaineDroitToClean.setCodeAssureurGarantie(null);
        domaineDroitToClean.setCodeOffre(null);
        domaineDroitToClean.setVersionOffre(null);
        domaineDroitToClean.setCodeOc(null);
        domaineDroitToClean.setCodeCarence(null);
        domaineDroitToClean.setCodeOrigine(null);
        domaineDroitToClean.setCodeAssureurOrigine(null);
        domaineDroitToClean.setPrestations(null);
        domaineDroitToClean.setPeriodeDroit(null);
        domaineDroitToClean.setPeriodeOnline(null);
        domaineDroitToClean.setPeriodeProductElement(null);
        domaineDroitToClean.setPeriodeCarence(null);
        domaineDroitToClean.setIsSuspension(null);
        domaineDroitToClean.setModeAssemblage(null);
        // Clean de prioriteDroit
        domaineDroitToClean.getPrioriteDroit().setNirPrio1(null);
        domaineDroitToClean.getPrioriteDroit().setNirPrio2(null);
        domaineDroitToClean.getPrioriteDroit().setPrioDroitNir1(null);
        domaineDroitToClean.getPrioriteDroit().setPrioDroitNir2(null);
        domaineDroitToClean.getPrioriteDroit().setPrioContratNir1(null);
        domaineDroitToClean.getPrioriteDroit().setPrioContratNir2(null);
      }
    }
  }
}
