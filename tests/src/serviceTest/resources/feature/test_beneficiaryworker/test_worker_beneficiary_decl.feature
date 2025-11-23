Feature: Consume a message and verify value is contained in db in the correct format

  @todosmokeTests @smokeTestsWithoutKafka @declaration @benefworker
  Scenario: I send a declaration and the benef will be created
    When I create a declaration from a file "benefworker/case1_decl1"
    Given I wait "3" seconds in order to consume the data
    Then I get all benef from the database
    Then The benef is identical to "case1_expected_benef" content

  @todosmokeTests @smokeTestsWithoutKafka @declaration @benefworker
  Scenario: I send declarations with more contracts and the benef will be created / updated
    When I create a declaration from a file "benefworker/case1_decl1"
    When I create a declaration from a file "benefworker/case2_decl1"
    Given I wait "3" seconds in order to consume the data
    Then I get all benef from the database
    Then The benef is identical to "case2_expected_benef" content
