Feature: Test generation carte avec suspension batch 620

  Background:
    Given I create a declarant from a file "declarant_6212"

    # BLUE-6215 CA04
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test generation des cartes selon les périodes de suspensions des droits TP
    Given I create a declaration from a file "batch620/declaration-withSuspension1"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/02/01          |
      | periodeFin       | 2024/02/29          |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/06/01          |
      | periodeFin       | 2024/10/31          |

    # BLUE-6215 CA05
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test 2 generation des cartes selon les périodes de suspensions des droits TP
    Given I create a declaration from a file "batch620/declaration-withSuspension2"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/06/01          |
      | periodeFin       | 2024/10/31          |

    # BLUE-6215 CA05 bis
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test generation des cartes avec une periode suspension sans fin
    Given I create a declaration from a file "batch620/declaration-withSuspension3"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 cards

    # BLUE-6215 CA06
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test generation des cartes avec une periode suspension sans fin qui debute au cours de l'année
    Given I create a declaration from a file "batch620/declaration-withNoSuspension1"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    And I create a declaration from a file "batch620/declaration-withSuspension4"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 cards
    # The previous card has been invalidated + new card generated
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | false               |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |

    # BLUE-6215 CA07
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test generation des cartes selon les périodes de suspensions des droits TP avec une carte déjà active du 01/01/2024 au 31/12/2024
    Given I create a declaration from a file "batch620/declaration-withNoSuspension1"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    And I create a declaration from a file "batch620/declaration-withSuspension3"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    # The previous card has been invalidated
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | false               |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |

    # BLUE-6215 CA08
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test generation des cartes avec levée de suspension et période éditable des domaines TP => 01/01/2024 => 31/12/2024
    Given I create a declaration from a file "batch620/declaration-withSuspension4"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    And I create a declaration from a file "batch620/declaration-leveeSuspension1"
    And I process declarations for carteDemat the "2024-02-29"
    Then I wait for 2 cards
    # The previous card has been invalidated + new card generated
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | false               |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |

    # BLUE-6215 CA08 bis
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test generation des cartes avec levée de suspension et période éditable des domaines TP => 01/03/2024 => 31/12/2024
    Given I create a declaration from a file "batch620/declaration-withSuspension4"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    And I create a declaration from a file "batch620/declaration-leveeSuspension2"
    And I process declarations for carteDemat the "2024-02-29"
    Then I wait for 2 cards
    # The previous card is not invalidated + new card generated
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/03/01          |
      | periodeFin       | 2024/12/31          |

    # BLUE-6215 CA09
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test 2 generation des cartes avec levée de suspension et période éditable des domaines TP => 01/01/2024 => 31/12/2024
    Given I create a declaration from a file "batch620/declaration-withSuspension4"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    And I create a declaration from a file "batch620/declaration-leveeSuspension3"
    And I process declarations for carteDemat the "2024-03-31"
    Then I wait for 3 cards
    # The previous card has been invalidated + 2 new cards generated
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | false               |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    Then the card at index 2 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/04/01          |
      | periodeFin       | 2024/12/31          |

    # BLUE-6215 CA09 bis
  @smokeTests @batch620 @generationCarteSuspension
  Scenario: Test 2 generation des cartes avec levée de suspension et période éditable des domaines TP => 01/03/2024 => 31/12/2024
    Given I create a declaration from a file "batch620/declaration-withSuspension4"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    And I create a declaration from a file "batch620/declaration-leveeSuspension4"
    And I process declarations for carteDemat the "2024-03-31"
    Then I wait for 2 cards
    # The previous card is not invalidated + new card generated
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/02/29          |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/04/01          |
      | periodeFin       | 2024/12/31          |
