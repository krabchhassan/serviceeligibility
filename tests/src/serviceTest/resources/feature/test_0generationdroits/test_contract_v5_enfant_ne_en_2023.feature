Feature: Test contract V5 with  enfant ne en 2023

  # Enfant né en 2023 : reception contrat famille sans l’enfant, puis reception contrat avec nouvel enfant
  @smokeTests @caseChild
  Scenario: I send a contract with a child born in 2023 : one contract without the child and then one contract with the child
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/enfant_ne_2023"
    When I get triggers with contract number "MBA1074" and amc "0000401166"
    Then I wait for 2 declarations
    Then I get 1 trigger with contract number "MBA1074" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |
    Then the beneficiary has this values
      | dateNaissance | rangNaissance | numeroPersonne |
      | 19730711      | 1             | MBA10742       |
    Then the beneficiary has this values with indice 1
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20230310      | 1             | MBA10741       |
    And I wait for 1 contract
