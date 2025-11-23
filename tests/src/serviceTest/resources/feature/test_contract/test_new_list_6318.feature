Feature: Test new list (contrat responsable, cmu...)

  Background:
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS |
      | libelle | IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo_5458"
    And I create TP card parameters from file "parametrageTPBaloo_5458"
    And I send a contract from file "contratV5/5516_one_benef" to version "V6"
    Then I wait for 1 declaration

  @todosmokeTests @smokeTestsWithoutKafka @contractV6
  Scenario: Check des nouvelles listes declaration
    #Test declaration
    Then The declaration has "contrat.periodeResponsableOuverts"
      | debut      | fin        |
      | 2018/01/01 | 2026/02/21 |
    And The declaration has "contrat.periodeCMUOuverts"
      |  |
    And The declaration has "beneficiaire.affiliationsRO"
      | nir.code      | nir.cle | rattachementRO.codeRegime | rattachementRO.codeCaisse | periode.debut | periode.fin |
      | 1860598145145 | 24      | 01                        | 781                       | 2018/01/01    | null        |
    And The declaration has "beneficiaire.periodesMedecinTraitant"
      |  |
    And The declaration has "beneficiaire.teletransmissions"
      | periode.debut | periode.fin | isTeletransmission |
      | 2018/01/01    | null        | true               |
    And The declaration has "beneficiaire.regimesParticuliers"
      |  |
    And The declaration has "beneficiaire.situationsParticulieres"
      |  |

  @todosmokeTests @smokeTestsWithoutKafka @contractV6
  Scenario: Check des nouvelles listes Contrat
    # Test contrat
    Then I wait for 1 contract
    Then The contrat has "periodeResponsableOuverts"
      | debut      | fin        |
      | 2018/01/01 | 2026/02/21 |
    And The contrat has "periodeCMUOuverts"
      |  |
    And The contrat has "beneficiaires.0.affiliationsRO"
      | nir.code      | nir.cle | rattachementRO.codeRegime | rattachementRO.codeCaisse | periode.debut | periode.fin |
      | 1860598145145 | 24      | 01                        | 781                       | 2018/01/01    | null        |
    And The contrat has "beneficiaires.0.periodesMedecinTraitant"
      |  |
    And The contrat has "beneficiaires.0.teletransmissions"
      | periode.debut | periode.fin | isTeletransmission |
      | 2018/01/01    | null        | true               |
    And The contrat has "beneficiaires.0.regimesParticuliers"
      |  |
    And The contrat has "beneficiaires.0.situationsParticulieres"
      |  |

  @todosmokeTests @smokeTestsWithoutKafka @contractV6
  Scenario: Check des nouvelles listes PAU
    #Test PAU
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-01-15' '2025-01-20' 0000401166 TP_ONLINE
    Then The PAU contrat has "responsibleContractOpenPeriods"
      | start      | end        |
      | 2018-01-01 | 2026-02-21 |
    Then The PAU contrat has "cmuContractOpenPeriods"
      |  |
    Then The PAU contrat has "insured.identity.affiliationsRO"
      | nir.code      | nir.key | attachementRO.regimeCode | attachementRO.healthInsuranceCompanyCode | period.start | period.end |
      | 1860598145145 | 24      | 01                       | 781                                      | 2018-01-01   | null       |
    Then The PAU contrat has "insured.attendingPhysicianOpenedPeriods"
      |  |
    Then The PAU contrat has "insured.digitRelation.remoteTransmissions"
      | period.start | period.end | isRemotelyTransmitted |
      | 2018-01-01   | null       | true                  |
    Then The PAU contrat has "insured.specialPlans"
      |  |
    Then The PAU contrat has "insured.specialStatuses"
      |  |


  @todosmokeTests @smokeTestsWithoutKafka @contractV6
  Scenario: Check des nouvelles listes IDB
    Then I post rest request from file "idb/v1/request_new_list" to the "IDB" endpoint
    Then The IDB response has "contrat"
      | isContratResponsable | isContratCMU | situationParticuliere |
      | true                 | null         | null                  |
    Then The IDB response has "beneficiaire.historiqueAffiliations"
      | medecinTraitant | regimeParticulier |
      | false           | null              |


  @todosmokeTests @smokeTestsWithoutKafka @contractV6
  Scenario: Check des nouvelles listes CLC
    Then I post rest request from file "idb/v1/request_new_list" to the "CLC" endpoint
    Then The CLC response has "contrat"
      | isContratResponsable | isContratCMU | situationParticuliere |
      | true                 | null         | null                  |
    Then The CLC response has "beneficiaire.historiqueAffiliations"
      | medecinTraitant | regimeParticulier |
      | false           | null              |
