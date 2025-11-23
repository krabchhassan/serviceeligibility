Feature: Forçage du PAU - Feature BLUE-6133 - Avec résiliation

  Background:
    Given I create a service prestation from a file "servicePrestation1-6133"
    Given I create a service prestation from a file "servicePrestation2-6133"
    Given I create a contrat from file "contrattp1-6133"
    Given I create a contrat from file "contrattp2-6133"
    Given I create a beneficiaire from file "benef-6133"
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS |
      | libelle | IS |
    When I send a contract from file "servicePrestation1-6133" to version "V6"
    When I send a contract from file "servicePrestation2-6133" to version "V6"
    When I send a contract from file "contratV6/createServicePrest1resil-6133" to version "V6"
    When I send a contract from file "contratV6/createServicePrest2resil-6133" to version "V6"
    Then I wait for 6 declarations

  @smokeTests @smokeTestsWithoutKafka @pauv5 @forcePAU @toChangeEveryYear
  Scenario: Modif des 2 contrats pour les résilier au 15/03/2025 + Appel du PAU sans date de fin avant la résiliation
    # Appel HTP
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2023-07-15' 0000401166 HTP for issuingCompanyCode 000ER00955
    Then we found 2 contracts
    Then the contract with indice 0 is not forced
    Then the contract with indice 1 is not forced
    Then the contract with indice 0 and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_ONLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2023-07-15' 0000401166 TP_ONLINE for issuingCompanyCode 000ER00955
    Then we found 2 contracts
    Then the contract with indice 0 is not forced
    Then the contract with indice 1 is not forced
    Then the contract with indice 0 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_OFFLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2023-07-15' 0000401166 TP_OFFLINE for issuingCompanyCode 000ER00955
    Then we found 2 contracts
    Then the contract with indice 0 is not forced
    Then the contract with indice 1 is not forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-12-31 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-12-31 |

  @smokeTests @smokeTestsWithoutKafka @pauv5 @forcePAU
  Scenario: Modif des 2 contrats pour les résilier au 15/03/2025 + Appel du PAU avec une date de fin avant le début des contrats
    # Appel HTP
    When I get contrat PAUV5 for 1000611111111 '19590224' 1 '2023-07-15' '2023-07-18' 0000401166 HTP for issuingCompanyCode 000ER00955
    And With this request I have a contract not found exception
    # Appel TP_ONLINE
    When I get contrat PAUV5 for 1000611111111 '19590224' 1 '2023-07-15' '2023-07-18' 0000401166 TP_ONLINE for issuingCompanyCode 000ER00955
    And With this request I have a contract resiliated exception
    # Appel TP_OFFLINE
    When I get contrat PAUV5 for 1000611111111 '19590224' 1 '2023-07-15' '2023-07-18' 0000401166 TP_OFFLINE for issuingCompanyCode 000ER00955
    And With this request I have a contract resiliated exception

  @smokeTests @smokeTestsWithoutKafka @pauv5 @forcePAU @toChangeEveryYear @release
  Scenario: Modif des 2 contrats pour les résilier au 15/03/2025 + Appel avec une date de fin avant le début des contrats et avec le forçage d’activé
    # Appel HTP
    When I get contrat PAUV5 for 1000611111111 '19590224' 1 '2023-07-15' '2023-07-18' 0000401166 HTP for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_ONLINE
    When I get contrat PAUV5 for 1000611111111 '19590224' 1 '2023-07-15' '2023-07-18' 0000401166 TP_ONLINE for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_OFFLINE
    When I get contrat PAUV5 for 1000611111111 '19590224' 1 '2023-07-15' '2023-07-18' 0000401166 TP_OFFLINE for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-12-31 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-12-31 |

  @smokeTests @smokeTestsWithoutKafka @pauv5 @forcePAU
  Scenario: Modif des 2 contrats pour les résilier au 15/03/2025 + Appel après la date de résiliation sans forçage
    # Appel HTP
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2026-07-15' 0000401166 HTP for issuingCompanyCode 000ER00955
    And With this request I have a contract not found exception
    # Appel TP_ONLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2026-07-15' 0000401166 TP_ONLINE for issuingCompanyCode 000ER00955
    And With this request I have a contract resiliated exception
    # Appel TP_OFFLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2026-07-15' 0000401166 TP_OFFLINE for issuingCompanyCode 000ER00955
    And With this request I have a contract resiliated exception

  @smokeTests @smokeTestsWithoutKafka @pauv5 @forcePAU
  Scenario: Modif des 2 contrats pour les résilier au 15/03/2025 + Appel après la date de résiliation avec forçage
    # Appel HTP
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2026-07-15' 0000401166 HTP for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_ONLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2026-07-15' 0000401166 TP_ONLINE for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_OFFLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2026-07-15' 0000401166 TP_OFFLINE for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-12-31 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-12-31 |

  @smokeTests @smokeTestsWithoutKafka @pauv5 @forcePAU @toChangeEveryYear
  Scenario: Modif des 2 contrats pour les résilier au 15/03/2025 + Appel après la date de résiliation avec forçage mais avant la date de fin OFFLINE
    # Appel HTP
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2025-07-15' 0000401166 HTP for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2024-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_ONLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2025-07-15' 0000401166 TP_ONLINE for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is forced
    Then the contract with indice 1 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-01-01 |
      | fin   | 2025-03-15 |
    # Appel TP_OFFLINE
    When I get contrat PAUV5 without endDate for 1000611111111 '19590224' 1 '2025-07-15' 0000401166 TP_OFFLINE for issuingCompanyCode 000ER00955 and isForced true
    Then we found 2 contracts
    Then the contract with indice 0 is not forced
    Then the contract with indice 1 is not forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2025-07-15 |
      | fin   | 2025-12-31 |
    Then the contract with indice 1 and the right 0 data has this period on the product 0
      | debut | 2025-07-15 |
      | fin   | 2025-12-31 |
