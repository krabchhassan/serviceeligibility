Feature: Test contract V5 with radiation or resiliation and suspension

  @smokeTests @caseRadiationAndSuspension @caseConsolidation @release
  Scenario: I send a contract with radiation cas 1 and suspension
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a beneficiaire from file "beneficiaryForPauV4/beneficiaire-5010"
    When I send a test contract from file "contratV5/5010-01"
    And I wait "3" seconds in order to consume the data
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declarations
    Then the declaration has the etat suspension "1" with this values
      | debut                  | fin        | typeSuspension | motifSuspension          | motifLeveeSuspension |
      | %%CURRENT_YEAR%%/03/01 | null       | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | %%CURRENT_YEAR%%/02/01 | 2025/03/01 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-01 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-04-01 | %%CURRENT_YEAR%%-04-01 |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "5010/contrat1" content

    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '%%CURRENT_YEAR%%-02-02' '%%CURRENT_YEAR%%-03-02' 0000401166 TP_OFFLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-02-02 | %%CURRENT_YEAR%%-03-02 |
      | OPTIQUEAUDIO    | %%CURRENT_YEAR%%-02-02 | %%CURRENT_YEAR%%-03-02 |

    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '%%CURRENT_YEAR%%-02-02' '%%CURRENT_YEAR%%-03-02' 0000401166 TP_ONLINE
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-02-02 | %%CURRENT_YEAR%%-03-02 |
      | OPTIQUEAUDIO    | %%CURRENT_YEAR%%-02-02 | %%CURRENT_YEAR%%-03-02 |

    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '%%CURRENT_YEAR%%-02-02' '%%CURRENT_YEAR%%-03-02' 0000401166 HTP
    Then the contract and the right 0 data has values
      | insurerId         | 0000401166       |
      | codeGT            | AXASCCGDIM       |
      | productCode       | AXASC_CGDIM      |
      | offerCode         | CONTRATGROUPEAXA |
      | insurerCode       | AXA              |
      | originCode        | null             |
      | originInsurerCode | null             |
      | waitingCode       | null             |
    Then there is 2 benefitType for the right 0 and the different benefitType has this values
      | code            | debut                  | fin                    |
      | HOSPITALISATION | %%CURRENT_YEAR%%-02-02 | %%CURRENT_YEAR%%-03-02 |
      | OPTIQUEAUDIO    | %%CURRENT_YEAR%%-02-02 | %%CURRENT_YEAR%%-03-02 |

    # le benef a les droits offline jusqu'au 31/12/2025 -> date fin fermeture
    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-06-02' 0000401166 TP_OFFLINE
    Then With this request I have a contract resiliated exception

    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-06-02' 0000401166 TP_ONLINE
    Then With this request I have a contract resiliated exception

    When I get contrat PAUV5 for 2730781111112 '20080519' 1 '%%CURRENT_YEAR%%-04-02' '%%CURRENT_YEAR%%-06-02' 0000401166 HTP
    Then With this request I have a contract not found exception

    # TO CHANGE TO IDB / CLC
#    Then I post rest request from file "./features/resources/consultationRest/5010_benef_request.json" to the consultation endpoint
#    Then the expected response "./features/resources/consultationRest/5010_benef_response.json" is identical
#
#    Then I post rest request from file "./features/resources/consultationRest/5010_benef_request_juillet.json" to the consultation endpoint
#    Then the expected response "./features/resources/consultationRest/5010_benef_response_juillet.json" is identical


  @smokeTests @caseRadiationAndSuspension
  Scenario: I send a contract with radiation cas 2 and suspension
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/5010-02"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declarations
    Then the declaration has the etat suspension "1" with this values
      | debut                  | fin                    | typeSuspension | motifSuspension          | motifLeveeSuspension |
      | %%CURRENT_YEAR%%/03/01 | null                   | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | %%CURRENT_YEAR%%/02/01 | %%CURRENT_YEAR%%/03/01 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-10-10 | %%CURRENT_YEAR%%-10-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-10-10 | %%CURRENT_YEAR%%-10-10 |
    Then I wait for 1 contract

  @smokeTests @caseRadiationAndSuspension
  Scenario: I send a contract with radiation cas 3 and suspension
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/5010-03"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declarations
    Then the declaration has the etat suspension "1" with this values
      | debut                  | fin                    | typeSuspension | motifSuspension          | motifLeveeSuspension |
      | %%CURRENT_YEAR%%/03/01 | null                   | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | %%CURRENT_YEAR%%/02/01 | %%CURRENT_YEAR%%/03/01 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-01-10 | %%CURRENT_YEAR%%-01-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-01-10 | %%CURRENT_YEAR%%-01-10 |
    Then I wait for 1 contract

  @smokeTests @caseRadiationAndSuspension
  Scenario: I send a contract with radiation cas 4 and suspension
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contratV5/5010-04"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declarations
    Then the declaration has the etat suspension "1" with this values
      | debut                  | fin                    | typeSuspension | motifSuspension          | motifLeveeSuspension |
      | %%CURRENT_YEAR%%/04/01 | null                   | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | %%CURRENT_YEAR%%/02/01 | %%CURRENT_YEAR%%/03/01 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-10 |
    Then I wait for 1 contract


  @smokeTests @caseRadiationAndSuspension
  Scenario: I send a contract with radiation and suspension + I send a contract with new suspensions
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/5010-04"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declarations
    Then the declaration has the etat suspension "1" with this values
      | debut                  | fin                    | typeSuspension | motifSuspension          | motifLeveeSuspension |
      | %%CURRENT_YEAR%%/04/01 | null                   | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | %%CURRENT_YEAR%%/02/01 | %%CURRENT_YEAR%%/03/01 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-10 |
    When I send a test contract from file "contratV5/5010-05"
    Then I wait for 2 declarations
    Then the declaration has the etat suspension "1" with this values with indice 1
      | debut                  | fin                    | typeSuspension | motifSuspension          | motifLeveeSuspension |
      | %%CURRENT_YEAR%%/04/03 | null                   | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | %%CURRENT_YEAR%%/04/01 | %%CURRENT_YEAR%%/04/02 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
      | %%CURRENT_YEAR%%/02/01 | %%CURRENT_YEAR%%/03/01 | Provisoire     | NON_PAIEMENT_COTISATIONS | null                 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-10 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-03-10 | %%CURRENT_YEAR%%-03-10 |
    Then I wait for 1 contract
