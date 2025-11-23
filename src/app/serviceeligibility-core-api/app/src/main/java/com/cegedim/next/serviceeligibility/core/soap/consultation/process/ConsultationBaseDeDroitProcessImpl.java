package com.cegedim.next.serviceeligibility.core.soap.consultation.process;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.MapperDroitsConsult;
import com.cegedim.next.serviceeligibility.core.bdd.backend.service.DeclarantBackendService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionNumeroAdherentAbsent;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionPriorisationGaranties;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireInconnu;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceBeneficiaireNonEligible;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceCartePapier;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitNonOuvert;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.exception.ExceptionServiceDroitResilie;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheBeneficiaireService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeRechercheSegmentService;
import com.cegedim.next.serviceeligibility.core.business.declaration.service.DeclarationService;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.CodeReponse;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreIncorrect;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionConsultationParametreSegmentsManquent;
import com.cegedim.next.serviceeligibility.core.soap.consultation.exception.ExceptionMetier;
import com.cegedim.next.serviceeligibility.core.soap.consultation.mapper.MapperBaseDeDroitToWebService;
import com.cegedim.next.serviceeligibility.core.utility.Conversion;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedimassurances.norme.amc.TypeAmc;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddRequest;
import com.cegedimassurances.norme.base_de_droit.GetInfoBddResponse;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.TypeProfondeurRecherche;
import com.cegedimassurances.norme.base_de_droit.TypeInfoBdd.TypeRechercheBeneficiaire;
import com.cegedimassurances.norme.beneficiaire.TypeBeneficiaireDemandeur;
import com.cegedimassurances.norme.commun.TypeDates;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Processus de consultation des droits. */
@Component
public class ConsultationBaseDeDroitProcessImpl implements ConsultationBaseDeDroitProcess {

  private static final Logger LOG =
      LoggerFactory.getLogger(ConsultationBaseDeDroitProcessImpl.class);

  @Autowired private DeclarationService declarationService;

  @Autowired private MapperBaseDeDroitToWebService mapperBaseDeDroitToWebService;

  @Autowired private HttpServletRequest request;

  @Autowired private MapperDroitsConsult mapperDroitsConsult;

  @Autowired private DeclarantBackendService declarantService;

  @Override
  public void execute(final GetInfoBddRequest requete, final GetInfoBddResponse response) {

    List<DeclarationDto> declarationList;
    DemandeInfoBeneficiaire infoBenef = getDemandeInfoBenef(requete);
    boolean isDemandeBeneficiaire = false;
    try {
      final TypeRechercheBeneficiaire typeRechercheBenef =
          requete.getInfoBdd().getTypeRechercheBenef();
      final XMLGregorianCalendar dateFin = requete.getDates().getDateFin();
      final XMLGregorianCalendar dateDebut = requete.getDates().getDateDebut();
      if (StringUtils.isNotBlank(requete.getInfoBdd().getTypeGaranties())
          && !"0".equals(requete.getInfoBdd().getTypeGaranties())
          && !"1".equals(requete.getInfoBdd().getTypeGaranties())) {
        throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
      }
      final boolean limitWaranties = "1".equals(requete.getInfoBdd().getTypeGaranties());
      if (isConsultationVersion3()
          && dateFin != null
          && dateDebut != null
          && dateDebut.getYear() != dateFin.getYear()) {
        throw new ExceptionMetier(CodeReponse.ERREUR_DATE_ANNEE_CIVILE);
      }

      switch (typeRechercheBenef) {
        case BENEFICIAIRE:
          isDemandeBeneficiaire = true;
          isValidBeneficiaireRequest(infoBenef);
          declarationList = consultDroitBeneficiaire(infoBenef, limitWaranties);
          break;
        case CARTE_FAMILLE:
          isValidCarteFamilleRequest(infoBenef);
          declarationList = consultDroitsCarteTiersPayant(infoBenef);
          break;
        default:
          throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
      }

      // #BLUE-3518 Start
      declarationList.forEach(d -> mapperDroitsConsult.transformDomainsDto(d));
      // #BLUE-3518 End

      boolean mapperWithAdresse = isConsultationVersion4();
      for (DeclarationDto declarationDto : declarationList) {
        mapperBaseDeDroitToWebService.mapInfoBddResponse(
            response,
            declarationDto,
            isDemandeBeneficiaire,
            dateFin,
            isConsultationVersion3(),
            mapperWithAdresse);
      }
      response.setCodeReponse(mapperBaseDeDroitToWebService.createCodeReponseOK());

    } catch (ExceptionNumeroAdherentAbsent e) {
      LOG.debug(
          "Le n°adhérent est absent et celui-ci est indispensable pour identifier le bénéficiaire. Veuillez émettre une nouvelle demande avec le n°adhérent",
          e);
      throw new ExceptionMetier(CodeReponse.NUM_ADHERENT_ABSENT);
    } catch (ExceptionConsultationParametreIncorrect e) {
      LOG.debug("Paramètre recherche incorrect", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS);
    } catch (ExceptionConsultationParametreSegmentsManquent e) {
      LOG.debug("Paramètre recherche incorrect, segments manquants", e);
      throw new ExceptionMetier(CodeReponse.PARAM_RECHERCHE_INCORRECTS_SEGMENTS_MANQUENT);
    } catch (ExceptionServiceCartePapier e) {
      LOG.debug("Consulter carte papier", e);
      throw new ExceptionMetier(CodeReponse.CONSULT_CARTE_PAPIER);
    } catch (ExceptionServiceBeneficiaireInconnu e) {
      LOG.debug("Bénéficiaire inconnu", e);
      throw new ExceptionMetier(CodeReponse.BENEF_INCONNU);
    } catch (ExceptionPriorisationGaranties e) {
      LOG.debug("Les garanties du bénéficiaire ne sont pas correctement priorisées", e);
      throw new ExceptionMetier(CodeReponse.PRIORISATION_INCORRECTE);
    } catch (ExceptionServiceBeneficiaireNonEligible e) {
      LOG.debug("Bénéficiaire non éligible", e);
      throw new ExceptionMetier(CodeReponse.BENEF_NON_ELIGIBLE);
    } catch (ExceptionServiceDroitNonOuvert | ExceptionServiceDroitResilie e) {
      LOG.debug("Droits bénéficiaire non ouverts", e);
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
      Date benefRefDate = infoBenef.getDateReference();
      String formattedDate = "";
      if (benefRefDate != null) {
        formattedDate = dateFormat.format(benefRefDate);
      }
      throw new ExceptionMetier(CodeReponse.DROIT_BENEF_NON_OUVERT, formattedDate);
    }
  }

  /**
   * Methode permettant de renvoyer la liste de declaration correspondant à la demande de
   * l'utilisateur.
   *
   * @param infoBenef demande reçue par web serice
   * @return liste de DeclarationDto dont chaque declaration matche avec les critères de selection
   *     en paramètre
   */
  private List<DeclarationDto> consultDroitBeneficiaire(
      final DemandeInfoBeneficiaire infoBenef, boolean limitWaranties) {
    return declarationService.getDroitsBeneficiaire(
        infoBenef,
        isConsultationVersion2(),
        isConsultationVersion3(),
        isConsultationVersion4(),
        limitWaranties);
  }

  /**
   * Methode permettant de renvoyer la liste de declaration d'une carte tiers payant d'une
   * beneficiaire.
   *
   * @param infoBenef demande reçue par web serice
   * @return liste de DeclarationDto dont chaque declaration matche avec les critères de selection
   *     en paramètre
   */
  private List<DeclarationDto> consultDroitsCarteTiersPayant(DemandeInfoBeneficiaire infoBenef) {
    return declarationService.getDeclarationsCarteTiersPayant(
        infoBenef, isConsultationVersion2(), isConsultationVersion3());
  }

  /**
   * Renvoie une demande d'infos beneficiaire.
   *
   * @param requete La requete de la demande d'infos.
   * @return La demande d'infos beneficiaire.
   */
  public DemandeInfoBeneficiaire getDemandeInfoBenef(GetInfoBddRequest requete) {
    TypeInfoBdd infoBdd = requete.getInfoBdd();
    TypeDates date = requete.getDates();
    TypeBeneficiaireDemandeur beneficiaire = requete.getBeneficiaire();
    TypeAmc amc = requete.getAmc();

    String dateNaissance = beneficiaire.getDateNaissance();
    String rangNaissance = String.valueOf(beneficiaire.getRangGemellaire());
    String nirBeneficiaire = beneficiaire.getNIRCertifie();
    String cleNirBneficiare = beneficiaire.getCleNIR();
    String numeroPrefectoral = amc.getNumeroAMCPrefectoral();
    String numeroAdherent = amc.getAdherent();
    Date dateReference = getDateTimeReference(date.getDateReference());
    Date dateFin = date.getDateFin() == null ? null : getDateTimeReference(date.getDateFin());

    DemandeInfoBeneficiaire infoBenef = new DemandeInfoBeneficiaire();
    infoBenef.setTypeRechercheSegment(
        TypeRechercheSegmentService.valueOf((infoBdd.getTypeRechercheSegment().toString())));
    infoBenef.setSegmentRecherche(infoBdd.getTypeSegmentRecherche());
    if (infoBdd.getListeSegmentRecherche() != null
        && infoBdd.getListeSegmentRecherche().getSegmentRecherche() != null) {
      // BLUE-4837 - Transcode domaines TP pour consultationDroits V4 (SOAP + REST)
      infoBenef.setListeSegmentRecherche(
          declarantService.transcodeDomain(
              numeroPrefectoral, infoBdd.getListeSegmentRecherche().getSegmentRecherche()));
    }
    infoBenef.setDateReference(dateReference);
    infoBenef.setDateFin(dateFin);
    infoBenef.setCleNirBneficiare(cleNirBneficiare);
    infoBenef.setDateNaissance(dateNaissance);
    infoBenef.setNirBeneficiaire(nirBeneficiaire);
    infoBenef.setNumeroPrefectoral(numeroPrefectoral);
    infoBenef.setNumeroAdherent(numeroAdherent);
    infoBenef.setRangNaissance(rangNaissance);
    infoBenef.setProfondeurRecherche(
        getTypeProfondeurRechercheService(infoBdd.getTypeProfondeurRecherche()));
    infoBenef.setTypeRechercheBeneficiaire(
        TypeRechercheBeneficiaireService.valueOf(infoBdd.getTypeRechercheBenef().toString()));

    return infoBenef;
  }

  private TypeProfondeurRechercheService getTypeProfondeurRechercheService(
      final TypeProfondeurRecherche typeProfondeurRecherche) {
    for (TypeProfondeurRechercheService val : TypeProfondeurRechercheService.values()) {
      if (val.name().equals(typeProfondeurRecherche.name())) {
        return val;
      }
    }
    return null;
  }

  /**
   * Valide si la requete pour un appel pour un beneficiaire est valide dans son format
   *
   * @param infoBenef l'objet de l'appel
   * @throws ExceptionMetier exception lorsque les possibilités d'appels ne sont pas respectées
   */
  public void isValidBeneficiaireRequest(DemandeInfoBeneficiaire infoBenef) {

    // Lien type de recherche segment et présence des parametres
    if (!TypeRechercheSegmentService.MONO_SEGMENT.equals(infoBenef.getTypeRechercheSegment())
        && !TypeRechercheSegmentService.LISTE_SEGMENT.equals(infoBenef.getTypeRechercheSegment())) {
      throw new ExceptionConsultationParametreIncorrect();
    } else if (TypeRechercheSegmentService.LISTE_SEGMENT.equals(infoBenef.getTypeRechercheSegment())
        && (infoBenef.getListeSegmentRecherche() == null
            || infoBenef.getListeSegmentRecherche().isEmpty())) {
      throw new ExceptionConsultationParametreSegmentsManquent();
    } else if (TypeRechercheSegmentService.MONO_SEGMENT.equals(infoBenef.getTypeRechercheSegment())
        && StringUtils.isBlank(infoBenef.getSegmentRecherche())) {
      throw new ExceptionConsultationParametreIncorrect();
    }

    if (!TypeProfondeurRechercheService.AVEC_FORMULES.equals(infoBenef.getProfondeurRecherche())) {
      throw new ExceptionConsultationParametreIncorrect();
    }

    isInfoBenefValid(infoBenef);
  }

  /**
   * Valide si la requete pour un appel pour une carte famille est valide dans son format
   *
   * @param infoBenef l'objet de l'appel
   * @throws ExceptionMetier exception lorsque les possibilités d'appels ne sont pas respectées
   */
  public void isValidCarteFamilleRequest(DemandeInfoBeneficiaire infoBenef) {
    if (!TypeRechercheSegmentService.PAS_DE_RECHERCHE_SEGMENT.equals(
            infoBenef.getTypeRechercheSegment())
        && !TypeRechercheSegmentService.TOUT_SEGMENT.equals(infoBenef.getTypeRechercheSegment())) {
      throw new ExceptionConsultationParametreIncorrect();
    }

    if (!TypeProfondeurRechercheService.SANS_FORMULES.equals(infoBenef.getProfondeurRecherche())) {
      throw new ExceptionConsultationParametreIncorrect();
    }

    isInfoBenefValid(infoBenef);
  }

  /**
   * Test si les parametres de recherche obligatoire sont présents
   *
   * @param infoBenef l'objet de recherche
   * @throws ExceptionMetier exception parametre incorrect si certains des parametres obligatoires
   *     sont manquant
   */
  private void isInfoBenefValid(DemandeInfoBeneficiaire infoBenef) {
    if (StringUtils.isBlank(infoBenef.getNirBeneficiaire())
        || StringUtils.isBlank(infoBenef.getCleNirBneficiare())
        || StringUtils.isBlank(infoBenef.getDateNaissance())
        || StringUtils.isBlank(infoBenef.getNumeroPrefectoral())) {
      throw new ExceptionConsultationParametreIncorrect();
    }
  }

  /**
   * Trouve si on invoke la version 2 du web service consultation.
   *
   * @return true si on invoke la version 2 du web service consultation sinon false
   */
  private boolean isConsultationVersion2() {
    String url = getUrl();
    return url.contains("V2");
  }

  private boolean isConsultationVersion3() {
    String url = getUrl();
    return url.contains("V3");
  }

  /**
   * @return true si on invoque le web service de consultation version 4
   */
  private boolean isConsultationVersion4() {
    String url = getUrl().toLowerCase();
    return url.contains("v4");
  }

  private String getUrl() {
    return request.getRequestURL().toString();
  }

  /**
   * Methode permettant de renvoyer la liste de declaration correspondant à la demande de
   * l'utilisateur.
   *
   * @param dateReference date Reference de la requete
   * @return dateReference plus 23:59:59
   */
  private Date getDateTimeReference(XMLGregorianCalendar dateReference) {
    Date dateReferenceSansHeures = Conversion.xmlGregorianCalendarToDate(dateReference);
    if (dateReference == null) {
      return new Date();
    }
    SimpleDateFormat sdfSansHeures = new SimpleDateFormat(Constants.YYYY_MM_DD);
    SimpleDateFormat sdfAvecHeures = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String dateSansHeures = sdfSansHeures.format(dateReferenceSansHeures);
    dateSansHeures = dateSansHeures + " 23:59:59";

    Date dateAvecHeures = null;
    try {
      dateAvecHeures = sdfAvecHeures.parse(dateSansHeures);
    } catch (ParseException e) {
      // On sait que la dateSansHeures a un format correcte
    }

    return dateAvecHeures;
  }
}
