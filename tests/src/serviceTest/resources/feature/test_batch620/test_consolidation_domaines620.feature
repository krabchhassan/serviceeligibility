Feature: Test Validation carte batch 620

  @smokeTests @batch620 @consolidationDomaines @release
  Scenario: Test batch 620 bout en bout, verification consolidation des domaines.
    Given I create a contract element from a file "gtaxa_5497"
    And I create a contract element from a file "gt_5660"
    And I create a contract element from a file "gtaxa_5497-2"
    And I create TP card parameters from file "parametrageTP5497"
    And I create a declarant from a file "declarantbalooEditable"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA/IS |
      | libelle | Ka    |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA |
      | libelle | Ka |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    # Generation des droits
    When I send a test contract v6 from file "consumer_worker/v6/contract_5497"
    Then I wait for 1 declarations
    Then there is 21 rightsDomains and the different rightsDomains has this values
      | garantie     | domaine | taux | unite | debut      | fin        | priorite |
      | BAL_5458_001 | OPTI    | PEC  | XX    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5497_001 | DENT    | NC   | XX    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 02       |
      | BAL_5497_002 | CURE    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 03       |
      | BAL_5458_001 | AUDI    | PEC  | XX    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5497_001 | HOSP    | PEC  | XX    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 02       |
      | BAL_5458_001 | DENT    | PEC  | XX    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5497_001 | PHOR    | 70   | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 02       |
      | BAL_5458_001 | HOSP    | PEC  | XX    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5497_001 | CURE    | 300  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 02       |
      | BAL_5497_001 | LABO    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 02       |
      | BAL_5458_001 | PHNO    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5497_001 | PHCO    | NC   | XX    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 02       |
      | BAL_5458_001 | PHOR    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5497_001 | LPPS    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 02       |
      | BAL_5458_001 | TRAN    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5458_001 | LABO    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5458_001 | PHCO    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5458_001 | EXTE    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5458_001 | LPPS    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5458_001 | MEDE    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5458_001 | CSTE    | 100  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 01       |
      | BAL_5458_001 | CURE    | 300  | PO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 03       |

    # Generation carte demat et papier
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 cartes papier
    And I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-8343484392 |
      | isLastCarteDemat | true                  |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01            |
      | periodeFin       | %%CURRENT_YEAR%%/12/31            |

    # Verification consolidation des domaines droit (INCLUSION/DELTA)
    And the card for the benef has 11 domainesRegroup
      | code | taux | unite | codeGarantie | libelleGarantie  | priorite |
      | OPTI | PEC  | XX    | BAL_5458_001 | CG SANTE CONFORT | 01       |
      | AUDI | PEC  | XX    | BAL_5458_001 | CG SANTE CONFORT | 01       |
      | DENT | PEC  | XX    | BAL_5497_001 | CG SANTE CONFORT | 02       |
      | HOSP | PEC  | XX    | BAL_5497_001 | CG SANTE CONFORT | 02       |
      | PHNO | 100  | PO    | BAL_5458_001 | CG SANTE CONFORT | 01       |
      | PHOR | 170  | PO    | BAL_5497_001 | CG SANTE CONFORT | 02       |
      | TRAN | 100  | PO    | BAL_5458_001 | CG SANTE CONFORT | 01       |
      | CURE | 300  | PO    | BAL_5497_002 | CG SANTE CONFORT | 03       |
      | LABO | 200  | PO    | BAL_5497_001 | CG SANTE CONFORT | 02       |
      | PHCO | 100  | PO    | BAL_5497_001 | CG SANTE CONFORT | 02       |
      | EXTE | 100  | PO    | BAL_5458_001 | CG SANTE CONFORT | 01       |

    # Verification création traces conso
    When I wait for 2 traceConsolidations
    And the trace at index 0 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-DEMATERIALISEE              |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |
    And the trace at index 1 has this values
      | idDeclarant          | 0000401166                        |
      | codeService          | CARTE-TP                          |
      | codeClient           | TNR                               |
      | collectionConsolidee | declarationsConsolideesCarteDemat |

    # Generation du contrat TP
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "contrattp-boutenbout620" content

    # Appel au PAU
    And I create a beneficiaire from file "beneficiaryForPauV4/benef_5497"
    When I get contrat PAUV5 for 1701062498046 '19791006' 1 '%%CURRENT_YEAR%%-11-30' '%%CURRENT_YEAR%%-12-01' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | 8343484392 |
