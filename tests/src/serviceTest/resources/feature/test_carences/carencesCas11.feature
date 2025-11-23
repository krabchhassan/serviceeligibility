Feature: Cas 11 : Ajout d'une période de carence sur un contrat HTP existant - Génération des droits TP

  @smokeTests @pauv5 @carences @caseConsolidation
  Scenario: Cas 11 : Ajout d'une période de carence sur un contrat HTP existant - Génération des droits TP
    And I create a contract element from a file "gtbasebalooCase2"
    And I create a contract element from a file "gtbasebalooCase2_2"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas11-1"
    Then I wait for 1 declaration
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut                  | fin                    |
      | GT_BASE2 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | APDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |

    When I send a test contract from file "contractForPauV5/createServicePrestationCas11-2"
    Then I wait for 3 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                  | fin                 |
      | GT_BASE2 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GT_BASE2 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GT_BASE2 | DENT    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GT_BASE2 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
      | GT_BASE2 | APDE    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 |
    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    |
      | GT_BASE2 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_REMP2 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-31 |
      | GT_REMP2 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-31 |
      | GT_REMP2 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-31 |
      | GT_BASE2 | HOSP    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | DENT    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | OPTI    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 |
      | GT_BASE2 | APDE    | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-12-31 |

    Then I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP2   |
      | productCode       | PDT_REMP2  |
      | offerCode         | CAS2_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE2   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 1 benefitType for the right 0 and the different benefitType has this values
      | code      | debut                  | fin                    |
      | PHARMACIE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 3 benefitType for the right 1 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP2   |
      | productCode       | PDT_REMP2  |
      | offerCode         | CAS2_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE2   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 3 benefitType for the right 1 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
      | OPTIQUE         | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
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

    Then I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP2   |
      | productCode       | PDT_REMP2  |
      | offerCode         | CAS2_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE2   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 1 benefitType for the right 0 and the different benefitType has this values
      | code      | debut                  | fin                    |
      | PHARMACIE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 3 benefitType for the right 1 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP2   |
      | productCode       | PDT_REMP2  |
      | offerCode         | CAS2_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE2   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 3 benefitType for the right 1 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
      | OPTIQUE         | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
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

    Then I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP2   |
      | productCode       | PDT_REMP2  |
      | offerCode         | CAS2_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE2   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | DENTAIRE             | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | OPTIQUE              | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE            | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 3 benefitType for the right 1 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | OPTIQUE         | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-27' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP2   |
      | productCode       | PDT_REMP2  |
      | offerCode         | CAS2_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE2   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-04-05 |
    Then there is 3 benefitType for the right 1 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
      | OPTIQUE         | %%CURRENT_YEAR%%-03-27 | %%CURRENT_YEAR%%-03-31 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE2   |
      | productCode       | PDT_BASE2  |
      | offerCode         | CAS2       |
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
    Then the expected contract TP is identical to "carences/contrattpcarence11" content
#    Then there is 5 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | APDE | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | APDE | 2025/04/01 | null       | ONLINE      |
#      | DENT | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | DENT | 2025/04/01 | null       | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | HOSP | 2025/04/01 | null       | ONLINE      |
#      | OPTI | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | OPTI | 2025/04/01 | null       | ONLINE      |
#      | PHAR | 2025/01/01 | 2025/12/31 | OFFLINE     |
#      | PHAR | 2025/01/01 | null       | ONLINE      |
#    Then there is 5 domaineDroits on warranty 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | DENT | 2025/01/01 | 2025/03/31 | ONLINE      |
#      | DENT | 2025/01/01 | 2025/03/31 | OFFLINE     |
#      | HOSP | 2025/01/01 | 2025/03/31 | ONLINE      |
#      | HOSP | 2025/01/01 | 2025/03/31 | OFFLINE     |
#      | OPTI | 2025/01/01 | 2025/03/31 | ONLINE      |
#      | OPTI | 2025/01/01 | 2025/03/31 | OFFLINE     |
