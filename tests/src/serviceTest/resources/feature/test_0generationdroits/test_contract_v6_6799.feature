Feature: Contract Element avec le même product element valide et annule

  Background:
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @6799
  Scenario: Cas 1 Réception image avec un assuré et 1 GT. Valide sur l'annee et annule en juin
    Given I create a contract element from a file "gtValideAnnule"
    When I send a test contract v6 from file "contratV6/base_6799"
    Then I wait for 1 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |


  @smokeTests @6799
  Scenario: Cas 1 Réception image avec un assuré et 1 GT. P1 couvre de 01/01/2025 au 01/05/%%CURRENT_YEAR%% & 02/07/2025 à null, P2 couvre le trou de 02/05/2025 au 01/07/2025
    Given I create a contract element from a file "gtTrous"
    When I send a test contract v6 from file "contratV6/base_6799"
    Then I wait for 1 declarations
    Then there is 15 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline              |
      | GT_10    | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-01 | %%CURRENT_YEAR%%-05-01 |
      | GT_10    | OPTI    | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_10    | OPTI    | %%CURRENT_YEAR%%-05-02 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-01 | %%CURRENT_YEAR%%-05-01 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-05-02 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 |
      | GT_10    | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-01 | %%CURRENT_YEAR%%-05-01 |
      | GT_10    | DENT    | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_10    | DENT    | %%CURRENT_YEAR%%-05-02 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 |
      | GT_10    | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-01 | %%CURRENT_YEAR%%-05-01 |
      | GT_10    | PHAR    | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_10    | PHAR    | %%CURRENT_YEAR%%-05-02 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 |
      | GT_10    | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-01 | %%CURRENT_YEAR%%-05-01 |
      | GT_10    | APDE    | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_10    | APDE    | %%CURRENT_YEAR%%-05-02 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 |


  @smokeTests @6799
  Scenario: Cas 1 Réception image avec un assuré et 1 GT. Valide sur l'annee et annule en juin
    Given I create a contract element from a file "gtContigue"
    When I send a test contract v6 from file "contratV6/base_6799"
    Then I wait for 1 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
