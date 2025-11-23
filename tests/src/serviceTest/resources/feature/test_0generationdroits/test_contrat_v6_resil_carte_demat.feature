Feature: Test resiliation/radiation d'un contrat v6 en exclusivité carte dématerialisée

  @todosmokeTests @contractV6 @excluCarteDemat
  Scenario: I send contract resiliated with "exclusivité carte demat"
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo_5458"
    And I create TP card parameters from file "parametrageTPBaloo_exclu_carte_demat"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    When I send a test contract from file "contratV6/one_benef"
    Then I send a test contract from file "contratV6/one_benef_resiliated"
    And I wait for 3 declarations
    Then I update beneficiary with the declaration
    Then I update beneficiary with the declaration with indice 1
    Then I update beneficiary with the declaration with indice 2
    And I wait "3" seconds in order to consume the data
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-12-20' '2025-12-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-12-27' '2025-12-27' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception

    When I get contrat PAUV5 without endDate for 1860598145145 '18690428' 1 '2026-01-01' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception

