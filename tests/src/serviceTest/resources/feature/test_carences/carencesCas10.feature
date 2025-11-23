Feature: Get Pau V4

  @smokeTests @pauv5 @carences @caseConsolidation
    # mock settings ui : wp_baloo_Cas10
  Scenario: Cas 10 : Paramétrage de carence qui évolue pendant la carence du contrat HTP - Nature de prestation en moins
    Given I create a contract element from a file "gtbasebalooCase10"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gtbasebalooCase10_2"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas10"
    Then I wait for 1 declaration
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie  | domaine | debut                  | fin                    | finOnline              |
      | GT_BASE10 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE10 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_REMP10 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-01-31 | %%CURRENT_YEAR%%-01-31 |
      | GT_BASE10 | DENT    | %%CURRENT_YEAR%%-02-01 | %%CURRENT_YEAR%%-12-31 | null                   |
      | GT_BASE10 | OPTI    | %%CURRENT_YEAR%%-03-01 | %%CURRENT_YEAR%%-12-31 | null                   |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP10  |
      | productCode       | PDT_REMP10 |
      | offerCode         | CAS10_REMP |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE10  |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-02-27' '%%CURRENT_YEAR%%-03-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-03-01 | %%CURRENT_YEAR%%-03-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-28' '%%CURRENT_YEAR%%-02-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP10  |
      | productCode       | PDT_REMP10 |
      | offerCode         | CAS10_REMP |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE10  |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-02-01 | %%CURRENT_YEAR%%-02-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-02-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-02-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-01-31 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP10  |
      | productCode       | PDT_REMP10 |
      | offerCode         | CAS10_REMP |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE10  |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-02-27' '%%CURRENT_YEAR%%-03-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-03-01 | %%CURRENT_YEAR%%-03-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-28' '%%CURRENT_YEAR%%-02-05' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP10  |
      | productCode       | PDT_REMP10 |
      | offerCode         | CAS10_REMP |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE10  |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-02-01 | %%CURRENT_YEAR%%-02-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-02-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-02-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-01-31 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP10  |
      | productCode       | PDT_REMP10 |
      | offerCode         | CAS10_REMP |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE10  |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-15 | %%CURRENT_YEAR%%-01-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-02-27' '%%CURRENT_YEAR%%-03-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 4 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
      | OPTIQUE         | %%CURRENT_YEAR%%-03-01 | %%CURRENT_YEAR%%-03-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-02-27 | %%CURRENT_YEAR%%-03-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-28' '%%CURRENT_YEAR%%-02-05' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE10  |
      | productCode       | PDT_BASE10 |
      | offerCode         | CAS10      |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then the contract and the right 1 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP10  |
      | productCode       | PDT_REMP10 |
      | offerCode         | CAS10_REMP |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE10  |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-02-01 | %%CURRENT_YEAR%%-02-05 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-02-05 |
      | PHARMACIE       | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-02-05 |
    Then there is 1 benefitType for the right 1 and the different benefitType has this values
      | code     | debut                  | fin                    |
      | DENTAIRE | %%CURRENT_YEAR%%-01-28 | %%CURRENT_YEAR%%-01-31 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "carences/contrattpcarence10" content
