Feature: BLUE-7201 - Ne pas créer de carte papier si tous les domaines de la carte demat sont NC

  @smokeTests @batch620 @7201
  Scenario: BLUE-7201 - Pas de carte papier car les domaines sont NC
    Given I create a declarant from a file "declarant7201"
    Then I create a declaration from a file "batch620/declaration7201"
    Then I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-CONSOW0     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    And the card for the benef with indice 0 has 2 domainesRegroup
      | code | taux     |
      | PHAR | NC/NC/NC |
      | AMM  | NC       |
    And the card for the benef has 4 domainesCouverture
      | code | tauxRemboursement |
      | PHCO | NC                |
      | AMM  | NC                |
      | PHNO | NC                |
      | PHOR | NC                |
    Then I wait for 0 cartes papier

  @smokeTests @batch620 @7201
  Scenario: BLUE-7201 - Carte papier créée car les domaines ne sont pas tous NC
    Given I create a declarant from a file "declarant7201"
    Then I create a declaration from a file "batch620/declaration7201-2"
    Then I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-CONSOW0     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    And the card for the benef with indice 0 has 2 domainesRegroup
      | code | taux     |
      | PHAR | NC/NC/NC |
      | AMM  | 100      |
    And the card for the benef has 4 domainesCouverture
      | code | tauxRemboursement |
      | PHCO | NC                |
      | AMM  | 100               |
      | PHNO | NC                |
      | PHOR | NC                |
    Then I wait for 1 cartes papier
