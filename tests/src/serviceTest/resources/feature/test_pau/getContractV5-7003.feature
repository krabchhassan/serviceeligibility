Feature: Search contract PAU V5 BLUE-7003

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Test contract search in the case of a divorced family, call with the mother's nir and the child's birthDate
    # Create beneficiaries
    Given I drop the collection for Beneficiary
    Given I create a beneficiaire from file "contractForPauV5/createBenefPere"
    Given I create a beneficiaire from file "contractForPauV5/createBenefMere"
    Given I create a beneficiaire from file "contractForPauV5/createBenefEnfant"

    # Create contracts
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "servicePrestation_pere"
    Given I create a service prestation from a file "servicePrestation_mere"

    # Call UAP with mother's nir and child's birth date
    When I get contrat PAUV5 for 1041062498045 '20041026' 1 '2022-12-24' '2025-12-24' 0000401166 HTP
    Then we found 2 contracts
    # Mother's contract
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | OOA0001    |
    # Father's contract
    Then the contract number 1 data has values
      | insurerId         | 0000401166 |
      | subscriberId      | MBA0001    |
