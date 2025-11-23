Feature: Get Pau V4 not yet implemented

  @smokeTests @pauv5 @carences @caseConsolidation
  Scenario: Cas 5 : Adhésion à une garantie avec une carence non configurée
    Given I create a contract element from a file "gtbasebalooCase5"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas5"
    Then I wait for 0 declarations
    Then I get one more trigger with contract number "MBA0003" and amc "0000401166" and indice "0" for benef
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | erreur         | La carence CARINC n'est pas paramétrée pour la période du %%CURRENT_YEAR%%-01-01 au %%CURRENT_YEAR%%-03-31 pour l'OC BALOO, l'offre CAS5 et le produit PDT_BASE5 |
      | statut         | Error                                                                                                                                                            |
      | nir            | 1041062498044                                                                                                                                                    |
      | numeroPersonne | MBA0003-002                                                                                                                                                      |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE5   |
      | productCode       | PDT_BASE5  |
      | offerCode         | CAS5       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 0 benefitType for the right 0 and the different benefitType has this values
      | code | debut | fin |
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-25' '%%CURRENT_YEAR%%-04-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE5   |
      | productCode       | PDT_BASE5  |
      | offerCode         | CAS5       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 0 benefitType for the right 0 and the different benefitType has this values
      | code | debut | fin |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-06-25' '%%CURRENT_YEAR%%-07-05' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE5   |
      | productCode       | PDT_BASE5  |
      | offerCode         | CAS5       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut                  | fin                    |
      | APPAREILLAGEDENTAIRE | %%CURRENT_YEAR%%-06-25 | %%CURRENT_YEAR%%-07-05 |
      | DENTAIRE             | %%CURRENT_YEAR%%-06-25 | %%CURRENT_YEAR%%-07-05 |
      | HOSPITALISATION      | %%CURRENT_YEAR%%-06-25 | %%CURRENT_YEAR%%-07-05 |
      | OPTIQUE              | %%CURRENT_YEAR%%-06-25 | %%CURRENT_YEAR%%-07-05 |
      | PHARMACIE            | %%CURRENT_YEAR%%-06-25 | %%CURRENT_YEAR%%-07-05 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-25' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-01-15' '%%CURRENT_YEAR%%-01-20' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-03-25' '%%CURRENT_YEAR%%-04-05' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception
