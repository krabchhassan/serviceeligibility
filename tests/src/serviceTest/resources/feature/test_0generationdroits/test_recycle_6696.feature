Feature: Test recycle

  Background:
    Given I create a contract element from a file "gt_6696"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @recycle @resetRecycling
  Scenario: Recycling a contract with removed beneficiary
    When I send a test contract from file "contratV5/6696_2benefs"
    Given I create a beneficiaire from file "beneficiaire_6696_1"
    Given I create a beneficiaire from file "beneficiaire_6696_2"
    Then I send a test contract from file "contratV5/6696_1benef"
    Then I wait for the last trigger with contract number "01599324" and amc "0000401166" to be "ProcessedWithErrors"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then the trigger of indice "1" has this values
      | status         | ProcessedWithErrors |
      | amc            | 0000401166          |
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithErrors |
      | amc            | 0000401166          |
    # I recycle trigger at indice 0
    Then I recycle the trigger
    Then I wait for the last trigger with contract number "01599324" and amc "0000401166" to be "ProcessedWithErrors"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithErrors |
      | amc            | 0000401166          |
    Then the trigger of indice "1" has this values
      | status         | ProcessedWithErrors |
      | amc            | 0000401166          |
