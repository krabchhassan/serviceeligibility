Feature: Search contract PAU V5 BLUE-4864


  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 with issuingCompanyCode
    And I create a contrat from file "contractForPauV5/cas2-contratEvent"
    And I create a contrat from file "contractForPauV5/cas2-contratEvent2"
    And I create a contrat from file "contractForPauV5/cas2-contratEvent3"
    And I create a contrat from file "contractForPauV5/cas2-contratEvent4"
    And I create a service prestation from a file "pauv5-cas2-contrat1-servicePrestation"
    And I create a service prestation from a file "pauv5-cas2-contrat2-servicePrestation"
    And I create a service prestation from a file "pauv5-cas2-contrat3-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauCas2"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP
    Then In the PAU, there is 3 contract
    Then the contract number 0 data has values
      | number | C00021 |
    Then the contract number 1 data has values
      | number | C00022 |
    Then the contract number 2 data has values
      | number | C00023 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP for issuingCompanyCode 0000401182
    Then In the PAU, there is 2 contract
    Then the contract number 0 data has values
      | number | C00021 |
    Then the contract number 1 data has values
      | number | C00023 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP AC00021 for issuingCompanyCode 0000401182
    Then In the PAU, there is 1 contract
    Then the contract number 0 data has values
      | number | C00023 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP AC00021 for issuingCompanyCode 1234567890
    Then With this request I have a beneficiary not found exception

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE
    Then In the PAU, there is 2 contract
    Then the contract number 0 data has values
      | number | C00024 |
    Then the contract number 1 data has values
      | number | C00023 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then In the PAU, there is 2 contract
    Then the contract number 0 data has values
      | number | C00024 |
    Then the contract number 1 data has values
      | number | C00023 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE for issuingCompanyCode 0000401182
    Then In the PAU, there is 2 contract
    Then the contract number 0 data has values
      | number | C00024 |
    Then the contract number 1 data has values
      | number | C00023 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE for issuingCompanyCode 0000401182
    Then In the PAU, there is 2 contract
    Then the contract number 0 data has values
      | number | C00024 |
    Then the contract number 1 data has values
      | number | C00023 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE AC00020 for issuingCompanyCode 0000401182
    Then In the PAU, there is 1 contract
    Then the contract number 0 data has values
      | number | C00022 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE AC00020 for issuingCompanyCode 0000401182
    Then In the PAU, there is 1 contract
    Then the contract number 0 data has values
      | number | C00022 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE AC00020 for issuingCompanyCode 1234567897
    Then With this request I have a beneficiary not found exception


    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE AC00020 for issuingCompanyCode 1234567897
    Then With this request I have a beneficiary not found exception

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE for issuingCompanyCode 1234567897
    Then I have a beneficiary without subscriber not found exception

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE for issuingCompanyCode 1234567897
    Then I have a beneficiary without subscriber not found exception

