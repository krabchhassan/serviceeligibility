Feature: Test contract V6 avec prise en compte de la période contexte tiers payant

  Background:
    Given I create a contract element from a file "gtaxa"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    And I create a declarant from a file "declarantbaloo_5458"
    And I create TP card parameters from file "parametrageTPBaloo_5458"

  @smokeTests @PeriodeContextTierPayant @5516
  Scenario: I send contracts with different contexts tiers payant
    When I send a test contract from file "contratV5/5516_one_benef"
    Then I send a test contract from file "contratV5/5516_two_benefs"
    Then I wait for 3 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null       | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null       | true       |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "5516/contrat1" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | codeGarantie | codeProduit | referenceCouverture | naturePrestation | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2025/01/01 | null       | ONLINE      | null         |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2025/01/01 | null       | ONLINE      | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |

  @smokeTests @PeriodeContextTierPayant @caseConsolidation @5516
  Scenario: I send contracts with different contexts tiers payant and a resiliation
    When I send a test contract from file "contratV5/5516_one_benef"
    Then I wait for 1 declaration
    Then I send a test contract from file "contratV5/5516_two_benefs_resiliation"
    Then I wait for 5 declarations
    Then The declaration number 0 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
    Then The declaration number 1 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      |
    Then The declaration number 2 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-10-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-10-01 | true       |
    Then The declaration number 3 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-10-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-10-01 | true       |
    # second benef :
    Then The declaration number 4 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-10-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-10-01 | true       |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "5516/contrat2" content
#    Then there is 2 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/09/01 | 2025/10/01 | ONLINE      | null         |
#      | HOSP | 2025/09/01 | 2025/10/01 | OFFLINE     | null         |
#      | OPAU | 2025/09/01 | 2025/10/01 | ONLINE      | null         |
#      | OPAU | 2025/09/01 | 2025/10/01 | OFFLINE     | null         |
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/01/01 | 2025/10/01 | OFFLINE     | 2025/12/31   |
#      | HOSP | 2025/01/01 | 2025/10/01 | ONLINE      | null         |
#      | OPAU | 2025/01/01 | 2025/10/01 | OFFLINE     | 2025/12/31   |
#      | OPAU | 2025/01/01 | 2025/10/01 | ONLINE      | null         |

  @smokeTests @PeriodeContextTierPayant @caseConsolidation @5516
  Scenario: I send contracts with different contexts tiers payant and a resiliation before context
    When I send a test contract from file "contratV5/5516_one_benef"
    Then I send a test contract from file "contratV5/5516_two_benefs_resiliation_past"
    Then I wait for 3 declarations
    Then The declaration number 0 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
    Then The declaration number 1 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-06-01 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-06-01 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-01 | true       |
    Then The declaration number 2 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-01 | %%CURRENT_YEAR%%-06-01 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-06-01 | %%CURRENT_YEAR%%-06-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-01 | %%CURRENT_YEAR%%-06-01 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-06-01 | %%CURRENT_YEAR%%-06-01 | true       |


    Then I wait for 1 contract
    Then the expected contract TP is identical to "5516/contrat3" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/01/01 | 2025/06/01 | ONLINE      | null         |
#      | HOSP | 2025/01/01 | 2025/06/01 | OFFLINE     | 2025/12/31   |
#      | OPAU | 2025/01/01 | 2025/06/01 | ONLINE      | null         |
#      | OPAU | 2025/01/01 | 2025/06/01 | OFFLINE     | 2025/12/31   |


  @smokeTests @PeriodeContextTierPayant @caseConsolidation @5516
  Scenario: I send contracts with different contexts tiers payant and a radiation on the new benef
    When I send a test contract from file "contratV5/5516_one_benef"
    Then I send a test contract from file "contratV5/5516_two_benefs_radiation_new_benef"
    Then I wait for 4 declarations
    Then The declaration number 0 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
    Then The declaration number 1 has codeEtat "R"
    Then The declaration number 2 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null       | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null       | true       |
    Then The declaration number 3 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-11-01 | %%CURRENT_YEAR%%-11-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-11-01 | %%CURRENT_YEAR%%-11-01 | true       |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "5516/contrat4" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | HOSP | 2025/01/01 | null       | ONLINE      | null         |
#      | OPAU | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | OPAU | 2025/01/01 | null       | ONLINE      | null         |
#
#    Then there is 2 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/09/01 | 2025/11/01 | ONLINE      | null         |
#      | HOSP | 2025/09/01 | 2025/11/01 | OFFLINE     | null         |
#      | OPAU | 2025/09/01 | 2025/11/01 | ONLINE      | null         |
#      | OPAU | 2025/09/01 | 2025/11/01 | OFFLINE     | null         |


  @smokeTests @PeriodeContextTierPayant @caseConsolidation @5516
  Scenario: I send contracts with different contexts tiers payant and a radiation on the old benef
    When I send a test contract from file "contratV5/5516_one_benef"
    Then I send a test contract from file "contratV5/5516_two_benefs_radiation_old_benef"
    Then I wait for 5 declarations
    Then The declaration number 0 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
    Then The declaration number 1 has codeEtat "R"
    Then The declaration number 2 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-11-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-11-01 | true       |
    Then The declaration number 3 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-11-01 | %%CURRENT_YEAR%%-11-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-11-01 | %%CURRENT_YEAR%%-11-01 | true       |
    Then The declaration number 4 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "5516/contrat5" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/01/01 | 2025/11/01 | OFFLINE     | 2025/12/31   |
#      | HOSP | 2025/01/01 | 2025/11/01 | ONLINE      | null         |
#      | OPAU | 2025/01/01 | 2025/11/01 | OFFLINE     | 2025/12/31   |
#      | OPAU | 2025/01/01 | 2025/11/01 | ONLINE      | null         |
#
#    Then there is 2 domaineDroits on benef 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/09/01 | null       | ONLINE      | null         |
#      | HOSP | 2025/09/01 | 2025/12/31 | OFFLINE     | null         |
#      | OPAU | 2025/09/01 | null       | ONLINE      | null         |
#      | OPAU | 2025/09/01 | 2025/12/31 | OFFLINE     | null         |


  @smokeTests @PeriodeContextTierPayant @caseConsolidation
  Scenario: I send contracts with different contexts tiers payant and a radiation before context on both benef
    When I send a test contract from file "contratV5/5516_one_benef"
    Then I send a test contract from file "contratV5/5516_two_benefs_radiation_benef_past"
    Then I wait for 4 declarations
    Then The declaration number 0 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | true       |
    Then The declaration number 1 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      |
    Then The declaration number 2 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-07-01 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-07-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-07-01 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-07-01 | true       |
    Then The declaration number 3 has codeEtat "R"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  | isEditable |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 | false      |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 | true       |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 | false      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-01 | true       |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "5516/contrat6" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/01/01 | 2025/07/01 | OFFLINE     | 2025/12/31   |
#      | HOSP | 2025/01/01 | 2025/07/01 | ONLINE      | null         |
#      | OPAU | 2025/01/01 | 2025/07/01 | OFFLINE     | 2025/12/31   |
#      | OPAU | 2025/01/01 | 2025/07/01 | ONLINE      | null         |


  @todosmokeTests @PeriodeContextTierPayant @pauv5
  Scenario: I send contracts with different contexts tiers payant. Then a radiation before context of benef 2
    When I send a test contract from file "contratV5/5516_one_benef"
    Then I send a test contract from file "contratV5/5516_two_benefs"
    Then I send a test contract from file "contratV5/5516_two_benefs_resiliation_past"
    Then I update beneficiary with the declaration
    Then I update beneficiary with the declaration with indice 1
    Then I update beneficiary with the declaration with indice 2
    Then I update beneficiary with the declaration with indice 3
    Then I update beneficiary with the declaration with indice 4
    Then I update beneficiary with the declaration with indice 5
    Then I update beneficiary with the declaration with indice 6
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-04-15' '2025-04-15' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-09-15' '2025-09-15' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-04-15' '2025-04-15' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-09-15' '2025-09-15' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |

    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '2025-06-15' '2025-06-15' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '2025-09-15' '2025-09-15' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '2025-04-15' '2025-04-15' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '2025-09-15' '2025-09-15' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |


  @todosmokeTests @PeriodeContextTierPayant
  Scenario: I send contracts with a 1 day open period then I resiliated it
    When I send a test contract from file "contratV5/6183_one_benef_one_day"
    Then I send a test contract from file "contratV5/6183_one_benef_one_day_resil"
    Then I update beneficiary with the declaration
    Then I update beneficiary with the declaration with indice 1
    Then I update beneficiary with the declaration with indice 2
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-01-01' '2025-12-31' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-01-01' '2025-01-01' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    When I get contrat PAUV5 for 1860598145145 '18690428' 1 '2025-01-02' '2025-01-02' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception
