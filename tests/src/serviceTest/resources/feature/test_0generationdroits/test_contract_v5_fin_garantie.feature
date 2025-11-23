Feature: Test contract V5 with fin garantie

  Background:
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @caseFinGarantie
  Scenario: I send a contract with rights open in 2024 and closed after the end date of Bobb => no TP rights
    Given I create a contract element from a file "gtaxa_withEndDate"
    When I send a test contract from file "contratV5/contrat_ouvert2025"
    # Aucun produit trouvé : car le contrat a des droits du 2024-01-01 au 2024-12-31 mais BOBB est fermé à partir du 2024-10-01
    Then I wait for 0 declaration

  @smokeTests @caseFinGarantie
  Scenario: I send a contract with rights open after the period of Bobb => no TP rights
    Given I create a contract element from a file "gtaxa_withEndDate"
    When I send a test contract from file "contratV5/contrat_partiellementOuvert2024"
    # Aucun produit trouvé : car le contrat a des droits du 2024-11-01 au 2024-12-31 mais BOBB est fermé à partir du 2024-10-01
    Then I wait for 0 declaration

  # contrat avec un seul benef avec 2 garanties dont une pas totalement couvert par Bobb => pas de droits tp
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties inluding one that is not totally covered by Bobb : no TP rights on warranty not covered, and TP rights for the other
    Given I create a contract element from a file "gtbaseblueCase1"
    And I create a contract element from a file "gtaxa_withEndDate"
    When I send a test contract from file "generationDroitsTP_4943/finGarantie/fin_garantie_en_2024_2_garanties"
    When I get 1 trigger with contract number "MBA12756" and amc "0000401166"
    # une garantie fermé en 2024 (dans le passé : ignorée) - l'autre est ouverte et couverte
    Then I wait for 1 declaration
    And I wait for 1 contract

  # contrat avec un seul benef avec 2 garanties debutant en 2023 sans dateFin => droits TP complet pour les 2 garanties
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties starting in 2023 : TP rights for both warranties
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/debutGarantie/2_garanties_debutant_en_2023"
    When I get triggers with contract number "MBA12756" and amc "0000401166"
    Then I wait for 1 declarations
    Then I get 1 trigger with contract number "MBA12756" and amc "0000401166"
    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract

      # contrat avec un seul benef avec 2 garanties debutant en 2024 sans dateFin => droits TP complet pour les 2 garanties
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties staring in 2024 : TP rights for both warranties
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/debutGarantie/2_garanties_une_debutant_en_2024"
    When I get triggers with contract number "MBA12756" and amc "0000401166"
    Then I wait for 1 declarations
    Then I get 1 trigger with contract number "MBA12756" and amc "0000401166"
    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract

  # contrat avec un seul benef avec 2 garanties dont un debutant en 2023 et l'autre en 2025 sans dateFin => droits TP complet pour les 2 garanties
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties, one staring in 2023 and the other in %%CURRENT_YEAR%% : TP rights for both warranties
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/debutGarantie/2_garanties_une_debutant_en_2025"
    When I get triggers with contract number "MBA12756" and amc "0000401166"
    Then I wait for 1 declarations
    Then I get 1 trigger with contract number "MBA12756" and amc "0000401166"
    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1  | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract

  # contrat avec un seul benef avec 2 garanties dont un debutant en 2023 et l'autre en 2026 sans dateFin => droits TP complet pour la garantie en 2023
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties, one staring in 2023 and the other in %%NEXT_YEAR%% : TP rights for only one warranties
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/debutGarantie/2_garanties_une_debutant_en_2026"
    When I get triggers with contract number "MBA12756" and amc "0000401166"
    Then I wait for 1 declarations
    Then I get 1 trigger with contract number "MBA12756" and amc "0000401166"
    Then there is 6 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie  | domaine | debut                  | fin                    | finOnline |
      | GT_BLUE_1 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract

  # contrat avec un seul benef avec 2 garanties dont une fermée avant 2023 => pas de droits tp pour la garantie fermée, et droits TP complet pour la 2ème
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties inluding one that ends before 2023 : no TP rights on closed warranty and TP rights for the other
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/finGarantie/fin_garantie_avant_2023_2_garanties"
    When I get triggers with contract number "MBA12755" and amc "0000401166"
    Then I wait for 1 declarations
    Then I get 1 trigger with contract number "MBA12755" and amc "0000401166"
    Then there is 6 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie  | domaine | debut                  | fin                    | finOnline |
      | GT_BLUE_1 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract

      # contrat avec un seul benef avec 2 garanties dont une fermée avant l'année courante => pas de droits tp pour la garantie fermée, et droits TP complet pour la 2ème
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties inluding one that ends before 2023 : no TP rights on closed warranty and TP rights for the other
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/finGarantie/fin_garantie_en_2025_2_garanties"
    When I get triggers with contract number "MBA12757" and amc "0000401166"
    Then I wait for 1 declarations
    Then there is 6 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie  | domaine | debut                  | fin                    | finOnline |
      | GT_BLUE_1 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BLUE_1 | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
    And I wait for 1 contract

  # contrat avec un seul benef avec 2 garanties dont une fermée l'année courante => droits TP jusque date fin garantie pour la garantie fermée, et droits TP complet pour la 2ème
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties inluding one that ends in current year : TP rights on closed warranty up to end date and TP rights for the other
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/finGarantie/fin_garantie_en_2025_2_garanties-2"
    When I get triggers with contract number "MBA12757" and amc "0000401166"
    Then I wait for 1 declarations
    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-06 | %%CURRENT_YEAR%%-06-06 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-06 | %%CURRENT_YEAR%%-06-06 |
      | GT_BLUE_1  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BLUE_1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BLUE_1  | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BLUE_1  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BLUE_1  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BLUE_1  | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
    And I wait for 1 contract

  # contrat avec un seul benef avec 2 garanties dont une fermée l'année pro => droits TP jusque date fin garantie pour la garantie fermée (ONLINE jusque date fin garantie, OFFLINE jusque fin 2025) et droits TP complet pour la 2ème
  @smokeTests @caseFinGarantie
  Scenario: I send a contract with one benef with 2 warranties inluding one that ends in next year : TP rights on closed warranty up to end date and TP rights for the other
    Given I create a contract element from a file "gtbaseblueCase1"
    When I send a test contract from file "generationDroitsTP_4943/finGarantie/fin_garantie_en_2026_2_garanties"
    When I get triggers with contract number "MBA12757" and amc "0000401166"
    Then I wait for 1 declarations
    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut                  | fin                    | finOnline           |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-06 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-06 |
      | GT_BLUE_1  | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                |
      | GT_BLUE_1  | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                |
      | GT_BLUE_1  | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                |
      | GT_BLUE_1  | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                |
      | GT_BLUE_1  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                |
      | GT_BLUE_1  | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                |
    And I wait for 1 contract

  # contrat ouvert sur toute l'année en cours | contrat avec une fin de droit l'année pro => droits TP jusque date de fin droit (ONLINE jusque date fin droit, OFFLINE jusque fin année courante)
  @smokeTests @caseFinGarantie @caseConsolidation
  Scenario: I send a contract open in current year | I send the same contract with an end date for the rights in next year : TP rights up to right end date
    # Envoi d'un contrat avec des droits ouverts seulement sur toute l’année en cours
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
    Then the expected contract TP is identical to "v5_fin_garantie/contrat1" content
#    Then there is 2 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/01/01 | 2025/12/31 | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/01/01 | 2025/12/31 | ONLINE      |
#      | OPAU | 2025/01/01 | 2025/12/31 | OFFLINE     |

    # Envoi du contrat avec une dateFinDroit au %%NEXT_YEAR%%-05-28
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
    Then the expected contract TP is identical to "v5_fin_garantie/contrat2" content
#    Then there is 2 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/01/01 | 2026/05/28 | ONLINE      |
#      | OPAU | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/01/01 | 2026/05/28 | ONLINE      |

  # contrat ouvert sur toute l'année en cours | contrat avec une fin de de la periode assuré l'année pro => droits TP jusqu'à la dateFin de la periodeAssure (ONLINE jusque date fin de la periode assuré, OFFLINE jusque fin année courante)
  @smokeTests @caseFinGarantie @caseConsolidation
  Scenario: I send a contract open in current year | I send the same contract with an end date for the insured period in next year : TP rights up to insured period end date
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
    Then the expected contract TP is identical to "v5_fin_garantie/contrat3" content
#    Then there is 2 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/01/01 | 2025/12/31 | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/01/01 | 2025/12/31 | ONLINE      |
#      | OPAU | 2025/01/01 | 2025/12/31 | OFFLINE     |

    # Envoi du contrat avec une fin de periodeAssuré au %%NEXT_YEAR%%-05-28
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
    Then the expected contract TP is identical to "v5_fin_garantie/contrat4" content
#    Then there is 2 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/01/01 | 2026/05/28 | ONLINE      |
#      | OPAU | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPAU | 2025/01/01 | 2026/05/28 | ONLINE      |
