Feature: Consume a message and verify value is contained in db

  @todosmokeTests @smokeTestsWithKafka @benef @benefworker
  Scenario: I send a benef event and the benef will be created in the database
    When I send a message from source ServicePrestation file case3_benef1
    Given I wait "3" seconds in order to consume the data
    Then I get all benef from the database
    Then The benef is identical to "case3_expected_benef" content

  @todosmokeTests @smokeTestsWithKafka @benef @benefworker
  Scenario: I send benef will be created / updated (different issuingCompany for the contracts of the benef)
    When I send a message from source ServicePrestation file case3_benef1
    When I send a message from source ServicePrestation file case4_benef1
    Given I wait "3" seconds in order to consume the data
    Then I get all benef from the database
    Then The benef is identical to "case4_expected_benef" content

  @todosmokeTests @smokeTestsWithKafka @benef @benefworker
  Scenario: I send benef will be created / updated (same issuingCompany for the contracts of the benef)
    When I send a message from source ServicePrestation file case3_benef1
    When I send a message from source ServicePrestation file case5_benef1
    Given I wait "3" seconds in order to consume the data
    Then I get all benef from the database
    Then The benef is identical to "case5_expected_benef" content

  @todosmokeTests @smokeTestsWithKafka @benef @benefworker
  Scenario: I send benef will be created / updated (same issuingCompany for the contracts of the benef + contracts with consecutive periods)
    When I send a message from source ServicePrestation file case3_benef1
    When I send a message from source ServicePrestation file case6_benef1
    Given I wait "3" seconds in order to consume the data
    Then I get all benef from the database
    Then The benef is identical to "case6_expected_benef" content

