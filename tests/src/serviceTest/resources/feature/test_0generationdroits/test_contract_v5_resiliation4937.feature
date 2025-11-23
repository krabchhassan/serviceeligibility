Feature: Test contract V5 with  resiliation 4937

  Background:
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @caseResiliation4937
  Scenario: JIRA 4937 : I send a contract with resiliation in %%CURRENT_YEAR%% : TP rights up to resiliation date
    When I send a test contract from file "generationDroitsTP_4943/4937/resil_en_2025_contrat0"
    When I get triggers with contract number "MBA1476" and amc "0000401166"
    Then I wait for 2 declarations
    Then I get 1 trigger with contract number "MBA1476" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract from file "generationDroitsTP_4943/4937/resil_en_2025_contrat1"
    When I get triggers with contract number "MBA1476" and amc "0000401166"
    Then I wait for 6 declarations
    Then I get one more trigger with contract number "MBA1476" and amc "0000401166" and indice "1" for benef
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
    Then the different rightsDomains has this values with indice 3
      | periodeDebut          | %%CURRENT_YEAR%%-01-01 |
      | periodeFin            | %%CURRENT_YEAR%%-03-25 |
      | motifEvenement        | FE                     |
      | periodeFermetureDebut | %%CURRENT_YEAR%%-03-26 |
      | periodeFermetureFin   | %%CURRENT_YEAR%%-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
    Then the different rightsDomains has this values with indice 5
      | periodeDebut          | %%CURRENT_YEAR%%-03-10 |
      | periodeFin            | %%CURRENT_YEAR%%-03-25 |
      | motifEvenement        | FE                     |
      | periodeFermetureDebut | %%CURRENT_YEAR%%-03-26 |
      | periodeFermetureFin   | %%CURRENT_YEAR%%-12-31 |

    When I create a trigger by UI from "triggerByUi"
    Then I get one more trigger with contract number "MBA1476" and amc "0000401166" and indice "2" for benef
    Then I wait for 6 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
    Then the different rightsDomains has this values with indice 3
      | periodeDebut          | %%CURRENT_YEAR%%-01-01 |
      | periodeFin            | %%CURRENT_YEAR%%-03-25 |
      | motifEvenement        | FE                     |
      | periodeFermetureDebut | %%CURRENT_YEAR%%-03-26 |
      | periodeFermetureFin   | %%CURRENT_YEAR%%-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-25 | %%CURRENT_YEAR%%-03-25 |
    Then the different rightsDomains has this values with indice 5
      | periodeDebut          | %%CURRENT_YEAR%%-03-10 |
      | periodeFin            | %%CURRENT_YEAR%%-03-25 |
      | motifEvenement        | FE                     |
      | periodeFermetureDebut | %%CURRENT_YEAR%%-03-26 |
      | periodeFermetureFin   | %%CURRENT_YEAR%%-12-31 |


  @nosmokeTests @caseResiliation4937
  Scenario: JIRA 4937 : I send a contract with resiliation in %%NEXT_YEAR%% : TP rights up to resiliation date
    When I send a test contract from file "generationDroitsTP_4943/4937/resil_en_2025_contrat0"
    When I get triggers with contract number "MBA1476" and amc "0000401166"
    Then I wait for 2 declarations
    Then I get 1 trigger with contract number "MBA1476" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | null      |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "4937/contrat1" content

    When I send a test contract from file "generationDroitsTP_4943/4937/resil_en_2026_contrat3"
    When I get triggers with contract number "MBA1476" and amc "0000401166"
    Then I wait for 4 declarations
    Then I get one more trigger with contract number "MBA1476" and amc "0000401166" and indice "1" for benef
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                    | finOnline           |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-11-25 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-11-25 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | finOnline           |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-11-25 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-11-25 |
    Then the different rightsDomains has this values with indice 3
      | periodeDebut   | %%CURRENT_YEAR%%-03-10 |
      | periodeFin     | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement | DE                     |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "4937/contrat2" content
