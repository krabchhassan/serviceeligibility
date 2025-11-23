Feature: Get Pau V4 for carences CAS1

  @smokeTests @pauv5 @carences @caseConsolidation @release
  Scenario: Cas 1 : Adhésion à une garantie avec une carence sans droit de remplacement
    And I create a contract element from a file "gtbasebalooCase1"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas1"
    Then I wait for 1 declaration
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_BASE  | OPTI    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | HOSP    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | DENT    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE  | APDE    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 1 benefitType for the right 0 and the different benefitType has this values
      | code      | debut                  | fin                    |
      | PHARMACIE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 1 benefitType for the right 0 and the different benefitType has this values
      | code      | debut                  | fin                    |
      | PHARMACIE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 1 benefitType for the right 0 and the different benefitType has this values
      | code      | debut                  | fin                    |
      | PHARMACIE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE    |
      | productCode       | PDT_BASE1  |
      | offerCode         | CAS1       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
    Then I wait for 1 contract
    Then the expected contract TP is identical to "carences/contrattpcarence1" content
#    Then there is 5 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2025/04/01 | null       | ONLINE      |
#      | APDE | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | DENT | 2025/04/01 | null       | ONLINE      |
#      | DENT | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/04/01 | null       | ONLINE      |
#      | HOSP | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | OPTI | 2025/04/01 | null       | ONLINE      |
#      | OPTI | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | PHAR | 2025/01/01 | null       | ONLINE      |
#      | PHAR | 2025/01/01 | 2025/12/31 | OFFLINE     |

