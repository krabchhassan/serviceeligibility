Feature: Test contract V5 manual renewal on anniversary date when PW modification
#  Use cases : https://cegedim-insurance.atlassian.net/wiki/spaces/AIN/pages/4174512129/Uses+Cases+du+renouvellement+manuel+en+mode+Anniversaire+Contrat

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a declarant from a file "declarantbaloo"

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @release
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°1 (January 2022)
# Résultats sur les droits TP : 1a 01/01/n-1  01/01/n 31/12/n On doit renouveler toute l’année n

    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"
    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-01-01"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations

    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-12-31 |

    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-12-31 |

    And I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat1" content

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract Use Case n°1 (January 2022)
# Résultats sur les droits TP : 1b 01/01/n  01/01/n 31/12/n On doit renouveler toute l’année n

    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-01-01"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-12-31 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-12-31 |

    And I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat2" content

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract with dateSouscription after renewal Use Case n°1 (January 2022)
# Résultats sur les droits TP : 1c 01/01/n+1  Aucune  Les droits TP seront pris en compte par le renouvellement n+1

    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-01-01"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for 0 declaration

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°2 (January 2022)
# Résultats sur les droits TP : 2a 15/02/n-1  01/01/n  14/02/n+1  Comme les droits TP sont à cheval sur plusieurs années il faut donc pour les assurés de ce contrat renouveler plusieurs années, étant donné que le délai de renouvellement par rapport à la date d’exécution est dépassé
    Given I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"
    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-02-15"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-01-01 | 2023-02-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-01-01 | 2023-02-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat3" content


  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°2 (January 2024)
# Résultats sur les droits TP : 2a 15/02/2023  01/01/2024  14/02/2025  Comme les droits TP sont à cheval sur plusieurs années il faut donc pour les assurés de ce contrat renouveler plusieurs années, étant donné que le délai de renouvellement par rapport à la date d’exécution est dépassé

    Given I create a contract element from a file "gtbasebalooCase1Bis"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2024-03"

    When I create a service prestation from a file "servicePrestationAnniversaire1" with the dateSouscription "2023-02-15"

    When I renew the rights on "2024-02-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | 5476     | DENT    | 2024-01-01 | 2025-02-14 |
      | 5476     | HOSP    | 2024-01-01 | 2025-02-14 |
      | 5476     | PHAR    | 2024-01-01 | 2025-02-14 |
      | 5476     | AUDI    | 2024-01-01 | 2025-02-14 |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | 5476     | DENT    | 2024-01-01 | 2025-02-14 |
      | 5476     | HOSP    | 2024-01-01 | 2025-02-14 |
      | 5476     | PHAR    | 2024-01-01 | 2025-02-14 |
      | 5476     | AUDI    | 2024-01-01 | 2025-02-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat4" content

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract Use Case n°2 (January 2022)
# Résultats sur les droits TP : 2b 15/02/n  15/02/n  14/02/n+1  Comme les droits TP sont à cheval sur plusieurs années il faut donc pour les assurés de ce contrat renouveler plusieurs années

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-02-15"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-02-15 | 2023-02-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-02-15 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-02-15 | 2023-02-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat5" content

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract dateSouscription after renewal Use Case n°2 (January 2022)
# Résultats sur les droits TP : 2c 15/02/n+1  Aucune  Les droits TP seront pris en compte par le renouvellement n+1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-02-15"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for 0 declaration

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°3 (January 2022)
# Résultats sur les droits TP : 3a 15/07/n-1  01/01/n 14/07/n Comme les droits TP sont à cheval sur plusieurs années il faut donc pour les assurés de ce contrat renouveler plusieurs années, Mais étant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on ne renouvelle pas l’année n+ 1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-07-15"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-07-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-07-14 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-07-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat6" content

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract Use Case n°3 (January 2022)
# Résultats sur les droits TP : 3b 15/07/n  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on ne renouvelle pas ce contrat.

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-07-15"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for 0 declaration

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract dateSouscription after renewal Use Case n°3 (January 2022)
# Résultats sur les droits TP : 3c 15/07/n+1  Aucune  Les droits TP seront pris en compte par le renouvellement n+1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-07-15"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for 0 declaration

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°4 (January 2022)
# Résultats sur les droits TP : 4a 31/12/n-1  01/01/n 30/12/n Comme les droits TP sont à cheval sur plusieurs années il faut donc pour les assurés de ce contrat renouveler plusieurs années, Mais étant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on ne renouvelle pas l’année n+ 1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-12-31"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-12-30 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | HOSP    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | PHAR    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | OPTI    | 2022-01-01 | 2022-12-30 |
      | GT_BASE  | APDE    | 2022-01-01 | 2022-12-30 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat7" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/01/01 | null       | ONLINE      |
#      | APDE | 2022/01/01 | 2022/12/30 | OFFLINE     |
#      | DENT | 2022/01/01 | null       | ONLINE      |
#      | DENT | 2022/01/01 | 2022/12/30 | OFFLINE     |
#      | HOSP | 2022/01/01 | null       | ONLINE      |
#      | HOSP | 2022/01/01 | 2022/12/30 | OFFLINE     |
#      | OPTI | 2022/01/01 | null       | ONLINE      |
#      | OPTI | 2022/01/01 | 2022/12/30 | OFFLINE     |
#      | PHAR | 2022/01/01 | null       | ONLINE      |
#      | PHAR | 2022/01/01 | 2022/12/30 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract Use Case n°4 (January 2022)
# Résultats sur les droits TP : 4b 31/12/n  Etant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on ne renouvelle pas ce contrat.

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-12-31"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for 0 declarations

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract dateSouscription after renewal Use Case n°4 (January 2022)
# Résultats sur les droits TP : 4c 31/12/n+1  Aucune  Les droits TP seront pris en compte par le renouvellement n+1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-01"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-12-31"

    When I renew the rights on "2022-02-01" with mode "NO_RDO"

    Then I wait for 0 declarations

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°1 (July 2022)
# Résultats sur les droits TP : 1a 01/01/n-1   01/07/n  31/12/n Le changement du paramétrage au 01/07/n sera donc pris en compte

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"
    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-01-01"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"

    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-07-01 | 2022-12-31 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-07-01 | 2022-12-31 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat8" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/01 | null       | ONLINE      |
#      | APDE | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | DENT | 2022/07/01 | null       | ONLINE      |
#      | DENT | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | HOSP | 2022/07/01 | null       | ONLINE      |
#      | HOSP | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | OPTI | 2022/07/01 | null       | ONLINE      |
#      | OPTI | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | PHAR | 2022/07/01 | null       | ONLINE      |
#      | PHAR | 2022/07/01 | 2022/12/31 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract Use Case n°1 (July 2022)
# Résultats sur les droits TP : 1b 01/01/n   01/07/n  31/12/n Le changement du paramétrage au 01/07/n sera donc pris en compte

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-01-01"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-07-01 | 2022-12-31 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2022-12-31 |
      | GT_BASE  | APDE    | 2022-07-01 | 2022-12-31 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat9" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/01 | null       | ONLINE      |
#      | APDE | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | DENT | 2022/07/01 | null       | ONLINE      |
#      | DENT | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | HOSP | 2022/07/01 | null       | ONLINE      |
#      | HOSP | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | OPTI | 2022/07/01 | null       | ONLINE      |
#      | OPTI | 2022/07/01 | 2022/12/31 | OFFLINE     |
#      | PHAR | 2022/07/01 | null       | ONLINE      |
#      | PHAR | 2022/07/01 | 2022/12/31 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract dateSouscription after renewal Use Case n°1 (July 2022)
# Résultats sur les droits TP : 1c 01/01/n+1   Aucune Les droits TP seront pris en compte par le renouvellement n+1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-01-01"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"

    Then I wait for 0 declarations

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°2 (July 2022)
# Résultats sur les droits TP : 2a 15/02/n-1   01/07/n  14/02/n+1 Le changement du paramétrage au 01/07/n sera donc pris en compte

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-02-15"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"

    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-07-01 | 2023-02-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-07-01 | 2023-02-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat10" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/01 | null       | ONLINE      |
#      | APDE | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | DENT | 2022/07/01 | null       | ONLINE      |
#      | DENT | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | HOSP | 2022/07/01 | null       | ONLINE      |
#      | HOSP | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | OPTI | 2022/07/01 | null       | ONLINE      |
#      | OPTI | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | PHAR | 2022/07/01 | null       | ONLINE      |
#      | PHAR | 2022/07/01 | 2023/02/14 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract Use Case n°2 (July 2022)
# Résultats sur les droits TP : 2b 15/02/n   01/07/n  14/02/n+1 Le changement du paramétrage au 01/07/n sera donc pris en compte

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-02-15"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"

    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-07-01 | 2023-02-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2023-02-14 |
      | GT_BASE  | APDE    | 2022-07-01 | 2023-02-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat11" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/01 | null       | ONLINE      |
#      | APDE | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | DENT | 2022/07/01 | null       | ONLINE      |
#      | DENT | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | HOSP | 2022/07/01 | null       | ONLINE      |
#      | HOSP | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | OPTI | 2022/07/01 | null       | ONLINE      |
#      | OPTI | 2022/07/01 | 2023/02/14 | OFFLINE     |
#      | PHAR | 2022/07/01 | null       | ONLINE      |
#      | PHAR | 2022/07/01 | 2023/02/14 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract with dateSouscription after renewal Use Case n°2 (July 2022)
# Résultats sur les droits TP : 2c 15/02/n+1   Aucune Les droits TP seront pris en compte par le renouvellement n+1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-02-15"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"

    Then I wait for 0 declarations

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°3 (July 2022)
# Résultats sur les droits TP : 3a 15/07/n-1  01/07/n 14/07/n+1 Le changement du paramétrage au 01/07/n est pris en compte avec 2 années de renouvellement.
# Etant donné que le délai de renouvellement par rapport à la date d’exécution est pas dépassé on renouvelle l’année n+ 1.

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"
    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-07-15"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"

    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | APDE    | 2022-07-01 | 2023-07-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2023-07-14 |
      | GT_BASE  | APDE    | 2022-07-01 | 2023-07-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat12" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/01 | null       | ONLINE      |
#      | APDE | 2022/07/01 | 2023/07/14 | OFFLINE     |
#      | DENT | 2022/07/01 | null       | ONLINE      |
#      | DENT | 2022/07/01 | 2023/07/14 | OFFLINE     |
#      | HOSP | 2022/07/01 | null       | ONLINE      |
#      | HOSP | 2022/07/01 | 2023/07/14 | OFFLINE     |
#      | OPTI | 2022/07/01 | null       | ONLINE      |
#      | OPTI | 2022/07/01 | 2023/07/14 | OFFLINE     |
#      | PHAR | 2022/07/01 | null       | ONLINE      |
#      | PHAR | 2022/07/01 | 2023/07/14 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract Use Case n°3 (July 2022)
# Résultats sur les droits TP : 3b 15/07/n  15/07/n 14/07/n+1 Le changement du paramétrage au 01/07/n sera donc pris en compte

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-07-15"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"

    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | APDE    | 2022-07-15 | 2023-07-14 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | HOSP    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | PHAR    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | OPTI    | 2022-07-15 | 2023-07-14 |
      | GT_BASE  | APDE    | 2022-07-15 | 2023-07-14 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat13" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/15 | null       | ONLINE      |
#      | APDE | 2022/07/15 | 2023/07/14 | OFFLINE     |
#      | DENT | 2022/07/15 | null       | ONLINE      |
#      | DENT | 2022/07/15 | 2023/07/14 | OFFLINE     |
#      | HOSP | 2022/07/15 | null       | ONLINE      |
#      | HOSP | 2022/07/15 | 2023/07/14 | OFFLINE     |
#      | OPTI | 2022/07/15 | null       | ONLINE      |
#      | OPTI | 2022/07/15 | 2023/07/14 | OFFLINE     |
#      | PHAR | 2022/07/15 | null       | ONLINE      |
#      | PHAR | 2022/07/15 | 2023/07/14 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract with dateSouscription after renewal Use Case n°3 (July 2022)
# Résultats sur les droits TP : 3c 15/07/n+1  Aucune  Les droits TP seront pris en compte par le renouvellement n+1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-07-15"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"

    Then I wait for 0 declarations

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract with dateSouscription before renewal Use Case n°4 (July 2022)
# Résultats sur les droits TP : 4a 31/12/n-1  01/07/n 30/12/n Le changement du paramétrage au 01/07/n est pris en compte avec 2 années de renouvellement.
# Mais étant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on ne renouvelle pas l’année n+ 1.

    Given I create a contract element from a file "gtbasebalooCase1"

    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2021-12-31"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"

    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | APDE    | 2022-07-01 | 2022-12-30 |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        |
      | GT_BASE  | DENT    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | HOSP    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | PHAR    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | OPTI    | 2022-07-01 | 2022-12-30 |
      | GT_BASE  | APDE    | 2022-07-01 | 2022-12-30 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat14" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2022/07/01 | null       | ONLINE      |
#      | APDE | 2022/07/01 | 2022/12/30 | OFFLINE     |
#      | DENT | 2022/07/01 | null       | ONLINE      |
#      | DENT | 2022/07/01 | 2022/12/30 | OFFLINE     |
#      | HOSP | 2022/07/01 | null       | ONLINE      |
#      | HOSP | 2022/07/01 | 2022/12/30 | OFFLINE     |
#      | OPTI | 2022/07/01 | null       | ONLINE      |
#      | OPTI | 2022/07/01 | 2022/12/30 | OFFLINE     |
#      | PHAR | 2022/07/01 | null       | ONLINE      |
#      | PHAR | 2022/07/01 | 2022/12/30 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract Use Case n°4 (July 2022)
# Résultats sur les droits TP : 4b 31/12/n  Mais étant donné que le délai de renouvellement par rapport à la date d’exécution n'est pas dépassé on ne renouvelle pas l’année n+ 1.

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"

    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2022-12-31"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"

    Then I wait for 0 declarations

    Then I wait for 0 contract

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel
  Scenario: I renew a contract with dateSouscription after renewal Use Case n°4 (July 2022)
# Résultats sur les droits TP : 4c 31/12/n+1  Aucune  Les droits TP seront pris en compte par le renouvellement n+1

    Given I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2022-07"
    When I create a service prestation from a file "servicePrestationAnniversaire" with the dateSouscription "2023-12-31"

    When I renew the rights on "2022-06-20" with mode "NO_RDO"

    Then I wait for 0 declarations

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract Use Case n°1 (January 2024)
# Résultats sur les droits TP : 1b 01/01/n  01/01/n 31/12/n On doit renouveler toute l’année n
    Given I create a contract element from a file "gtbasebalooCase1Bis"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2024-01"

    When I create a service prestation from a file "servicePrestationAnniversaireSansRadiation" with the dateSouscription "2023-01-01"

    When I renew the rights on "2024-01-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut      | fin        | finOnline |
      | 5476     | DENT    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | HOSP    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | PHAR    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | AUDI    | 2024-01-01 | 2024-12-31 | null      |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        | finOnline |
      | 5476     | DENT    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | HOSP    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | PHAR    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | AUDI    | 2024-01-01 | 2024-12-31 | null      |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat15" content
#    Then there is 4 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | AUDI | 2024/01/01 | null       | ONLINE      |
#      | AUDI | 2024/01/01 | 2024/12/31 | OFFLINE     |
#      | DENT | 2024/01/01 | null       | ONLINE      |
#      | DENT | 2024/01/01 | 2024/12/31 | OFFLINE     |
#      | HOSP | 2024/01/01 | null       | ONLINE      |
#      | HOSP | 2024/01/01 | 2024/12/31 | OFFLINE     |
#      | PHAR | 2024/01/01 | null       | ONLINE      |
#      | PHAR | 2024/01/01 | 2024/12/31 | OFFLINE     |

  @smokeTests @caseRenouvellement @genDroitsAnnivManuel @caseConsolidation
  Scenario: I renew a contract Use Case n°1 (January 2024 bis)
# Résultats sur les droits TP : 1b 01/01/n  01/01/n 31/12/n On doit renouveler toute l’année n
    Given I create a contract element from a file "gtbasebalooCase1Bis"
    And I create TP card parameters from file "parametrageTPBalooAnniversaireManuel2024-02"

    When I create a service prestation from a file "servicePrestationAnniversaireSansRadiation2" with the dateSouscription "2023-01-01"

    When I renew the rights on "2024-01-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "547601010102" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed      |
      | nir            | 1547601010102  |
      | numeroPersonne | 54760101010201 |

    Then I wait for 1 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut      | fin        | finOnline |
      | 5476     | DENT    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | AUDI    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | PHAR    | 2024-01-01 | 2024-12-31 | null      |
      | 5476     | HOSP    | 2024-01-01 | 2024-12-31 | null      |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "genDroitsAnnivManuel/contrat16" content
#    Then there is 4 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | AUDI | 2024/01/01 | null       | ONLINE      |
#      | AUDI | 2024/01/01 | 2024/12/31 | OFFLINE     |
#      | DENT | 2024/01/01 | null       | ONLINE      |
#      | DENT | 2024/01/01 | 2024/12/31 | OFFLINE     |
#      | HOSP | 2024/01/01 | null       | ONLINE      |
#      | HOSP | 2024/01/01 | 2024/12/31 | OFFLINE     |
#      | PHAR | 2024/01/01 | null       | ONLINE      |
#      | PHAR | 2024/01/01 | 2024/12/31 | OFFLINE     |
