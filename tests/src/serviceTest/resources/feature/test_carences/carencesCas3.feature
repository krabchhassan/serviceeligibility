Feature: Get Pau V4 for carences CAS3

  @smokeTests @pauv5 @carences @caseConsolidation
  Scenario: Cas 3 : Adhésion à une garantie avec plusieurs carence sans droit de remplacement
    Given I create a contract element from a file "gtbasebalooCase3"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas3"
    Then I wait for 1 declaration
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_BASE3 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE3 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE3 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE3 | OPTI    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_BASE3 | APDE    | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-12-31 | null      |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-15' '%%CURRENT_YEAR%%-04-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-06-27' '%%CURRENT_YEAR%%-07-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-07-02' '%%CURRENT_YEAR%%-07-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-15' '%%CURRENT_YEAR%%-04-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-06-27' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-07-02' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-15' '%%CURRENT_YEAR%%-04-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-15 | %%CURRENT_YEAR%%-04-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-06-27' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-07-01 | %%CURRENT_YEAR%%-07-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-06-27 | %%CURRENT_YEAR%%-07-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-07-02' '%%CURRENT_YEAR%%-07-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE3   |
      | productCode       | PDT_BASE3  |
      | offerCode         | CAS3       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-07-02 | %%CURRENT_YEAR%%-07-05 |
    Then I wait for 1 contract
    Then the expected contract TP is identical to "carences/contrattpcarence3" content
#    Then there is 5 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2025/07/01 | null       | ONLINE      |
#      | APDE | 2025/07/01 | 2025/12/31 | OFFLINE     |
#      | DENT | 2025/01/01 | null       | ONLINE      |
#      | DENT | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/01/01 | null       | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPTI | 2025/04/01 | null       | ONLINE      |
#      | OPTI | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | PHAR | 2025/01/01 | null       | ONLINE      |
#      | PHAR | 2025/01/01 | 2025/12/31 | OFFLINE     |

