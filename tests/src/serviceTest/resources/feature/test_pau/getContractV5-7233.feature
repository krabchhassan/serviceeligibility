Feature: Search contract PAU V5 BLUE-7233

  Background:
    Given I create a contract element from a file "gtaxa"
    Given I create a contract element from a file "gt10"
    Given I create a declarant from a file "declarantbaloo"
    Given I create TP card parameters from file "parametrageTPBaloo"
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @pauv5 @7233
  Scenario: PAU Priorisation TP - Contrat, dont l’un des bénéficiaires portant le NIR reçu, est assuré principal
    Given I create a beneficiaire from file "contractForPauV5/benef7233-homme"
    Given I create a beneficiaire from file "contractForPauV5/benef7233-femme"
    Given I send a contract from file "servicePrestation_7233-1" to version "V6"
    Given I send a contract from file "servicePrestation_7233-2" to version "V6"

    Then I wait "3" seconds in order to consume the data

    When I get contrat PAUV5 for 1000111111111 '20000103' 1 '2025-01-01' '2025-01-01' 0000401166 TP_ONLINE
    Then we found 2 contracts
    # Le premier contrat doit être le contrat où l'assuré principal est 1000111111111
    Then the contract number 0 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET     |
      | number       | ET2        |
    Then the contract number 1 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET     |
      | number       | ET1        |

    When I get contrat PAUV5 for 1000111111111 '20000103' 1 '2025-01-01' '2025-01-01' 0000401166 TP_OFFLINE
    Then we found 2 contracts
    # Le premier contrat doit être le contrat où l'assuré principal est 1000111111111
    Then the contract number 0 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET     |
      | number       | ET2        |
    Then the contract number 1 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET     |
      | number       | ET1        |

  @smokeTests @pauv5 @7233
  Scenario: PAU Priorisation HTP et TP - Prioriser les contrats collectifs par rapport aux contrats individuels
    Given I create a beneficiaire from file "contractForPauV5/benef7233-femmeCollIndiv"
    Given I send a contract from file "servicePrestation_7233-1-collectif" to version "V6"
    Given I send a contract from file "servicePrestation_7233-2-individuel" to version "V6"

    Then I wait "3" seconds in order to consume the data

    When I get contrat PAUV5 for 2001122334444 '20001103' 1 '2025-01-01' '2025-01-01' 0000401166 HTP
    Then we found 2 contracts
    # Le premier contrat doit être le contrat collectif
    Then the contract number 0 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET6    |
      | number       | ET1        |
    Then the contract number 1 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET6    |
      | number       | ET3        |

    When I get contrat PAUV5 for 2001122334444 '20001103' 1 '2025-01-01' '2025-01-01' 0000401166 TP_ONLINE
    Then we found 2 contracts
    # Le premier contrat doit être le contrat collectif
    Then the contract number 0 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET6    |
      | number       | ET1        |
    Then the contract number 1 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET6    |
      | number       | ET3        |

    When I get contrat PAUV5 for 2001122334444 '20001103' 1 '2025-01-01' '2025-01-01' 0000401166 TP_OFFLINE
    Then we found 2 contracts
    # Le premier contrat doit être le contrat collectif
    Then the contract number 0 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET6    |
      | number       | ET1        |
    Then the contract number 1 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ADH-ET6    |
      | number       | ET3        |
