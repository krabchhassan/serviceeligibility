Feature: Search contract collective data service

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search collective contract collective data from servicePrestation
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "contractCollectiveData/servicePrestationCollectifTemoin"
    Given I create a service prestation from a file "contractCollectiveData/servicePrestationCollectif"
    When I search contract collective data with contractNumber "8343484392" and context "HTP" and insurerId "929405823444" and subscriberId "BLUE-3918-0809-007D"
    Then the contract collective data has values
      | isIndividualContract     | false      |
      | collectiveContractNumber | 03094493   |
      | companyId                | 156        |
      | companyName              | entreprise |
      | siret                    | siret      |
      | populationGroup          | population |

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search individual contract collective data from servicePrestation
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "contractCollectiveData/servicePrestationCollectifTemoin"
    Given I create a service prestation from a file "contractCollectiveData/servicePrestationIndividuel"
    When I search contract collective data with contractNumber "8343484393" and context "HTP" and insurerId "929405823444" and subscriberId "83747438"
    Then the contract collective data has values
      | isIndividualContract | true |

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search collective contract collective data from TP_ONLINE
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif"
    When I search contract collective data with contractNumber "B1006221" and context "TP_ONLINE" and insurerId "929405823444" and subscriberId "B1006221"
    Then the contract collective data has values
      | isIndividualContract     | false      |
      | collectiveContractNumber | B0001189   |
      | companyId                | 156        |
      | companyName              | entreprise |
      | siret                    | siret      |
      | populationGroup          | population |

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search individual contract collective data from TP_ONLINE
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_individuel"
    When I search contract collective data with contractNumber "B1006222" and context "TP_ONLINE" and insurerId "929405823444" and subscriberId "B1006221"
    Then the contract collective data has values
      | isIndividualContract | true |

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search collective contract collective data from TP_OFFLINE
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif"
    When I search contract collective data with contractNumber "B1006221" and context "TP_OFFLINE" and insurerId "929405823444" and subscriberId "B1006221"
    Then the contract collective data has values
      | isIndividualContract     | false      |
      | collectiveContractNumber | B0001189   |
      | companyId                | 156        |
      | companyName              | entreprise |
      | siret                    | siret      |
      | populationGroup          | population |

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search individual contract collective data from TP_OFFLINE
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_individuel"
    When I search contract collective data with contractNumber "B1006222" and context "TP_OFFLINE" and insurerId "929405823444" and subscriberId "B1006221"
    Then the contract collective data has values
      | isIndividualContract | true |

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search contract collective data returns error with wrong context
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_individuel"
    When I try to search contract collective data with contractNumber "B1006222" and context "TP" and insurerId "929405823444" and subscriberId "B1006221"
    Then the answer is wrong context

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search contract collective data returns error when contractNumber is missing
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_individuel"
    When I try to search contract collective data with contractNumber "" and context "TP_ONLINE" and insurerId "929405823444" and subscriberId "B1006221"
    Then the answer is missing mandatory param

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search contract collective data returns error when context is missing
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_individuel"
    When I try to search contract collective data with contractNumber "B1006222" and context "" and insurerId "929405823444" and subscriberId "B1006221"
    Then the answer is missing mandatory param

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search contract collective data returns error when insurerId is missing
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_individuel"
    When I try to search contract collective data with contractNumber "B1006222" and context "TP_ONLINE" and insurerId "" and subscriberId "B1006221"
    Then the answer is missing mandatory param

  @todosmokeTests @smokeTestsWithoutKafka @contractCollectiveData
  Scenario: Get search contract collective data returns empty response when no contract found
    Given I create a contrat from file "contractCollectiveData/contrat_TP_collectif_temoin"
    Given I create a contrat from file "contractCollectiveData/contrat_TP_individuel"
    When I try to search contract collective data with contractNumber "B1006228" and context "TP_OFFLINE" and insurerId "929405823444" and subscriberId "B1006221"
    Then the contract collective data is empty
