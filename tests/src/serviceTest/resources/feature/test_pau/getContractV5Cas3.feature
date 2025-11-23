Feature: Search contract PAU V5 BLUE-4838

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get PAU V5 TP_OFFLINE almost like TP_ONLINE
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas3-contratEvent"
    And I create a contrat from file "contractForPauV5/cas3-contratEvent2"
    And I create a service prestation from a file "pauv5-cas3-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00031"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00032"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00033"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE C00030
    Then the contract number 0 data has values
      | number | C00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00032 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401182 TP_OFFLINE C00030
    Then the contract number 0 data has values
      | number | C00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00032 |

    When I get contrat PAUV5 for 1591275115461 '19621023' 1 '2018-01-02' '2023-01-03' 0000401182 TP_OFFLINE C00030
    Then the contract number 0 data has values
      | number | C00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00032 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | number | C00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00032 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401182 TP_OFFLINE
    Then the contract number 0 data has values
      | number | C00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00032 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE C0003X
    Then the contract number 0 data has values
      | number | C00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00032 |

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401182 TP_ONLINE C0003X
    Then the contract number 0 data has values
      | number | C00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00032 |

    When I get contrat PAUV5 for 2770662498046 '19770224' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE D00030
    Then the contract number 0 data has values
      | number | D00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00033 |

    When I get contrat PAUV5 for 2770662498046 '19770224' 1 '2018-01-02' '2023-01-03' 0000401182 TP_OFFLINE D00030
    Then the contract number 0 data has values
      | number | D00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00033 |

    When I get contrat PAUV5 for 2770662498046 '19770224' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE D00030
    Then the contract number 0 data has values
      | number | D00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00033 |

    When I get contrat PAUV5 for 2770662498046 '19770224' 1 '2018-01-02' '2023-01-03' 0000401182 TP_OFFLINE D00030
    Then the contract number 0 data has values
      | number | D00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00033 |

    When I get contrat PAUV5 for 2770662498046 '19770224' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the contract number 0 data has values
      | number | D00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00033 |

    When I get contrat PAUV5 for 2770662498046 '19770224' 1 '2018-01-02' '2023-01-03' 0000401182 TP_OFFLINE
    Then the contract number 0 data has values
      | number | D00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00033 |

    When I get contrat PAUV5 for 2770662498046 '19770224' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE D00040
    Then the contract number 0 data has values
      | number | D00030 |
    Then the contract number 0 data has this personNumber
      | personNumber | 0000401166-P00033 |
