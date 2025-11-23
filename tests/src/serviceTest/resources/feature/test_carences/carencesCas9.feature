Feature: Get Pau V4

  @smokeTests @pauv5 @carences @caseConsolidation
  Scenario: Cas 9 : Paramétrage de carence qui évolue pendant la carence du contrat HTP - Nature de prestation en plus
    Given I create a contract element from a file "gtbasebalooCase9"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gtbasebalooCase9_2"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas9"
    Then I wait for 1 declaration
    Then there is 6 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline              |
      | GT_BASE9 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE9 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE9 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-01-31 | %%CURRENT_YEAR%%-01-31 |
      | GT_REMP9 | DENT    | %%CURRENT_YEAR%%-02-01 | %%CURRENT_YEAR%%-03-31 | %%CURRENT_YEAR%%-03-31 |
      | GT_BASE9 | DENT    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE9 | OPTI    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 | null                   |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP9   |
      | productCode       | PDT_REMP9  |
      | offerCode         | CAS9_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE9   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP9   |
      | productCode       | PDT_REMP9  |
      | offerCode         | CAS9_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE9   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP9   |
      | productCode       | PDT_REMP9  |
      | offerCode         | CAS9_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE9   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE9   |
      | productCode       | PDT_BASE9  |
      | offerCode         | CAS9       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-04-05 |
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "carences/contrattpcarence9" content
#    Then there is 4 domaineDroits on warranty 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | DENT | 2025/01/01 | 2025/01/31 | ONLINE      |
#      | DENT | 2025/04/01 | null       | ONLINE      |
#      | DENT | 2025/01/01 | 2025/01/31 | OFFLINE     |
#      | DENT | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/01/01 | null       | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPTI | 2025/04/01 | null       | ONLINE      |
#      | OPTI | 2025/04/01 | 2025/12/31 | OFFLINE     |
#      | PHAR | 2025/01/01 | null       | ONLINE      |
#      | PHAR | 2025/01/01 | 2025/12/31 | OFFLINE     |
#
#    Then there is 4 domaineDroits on warranty 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | DENT | 2025/02/01 | 2025/03/31 | ONLINE      |
#      | DENT | 2025/02/01 | 2025/03/31 | OFFLINE     |
