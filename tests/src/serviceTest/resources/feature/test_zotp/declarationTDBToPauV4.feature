Feature: Pau v4 tdb -> pau TP online

  @todosmokeTests @smokeTestsOTPWithKafka @pauv5 @tdbToPauV4 @caseConsolidation
  Scenario: Les mariés de Marianne
    Given I create a declaration from a file "declarationForPauV4/01-declaration-mari"
    And I create a declaration from a file "declarationForPauV4/01-declaration-femme"
    And I create a beneficiaire from file "beneficiaryForPauV4/01-benef-1"
    And I create a beneficiaire from file "beneficiaryForPauV4/01-benef-2"
    Then I wait for 2 contracts
    When I get contrat PAUV5 OTP for 1810131111111 '19810101' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE A0001
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00010     |
    When I get contrat PAUV5 OTP for 2810631555490 '19810619' 1 '2023-06-01' '2023-06-01' 0000401166 TP_OFFLINE A0001
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00011     |
    When I get contrat PAUV5 OTP for 1810131111112 '19810101' 1 '2023-06-01' '2023-06-01' 0000401166 TP_OFFLINE
    Then I have a beneficiary without subscriber not found exception
    When I get contrat PAUV5 OTP for 1810131111112 '19810101' 1 '2023-06-01' '2023-06-01' 0000401166 TP_OFFLINE A0001
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00010     |

  @todosmokeTests @smokeTestsOTPWithKafka @pauv5 @tdbToPauV4 @caseConsolidation
  Scenario: Les jumeaux de Marianne

    And I create a declaration from a file "declarationForPauV4/05-declaration-pere"
    And I create a declaration from a file "declarationForPauV4/05-declaration-mere"
    And I create a declaration from a file "declarationForPauV4/05-declaration-jumeau1"
    And I create a declaration from a file "declarationForPauV4/05-declaration-jumeau2"
    And I create a beneficiaire from file "beneficiaryForPauV4/jumeaux-benef-pere"
    And I create a beneficiaire from file "beneficiaryForPauV4/jumeaux-benef-mere"
    And I create a beneficiaire from file "beneficiaryForPauV4/jumeaux-benef-enfant1"
    And I create a beneficiaire from file "beneficiaryForPauV4/jumeaux-benef-enfant2"
    Then I wait for 2 contracts
    When I get contrat PAUV5 OTP for 2740131111112 '20101010' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-00053 |
    When I get contrat PAUV5 OTP for 2740131111112 '20101010' 2 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-00054 |
    When I get contrat PAUV5 OTP for 1720131111112 '20101010' 2 '2023-06-01' '2023-06-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-00054 |
    When I get contrat PAUV5 OTP for 2101031111113 '20101010' 1 '2023-06-01' '2023-06-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-00053 |
    When I get contrat PAUV5 OTP for 2101031111114 '20101010' 2 '2023-06-01' '2023-06-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-00054 |

  @todosmokeTests @smokeTestsOTPWithKafka @pauv5 @tdbToPauV4 @caseConsolidation
  Scenario: BLUE-4830 : Date de naissance non mis à jour sur le contrats TP

    And I create a declaration from a file "declarationForPauV4/02-declaration-1"
    And I create a beneficiaire from file "beneficiaryForPauV4/02-benef-2"
    Then I wait for 1 contract
    When I get contrat PAUV5 OTP for 1700131111111 '19700103' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A0002      |
    Given I create a declaration from a file "declarationForPauV4/02-declaration-2"
    Then I wait for 1 contract
    When I get contrat PAUV5 OTP for 1700131111111 '19700130' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A0002      |

  @todosmokeTests @smokeTestsOTPWithKafka @pauv5 @tdbToPauV4 @caseConsolidation
  Scenario: BLUE-4830 : Rang de naissance non mis à jour sur le contrats TP

    And I create a declaration from a file "declarationForPauV4/03-declaration-1"
    And I create a beneficiaire from file "beneficiaryForPauV4/03-benef-2"
    Then I wait for 1 contract
    When I get contrat PAUV5 OTP for 1710131111111 '19710103' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A0003      |
    Given I create a declaration from a file "declarationForPauV4/03-declaration-2"
    Then I wait for 1 contract
    When I get contrat PAUV5 OTP for 1710131111111 '19710103' 2 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A0003      |

  @todosmokeTests @smokeTestsOTPWithKafka @pauv5 @tdbToPauV4 @caseConsolidation
  Scenario: BLUE-4829 Probleme sur la mise à jour du beneficiaire à la reception d'un 2eme contrat TP

    And I create a declaration from a file "declarationForPauV4/08-declaration-contrat-1"
    And I create a beneficiaire from file "beneficiaryForPauV4/08-benef-1"

    Then I wait for 1 contract
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE for domains OPTI
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    Given I create a declaration from a file "declarationForPauV4/08-declaration-contrat-2"
    Then I wait for 2 contracts
    And I drop the collection for Beneficiary
    And I create a beneficiaire from file "beneficiaryForPauV4/08-benef-2"
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE for domains MEDG
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00082     |
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE for domains OPTI
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-01-02' '2023-01-02' 0000401166 TP_OFFLINE for domains PHAR
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-06-02' '2023-06-02' 0000401166 TP_OFFLINE for domains PHAR
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-12-15' '2023-12-15' 0000401166 TP_OFFLINE for domains PHAR
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-12-15' '2023-12-15' 0000401166 TP_OFFLINE
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-12-15' '2023-12-15' 0000401166 TP_OFFLINE for domains PHAR,MEDG
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    When I get contrat PAUV5 OTP for 1750131111111 '19750103' 1 '2023-12-15' '2023-12-15' 0000401166 TP_OFFLINE A00081
    Then we found 1 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |

  @todosmokeTests @smokeTestsOTPWithKafka @pauv5 @tdbToPauV4 @caseConsolidation
  Scenario: Tri : Contrat portant la garantie ayant la priorité la plus basse

    And I create a declaration from a file "declarationForPauV4/declaration-PourTriGarantie1"
    And I create a declaration from a file "declarationForPauV4/declaration-PourTriGarantie2"
    And I create a declaration from a file "declarationForPauV4/declaration-PourTriGarantie3"
    And I create a beneficiaire from file "beneficiaryForPauV4/benef-3contrats"
    Then I wait for 3 contracts
    When I get contrat PAUV5 OTP for 1750131111112 '19750103' 1 '2023-12-01' '2023-12-01' 0000401166 TP_OFFLINE A00082
    Then we found 3 contracts
    Then the contract number 0 data has values
      | insurerId | 0000401166 |
      | number    | A00083     |
    Then the contract number 1 data has values
      | insurerId | 0000401166 |
      | number    | A00081     |
    Then the contract number 2 data has values
      | insurerId | 0000401166 |
      | number    | A00082     |
    Then the contract and the right 0 after sort data has values
      | insurerId           | 0000401166 |
      | codeGT              | GT_BASE2   |
      | prioritizationOrder | 02         |
    Then the contract and the right 1 after sort data has values
      | insurerId           | 0000401166 |
      | codeGT              | GT_BASE    |
      | prioritizationOrder | 03         |
