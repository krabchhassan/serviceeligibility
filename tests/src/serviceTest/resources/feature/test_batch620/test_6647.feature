Feature: Test domaines de convention non doublés BLUE-6647

  Background:
    Given I create a declarant from a file "declarant_6647"

  @smokeTests @batch620
  Scenario: Test domaines de convention non doublés BLUE-6647
    And I create a declaration from a file "declaration_6647"
    And I create a declaration from a file "declaration_6647-1"
    And I process declarations for carteDemat the "2025-01-01"
    And I wait for 1 cards
    Then there are 11 domainesConventions on the card at index 0 has this values
      | code | rang |
      | PHAR | 2    |
      | MED  | 4    |
      | SVIL | 5    |
      | CSTE | 8    |
      | TRAN | 9    |
      | DESO | 11   |
      | EXTE | 14   |
      | HOSP | 15   |
      | OPTI | 17   |
      | AUDI | 18   |
      | DEPR | 19   |
