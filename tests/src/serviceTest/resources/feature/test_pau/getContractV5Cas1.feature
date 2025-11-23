Feature: Search contract PAU V5 BLUE-4863


  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 with Id beneficiary
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas1-contractEvent"
    And I create a contrat from file "contractForPauV5/cas1-contractEvent2"
    And I create a service prestation from a file "pauv5-cas1-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00011"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00013"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP for beneficiaryId P00011
    Then the contract number 0 data has values
      | number | C00010 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the contract number 0 data has values
      | number | C00010 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE for beneficiaryId P00013
    Then the contract number 0 data has values
      | number | D00010 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE for beneficiaryId P00012
    Then the contract number 0 data has values
      | number | C00010 |
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE for beneficiaryId P00013
    Then the contract number 0 data has values
      | number | D00010 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP for beneficiaryId P000XX
    Then With this request I have a beneficiary not found exception

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE for beneficiaryId P000XX
    Then With this request I have a beneficiary not found exception

    When I get contrat PAUV5 for 1591275115499 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE for beneficiaryId P000XX
    Then With this request I have a beneficiary not found exception

    When I get contrat PAUV5 for 1591275115499 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP for beneficiaryId P00011
    Then the contract number 0 data has values
      | number | C00010 |

    When I get contrat PAUV5 for 1591275115499 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the contract number 0 data has values
      | number | C00010 |

    When I get contrat PAUV5 for 1591275115499 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE for beneficiaryId P00013
    Then the contract number 0 data has values
      | number | D00010 |

    When I get contrat PAUV5 for 1591275115499 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP
    Then With this request I have a beneficiary not found exception

    When I get contrat PAUV5 for 1591275115499' '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE
    Then I have a beneficiary without subscriber not found exception

    When I get contrat PAUV5 for 1591275115499 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then I have a beneficiary without subscriber not found exception


    When I get contrat PAUV5 without Benef Info for '2018-01-02' '2023-01-03' 0000401166 HTP
    Then an error "400" is returned with message "Veuillez renseigner les critères de recherche du bénéficiaire"

    When I get contrat PAUV5 without Benef Info for '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE
    Then an error "400" is returned with message "Veuillez renseigner les critères de recherche du bénéficiaire"

    When I get contrat PAUV5 without Benef Info for '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then an error "400" is returned with message "Veuillez renseigner les critères de recherche du bénéficiaire"

