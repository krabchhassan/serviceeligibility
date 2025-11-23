Feature: Test contract V5 de renouvellement à date anniversaire contrat manuel en RDO
#  Use cases : https://cegedim-insurance.atlassian.net/wiki/spaces/AIN/pages/4174512129/Uses+Cases+du+renouvellement+manuel+en+mode+Anniversaire+Contrat

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation @release
  Scenario: I renew a contract in 2021 starting on the 2001-01-01
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"

#    Résultats sur les droits TP 2021 :1a  01/01/2001  01/01/2021  31/12/2021  On doit renouveler toute l’année 2021
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO1a"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | HOSP    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | PHAR    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | OPTI    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | APDE    | 2021-01-01 | 2021-12-31 |

  #  Résultats sur les droits TP 2022 :1a  01/01/2001  01/01/2022  31/12/2022  On doit renouveler toute l’année 2022
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | APDE    | 2022-01-01 | 2022-12-31 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | APDE    | 2022-01-01 | 2022-12-31 |

#  Résultats sur les droits TP 2023 :1a  01/01/2001  01/01/2023  31/12/2023  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 6 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | HOSP    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | PHAR    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | OPTI    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | APDE    | 2023-01-01 | 2023-12-31 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | HOSP    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | PHAR    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | OPTI    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | APDE    | 2023-01-01 | 2023-12-31 |

    And I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat1" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/01/01 | null       | ONLINE      |
#      | APDE | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | DENT | 2021/01/01 | null       | ONLINE      |
#      | DENT | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | HOSP | 2021/01/01 | null       | ONLINE      |
#      | HOSP | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | OPTI | 2021/01/01 | null       | ONLINE      |
#      | OPTI | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | PHAR | 2021/01/01 | null       | ONLINE      |
#      | PHAR | 2021/01/01 | 2023/12/31 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2021-01-01

#    Résultats sur les droits TP 2021 :1b  01/01/2021  01/01/2021  31/12/2021  On doit renouveler toute l’année 2021
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO1b"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | HOSP    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | PHAR    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | OPTI    | 2021-01-01 | 2021-12-31 |
      | GT_BASEB | APDE    | 2021-01-01 | 2021-12-31 |

#  Résultats sur les droits TP 2022 :1b  01/01/2021  01/01/2022  31/12/2022  On doit renouveler toute l’année 2022
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | APDE    | 2022-01-01 | 2022-12-31 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | APDE    | 2022-01-01 | 2022-12-31 |

#  Résultats sur les droits TP 2023 :1b  01/01/2001  01/01/2023  31/12/2023  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 6 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | HOSP    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | PHAR    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | OPTI    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | APDE    | 2023-01-01 | 2023-12-31 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | HOSP    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | PHAR    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | OPTI    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | APDE    | 2023-01-01 | 2023-12-31 |

    And I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat2" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/01/01 | null       | ONLINE      |
#      | APDE | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | DENT | 2021/01/01 | null       | ONLINE      |
#      | DENT | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | HOSP | 2021/01/01 | null       | ONLINE      |
#      | HOSP | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | OPTI | 2021/01/01 | null       | ONLINE      |
#      | OPTI | 2021/01/01 | 2023/12/31 | OFFLINE     |
#      | PHAR | 2021/01/01 | null       | ONLINE      |
#      | PHAR | 2021/01/01 | 2023/12/31 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2022-01-01
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"

#    Résultats sur les droits TP 2021 :1c  01/01/2022 Aucune  Les droits TP seront pris en compte par le renouvellement 2022
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO1c"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 0 declarations

    When I remove all TP card parameters from database

#  Résultats sur les droits TP 2022 :1c  01/01/2022  01/01/2022  31/12/2022  On doit renouveler toute l’année 2022
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | APDE    | 2022-01-01 | 2022-12-31 |


    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASEB | APDE    | 2022-01-01 | 2022-12-31 |

    When I remove all TP card parameters from database

#  Résultats sur les droits TP 2023 :1c  01/01/2022  01/01/2023  31/12/2023  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | HOSP    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | PHAR    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | OPTI    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | APDE    | 2023-01-01 | 2023-12-31 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | HOSP    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | PHAR    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | OPTI    | 2023-01-01 | 2023-12-31 |
      | GT_BASEB | APDE    | 2023-01-01 | 2023-12-31 |

    And I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat3" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/01/01 | null       | ONLINE      |
#      | APDE | 2022/01/01 | 2023/12/31 | OFFLINE     |
#      | DENT | 2022/01/01 | null       | ONLINE      |
#      | DENT | 2022/01/01 | 2023/12/31 | OFFLINE     |
#      | HOSP | 2022/01/01 | null       | ONLINE      |
#      | HOSP | 2022/01/01 | 2023/12/31 | OFFLINE     |
#      | OPTI | 2022/01/01 | null       | ONLINE      |
#      | OPTI | 2022/01/01 | 2023/12/31 | OFFLINE     |
#      | PHAR | 2022/01/01 | null       | ONLINE      |
#      | PHAR | 2022/01/01 | 2023/12/31 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2001-02-15

# Résultats sur les droits TP 2021 : 2a 15/02/2001  15/02/2021  14/02/2022  En mode RDO on ne génère les droits que pour une seule année
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO2a"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | HOSP    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | PHAR    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | OPTI    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | APDE    | 2021-02-15 | 2022-02-14 |

# Résultats sur les droits TP 2022 : 2a 15/02/2001  15/02/2022  14/02/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | APDE    | 2022-02-15 | 2023-02-14 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | APDE    | 2022-02-15 | 2023-02-14 |

# Résultats sur les droits TP 2023 : 2a 15/02/2001  15/02/2023  14/02/2024  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 6 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | HOSP    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | PHAR    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | OPTI    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | APDE    | 2023-02-15 | 2024-02-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | HOSP    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | PHAR    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | OPTI    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | APDE    | 2023-02-15 | 2024-02-14 |

    And I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat4" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/02/15 | null       | ONLINE      |
#      | APDE | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | DENT | 2021/02/15 | null       | ONLINE      |
#      | DENT | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | HOSP | 2021/02/15 | null       | ONLINE      |
#      | HOSP | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | OPTI | 2021/02/15 | null       | ONLINE      |
#      | OPTI | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | PHAR | 2021/02/15 | null       | ONLINE      |
#      | PHAR | 2021/02/15 | 2024/02/14 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2021-02-15

# Résultats sur les droits TP 2021 : 2b 15/02/2021  15/02/2021  14/02/2022  En mode RDO on ne génère les droits que pour une seule année
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO2b"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | HOSP    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | PHAR    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | OPTI    | 2021-02-15 | 2022-02-14 |
      | GT_BASEB | APDE    | 2021-02-15 | 2022-02-14 |

# Résultats sur les droits TP 2022 : 2b 15/02/2021  15/02/2022  14/02/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | APDE    | 2022-02-15 | 2023-02-14 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | APDE    | 2022-02-15 | 2023-02-14 |

# Résultats sur les droits TP 2023 : 2b 15/02/2021  15/02/2023  14/02/2024  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 6 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | HOSP    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | PHAR    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | OPTI    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | APDE    | 2023-02-15 | 2024-02-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | HOSP    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | PHAR    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | OPTI    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | APDE    | 2023-02-15 | 2024-02-14 |

    And I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat5" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/02/15 | null       | ONLINE      |
#      | APDE | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | DENT | 2021/02/15 | null       | ONLINE      |
#      | DENT | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | HOSP | 2021/02/15 | null       | ONLINE      |
#      | HOSP | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | OPTI | 2021/02/15 | null       | ONLINE      |
#      | OPTI | 2021/02/15 | 2024/02/14 | OFFLINE     |
#      | PHAR | 2021/02/15 | null       | ONLINE      |
#      | PHAR | 2021/02/15 | 2024/02/14 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2022-02-15

# Résultats sur les droits TP 2021 : 2c 15/02/2022  Aucune  Les droits TP seront pris en compte par le renouvellement 2022
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO2c"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 0 declarations

# Résultats sur les droits TP 2022 : 2c 15/02/2022  15/02/2022  14/02/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | APDE    | 2022-02-15 | 2023-02-14 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASEB | APDE    | 2022-02-15 | 2023-02-14 |

# Résultats sur les droits TP 2023 : 2c 15/02/2021  15/02/2023  14/02/2024  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | HOSP    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | PHAR    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | OPTI    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | APDE    | 2023-02-15 | 2024-02-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | HOSP    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | PHAR    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | OPTI    | 2023-02-15 | 2024-02-14 |
      | GT_BASEB | APDE    | 2023-02-15 | 2024-02-14 |

    And I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat6" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/02/15 | null       | ONLINE      |
#      | APDE | 2022/02/15 | 2024/02/14 | OFFLINE     |
#      | DENT | 2022/02/15 | null       | ONLINE      |
#      | DENT | 2022/02/15 | 2024/02/14 | OFFLINE     |
#      | HOSP | 2022/02/15 | null       | ONLINE      |
#      | HOSP | 2022/02/15 | 2024/02/14 | OFFLINE     |
#      | OPTI | 2022/02/15 | null       | ONLINE      |
#      | OPTI | 2022/02/15 | 2024/02/14 | OFFLINE     |
#      | PHAR | 2022/02/15 | null       | ONLINE      |
#      | PHAR | 2022/02/15 | 2024/02/14 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2001-07-15

# Résultats sur les droits TP 2021 : 3a 15/07/2001  15/07/2021  14/07/2022  En mode RDO on ne génère les droits que pour une seule année
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO3a"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | HOSP    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | PHAR    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | OPTI    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | APDE    | 2021-07-15 | 2022-07-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | HOSP    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | PHAR    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | OPTI    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | APDE    | 2021-07-15 | 2022-07-14 |

# Résultats sur les droits TP 2022 : 3a 15/07/2001  15/07/2022  14/07/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | APDE    | 2022-07-15 | 2023-07-14 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | APDE    | 2022-07-15 | 2023-07-14 |

# Résultats sur les droits TP 2023 : 3a 15/07/2001  15/07/2023  14/07/2024  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 6 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | HOSP    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | PHAR    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | OPTI    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | APDE    | 2023-07-15 | 2024-07-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | HOSP    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | PHAR    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | OPTI    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | APDE    | 2023-07-15 | 2024-07-14 |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat7" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/07/15 | null       | ONLINE      |
#      | APDE | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | DENT | 2021/07/15 | null       | ONLINE      |
#      | DENT | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | HOSP | 2021/07/15 | null       | ONLINE      |
#      | HOSP | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | OPTI | 2021/07/15 | null       | ONLINE      |
#      | OPTI | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | PHAR | 2021/07/15 | null       | ONLINE      |
#      | PHAR | 2021/07/15 | 2024/07/14 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2021-07-15

# Résultats sur les droits TP 2021 : 3b 15/07/2021  15/07/2021  14/07/2022  En mode RDO on ne génère les droits que pour une seule année
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO3b"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | HOSP    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | PHAR    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | OPTI    | 2021-07-15 | 2022-07-14 |
      | GT_BASEB | APDE    | 2021-07-15 | 2022-07-14 |

# Résultats sur les droits TP 2022 : 3b 15/07/2021  15/07/2022  14/07/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | APDE    | 2022-07-15 | 2023-07-14 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | APDE    | 2022-07-15 | 2023-07-14 |

# Résultats sur les droits TP 2023 : 3b 15/07/2021  15/07/2023  14/07/2024  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 6 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | HOSP    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | PHAR    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | OPTI    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | APDE    | 2023-07-15 | 2024-07-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | HOSP    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | PHAR    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | OPTI    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | APDE    | 2023-07-15 | 2024-07-14 |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat8" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/07/15 | null       | ONLINE      |
#      | APDE | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | DENT | 2021/07/15 | null       | ONLINE      |
#      | DENT | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | HOSP | 2021/07/15 | null       | ONLINE      |
#      | HOSP | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | OPTI | 2021/07/15 | null       | ONLINE      |
#      | OPTI | 2021/07/15 | 2024/07/14 | OFFLINE     |
#      | PHAR | 2021/07/15 | null       | ONLINE      |
#      | PHAR | 2021/07/15 | 2024/07/14 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2022-07-15

# Résultats sur les droits TP 2021 : 3c 15/07/2022  Aucune  Les droits TP seront pris en compte par le renouvellement 2022
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO3c"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 0 declarations

# Résultats sur les droits TP 2022 : 3c 15/07/2022  15/07/2022  14/07/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | APDE    | 2022-07-15 | 2023-07-14 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASEB | APDE    | 2022-07-15 | 2023-07-14 |

# Résultats sur les droits TP 2023 : 3c 15/07/2022  15/07/2023  14/07/2024  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on renouvelle pour l'année 2023
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | HOSP    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | PHAR    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | OPTI    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | APDE    | 2023-07-15 | 2024-07-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | HOSP    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | PHAR    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | OPTI    | 2023-07-15 | 2024-07-14 |
      | GT_BASEB | APDE    | 2023-07-15 | 2024-07-14 |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat9" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/15 | null       | ONLINE      |
#      | APDE | 2022/07/15 | 2024/07/14 | OFFLINE     |
#      | DENT | 2022/07/15 | null       | ONLINE      |
#      | DENT | 2022/07/15 | 2024/07/14 | OFFLINE     |
#      | HOSP | 2022/07/15 | null       | ONLINE      |
#      | HOSP | 2022/07/15 | 2024/07/14 | OFFLINE     |
#      | OPTI | 2022/07/15 | null       | ONLINE      |
#      | OPTI | 2022/07/15 | 2024/07/14 | OFFLINE     |
#      | PHAR | 2022/07/15 | null       | ONLINE      |
#      | PHAR | 2022/07/15 | 2024/07/14 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2001-12-31

# Résultats sur les droits TP 2021 : 4a 31/12/2001  31/12/2021  30/12/2022  En mode RDO on ne génère les droits que pour une seule année
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO4a"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | HOSP    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | PHAR    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | OPTI    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | APDE    | 2021-12-31 | 2022-12-30 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | HOSP    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | PHAR    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | OPTI    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | APDE    | 2021-12-31 | 2022-12-30 |

# Résultats sur les droits TP 2022 : 4a 31/12/2001  31/12/2022  30/12/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | HOSP    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | PHAR    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | OPTI    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | APDE    | 2022-12-31 | 2023-12-30 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | HOSP    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | PHAR    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | OPTI    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | APDE    | 2022-12-31 | 2023-12-30 |

# Résultats sur les droits TP 2023 : 4a 31/12/2001  Etant donné que le délai de renouvellement par rapport à la date d’exécution est dépassé on ne renouvelle pas l’année 2023 => 2024
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 4 declarations

    Then I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat10" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/12/31 | null       | ONLINE      |
#      | APDE | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | DENT | 2021/12/31 | null       | ONLINE      |
#      | DENT | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | HOSP | 2021/12/31 | null       | ONLINE      |
#      | HOSP | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | OPTI | 2021/12/31 | null       | ONLINE      |
#      | OPTI | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | PHAR | 2021/12/31 | null       | ONLINE      |
#      | PHAR | 2021/12/31 | 2023/12/30 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2021-12-31

# Résultats sur les droits TP 2021 : 4b 31/12/2021  31/12/2021  30/12/2022  En mode RDO on ne génère les droits que pour une seule année
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO4b"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | HOSP    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | PHAR    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | OPTI    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | APDE    | 2021-12-31 | 2022-12-30 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | HOSP    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | PHAR    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | OPTI    | 2021-12-31 | 2022-12-30 |
      | GT_BASEB | APDE    | 2021-12-31 | 2022-12-30 |

# Résultats sur les droits TP 2022 : 4b 31/12/2021  31/12/2022  30/12/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 4 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | HOSP    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | PHAR    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | OPTI    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | APDE    | 2022-12-31 | 2023-12-30 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | HOSP    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | PHAR    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | OPTI    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | APDE    | 2022-12-31 | 2023-12-30 |

# Résultats sur les droits TP 2023 : 4b 31/12/2021  Etant donné que le délai de renouvellement par rapport à la date d’exécution est dépassé on ne renouvelle pas l’année 2023 => 2024
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 4 declarations

    Then I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat11" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2021/12/31 | null       | ONLINE      |
#      | APDE | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | DENT | 2021/12/31 | null       | ONLINE      |
#      | DENT | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | HOSP | 2021/12/31 | null       | ONLINE      |
#      | HOSP | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | OPTI | 2021/12/31 | null       | ONLINE      |
#      | OPTI | 2021/12/31 | 2023/12/30 | OFFLINE     |
#      | PHAR | 2021/12/31 | null       | ONLINE      |
#      | PHAR | 2021/12/31 | 2023/12/30 | OFFLINE     |

  @smokeTests @genDroitsAnnivManuelRDO @caseConsolidation
  Scenario: I renew a contract in 2021, 2022 and 2023 starting on the 2022-12-31

# Résultats sur les droits TP 2021 : 4c 31/12/2022  Aucune  Les droits TP seront pris en compte par le renouvellement 2022
    Given I create a contract element from a file "gtbasebalooCase1B"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2021" with date "2023-06-20"
    When I create a service prestation from a file "servicePrestationAnnivManuelRDO4c"

    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 0 declarations

# Résultats sur les droits TP 2022 : 4c 31/12/2022  31/12/2022  30/12/2023  En mode RDO on ne génère les droits que pour une seule année
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2022" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | HOSP    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | PHAR    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | OPTI    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | APDE    | 2022-12-31 | 2023-12-30 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASEB | DENT    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | HOSP    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | PHAR    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | OPTI    | 2022-12-31 | 2023-12-30 |
      | GT_BASEB | APDE    | 2022-12-31 | 2023-12-30 |

# Résultats sur les droits TP 2023 : 4c 31/12/2022  Etant donné que le délai de renouvellement par rapport à la date d’exécution est dépassé on ne renouvelle pas l’année 2023 => 2024
    When I remove all TP card parameters from database
    And I create manual TP parameters from file "parametrageTPBalooAnniversaireManuel2023" with date "2023-06-20"
    When I renew the rights on "2023-06-20" with mode "RDO"

    Then I wait for 2 declarations

    Then I wait for 1 contract
    Then the expected contract TP is identical to "genDroitsAnnivManuel/rdo/contrat12" content
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/12/31 | null       | ONLINE      |
#      | APDE | 2022/12/31 | 2023/12/30 | OFFLINE     |
#      | DENT | 2022/12/31 | null       | ONLINE      |
#      | DENT | 2022/12/31 | 2023/12/30 | OFFLINE     |
#      | HOSP | 2022/12/31 | null       | ONLINE      |
#      | HOSP | 2022/12/31 | 2023/12/30 | OFFLINE     |
#      | OPTI | 2022/12/31 | null       | ONLINE      |
#      | OPTI | 2022/12/31 | 2023/12/30 | OFFLINE     |
#      | PHAR | 2022/12/31 | null       | ONLINE      |
#      | PHAR | 2022/12/31 | 2023/12/30 | OFFLINE     |

