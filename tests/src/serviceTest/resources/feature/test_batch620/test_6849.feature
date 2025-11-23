Feature: BLUE-6849 : Test 2 cartes créées suite à un changement de contexteTP sur l'année alors qu'une seule aurait dû être générée

  Background:
    Given I create a declarant from a file "declarantbalooDematPapier"
    Given I create TP card parameters from file "parametrageTPBaloo_pilotageBOCarte"
    Given I create a contract element from a file "gt_6849"
    And I create a service prestation from a file "servicePrestation-6849"
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL/IS |
      | libelle | AL/IS |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL |
      | libelle | AL |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS |
      | libelle | IS |

  @smokeTests @batch620
  Scenario: Test 2 cartes créées suite à un changement de contexteTP sur l'année alors qu'une seule aurait dû être générée
    And I create a declaration from a file "declaration_renewal-1_6849"
    And I create a declaration from a file "declaration_renewal-2_6849"
    # ServicePrestation avec contexteTiersPayant du %%CURRENT_YEAR%%-01-01 au %%CURRENT_YEAR%%-07-02
    And I send a contract from file "contratV6/6849-1" to version "V6"
    And I wait for 3 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline              | isEditable |
      | GT_6476  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-02 | true       |
      | GT_6476  | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-02 | true       |
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    And I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-12345678    |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/07/02 |
#     ServicePrestation avec contexteTiersPayant du %%CURRENT_YEAR%%-07-03 au %%NEXT_YEAR%%-07-02 (suite à un changement d'offre PW)
    And I send a contract from file "contratV6/6849-2" to version "V6"
    And I wait for 7 declarations
    # R
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut               | fin                 | finOnline           | isEditable | periodeFermetureDebut  | periodeFermetureFin |
      | GT_6476  | HOSP    | %%LAST_YEAR%%-07-03 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GT_6476  | PHOR    | %%LAST_YEAR%%-07-03 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
    # R
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut               | fin                 | finOnline           | isEditable | periodeFermetureDebut  | periodeFermetureFin |
      | GT_6476  | HOSP    | %%LAST_YEAR%%-07-03 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GT_6476  | PHOR    | %%LAST_YEAR%%-07-03 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | false      | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
    # R
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut                  | fin                    | finOnline              | isEditable | periodeFermetureDebut  | periodeFermetureFin    |
      | GT_6476  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-02 | false      | %%CURRENT_YEAR%%-07-03 | %%CURRENT_YEAR%%-07-02 |
      | GT_6476  | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-02 | false      | %%CURRENT_YEAR%%-07-03 | %%CURRENT_YEAR%%-07-02 |
    # V
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 6
      | garantie | domaine | debut                  | fin                 | finOnline | isEditable |
      | GT_6476  | HOSP    | %%CURRENT_YEAR%%-07-03 | %%NEXT_YEAR%%-07-02 | null      | true       |
      | GT_6476  | PHOR    | %%CURRENT_YEAR%%-07-03 | %%NEXT_YEAR%%-07-02 | null      | true       |
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-02"
    And I wait for 2 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-12345678    |
      | isLastCarteDemat | false                  |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/07/02 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-12345678    |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/07/03 |
      | periodeFin       | %%NEXT_YEAR%%/07/02    |
    And I wait for 2 cartes papier
    And the carte papier at index 0 has these values
      | numeroAMC      | 0000401166             |
      | periodeDebut   | %%CURRENT_YEAR%%/01/01 |
      | periodeFin     | %%CURRENT_YEAR%%/07/02 |
      | dateTraitement | %%CURRENT_YEAR%%-01-01 |
    And the carte papier at index 1 has these values
      | numeroAMC      | 0000401166             |
      | periodeDebut   | %%CURRENT_YEAR%%/07/03 |
      | periodeFin     | %%NEXT_YEAR%%/07/02    |
      | dateTraitement | %%CURRENT_YEAR%%-01-02 |
