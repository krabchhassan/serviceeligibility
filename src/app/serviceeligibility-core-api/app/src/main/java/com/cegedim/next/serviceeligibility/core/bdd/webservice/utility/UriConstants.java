package com.cegedim.next.serviceeligibility.core.bdd.webservice.utility;

public final class UriConstants {

  /******* BENEFICIAIRE *******/
  public static final String BENEFICIARY_TP_DETAILS = "/beneficiaryTpDetails";

  public static final String NEXT_CONSOLIDATED_CONTRATS_TP = "/nextConsolidatedContratsTP";
  public static final String NEXT_CERTIFICATIONS = "/nextCertifications";
  public static final String NEXT_DECLARATIONS = "/nextDeclarations";

  /******* DECLARANT *******/
  public static final String DECLARANTS = "/declarants";

  public static final String DECLARANTS_LAST_MODIFIED = "/last";
  public static final String DECLARANTS_LAST_MODIFIED_TOTAL = "/last/total";
  public static final String DECLARANTS_SEARCH = "/find";
  public static final String DECLARANTS_LIGHT = "/light";
  public static final String DECLARANTS_OPEN = "/open";
  public static final String CREATE_DECLARANT = "/create";
  public static final String UPDATE_DECLARANT = "/update";

  /******* TRANSCODAGE *******/
  public static final String TRANSCODAGE = "/transcodage";

  public static final String CREATE_TRANSCODAGE = "/create";
  public static final String UPDATE_TRANSCODAGE = "/update";
  public static final String ALL_SERVICES_TRANSCODAGE = "/services";
  public static final String SERVICE_BY_ID = "/services/{id}";
  public static final String TRANSCO_PAR_SERVICE = "services/{codeService}/{codeObjetTransco}";

  /******* PARAM TRANSCODAGE *******/
  public static final String PARAM_TRANSCODAGE = "/paramTranscodage";

  public static final String PARAM_TRANSCODAGE_BY_CODE = "/{code}";

  /******* VOLUMETRIE *******/
  public static final String VOLUMETRIE = "/volumetrie";

  /******* FLUX *******/
  public static final String FLUX = "/flux";

  /******* PARAMETRE *******/
  public static final String ALL_PROCESSUS = "/processus";

  public static final String ALL_TYPE_FICHIER = "/typeFichiers";
  public static final String ALL_REJETS = "/rejets";
  public static final String ALL_CODES_RENVOI = "/codesRenvoi";
  public static final String PARAMETERS = "/parameters";
  public static final String PARAMETERS_BY_TYPE = "/parameters/{type}";
  public static final String ONE_PARAMETER_BY_TYPE = "/parameters/{type}/{code}";
  public static final String ALL_CIRCUITS = "/circuits";
  public static final String ALL_CONVENTIONS = "/conventions";
  public static final String ALL_SERVICES_TRIES = "/services";
  public static final String SERVICES_BY_NAME = "/services/{name}";
  public static final String PRESTATIONS = "prestations";

  /******* DECLARANT ECHANGE *******/
  public static final String DECLARANT_ECHANGES = "/declarantechanges";

  /******* PARAMETRAGE CARTE TP *******/
  public static final String PARAMETRAGE_CARTE_TP = "/parametragesCarteTP";

  public static final String REFERENTIEL_PARAMETRAGE_CARTE_TP = "/referentielParametrageCarteTP";

  /******* DECLENCHEUR DE GENERATION DE CARTE TP *******/
  public static final String DECLENCHEURS = "/declencheursCarteTP";

  /******* CREATION DE L'OBJET FINANCIER *******/
  public static final String CONTRACT_COLLECTIVE_DATA = "/contractCollectiveData";

  private UriConstants() {}
}
