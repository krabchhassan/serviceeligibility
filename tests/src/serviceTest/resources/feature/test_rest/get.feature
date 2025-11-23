Feature: Get one declaration

#  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4 il manque le codeOptionMutualiste et libelleOptionMutualiste dans PeriodeDroitContrat
#  Scenario: Call REST test Nominal no contract
#    Given I create a declarant from a file "1_nominal_no_contrat/create_declarant2"
#    Given I create a contrat from file "soapContrat/contratV4"
#    Then I post rest request from file "./features/resources/consultationRest/1_nominal_no_contratvisio_droit_nominal_requestV4.json" to the consultation endpoint "V4"
#    Then the expected response "./features/resources/consultationRest/1_nominal_no_contratvisio_droit_nominal_responseV4.json" is identical

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal dateRenouvellement with dureeValidite
    Given I create a declarant from a file "consultationRest/amc_duree_validite"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_HOSP"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_HOSP_request.json" to the consultation endpoint
    Then the dateRenouvellement is set according to dureeValidite

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal dateRenouvellement with periodeValidite debut
    Given I create a declarant from a file "consultationRest/amc_periode_validite_debut"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_HOSP"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_HOSP_request.json" to the consultation endpoint
    Then the dateRenouvellement is set according to periodeValidite debut

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal dateRenouvellement with periodeValidite milieu
    Given I create a declarant from a file "consultationRest/amc_periode_validite_milieu"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_HOSP"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_HOSP_request.json" to the consultation endpoint
    Then the dateRenouvellement is set according to periodeValidite milieu

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal dateRenouvellement with periodeValidite fin
    Given I create a declarant from a file "consultationRest/amc_periode_validite_fin"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_HOSP"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_HOSP_request.json" to the consultation endpoint
    Then the dateRenouvellement is set according to periodeValidite fin

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal dateRenouvellement without duree or periode
    Given I create a declarant from a file "consultationRest/amc_sans_duree_ou_periode"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_HOSP1"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_HOSP_request1.json" to the consultation endpoint
    Then the dateRenouvellement is set to periodeFin

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal dateRenouvellement without duree or periode in the past
    Given I create a declarant from a file "consultationRest/amc_sans_duree_ou_periode"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_HOSP"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_HOSP_request_past.json" to the consultation endpoint
    Then the dateRenouvellement is set to today

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal no dateRenouvellement with PHAR
    Given I create a declarant from a file "consultationRest/amc_periode_validite_fin"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_PHAR"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_PHAR_request.json" to the consultation endpoint
    Then the dateRenouvellement is not set

  @todosmokeTests @smokeTestsWithoutKafka @rest @restV4
  Scenario: Call REST test Nominal dateRenouvellement with duree on AMC and end asked period in futur
    Given I create a declarant from a file "consultationRest/amc_duree_validite"
    Given I create a contrat from file "consultationRest/contrat_date_renouvellement_HOSP"
    Then I post rest request from file "./features/resources/consultationRest/date_renouvellement_HOSP_request_furtur.json" to the consultation endpoint "V4"
    Then the dateRenouvellement is set to periodeFin
