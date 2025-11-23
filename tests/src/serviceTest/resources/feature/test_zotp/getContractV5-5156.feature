Feature: Search contract PAU V5 BLUE-5156 - TDB with new endpoint

  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv5tdb
  Scenario: Get PAU V5 TP_OFFLINE with Id beneficiary and domains on TDB
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-5156"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire-5156"
    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2023-01-15' '2023-01-20' 0000401166 TP_OFFLINE for beneficiaryId 0000452433-2125259

    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2023-01-15 | 2023-01-20 |
      | OPTIQUEAUDIO    | 2023-01-15 | 2023-01-20 |

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2023-01-15' '2023-01-20' 0000401166 TP_OFFLINE for domains HOSP for beneficiaryId 0000452433-2125259
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2023-01-15 | 2023-01-20 |
      | OPTIQUEAUDIO    | 2023-01-15 | 2023-01-20 |

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2023-01-15' '2023-01-20' 0000401166 TP_OFFLINE for domains OPTI for beneficiaryId 0000452433-2125259
    Then With this request I have a contract without any rights open exception

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2022-01-01' '2023-06-01' 0000401166 TP_OFFLINE for beneficiaryId 0000452433-2125259
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2022-01-01 | 2023-06-01 |
      | OPTIQUEAUDIO    | 2022-01-01 | 2023-06-01 |

    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-01-01 |
      | fin   | 2023-06-01 |

  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv5tdb
  Scenario: Get PAU V5 TP_ONLINE with Id beneficiary and domains on TDB
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-5156-online"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire-5156"
    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2023-01-15' '2023-01-20' 0000401166 TP_ONLINE for beneficiaryId 0000452433-2125259

    Then there is 6 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | AUDIOLOGIE      | 2023-01-15 | 2023-01-20 |
      | DENTAIRE        | 2023-01-15 | 2023-01-20 |
      | HOSPITALISATION | 2023-01-15 | 2023-01-20 |
      | MEDECINE        | 2023-01-15 | 2023-01-20 |
      | OPTIQUE         | 2023-01-15 | 2023-01-20 |
      | PHARMACIE       | 2023-01-15 | 2023-01-20 |

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2023-01-15' '2023-01-20' 0000401166 TP_ONLINE for domains HOSP for beneficiaryId 0000452433-2125259
    Then there is 6 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | AUDIOLOGIE      | 2023-01-15 | 2023-01-20 |
      | DENTAIRE        | 2023-01-15 | 2023-01-20 |
      | HOSPITALISATION | 2023-01-15 | 2023-01-20 |
      | MEDECINE        | 2023-01-15 | 2023-01-20 |
      | OPTIQUE         | 2023-01-15 | 2023-01-20 |
      | PHARMACIE       | 2023-01-15 | 2023-01-20 |

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2023-01-15' '2023-01-20' 0000401166 TP_ONLINE for domains OPTI for beneficiaryId 0000452433-2125259
    Then With this request I have a contract without any rights open exception

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2022-01-01' '2023-06-01' 0000401166 TP_ONLINE for beneficiaryId 0000452433-2125259
    Then there is 6 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | AUDIOLOGIE      | 2022-01-01 | 2023-06-01 |
      | DENTAIRE        | 2022-01-01 | 2023-06-01 |
      | HOSPITALISATION | 2022-01-01 | 2023-06-01 |
      | MEDECINE        | 2022-01-01 | 2023-06-01 |
      | OPTIQUE         | 2022-01-01 | 2023-06-01 |
      | PHARMACIE       | 2022-01-01 | 2023-06-01 |

    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-01-01 |
      | fin   | 2023-06-01 |

  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv5tdb
  Scenario: Get PAU V5 TP_ONLINE with Id beneficiary and domains on TDB on multiple version
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/contrat-5156-multipleversion"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire-5156"

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2022-01-01' '2022-12-31' 0000401166 TP_ONLINE for beneficiaryId 0000452433-2125259
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2022-01-01 | 2022-12-31 |
      | OPTIQUE         | 2022-01-01 | 2022-03-31 |
      | OPTIQUE         | 2022-07-01 | 2022-12-31 |

    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-01-01 |
      | fin   | 2022-12-31 |

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2022-01-01' '2022-12-31' 0000401166 TP_OFFLINE for beneficiaryId 0000452433-2125259
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2022-01-01 | 2022-12-31 |
      | OPTIQUE         | 2022-01-01 | 2022-03-31 |
      | OPTIQUE         | 2022-07-01 | 2022-12-31 |

    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-01-01 |
      | fin   | 2022-12-31 |

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2022-02-01' '2022-07-05' 0000401166 TP_ONLINE for beneficiaryId 0000452433-2125259
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | HOSPITALISATION | 2022-02-01 | 2022-07-05 |
      | OPTIQUE         | 2022-02-01 | 2022-03-31 |
      | OPTIQUE         | 2022-07-01 | 2022-07-05 |

    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-02-01 |
      | fin   | 2022-07-05 |


  @todosmokeTests @smokeTestsOTPWithoutKafka @pauv5tdb
  Scenario: Get PAU V5 TP with multiple contract
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas1-contractTDB"
    And I create a contrat from file "contractForPauV5/cas1-contrat2-TDB"
    And I create a contrat from file "contractForPauV5/cas2-contractTDB"
    And I create a contrat from file "contractForPauV5/cas3-contractTDB"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00011"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2022-01-01' '2022-12-31' 0000401166 TP_ONLINE
    Then there is 7 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | AUDIOLOGIE      | 2022-01-01 | 2022-12-31 |
      | DENTAIRE        | 2022-01-01 | 2022-12-31 |
      | HOSPITALISATION | 2022-01-01 | 2022-12-31 |
      | MEDECINE        | 2022-01-01 | 2022-12-31 |
      | OPTIQUE         | 2022-01-01 | 2022-12-31 |
      | PHARMACIE       | 2022-01-01 | 2022-12-31 |
      | RADIOLOGIE      | 2022-01-01 | 2022-12-31 |

    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-01-01 |
      | fin   | 2022-12-31 |

    When I get contrat PAUV5 OTP for 1591275115460 '19621023' 1 '2022-01-01' '2022-12-31' 0000401166 TP_OFFLINE
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut      | fin        |
      | DENTAIRE        | 2022-01-01 | 2022-12-31 |
      | HOSPITALISATION | 2022-01-01 | 2022-12-31 |
      | OPTIQUE         | 2022-01-01 | 2022-12-31 |

    Then the contract and the right 0 data has this period on the product 0
      | debut | 2022-01-01 |
      | fin   | 2022-12-31 |
