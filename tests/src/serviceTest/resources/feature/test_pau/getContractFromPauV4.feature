Feature: Search contract PAU V4

#  BLUE-4805
  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get pau TP_ONLINE
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/createContratTP"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 for 1791062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    When I get contrat PAUV5 for 1791062498045 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE
    Then With this request I have a beneficiary not found without subscriber exception
    When I get contrat PAUV5 for 1791062498045 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE MBA0003
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    When I get contrat PAUV5 for 1791062498045 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE MBA0004
    Then With this request I have a beneficiary not found exception

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Test basic contract search - first iteration
    # Create beneficiaries
    Given I drop the collection for Beneficiary
    Given I create a beneficiaire from file "contractForPauV5/createBenef01"
    Given I create a beneficiaire from file "contractForPauV5/createBenef02"
    Given I create a beneficiaire from file "contractForPauV5/createBenef03"
    Given I create a beneficiaire from file "contractForPauV5/createBenef04"

    # Create contracts
    Given I drop the collection for Contract
    Given I create a contrat from file "contractForPauV5/createContratConsolide001-01"
    Given I create a contrat from file "contractForPauV5/createContratConsolide002-01"

    # Call UAP
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE
    Then we found 2 contracts
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
      | suspensionPeriods | null       |
      | number            | 001        |
    Then the contract number 1 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
      | suspensionPeriods | null       |
      | number            | 002        |

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Test basic contract search - Second iteration
    # Create beneficiaries
    Given I drop the collection for Beneficiary
    Given I create a beneficiaire from file "contractForPauV5/createBenef01"
    Given I create a beneficiaire from file "contractForPauV5/createBenef02"
    Given I create a beneficiaire from file "contractForPauV5/createBenef03"
    Given I create a beneficiaire from file "contractForPauV5/createBenef04"

    # Create contracts
    Given I drop the collection for Contract
    Given I create a contrat from file "contractForPauV5/createContratConsolide001-01"
    Given I create a contrat from file "contractForPauV5/createContratConsolide002-01"

    # Call UAP
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401167 TP_ONLINE
    Then we found 2 contracts
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
      | suspensionPeriods | null       |
      | number            | 001        |
    Then the contract number 1 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
      | suspensionPeriods | null       |
      | number            | 002        |

    # Call UAP
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401167 TP_ONLINE MBA0001
    Then we found 2 contracts
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
      | suspensionPeriods | null       |
      | number            | 001        |
    Then the contract number 1 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
      | suspensionPeriods | null       |
      | number            | 002        |

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Test contract error 'Type de dépense non ouvert' pau TP_ONLINE
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/createContratWithoutRight"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 for 1791062498044 '20041026' 1 '2023-12-24' '2023-12-24' 0000401166 TP_ONLINE MBA0002 for domains OPT
    Then With this request I have a contract without any rights open for this domain exception
    When I get contrat PAUV5 for 1791062498044 '20041026' 1 '2023-01-24' '2023-01-24' 0000401166 TP_ONLINE for domains PHAR
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Test contract error pau TP_ONLINE
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 for 1791062498044 '20041026' 1 '2023-12-24' '2023-12-24' 0000401166 TP_ONLINE for domains OPT
    Then With this request I have a contract resiliated exception

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Fail to get benef because numeroAdherent is missing
    Given I drop the collection for Beneficiary
    Given I create a beneficiaire from file "contractForPauV5/createBenef01"
    Given I create a beneficiaire from file "contractForPauV5/createBenef02"
    Given I create a beneficiaire from file "contractForPauV5/createBenef03"
    Given I create a beneficiaire from file "contractForPauV5/createBenef04"
    When I get contrat PAUV5 for 1791062498044 '20041026' 1 '2023-12-24' '2023-12-24' 0000401166 TP_ONLINE
    Then I have a beneficiary without subscriber not found exception

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Test contract error pau TP_ONLINE
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary

    Given I create a beneficiaire from file "contractForPauV5/createBenef01"
    Given I create a beneficiaire from file "contractForPauV5/createBenef02"
    Given I create a beneficiaire from file "contractForPauV5/createBenef03"
    Given I create a beneficiaire from file "contractForPauV5/createBenef04"

    # Create contracts
    Given I drop the collection for Contract
    Given I create a contrat from file "contractForPauV5/createContratSamePeriod1"
    Given I create a contrat from file "contractForPauV5/createContratSamePeriod2"
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '2021-01-01' '2021-02-01' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception


  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Test muliple benef on contract
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary

    Given I create a beneficiaire from file "contractForPauV5/createBenefFamily1"
    Given I create a beneficiaire from file "contractForPauV5/createBenefFamily2"

    # Create contracts
    # contrat 1 -> toute la famille (numero adherent 22091400000020)
    # contrat 2 -> les epoux (numero adherent 22091400000021)
    # contrat 3 -> le mari seul (numero adherent 22091400000021) qui débute après le contrat 1 au niveau des periodes de droit
    Given I drop the collection for Contract
    Given I create a contrat from file "contractForPauV5/createContractFamily1"
    Given I create a contrat from file "contractForPauV5/createContractFamily2"
    Given I create a contrat from file "contractForPauV5/createContractFamily3"
    When I get contrat PAUV5 for 1770910079771 '19770910' 1 '2022-02-01' '2022-06-01' 0000401166 TP_ONLINE
    Then we found 2 contracts
    Then the contract number 0 data has values
      | insurerId         | 0000401166         |
      | subscriberId      | ADH_22091400000021 |
      | number            | 22091400000021     |
      | suspensionPeriods | null               |
    Then the contract number 1 data has values
      | insurerId         | 0000401166         |
      | subscriberId      | ADH_22091400000021 |
      | number            | 22091400000022     |
      | suspensionPeriods | null               |

    When I get contrat PAUV5 for 2890327887247 '19890404' 1 '2022-08-01' '2023-12-01' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166         |
      | subscriberId      | ADH_22091400000020 |
      | number            | 22091400000020     |
      | suspensionPeriods | null               |

  @smokeTests @pauv5
  Scenario: Get pauv5 TP_ONLINE to check there isn't label in contractCollective
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/createContratTP"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 for 1791062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    And the contract number 0 hasn't companyName in collectiveContract

  @smokeTests @pauv5
  Scenario: Get pauv5 TP_OFFLINE to check there isn't label in contractCollective
    Given I drop the collection for Contract
    Given I drop the collection for Beneficiary
    Given I create a contrat from file "contractForPauV5/createContratTP"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaire"
    When I get contrat PAUV5 for 1791062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    And the contract number 0 hasn't companyName in collectiveContract

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: TP_ONLINE choose between two subscriber on same period
    # Create beneficiaries
    Given I drop the collection for Beneficiary
    Given I create a beneficiaire from file "contractForPauV5/createBenef01"
    Given I create a beneficiaire from file "contractForPauV5/createBenef02"
    Given I create a beneficiaire from file "contractForPauV5/createBenef03"
    Given I create a beneficiaire from file "contractForPauV5/createBenef04"

    # Create contracts
    Given I drop the collection for Contract
    Given I create a contrat from file "contractForPauV5/createContratConsolide001-01"
    Given I create a contrat from file "contractForPauV5/createContratConsolide003-01"

    # Call UAP
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '2022-12-24' '2022-12-24' 0000401166 TP_ONLINE
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
      | number            | 001        |
      | suspensionPeriods | null       |