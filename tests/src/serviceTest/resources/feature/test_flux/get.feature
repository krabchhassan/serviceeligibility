Feature: Get card

  @@todosmokeTests @smokeTestsWithoutKafka @flux
  Scenario: Get one specific flux
    Given I import a complete file
    When I get flux with parameters
      | fichierEmis  | true       |
      | processus    | Aiguillage |
      | numberByPage | 10         |
      | typeFichier  | DCLBEN     |
    Then 1 flux is returned
    Then I received a flux with the type file "DCLBEN", the declarant id "0000000001" and the processus "Aiguillage"

  @@todosmokeTests @smokeTestsWithoutKafka @flux
  Scenario: Trying to get flux without fichierEmis
    Given I import a complete file
    When I get flux with parameters
      | processus    | Aiguillage |
      | numberByPage | 10         |
      | typeFichier  | DCLBEN     |
    Then 0 flux is returned
