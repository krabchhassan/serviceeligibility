Feature: Test contract V5 with productWorkshop Changement version

  @smokeTests @caseChangementProductWorkshop @caseConsolidation @release
  Scenario: I send a contract with modification productWorkshop in 2025 and 2026
    And I create a contract element from a file "gtbaloo_multiversion"
    And I create a declarant from a file "declarantbalooEditable"
    And I create TP card parameters from file "parametrageTPBalooEditable"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4882"

    When I get triggers with contract number "99992004002" and amc "0000401166"
    Then I wait for 1 declarations
    Then there is 6 rightsDomains and the different rightsDomains has this values
      | garantie  | domaine | debut                  | fin                    | finOnline              |
      | GT_BL_002 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BL_002 | PHCO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BL_002 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BL_002 | PHCN    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BL_002 | PHNO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BL_002 | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |

    When I renew the rights on "%%NEXT_YEAR%%-01-01" with mode "NO_RDO"
    When I wait "1" seconds in order to consume the data

    Then I wait for the last renewal trigger with contract number "99992004002" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1791062498046 |
      | numeroPersonne | D999200400201 |

    Then I wait for 3 declarations

    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie  | domaine | debut               | fin                 | finOnline |
      | GT_BL_002 | RADL    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_BL_002 | OPTI    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_BL_002 | DENT    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_BL_002 | PHAR    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 2 card

    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-99992004002 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |

    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-99992004002 |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%NEXT_YEAR%%/01/01    |
      | periodeFin       | %%NEXT_YEAR%%/12/31    |

    #test delete contract HTP et impact sur génération déclarations et contrat TP
    When I delete a contract "99992004002" for amc "0000401166"
    When I wait "3" seconds in order to consume the data
    Then I wait for 5 declarations
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "4882/contrat1" content

    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 2 card

    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-99992004002 |
      | isLastCarteDemat | false                  |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |

    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-99992004002 |
      | isLastCarteDemat | false                  |
      | periodeDebut     | %%NEXT_YEAR%%/01/01    |
      | periodeFin       | %%NEXT_YEAR%%/12/31    |


  @smokeTests @caseChangementProductWorkshop
  Scenario: I send a contract with modification productWorkshop in january 2025 and june 2025
    And I create a contract element from a file "gtbaloo_multiversion2"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4882-1"
    When I get triggers with contract number "99992004002" and amc "0000401166"
    Then I wait for 1 declarations
    Then The declaration has this adresse
      | ligne1             | ligne4              | ligne6        | codePostal |
      | Monsieur POLO JOEL | 12 CHEMIN DU MOULIN | 14125 DONAZAC | 14125      |
    Then there is 7 rightsDomains and the different rightsDomains has this values
      | garantie  | domaine | debut                  | fin                    | finOnline              |
      | GT_BL_001 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |
      | GT_BL_001 | PHCO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |
      | GT_BL_001 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |
      | GT_BL_001 | PHCN    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |
      | GT_BL_001 | PHNO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |
      | GT_BL_001 | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-05-31 | %%CURRENT_YEAR%%-05-31 |
      | GT_BL_001 | DENT    | %%CURRENT_YEAR%%-06-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |

  # TODO voir pour faire un changement de pw en cours d'année, dans ce cas il faudrait un renouvellement à date et non un renouvellement à échéance
#    When I renew the rights on "2023-06-01" with mode "NO_RDO"
#
#    Then I wait for the last renewal trigger with contract number "99992004002" and amc "0000401166" to be "Processed"
#    When I get the triggerBenef on the trigger
#    Then the triggerBenef has this values
#      | statut          | Processed      |
#      | nir             | 1791062498046  |
#      | numeroPersonne  | 9999200400201  |
#
#    When I get all declarations
#    Then 3 declarations are returned
#    Then there is 6 rightsDomains and the different rightsDomains has this values with indice 1
#      | garantie   | domaine | debut      | fin        | finOnline  |
#      | GT_BL_002  | DENT    | 2023-01-01 | 2022-12-31 | 2022-12-31 |
#      | GT_BL_002  | PHCO    | 2023-01-01 | 2022-12-31 | 2022-12-31 |
#      | GT_BL_002  | OPTI    | 2023-01-01 | 2022-12-31 | 2022-12-31 |
#      | GT_BL_002  | PHCN    | 2023-01-01 | 2022-12-31 | 2022-12-31 |
#      | GT_BL_002  | PHNO    | 2023-01-01 | 2022-12-31 | 2022-12-31 |
#      | GT_BL_002  | PHOR    | 2023-01-01 | 2022-12-31 | 2022-12-31 |
#
#    Then there is 1 rightsDomains and the different rightsDomains has this values with indice 2
#      | garantie   | domaine | debut      | fin        | finOnline  |
#      | GT_BL_002  | DENT    | 2023-01-01 | 2023-05-31 | 2023-05-31 |
#      | GT_BL_002  | PHCO    | 2023-01-01 | 2023-05-31 | 2023-05-31 |
#      | GT_BL_002  | OPTI    | 2023-01-01 | 2023-05-31 | 2023-05-31 |
#      | GT_BL_002  | PHCN    | 2023-01-01 | 2023-05-31 | 2023-05-31 |
#      | GT_BL_002  | PHNO    | 2023-01-01 | 2023-05-31 | 2023-05-31 |
#      | GT_BL_002  | PHOR    | 2023-01-01 | 2023-05-31 | 2023-05-31 |
#      | GT_BL_002  | DENT    | 2023-06-01 | 2023-12-31 | null       |
