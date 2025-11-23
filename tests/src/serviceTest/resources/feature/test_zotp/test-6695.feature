Feature: Pau tdb -> pau TP online limit nature on online

  @todosmokeTests @smokeTestsWithKafka @pauv5
  Scenario: BLUE-6695
    Given I create a contrat from file "contract-6695"
    Given I create a beneficiaire from file "benef-6695"
    When I get contrat PAUV5 OTP for 2800682012094 '20160122' 1 '2025-01-02' '2025-01-03' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    Then the contract has 2 right
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code          | debut      | fin        |
      | PHARMACIE     | 2025-01-02 | 2025-01-03 |
      | SOINSEXTERNES | 2025-01-02 | 2025-01-03 |

    Then there is 2 benefitType for the right 1 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2025-01-02 | 2025-01-03 |
      | PHARMACIE       | 2025-01-02 | 2025-01-03 |

    When I get contrat PAUV5 OTP for 2800682012094 '20160122' 1 '2025-01-02' '2025-01-03' 0000401166 TP_ONLINE for domains HOSP
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    Then the contract has 1 right

    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2025-01-02 | 2025-01-03 |
      | PHARMACIE       | 2025-01-02 | 2025-01-03 |

    When I get contrat PAUV5 OTP for 2800682012094 '20160122' 1 '2025-01-02' '2025-01-03' 0000401166 TP_ONLINE for domains PHAR
    Then the contract number 0 data has values
      | insurerId         | 0000401166 |
      | suspensionPeriods | null       |
    Then the contract has 1 right
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code          | debut      | fin        |
      | PHARMACIE     | 2025-01-02 | 2025-01-03 |
      | SOINSEXTERNES | 2025-01-02 | 2025-01-03 |
