Feature: Test contract V5 with  consolidation

  @smokeTests @caseConsolidation @caseRenouvellement
  Scenario: I renew a contract in 2021, 2022 and 2023 with multiple benef
    And I create a contract element from a file "gtaxa_cgdiv"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2021"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I create a service prestation from a file "servicePrestationGIB"

    When I renew the rights today with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2021-01-01 | 2021-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2021-01-01 | 2021-12-31 | 2022-12-31 |

    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2022"
    And I renew the rights today with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 8 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2022-01-01 | 2022-12-31 | 2022-12-31 |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 7
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2022-01-01 | 2022-12-31 | 2022-12-31 |

    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2023"
    And I renew the rights today with mode "RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 17 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 8
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2021-01-01 | 2021-12-31 | 2021-12-31 |
      | AXASCCGDIV | HOSP    | 2021-01-01 | 2021-12-31 | 2021-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 9
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 10
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIV | OPAU    | 2023-01-01 | 2023-12-31 | null      |
      | AXASCCGDIV | HOSP    | 2023-01-01 | 2023-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 11
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2021-01-01 | 2021-12-31 | 2021-12-31 |
      | AXASCCGDIV | HOSP    | 2021-01-01 | 2021-12-31 | 2021-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 12
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 13
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIV | OPAU    | 2023-01-01 | 2023-12-31 | null      |
      | AXASCCGDIV | HOSP    | 2023-01-01 | 2023-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 14
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2021-01-01 | 2021-12-31 | 2021-12-31 |
      | AXASCCGDIV | HOSP    | 2021-01-01 | 2021-12-31 | 2021-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 15
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
      | AXASCCGDIV | HOSP    | 2022-01-01 | 2022-12-31 | 2022-12-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 16
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIV | OPAU    | 2023-01-01 | 2023-12-31 | null      |
      | AXASCCGDIV | HOSP    | 2023-01-01 | 2023-12-31 | null      |

    And I wait for 1 contract
  Then the expected contract TP is identical to "contrattp_v5_renouvellement" content
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and refCouverture 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2021/01/01 | 2022/12/31 | ONLINE      |
#      | HOSP | 2021/01/01 | 2022/12/31 | OFFLINE     |
#      | OPAU | 2021/01/01 | 2022/12/31 | ONLINE      |
#      | OPAU | 2021/01/01 | 2022/12/31 | OFFLINE     |
#
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and refCouverture 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2023/01/01 | null       | ONLINE      |
#      | HOSP | 2023/01/01 | 2023/12/31 | OFFLINE     |
#      | OPAU | 2023/01/01 | null       | ONLINE      |
#      | OPAU | 2023/01/01 | 2023/12/31 | OFFLINE     |

