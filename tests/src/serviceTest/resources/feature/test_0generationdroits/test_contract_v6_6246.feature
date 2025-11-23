Feature:  BDDS - Réception d'une image contrat avec une fermeture de garantie pour l'année n-1

  Background:
    Given I create a contract element from a file "gt10"
    And I create a contract element from a file "gtbaloo5515"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @6246
  Scenario: Cas 1 Réception image avec un assuré et 1 GT. Puis réception du même contrat avec la GT fermée au 31/12/N-1
    When I send a test contract v6 from file "contratV6/6246/base_cas1"
    Then I wait for 1 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas1"
    Then I wait for 2 declarations
    And The declaration number 1 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |

  @smokeTests @6246
  Scenario: Cas 2 Réception image avec un assuré et 2 GT. Puis réception du même contrat avec 1 des GT fermées au 31/12/N-1
    When I send a test contract v6 from file "contratV6/6246/base_cas2_cas3"
    Then I wait for 1 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas2"
    Then I wait for 3 declarations
    And The declaration number 1 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    And The declaration number 2 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |


  @smokeTests @6246
  Scenario: Cas 3 Réception image avec un assuré et 2 GT. Puis réception du même contrat avec les 2 GT fermées au 31/12/N-1
    When I send a test contract v6 from file "contratV6/6246/base_cas2_cas3"
    Then I wait for 1 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas3"
    Then I wait for 2 declarations
    And The declaration number 1 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |


  @smokeTests @6246
  Scenario: Cas 4 Réception image avec 2 assurés et 1 GT chacun. Puis réception du même contrat avec la GT fermée au 31/12/N-1 pour un des assurés
    When I send a test contract v6 from file "contratV6/6246/base_cas4_cas5"
    Then I wait for 2 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas4"
    Then I wait for 4 declarations
    And The declaration number 2 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And The declaration number 3 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |


  @smokeTests @6246
  Scenario: Cas 5 Réception image avec 2 assurés et 1 GT chacun. Puis réception du même contrat avec la GT fermée au 31/12/N-1 pour les 2 assurés
    When I send a test contract v6 from file "contratV6/6246/base_cas4_cas5"
    Then I wait for 2 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas5"
    Then I wait for 4 declarations
    And The declaration number 2 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    And The declaration number 3 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |


  @smokeTests @6246
  Scenario: Cas 6 Réception image avec 2 assurés et 2 GT chacun. Puis réception du même contrat avec 1 des GT fermée au 31/12/N-1 pour un des assurés
    When I send a test contract v6 from file "contratV6/6246/base_cas6_cas7_cas8_cas9"
    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas6"
    Then I wait for 5 declarations
    And The declaration number 2 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And The declaration number 3 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    And The declaration number 4 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |


  @smokeTests @6246
  Scenario: Cas 7 Réception image avec 2 assurés et 2 GT chacun. Puis réception du même contrat avec 1 des GT fermée au 31/12/N-1 pour les 2 assurés
    When I send a test contract v6 from file "contratV6/6246/base_cas6_cas7_cas8_cas9"
    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas7"
    Then I wait for 6 declarations
    And The declaration number 2 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    And The declaration number 3 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And The declaration number 4 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    And The declaration number 5 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |


  @smokeTests @6246
  Scenario: Cas 8 Réception image avec 2 assurés et 2 GT chacun. Puis réception du même contrat avec les 2 GT fermée au 31/12/N-1 pour un des assurés
    When I send a test contract v6 from file "contratV6/6246/base_cas6_cas7_cas8_cas9"
    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas8"
    Then I wait for 4 declarations
    And The declaration number 2 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And The declaration number 3 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |

  @smokeTests @6246
  Scenario: Cas 9 Réception image avec 2 assurés et 2 GT chacun. Puis réception du même contrat avec les 2 GT fermée au 31/12/N-1 pour les 2 assurés
    When I send a test contract v6 from file "contratV6/6246/base_cas6_cas7_cas8_cas9"
    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I send a test contract v6 from file "contratV6/6246/fin_2023_cas9"
    Then I wait for 4 declarations
    And The declaration number 2 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    And The declaration number 3 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BLO1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
