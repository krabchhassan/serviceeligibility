Feature: 6491 BOBB fermé dans le futur

  Background:
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a declarant from a file "declarantbaloo"

  @smokeTests @6491 @release
  Scenario: BOBB fermé dans le futur + contrat résilié
    Given I create a contract element from a file "gtbasebaloo6491_2026"
    When I send a test contract from file "contractFor6491/contrat6491"
    # produit trouvé sur l'année courante : le contrat a des droits infinis et BOBB est fermé à partir du n+1-03-01
    Then I wait for 1 declaration

    Then I wait for the first trigger with contract number "12345679" and amc "0000401166" to be "Processed"

    # Envoi d'un contrat résilié le n+1-02-02
    When I send a test contract from file "contractFor6491/contrat6491-resilie"
    Then I wait for 3 declaration
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
    Then I wait for 1 contract

  @smokeTests @6491
  Scenario: Contrat avec 1 droit + avec des dates de fin différentes
    Given I create a contract element from a file "gtbasebaloo6491"
    # bobb fini au 1 mars de l'année courante n

    # Envoi d'un contrat avec une dateRadiation au 02/03 de l'année courante
    When I send a test contract from file "contractFor6491/contrat6491-radie"
    # Aucun produit trouvé car le contrat a une radiation benef au 02/03 de l'année courante mais BOBB est fermé à partir du 01/03 de l'année courante
    Then I wait for 0 declaration

    Then I wait for the first trigger with contract number "12345679" and amc "0000401166" to be "ProcessedWithErrors"
    Given I abandon the trigger

    # Envoi d'un contrat avec une dateResiliation au n-03-02
    When I send a test contract from file "contractFor6491/contrat6491-resilie2"
    # Aucun produit trouvé : car le contrat est résilié au n-03-02 mais BOBB est fermé à partir du n-03-01
    Then I wait for 0 declaration

    Then I wait for the first trigger with contract number "12345679" and amc "0000401166" to be "ProcessedWithErrors"
    Given I abandon the trigger

    # Envoi d'un contrat avec une dateResiliation au n-02-02
    When I send a test contract from file "contractFor6491/contrat6491-1GTmalResilie"
    Then I wait for 0 declaration

  @smokeTests @6491
  Scenario: Contrat avec 1 droit + avec dateResiliation au 02/12 de l'année en cours n, dateRadiation au 08/11/n, periodeFinAssure au 03/11/n
    Given I create a contract element from a file "gtbasebaloo6491_2026"
    When I send a test contract from file "contractFor6491/contrat6491-plDatesFin"
    Then I wait for 1 declaration
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
    Then I wait for 1 contract

  @smokeTests @6491
  Scenario: Contrat avec 1 droit + avec dateResiliation au 02/12/n, dateRadiation au 31/10/n, periodeFinAssure au 03/11/n
    Given I create a contract element from a file "gtbasebaloo6491_2026"
    When I send a test contract from file "contractFor6491/contrat6491-plDatesFin2"
    Then I wait for 1 declaration
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-11-08 |
    Then I wait for 1 contract

  @smokeTests @6491
  Scenario: Reception contrat avec debut droit au 01/01/n => droits generes ok | modification contrat avec des droits fermés | regenere les droits =>  fermeture
    Given I create a contract element from a file "gtbasebaloo6491_2026"
    When I send a test contract from file "contractFor6491/contrat6491-ouvert2025"
    Then I wait for 1 declaration
    Then The declaration number 0 has codeEtat "V"
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline              |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 |

    # Envoi d'un contrat avec une dateResiliation au n-09-20, dateRadiation au n-12-12, periodeFinAssure au n-11-10, finDroit au n-10-04
    When I send a test contract from file "contractFor6491/contrat6491-plDatesFin3"
    Then I wait for 4 declaration
    Then The declaration number 1 has codeEtat "R"
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                 | finOnline           |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
    Then The declaration number 2 has codeEtat "V"
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline              |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-09-20 |
    Then The declaration number 3 has codeEtat "R"
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                    | finOnline              |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-20 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-20 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-20 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-20 | %%CURRENT_YEAR%%-09-20 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-20 | %%CURRENT_YEAR%%-09-20 |
    And I wait for 1 contract
    #//TODO consolidate

  @smokeTests @6491
  Scenario: Contrat avec 2 droits + avec des dates de fin différentes
    Given I create a contract element from a file "gtbasebaloo6491_2026"
    Given I create a contract element from a file "gtbasebalooCase2"
    # Envoi d'un contrat avec une dateResiliation au n-02-02
    When I send a test contract from file "contractFor6491/contrat6491-2GTmalResilie"
    Then I wait for 1 declaration
    Then there is 10 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-30 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-30 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-30 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-30 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-09-30 |
    Then I wait for 1 contract
