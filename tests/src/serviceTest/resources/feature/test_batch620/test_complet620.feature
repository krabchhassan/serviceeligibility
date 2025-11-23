Feature: Test complet batch 620

  Background:
    Given I create a declarant from a file "declarantbalooDematPapier"
    And I create TP card parameters from file "parametrageCarteTPDematPapier"
    And I create a contract element from a file "gt10"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @batch620
  Scenario: Test réception d'un contrat 1 benef, génération des droits, création des cartes, géneration du contrat TP consolide, appel PAU
    And I create a beneficiaire from file "batch620/beneficiary/benef1-forPAU"
    When I send a contract from file "batch620/servicePrestation/servicePrestation-1benef" to version "V6"
    And I wait "5" seconds in order to consume the data
    And I wait for 1 declarations
    # Creation des cartes (demat + papier)
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    And I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-14725497 |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01          |
      | periodeFin       | %%CURRENT_YEAR%%/12/31          |
    Then the card for the benef has 1 domainesCouverture
      | code | tauxRemboursement | codeGarantie | libelleGarantie |
      | HOSP | PEC               | GT_10        | GT_10           |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | HOSP | 1    |
    And I wait for 1 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166 |
      | nomAMC       | BALOO      |
      | libelleAMC   | BALOO      |
      | periodeDebut | %%CURRENT_YEAR%%/01/01 |
      | periodeFin   | %%CURRENT_YEAR%%/12/31 |
    # Verification des traces
    When I wait for 2 traceConsolidations
    And the trace at index 0 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-TP                          |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 1 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    # Consolidation => generation du contrat
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "contrattp-testcomplet620" content
    # Appel au PAU
    When I get contrat PAUV5 for 2800619784004 '19800615' 1 '%%CURRENT_YEAR%%-01-01' '%%CURRENT_YEAR%%-12-31' 0000401166 TP_OFFLINE 14725497
    Then In the PAU, there is 1 contract
    Then the contract number 0 data has values
      | number | 14725497 |

  @smokeTests @batch620
  Scenario: Test réception d'un contrat 2 benef, génération des droits, création des cartes, géneration du contrat TP consolide, appel PAU
    And I create a beneficiaire from file "batch620/beneficiary/benef1-forPAU"
    And I create a beneficiaire from file "batch620/beneficiary/benef2-forPAU"
    When I send a contract from file "batch620/servicePrestation/servicePrestation-2benef" to version "V6"
    And I wait "5" seconds in order to consume the data
    And I wait for 2 declarations
    # Creation des cartes (demat + papier)
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    And I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-14725497 |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01          |
      | periodeFin       | %%CURRENT_YEAR%%/12/31          |
    Then the card for the benef with indice 0 has 1 domainesCouverture
      | code | tauxRemboursement | codeGarantie | libelleGarantie |
      | HOSP | PEC               | GT_10        | GT_10           |
    Then the card for the benef with indice 1 has 1 domainesCouverture
      | code | tauxRemboursement | codeGarantie | libelleGarantie |
      | HOSP | PEC               | GT_10        | GT_10           |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | HOSP | 1    |
    And I wait for 1 cartes papier
    Then the carte papier at index 0 has these values
      | numeroAMC    | 0000401166 |
      | nomAMC       | BALOO      |
      | libelleAMC   | BALOO      |
      | periodeDebut | %%CURRENT_YEAR%%/01/01 |
      | periodeFin   | %%CURRENT_YEAR%%/12/31 |
    Then the carte papier at index 0 has this adresse
      | ligne1                   | ligne4             | ligne6         | ligne7 | codePostal |
      | Mme DE BENARD Christelle | 3 chemin de Launay | 31000 Toulouse | FRANCE | 31000      |
    # Verification des traces
    When I wait for 4 traceConsolidations
    And the trace at index 0 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-TP                          |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 1 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 2 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-TP                          |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 3 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    # Consolidation => generation du contrat
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "contrattp-testcomplet620-2" content

    # Appel au PAU
    When I get contrat PAUV5 for 2800619784004 '19800615' 1 '%%CURRENT_YEAR%%-01-01' '%%CURRENT_YEAR%%-12-31' 0000401166 TP_OFFLINE 14725497
    Then In the PAU, there is 1 contract
    Then the contract number 0 data has values
      | number | 14725497 |
