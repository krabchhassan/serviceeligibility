Feature: Test generation de cartes TP avec code ITELIS

  @smokeTests @batch620 @itelis
  Scenario: Test réception d'un contrat et création carte avec code ITELIS
    Given I create a contract element from a file "gtaxa"
    Given I create a declarant from a file "declarantbaloo_5813"
    Given I create an automatic TP card parameters on next year from file "parametrageTP_CodeItelis"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract from file "5726-Contrat1"
    Then I wait for 1 declarations
    Then The declaration with indice 0 has code itelis "BLABLO_OPTIQUE"
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    Then the card has code itelis "BLABLO_OPTIQUE"
    Then I wait for 1 cartes papier
    And the carte papier has code itelis "BLABLO_OPTIQUE"

  @smokeTests @batch620 @itelis
  Scenario: Test réception d'un contrat avec 2 benefs et création carte demat avec code ITELIS
    Given I create a contract element from a file "gtaxa"
    Given I create a declarant from a file "declarantbaloo_5813"
    Given I create an automatic TP card parameters on next year from file "parametrageTP_CodeItelis"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract from file "5813-CasAssPrincSansAdresse"
    Then I wait for 2 declarations
    Then The declaration with indice 0 has code itelis "BLABLO_OPTIQUE"
    Then The declaration with indice 1 has code itelis "BLABLO_OPTIQUE"
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card has code itelis "BLABLO_OPTIQUE"

  @smokeTests @batch620 @itelis
  Scenario: Test réception d'un contrat avec 2 benefs et création carte demat et papier avec code ITELIS
    Given I create a contract element from a file "gtaxa"
    Given I create a declarant from a file "declarantbaloo_5813"
    Given I create an automatic TP card parameters on next year from file "parametrageTP_CodeItelis"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract from file "contrat2benefsAvecCartes"
    Then I wait for 2 declarations
    Then The declaration with indice 0 has code itelis "BLABLO_OPTIQUE"
    Then The declaration with indice 1 has code itelis "BLABLO_OPTIQUE"
    Then I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 card
    Then the card has code itelis "BLABLO_OPTIQUE"
    Then I wait for 1 cartes papier
    And the carte papier has code itelis "BLABLO_OPTIQUE"

  @smokeTests @batch620 @itelis
  Scenario: Test rejet C25 codes ITELIS différents
    When I create a declarant from a file "declarantbaloo_5813"
    When I create a declaration from a file "batch620/ARL/declarationCasErreurC25"
    When I create a declaration from a file "batch620/ARL/declarationCasErreurC25-benef2"
    When I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 card
    Then I wait for 0 cartes papier
    Then I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-DEMATERIALISEE" with index 1

    When I wait for 8 traceConsolidations
    And the trace at index 4 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

    And the trace at index 5 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

    And the trace at index 6 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

    And the trace at index 7 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

  @smokeTests @batch620 @itelis
  Scenario: Test rejet C25 1 benef avec code ITELIS et 1 benef sans
    Given I create a declarant from a file "declarantbaloo_5813"
    When I create a declaration from a file "batch620/ARL/declarationCasErreurC25"
    When I create a declaration from a file "batch620/ARL/declarationCasErreurC25-benef3-sansCodeItelis"
    When I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 card
    Then I wait for 0 cartes papier
    Then I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-DEMATERIALISEE" with index 1

    When I wait for 8 traceConsolidations
    And the trace at index 4 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

    And the trace at index 5 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

    And the trace at index 6 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

    And the trace at index 7 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-TP                 |
      | codeClient    | TNR                      |
      | codeRejet     | C25                      |

  @smokeTests @batch620 @itelis
  Scenario: Test 1 benef sans code ITELIS et 1 benef avec code ITELIS valorisé à null, carte créée
    Given I create a declarant from a file "declarantbaloo_5813"
    When I create a declaration from a file "batch620/ARL/declarationCasErreurC25-benef3-codeItelis-null"
    When I create a declaration from a file "batch620/ARL/declarationCasErreurC25-benef3-sansCodeItelis"
    When I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    Then the card has code itelis "null"
    Then I wait for 1 cartes papier
    Then the carte papier has code itelis "null"
    Then I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    And 0 flux is returned
