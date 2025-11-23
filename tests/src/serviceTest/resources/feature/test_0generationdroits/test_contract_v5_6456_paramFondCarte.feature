Feature: Paramétrage de fond de carte TP

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |

  @smokeTests @caseFondCarte
  Scenario: Parametrage de fondCarte en fonction du paramétrage AMC
    And I create a contract element from a file "gt_5660"
    And I create a declarant from a file "declarantbaloo_withFondCarte"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "6456-Contrat1"
    Then I wait for 1 declaration
    And I process declarations for carteDemat the "2025-01-01"
    And I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-54582311001 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
      | fondCarte        | fondCarte.jpeg         |
    Then I wait for 1 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166             |
      | nomAMC       | BALOO                  |
      | libelleAMC   | BALOO                  |
      | periodeDebut | %%CURRENT_YEAR%%/01/01 |
      | periodeFin   | %%CURRENT_YEAR%%/12/31 |
      | fondCarte    | fondCarte.jpeg         |

  @smokeTests @caseFondCarte
  Scenario: Parametrage de fondCarte en fonction du paramétrage AMC, mais plusieurs fondsCarte sont présents et fond référence à différents fonds de cartes
    And I create a contract element from a file "gt_5660"
    And I create a declarant from a file "declarantbaloo_withDifferentFondsCartes"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "6456-Contrat1"
    Then I wait for 1 declaration
    And I process declarations for carteDemat the "2025-01-01"
    And I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-54582311001 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
      | fondCarte        | null                   |
    Then I wait for 1 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166             |
      | nomAMC       | BALOO                  |
      | libelleAMC   | BALOO                  |
      | periodeDebut | %%CURRENT_YEAR%%/01/01 |
      | periodeFin   | %%CURRENT_YEAR%%/12/31 |
      | fondCarte    | null                   |

  @smokeTests @caseFondCarte
  Scenario: Parametrage de fondCarte en fonction du paramétrage AMC, mais plusieurs fondsCarte sont présents et fond référence à un même fond de carte
    And I create a contract element from a file "gt_5660"
    And I create a declarant from a file "declarantbaloo_withSameFondsCartes"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "6456-Contrat1"
    Then I wait for 1 declaration
    And I process declarations for carteDemat the "2025-01-01"
    And I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-54582311001 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
      | fondCarte        | fondCarte.jpeg         |
    Then I wait for 1 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166             |
      | nomAMC       | BALOO                  |
      | libelleAMC   | BALOO                  |
      | periodeDebut | %%CURRENT_YEAR%%/01/01 |
      | periodeFin   | %%CURRENT_YEAR%%/12/31 |
      | fondCarte    | fondCarte.jpeg         |

  @smokeTests @caseFondCarte
  Scenario: Parametrage de fondCarte en fonction du paramétrage AMC "Sans réseau de soin"
    And I create a contract element from a file "gt_5660_noReseauSoin"
    And I create a declarant from a file "declarantbaloo_withFondCarteSansReseauSoin"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "6456-Contrat1"
    Then I wait for 1 declaration
    And I process declarations for carteDemat the "2025-01-01"
    And I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-54582311001       |
      | isLastCarteDemat | true                         |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01       |
      | periodeFin       | %%CURRENT_YEAR%%/12/31       |
      | fondCarte        | fondCarteSansReseauSoin.jpeg |
    Then I wait for 1 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166                   |
      | nomAMC       | BALOO                        |
      | libelleAMC   | BALOO                        |
      | periodeDebut | %%CURRENT_YEAR%%/01/01       |
      | periodeFin   | %%CURRENT_YEAR%%/12/31       |
      | fondCarte    | fondCarteSansReseauSoin.jpeg |

  @smokeTests @caseFondCarte
  Scenario: Parametrage de fondCarte si aucun paramétrage générationDroitsTP et aucun paramétrage dans l'AMC
    Given I create a contract element from a file "gt_5660_noReseauSoin"
    And I create a declarant from a file "declarantbalooEditable"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "6456-Contrat1"
    Then I wait for 1 declaration
    And I process declarations for carteDemat the "2025-01-01"
    And I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-54582311001 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
      | fondCarte        | null                   |
    Then I wait for 1 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166             |
      | nomAMC       | BALOO                  |
      | libelleAMC   | BALOO                  |
      | periodeDebut | %%CURRENT_YEAR%%/01/01 |
      | periodeFin   | %%CURRENT_YEAR%%/12/31 |
      | fondCarte    | null                   |

  @smokeTests @caseFondCarte
  Scenario: Parametrage de fondCarte mais changement de réseaux de soins en cours d’année
    Given I create a contract element from a file "gt_5660_multiple"
    And I create a declarant from a file "declarantbaloo_withFondCarte"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "6456-Contrat1"
    Then I wait for 1 declaration
    And I process declarations for carteDemat the "2025-01-01"
    And I wait for 2 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-54582311001 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/02/01 |
      | fondCarte        | null                   |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-54582311001 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/02/02 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
      | fondCarte        | null                   |
    Then I wait for 2 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166             |
      | nomAMC       | BALOO                  |
      | libelleAMC   | BALOO                  |
      | periodeDebut | %%CURRENT_YEAR%%/01/01 |
      | periodeFin   | %%CURRENT_YEAR%%/02/01 |
      | fondCarte    | null                   |
    Then the carte papier at index 1 has these values
      | numeroAMC    | 0000401166             |
      | nomAMC       | BALOO                  |
      | libelleAMC   | BALOO                  |
      | periodeDebut | %%CURRENT_YEAR%%/02/02 |
      | periodeFin   | %%CURRENT_YEAR%%/12/31 |
      | fondCarte    | null                   |
