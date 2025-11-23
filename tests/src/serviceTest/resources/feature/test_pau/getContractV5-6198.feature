Feature: Search contract PAU V5 BLUE-6198


  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 without end date
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas1-contractEvent"
    And I create a contrat from file "contractForPauV5/cas1-contractEvent2"
    And I create a contrat from file "contractForPauV5/cas1-contrat2-Event"
    And I create a service prestation from a file "pauv5-6198-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00011"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00013"
    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2018-01-02' 0000401166 HTP
    Then the contract number 0 data has values
      | number | C00010 |

    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2018-01-02' 0000401166 TP_ONLINE
    Then the contract number 0 data has values
      | number | D00010 |
    Then the contract number 1 data has values
      | number | D00011 |

    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2018-01-02' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | number | D00010 |
    Then the contract number 1 data has values
      | number | D00011 |

    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2023-01-02' 0000401166 HTP
    Then With this request I have a contract not found exception

    # with subscriberId Veuillez faire la recherche de droits en renseignant le n° d’adhérent
    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2023-01-02' 0000401166 TP_ONLINE 00052492
    Then the contract number 0 data has values
      | number | D00011 |

    When I get contrat PAUV5 without endDate for 1591275115460 '19621023' 1 '2023-01-02' 0000401166 TP_OFFLINE 00052492
    Then the contract number 0 data has values
      | number | D00011 |


