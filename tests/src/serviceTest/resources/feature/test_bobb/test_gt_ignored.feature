Feature: Generate TP rights with Bobb ignored

  Background:
    Given I create a contract element from a file "gtaxa_ignored"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @gtIgnored
  Scenario: Ignored warranties
    When I send a test contract from file "contratV5/contrat_ouvert2025"
    Then I get 1 trigger with contract number "MBA11754" and amc "0000401166"
    Then I wait for 0 declaration

  @smokeTests @gtIgnored @release
  Scenario: Ignored warranty + normal warranty
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/debutGarantie/2_garanties_une_debutant_en_2025"
    Then I wait for 1 declarations
    Then The declaration number 0 has codeEtat "V"
    Then I get 1 trigger with contract number "MBA12756" and amc "0000401166"
    Then there is 6 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie  | domaine | debut                  | fin                    | finOnline |
      | GT_BLUE_1 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
