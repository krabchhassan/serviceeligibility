Feature: 6526

  Background:
    Given I create a contract element from a file "gtaxa_ok"
    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @changementFinDroits
  Scenario: Cas 6526 changement date de fin de droit sur les droits du contrat
    When I send a test contract v6 from file "contratV6/createServicePrestation6526"
    Then I wait for 1 declaration
    Then I wait for 1 contract
    Then the expected contract TP is identical to "6526/contrattp-1" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/06/20 | null       | ONLINE      |
#      | HOSP | 2025/06/20 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/06/20 | null       | ONLINE      |
#      | OPAU | 2025/06/20 | 2025/12/31 | OFFLINE     |

    When I send a test contract v6 from file "contratV6/createServicePrestation6526_finDroit"

    Then I wait for 3 declaration
    Then I wait for 1 contract
    Then the expected contract TP is identical to "6526/contrattp-2" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/06/20 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/06/20 | 2025/11/20 | ONLINE      |
#      | OPAU | 2025/06/20 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/06/20 | 2025/11/20 | ONLINE      |
#

    When I send a test contract v6 from file "contratV6/createServicePrestation6526"
    Then I wait for 5 declaration
    Then I wait for 1 contract
    Then the expected contract TP is identical to "6526/contrattp-3" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/06/20 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/06/20 | null       | ONLINE      |
#      | OPAU | 2025/06/20 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/06/20 | null       | ONLINE      |

  @smokeTests @changementFinDroits @caseConsolidation @release
  Scenario: Cas 6526 changement date de fin de droit sur les droits du contrat et gt
    When I send a test contract v6 from file "contratV6/createServicePrestation6526"
    Then I wait for 1 declaration
    Then I wait for 1 contract
    Then the expected contract TP is identical to "6526/contrattp-4" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/06/20 | null       | ONLINE      |
#      | HOSP | 2025/06/20 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/06/20 | null       | ONLINE      |
#      | OPAU | 2025/06/20 | 2025/12/31 | OFFLINE     |

    When I send a test contract v6 from file "contratV6/createServicePrestation6526_changementgtEtFinDroit"

    Then I wait for 3 declaration
    Then I wait for 1 contract

    Then the expected contract TP is identical to "6526/contrattp-5" content

    When I send a test contract v6 from file "contratV6/createServicePrestation6526"
    Then I wait for 5 declaration
    Then I wait for 1 contract
    Then the expected contract TP is identical to "6526/contrattp-6" content
#    Then there is 6 domaineDroits on benef 0 and warranty 0 and product 0 and refCouverture 0 and the different domaineDroits has this values
#      | code | debut      | fin        | finFermeture | typePeriode |
#      | APDE | 2025/06/20 | 2025/06/19 | 2025/11/20   | OFFLINE     |
#      | DENT | 2025/06/20 | 2025/06/19 | 2025/11/20   | OFFLINE     |
#      | HOSP | 2025/06/20 | 2025/12/31 | null         | OFFLINE     |
#      | HOSP | 2025/06/20 | null       | null         | ONLINE      |
#      | OPAU | 2025/06/20 | 2025/12/31 | null         | OFFLINE     |
#      | OPAU | 2025/06/20 | null       | null         | ONLINE      |
#      | OPTI | 2025/06/20 | 2025/06/19 | 2025/11/20   | OFFLINE     |
#      | PHAR | 2025/06/20 | 2025/06/19 | 2025/11/20   | OFFLINE     |
#
#    Then there is 6 domaineDroits on benef 0 and warranty 0 and product 1 and refCouverture 0 and the different domaineDroits has this values
#      | code | debut      | fin        | finFermeture | typePeriode |
#      | HOSP | 2025/06/20 | 2025/06/19 | 2024/11/20   | OFFLINE     |
