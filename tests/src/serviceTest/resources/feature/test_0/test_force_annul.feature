Feature: Test de la propriete annulDroitsOffline dans le parametrage carte TP

  Background:
    And I create a contract element from a file "gtaxa_cgdiv_6485_v1"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    And I create a service prestation from a file "servicePrestationForceAnnul"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I renew the rights today with mode "RDO"
    Then I wait for 1 declaration
    And I remove all TP card parameters from database
    And I create a contract element from a file "gtaxa_cgdiv_6485_v2"

  @smokeTests @forceAnnul
  Scenario: I renew a contract 2024 with forceAnnul false
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    And I renew the rights today with mode "RDO"
    And I wait for 3 declarations
    Then The declaration number 0 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIV | OPAU    | 2024-01-01 | 2024-12-31 | null      |
      | AXASCCGDIV | HOSP    | 2024-01-01 | 2024-12-31 | null      |
    Then The declaration number 1 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | periodeFermetureFin | finOnline  |
      | AXASCCGDIV | OPAU    | 2024-01-01 | 2023-12-31 | 2024-12-31          | 2023-12-31 |
      | AXASCCGDIV | HOSP    | 2024-01-01 | 2023-12-31 | 2024-12-31          | 2023-12-31 |
    Then The declaration number 2 has codeEtat "V"
    Then there is 1 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIV | OPAU    | 2024-01-01 | 2024-12-31 | null      |
    Then I wait for 1 contract
    And the expected contract TP is identical to "forceAnnul/contrat1" content


  @smokeTests @forceAnnul @release
  Scenario: I renew a contract 2024 with forceAnnul true
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024_forceAnnul"
    And I renew the rights today with mode "RDO"
    And I wait for 3 declarations
    Then The declaration number 0 has codeEtat "V"
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIV | OPAU    | 2024-01-01 | 2024-12-31 | null      |
      | AXASCCGDIV | HOSP    | 2024-01-01 | 2024-12-31 | null      |
    Then The declaration number 1 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | periodeFermetureFin | finOnline  |
      | AXASCCGDIV | OPAU    | 2024-01-01 | 2023-12-31 | 2023-12-31          | 2023-12-31 |
      | AXASCCGDIV | HOSP    | 2024-01-01 | 2023-12-31 | 2023-12-31          | 2023-12-31 |
    Then The declaration number 2 has codeEtat "V"
    Then there is 1 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIV | OPAU    | 2024-01-01 | 2024-12-31 | null      |
    Then I wait for 1 contract
    And the expected contract TP is identical to "forceAnnul/contrat2" content
