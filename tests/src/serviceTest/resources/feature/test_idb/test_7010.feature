Feature: Test interrogation des droits des bénéficiaires

  Background:
    Given I drop the collection for Contract

  @smokeTests @clc @7010
  Scenario: Test CLC même benef avec numéro de personne différent
    Then I create a declarant from a file "declarantbaloo"
    Then I create a contrat from file "idb/v1/contrat_7010"
    Then I post rest request from file "idb/v1/request_7010" to the "CLC" endpoint
    And the expected response is identical to "idb/v1/response_7010" content
