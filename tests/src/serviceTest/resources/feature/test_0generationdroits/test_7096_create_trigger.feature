Feature: BLUE-7096 Création de déclencheur en anomalie lorque la réception d'une image contrat ne permet pas d'en créer

  @smokeTests @7096
  Scenario: Paramétrage BOBB erroné - CA2 - Exemple 1 avec 2 assurés
    Given I create a contract element from a file "gtbaloo_7096_ex1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA/IS |
      | libelle | Ka/Is |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA |
      | libelle | Ka |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract v6 from file "contratV6/7096-ex1"
    When I get triggers with contract number "5894-01" and amc "0000401166"
    Then I get 1 trigger with contract number "5894-01" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithErrors |
      | amc            | 0000401166          |
      | nbBenef        | 2                   |
      | nbBenefKO      | 2                   |
      | nbBenefWarning | 0                   |
    When I get the triggerBenef on the trigger with the index "0"
    Then the triggerBenef has this values
      | statut           | Error                      |
      | derniereAnomalie | Sas trouvé pour ce contrat |
      | nir              | 1800692014015              |
      | numeroPersonne   | 1234567895894-01           |
    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut           | Error                                                                    |
      | derniereAnomalie | La garantie BAL_5497_001 ne référence aucun produit depuis le 2024-07-01 |
      | nir              | 2000622222222                                                            |
      | numeroPersonne   | 1234567895894-02                                                         |

    When I wait for 0 declaration

  @smokeTests @7096 @release
  Scenario: Paramétrage BOBB erroné - CA2 - Exemple 2 avec 2 assurés
    Given I create a contract element from a file "gtbaloo_7096_ex2"
    Given I create a contract element from a file "gtbaloo_7096-2_ex2"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA/IS |
      | libelle | Ka/Is |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA |
      | libelle | Ka |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract v6 from file "contratV6/7096-ex2"
    When I get triggers with contract number "5894-01" and amc "0000401166"
    Then I get 1 trigger with contract number "5894-01" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithErrors |
      | amc            | 0000401166          |
      | nbBenef        | 2                   |
      | nbBenefKO      | 2                   |
      | nbBenefWarning | 0                   |
    When I get the triggerBenef on the trigger with the index "0"
    Then the triggerBenef has this values
      | statut           | Error                      |
      | derniereAnomalie | Sas trouvé pour ce contrat |
      | nir              | 1800692014015              |
      | numeroPersonne   | 1234567895894-01           |
    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut           | Error                                                                    |
      | derniereAnomalie | La garantie BAL_5497_002 ne référence aucun produit depuis le 2024-07-01 |
      | nir              | 2000622222222                                                            |
      | numeroPersonne   | 1234567895894-02                                                         |

    When I wait for 0 declaration

  @smokeTests @7096
  Scenario: Paramétrage BOBB erroné - CA2 - Exemple 3 avec 2 assurés (pas de paramétrage BOBB correspondant)
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA/IS |
      | libelle | Ka/Is |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA |
      | libelle | Ka |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract v6 from file "contratV6/7096-ex3"
    When I get triggers with contract number "5894-01" and amc "0000401166"
    Then I get 1 trigger with contract number "5894-01" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithErrors |
      | amc            | 0000401166          |
      | nbBenef        | 2                   |
      | nbBenefKO      | 2                   |
      | nbBenefWarning | 0                   |
    When I get the triggerBenef on the trigger with the index "0"
    Then the triggerBenef has this values
      | statut           | Error                                                                    |
      | derniereAnomalie | La garantie BAL_5497_003 ne référence aucun produit depuis le 2025-07-01 |
      | nir              | 1800692014015                                                            |
      | numeroPersonne   | 1234567895894-01                                                         |
    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut           | Error                      |
      | derniereAnomalie | Sas trouvé pour ce contrat |
      | nir              | 2000622222222              |
      | numeroPersonne   | 1234567895894-02           |

    When I wait for 0 declaration

  @smokeTests @7096
  Scenario: Paramétrage BOBB erroné - CA2 - Exemple 3 avec 2 assurés (paramétrage BOBB avec GT à ignorer)
    Given I create a contract element from a file "gtbaloo_7096_ex3_ignored"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSanté |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA/IS |
      | libelle | Ka/Is |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA |
      | libelle | Ka |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract v6 from file "contratV6/7096-ex3"
    When I get triggers with contract number "5894-01" and amc "0000401166"
    Then I get 1 trigger with contract number "5894-01" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithWarnings |
      | amc            | 0000401166            |
      | nbBenef        | 2                     |
      | nbBenefKO      | 0                     |
      | nbBenefWarning | 2                     |
    When I get the triggerBenef on the trigger with the index "0"
    Then the triggerBenef has this values
      | statut           | Warning                                             |
      | derniereAnomalie | L'ensemble des garanties de l'assuré sont à ignorer |
      | nir              | 1800692014015                                       |
      | numeroPersonne   | 1234567895894-01                                    |
    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut           | Warning                                             |
      | derniereAnomalie | L'ensemble des garanties de l'assuré sont à ignorer |
      | nir              | 2000622222222                                       |
      | numeroPersonne   | 1234567895894-02                                    |

    When I wait for 0 declaration
