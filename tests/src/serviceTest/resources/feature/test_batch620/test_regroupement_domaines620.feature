Feature: Test regroupement domaines

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU sans niveau de rbt identique et 2 benefs ayant des domaines differents
    And I create a declarant from a file "declarantbaloo_5770"
    And I create a declaration from a file "batch620/declaration-benef1-AUDIOPTI"
    And I create a declaration from a file "batch620/declaration-benef2-OPTI"
    Then I wait for 2 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card for the benef with indice 0 has 1 domainesRegroup
      | code | taux    |
      | OPAU | PEC/PEC |

  @smokeTests @batch620 @regroupement @release
  Scenario: Test Regroupement PHAR et OPAU
    And I create a declarant from a file "declarantbaloo_5895"
    And I create a declaration from a file "batch620/declaration-benef1"
    Then I wait for 1 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card for the benef with indice 0 has 4 domainesRegroup
      | code | taux            | rang |
      | OPAU | PEC             | 1    |
      | DENT | PEC             | 3    |
      | HOSP | 100             | 4    |
      | PHAR | 100/100/100/100 | 5    |
    And the card for the benef with indice 0 and domaineRegroup with indice 0
      | code                | OPAU              |
      | taux                | PEC               |
      | unite               | XX                |
      | codeProduit         | 1472_DELTA_01_P01 |
      | categorieDomaine    | OPTI              |
      | referenceCouverture | OPTI-099          |
    And the card for the benef with indice 0 and domaineRegroup with indice 1
      | code                | DENT              |
      | taux                | PEC               |
      | unite               | XX                |
      | codeProduit         | 1472_DELTA_01_P01 |
      | categorieDomaine    | DENT              |
      | referenceCouverture | DENT-099          |
    And the card for the benef with indice 0 and domaineRegroup with indice 2
      | code                | HOSP              |
      | taux                | 100               |
      | unite               | PO                |
      | codeProduit         | 1472_DELTA_01_P01 |
      | categorieDomaine    | HOSP              |
      | referenceCouverture | HOSP-010          |
    And the card for the benef with indice 0 and domaineRegroup with indice 3
      | code                | PHAR                   |
      | taux                | 100/100/100/100        |
      | unite               | PO                     |
      | codeProduit         | 1472_DELTA_01_P01_PHNO |
      | categorieDomaine    | PHNO                   |
      | referenceCouverture | PHNO-010               |

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU avec niveau de rbt identique et 2 benefs ayant des domaines differents
    And I create a declarant from a file "declarantbaloo_5770_RbtIdentique"
    And I create a declaration from a file "batch620/declaration-benef1-AUDIOPTI"
    And I create a declaration from a file "batch620/declaration-benef2-OPTI"
    Then I wait for 2 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    # Anomalie car les bénéficiaires n'ont pas tous les mêmes domaines
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-DEMATERIALISEE" with index 1

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU avec niveau de rbt identique avec 1 benef avec OPTI
    And I create a declarant from a file "declarantbaloo_5770_RbtIdentique"
    And I create a declaration from a file "batch620/declaration-benef2-OPTI"
    Then I wait for 1 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card for the benef has 1 domainesRegroup
      | code | taux |
      | OPTI | PEC  |

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU sans niveau de rbt identique avec 1 benef avec OPTI
    And I create a declarant from a file "declarantbaloo_5770"
    And I create a declaration from a file "batch620/declaration-benef2-OPTI"
    Then I wait for 1 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card for the benef has 1 domainesRegroup
      | code | taux   |
      | OPAU | NC/PEC |

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU avec niveau de rbt identique et 2 benefs ayant des domaines differents
    And I create a declarant from a file "declarantbaloo_5770_RbtIdentique"
    And I create a declaration from a file "batch620/declaration-benef1-AUDIOPTI"
    And I create a declaration from a file "batch620/declaration-benef-OPTILABO"
    Then I wait for 2 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    # Anomalie car les bénéficiaires n'ont pas tous les mêmes domaines
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-DEMATERIALISEE" with index 1

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU avec niveau de rbt identique et 3 benefs ayant des domaines differents
    And I create a declarant from a file "declarantbaloo_5770_RbtIdentique"
    And I create a declaration from a file "batch620/declaration-benef1-AUDIOPTI"
    And I create a declaration from a file "batch620/declaration-benef2-AUDIOPTI"
    And I create a declaration from a file "batch620/declaration-benef-OPTILABO"
    Then I wait for 3 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    # Anomalie car les bénéficiaires n'ont pas tous les mêmes domaines
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-DEMATERIALISEE" with index 1

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU avec niveau de rbt identique et LARA sans, 3 benefs ayant des domaines differents
    And I create a declarant from a file "declarant_5770_2"
    And I create a declaration from a file "batch620/declaration-benef1-AUDIOPTI"
    And I create a declaration from a file "batch620/declaration-benef2-AUDIOPTI"
    And I create a declaration from a file "batch620/declaration-benef3-AUDIOPTILABO"
    Then I wait for 3 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card for the benef with indice 0 has 1 domainesRegroup
      | code | taux |
      | OPAU | PEC  |
    And the card for the benef with indice 1 has 1 domainesRegroup
      | code | taux |
      | OPAU | PEC  |
    And the card for the benef with indice 2 has 2 domainesRegroup
      | code | taux   |
      | LARA | PEC/NC |
      | OPAU | PEC    |

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement OPAU avec niveau de rbt identique et LARA sans, 3 benefs ayant des domaines differents n°2
    And I create a declarant from a file "declarant_5770_2"
    And I create a declaration from a file "batch620/declaration-benef2-OPTILABO"
    And I create a declaration from a file "batch620/declaration-benef2-OPTI"
    And I create a declaration from a file "batch620/declaration-benef-OPTILABO"
    Then I wait for 3 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card for the benef with indice 0 has 1 domainesRegroup
      | code | taux |
      | OPTI | PEC  |
    And the card for the benef with indice 1 has 2 domainesRegroup
      | code | taux   |
      | LARA | PEC/NC |
      | OPTI | PEC    |
    And the card for the benef with indice 1 has 2 domainesRegroup
      | code | taux   |
      | LARA | PEC/NC |
      | OPTI | PEC    |

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement 3 benefs ayant des domaines differents
    Given I create a declarant from a file "declarant_5770_2"
    And I create a declaration from a file "batch620/declaration-benef3-AUDIOPTILABO"
    And I create a declaration from a file "batch620/declaration-benef2-OPTI"
    And I create a declaration from a file "batch620/declaration-benef-OPTILABO"
    Then I wait for 3 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    # Anomalie car les bénéficiaires n'ont pas tous les mêmes domaines
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-DEMATERIALISEE" with index 1

  @smokeTests @batch620 @regroupement
  Scenario: Test Regroupement avec date debut carte non comprise dans le parametrage de regroupement
    And I create a declarant from a file "declarantbaloo_5770"
    And I create a declaration from a file "batch620/declaration-benef-PHAR"
    Then I wait for 1 declarations
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 card
    And the card for the benef has 2 domainesRegroup
      | code | taux |
      | PHNO | PEC  |
      | PHOR | PEC  |

  @smokeTests @batch620
  Scenario: Test avec regroupement OPAU, réception d'un contrat avec 2 benefs, génération des droits, création des cartes REJET C20
    And I create a declarant from a file "declarantbalooC20Reject"
    And I create TP card parameters from file "parametrageCarteTPDematPapier"
    And I create a contract element from a file "gt_5660"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA/IS   |
      | libelle | IKEA/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA |
      | libelle | KA |
    When I send a contract from file "batch620/servicePrestation/servicePrestation-2benef-regroupement" to version "V6"
    And I wait for 2 declarations
    # Creation des cartes (demat + papier)
    And I process declarations for carteDemat the "2024-01-01"
    # Pas de cartes car Rejet C20 => Codes renvoi différents
    And I wait for 0 cards
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    And I wait "5" seconds in order to consume the data
    Then 2 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-DEMATERIALISEE" with index 1

    When I wait for 8 traceConsolidations
    # 4 pour les déclarations consolidées créées et 4 pour l'ARL
    And the trace at index 4 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-TP                 |
      | codeClient    | BAL                      |
      | codeRejet     | C20                      |
    And the trace at index 5 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | BAL                      |
      | codeRejet     | C20                      |
    And the trace at index 6 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-TP                 |
      | codeClient    | BAL                      |
      | codeRejet     | C20                      |
    And the trace at index 7 has this values
      | idDeclarant   | 0000401166               |
      | dateExecution | 2024-01-01T00:00:00.000Z |
      | codeService   | CARTE-DEMATERIALISEE     |
      | codeClient    | BAL                      |
      | codeRejet     | C20                      |
