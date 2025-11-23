Feature: Test l'ordre des domaines suivant le parametrage

  @smokeTests
  Scenario: Test ordre des droits
    And I create a contract element from a file "gtaxa_cgdiv-5726"
    And I create a contract element from a file "gtaxa_cgdiv-5726_labo"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageBalooHospLaboOpau"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "6574"
    Then I wait for 1 declaration
    And The declaration has domains with noOrdreDroit
      | code | order |
      | HOSP | 1     |
      | LABO | 2     |
      | OPAU | 3     |
