Feature: Test ARL batch 620

  @smokeTests @batch620 @creationArl
  Scenario: Test ARL et tracesFlux sur Conso et Cartes
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/ARL/declarationCasARLDomainDuplicate"
    And I create a declaration from a file "batch620/ARL/declarationCasARLNoAddress"
    And I process declarations for carteDemat the "2024-01-01"
    # on génère la carte demat si pas d'adresse
    Then I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000452433 |
      | numberByPage | 10         |
    # il n'y a que 2 traces flux car la carte demat sans adresse est passé.
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-DEMATERIALISEE" with index 1

    When I wait for 4 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

    And the trace at index 1 has this values
      | idDeclarant          | 0000452433                        |
      | codeService          | CARTE-TP                          |
      | codeRejet            | C19                               |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

    And the trace at index 2 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C02                      |

    And the trace at index 3 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C02                      |

  @smokeTests @batch620 @creationArl
  Scenario: Test ARL et tracesFlux sur Conso.
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/ARL/declarationCasARLDomainDuplicate"
    And I create a declaration from a file "batch620/ARL/declarationCasARLvalide"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000452433 |
      | numberByPage | 10         |
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-DEMATERIALISEE" with index 1

    When I wait for 4 traceConsolidations

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

    And the trace at index 2 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C02                      |

    And the trace at index 3 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C02                      |

  @smokeTests @batch620 @creationArl
  Scenario: Test ARL et tracesFlux sur Cartes.
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/ARL/declarationCasARLvalide"
    And I create a declaration from a file "batch620/ARL/declarationCasARLNoAddress"
    And I process declarations for carteDemat the "2024-01-01"
    # same contract, only 1 card
    Then I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000452433 |
      | numberByPage | 10         |
    Then 1 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-TP"

    When I wait for 4 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |

    And the trace at index 1 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C19                      |

    And the trace at index 2 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |

    And the trace at index 3 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C19                      |

  @smokeTests @batch620 @creationArl
  Scenario: Test ARL et tracesFlux sur Cartes sur le même contrat
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/ARL/declarationCasARLAvecAddress"
    And I create a declaration from a file "batch620/ARL/declarationCasARLNoAddress"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000452433 |
      | numberByPage | 10         |
    Then 1 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-TP"

    When I wait for 4 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |

    And the trace at index 1 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C19                      |

    And the trace at index 2 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |

    And the trace at index 3 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |

    When I wait for 1 traceExtraConsolidations
    Then the extraction conso trace at index 0 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C19                      |

  @smokeTests @batch620 @creationArl
  Scenario: C16 C17 - Test ARL et tracesFlux sur Cartes sur le même contrat
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/ARL/declarationCasErreurC16"
    And I create a declaration from a file "batch620/ARL/declarationCasErreurC17"
    And I process declarations for carteDemat the "2024-01-01"
    # same contract, 0 card
    Then I wait for 0 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000452433 |
      | numberByPage | 10         |
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-DEMATERIALISEE" with index 1

    When I wait for 4 traceConsolidations

    And the trace at index 0 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C16                      |

    And the trace at index 1 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C16                      |

    And the trace at index 2 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C17                      |

    And the trace at index 3 has this values
      | idDeclarant   | 0000452433               |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C17                      |

  @smokeTests @batch620 @creationArl
  Scenario: Test ARL et tracesFlux sur Cartes sur erreur d'adresse + vérification trace consolidation / trace extraction conso
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/ARL/declarationCasARLNoAddress"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000452433 |
      | numberByPage | 10         |
    Then 1 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000452433" and the processus "CARTE-DEMATERIALISEE"

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

    Then I compare the card and the trace at index 0

  @smokeTests @batch620 @creationArl
  Scenario: Test sans ARL ni tracesFlux.
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/ARL/declarationCasARLvalide"
    And I create a declaration from a file "batch620/ARL/declarationCasARLvalide2"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 cards
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000452433 |
      | numberByPage | 10         |
    Then 0 flux is returned


  @nosmokeTests @batch620 @creationArl
  # l'appel au batch sort en erreur 500
  Scenario: Test ARL et tracesFlux sur Cartes sur le même contrat
    Given I create a declarant from a file "declarant_5801"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur1"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur2"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur3"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur4"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur5"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur6"
    And I process declarations for carteDemat the "2024-01-01"
    # same contract, no card
    Then I wait for 0 card

    And I empty the declaration database

    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur1"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur2_corrige"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur3"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur4"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur5"
    And I create a declaration from a file "batch620/erreurTechnique/declarationCasErreur6"

    #TODO
    # same contract, 4 card
    Then I wait for 4 cards
