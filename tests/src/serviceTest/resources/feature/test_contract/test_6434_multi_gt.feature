Feature: 6434

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @6434 @release
  Scenario: Droit infi, 2 gt non contigues sur la periode -> Aucun produit. 2gt contigue sur la periode -> generation des droits
    And I create a contract element from a file "gtbasebaloo6434NonContigues"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    # Aucun produit
    Then I wait for 0 declaration
    And I create a contract element from a file "gtbasebaloo6434Contigues"
    Then I wait for the first trigger with contract number "MBA0003" and amc "0000401166" to be "ProcessedWithErrors"
    Given I abandon the trigger
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    Then I wait for 1 declaration
    Then I wait for 1 contract


  @smokeTests @6434
  Scenario: bobb avec GT avec 1 correspondance valide au 01/01/2020 | reception contrat avec debut droit au 01/01/2025 => droits generes ok | modification bobb avec 2 periodes contigues | regenere les droits =>  fermeture + ouverture sur les 2 periodes …attention aux periodes offlines
    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    Then I wait for 1 declaration
    And I create a contract element from a file "gtbasebaloo6434Contigues"
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    Then I wait for 3 declaration
    Then The declaration number 0 has codeEtat "V"
    Then The declaration number 1 has codeEtat "R"
    Then The declaration number 2 has codeEtat "V"
    # Les droits TP OFFLINE de la déclaration de fermeture ne doivent pas être fermés car il y a une carte TP en cours de circulation
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin    |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
    Then I wait for 1 contract

  @smokeTests @6434
  Scenario: bobb avec GT avec 1 correspondance valide au 01/01/2020 | reception contrat avec debut droit au 01/01/2025 => droits generes ok | modification bobb avec 2 periodes non-contigues | regenere les droits =>  fermeture + ouverture sur les 2 periodes …attention aux periodes offlines
    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6434/createServicePrestationTest4Test5"
    Then I wait for 1 declaration
    And I create a contract element from a file "gtbasebaloo6434NonContigues"
    When I send a test contract from file "contractFor6434/createServicePrestationTest4Test5"
    Then I wait for 3 declaration
    Then The declaration number 0 has codeEtat "V"
    Then The declaration number 1 has codeEtat "R"
    Then The declaration number 2 has codeEtat "V"
    # Les droits TP OFFLINE de la déclaration de fermeture ne doivent pas être fermés car il y a une carte TP en cours de circulation
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                    | finOnline              | periodeFermetureDebut  | periodeFermetureFin    |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-08-31 | %%CURRENT_YEAR%%-09-01 | %%CURRENT_YEAR%%-12-31 |
    Then I wait for 1 contract

  @smokeTests @6434
  Scenario: bobb avec GT avec 1 correspondance valide au 01/01/2020 | reception contrat avec debut droit au 01/01/2025 => droits generes ok | modification bobb avec 2 periodes non-contigues | regenere les droits => Aucun produit trouvé
    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    Then I wait for 1 declaration
    And I create a contract element from a file "gtbasebaloo6434NonContigues"
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    # Aucun produit trouvé => pas de nouvelle declaration créée
    Then I wait for 1 declaration
    Then I wait for 1 contract

  @smokeTests @6434
  Scenario: bobb avec GT avec 1 correspondance valide au 01/01/2020 | reception contrat avec debut droit au 01/01/2025 => droits generes ok | modification bobb avec 2 periodes non-contigues | regenere les droits => Aucun produit trouvé
    And I create a contract element from a file "gtbasebalooCase1"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    Then I wait for 1 declaration
    And I create a contract element from a file "gtbasebaloo6434NonContigues"
    When I send a test contract from file "contractFor6434/createServicePrestationBase"
    # Aucun produit trouvé => pas de nouvelle declaration créée
    Then I wait for 1 declaration
    Then I wait for 1 contract

  @smokeTests @6434 @6501
  Scenario: bobb avec GT avec 1 correspondance valide au 01/01/2035 | reception contrat avec debut droit au 01/01/2001 => droits generes ko | modification bobb qui debute en 1900 | regenere les droits =>  droits generes ok
    And I create a contract element from a file "gtbasebaloo6501_start_in_2035"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6501/createServicePrestationBase"
    Then I wait for 0 declaration
    Then I wait for the first trigger with contract number "MBA0003" and amc "0000401166" to be "ProcessedWithErrors"
    Given I abandon the trigger
    And I create a contract element from a file "gtbasebaloo6501_start_in_1900"
    When I send a test contract from file "contractFor6501/createServicePrestationBase"
    Then I wait for 1 declaration
    Then I wait for 1 contract

  @smokeTests @6434 @6505
  Scenario: bobb avec GT avec 1 correspondance valide au 01/01/2023 | reception contrat avec debut droit au 01/10/2025 => droits generes ok
    And I create a contract element from a file "gtbasebaloo6505"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6505/createServicePrestationBase"
    Then I wait for 1 declaration
    Then I wait for 1 contract

  @smokeTests @6434
  Scenario: Radiation / Resil / Fermeture garantie
    And I create a contract element from a file "gtbasebaloo6434NonContigues"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractFor6434/createServicePrestationTest4Test5"
    Then I wait for 1 declaration
    And I create a contract element from a file "gtbasebaloo6434NonContigues"
    When I send a test contract from file "contractFor6434/createServicePrestationTest5Fermeture"
    Then I wait for 3 declaration
    Then The declaration number 0 has codeEtat "V"
    Then The declaration number 1 has codeEtat "R"
    Then The declaration number 2 has codeEtat "V"

  @smokeTests @6476 @caseChangementCorrespondanceBobb
  Scenario: Changement de correspondance BOBB
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL |
      | libelle | AL |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL/IS |
      | libelle | AL/IS |
    Given I create a contract element from a file "gt_6476"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I send a test contract from file "contractFor6434/servicePrestationGT_6476"
    Then I wait for 1 declaration
    Given I create a contract element from a file "gt_6476_changementProduit"
    Given I send a test contract from file "contractFor6434/servicePrestationGT_6476"

    Then I wait for 3 declaration
    Then The declaration number 0 has codeEtat "V"
    Then The declaration number 1 has codeEtat "R"
    Then The declaration number 2 has codeEtat "V"
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | codeProduit       | debut                  | fin                    | finOnline              | periodeFermetureDebut  | periodeFermetureFin    |
      | GT_6476  | HOSP    | 1472_DELTA_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31    | %%LAST_YEAR%%-12-31    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-02 |
      | GT_6476  | HOSP    | 1472_DELTA_01_P01 | %%CURRENT_YEAR%%-07-03 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-03 | %%CURRENT_YEAR%%-12-31 |
      | GT_6476  | PHOR    | 1472_DELTA_01_P01 | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31    | %%LAST_YEAR%%-12-31    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-02 |
      | GT_6476  | PHOR    | 1472_DELTA_01_P01 | %%CURRENT_YEAR%%-07-03 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-03 | %%CURRENT_YEAR%%-12-31 |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-6434" content
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 0 and refCouverture 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      # Produit P01 Hospitalisation et Medecine
#      | HOSP | 2025/01/01 | 2024/12/31 | OFFLINE     | 2025/12/31   |
#      | PHOR | 2025/01/01 | 2024/12/31 | OFFLINE     | 2025/12/31   |
#
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 1 and refCouverture 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      # P02 Hospitalisation
#      | HOSP | 2025/01/01 | 2025/07/02 | ONLINE      | null         |
#      | HOSP | 2025/01/01 | 2025/07/02 | OFFLINE     | null         |
#       # P02 Medecine
#      | PHOR | 2025/01/01 | 2025/07/02 | ONLINE      | null         |
#      | PHOR | 2025/01/01 | 2025/07/02 | OFFLINE     | null         |
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 1 and refCouverture 0 and naturePrest 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      # P02 HospitaliZation
#      | HOSP | 2025/07/03 | null       | ONLINE      | null         |
#      | HOSP | 2025/07/03 | 2025/12/31 | OFFLINE     | null         |
#       # P02 MedeZine
#      | PHOR | 2025/07/03 | null       | ONLINE      | null         |
#      | PHOR | 2025/07/03 | 2025/12/31 | OFFLINE     | null         |



