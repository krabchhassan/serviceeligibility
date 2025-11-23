Feature: Search contract PAU V3 -> V4

  @smokeTests @pauv5 @release
  Scenario: Get pau TP_OFFLINE
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contract1"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response1-TPOFF" content

  @smokeTests @pauv5
  Scenario: Get pau TP_OFFLINE without nirCode but with subscriberId
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contract1"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE 00052492
    Then the pau is identical to "pau/v5/response1-TPOFF" content

  @smokeTests @pauv5
  Scenario: Get pau TP_OFFLINE without nirCode and without subscriberId
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contract1"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then an error "400" is returned with message "Veuillez renseigner les critères de recherche du bénéficiaire"

  @smokeTests @pauv5
  Scenario: Get pau TP_OFFLINE (sans beneficiaire)
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contract1"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2016-01-02' '2017-01-03' 0000401166 TP_OFFLINE
    Then I have a beneficiary without subscriber not found exception

  @smokeTests @pauv5
  Scenario: Get pau TP_OFFLINE (sans contrat)
    Given I drop the collection for Contract
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception

  @smokeTests @pauv5
  Scenario: Get pau TP_OFFLINE (2 contrats)
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contract1"
    Given I create a contrat from file "pau/v5/contract2"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response1-2contrats-TPOFF" content

  @smokeTests @pauv5 @suspension
  Scenario: Get pau TP_OFFLINE with suspension
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contractWithSuspension"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response1-withSuspension-TPOFF" content

  @smokeTests @pauv5 @suspension
  Scenario: Get pau TP_OFFLINE with suspension with request dates out of suspension
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contractWithSuspension"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2021-01-03' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response2-withSuspension-TPOFF" content

  @smokeTests @pauv5 @suspension
  Scenario: Get pau TP_OFFLINE with 3 suspensions, only one requested
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contractWith3Suspensions"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2022-11-11' '2022-11-11' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response3-withSuspension-TPOFF" content

  @smokeTests @pauv5 @suspension
  Scenario: Get pau TP_OFFLINE with suspension
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contractWith3Suspensions"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response4-with3Suspensions-TPOFF" content

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2022-09-18' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response4-with1Suspension-TPOFF" content

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2022-11-18' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response4-with2Suspensions-TPOFF" content

    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2022-09-18' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response4-with1Suspension-TPOFF" content

  @smokeTests @pauv5 @suspension
  Scenario: Get pau TP_OFFLINE with suspension
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contractWith3Suspensions"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2022-12-17' '2022-12-17' 0000401166 TP_OFFLINE
    Then the pau is identical to "pau/v5/response5-withSuspension-TPOFF" content

  @smokeTests @pauv5
  Scenario: Get pau ALL CONTEXT
    Given I drop the collection for Contract
    Given I create a contrat from file "pau/v5/contract1"
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "contratHTPMultiple"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-12-17' '2022-12-17' 0000401166 TP_ONLINE,TP_OFFLINE,HTP
    Then an error "400" is returned with message "Le paramètre context doit ne contenir qu'un seul contexte"


  @smokeTests @pauv5
  Scenario: Get pau HTP
    Given I create a beneficiaire from file "pau/v5/beneficiaire"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Adhesion_Cas01"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 HTP
    Then With this request I have a contract not found exception

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas1
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Adhesion_Cas01"
    When I get contrat PAUV5 for 1791062498044 '19791006' 1 '2022-01-10' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response1-HTP" content


  @smokeTests @pauv5
  Scenario: Get pau HTP Cas1bis
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Adhesion_Cas01"
    When I get contrat PAUV5 for 1791062498044 '19791006' 1 '2022-06-15' '2022-07-15' 0000401166 HTP
    Then the pau is identical to "pau/v5/response2-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas2
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP2"
    Given I create a contract element from a file "ce_baloo2"
    Given I create a service prestation from a file "Adhesion_Cas02"
    When I get contrat PAUV5 for 1791062498044 '19791006' 1 '2022-01-10' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response3-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas2bis
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP2"
    Given I create a contract element from a file "ce_baloo2"
    Given I create a service prestation from a file "Adhesion_Cas02"
    When I get contrat PAUV5 for 1791062498044 '19791006' 1 '2022-06-15' '2022-07-15' 0000401166 HTP
    Then the pau is identical to "pau/v5/response4-HTP" content


  @smokeTests @pauv5
  Scenario: Get pau HTP Cas3
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP3"
    Given I create a contract element from a file "ce_baloo3"
    Given I create a service prestation from a file "Adhesion_Cas03"
    When I get contrat PAUV5 for 1791062498044 '19791006' 1 '2022-06-15' '2022-07-15' 0000401166 HTP
    Then the pau is identical to "pau/v5/response5-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas4
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP4"
    Given I create a contract element from a file "ce_baloo4"
    Given I create a service prestation from a file "Adhesion_Cas04"
    When I get contrat PAUV5 for 1791054142506 '19791006' 1 '2022-01-10' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response6-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas4bis
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP4"
    Given I create a contract element from a file "ce_baloo4"
    Given I create a service prestation from a file "Adhesion_Cas04"
    When I get contrat PAUV5 for 1791054142506 '19791006' 1 '2022-06-15' '2022-07-15' 0000401166 HTP
    Then the pau is identical to "pau/v5/response7-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas5
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP5"
    Given I create a contract element from a file "ce_baloo5"
    Given I create a service prestation from a file "Adhesion_Cas05"
    When I get contrat PAUV5 for 1791054102543 '19791006' 1 '2022-01-10' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response8-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas6 (cas de la GT Ignored, on renvoit un contrat avec des droits sans produit)
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP6"
    Given I create a contract element from a file "ce_baloo6"
    Given I create a service prestation from a file "Adhesion_Cas06"
    When I get contrat PAUV5 for 1791054102544 '19791006' 1 '2022-01-10' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response9-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas6 (cas de la GT sans produit, on renvoit un contrat avec des droits sans produit)
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP6"
    Given I create a contract element from a file "ce_baloo6bis"
    Given I create a service prestation from a file "Adhesion_Cas06"
    When I get contrat PAUV5 for 1791054102544 '19791006' 1 '2022-01-10' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response10-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas pere et mere famille divorcé cas 1
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTPPere"
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTPMere"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Cas01_Contrat_Mere"
    Given I create a service prestation from a file "Cas01_Contrat_Pere"
    When I get contrat PAUV5 for 1791059632524 '20041026' 1 '2022-01-01' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response11-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas pere et mere famille divorcé cas 2
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTPPere"
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTPMere"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Cas01_Contrat_Mere"
    Given I create a service prestation from a file "Cas01_Contrat_Pere"
    When I get contrat PAUV5 for 2791059632524 '20041026' 1 '2022-01-01' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response12-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas contrats aidés 2022
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTPCMU"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Cas02_Contrat_Aides_01"
    Given I create a service prestation from a file "Cas02_Contrat_Aides_02"
    When I get contrat PAUV5 for 1791059632525 '19791006' 1 '2022-01-01' '2022-12-31' 0000401166 HTP
    Then the pau is identical to "pau/v5/response13-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP Cas contrats aidés 2023
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTPCMU"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Cas02_Contrat_Aides_01"
    Given I create a service prestation from a file "Cas02_Contrat_Aides_02"
    When I get contrat PAUV5 for 1791059632525 '19791006' 1 '2024-01-01' '2024-01-03' 0000401166 HTP
    Then the pau is identical to "pau/v5/response14-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau HTP with new field in contractCollective
    Given I create a beneficiaire from file "pau/v5/beneficiaireHTP"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "servicePrestationWithCollectif"
    When I get contrat PAUV5 for 1791062498044 '19791006' 1 '2022-01-10' '2022-01-10' 0000401166 HTP
    Then the pau is identical to "pau/v5/response15-HTP" content

  @smokeTests @pauv5
  Scenario: Get pau TP_OFFLINE from TDB with OPTI domain
    When I create a contrat from file "pau/v5/contratTP"
    Given I create a beneficiaire from file "pau/v5/beneficiairePauPC00020"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2021-02-15' '2022-11-01' 0000401166 TP_OFFLINE for domains OPTI for beneficiaryId 0000401166-PC00020
    Then the pau is identical to "pau/v5/response16-HTP" content
