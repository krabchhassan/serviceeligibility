Feature: Test contract V5 with radiation or resiliation

  @smokeTests @caseRadiation
  Scenario: I send a contract with radiation cas 1 : no trigger on renewal
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4896-01"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 0 declarations
    When I renew the rights on "2023-01-01" with mode "NO_RDO"
    Then I get 1 trigger with contract number "01599324" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithWarnings |
      | amc            | 0000401166            |
      | nbBenef        | 2                     |
      | nbBenefKO      | 0                     |
      | nbBenefWarning | 2                     |

  @smokeTests @caseRadiation
  Scenario: I send a contract with radiation cas 1 bis
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4896-01bis"
    When I get triggers with contract number "MBA01bis" and amc "0000401166"
    Then I wait for 1 declarations
    Then the different rightsDomains has this values
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement | DE                     |
    And I empty the declaration database
    When I renew the rights on "2025-01-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "MBA01bis" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111178 |
      | numeroPersonne | MBA01bis1     |
    Then I wait for 1 declarations
    Then the different rightsDomains has this values
      | periodeDebut   | 2025-01-01 |
      | periodeFin     | 2025-12-31 |
      | motifEvenement | RE         |


  @smokeTests @caseRadiation @caseConsolidation
  Scenario: I send a contract with radiation cas 2
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4896-02"
    When I get triggers with contract number "MBA02" and amc "0000401166"
    Then I wait for 2 declarations
    Then the different rightsDomains has this values
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement | DE                     |
    Then the different rightsDomains has this values with indice 1
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-03-10 |
      | motifEvenement | DE                     |

    When I renew the rights on "2025-01-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "MBA02" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    # l'ordre des triggerBenefs a changé vu que l'on insère le "souscripteur" en premier.
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111166 |
      | numeroPersonne | MBA042        |
    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111177 |
      | numeroPersonne | MBA041        |
    Then I wait for 4 declarations
    # l'assuré qui n'est pas radié :
    Then the different rightsDomains has this values with indice 2
      | periodeDebut   | 2025-01-01 |
      | periodeFin     | 2025-03-10 |
      | motifEvenement | RE         |

    Then the different rightsDomains has this values with indice 3
      | periodeDebut   | 2025-01-01 |
      | periodeFin     | 2025-12-31 |
      | motifEvenement | RE         |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-4896" content
#    Then there is 5 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | APDE | 2025/01/01 | 2025/03/10 | ONLINE      | null         |
#      | APDE | 2025/01/01 | 2025/03/10 | OFFLINE     | null         |
#      | DENT | 2025/01/01 | 2025/03/10 | ONLINE      | null         |
#      | DENT | 2025/01/01 | 2025/03/10 | OFFLINE     | null         |
#      | HOSP | 2025/01/01 | 2025/03/10 | ONLINE      | null         |
#      | HOSP | 2025/01/01 | 2025/03/10 | OFFLINE     | null         |
#      | OPTI | 2025/01/01 | 2025/03/10 | ONLINE      | null         |
#      | OPTI | 2025/01/01 | 2025/03/10 | OFFLINE     | null         |
#      | PHAR | 2025/01/01 | 2025/03/10 | ONLINE      | null         |
#      | PHAR | 2025/01/01 | 2025/03/10 | OFFLINE     | null         |
#
#    Then there is 5 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | APDE | 2025/01/01 | null       | ONLINE      | null         |
#      | APDE | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | DENT | 2025/01/01 | null       | ONLINE      | null         |
#      | DENT | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | HOSP | 2025/01/01 | null       | ONLINE      | null         |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | OPTI | 2025/01/01 | null       | ONLINE      | null         |
#      | OPTI | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | PHAR | 2025/01/01 | null       | ONLINE      | null         |
#      | PHAR | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |


  @smokeTests @caseRadiation
  Scenario: I send a contract with radiation cas 2 on next year
    Given I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4896-02-nextYear"
    When I get triggers with contract number "MBA02" and amc "0000401166"
    Then I wait for 2 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline  |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 2026-03-10 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 2026-03-10 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 2026-03-10 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 2026-03-10 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | 2026-03-10 |


  @smokeTests @caseRadiation
  Scenario: I send a contract with radiation not on first call
    Given I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4896-00"
    When I get triggers with contract number "MBA02" and amc "0000401166"
    Then I wait for 2 declarations
    Then the different rightsDomains has this values
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement | DE                     |
    Then the different rightsDomains has this values with indice 1
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement | DE                     |

    When I send a test contract from file "contratV5/4896-02-bis"
    Then I wait for 6 declarations
    Then the different rightsDomains has this values with indice 2
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement | DE                     |
    Then the different rightsDomains has this values with indice 3
      | periodeDebut          | %%CURRENT_YEAR%%-01-01 |
      | periodeFin            | %%LAST_YEAR%%-12-31    |
      | periodeDebutFermeture | %%CURRENT_YEAR%%-01-01 |
      | periodeFinFermeture   | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement        | DE                     |

    Then the different rightsDomains has this values with indice 4
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement | DE                     |
    Then the different rightsDomains has this values with indice 5
      | periodeDebut          | %%CURRENT_YEAR%%-01-01 |
      | periodeFin            | %%CURRENT_YEAR%%-03-10 |
      | periodeDebutFermeture | %%CURRENT_YEAR%%-03-11 |
      | periodeFinFermeture   | %%CURRENT_YEAR%%-12-31 |
      | motifEvenement        | FE                     |


  @smokeTests @caseRadiation
  Scenario: cas 3 Contrat HTP  avec date de resiliation inférieure à la date de renouvellement
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a contract from file "contratV5/4896-03" to version "V5"
    Then I wait for 0 declarations

    When I renew the rights on "2023-01-01" with mode "NO_RDO"
    Then I wait for 0 declarations

  @smokeTests @caseRadiation
  Scenario: I send a contract with radiation cas 3 bis (les 2 benefs sont résiliés)
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4896-03bis"
    When I get triggers with contract number "MBA03bis" and amc "0000401166"
    Then I wait for 2 declarations
    Then the different rightsDomains has this values
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-03-10 |
      | motifEvenement | DE         |

    Then the different rightsDomains has this values with indice 1
      | periodeDebut   | %%CURRENT_YEAR%%-01-01 |
      | periodeFin     | %%CURRENT_YEAR%%-03-10 |
      | motifEvenement | DE         |

    When I renew the rights on "2025-01-01" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "MBA03bis" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111169 |
      | numeroPersonne | MBA03bis2     |

    When I get the triggerBenef on the trigger with the index "1"
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 2730781111180 |
      | numeroPersonne | MBA03bis1     |

    Then I wait for 4 declarations
    Then the different rightsDomains has this values with indice 2
      | periodeDebut   | 2025-01-01 |
      | periodeFin     | 2025-03-10 |
      | motifEvenement | RE         |

    Then the different rightsDomains has this values with indice 3
      | periodeDebut   | 2025-01-01 |
      | periodeFin     | 2025-03-10 |
      | motifEvenement | RE         |
