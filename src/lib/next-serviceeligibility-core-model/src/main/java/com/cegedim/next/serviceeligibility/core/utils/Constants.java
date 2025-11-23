package com.cegedim.next.serviceeligibility.core.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {
  public static final String ACTION_A_REALISER = "actionARealiser";
  public static final String AIGUILLAGE = "AIGUILLAGE";
  public static final String AMC = "amc";
  public static final String AMC_CONTRAT = "AMC_contrat";
  public static final String ASSURES = "assures";
  public static final String ASSURE = "assure";
  public static final String CONTRAT = "contrat";
  public static final String INFOS_CARTE_TP = "infosCarteTP";

  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String BENEF_IN_DECLARATION = "beneficiaire";
  public static final String BENEF_TRACE = "beneficiaryTrace";

  public static final String SERVICE_PRESTATION = "ServicePrestation";

  public static final String TRACE_CONSOLIDATION_COLLECTION = "tracesConsolidation";

  public static final String TRACE_EXTRA_CONSO_COLLECTION = "tracesExtractionConso";

  public static final String DECLARANTS_COLLECTION = "declarants";

  public static final String PRESTIJ_TRACE = "servicePrestIJTrace";
  public static final String PRESTIJ = "servicePrestIJ";

  public static final String SERVICE_TP = "Service_TP";

  public static final String CONTRACT_TRACE = "servicePrestationTrace";
  public static final String PERSON_TRACE = "personTrace";

  public static final String TRIGGER_COLLECTION = "trigger";
  public static final String TRIGGERED_BENEFICIARY_COLLECTION = "triggeredBeneficiary";
  public static final String PARAMETRAGE_CARTETP_COLLECTION = "parametragesCarteTP";

  public static final String SAS_CONTRAT_COLLECTION = "sasContrat";

  public static final String CONTRACT_ELEMENT_COLLECTION = "contractElement";

  public static final String LOT_COLLECTION = "lot";

  public static final String TRACESFLUX_COLLECTION = "tracesFlux";

  public static final String BIRTH_DATE_FORMAT = "yyyyMMdd";
  public static final String CODE = "code";
  public static final String CODE_OC = "code";
  public static final String CODE_OFFER = "codeOffer";
  public static final String CODE_OFFRE = "code";
  public static final String CODE_PRODUIT = "code";

  public static final String COLLECTIVITE = "collectivite";

  public static final String IDENTIFIANT_COLLECTIVITE = "identifiantCollectivite";

  public static final String GROUPE_POPULATION = "groupePopulation";
  public static final String COLLEGE = "college";
  public static final String CONTEXTE_TIERS_PAYANT = "contexteTiersPayant";
  public static final String CONTRATS_COLLECTION_NAME = "contratsTP";

  public static final String BENEFICIAIRE_COLLECTION_NAME = "beneficiaires";
  public static final String CONTROLES_SERVICE_PRESTATION = "controles_service_prestation";
  public static final String CONVENTIONNEMENT = "conventionnement";
  public static final String CRITERE_SECONDAIRE_DETAILLE = "critereSecondaireDetaille";
  public static final String EMPTY_DATE = "-";
  public static final String DATE_CREATION = "dateCreation";
  public static final String DATE_DEBUT_TRAITEMENT = "dateDebutTraitement";
  public static final String DATE_DEBUT_VALIDITE = "dateDebutValidite";

  public static final String DATE_EXECUTION_BATCH = "dateExecutionBatch";

  public static final String DATE_DECLENCHEMENT_MANUEL_COURANT = "dateDeclenchementManuelCourant";
  public static final String DATE_EFFET = "dateEffet";
  public static final String DATE_FIN_STANDBY = "dateFinStandBy";
  public static final String DATE_FIN_TRAITEMENT = "dateFinTraitement";
  public static final String DATE_MODIFICATION = "dateModification";
  public static final String DATE_NAISSANCE = "dateNaissance";
  public static final String DATE_RENOUVELLEMENT_CARTE_TP = "dateRenouvellementCarteTP";
  public static final String DATE_SOUSCRIPTION = "dateSouscription";
  public static final String DEBUT_ECHEANCE = "debutEcheance";
  public static final String DEBUT_ECHEANCE_COURANT = "debutEcheanceCourant";
  public static final String DEBUT_ECHEANCE_COURANT_DATE = "debutEcheanceCourantDate";
  public static final String DEBUT_ECHEANCE_MATCH = "debutEcheanceMatch";
  public static final String DEBUT_ECHEANCE_MATCH_STRING = "debutEcheanceMatchString";
  public static final String DEBUT_VALIDITE_MATCH_STRING = "debutValiditeMatchString";
  public static final String DECLARATION_COLLECTION = "declarations";
  public static final String DECLARATION_TRACE = "declarationTrace";
  public static final String DECLARATION_LIGHT_COLLECTION = "tmpPurgeDeclarations";
  public static final String DECLARATION_NUMERO_CONTRAT = "contrat.numero";
  public static final String DELAI_DECLENCHEMENT_CARTE_TP = "delaiDeclenchementCarteTP";
  public static final String DELETE_ENDPOINT = "deleteEndpoint";
  public static final String DERNIERE_ANOMALIE = "derniereAnomalie";
  public static final String DERNIER_MOTIF_ANOMALIE = "dernierMotifAnomalie";
  public static final String SAS_TROUVE = "Sas trouvé pour ce contrat";
  public static final String DESC = "DESC";
  public static final String DOLLAR_S = "$%s";
  public static final String DOLLAR_S_S = "$%s.%s";
  public static final String DS_S = "$%s.%s";

  public static final String CODE_ETAT = "codeEtat";
  public static final String EFFET_DEBUT = "effetDebut";
  public static final String ENV_EXTERNE = "external";
  public static final String ENV_INTERNE = "internal";
  public static final String FILE_FLOW_METADATA_COLLECTION = "fileFlowMetadata";
  public static final String FORMAT_DATE_MONGO = "%Y-%m-%d";
  public static final String HISTORIQUE_EXECUTIONS_COLLECTION = "historiqueExecutions";
  public static final String CIRCUITS_COLLECTION = "circuits";
  public static final String TRANSCODAGE_COLLECTION = "transcodage";

  public static final String ID = "_id";
  public static final String ID_DECLARANT = "idDeclarant";
  public static final String ID_DECLARATION = "idDeclarations";
  public static final String ID_OFFRE = "id";
  public static final String ID_PRODUIT = "id";
  public static final String ID_TRIGGER = "idTrigger";
  public static final String IGNORED = "ignored";
  public static final String NIR_BENEF = "nirBeneficiaire";
  public static final String COUNT = "count";

  public static final String ACTIVE = "ACTIVE";
  public static final String IS_CONTRAT_INDIVIDUEL = "isContratIndividuel";
  public static final String KAFKA_DEMANDE_DECLARATION_HEADER_ID_TRIGGER = "idTrigger";
  public static final String KAFKA_DEMANDE_DECLARATION_HEADER_RECYCLAGE = "recyclage";

  public static final String KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER = "updateTrigger";
  public static final String KAFKA_RETENTION_HEADER_MAINORGANIZATIONCODE = "mainorganization";
  public static final String KAFKA_RETENTION_HEADER_SECONDARYORGANIZATIONCODE =
      "secondaryorganization";

  public static final String KAFKA_DEMANDE_DECLARATION_HEADER_NO_UPDATE = "0";

  public static final String KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_TRIGGER_INPROGRESS = "1";

  public static final String KAFKA_DEMANDE_DECLARATION_HEADER_UPDATE_LAST_TRIGGERBENEF = "2";

  public static final String LAST_ACTION = "lastAction";
  public static final String LIBELLE = "libelle";
  public static final String DOMAINE = "domaine";
  public static final String LIBELLE_OC = "label";
  public static final String LIBELLE_OFFRE = "label";
  public static final String LIBELLE_PRODUIT = "label";
  public static final String LISTE_VALEURS = "listeValeurs";
  public static final int MILLISECONDS_FOR_ONE_DAY = 86400000;
  public static final String MILLISECOND_TO_SUBSTRACT = "millisecondToSubstract";
  public static final String NB_BENEF = "nbBenef";
  public static final String NB_BENEF_KO = "nbBenefKO";

  public static final String NB_BENEF_WARNING = "nbBenefWarning";
  public static final String NB_BENEF_TO_PROCESS = "nbBenefToProcess";

  public static final String COUNT_TRIGGER_UNITAIRE = "count";

  public static final String NEXT_ENGINE_CORE_00408_1 = "NEXT-ENGINE-CORE-00408-1";
  public static final String NEXT_ENGINE_CORE_00408_2 = "NEXT-ENGINE-CORE-00408-2";

  public static final String NEXT_ENGINE_CORE_00430_3 = "NEXT-ENGINE-CORE-00430-3";

  public static final String NIR = "nir";
  public static final String NOM_FICHIER_ORIGINE = "nomFichierOrigine";
  public static final String NUMERO = "numero";
  public static final String NUMERO_ADHERENT = "numeroAdherent";
  public static final String NUMERO_CONTRAT = "numeroContrat";
  public static final String CONTRAT_COLLECTIF = "contratCollectif";
  public static final String NUMERO_CONTRAT_INDIVIDUEL = "numero";
  public static final String NUMERO_PERSONNE = "numeroPersonne";
  public static final String NUMERO_AMC_ECHANGE = "numAMCEchange";
  public static final String OC_PARAM_AMC = "amc";
  public static final String OFFRE = "offre";
  public static final String ORIGINE = "origine";
  public static final String PAGE = "page";
  public static final String PAGE2 = "page";
  public static final String PAGING = "paging";
  public static final String PARAMETRAGE_CARTE_TP_COLLECTION = "parametragesCarteTP";
  public static final String PARAMETRAGE_CARTE_TP_ID = "parametrageCarteTPId";
  public static final String PARAMETRAGE_RENOUVELLEMENT = "parametrageRenouvellement";

  public static final String PARAMETRAGE_RENOUVELLEMENT_MODE_DECLENCHEMENT =
      "parametrageRenouvellement.modeDeclenchement";
  public static final String PARAMETRES = "parametres";
  public static final String PARAMETRE_ACTION = "parametreAction";
  public static final String PERIODES = "periodes";
  public static final String PERIODE_DEBUT = "periodeDebut";
  public static final String PERIODE_FIN = "periodeFin";
  public static final String PER_PAGE = "perPage";
  public static final String PRODUIT = "produit";
  public static final String PRODUITS = "products";

  public static final String BENEFIT_NATURES = "benefitNatures";

  public static final String CODE_BENEFIT_NATURES = "code";

  public static final String LABEL_BENEFIT_NATURES = "label";

  public static final String DOMAIN_TPS = "domainTps";

  public static final String DOMAIN_CODE = "code";
  public static final String PW_PARAM_DATE = "date";
  public static final String PW_PARAM_DATE_FIN = "endDate";
  public static final String PW_PARAM_ISSUER_COMPANY = "issuerCompany";
  public static final String PW_PARAM_PRODUCT_CODE = "productCode";

  public static final String PW_PARAM_DATE_END = "endDate";
  public static final String PW_OFFER_CODE = "offerCode";
  public static final String PW_PRODUCT_CODE = "productCode";
  public static final String PW_CONTEXT = "context";

  public static final String PW_VALIDITY_DATE = "validityDate";
  public static final String PW_END_VALIDITY_DATE = "endValidityDate";

  public static final String SE_PARAM_ISSUER_COMPANY = "issuerCompany.equals";
  public static final String SE_OFFER_CODE = "offerCode.equals";
  public static final String SE_PARAM_PRODUCT_CODE = "productCode.equals";

  public static final String RANG_NAISSANCE = "rangNaissance";
  public static final String RANG_ADMINISTRATIF = "rangAdministratif";
  public static final String REFERENTIEL_PARAMETRAGE_CARTE_TP_COLLECTION =
      "referentielParametragesCarteTP";
  public static final String SERVICEDROITS = "services";
  public static final String SERVICE_PRESTATION_COLLECTION = "servicePrestation";
  public static final String RETENTION_COLLECTION = "retention";
  public static final String SERVICE_PRESTATION_ID = "servicePrestationId";
  public static final String SERVICE_PRESTATION_TRACE_ID = "traceId";
  public static final String SERVICE_PRESTATION_NUMERO = "numero";
  public static final String SERVICE_PRESTATION_LIST_NUMERO_PERSONNE = "listNumeroPersonne";
  public static final String SERVICE_PRESTATION_ASSURES_IDENTITE_NUMERO =
      "assures.identite.numeroPersonne";
  public static final String STATUS = "status";
  public static final String STATUT = "statut";
  public static final String STRING_ID = "stringId";
  public static final String S_S = "%s.%s";
  public static final String TOTAL_ELEMENTS = "totalElements";
  public static final String TOTAL_PAGES = "totalPages";
  public static final String TRIGGER = "trigger";
  public static final String TRIGGERED_BENEFICIARY = "triggeredBeneficiary";
  public static final String TRIGGERS = "triggers";
  public static final String TRIGGERS_BENEFS = "triggersBenefs";
  public static final String TRIGGERS_BENEFS_NUMERO_PERSONNE =
      "triggersBenefs.benefsInfos.numeroPersonne";
  public static final String TRIGGER_ID = "triggerId";
  public static final String TRIGGER_OBJ_ID = "triggerObjId";
  public static final String TYPE_DESTINATAIRE_PAIEMENT = "DestPaiement";
  public static final String TYPE_DESTINATAIRE_RELEVE_PRESTATION = "DestRelevePrest";
  public static final String UNIDENTIFIED = "Unidentified";
  public static final String USER_CREATION = "userCreation";
  public static final String USER_MODIFICATION = "userModification";
  public static final String VERSION_OFFRE = "offerVersion";

  public static final String VERSION_OFFRES = "offerVersions";

  public static final String SUSPENSION = "1";
  public static final String LEVEE_SUSPENSION = "2";
  public static final String ERREUR_SUSPENSION = "-1";

  public static final String CODE_ETAT_VALIDE = "V";
  public static final String CODE_ETAT_INVALIDE = "R";

  public static final String MODE_PAIEMENT_VIR = "VIR";

  public static final String ORIGINE_DECLARATIONTDB = "TDB/TFD";
  public static final String ORIGINE_DECLARATIONEVT = "Event";

  public static final String GENERATION_DROIT_TP = "GenerationDroitTP";

  public static final String NATURE_PRESTATION_VIDE_BOBB = "";

  public static final String NATURE_PRESTATION_OTP = "";

  public static final String YYYY_MM_DD = "yyyy-MM-dd";
  public static final String SLASHED_YYYY_MM_DD = "yyyy/MM/dd";
  public static final String XMLCALENDAR_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXX";

  public static final String N_A = "N/A";

  public static final String INSURED_DATA_TRACE = "insuredDataTrace";

  public static final String KAFKA_HEADER = "contractVersion";
  public static final String KAFKA_HEADER_TRACE = "traceId";
  public static final String KAFKA_HEADER_ID_CLIENT_BO = "idClientBo";
  public static final String KAFKA_HEADER_ID_DECLARANT = "idDeclarant";
  public static final String ERROR_PROVIDER = "BDDS";
  public static final String ID_DECLARANT_BENEF = "amc.idDeclarant";
  public static final String NUMERO_CONTRAT_BENEF = "contrats.numeroContrat";
  public static final String NUMERO_ADHERENT_CONTRAT_BENEF = "contrats.numeroAdherent";
  public static final String PERIODE_DEBUT_CONTRAT_BENEF = "contrats.periodes.debut";
  public static final String PERIODE_FIN_CONTRAT_BENEF = "contrats.periodes.fin";
  public static final String NUMERO_PERSONNE_BENEF = "identite.numeroPersonne";
  public static final String CONTRAT_NUMERO = "contrat.numero";
  public static final String CONTRAT_QUALIFICATION = "contrat.qualification";
  public static final String BENEFICIAIRE_NUMERO_PERSONNE = "beneficiaire.numeroPersonne";
  public static final String BENEFICIAIRES_NUMERO_PERSONNE = "beneficiaires.numeroPersonne";
  public static final String CARTE_NUMERO_PERSONNE = "beneficiaires.beneficiaire.numeroPersonne";
  public static final String CONTRAT_NUMERO_ADHERENT = "contrat.numeroAdherent";

  public static final String IS_LAST_CARTE_DEMAT = "isLastCarteDemat";

  /******* EVENT CONTRAT *******/
  public static final String EVENT_CONTRAT_V5 = "/v5/declarants/{idDeclarant}/contracts";

  public static final String EVENT_CONTRAT_V6 = "/v6/declarants/{idDeclarant}/contracts";
  public static final String GTLIST_V2 = "/v2/contractelement/gtlist";

  public static final String EVENT_CONTRAT_TEST = "/v5/declarants/{idDeclarant}/testContracts";

  public static final String EVENT_CONTRAT_TESTV6 = "/v6/declarants/{idDeclarant}/testContracts";
  public static final String DELETE_CONTRAT_V3 =
      "/v3/declarants/{idDeclarant}/contracts/{numeroContrat}";
  public static final String DELETE_CONTRAT_V4 =
      "/v4/declarants/{idDeclarant}/contracts/{numeroContrat}/subscriberId/{subscriberId}";
  public static final String CONTRAT_VERSION_5 = "5";
  public static final String CONTRAT_VERSION_6 = "6";
  public static final String CONTRACT_VERSION_V5 = "V5";
  public static final String CONTRACT_VERSION_V6 = "V6";

  public static final String GESTIONNAIRE = "gestionnaire";
  public static final String DATE_RESILIATION = "dateResiliation";

  public static final String CLAIM_CODE_CONTAINS_WHITE_SPACE =
      "Ce code de prestation contient un ou plusieurs espaces";

  public static final String SLASH = "/";

  public static final String ID_DELCENCHEUR = "idDeclencheur";

  public static final String TYPE = "type";

  public static final String OPERATION_DATE = "operationDate";

  public static final String ID_PERSONNE_BEYOND = "idPersonneBeyond";

  public static final String SOCIETE_EMETTRICE = "societeEmettrice";
  public static final String SOCIETES_EMETTRICES = "societesEmettrices";

  public static final String CODE_ANOMALIE = "codeAnomalie";

  public static final String CODE_ERREUR = "codeErreur";

  public static final String LABEL_ERREUR = "labelErreur";
  public static final String NUMERO_CONTRAT_COLLECTIVE = "numeroContratCollectif";

  public static final String CLIENT_TYPE_OTP = "OTP";

  public static final String CLIENT_TYPE_INSURER = "INSURER";

  public static final String ADRESSE_TYPE_ADHERENT = "AD";

  public static final String ADRESSE_TYPE_GESTIONNAIRE = "GE";

  public static final String CONTRAT_TYPE_A = "A";

  public static final String ORIGINE_RECYCLAGE = "Recyclage";

  public static final String NON_PAIEMENT_COTISATIONS = "NON_PAIEMENT_COTISATIONS";

  public static final String PORTABILITE_NON_JUSTIFIEE = "PORTABILITE_NON_JUSTIFIEE";

  public static final String AUCUNE_EDITION_CARTETP = "0";
  public static final String EDITION_CARTETP_A_FAIRE = "1";
  public static final String ATTESTATION_DIGITALE_UNIQUEMENT_A_DELIVRER = "2";
  public static final String EDITION_CARTETP_AFAIRE_ET_ATTESTATION_DIGITALE_A_DELIVRER = "3";
  public static final String ATTESTATION_DIGITALE_A_DELIVRER_ET_AUCUNE_EDITION_CARTETP = "4";
  public static final String UPDATE_BENEF_SOURCE = "updateBenefSource";

  public static final String ORIGINE_SERVICE_PRESTATION = "ServicePrestation";
  public static final String ORIGINE_PREST_IJ = "PrestIJ";

  public static final String ID_LOT = "idLots";

  public static final String GARANTIE_TECHNIQUES = "garantieTechniques";
  public static final String CODE_ASSUREUR = "codeAssureur";
  public static final String CODE_GARANTIE = "codeGarantie";

  public static final String LAST_DECLARATION_ID = "lastDeclarationId";

  public static final String QUERY_KEY = "query";
  public static final String NB_CREATED_BENEF = "nbBenefCreated";

  public static final String BATCH_MODE_NO_RDO = "NO_RDO";

  public static final String BATCH_MODE_RDO = "RDO";

  public static final String CARTE_DEMATERIALISEE = "CARTE-DEMATERIALISEE";
  public static final String CARTE_TP = "CARTE-TP";

  public static final String ALMV3 = "ALMV3";

  public static final String CARTETP_ONLY_FORUI = "CARTETP";

  public static final String JOB_620 = "job620";

  public static final String NUMERO_BATCH_620 = "620";
  public static final String NUMERO_BATCH_607 = "607";
  public static final String NUMERO_BATCH_608 = "608";

  public static final String DECLARATIONS_CONSOLIDEES_ALMERYS = "declarationsConsolideesAlmerys";

  public static final String TYPE_CONV_CODE =
      "domaineDroits.conventionnements.typeConventionnement.code";
  public static final String CRITERE_SECONDAIRE = "critereSecondaire";
  public static final String CONTRAT_CRITERE_SECONDAIRE = "contrat.critereSecondaire";
  public static final String CONTRAT_CRITERE_SECONDAIRE_DETAILLE =
      "contrat.critereSecondaireDetaille";
  public static final String TYPE_CONVENTIONNEMENT = "typeConventionnement";
  public static final String AMC_CONTRAT_CSD = "AMC_contrat_CSD";
  public static final String DATE_CONSOLIDATION = "dateConsolidation";
  public static final String DOMAINE_DROIT_CODE_EXTERNE_PRODUIT = "domaineDroit.codeExterneProduit";
  public static final String DOMAINE_DROIT = "domaineDroit";

  public static final String INDEX_DECLARATION_BATCH_620 = "IdxBatch620";

  /** Constante contenant le code état d'une déclaration résiliée */
  public static final String CODE_ETAT_RESILIATION = "R";

  /** Constante contenant le code etat d'une déclaration modifiée manuellement en base */
  public static final String CODE_REPRISE_TECHNIQUE = "Z";

  /** Constante fictive dans le cas de domaine multiple */
  public static final String CODE_DOMAINE_MULTIPLE = "XXX";

  /** Numero préfectoral CCMO */
  public static final String NUMERO_CCMO_ECHANGE = "0060005615";

  public static final String NUMERO_CCMO = "0780508073";

  /** Numero prefectoral AON */
  public static final String NUMERO_AON = "0000401182";

  public static final String IDENTIFIANT = "identifiant";

  public static final String V_3_1 = "3.1";

  public static final String CODE_SERVICE = "codeService";
  public static final String CODE_SERVICES = "codeServices";
  public static final int STEP_ONE = 1;
  public static final int STEP_TWO = 2;

  public static final String CODE_CARTES_PAPIER = "CARTES_PAPIER";

  public static final String EMISSION = "EMISSION";

  public static final String TO_BE_ISSUED = "TO_BE_ISSUED";
  public static final String ISSUED = "ISSUED";

  public static final String EDITING = "editing";
  public static final String LABEL_DOCUMENT_TYPE = "labelDocumentType";
  public static final String COMMUNICATION_CHANNEL = "communicationChannel";
  public static final String RECIPIENT_TYPE = "recipientType";
  public static final String GED_INDICATOR = "gedIndicator";
  public static final String CARTES_PAPIER_COLLECTION = "cartesPapier";
  public static final String CARTES_DEMAT_COLLECTION = "cartesDemat";
  public static final String DEFAUT = "defaut";
  public static final String SCOPE = "scope";
  public static final String COMMON = "common";
  public static final String DOCUMENT_TYPE = "documentType";

  public static final String QUALITE_A = "A";
  public static final String QUALITE_C = "C";
  public static final String QUALITE_E = "E";

  public static final String BATCH = "Batch";

  public static final String UPDATE_DATE = "updateDate";
  public static final String ERROR_MESSAGES = "errorMessages";
  public static final String ERROR_MESSAGE = "errorMessage";
  public static final String LINE_NUMBER = "lineNumber";

  public static final String BENEF_ID = "benefId";

  public static final String QUOTE = "\"";

  public static final String SERVICE_PRESTATIONS_RDO = "servicePrestationsRdo";
  public static final String RDO_SERVICE_PRESTATION_COLLECTION = "rdoServicePrestation";

  public static final String CSV = ".csv";

  public static final String NO_EXPORT = "NO EXPORT";
  public static final String EXPORTED = "exported";

  public static final String TRACES = "traces";
  public static final String NUMERO_AMC = "numeroAMC";

  public static final String ANNUEL = "A";
  public static final String QUOTIDIEN = "Q";

  public static final String MOTIF_RENOUVELLEMENT = "RE";

  public static final String LOW_GUARANTEE_LEVEL = "LOW";
  public static final String CODE_LPP = "LPP";

  public static final String MAILLE_NATURE_PRESTATIONS_FOR_UI = "mailleNatureDePrestations";
  public static final String MAILLE_REF_COUVERTURES_FOR_UI = "mailleRéférenceDeCouverture";
  public static final String MAILLE_PRODUIT_FOR_UI = "mailleProduit";
  public static final String MAILLE_GARANTIE_FOR_UI = "mailleGarantie";
  public static final String MAILLE_DOMAINE_TP_FOR_UI = "mailleDomaineTP";
  public static final int MAX_DECLARATIONS_FOR_UI = 500;
  public static final int MAX_ITEMS_PER_LOAD_UI = 10;

  public static final String HOSP = "HOSP";
  public static final String SEL_ROC = "SEL-ROC";
  public static final String DEBUT_MOIS = "debutMois";
  public static final String MILIEU_MOIS = "milieuMois";

  public static final String ONE_YEAR_DAYS = "365";

  public static final String RESTITUTION_CARTE_COLLECTION = "restitutionCarte";
  public static final String DATE_RESTITUTION_CARTE = "dateRestitutionCarte";
  public static final String ID_LOTS = "idLots";
  public static final String ID_LOT_ALIAS = "idLot";
  public static final String LOT_ALMERYS_LIST = "lotAlmerysList";
  public static final String DATE_FIN_DATE = "dateFinDate";
  public static final String DATE_DEBUT_DATE = "dateDebutDate";
  public static final String DATE_DEBUT = "dateDebut";
  public static final String DATE_FIN = "dateFin";
  public static final String PRODUCT_COMBINATIONS = "productCombinations";
  public static final String ALMERYS_PRODUCT_COLLECTION = "almerysProduct";
  public static final String DOT = ".";
  public static final String FORMAT_DATE = "%Y/%m/%d";
  public static final String GARANTIE_TECHNIQUE_LIST = "garantieTechniqueList";
  public static final String DATE_SUPPRESSION_LOGIQUE = "dateSuppressionLogique";
}
