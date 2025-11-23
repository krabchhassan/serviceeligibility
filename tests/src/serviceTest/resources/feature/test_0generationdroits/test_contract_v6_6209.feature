Feature:  BDDS - Réception d'un fermeture de garantie post renouvellement

  @smokeTests
  Scenario: BDDS - Réception d'un fermeture de garantie post renouvellement
    Given I create a contract element from a file "gt10"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract v6 from file "contratV6/6209"
    Then I get 1 trigger with contract number "6209-01" and amc "0000401166"

    Then I wait for 2 declarations

    When I send a test contract v6 from file "contratV6/6209-UnAssureFinDroit"
    Then I get 2 trigger with contract number "6209-01" and amc "0000401166"

    Then I wait for 4 declarations
