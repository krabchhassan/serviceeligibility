Feature: get issuingCompanyCode to blb
  get insurer identifier from priority contract to blb

  @todosmokeTests @smokeTestsWithoutKafka @priorityContract
  Scenario: Get issuingCompanyCode id with context TP_ONLINE/TP_OFFLINE without subscriberId and domain
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas1-contractEvent"
    And I create a contrat from file "contractForPauV5/cas1-contractEvent2"
    And I create a service prestation from a file "pauv5-cas1-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00011"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00013"
    When I get issuingCompanyCode and beneficiaryId for '1591275115460' '19621023' '1' '2023-10-02' 'TP_ONLINE'
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00012
    When I get issuingCompanyCode and beneficiaryId for '1591275115460' '19621023' '1' '2023-10-02' 'TP_OFFLINE'
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00012

  @todosmokeTests @smokeTestsWithoutKafka @priorityContract
  Scenario: Get issuingCompanyCode id with context TP_ONLINE/TP_OFFLINE with subscriberId and/or domain
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas1-contractEvent"
    And I create a contrat from file "contractForPauV5/cas1-contractEvent2"
    And I create a service prestation from a file "pauv5-cas1-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00011"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00013"
    When I get issuingCompanyCode and beneficiaryId for '1591275115460' '19621023' '1' '2023-10-02' 'TP_ONLINE' with domain DENT
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00011
    When I get issuingCompanyCode and beneficiaryId for '1591275115460' '19621023' '1' '2023-10-02' 'TP_ONLINE' with subscriberId C00010 with domain DENT
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00011
    When I get issuingCompanyCode and beneficiaryId for '1591275115460' '19621023' '1' '2023-10-02' 'TP_OFFLINE' with domain DENT
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00012
    When I get issuingCompanyCode and beneficiaryId for '1591275115460' '19621023' '1' '2023-10-02' 'TP_OFFLINE' with subscriberId C00010 with domain DENT
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00012

  @todosmokeTests @smokeTestsWithoutKafka @priorityContract
  Scenario: Get all controller parameters errors case
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas2-contratEvent"
    And I create a contrat from file "contractForPauV5/cas2-contratEvent2"
    And I create a contrat from file "contractForPauV5/cas2-contratEvent3"
    And I create a contrat from file "contractForPauV5/cas2-contratEvent4"
    And I create a service prestation from a file "pauv5-cas2-contrat1-servicePrestation"
    And I create a service prestation from a file "pauv5-cas2-contrat2-servicePrestation"
    And I create a service prestation from a file "pauv5-cas2-contrat3-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauCas2"
    When I get issuingCompanyCode and beneficiaryId for '' '19621021' '1' '2023-10-02' 'TP_ONLINE' with domain DENT
    Then an error "400" is returned with message "Le code nir doit être renseigné"
    When I get issuingCompanyCode and beneficiaryId for '145222' '' '1' '2023-10-02' 'TP_ONLINE' with domain DENT
    Then an error "400" is returned with message "La date de naissance doit être renseignée"
    When I get issuingCompanyCode and beneficiaryId for '145222' '747588' '' '2023-10-02' 'TP_ONLINE' with domain DENT
    Then an error "400" is returned with message "Le rang de naissance doit être renseigné"
    When I get issuingCompanyCode and beneficiaryId for '' '19621021' '1' '2023-10-02' 'TP_OFFLINE' with domain DENT
    Then an error "400" is returned with message "Le code nir doit être renseigné"
    When I get issuingCompanyCode and beneficiaryId for '145222' '' '1' '2023-10-02' 'TP_OFFLINE' with domain DENT
    Then an error "400" is returned with message "La date de naissance doit être renseignée"
    When I get issuingCompanyCode and beneficiaryId for '145222' '1452366' '' '2023-10-02' 'TP_OFFLINE' with domain DENT
    Then an error "400" is returned with message "Le rang de naissance doit être renseigné"
    When I get issuingCompanyCode and beneficiaryId for '145222' '1452366' '1' '2023-10-02' 'unknow' with domain DENT
    Then an error "400" is returned with message "Le contexte 'unknow' est inconnu. Les contextes permis sont 'TP_ONLINE' et 'TP_OFFLINE'."
    When I get issuingCompanyCode and beneficiaryId for '145222' '1962-10-23' '1' '2023-10-02' 'TP_OFFLINE' with domain DENT
    Then an error "400" is returned with message "La date de naissance 1962-10-23 n'est pas valide"
    When I get issuingCompanyCode and beneficiaryId for '145222' '19621023' '1' '2023/10/02' 'TP_OFFLINE' with domain DENT
    Then an error "400" is returned with message "'startDate' avec la valeur '2023/10/02' ne respecte pas le format 'yyyy-MM-dd' ."
    When I get issuingCompanyCode and beneficiaryId for '145222' '19621023' '1' '20231002' 'TP_OFFLINE' with domain DENT
    Then an error "400" is returned with message "'startDate' avec la valeur '20231002' ne respecte pas le format 'yyyy-MM-dd' ."

  @todosmokeTests @smokeTestsWithoutKafka @priorityContract
  Scenario: Get all functional errors case
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas3-contratEvent"
    And I create a contrat from file "contractForPauV5/cas3-contratEvent2"
    And I create a service prestation from a file "pauv5-cas1-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00031"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00032"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00033"
    When I get issuingCompanyCode and beneficiaryId for '1591275115460' '19621023' '1' '2022-10-02' 'TP_ONLINE'
    # BLUE-5911
    Then With this request I have this issuingCompanyCode IGestion
    When I get issuingCompanyCode and beneficiaryId for '1591275115461' '19621023' '1' '2023-10-02' 'TP_ONLINE'
    Then an error "400" is returned with message "Veuillez faire la recherche de droits en renseignant le n° d’adhérent"

  @todosmokeTests @smokeTestsWithoutKafka
  Scenario: Get insurer id with bad nir and good subscriber id
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas3-contratEvent"
    And I create a contrat from file "contractForPauV5/cas3-contratEvent2"
    And I create a service prestation from a file "pauv5-cas1-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00031"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00032"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00033"
    When I get issuingCompanyCode and beneficiaryId for '1591275115461' '19621023' '1' '2023-01-02' 'TP_ONLINE' with subscriberId C00030
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00032
    When I get issuingCompanyCode and beneficiaryId for '1591275115461' '19621023' '1' '2023-01-02' 'TP_OFFLINE' with subscriberId C00030
    Then With this request I have this issuingCompanyCode IGestion
    And With this request I have this beneficiaryId 0000401166-P00032

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°1 a
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-2"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2024-06-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS3

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°1 b
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-2"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2023-01-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS1

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°1 c
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-2"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2023-05-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS2

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°1 d
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-2"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2000-01-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS1

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°2 a
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023-2"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-3"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-4"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2024-04-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS3

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°2 b
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023-2"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-3"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-4"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2023-01-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS1

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°2 c
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023-2"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-3"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-4"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2024-05-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS3

  @todosmokeTests @priorityContract
  Scenario: Test to check issuingCompanyCode UseCase n°2 d
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2023-2"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-3"
    And I create a contrat from file "contractForPauV5/contrat-2benef-2024-4"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire1-contratOC"
    When I get issuingCompanyCode and beneficiaryId for '2800619784004' '19800615' '1' '2000-01-01' 'TP_ONLINE' with subscriberId 14725497
    Then With this request I have this issuingCompanyCode OS1
