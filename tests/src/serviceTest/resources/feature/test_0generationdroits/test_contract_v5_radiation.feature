Feature: Test contract V5 with  radiation

  # contrat avec un seul benef radié avant 2023 => pas de droits tp
  @smokeTests @caseRadiationR @release
  Scenario: I send a contract with one benef deregistered before 2023 : no TP rights
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/radiation/radiation_avant_2023"
    Then I wait for 0 declaration
    Then I get 1 trigger with contract number "MBA11752" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status         | ProcessedWithWarnings |
      | amc            | 0000401166            |
      | nbBenef        | 1                     |
      | nbBenefKO      | 0                     |
      | nbBenefWarning | 1                     |

  # contrat avec un seul benef radié lors de l'année courante => droits TP jusque date radiation
  @smokeTests @caseRadiationR
  Scenario: I send a contract with one benef deregistered in %%CURRENT_YEAR%% : TP rights up to deregistration date
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/radiation/radiation_en_2025"
    When I get triggers with contract number "MBA11753" and amc "0000401166"
    Then I wait for 1 declarations
    Then I get 1 trigger with contract number "MBA11753" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-10 | %%CURRENT_YEAR%%-06-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-10 | %%CURRENT_YEAR%%-06-10 |
    And I wait for 1 contract

  # contrat ouvert sur toute l'année courante | contrat avec un seul benef radié l'année pro => droits TP jusque date radiation (ONLINE jusque date radiation, OFFLINE jusque fin année courante)
  @smokeTests @caseRadiationR @caseConsolidation
  Scenario: I send a contract open in %%CURRENT_YEAR%% | I send the same contract with one benef deregistered in %%NEXT_YEAR%% : TP rights up to deregistration date
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    # Envoi d'un contrat avec des droits ouverts seulement sur toute l’année courante
    When I send a test contract from file "contratV5/contrat_ouvert2025"
    When I get triggers with contract number "MBA11754" and amc "0000401166"
    Then I wait for 1 declarations
    Then The declaration number 0 has codeEtat "V"
    Then I get 1 trigger with contract number "MBA11754" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
    And I wait for 1 contract
    Then the expected contract TP is identical to "v5_radiation/contrat1" content
#    Then there is 2 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/01/01 | 2025/12/31 | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/01/01 | 2025/12/31 | ONLINE      |
#      | OPAU | 2025/01/01 | 2025/12/31 | OFFLINE     |

    # Envoi du contrat avec une dateRadiation au %%NEXT_YEAR%%-05-28
    When I send a test contract from file "generationDroitsTP_4943/radiation/radiation_en_2026"
    When I get triggers with contract number "MBA11754" and amc "0000401166"
    Then I wait for 3 declarations
    Then The declaration number 1 has codeEtat "R"
    Then The declaration number 2 has codeEtat "V"
    Then I get one more trigger with contract number "MBA11754" and amc "0000401166" and indice "1" for benef
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                    | finOnline           |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-05-28 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-05-28 |
    And I wait for 1 contract
    Then the expected contract TP is identical to "v5_radiation/contrat2" content
#    Then there is 2 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/01/01 | 2026/05/28 | ONLINE      |
#      | OPAU | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/01/01 | 2026/05/28 | ONLINE      |

  # contrat avec 2 bénef dont un benef radié avant 2023 => pas de droits tp pour celui radié et droits TP complet pour l'autre
  @smokeTests @caseRadiationR
  Scenario: I send a contract with two benef including one deregistered before 2023 : TP rights up to deregistration date
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/radiation/radiation_avant_2023_2_benefs"
    When I get triggers with contract number "MBA11755" and amc "0000401166"
    Then I wait for 1 declaration
    Then I get 1 trigger with contract number "MBA11755" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract

  # contrat avec 2 bénef dont un benef radié dans l'année courante => droits TP jusque date résile pour celui radié et droits complet TP pour l'autre
  @smokeTests @caseRadiationR
  Scenario: I send a contract with two benef including one deregistered in current year : TP rights up to deregistration date for one and complete for the other one
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/radiation/radiation_en_2025_2_benefs"
    When I get triggers with contract number "MBA11757" and amc "0000401166"
    Then I wait for 2 declarations
    Then I get 1 trigger with contract number "MBA11757" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-06-15 | %%CURRENT_YEAR%%-06-15 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-06-15 | %%CURRENT_YEAR%%-06-15 |
    And I wait for 1 contract

  # contrat avec 2 bénef dont un benef radié l'année pro => droits TP jusque date résile pour celui radié (ONLINE jusque date radiation OFFLINE jusque fin année courante) et droits TP pour complet l'autre
  @smokeTests @caseRadiationR
  Scenario: I send a contract with two benef including one deregistered in current year : TP rights up to deregistration date for one and complete for the other one
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "generationDroitsTP_4943/radiation/radiation_en_2026_2_benefs"
    When I get triggers with contract number "MBA11756" and amc "0000401166"
    Then I wait for 2 declarations
    Then I get 1 trigger with contract number "MBA11756" and amc "0000401166"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline           |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-15 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-15 |
    And I wait for 1 contract

  @smokeTests @caseRadiation
  Scenario: I send a contract with radiation in %%CURRENT_YEAR%% : contractTP contains radiation date
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contractFor7168/radiation_2025"
    When I wait for 1 declarations
    And The declaration with the indice 0 has this values
      | codeEtat       | V                      |
      | numeroPersonne | 111537                 |
      | nomPorteur     | JANOH                  |
      | dateRadiation  | %%CURRENT_YEAR%%/01/01 |
    And I wait for 1 contract
    Then the consolidated contract has values
      | numeroContrat | 1139211                |
      | idDeclarant   | 0000401166             |
      | dateRadiation | %%CURRENT_YEAR%%/01/01 |
