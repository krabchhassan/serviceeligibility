Feature: Test contract avec code Itelis

  @smokeTests @itelis @release
  Scenario: I send event/ui/renew and recycling contract with code itelis
    And I create a contract element from a file "gtaxa_bad_axo"
    And I create a declarant from a file "declarantbaloo_5458"
    And I create TP card parameters from file "parametrageTP_CodeItelis"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    # Recycle
    Given I create a beneficiaire from file "beneficiaire-codeitelis"
    When I send a test contract from file "contratV5/5516_one_benef"
    And I wait for the last trigger with contract number "93000808" and amc "0000401166" to be "ProcessedWithErrors"
    And I create a contract element from a file "gtaxa"
    And I recycle the trigger
    And I wait for 1 declaration
    Then The declaration with indice 0 has code itelis "BLABLO_OPTIQUE"

    # Event
    When I send a test contract from file "contratV5/5516_one_benef"
    And I wait for 2 declarations
    Then The declaration with indice 1 has code itelis "BLABLO_OPTIQUE"

    # UI
    When I create a trigger by UI from "trigger_by_ui_codeItelis"
    And I wait for 3 declarations
    Then The declaration with indice 2 has code itelis "BLABLO_OPTIQUE"

    # Renew
    When I renew the rights on "%%CURRENT_YEAR%%-01-01" with mode "NO_RDO"
    And I wait for 4 declarations
    Then The declaration with indice 3 has code itelis "BLABLO_OPTIQUE"

    Then I wait for 1 contract
    Then the consolidated contract has values
      | numeroContrat | 93000808       |
      | idDeclarant   | 0000401166     |
      | codeItelis    | BLABLO_OPTIQUE |

    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '%%CURRENT_YEAR%%-12-24' '%%CURRENT_YEAR%%-12-24' 0000401166 TP_ONLINE
    Then the pau is identical to "pau/v5/expectedpau-5921" content
