Feature: Search contract PAU V3 -> V4

  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv4 @oldpauv3
  Scenario: Get pau TP_OFFLINE TDB
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/contractTDB"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |

  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv4 @oldpauv3
  Scenario: Get pau TP_OFFLINE (sans beneficiaire)
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/contractTDB"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2016-01-02' '2017-01-03' 0000401166 TP_OFFLINE
    Then I have a beneficiary without subscriber not found exception

  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv4 @oldpauv3
  Scenario: Get pau TP_OFFLINE (avec 1 contrat pas dans les dates requises )
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/contractTDBavecproduitdepasse"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2020-01-02' '2020-01-03' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception

  @todosmokeTests @pauv5
  Scenario: Get pau TP_OFFLINE with new field in contractCollective
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/contractWithRaisonSociale"
    Given I create a beneficiaire from file "contractForPauV5/beneficiairecollectiveContract"
    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    And the contract number 0 has these collectiveContract values
      | companyName    | ABC  |
      | number         | 01   |
      | externalNumber | 1234 |

  @todosmokeTests @pauv5
  Scenario: Get pau TP_ONLINE with new field in contractCollective
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/contractWithRaisonSociale"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    And the contract number 0 has these collectiveContract values
      | companyName    | ABC  |
      | number         | 01   |
      | externalNumber | 1234 |


  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv5
  Scenario: Get PAU V5 TP_OFFLINE mari et femme
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contratTDB-femme"
    And I create a contrat from file "contractForPauV5/contratTDB-mari"
    And I create a beneficiaire from file "contractForPauV5/01-benef-mari"
    And I create a beneficiaire from file "contractForPauV5/01-benef-femme"
    When I get contrat PAUV5 OTP for 1810131111110 '19810101' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE A0001
    Then In the PAU, there is 2 contract
    Then the contract number 0 data has values
      | number | A00010 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-00012 |
    Then the contract number 1 data has values
      | number | A00011 |
    Then the contract number 1 data has this personNumber
      | personNumber | 0000401166-0011 |

#  BLUE-4805
  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv4
  Scenario: Get pau TP_ONLINE
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV4/createContratTP"
    Given I create a beneficiaire from file "contractForPauV4/beneficiaire"
    When I get contrat PAUV5 OTP for 1791062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    When I get contrat PAUV5 OTP for 1791062498045 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE
    Then With this request I have a beneficiary not found without subscriber exception
    When I get contrat PAUV5 OTP for 1791062498045 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE MBA0003
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    When I get contrat PAUV5 OTP for 1791062498045 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE MBA0004
    Then With this request I have a beneficiary not found exception

  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv4
  Scenario: Test contract error 'Droits non ouverts' pau TP_ONLINE
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV4/createContratWithoutOpenRights"
    Given I create a beneficiaire from file "contractForPauV4/beneficiaire"
    When I get contrat PAUV5 OTP for 1791062498044 '20041026' 1 '2023-12-24' '2023-12-24' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    When I get contrat PAUV5 OTP for 1791062498044 '20041026' 1 '2023-12-24' '2023-12-24' 0000401166 TP_ONLINE for domains OPT
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    When I get contrat PAUV5 OTP for 1791062498044 '20041026' 1 '2023-01-24' '2023-01-24' 0000401166 TP_ONLINE MBA0002 for domains OPT
    Then With this request I have a contract without any rights open exception
    When I get contrat PAUV5 OTP for 1791062498044 '20041026' 1 '2023-01-24' '2023-01-24' 0000401166 TP_ONLINE for domains PHAR
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
      | subscriberId      | MBA0003    |
