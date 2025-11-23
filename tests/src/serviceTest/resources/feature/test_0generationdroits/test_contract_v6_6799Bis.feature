Feature: Contract Element avec le même product element valide et annule

  Background:
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @6799
  Scenario: Réception image avec un assuré et 1 GT contenant un productElement par nature de prestation -> ne doit pas générer un domaineTP pour chaque productEl
    Given I create a contract element from a file "gtBenefitNature"
    When I send a test contract v6 from file "contratV6/createServicePrest_GTBenefitsNatures"
    Then I wait for 1 declarations
    # Avec le bug, on a 24 domaines (4 x chacun des domaines renvoyé par PW (PHCO, OPTI, AUDI, DENT, PHNO, PHOR)) --> au lieu d'avoir les 6 domaines une seule fois
    Then there is 6 rightsDomains and the different rightsDomains has this values
      | garantie  | domaine | debut                  | fin                    | finOnline |
      | GT_NATURE | PHCO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_NATURE | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_NATURE | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_NATURE | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_NATURE | PHNO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_NATURE | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |



