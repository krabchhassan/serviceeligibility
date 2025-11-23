Feature: Get Pau V4 for carences CAS7

  @smokeTests @pauv5 @carences @caseConsolidation
  Scenario: Cas 7 : Naissance d’un enfant avec une GT en carence sans droit de remplacement + Renouvellement
    Given I create a contract element from a file "gtbasebalooCase7"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "contractForPauV5/beneficiaireCarence"
    When I send a test contract from file "contractForPauV5/createServicePrestationCas7"
    Then I wait for 0 declaration

    Then I get one more trigger with contract number "MBA0003" and amc "0000401166" and indice "0" for benef
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | erreur         | La ou les carences positionnées ne permettent pas de générer des droits TP pour la période demandée |
      | statut         | Warning                                                                                             |
      | nir            | 1041062498044                                                                                       |
      | numeroPersonne | MBA0003-002                                                                                         |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 HTP
    Then With this request I have a contract not found exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-12-31' '%%NEXT_YEAR%%-03-31' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE7   |
      | productCode       | PDT_BASE7  |
      | offerCode         | CAS7       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut               | fin                 |
      | APPAREILLAGEDENTAIRE | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | DENTAIRE             | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | HOSPITALISATION      | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | OPTIQUE              | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | PHARMACIE            | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-12-31' '%%NEXT_YEAR%%-03-31' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-12-31' '%%NEXT_YEAR%%-03-31' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception

    # Renouvellement
    When I renew the rights on "%%NEXT_YEAR%%-01-01" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "MBA0003" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1041062498044 |
      | numeroPersonne | MBA0003-002   |

    When I wait for 1 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_BASE7 | PHAR    | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_BASE7 | DENT    | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_BASE7 | HOSP    | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_BASE7 | OPTI    | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_BASE7 | APDE    | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-12-31 | null      |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%NEXT_YEAR%%-01-01' '%%NEXT_YEAR%%-01-31' 0000401166 HTP
    Then With this request I have a contract not found exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-12-31' '%%NEXT_YEAR%%-03-31' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE7   |
      | productCode       | PDT_BASE7  |
      | offerCode         | CAS7       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut               | fin                 |
      | APPAREILLAGEDENTAIRE | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | DENTAIRE             | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | HOSPITALISATION      | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | OPTIQUE              | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | PHARMACIE            | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-12-31' '%%NEXT_YEAR%%-03-31' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE7   |
      | productCode       | PDT_BASE7  |
      | offerCode         | CAS7       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut               | fin                 |
      | APPAREILLAGEDENTAIRE | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | DENTAIRE             | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | HOSPITALISATION      | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | OPTIQUE              | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | PHARMACIE            | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |

    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-10-15' '%%CURRENT_YEAR%%-10-20' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception
    When I get contrat PAUV5 for 1041062498044 '20041026' 1 '%%CURRENT_YEAR%%-12-31' '%%NEXT_YEAR%%-03-31' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166 |
      | codeGT            | GT_BASE7   |
      | productCode       | PDT_BASE7  |
      | offerCode         | CAS7       |
      | insurerCode       | BALOO      |
      | originCode        | null       |
      | originInsurerCode | null       |
      | waitingCode       | null       |
    Then there is 5 benefitType for the right 0 and the different benefitType has this values
      | code                 | debut               | fin                 |
      | APPAREILLAGEDENTAIRE | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | DENTAIRE             | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | HOSPITALISATION      | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | OPTIQUE              | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
      | PHARMACIE            | %%NEXT_YEAR%%-02-01 | %%NEXT_YEAR%%-03-31 |
