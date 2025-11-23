Feature: Test interrogation des droits des bénéficiaires

  Background:
    Given I drop the collection for Contract

  @smokeTests @idb @release
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, mono beneficiaire
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_1" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_1" content

  @smokeTests @idb
  Scenario: Test IDB periode du 15/12/2021 au 20/11/2022, cas droits ouverts contigus, mono beneficiaire
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_2" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_2" content

  @smokeTests @idb
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts non contigus, mono beneficiaire
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_3"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_3" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_3" content

  @smokeTests @idb
  Scenario: Test IDB periode du 15/08/2022 au 15/10/2022, cas droits ouverts non contigus, mono beneficiaire
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_3"
    Then I post rest request from file "idb/v1/request_4" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_4" content

  @smokeTests @idb
  Scenario: Test IDB periode du 01/01/2024 au 01/04/2024, cas droits nonouverts, mono beneficiaire
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_3"
    Then I post rest request from file "idb/v1/request_5" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_5" content

  @smokeTests @idb
  Scenario: Test IDB periode du 05/01/2022 au 01/05/2023, mono beneficiaire, multi domaine
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_6" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_6" content

  @smokeTests @idb
  Scenario: Test IDB periode du 05/01/2022 au 01/05/2023, cas droits ouverts sur plusieurs contrats, mono beneficiaire, multi-domaines
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide_4"
    Then I post rest request from file "idb/v1/request_7" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_7" content

  @smokeTests @idb
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, multi-beneficiaires
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_1_2benefs"
    Then I post rest request from file "idb/v1/request_8" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_8" content

  @smokeTests @idb
  Scenario: Test IDB periode du 01/01/2022 au 05/04/2022, cas droits ouverts contigus, multi-beneficiaires
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_1_2benefs"
    Then I post rest request from file "idb/v1/request_9" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_9" content

  @smokeTests @idb
  Scenario: Test IDB periode du 01/01/2022 au 20/07/2022, cas droits ouverts non contigus, multi-beneficiaires
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_nonContigus_1_2benefs"
    Then I create a contrat from file "idb/v1/contrat_consolide_nonContigus_2_2benefs"
    Then I post rest request from file "idb/v1/request_10" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_10" content

  @smokeTests @idb
  Scenario: Test IDB periode du 01/01/2022 au 25/10/2023, cas droits ouverts contigus, multi-beneficiaires, plusieurs contrats
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_2_2benefs"
    Then I create a contrat from file "idb/v1/contrat_consolide_nonContigus_2_2benefs"
    Then I post rest request from file "idb/v1/request_11" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_11" content

  @smokeTests @idb
  Scenario: Test IDB periode du 01/10/2022 au 25/10/2023, cas droits ouverts contigus, multi-beneficiaires, plusieurs contrats 1
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_2_2benefs"
    Then I create a contrat from file "idb/v1/contrat_consolide_nonContigus_2_2benefs"
    Then I post rest request from file "idb/v1/request_12" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_12" content

  @smokeTests @idb
  Scenario: Test IDB periode du 01/10/2022 au 25/10/2023, cas droits ouverts contigus, multi-beneficiaires, plusieurs contrats 2
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_nonContigus_2_2benefs"
    Then I create a contrat from file "idb/v1/contrat_consolide_3_2benefs"
    Then I post rest request from file "idb/v1/request_12" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_13" content

  @smokeTests @idb
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, mono beneficiaire, avec typeGaranties valorise
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_1bis" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_1" content


  @smokeTests @clc @release
  Scenario: Test CLC periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, mono beneficiaire
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_1" to the "CLC" endpoint
    And the expected response is identical to "clc/v1/response_1_clc" content

  @smokeTests @idb @clc
  Scenario: Test IDB/CLC periodes avec une fin nulle dans le contrat et la requete
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1_noEnd"
    Then I post rest request from file "idb/v1/request_1_noEnd" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_1_noEnd" content
    When I post rest request from file "idb/v1/request_1_noEnd" to the "CLC" endpoint
    And the expected response is identical to "clc/v1/response_1_clc_noEnd" content

  # TODO
  @smokeTests @clc @lpp
  Scenario: Test CLC avec transfo prestation LPP
    Given I import an empty file
    Given I import a complete file for parametrage
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1_LPP"
    When I post rest request from file "idb/v1/request_1_noEnd" to the "CLC" endpoint
    And the expected response is identical to "clc/v1/response_1_clc_LPP" content

  @smokeTests @idb
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, mono beneficiaire, ROC : check dateRenouvellement dureeValidite
    Then I create a declarant from a file "declarantbaloo_ROC/declarantbaloo_ROC_dureeValidite"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_1" to the "IDB" endpoint
    Then the IDB dateRenouvellement is set according to dureeValidite 5

  @smokeTests @idb
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, mono beneficiaire, ROC : check dateRenouvellement periodeValidite debutMois
    Then I create a declarant from a file "declarantbaloo_ROC/declarantbaloo_ROC_periodeValidite_debut"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_1" to the "IDB" endpoint
    Then the IDB dateRenouvellement is set according to periodeValidite "debut"

  @smokeTests @idb
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, mono beneficiaire, ROC : check dateRenouvellement periodeValidite milieuMois
    Then I create a declarant from a file "declarantbaloo_ROC/declarantbaloo_ROC_periodeValidite_milieu"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_1" to the "IDB" endpoint
    Then the IDB dateRenouvellement is set according to periodeValidite "milieu"

  @smokeTests @idb
  Scenario: Test IDB periode du 05/04/2022 au 20/07/2022, cas droits ouverts contigus, mono beneficiaire, ROC : check dateRenouvellement periodeValidite finMois
    Then I create a declarant from a file "declarantbaloo_ROC/declarantbaloo_ROC_periodeValidite_fin"
    Then I create a contrat from file "idb/v1/contrat_consolide_benef1"
    Then I create a contrat from file "idb/v1/contrat_consolide2_benef1"
    Then I post rest request from file "idb/v1/request_1" to the "IDB" endpoint
    Then the IDB dateRenouvellement is set according to periodeValidite "fin"

    #TODO partie create a declaration + consultationDroitsV4
  @smokeTests @idb @clc @consultV4
  Scenario: Priorite unique sur même periode
    Then I create a declarant from a file "declarantbaloo"
    Then I create a declaration from a file "maille_otp/decl1"
    Then I create a declaration from a file "maille_otp/decl2"
    Then I create a declaration from a file "maille_otp/decl3"
    Then I create a declaration from a file "maille_otp/decl4"
    Then I create a declaration from a file "maille_otp/decl5"
    Then I create a declaration from a file "maille_otp/decl6"
    Then I create a declaration from a file "maille_otp/decl7"
    Then I create a contrat from file "idb/v1/contrat_conso_maille_otp"
    Then I post rest request from file "idb/v1/interroMailleOtp" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/responseMailleOtp" content
    Then I post rest request from file "idb/v1/interroMailleOtp" to the "CLC" endpoint
    And the expected response is identical to "clc/v1/clcResponseMailleOtp" content
    #todotests
#    Then I post rest request from file "./features/resources/idb/v1/interroMailleOtp.json" to the consultation endpoint "V4"
#    Then the expected response "./features/resources/consultationRest/interroMailleOtp.json" is identical

  @smokeTests @idb
  Scenario: Demande IDB avec des dates inversees, doit retourner une liste de droit vide et pas une erreur technique
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_6897"
    Then I post rest request from file "idb/v1/request_6897" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_6897" content


  @smokeTests @idb
  Scenario: Dates IDB egales consultationV4
    Then I create a declarant from a file "declarant7419"
    Then I create a contrat from file "idb/v1/contrat_7419"
    Then I post rest request from file "idb/v1/request_7419" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_7419" content
    # Appel a IDB doit retourner la meme date de renouvellement au 2025-01-14 que consultation v4
    # Then I post rest request from file "idb/v1/request_7419" to the "V4" endpoint
    # TODO voir pourquoi le retour de v4 n est pas mappe en TypeHistoriquePeriodeDroitV4
    #  et donc perd la dateRenovu juste pour les tests

  @smokeTests @idb
  Scenario: Dates IDB egales consultationV4 sans param declarant
    Then I create a declarant from a file "declarant7419_no_param"
    Then I create a contrat from file "idb/v1/contrat_7419"
    Then I post rest request from file "idb/v1/request_7419_2" to the "IDB" endpoint
    And the expected response is identical to "idb/v1/response_7419_2" content
