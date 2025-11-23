Feature: Get Digital Contract Informations
  Parametres obligatoires lors de l appel au WS :
  - AMC : idDeclarant
  - Adherent : subscriberId
  - numeroContrat : contractNumber
  - numeroPersonne : personNumber
  - Date : date
  - Domaines : domains

  Background:
    Given I create a beneficiaire from file "digital_contract_infos/beneficiaire"
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
    And I get the parameter with code "HOSP", for type "domaine" in version "V2"
    And I get the parameter with code "LABO", for type "domaine" in version "V2"
    And I get the parameter with code "LARA", for type "domaine" in version "V2"
    And I get the parameter with code "PHOR", for type "domaine" in version "V2"
    And I get the parameter with code "RADL", for type "domaine" in version "V2"

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with only one transcoded domain LARA (LARA = LABO - RADL), and benef has LABO and RADL
    Given I create a contrat from file "digital_contract_infos/contratTP"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "LARA"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | false              |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 1 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | LARA        |
      | domainLabel        | LABO + RADL |
      | conventionCode     | IS          |
      | conventionLabel    | IS          |
      | conventionPriority | 1           |
    And the result with contract id "0" for domain with id "0" has 2 regroupedDomain(s)
      | domainCode | domainLabel | conventionCode | conventionLabel | conventionPriority |
      | LABO       | Laboratoire | IS             | IS              | 1                  |
      | RADL       | Radiologue  | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with only one domain HOSP, and benef has HOSP
    Given I create a contrat from file "digital_contract_infos/contratTP"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "HOSP"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | fals               |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 1 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | HOSP                              |
      | domainLabel        | Hospitalisation hors soin externe |
      | conventionCode     | IS                                |
      | conventionLabel    | IS                                |
      | conventionPriority | 1                                 |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                       | conventionCode | conventionLabel | conventionPriority |
      | HOSP       | Hospitalisation hors soin externe | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer (LARA = LABO - RADL), and benef has LABO and RADL
    Given I create a contrat from file "digital_contract_infos/contratTP"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "LARA,HOSP,PHOR"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | fals               |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 3 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | HOSP                              |
      | domainLabel        | Hospitalisation hors soin externe |
      | conventionCode     | IS                                |
      | conventionLabel    | IS                                |
      | conventionPriority | 1                                 |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                       | conventionCode | conventionLabel | conventionPriority |
      | HOSP       | Hospitalisation hors soin externe | IS             | IS              | 1                  |
  # domaine 2
    And the domain result with contract id "0" for domain with id "1" has values
      | domainCode         | LARA        |
      | domainLabel        | LABO + RADL |
      | conventionCode     | IS          |
      | conventionLabel    | IS          |
      | conventionPriority | 1           |
    And the result with contract id "0" for domain with id "1" has 2 regroupedDomain(s)
      | domainCode | domainLabel | conventionCode | conventionLabel | conventionPriority |
      | LABO       | Laboratoire | IS             | IS              | 1                  |
      | RADL       | Radiologue  | IS             | IS              | 1                  |
  # domaine 3
    And the domain result with contract id "0" for domain with id "2" has values
      | domainCode         | PHOR                         |
      | domainLabel        | Pharmacie remboursable à 15% |
      | conventionCode     | IS                           |
      | conventionLabel    | IS                           |
      | conventionPriority | 1                            |
    And the result with contract id "0" for domain with id "2" has 1 regroupedDomain(s)
      | domainCode | domainLabel                  | conventionCode | conventionLabel | conventionPriority |
      | PHOR       | Pharmacie remboursable à 15% | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer (LARA = LABO - RADL), but grouping impossible between LABO and RADL
    Given I create a contrat from file "digital_contract_infos/contratTP_impossibleGrouping"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "LARA,HOSP,PHOR"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | fals               |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 4 domains
    # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | HOSP                              |
      | domainLabel        | Hospitalisation hors soin externe |
      | conventionCode     | IS                                |
      | conventionLabel    | IS                                |
      | conventionPriority | 1                                 |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                       | conventionCode | conventionLabel | conventionPriority |
      | HOSP       | Hospitalisation hors soin externe | IS             | IS              | 1                  |
    # domaine 2
    And the domain result with contract id "0" for domain with id "1" has values
      | domainCode         | LABO        |
      | domainLabel        | Laboratoire |
      | conventionCode     | IT          |
      | conventionLabel    | IT          |
      | conventionPriority | 1           |
    And the result with contract id "0" for domain with id "1" has 1 regroupedDomain(s)
      | domainCode | domainLabel | conventionCode | conventionLabel | conventionPriority |
      | LABO       | Laboratoire | IT             | IT              | 1                  |
    # domaine 3
    And the domain result with contract id "0" for domain with id "2" has values
      | domainCode         | RADL       |
      | domainLabel        | Radiologue |
      | conventionCode     | IS         |
      | conventionLabel    | IS         |
      | conventionPriority | 1          |
    And the result with contract id "0" for domain with id "2" has 1 regroupedDomain(s)
      | domainCode | domainLabel | conventionCode | conventionLabel | conventionPriority |
      | RADL       | Radiologue  | IS             | IS              | 1                  |
  # domaine 4
    And the domain result with contract id "0" for domain with id "3" has values
      | domainCode         | PHOR                         |
      | domainLabel        | Pharmacie remboursable à 15% |
      | conventionCode     | IS                           |
      | conventionLabel    | IS                           |
      | conventionPriority | 1                            |
    And the result with contract id "0" for domain with id "3" has 1 regroupedDomain(s)
      | domainCode | domainLabel                  | conventionCode | conventionLabel | conventionPriority |
      | PHOR       | Pharmacie remboursable à 15% | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer (LARA = LABO - RADL), but benef has only RADL
    Given I create a contrat from file "digital_contract_infos/contratTP_onlyRADL"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "LARA,HOSP,PHOR"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | fals               |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 3 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | HOSP                              |
      | domainLabel        | Hospitalisation hors soin externe |
      | conventionCode     | IS                                |
      | conventionLabel    | IS                                |
      | conventionPriority | 1                                 |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                       | conventionCode | conventionLabel | conventionPriority |
      | HOSP       | Hospitalisation hors soin externe | IS             | IS              | 1                  |
  # domaine 2
    And the domain result with contract id "0" for domain with id "1" has values
      | domainCode         | LARA        |
      | domainLabel        | LABO + RADL |
      | conventionCode     | IS          |
      | conventionLabel    | IS          |
      | conventionPriority | 1           |
    And the result with contract id "0" for domain with id "1" has 1 regroupedDomain(s)
      | domainCode | domainLabel | conventionCode | conventionLabel | conventionPriority |
      | RADL       | Radiologue  | IS             | IS              | 1                  |
  # domaine 3
    And the domain result with contract id "0" for domain with id "2" has values
      | domainCode         | PHOR                         |
      | domainLabel        | Pharmacie remboursable à 15% |
      | conventionCode     | IS                           |
      | conventionLabel    | IS                           |
      | conventionPriority | 1                            |
    And the result with contract id "0" for domain with id "2" has 1 regroupedDomain(s)
      | domainCode | domainLabel                  | conventionCode | conventionLabel | conventionPriority |
      | PHOR       | Pharmacie remboursable à 15% | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer (LARA = LABO - RADL), but benef has only LARA
    Given I create a contrat from file "digital_contract_infos/contratTP_hasOnlyLARA"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "LARA,HOSP,PHOR"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | fals               |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 3 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | HOSP                              |
      | domainLabel        | Hospitalisation hors soin externe |
      | conventionCode     | IS                                |
      | conventionLabel    | IS                                |
      | conventionPriority | 1                                 |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                       | conventionCode | conventionLabel | conventionPriority |
      | HOSP       | Hospitalisation hors soin externe | IS             | IS              | 1                  |
  # domaine 2
    And the domain result with contract id "0" for domain with id "1" has values
      | domainCode         | LARA        |
      | domainLabel        | LABO + RADL |
      | conventionCode     | IS          |
      | conventionLabel    | IS          |
      | conventionPriority | 1           |
    And the result with contract id "0" for domain with id "1" has 1 regroupedDomain(s)
      | domainCode | domainLabel | conventionCode | conventionLabel | conventionPriority |
      | LARA       | LABO + RADL | IS             | IS              | 1                  |
  # domaine 3
    And the domain result with contract id "0" for domain with id "2" has values
      | domainCode         | PHOR                         |
      | domainLabel        | Pharmacie remboursable à 15% |
      | conventionCode     | IS                           |
      | conventionLabel    | IS                           |
      | conventionPriority | 1                            |
    And the result with contract id "0" for domain with id "2" has 1 regroupedDomain(s)
      | domainCode | domainLabel                  | conventionCode | conventionLabel | conventionPriority |
      | PHOR       | Pharmacie remboursable à 15% | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer (LARA = LABO - RADL), but benef has only LARA and RADL
    Given I create a contrat from file "digital_contract_infos/contratTP_hasOnlyLARAandRADL"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "LARA,HOSP,PHOR"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | fals               |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 3 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | HOSP                              |
      | domainLabel        | Hospitalisation hors soin externe |
      | conventionCode     | IS                                |
      | conventionLabel    | IS                                |
      | conventionPriority | 1                                 |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                       | conventionCode | conventionLabel | conventionPriority |
      | HOSP       | Hospitalisation hors soin externe | IS             | IS              | 1                  |
  # domaine 2
    And the domain result with contract id "0" for domain with id "1" has values
      | domainCode         | LARA        |
      | domainLabel        | LABO + RADL |
      | conventionCode     | IS          |
      | conventionLabel    | IS          |
      | conventionPriority | 1           |
    And the result with contract id "0" for domain with id "1" has 1 regroupedDomain(s)
      | domainCode | domainLabel | conventionCode | conventionLabel | conventionPriority |
      | RADL       | Radiologue  | IS             | IS              | 1                  |
  # domaine 3
    And the domain result with contract id "0" for domain with id "2" has values
      | domainCode         | PHOR                         |
      | domainLabel        | Pharmacie remboursable à 15% |
      | conventionCode     | IS                           |
      | conventionLabel    | IS                           |
      | conventionPriority | 1                            |
    And the result with contract id "0" for domain with id "2" has 1 regroupedDomain(s)
      | domainCode | domainLabel                  | conventionCode | conventionLabel | conventionPriority |
      | PHOR       | Pharmacie remboursable à 15% | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations, with rights domain transcoded for this insurer (LARA = LABO - RADL), but benef has neither LARA, LABO or RADL
    Given I create a contrat from file "digital_contract_infos/contratTP_hasNothing"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "LARA,HOSP,PHOR"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | fals               |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 2 domains
  # domaine 1
    And the domain result with contract id "0" for domain with id "0" has values
      | domainCode         | HOSP                              |
      | domainLabel        | Hospitalisation hors soin externe |
      | conventionCode     | IS                                |
      | conventionLabel    | IS                                |
      | conventionPriority | 1                                 |
    And the result with contract id "0" for domain with id "0" has 1 regroupedDomain(s)
      | domainCode | domainLabel                       | conventionCode | conventionLabel | conventionPriority |
      | HOSP       | Hospitalisation hors soin externe | IS             | IS              | 1                  |
  # domaine 2
    And the domain result with contract id "0" for domain with id "1" has values
      | domainCode         | PHOR                         |
      | domainLabel        | Pharmacie remboursable à 15% |
      | conventionCode     | IS                           |
      | conventionLabel    | IS                           |
      | conventionPriority | 1                            |
    And the result with contract id "0" for domain with id "1" has 1 regroupedDomain(s)
      | domainCode | domainLabel                  | conventionCode | conventionLabel | conventionPriority |
      | PHOR       | Pharmacie remboursable à 15% | IS             | IS              | 1                  |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations whith domain MEDE, but benef doesn't have rights domain MEDE
    Given I create a contrat from file "digital_contract_infos/contratTP_hasNothing"
    When I search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "MEDE"
    Then the search result with contract id "0" has values
      | nirCode                  | 2011099352674      |
      | birthDate                | 19791006           |
      | birthRank                | 1                  |
      | personNumber             | 02785498           |
      | administrativeRank       | 1                  |
      | isSubscriber             | false              |
      | quality                  | A                  |
      | lastName                 | MASON              |
      | firstName                | EDWIN              |
      | civility                 | Mr                 |
      | paymentRecipientId       | 2240266            |
      | beyondPaymentRecipientId | 2240266-0000401166 |
    And the result with contract id "0" has 0 domains

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations whith no domains
    Given I create a contrat from file "digital_contract_infos/contratTP_hasNothing"
    When I try to search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains ""
    Then an error "400" is returned with message "Required request parameter 'domains' for method parameter type String is not present"

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @digitalContractInfos @digitalContractInfosDomains
  Scenario: Get Digital Contract Informations with domain but contractTP doesn't exist
    When I try to search the contract informations for amc "0000401166", adherent "02785498", contract number "02785498", date "2022-04-01", domains "MEDE"
    Then an error "404" is returned with message "No contract found for this beneficiary"
