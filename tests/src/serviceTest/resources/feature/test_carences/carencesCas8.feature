Feature: Get Pau V4 not yet implemented

  @smokeTests @pauv5 @carences @caseConsolidation
  Scenario: Cas 8 : Naissance d’un enfant avec une GT en carence avec droit de remplacement + Renouvellement
    Given I create a contract element from a file "gtbasebalooCase8"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gtbasebalooCase8_2"
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas8"
    Then I wait for 1 declaration
    Then there is 3 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut                  | fin                    | finOnline           |
      | GT_REMP8 | PHAR    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-01-31 |
      | GT_REMP8 | DENT    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-01-31 |
      | GT_REMP8 | HOSP    | %%CURRENT_YEAR%%-10-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-01-31 |
    Then I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP8   |
      | productCode       | PDT_REMP8  |
      | offerCode         | CAS8_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE8   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-08-27' '%%CURRENT_YEAR%%-09-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE8   |
      | productCode       | PDT_BASE8  |
      | offerCode         | CAS8       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-08-27 | %%CURRENT_YEAR%%-09-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-08-27 | %%CURRENT_YEAR%%-09-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-08-27 | %%CURRENT_YEAR%%-09-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-08-27 | %%CURRENT_YEAR%%-09-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-08-27 | %%CURRENT_YEAR%%-09-05 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%NEXT_YEAR%%-04-02' '%%NEXT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE8   |
      | productCode       | PDT_BASE8  |
      | offerCode         | CAS8       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |

    Then I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP8   |
      | productCode       | PDT_REMP8  |
      | offerCode         | CAS8_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE8   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-08-27' '%%CURRENT_YEAR%%-09-05' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception

    Then I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_REMP8   |
      | productCode       | PDT_REMP8  |
      | offerCode         | CAS8_REMP  |
      | insurerCode       | BALOO      |
      | originCode        | GT_BASE8   |
      | originInsurerCode | BALOO      |
      | waitingCode       | CAR001     |
    Then there is 3 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | DENTAIRE        | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
      | HOSPITALISATION | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
      | PHARMACIE       | %%CURRENT_YEAR%%-10-15 | %%CURRENT_YEAR%%-10-20 |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-08-27' '%%CURRENT_YEAR%%-09-05' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "carences/contrattpcarence8" content
