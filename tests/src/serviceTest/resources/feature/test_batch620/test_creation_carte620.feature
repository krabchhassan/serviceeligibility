Feature: Test Validation carte batch 620

  @smokeTests @batch620 @validationCarte
  Scenario: Consolidation de déclarations avec carteTPaEditerOuDigitale = 0
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-benef-nocard"
    And I process declarations for carteDemat the "2024-01-01"

    # We're expecting no cards because "carteTPaEditerOuDigitale" equals 0
    Then I wait for 0 cards

    When I wait for 0 traceConsolidations

  @smokeTests @batch620 @validationCarte
  Scenario: Test creation carte avec declaration sans versionDeclaration avec 1 domaine droit editable et 1 non editable
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-sansVersion-domaineEditable"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | OPAU | 0    |
    And the card for the benef has 1 domainesCouverture
      | code | tauxRemboursement |
      | OPAU | PEC               |
    And the card for the benef has 1 domainesRegroup
      | code | taux |
      | OPAU | PEC  |

    When I wait for 2 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 1 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-TP                          |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

  @smokeTests @batch620 @validationCarte
  Scenario: Test creation carte avec declaration sans versionDeclaration et domaine droit non editable
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-sansVersion-sansDomaineEditable"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 cards

  @smokeTests @batch620 @validationCarte
  Scenario: Test creation carte avec declaration v2 avec 1 domaine droit editable et 1 non editable
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-v2-domaineEditable"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | OPAU | 0    |
    And the card for the benef has 1 domainesCouverture
      | code | tauxRemboursement |
      | OPAU | PEC               |
    And the card for the benef has 1 domainesRegroup
      | code | taux |
      | OPAU | PEC  |

    When I wait for 2 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 1 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-TP                          |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

  @smokeTests @batch620 @validationCarte
  Scenario: Test validation des cartes avec dates de domaine différent et radiation du benef ensuite.
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-benef1-OPAU"
    And I create a declaration from a file "batch620/declaration-benef2-OPAU"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |

    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/03/01          |
      | periodeFin       | 2024/12/31          |

    # "carteTPaEditerOuDigitale" = 2, so we only have traces for CARTE-DEMATERIALISEE
    When I wait for 2 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

    And the trace at index 1 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

    # no news to declarations to process -> no news cards to create
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 cards

    # No new traces are created
    When I wait for 2 traceConsolidations

    # next step -> radiation du 2nd benef
    And I create a declaration from a file "batch620/declaration-benef2-radiation-OPAU" and change effet debut
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 5 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | false               |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |

    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | false               |
      | periodeDebut     | 2024/03/01          |
      | periodeFin       | 2024/12/31          |

    Then the card at index 2 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |

    Then the card at index 3 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/03/01          |
      | periodeFin       | 2024/07/31          |

    Then the card at index 4 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/08/01          |
      | periodeFin       | 2024/12/31          |

    # We have another consolidation, so we have another trace
    When I wait for 3 traceConsolidations

    And the trace at index 2 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

  @smokeTests @batch620 @validationCarte
  Scenario: Test validation des cartes avec des domaines en plus sur un benef
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-benef1-OPAU"
    And I create a declaration from a file "batch620/declaration-benef2-OPAU-HOSP"
    And I process declarations for carteDemat the "2024-02-01"
    Then I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    Then there are 2 domainesConventions on the card at index 0 has this values
      | code | rang |
      | OPAU | 0    |
      | HOSP | 0    |
    Then there are 1 conventions on the domainesConventions at index 0 on the card at index 0 has this values
      | code | priorite |
      | IS   | 0        |

    Then there are 2 conventions on the domainesConventions at index 1 on the card at index 0 has this values
      | code | priorite |
      | IS   | 0        |
      | IT   | 0        |


  @smokeTests @batch620 @validationCarte
  Scenario: Test validation des cartes avec des trous dans les domaines
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-benef1-domaine-coupe"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 card

    And I empty the declaration database
    And I create a declaration from a file "batch620/declaration-benef2-domaine-coupe" and change effet debut
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 6 card


  @smokeTests @batch620 @validationCarte
  Scenario: Changement de garantie sur le meme code domaine, prise en compte de la date fin online pour l'ancienne garantie meme si les priorites sont les memes
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-garantie1-dent"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-CONSOW0 |
      | isLastCarteDemat | true               |
      | periodeDebut     | 2024/01/01         |
      | periodeFin       | 2024/12/31         |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | DENT | 0    |
    And I empty the declaration database
    And I create a declaration from a file "batch620/declaration-garantie1&2-dent" and change effet debut
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 3 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-CONSOW0 |
      | isLastCarteDemat | false              |
      | periodeDebut     | 2024/01/01         |
      | periodeFin       | 2024/12/31         |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | DENT | 0    |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-CONSOW0 |
      | isLastCarteDemat | true               |
      | periodeDebut     | 2024/01/01         |
      | periodeFin       | 2024/09/30         |
    Then there are 1 domainesConventions on the card at index 1 has this values
      | code | rang |
      | DENT | 0    |
    Then the card at index 2 has this values
      | AMC_contrat      | 0000452433-CONSOW0 |
      | isLastCarteDemat | true               |
      | periodeDebut     | 2024/10/01         |
      | periodeFin       | 2024/12/31         |
    Then there are 1 domainesConventions on the card at index 2 has this values
      | code | rang |
      | DENT | 0    |

    #to be continue


  @smokeTests @batch620 @validationCarte
  Scenario: Test creation carte avec declaration avec 2 domaines identiques sauf la garantie et le taux (PEC et NC), le moins prioritaire doit s'afficher dans la carte
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-2domainesEditablesIdentiques"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | OPAU | 0    |
    And the card for the benef has 1 domainesCouverture
      | code | tauxRemboursement | codeGarantie | libelleGarantie |
      | OPAU | PEC               | AXASCCGDIV   | CG SANTE ++     |
    And the card for the benef has 1 domainesRegroup
      | code | taux | codeGarantie | libelleGarantie |
      | OPAU | PEC  | AXASCCGDIV   | CG SANTE ++     |

    When I wait for 2 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 1 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-TP                          |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

  @smokeTests @batch620 @validationCarte
  Scenario: Test creation carte avec declaration contenant plusieurs garanties pour un même domaineTP, la moins prioritaire est affichée
    Given I create a declarant from a file "declarantbaloo_5813"
    And I create a declaration from a file "batch620/declaration-5716"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5497MBA004 |
      | isLastCarteDemat | true                  |
      | periodeDebut     | 2024/01/01            |
      | periodeFin       | 2024/12/31            |
    And the card for the benef has 5 domainesCouverture
      | code | tauxRemboursement | codeGarantie | libelleGarantie |
      | HOSP | 200               | GT_5497_D2   | BLUE            |
      | PHNO | 200               | GT_5497_D2   | BLUE            |
      | PHOR | 180               | GT_5497_D2   | BLUE            |
      | PHAR | 200               | GT_5497_D2   | BLUE            |
      | SVIL | 200               | GT_5497_D2   | BLUE            |
    And the card for the benef has 5 domainesRegroup
      | code | taux | codeGarantie | libelleGarantie |
      | HOSP | 200  | GT_5497_D2   | BLUE            |
      | PHNO | 200  | GT_5497_D2   | BLUE            |
      | PHOR | 180  | GT_5497_D2   | BLUE            |
      | PHAR | 200  | GT_5497_D2   | BLUE            |
      | SVIL | 200  | GT_5497_D2   | BLUE            |

  @smokeTests @batch620 @validationCarte
  Scenario: Test validation des cartes avec service Carte TP ouvert dans le futur dans les domaines
    Given I create a declarant from a file "declarant_5497_carte_tp_ferme"
    And I create a declaration from a file "batch620/declaration_5497"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-5497NLE0038 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | 2024/01/01             |
      | periodeFin       | 2024/12/31             |
      | codeServices     | CARTE-DEMATERIALISEE   |
      | codeServicesSize | 1                      |

  @smokeTests @batch620 @validationCarte
  Scenario: Test validation des cartes avec service Carte Demat ouvert dans le futur dans les domaines
    Given I create a declarant from a file "declarant_5497_carte_demat_ferme"
    And I create a declaration from a file "batch620/declaration_5497"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-5497NLE0038 |
      | isLastCarteDemat | false                  |
      | periodeDebut     | 2024/01/01             |
      | periodeFin       | 2024/12/31             |
      | codeServices     | CARTE-TP               |
      | codeServicesSize | 1                      |

  @smokeTests @batch620 @validationCarte
  Scenario: Test les domaines avec un taux à 0 sont présents dans la carte
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-domaineTaux0-6380-1"
    And I create a declaration from a file "batch620/declaration-domaineTaux0-6380-2"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-21234567  |
      | isLastCarteDemat | true                 |
      | periodeDebut     | 2024/04/01           |
      | periodeFin       | 2024/12/31           |
      | codeServices     | CARTE-DEMATERIALISEE |
      | codeServicesSize | 2                    |
    And the card for the benef has 4 domainesCouverture
      | code | tauxRemboursement | codeGarantie | libelleGarantie |
      | PHCN | 100               | OPTIONA      | BASE 1          |
      | PHOR | 0                 | OPTIONA      | BASE 1          |
      | SVIL | 0                 | OPTIONA      | BASE 1          |
      | TRAN | 100               | OPTIONA      | BASE 1          |
    And the card for the benef has 4 domainesRegroup
      | code | taux | codeGarantie | libelleGarantie |
      | PHCN | 100  | OPTIONA      | BASE 1          |
      | PHOR | 0    | OPTIONA      | BASE 1          |
      | SVIL | 0    | OPTIONA      | BASE 1          |
      | TRAN | 100  | OPTIONA      | BASE 1          |
