Feature: Test renewal with batch

  @smokeTests @caseConsolidation @caseRenouvellement @toChangeEveryYear

  # envoi contrat (1 déclaration d'ouverture)
  # renouvellement année suivante (1 déclaration d'ouverture)
  # envoi contrat avec dateResiliation au 20-12 de l'année courante (2 déclarations : ouverture + fermeture)
  # envoi contrat avec dateRestitutionCarte au 25-12 de l'année courante (2 déclarations : ouverture + fermeture)

  Scenario: JIRA-5660
    Given I create a declarant from a file "declarantbaloo"
    And I create a contract element from a file "gt_5660"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA/IS |
      | libelle | KA/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | KA |
      | libelle | KA |
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    When I send a test contract from file "5660-Contrat1"
    Then I wait for 1 declaration
    Then there is 13 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie     | domaine | debut      | fin        | finOnline | isEditable |
      | BAL_5458_001 | LABO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | PHCO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | EXTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | LPPS    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | TRAN    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | PHNO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | MEDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | CSTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
      | BAL_5458_001 | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      | false      |
    When I renew the rights today
    When I wait "3" seconds in order to consume the data
    When I send a test contract from file "5660-Contrat2"
    Then I wait for 4 declarations
    Then there is 13 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie     | domaine | debut      | fin        | finOnline  | isEditable |
      | BAL_5458_001 | LABO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | PHCO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | EXTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | LPPS    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | TRAN    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | PHNO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | MEDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | CSTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |
      | BAL_5458_001 | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-20 | false      |

    Then there is 13 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie     | domaine | debut      | fin        | finOnline  | isEditable | periodeFermetureFin
      | BAL_5458_001 | LABO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | PHCO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | EXTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | LPPS    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | TRAN    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | PHNO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | MEDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | CSTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31
      | BAL_5458_001 | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | false      | %%CURRENT_YEAR%%-12-31

    When I send a test contract from file "5660-Contrat3"
    Then I wait for 6 declarations
    Then there is 13 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie     | domaine | debut      | fin        | finOnline  | periodeOfflineFin |
      | BAL_5458_001 | LABO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | PHCO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | EXTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | LPPS    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | AUDI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | TRAN    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | PHNO    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | MEDE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | CSTE    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |
      | BAL_5458_001 | PHOR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-20 | %%CURRENT_YEAR%%-12-25        |

    Then I wait for 1 contract
    # Date de finFermeture cohérante. Vérifier pourquoi dans consultationDroitsV4 on utilise pas la dateRestitution
    Then the expected contract TP is identical to "contrattp-5660" content
#    Then there is 13 domaineDroits on benef 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | AUDI | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | AUDI | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | AUDI | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | CSTE | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | CSTE | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | CSTE | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | DENT | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | DENT | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | DENT | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | EXTE | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | EXTE | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | EXTE | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | HOSP | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | HOSP | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | HOSP | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | LABO | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | LABO | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | LABO | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | LPPS | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | LPPS | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | LPPS | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | MEDE | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | MEDE | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | MEDE | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | OPTI | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | OPTI | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | OPTI | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | PHCO | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | PHCO | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | PHCO | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | PHNO | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | PHNO | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | PHNO | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | PHOR | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | PHOR | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | PHOR | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |
#      | TRAN | 2025/01/01 | 2025/12/20 | ONLINE      | null         |
#      | TRAN | 2025/01/01 | 2025/12/20 | OFFLINE     | 2025/12/31   |
#      | TRAN | 2026/01/01 | 2026/12/31 | OFFLINE     | null         |

  #TODO #todosoap
#    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "soapContrat/request5660_droitsOuverts.xml"
#    Then the expected soap response is identical to "soapContrat/expectedResponseContrat5660_droitsOuvertsV3.xml" content
#    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "soapContrat/request5660_droitsNonOuverts.xml"
#    Then the expected soap response is identical to "soapContrat/expectedResponseContrat5660_droitsNonOuverts.xml" content
