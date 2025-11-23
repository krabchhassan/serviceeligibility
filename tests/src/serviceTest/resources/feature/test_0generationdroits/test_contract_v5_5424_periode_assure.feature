Feature: Test contract V5 with contigious insured period

  @smokeTests @caseRadiationAndSuspension
  Scenario: I send a contract with periode assure
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a beneficiaire from file "beneficiaryForPauV4/beneficiaire-5010"
    When I send a test contract from file "contratV5/5424-periodesAssure"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declaration
    Then the declaration has the etat suspension "1" with this values
      | debut      | fin        | typeSuspension | motifSuspension          | motifLeveeSuspension |
      | 2025/03/01 | null       | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | 2025/02/01 | 2025/03/01 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2025-01-01 | 2025-04-01 | 2025-04-01 |
      | AXASCCGDIM | HOSP    | 2025-01-01 | 2025-04-01 | 2025-04-01 |
    And I wait for 1 contract
    Then the periodeSuspension with indice 0 of the consolidated contract has these values
      | dateDebutSuspension | 2025/02/01 |
      | dateFinSuspension   | 2025/03/01 |
    Then the periodeSuspension with indice 1 of the consolidated contract has these values
      | dateDebutSuspension | 2025/03/01 |
      | dateFinSuspension   | null       |

    And I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-5424" content
#    Then there is 2 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | 2025/01/01 | 2025/04/01 | ONLINE      | null         |
#      | HOSP | 2025/01/01 | 2025/04/01 | OFFLINE     | null         |
#      | OPAU | 2025/01/01 | 2025/04/01 | ONLINE      | null         |
#      | OPAU | 2025/01/01 | 2025/04/01 | OFFLINE     | null         |
