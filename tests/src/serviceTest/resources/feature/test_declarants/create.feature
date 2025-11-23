Feature: Get one declaration

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: I create a declarant via UI
    When I create a declarant from UI with parameters
      | numero | 0123456789 |
    When I get a declarant for UI with ID "0123456789"
    Then the declarant for UI with id "0123456789" exists

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: I create a declarant that exists already
    Given I create a declarant from UI with parameters
      | numero | 0123456789 |
    When I try to create a declarant from UI with parameters
      | numero | 0123456789 |
    Then an error "400" is returned

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: I create a declarant with duplicate ConventionTP
    When I try to create a declarant from UI with a file "declarant/declarantWithDuplicateConventionsTP"
    Then an error "400" is returned with message "Erreur: Convention TP non unique"

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: I create a declarant with duplicate CodeRenvoi
    When I try to create a declarant from UI with a file "declarant/declarantWithDuplicateCodesRenvoi"
    Then an error "400" is returned with message "Erreur: Code renvoi non unique"

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: I create a declarant with duplicate RegroupementDomainesTP
    When I try to create a declarant from UI with a file "declarant/declarantWithDuplicateRegroupements"
    Then an error "400" is returned with message "Erreur: Regroupement des domaines TP non unique"

  @todosmokeTests @smokeTestsWithoutKafka @declarant
  Scenario: I create a declarant with duplicate FondCarteTP
    When I try to create a declarant from UI with a file "declarant/declarantWithDuplicateFondsCarte"
    Then an error "400" is returned with message "Erreur: Fond de carte non unique"
