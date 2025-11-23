Feature: Test contract V5 with  resiliation

  Background:
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @caseResiliation
  Scenario: I send a contract with resiliation before 2023 : no TP rights
    When I send a test contract from file "generationDroitsTP_4943/resiliation_avant_2023"
    When I get triggers with contract number "MBA1075" and amc "0000401166"
    Then I wait for 0 declaration
    Then I get 1 trigger with contract number "MBA1075" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithWarnings |
      | amc            | 0000401166            |
      | nbBenef        | 2                     |
      | nbBenefKO      | 0                     |
      | nbBenefWarning | 2                     |

  @smokeTests @caseResiliation
  Scenario: I send a contract with resiliation in next year : TP rights up to resiliation date (resiliation %%NEXT_YEAR%%-10-10)
    When I send a test contract from file "generationDroitsTP_4943/resil_en_2026"
    When I get triggers with contract number "MBA1076" and amc "0000401166"
    Then I wait for 2 declarations
    Then I get 1 trigger with contract number "MBA1076" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline           |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-10-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-10-10 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline           |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-10-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-10-10 |

#  contrat résilié l'année courante => droits TP jusque date résile (ONLINE jusque date résiliation OFFLINE jusque fin année n-1)
  @smokeTests @caseResiliation
  Scenario: I send a contract with resiliation in current year : TP rights up to resiliation date (resiliation %%CURRENT_YEAR%%-10-10)
    When I send a test contract from file "generationDroitsTP_4943/resiliation_2025"
    When I get triggers with contract number "MBA1077" and amc "0000401166"
    Then I wait for 2 declarations
    Then I get 1 trigger with contract number "MBA1077" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-10-10 | %%CURRENT_YEAR%%-10-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-10-10 | %%CURRENT_YEAR%%-10-10 |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-10-10 | %%CURRENT_YEAR%%-10-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-10-10 | %%CURRENT_YEAR%%-10-10 |

  @smokeTests @caseResiliation
  Scenario: I send a contract with no resiliation, and the same contract resiliated in current year : contractTP contains resiliated date
    When I send a test contract from file "contractFor7168/noResiliation"
    When I wait for 1 declarations
    When I send a test contract from file "contractFor7168/resiliation_2025"
    When I wait for 2 declarations
    And The declaration with the indice 1 has this values
      | codeEtat        | R                      |
      | numeroPersonne  | 111537                 |
      | nomPorteur      | JANOH                  |
      | dateResiliation | %%CURRENT_YEAR%%/01/01 |
    And I wait for 1 contracts
    Then the consolidated contract has values
      | numeroContrat   | 1139211                |
      | idDeclarant     | 0000401166             |
      | dateResiliation | %%CURRENT_YEAR%%/01/01 |
