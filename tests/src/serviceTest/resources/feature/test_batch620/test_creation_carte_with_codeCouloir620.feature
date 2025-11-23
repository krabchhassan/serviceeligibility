Feature: Test Creation carte avec codeCouloir batch 620

  @smokeTests @batch620 @couloirClient
  Scenario: Creation carte pour AMC avec CARTE-DEMAT et CARTE-TP avec codeCouloir = BAL et lancement du batch avec BOL
    Given I create a declarant from a file "declarant_5874"
    And I create a declaration from a file "batch620/declaration-benef1"
    And I process declarations for carteDemat the "2024-01-01" with codeCouloir "BOL"
    Then I wait for 0 cards
    Then I wait for 0 cartes papier

  @smokeTests @batch620 @couloirClient
  Scenario: Creation carte pour AMC avec CARTE-DEMAT et CARTE-TP avec codeCouloir = BAL et lancement du batch avec BAL
    Given I create a declarant from a file "declarant_5874"
    And I create a declarant from a file "declarantWithCartePapier"
    And I create a declaration from a file "batch620/declaration-benef1"
    And I create a declaration from a file "batch620/declaration2024"
    And I process declarations for carteDemat the "2024-01-01" with codeCouloir "BAL"

    Then I wait for 1 cards
    Then I wait for 1 cartes papier

  @smokeTests @batch620 @couloirClient
  Scenario: Creation carte pour AMC avec CARTE-DEMAT avec codeCouloir = BAL et CARTE-TP sans codeCouloir et lancement du batch avec BAL
    Given I create a declarant from a file "declarant_5874_CarteTPsansCouloirClient"
    And I create a declarant from a file "declarantWithCartePapier"
    And I create a declaration from a file "batch620/declaration-benef1"
    And I create a declaration from a file "batch620/declaration2024"
    And I process declarations for carteDemat the "2024-01-01" with codeCouloir "BAL"

    Then I wait for 1 cards
    Then I wait for 0 cartes papier
