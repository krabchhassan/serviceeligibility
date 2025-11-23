Feature: Get Digital Contract Informations
  Parametres obligatoires lors de l appel au WS :
  - AMC : idDeclarant
  - Adherent : subscriberId
  - numeroContrat : contractNumber
  - numeroPersonne : personNumber
  - Date : date
  - Domaines : domains

  Background:
    And I create a beneficiaire from file "digital_contract_infos/beneficiaire3"
    And I create a declarant from a file "digital_contract_infos/declarantbaloo_withTransco"
    And I try to create a parameter for type "domaine" in version "V2" with parameters
      | code              | HOSP                              |
      | transcodification | HOSP                              |
      | libelle           | Hospitalisation hors soin externe |
    And I try to create a parameter for type "domaine" in version "V2" with parameters
      | code              | LABO        |
      | transcodification | LABO        |
      | libelle           | Laboratoire |
    And I try to create a parameter for type "domaine" in version "V2" with parameters
      | code              | LARA        |
      | transcodification | LARA        |
      | libelle           | LABO + RADL |
    And I try to create a parameter for type "domaine" in version "V2" with parameters
      | code              | PHOR                         |
      | transcodification | PHOR                         |
      | libelle           | Pharmacie remboursable à 15% |
    And I try to create a parameter for type "domaine" in version "V2" with parameters
      | code              | RADL       |
      | transcodification | RADL       |
      | libelle           | Radiologue |
    And I try to create a parameter for type "domaine" in version "V2" with parameters
      | code              | OPAU                                   |
      | transcodification | OPAU                                   |
      | libelle           | Optique et audioprothèse (OPTI + AUDI) |
    And I get the parameter with code "HOSP", for type "domaine" in version "V2"
    And I get the parameter with code "LABO", for type "domaine" in version "V2"
    And I get the parameter with code "LARA", for type "domaine" in version "V2"
    And I get the parameter with code "PHOR", for type "domaine" in version "V2"
    And I get the parameter with code "RADL", for type "domaine" in version "V2"
    And I get the parameter with code "OPAU", for type "domaine" in version "V2"

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer with 2 guaranties on the same convention
    Given I create a contrat from file "digital_contract_infos/contratTP3"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2024-01-01", domains "OPAU"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | true               |
      | quality                  | A                  |
      | lastName                 | DELMOTTE           |
      | firstName                | LAURA              |
      | civility                 | Mme                |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 1 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | OPAU                                   |
      | domainLabel        | Optique et audioprothèse (OPTI + AUDI) |
      | conventionCode     | IS                                     |
      | conventionLabel    | IS                                     |
      | conventionPriority | 1                                      |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                            | conventionCode | conventionLabel | conventionPriority |
      | OPAU       | Optique et audioprothèse (OPTI + AUDI) | IS             | IS              | 1                  |


  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer with 2 guaranties on the same convention
    Given I create a contrat from file "digital_contract_infos/contratTP4"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2024-01-01", domains "OPAU"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | true               |
      | quality                  | A                  |
      | lastName                 | DELMOTTE           |
      | firstName                | LAURA              |
      | civility                 | Mme                |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 1 domains
    And the domain result with contract id "0" for domain with id "0" with 2 conventions has values
      | domainCode          | OPAU                                   |
      | domainLabel         | Optique et audioprothèse (OPTI + AUDI) |
      | conventionCode1     | CB                                     |
      | conventionLabel1    | CB                                     |
      | conventionPriority1 | 1                                      |
      | conventionCode2     | IS                                     |
      | conventionLabel2    | IS                                     |
      | conventionPriority2 | 1                                      |
    And the result with contract id "0" for domain with id "0" with 2 conventions has 1 regroupedDomain(s)
      | domainCode | domainLabel                            | conventionCode1 | conventionLabel1 | conventionPriority1 | conventionCode2 | conventionLabel2 | conventionPriority2 |
      | OPAU       | Optique et audioprothèse (OPTI + AUDI) | CB              | CB               | 1                   | IS              | IS               | 1                   |
