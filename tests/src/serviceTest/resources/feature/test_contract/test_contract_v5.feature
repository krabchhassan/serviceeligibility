Feature: Test contract V5 creation

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS |
      | libelle | IS |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL |
      | libelle | AL |

  @smokeTests @caseNewContractV5 @release @rv
  Scenario: Send a contract and verify that the trace references the contract
    Given I create a contract element from a file "gtdnt"
    Given I create a contract element from a file "gtdntass"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract from file "consumer_worker/v5/case1/contract1"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v5/case1/expectedContract2" content
    When I wait for the last trigger with contract number "8343484392" and amc "0008400004" to be "ProcessedWithErrors"
    Then I get the triggerBenef on the trigger with the index "0"
    And the triggerBenef has this values
      | statut           | Error                                |
      | derniereAnomalie | Aucun paramétrage de carte TP trouvé |
      | nir              | 1701062498046                        |
      | numeroPersonne   | 288939000                            |

  @nosmokeTests @nosmokeTestsWithoutKafka @contractV5 @insuredV5
  Scenario: Send an insured event
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract from file "consumer_worker/v5/case1/contract1"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v5/case1/expectedContract2" content
    When I send an insured from file "consumer_worker/v5/case1/insured1" to version "V5" for declarant "0008400004", contract "8343484392" and numeroPersonne "288939000"
    When I callback the contract
    Then the expected contract is identical to "consumer_worker/v5/case1/expectedInsured1" content

  @nosmokeTests @nosmokeTestsWithoutKafka @contractV5 @insuredV5
  Scenario: Send an insured event without new data
    Given I create a declarant from a file "declarantTestBlue"
    When I send a test contract from file "consumer_worker/v5/case6/contract1"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v5/case6/expectedContract1" content
    When I send an insured from file "consumer_worker/v5/case6/insured1" to version "V5" for declarant "0008400004", contract "8343484392" and numeroPersonne "288939000"
    When I callback the contract
    Then the expected contract is identical to "consumer_worker/v5/case6/expectedInsured1" content

  @todosmokeTests @smokeTestsWithoutKafka @contractV5 @insuredV5
  Scenario: Send an insured event with numeroPersonne
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a contract from file "consumer_worker/v5/case1/contract1" to version "V5"
    When the trace is created and contains the contract id for contract number "8343484392"
    Then the expected contract is identical to "consumer_worker/v5/case1/expectedContract1" content
    When I send an insured from file "consumer_worker/v5/case1/insured1V5" to version "V5" for declarant "0008400004", contract "8343484392" and numeroPersonne "288939000"
    When I callback the contract
    Then the expected contract is identical to "consumer_worker/v5/case1/expectedInsured1V5" content

  @smokeTests @caseRequiredData
  Scenario: I try to send a contract without data : dateFin for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case2/contractNoDateFinContexteTP"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.fin est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseRequiredData
  Scenario: I try to send a contract without data : dateDebut for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case3/contractNoDateDebutContexteTP"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.debut est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseBadData
  Scenario: I try to send a contract with bad data : dateDebut for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case4/contractDateDebutContexteTPMauvaisFormat"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.debut(2020/01/01) n'est pas une date au format yyyy-MM-dd"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseBadData
  Scenario: I try to send a contract with bad data : dateFin for ContexteTiersPayant
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case5/contractDateFinContexteTPMauvaisFormat"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information contexteTiersPayant.periodesDroitsCarte.fin(2023/12/31) n'est pas une date au format yyyy-MM-dd"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @nosmokeTests @contractV5 @caseNewContractV51
  Scenario: I try to send a contract without data : etat for periodesDroitsComptables
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case7/contract1"
    Then the response has an HTTP code "400"
    Then the first error message is "periodesDroitsComptable.etat est vide ou absent"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseRequiredData
  Scenario: I try to send a contract without data : email for dematerialisation
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case7/contract2"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 et l'assuré n°288939012 : l'information data.destinatairesRelevePrestations.dematerialisation.email ou data.destinatairesRelevePrestations.dematerialisation.mobile est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @nosmokeTests @contractV5 @caseRequiredData
  Scenario: I try to send a contract without data : periodesDroitsComptables
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case7/contract3"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 : periodesDroitsComptable est vide ou absent"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseNewContractV5
  Scenario: I send a contract with a state set to 'Fermeture'
    Given I create a contract element from a file "gtklesia"
    Given I create a declarant from a file "declarantTestBlue"
    When I send a contract from file "consumer_worker/v5/case7/contract4" to version "V5"
    When the trace is created and contains the contract id for contract number "Test1"
    Then the expected contract is identical to "consumer_worker/v5/case7/expectedContract1" content

  @nosmokeTests @contractV5 @caseRequiredData
  Scenario: I try to send a contract without data : type for periodesDroitsComptables
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case7/contract5"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°Test1 de l'adhérent n°Test1 lié à l'AMC 0008400004 : Si periodesDroitsComptable.etat est en Fermeture, periodesDroitsComptable.type ne doit pas etre null"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseSuspension
  Scenario: I send a contract with suspension
    Given I create a contract element from a file "gtklesia"
    Given I create a contract element from a file "gttestbluePlatineComp"
    Given I create a contract element from a file "gttestbluePlatineBase"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWithoutSuspension"
    When the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    When I get triggers with contract number "NLE-3957-0110-001" and amc "0008400004"
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspension"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "1" for benef
    Then I get "1" suspension periods on the trigger

  @smokeTests @caseSuspension
  Scenario: I send a contract with suspension
    Given I create a contract element from a file "gtklesia"
    Given I create a contract element from a file "gttestbluePlatineComp"
    Given I create a contract element from a file "gttestbluePlatineBase"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWithoutSuspension"
    When the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    When I get triggers with contract number "NLE-3957-0110-001" and amc "0008400004"
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspension"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "1" for benef
    Then I get "1" suspension periods on the trigger
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspension"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "2" for benef
    Then I get "1" suspension periods on the trigger
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWith2Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "3" for benef
    Then I get "2" suspension periods on the trigger
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWith3Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "4" for benef
    Then I get "3" suspension periods on the trigger
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWith4Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "5" for benef
    Then I get "4" suspension periods on the trigger

  @smokeTests @caseSuspension
  Scenario: I send a contract with RDO suspension
    Given I create a contract element from a file "gttestbluePlatineBase"
    Given I create a contract element from a file "gtklesia"
    Given I create a contract element from a file "gttestbluePlatineComp"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract from file "consumer_worker/v5/case8-suspension/contractWith4Suspensions"
    Then the trace is created and contains the contract id for contract number "NLE-3957-0110-001"
    Then I get one more trigger with contract number "NLE-3957-0110-001" and amc "0008400004" and indice "0" for benef
    Then I get "4" suspension periods on the trigger

  @smokeTests @caseSuspension
  Scenario: I try to send a contract with suspension and missing Period
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspensionMissingPeriod"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : l'information periodesSuspension.periode est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseSuspension
  Scenario: I try to send a contract with suspension and missing Period start
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspensionMissingPeriodStart"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : l'information periodesSuspension.periode.debut est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseSuspension
  Scenario: I try to send a contract with suspension and missing TypeSuspension
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspensionMissingTypeSuspension"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : L'information periodesSuspension.typeSuspension est obligatoire"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseSuspension
  Scenario: I try to send a contract with suspension and wrong TypeSuspension
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspensionWithWrongTypeSuspension"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : L'information periodesSuspension.typeSuspension ne contient pas une valeur autorisée"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseSuspension
  Scenario: I try to send a contract with suspension and wrong date format
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I try to send a test contract from file "consumer_worker/v5/case8-suspension/contractWithSuspensionWrongDateFormat"
    Then the response has an HTTP code "400"
    Then the first error message is "Pour le contrat n°NLE-3957-0110-001 de l'adhérent n°NLE-3957-0110-001 lié à l'AMC 0008400004 : L'information periodesSuspension.periode.debut(2021-03-0s1) n'est pas une date au format yyyy-MM-dd"
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"

  @smokeTests @caseClosedRights
  Scenario: I send a contract with base rights that are being closed
    Given I create a contract element from a file "gtklesia"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract from file "consumer_worker/v5/case9-fermetureDroits/contractWithOpenedBase"
    When the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    When I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"
    When I send a test contract from file "consumer_worker/v5/case9-fermetureDroits/contractWithClosedBase"
    Then the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    Then I get one more trigger with contract number "BLUE-3918-0809-007" and amc "0008400004" and indice "0" for benef

  @smokeTests @caseClosedRights
  Scenario: I send a contract with base and option rights and only the option rights are being closed
    Given I create a contract element from a file "gtklesia"
    Given I create a contract element from a file "gtklesia_option"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract from file "consumer_worker/v5/case9-fermetureDroits/contractWithOpenedOption"
    When the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    When I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"
    When I send a test contract from file "consumer_worker/v5/case9-fermetureDroits/contractWithClosedOption"
    Then the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    Then I get one more trigger with contract number "BLUE-3918-0809-007" and amc "0008400004" and indice "0" for benef

  @smokeTests @caseCarences
  Scenario: I send a contract with a carence with replacement rights
    Given I create a contract element from a file "gtklesia"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract from file "consumer_worker/v5/case10-carences/contractWithCarenceAndReplacementRights"
    When the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    When I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"

  @smokeTests @caseCarences
  Scenario: I send a contract with a carence without replacement rights
    Given I create a contract element from a file "gttestbluePlatineBase"
    Given I create a declarant from a file "declarantTestBlue"
    Given I create TP card parameters from file "parametragesCarteTP"
    When I send a test contract from file "consumer_worker/v5/case10-carences/contractWithCarenceAndWithoutReplacementRights"
    When the trace is created and contains the contract id for contract number "BLUE-3918-0809-007"
    When I get triggers with contract number "BLUE-3918-0809-007" and amc "0008400004"

  @smokeTests @caseNewContractV5 @sansEffetAssure
  Scenario: Send contracts and check sans effet assure without TP rights n°1
    Given I create a declarant from a file "declarantbaloo"
    And I create a contract element from a file "gtbasebalooCase1"
    Given I send a contract from file "consumer_worker/v5/sansEffet/contratAvecPereEtFils" to version "V5"
    Given I wait "1" seconds in order to consume the data
    When I send a contract from file "consumer_worker/v5/sansEffet/contratAvecPere" to version "V5"
    Then I get all benef from the database
    Then The benef 0 is identical to "beneficiary_worker/case1_SansEffet_benef1" content
    Then The benef 1 is identical to "beneficiary_worker/case1_SansEffet_benef2" content

  @todosmokeTests @smokeTestsWithoutKafka @contractV5 @caseNewContractV5 @sansEffetAssure
  Scenario: Send contracts and check sans effet assure with TP rights
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    Given I create a declarant from a file "declarantbaloo"
    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I send a contract from file "consumer_worker/v5/sansEffet/contrat2assures" to version "V5"
    When I send a message from file declaration_ouverture_benef1 to the kafka topic decl
    When I send a message from file declaration_ouverture_benef2 to the kafka topic decl
    Given I wait "1" seconds in order to consume the data
    When I send a contract from file "consumer_worker/v5/sansEffet/contrat1assure" to version "V5"
    When I send a message from file declaration_fermeture_benef2 to the kafka topic decl
    Then I get all benef from the database
    Then The benef 0 is identical to "beneficiary_worker/case2_SansEffet_benef1" content
    Then The benef 1 is identical to "beneficiary_worker/case2_SansEffet_benef2" content
    Then I wait for 4 declarations
    Then the declaration with indice 0 has
      | codeEtat       | V           |
      | numeroPersonne | 20280120-01 |
    Then the declaration with indice 1 has
      | codeEtat       | V           |
      | numeroPersonne | 20280120-02 |
    Then the declaration with indice 2 has
      | codeEtat       | V           |
      | numeroPersonne | 20280120-01 |
    Then the declaration with indice 3 has
      | codeEtat       | R           |
      | numeroPersonne | 20280120-02 |

  @smokeTests @caseNewContractV5 @sansEffetAssure
  Scenario: Send contracts and check sans effet assure without TP rights n°2
    And I create a contract element from a file "gtbasebalooCase1"
    Given I create a declarant from a file "declarantbaloo"
    Given I send a contract from file "consumer_worker/v5/sansEffet/contratAvecPereEtFils" to version "V5"
    Given I send a contract from file "consumer_worker/v5/sansEffet/contratAvecMereEtFils" to version "V5"
    Given I wait "1" seconds in order to consume the data
    When I send a contract from file "consumer_worker/v5/sansEffet/contratAvecPere" to version "V5"
    Then I get all benef from the database
    Then The benef 1 is identical to "beneficiary_worker/case3_SansEffet" content

  @todosmokeTests @smokeTestsWithoutKafka @contractV5 @caseRequiredData
  Scenario: I try to send a contract with missing datas
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a test contract from file "consumer_worker/v5/case2/contractWithMissingfFields"
    Then the response has an HTTP code "400"
    Then the error messages has this values
      | "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 : L'information dateSouscription est obligatoire"                                                                  |
      | "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 et l'assuré n°288939000 : l'information data.destinatairesRelevePrestations.dematerialisation est obligatoire"     |
      | "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 et l'assuré n°MBA098 : l'information identite.nir.cle est obligatoire"                                             |
      | "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 et l'assuré n°MBA098 : l'information data.destinatairesRelevePrestations.dematerialisation est obligatoire"        |
      | "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 et l'assuré n° non renseigné : l'information identite.numeroPersonne est obligatoire"                              |
      | "Pour le contrat n°8343484392 de l'adhérent n°83747438 lié à l'AMC 0008400004 et l'assuré n° non renseigné : l'information data.destinatairesRelevePrestations.dematerialisation est obligatoire |
    Then the status is "ValidationFailed"
    Then the error provider is "BDDS"
    When I get all servicePrestationTrace
    Then 1 service prestation traces is returned

  @smokeTests @isContratResponsable
  Scenario: Send contracts and check if isContratResponsable is correctly defined
    Given I create a contract element from a file "gtbasebalooCase1"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a declarant from a file "declarantbaloo_5770"
    And I create TP card parameters from file "parametrageTPBaloo_6278"
    When I send a test contract from file "consumer_worker/v5/contratResponsable/contrat_isContratResponsableTrue"
    Then I wait for 1 declarations
    Then I wait for 1 contract
    Then the consolidated contract has values
      | numeroContrat        | 02967043   |
      | idDeclarant          | 0000401166 |
      | isContratResponsable | true       |
    Then I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat          | 0000401166-02967043    |
      | isLastCarteDemat     | true                   |
      | periodeDebut         | %%CURRENT_YEAR%%/09/05 |
      | periodeFin           | %%CURRENT_YEAR%%/12/31 |
      | isContratResponsable | true                   |

  @smokeTests @isContratResponsable
  Scenario: Send contracts and check if isContratResponsable is correctly defined
    Given I create a contract element from a file "gtbasebalooCase1"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a declarant from a file "declarantbaloo_5770"
    And I create TP card parameters from file "parametrageTPBaloo_6278"
    When I send a test contract from file "consumer_worker/v5/contratResponsable/contrat_isContratResponsableFalse"
    Then I wait for 1 declarations
    Then I wait for 1 contract
    Then the consolidated contract has values
      | numeroContrat        | 02967043   |
      | idDeclarant          | 0000401166 |
      | isContratResponsable | false      |
    Then I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat          | 0000401166-02967043    |
      | isLastCarteDemat     | true                   |
      | periodeDebut         | %%CURRENT_YEAR%%/09/05 |
      | periodeFin           | %%CURRENT_YEAR%%/12/31 |
      | isContratResponsable | false                  |

  @smokeTests @sansEffet
  Scenario: I send a contract with sans effet in the insured period  => no TP rights
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contratV5/contrat_sansEffetPeriodeAssure"
    # Aucun produit trouvé : car le contrat a un sans effet periode assure
    Then I wait for 0 declaration
    Then I wait for 0 contract

  @smokeTests @sansEffet
  Scenario: I send a contract with sans effet in the right period  => no TP rights
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contratV5/contrat_sansEffetPeriodeDroit"
    # Aucun produit trouvé : car le contrat a un sans effet periode droit
    Then I wait for 0 declaration
    Then I wait for 0 contract
