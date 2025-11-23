Feature: Get Digital Contract Informations
  Parametres obligatoires lors de l appel au WS :
  - AMC : idDeclarant
  - Adherent : subscriberId
  - numeroContrat : contractNumber
  - numeroPersonne : personNumber
  - Date : date
  - Domaines : domains

  Background:
    Given I drop the collection for Beneficiary
    And I drop the collection for Service Prestation
    And I drop the collection for Contract
    And I create a beneficiaire from file "digital_contract_infos/beneficiaire"

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos
  Scenario: Get Digital Contract Informations
    Given I create a contrat from file "digital_contract_infos/contratTP"
    Given I create a service prestation from a file "servicePrestationContract01"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains """"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | true               |
      | quality                  | A                  |
      | lastName                 | YAKER              |
      | firstName                | MILISSA            |
      | civility                 | MME                |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos
  Scenario: Get Digital Contract Informations with 2 payments recipients in result
    Given I create a service prestation from a file "servicePrestationContract01"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-03-01", domains """"
    Then the result has 2 payments

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos
  Scenario: Get Digital Contract Informations for several benefs
    Given I create a beneficiaire from file "digital_contract_infos/beneficiaire2"
    Given I create a contrat from file "digital_contract_infos/contratTP2"
    Given I create a service prestation from a file "servicePrestationContract01"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains """"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | true               |
      | quality                  | A                  |
      | lastName                 | YAKER              |
      | firstName                | MILISSA            |
      | civility                 | MME                |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    Then the search result with contract id "1" has values
      | nirCode            | 2011099352674 |
      | birthDate          | 19791012      |
      | birthRank          | 1             |
      | personNumber       | 02785498-02   |
      | administrativeRank | 1             |
      | isSubscriber       | false         |
      | quality            | A             |
      | lastName           | DELMOTTE      |
      | firstName          | LAURA         |
      | civility           | Mme           |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos
  Scenario: Get Digital Contract Informations but no contract found for the beneficiary
    Given I create a service prestation from a file "servicePrestationContract01"
    When I try to search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2020-03-01", domains """"
    Then an error "404" is returned with message "No contract found for this beneficiary"

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos
  Scenario: Get Digital Contract Informations with bad date format
    Given I create a service prestation from a file "servicePrestationContract01"
    When I try to search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2020-3-01", domains """"
    Then an error "400" is returned with message "The date 2020-3-01 is not valid"

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos
  Scenario: Get Digital Contract Informations with a benef with only contratTP
    Given I create a contrat from file "digital_contract_infos/contratTP"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains """"
    Then the search result with contract id "0" has values
      | nirCode            | 2011099352674 |
      | birthDate          | 19791006      |
      | birthRank          | 1             |
      | personNumber       | 02785498      |
      | administrativeRank | 1             |
      | isSubscriber       | false         |
      | quality            | A             |
      | lastName           | MASON         |
      | firstName          | EDWIN         |
      | civility           | Mr            |
