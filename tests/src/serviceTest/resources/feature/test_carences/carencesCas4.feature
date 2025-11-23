Feature: Get Pau V4 for carences CAS4

  @smokeTests @pauv5 @carences @caseConsolidation
  Scenario: Cas 4 : Adhésion à une garantie avec plusieurs carence avec des droits de remplacement
    Given I create a contract element from a file "gtbasebalooCase4"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gtbasebalooCase4_2"
    And I create a contract element from a file "gtbasebalooCase4_3"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas4"
    Then I wait for 1 declaration
    Then there is 7 rightsDomains and the different rightsDomains has this values
      | garantie    | domaine | debut                  | fin                    | finOnline              |
      | GT_BASE4    | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE4    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_REMP4_01 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-31 | %%CURRENT_YEAR%%-03-31 |
      | GT_REMP4_02 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-30 | %%CURRENT_YEAR%%-06-30 |
      | GT_BASE4    | OPTI    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE4    | DENT    | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE4    | APDE    | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-12-31 | null                   |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_01  |
      | productCode       | PDT_REMP4_01 |
      | offerCode         | CAS4_REMP_01 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR001       |
    Then the contract and the right 2 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code    | debut                  | fin                    |
      | OPTIQUE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 2 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-15' '%%CURRENT_YEAR%%-04-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_01  |
      | productCode       | PDT_REMP4_01 |
      | offerCode         | CAS4_REMP_01 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR001       |
    Then the contract and the right 2 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code    | debut                  | fin                    |
      | OPTIQUE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    Then there is 1 benefitType for the right 2 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-06-27' '%%CURRENT_YEAR%%-07-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-06-30 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-07-02' '%%CURRENT_YEAR%%-07-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_01  |
      | productCode       | PDT_REMP4_01 |
      | offerCode         | CAS4_REMP_01 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR001       |
    Then the contract and the right 2 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code    | debut                  | fin                    |
      | OPTIQUE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 2 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-15' '%%CURRENT_YEAR%%-04-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_01  |
      | productCode       | PDT_REMP4_01 |
      | offerCode         | CAS4_REMP_01 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR001       |
    Then the contract and the right 2 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code    | debut                  | fin                    |
      | OPTIQUE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    Then there is 1 benefitType for the right 2 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-06-27' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-06-30 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-07-02' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_01  |
      | productCode       | PDT_REMP4_01 |
      | offerCode         | CAS4_REMP_01 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR001       |
    Then the contract and the right 2 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code    | debut                  | fin                    |
      | OPTIQUE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 2 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-15' '%%CURRENT_YEAR%%-04-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_01  |
      | productCode       | PDT_REMP4_01 |
      | offerCode         | CAS4_REMP_01 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR001       |
    Then the contract and the right 2 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code    | debut                  | fin                    |
      | OPTIQUE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    Then there is 1 benefitType for the right 2 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-06-27' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166   |
      | codeGT            | GT_REMP4_02  |
      | productCode       | PDT_REMP4_02 |
      | offerCode         | CAS4_REMP_02 |
      | insurerCode       | BALOO        |
      | originCode        | GT_BASE4     |
      | originInsurerCode | BALOO        |
      | waitingCode       | CAR002       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-06-30 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-07-02' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE4   |
      | productCode       | PDT_BASE4  |
      | offerCode         | CAS4       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "carences/contrattpcarence4" content
#    Then there is 5 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2025/07/01 | null       | ONLINE      |
#      | APDE | 2025/07/01 | 2025/12/31 | OFFLINE     |
#      | DENT | 2025/07/01 | null       | ONLINE      |
#      | DENT | 2025/07/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/01/01 | null       | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPTI | 2025/04/01 | null       | ONLINE      |
#      | OPTI | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | PHAR | 2025/01/01 | null       | ONLINE      |
#      | PHAR | 2025/01/01 | 2025/12/31 | OFFLINE     |
#    Then there is 5 domaineDroits on warranty 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | DENT | 2025/01/01 | 2025/06/30 | ONLINE      |
#      | DENT | 2025/01/01 | 2025/06/30 | OFFLINE     |
#      | OPTI | 2025/01/01 | 2025/03/31 | ONLINE      |
#      | OPTI | 2025/01/01 | 2025/03/31 | OFFLINE     |
