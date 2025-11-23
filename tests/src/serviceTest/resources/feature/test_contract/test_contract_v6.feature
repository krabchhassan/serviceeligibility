Feature: Test contract v6 creation

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS |
      | libelle | IS |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL |
      | libelle | AL |

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseNewContractV6
  Scenario: Send a contract and verify that the trace references the contract
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract v6 from file "consumer_worker/v6/case1/contract1"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v6/case1/expectedContract1" content

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseNewContractV6
  Scenario: Send a contract and verify that the trace references the contract number
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract v6 from file "consumer_worker/v6/case1/contract1"
    Then the trace is created and contains the contract number "8343484392"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @insuredV5
  Scenario: Send an insured event
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract v6 from file "consumer_worker/v6/case1/contract1"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v6/case1/expectedContract2" content
    When I send an insured from file "consumer_worker/v6/case1/insured1" to version "V5" for declarant "0008400004", contract "8343484392" and numeroPersonne "288939000"
    When I callback the contract
    Then the expected contract is identical to "consumer_worker/v6/case1/expectedInsured1" content

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @insuredV5
  Scenario: Send an insured event without new data
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract v6 from file "consumer_worker/v6/case2/contract1"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v6/case2/expectedContract1" content
    When I send an insured from file "consumer_worker/v6/case2/insured1" to version "V5" for declarant "0008400004", contract "8343484392" and numeroPersonne "288939000"
    When I callback the contract
    Then the expected contract is identical to "consumer_worker/v6/case2/expectedInsured1" content

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @insuredV5
  Scenario: Send an insured event with numeroPersonne
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract v6 from file "consumer_worker/v6/case1/contract1"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v6/case1/expectedContract1" content
    When I send an insured from file "consumer_worker/v6/case1/insured1V5" to version "V5" for declarant "0008400004", contract "8343484392" and numeroPersonne "288939000"
    When I callback the contract
    Then the expected contract is identical to "consumer_worker/v6/case1/expectedInsured1V5" content

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseRequiredData
  Scenario: I try to send a contract without data : dateFin for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case2/contractNoDateFinContexteTP" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.fin est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseRequiredData
  Scenario: I try to send a contract without data : dateDebut for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case3/contractNoDateDebutContexteTP" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.debut est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseBadData
  Scenario: I try to send a contract with bad data : dateDebut for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case4/contractDateDebutContexteTPMauvaisFormat" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.debut(2020/01/01) n'est pas une date au format yyyy-MM-dd"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseBadData
  Scenario: I try to send a contract with bad data : dateFin for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case5/contractDateFinContexteTPMauvaisFormat" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.fin(2023/12/31) n'est pas une date au format yyyy-MM-dd"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseRequiredData
  Scenario: I try to send a contract without data : email for dematerialisation
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case6/contract1" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 et l'assuré n°288939012 : l'information data.destinatairesRelevePrestations.dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @contractV6 @caseRequiredData
  Scenario: I try to send a contract without data : contratCollectif identifiantCollectivite
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case6/contract2" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 : L'information contratCollectif.identifiantCollectivite est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @contractV6 @caseRequiredData
  Scenario: I try to send a contract without data : contratCollectif numero
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case6/contract2bis" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 : L'information contratCollectif.numero est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @contractV6 @caseRequiredData
  Scenario: I try to send a contract without data : contratCollectif raisonSociale
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case6/contract3" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 : L'information contratCollectif.raisonSociale est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @contractV6 @caseRequiredData
  Scenario: I try to send a contract without data : contratCollectif groupePopulation
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a contract from file "consumer_worker/v6/case6/contract4" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 : L'information contratCollectif.groupePopulation est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseNewContractV6
  Scenario: I send a contract with a state set to 'Fermeture'
    Given I create a contract element from a file "gtklesia"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract v6 from file "consumer_worker/v6/case6/contract5"
    When the trace is created and contains the contract id for contract number "Test1"
    Then the expected contract is identical to "consumer_worker/v6/case6/expectedContract1" content

  @todosmokeTests @smokeTestsWithKafka @contractV6 @caseSuspension
  Scenario: I send a contract with suspension
    Given I create a contract element from a file "gttestbluePlatineBase"
    Given I create a contract element from a file "gttestbluePlatineComp"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWithoutSuspension"
    When the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    When I get triggers with contract number "NLE-3957-0110-001" and amc "0008400004"
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWithSuspension"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "1" for benef
    Then I get "1" suspension periods on the trigger

  @todosmokeTests @smokeTestsWithKafka @contractV6 @caseSuspension @pauv5
  Scenario: I send a contract with suspension
    Given I create a contract element from a file "gtklesia"
    Given I create a contract element from a file "gttestbluePlatineComp"
    Given I create a contract element from a file "gttestbluePlatineBase"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWithoutSuspension"
    When the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    When I get triggers with contract number "NLE-3957-0110-001" and amc "0008400004"
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWithSuspension"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "1" for benef
    Then I get "1" suspension periods on the trigger
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWithSuspension"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "2" for benef
    Then I get "1" suspension periods on the trigger
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWith2Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "3" for benef
    Then I get "2" suspension periods on the trigger
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWith3Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "4" for benef
    Then I get "3" suspension periods on the trigger
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWith4Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "5" for benef
    Then I get "4" suspension periods on the trigger

    Given I create a beneficiaire from file "contractForPauV5/beneficiaireWithSuspension"

    When I get contrat PAUV5 for 1701062498046 '19791006' 1 '2021-01-01' '2021-02-25' 0008400004 HTP
    Then the 1 suspensions in contract data has values
      | suspensionStart | suspensionEnd | suspensionType | suspensionReason         | suspensionRemovalReason |
      | 2021-02-01      | 2021-03-01    | Provisoire     | NON_PAIEMENT_COTISATIONS | Paiement benef          |

    When I get contrat PAUV5 for 1701062498046 '19791006' 1 '2021-01-01' '2021-03-25' 0008400004 HTP
    Then the 2 suspensions in contract data has values
      | suspensionStart | suspensionEnd | suspensionType | suspensionReason         | suspensionRemovalReason |
      | 2021-03-01      | 2021-02-28    | Provisoire     | NON_PAIEMENT_COTISATIONS | Gratuit                 |
      | 2021-02-01      | 2021-03-01    | Provisoire     | NON_PAIEMENT_COTISATIONS | Paiement benef          |

    When I get contrat PAUV5 for 1701062498046 '19791006' 1 '2021-01-01' '2021-06-25' 0008400004 HTP
    Then the 3 suspensions in contract data has values
      | suspensionStart | suspensionEnd | suspensionType | suspensionReason         | suspensionRemovalReason |
      | 2021-05-01      | 2021-05-31    | Provisoire     | NON_PAIEMENT_COTISATIONS | null                    |
      | 2021-03-01      | 2021-02-28    | Provisoire     | NON_PAIEMENT_COTISATIONS | Gratuit                 |
      | 2021-02-01      | 2021-03-01    | Provisoire     | NON_PAIEMENT_COTISATIONS | Paiement benef          |

    When I get contrat PAUV5 for 1701062498046 '19791006' 1 '2021-03-01' '2021-06-25' 0008400004 HTP
    Then the 2 suspensions in contract data has values
      | suspensionStart | suspensionEnd | suspensionType | suspensionReason         | suspensionRemovalReason |
      | 2021-05-01      | 2021-05-31    | Provisoire     | NON_PAIEMENT_COTISATIONS | null                    |
      | 2021-02-01      | 2021-03-01    | Provisoire     | NON_PAIEMENT_COTISATIONS | Paiement benef          |

    When I get contrat PAUV5 for 1701062498046 '19791006' 1 '2021-01-01' '2021-09-25' 0008400004 HTP
    Then the 4 suspensions in contract data has values
      | suspensionStart | suspensionEnd | suspensionType | suspensionReason         | suspensionRemovalReason |
      | 2021-09-01      | null          | Provisoire     | NON_PAIEMENT_COTISATIONS | null                    |
      | 2021-05-01      | 2021-05-31    | Provisoire     | NON_PAIEMENT_COTISATIONS | null                    |
      | 2021-03-01      | 2021-02-28    | Provisoire     | NON_PAIEMENT_COTISATIONS | Gratuit                 |
      | 2021-02-01      | 2021-03-01    | Provisoire     | NON_PAIEMENT_COTISATIONS | Paiement benef          |

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseSuspension
  Scenario: I send a contract with RDO suspension
    Given I create a contract element from a file "gttestbluePlatineComp"
    Given I create a contract element from a file "gttestbluePlatineBase"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract v6 from file "consumer_worker/v6/caseSuspension/contractWith4Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "0" for benef
    Then I get "4" suspension periods on the trigger

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseSuspension
  Scenario: I try to send a contract with suspension and missing Period
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a contract from file "consumer_worker/v6/caseSuspension/contractWithSuspensionMissingPeriod" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : l'information periodesSuspension.periode est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseSuspension
  Scenario: I try to send a contract with suspension and missing Period start
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a contract from file "consumer_worker/v6/caseSuspension/contractWithSuspensionMissingPeriodStart" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : l'information periodesSuspension.periode.debut est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseSuspension
  Scenario: I try to send a contract with suspension and missing TypeSuspension
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a contract from file "consumer_worker/v6/caseSuspension/contractWithSuspensionMissingTypeSuspension" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : L'information periodesSuspension.typeSuspension est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseSuspension
  Scenario: I try to send a contract with suspension and wrong TypeSuspension
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a contract from file "consumer_worker/v6/caseSuspension/contractWithSuspensionWithWrongTypeSuspension" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : L'information periodesSuspension.typeSuspension ne contient pas une valeur autorisée"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseSuspension
  Scenario: I try to send a contract with suspension and wrong date format
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a contract from file "consumer_worker/v6/caseSuspension/contractWithSuspensionWrongDateFormat" to version "V6"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : L'information periodesSuspension.periode.debut(2021-03-0s1) n'est pas une date au format yyyy-MM-dd"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseClosedRights
  Scenario: I send a contract with base rights that are being closed
    Given I create a contract element from a file "gtklesia"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract v6 from file "consumer_worker/v6/fermetureDroits/contractWithOpenedBase"
    When the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    When I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"
    When I send a test contract v6 from file "consumer_worker/v6/fermetureDroits/contractWithClosedBase"
    Then the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    Then I get one more trigger with contract number "BLUE-3918-0809-007" and amc "0008400004" and indice "1" for benef

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseClosedRights
  Scenario: I send a contract with base and option rights and only the option rights are being closed
    Given I create a contract element from a file "gtklesia"
    Given I create a contract element from a file "gtklesia_option"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract v6 from file "consumer_worker/v6/fermetureDroits/contractWithOpenedOption"
    When the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    When I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"
    When I send a test contract v6 from file "consumer_worker/v6/fermetureDroits/contractWithClosedOption"
    Then the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    Then I get one more trigger with contract number "BLUE-3918-0809-007" and amc "0008400004" and indice "0" for benef

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseCarences
  Scenario: I send a contract with a carence with replacement rights
    Given I create a contract element from a file "gtklesia"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract v6 from file "consumer_worker/v6/caseCarences/contractWithCarenceAndReplacementRights"
    Then the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    Then I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"

  @todosmokeTests @smokeTestsWithoutKafka @contractV6 @caseCarences @caseConsolidation
  Scenario: I send a contract with a carence without replacement rights (check collectivite value on declaration contrat)
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL |
      | libelle | AL |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a declarant from a file "declarantTestBlue"
    And I create a contract element from a file "gttestbluePlatineBase"
    And I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract v6 from file "consumer_worker/v6/caseCarences/contractWithCarenceAndWithoutReplacementRights"
    When I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"
    When I wait for 1 declarations
    Then the declaration has this contract collective information
      | identifiantCollectivite | coll1             |
      | raisonSociale           | CGW               |
      | siret                   | 552 178 639 00132 |
      | groupePopulation        | cadre             |
    Then I wait for 1 contract
    Then the consolidated contract has values
      | identifiantCollectivite | coll1              |
      | raisonSociale           | CGW                |
      | siret                   | 552 178 639 00132  |
      | groupePopulation        | cadre              |
      | numeroContrat           | BLUE-3918-0809-007 |
      | idDeclarant             | 0008400004         |

  @todosmokeTests @contractV6 @suspension
  Scenario: I process declarations with 1 suspension
    Then I create a declaration from a file "declarationWithSuspension1"
    Then I wait for 1 declaration
    Then I process all declaration for idDeclarant "0000401166", numContrat "549726010101", numAdhérent "549726010101"
    And I wait "2" seconds in order to consume the data
    Then I wait for 1 contract
    Then the consolidated contract has 1 periodeSuspension
    Then the periodeSuspension of the consolidated contract has these values
      | dateDebutSuspension | 2024-01-02 |
      | dateFinSuspension   | 2024-03-31 |

  @todosmokeTests @contractV6 @suspension
  Scenario: I process declarations with several suspensions
    Then I create a declaration from a file "declarationWithSuspension1"
    Then I create a declaration from a file "declarationWithSuspension2"
    Then I wait for 2 declarations
    Then I process all declaration for idDeclarant "0000401166", numContrat "549726010101", numAdhérent "549726010101"
    And I wait "2" seconds in order to consume the data
    Then I wait for 1 contract
    Then the consolidated contract has 2 periodeSuspension
    Then the periodeSuspension with indice 0 of the consolidated contract has these values
      | dateDebutSuspension | 2024-01-02 |
      | dateFinSuspension   | 2024-03-31 |
    Then the periodeSuspension with indice 1 of the consolidated contract has these values
      | dateDebutSuspension | 2024-06-01 |
      | dateFinSuspension   | null       |

  @todosmokeTests @contractV6 @suspension
  Scenario: I process declarations with several suspensions + levee
    Then I create a declaration from a file "declarationWithSuspension1"
    Then I create a declaration from a file "declarationWithSuspension2"
    Then I create a declaration from a file "declarationWithSuspension3"
    Then I wait for 3 declarations
    Then I process all declaration for idDeclarant "0000401166", numContrat "549726010101", numAdhérent "549726010101"
    And I wait "2" seconds in order to consume the data
    Then I wait for 1 contract
    Then the consolidated contract has 2 periodeSuspension
    Then the periodeSuspension with indice 0 of the consolidated contract has these values
      | dateDebutSuspension | 2024-01-02 |
      | dateFinSuspension   | 2024-03-31 |
    Then the periodeSuspension with indice 1 of the consolidated contract has these values
      | dateDebutSuspension | 2024-06-01 |
      | dateFinSuspension   | 2024-06-30 |

  @todosmokeTests @contractV6
  Scenario: Send contract without contexteTP, then send the same contract with contexteTP
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL/IS |
      | libelle | AL/IS |
    Given I create a declarant from a file "declarantbaloo"
    And I create a contract element from a file "gtaxa"
    And I create TP card parameters from file "parametrageTPBalooEditable"
    When I send a test contract v6 from file "consumer_worker/v6/contract_6321"
    Then I wait for 1 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | isEditable |
      | AXASCCGDIM | HOSP    | 2025-01-01 | 2025-12-31 | true       |
      | AXASCCGDIM | OPAU    | 2025-01-01 | 2025-12-31 | false      |

    When I send a test contract v6 from file "consumer_worker/v6/contract_6321_withContexteTiersPayant"
    Then I wait for 2 declarations
    Then there is 3 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | isEditable |
      | AXASCCGDIM | HOSP    | 2025-01-01 | 2025-07-04 | false      |
      | AXASCCGDIM | HOSP    | 2025-07-05 | 2025-12-31 | true       |
      | AXASCCGDIM | OPAU    | 2025-01-01 | 2025-12-31 | false      |

  @todosmokeTests @contractV6
  Scenario: Send contract with a family name and a use name different
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL/IS |
      | libelle | AL/IS |
    Given I create a declarant from a file "declarantbaloo_5770"
    And I create a contract element from a file "gtaxa"
    And I create TP card parameters from file "parametrageTPBalooEditable"
    When I send a test contract v6 from file "consumer_worker/v6/contract_6321"
    Then I wait for 1 declarations
    Then the declaration with indice 0 has
      | codeEtat       | V       |
      | numeroPersonne | 691951  |
      | nomPorteur     | BILLOTE |


  @todosmokeTests @contractV6 @pauv5 @toChangeEveryYear
  Scenario: BLUE-6850 appel au PAU juste avec num adherent sans date/rang de naissance
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "benef4-6624"
    When I send a contract from file "contratV6/6624-1assure" to version "V6"
    Then I wait for 1 declaration
    When I get contrat PAUV5 for '2025-01-01' '2025-01-02' 0000401166 HTP and numAdherent 5894-01
    Then In the PAU, there is 1 contract
    When I get contrat PAUV5 for '2025-01-01' '2025-01-02' 0000401166 TP_ONLINE and numAdherent 5894-01
    Then In the PAU, there is 1 contract
    When I get contrat PAUV5 for '2025-01-01' '2025-01-02' 0000401166 TP_OFFLINE and numAdherent 5894-01
    Then In the PAU, there is 1 contract


  @todosmokeTests @contractV6 @6865
  Scenario: Réception d'un contrat résilié sans génération de droits TP
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL/IS |
      | libelle | AL/IS |
    Given I create a declarant from a file "declarantbaloo_5770"
    And I create a contract element from a file "gtaxa"
    When I send a test contract v6 from file "consumer_worker/v6/contract_6321"
    Then I wait for 0 declaration
    Then I get 1 trigger with contract number "1107070" and amc "0000401166"
    Then I wait for the first trigger with contract number "1107070" and amc "0000401166" to be "ProcessedWithErrors"
    Given I abandon the trigger
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And The declaration number 0 has codeEtat "V"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 2 declarations
    And The declaration number 1 has codeEtat "V"
    And I create TP card parameters from file "parametrageTPBaloo_pilotageBO"
    When I send a test contract v6 from file "consumer_worker/v6/contract_6865_resil"
    Then I wait for 5 declarations
    And The declaration number 2 has codeEtat "R"
    And The declaration number 3 has codeEtat "V"
    And The declaration number 4 has codeEtat "R"
