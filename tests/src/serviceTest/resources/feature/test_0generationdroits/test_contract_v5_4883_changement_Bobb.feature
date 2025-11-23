Feature: Test contract V5 with Bobb Changement version

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @caseChangementBobb
  Scenario: I send a contract with modification product Bobb on tomorrow (not possible, The GT doesn't cover all the right).
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4883"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    When I end mapping of product element with "CONTRATGROUPEAXA", "AXASC_CGDIM", "", "CONTRAT_GROUPE_AXA" to the contract element with "CASSA", "AXASCCGDIM", "AXA" on tomorrow
    And I send a test contract from file "contratV5/4883"
    # Aucun produit trouvé :)
    Then I wait for 1 declarations


  @smokeTests @caseChangementBobb
  Scenario: I send a contract with modification product Bobb
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/4883"
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then I wait for 1 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    When I update product element with "CONTRATGROUPEAXA", "AXASC_CGDIM", "OPTIQUEAUDIO", "CONTRAT_GROUPE_AXA" to the contract element with "CASSA", "AXASCCGDIM", "AXA"
    And I wait "1" seconds in order to consume the data
    And I send a test contract from file "contratV5/4883"

    Then I wait for 3 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut                  | fin                 | finOnline           | periodeFermetureDebut  | periodeFermetureFin    |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |

    Then there is 1 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut                  | fin                    | finOnline |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |


  @smokeTests @caseChangementBobb @caseConsolidation @elasticHistoContrat @release
  Scenario: I send a contract with modification product Bobb with contract historic
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I delete the contract histo for this contract 01599324
    When I send a test contract from file "contratV5/4883"
    # pas de paramétrage carte tp
    Then I wait for 0 declarations

    Then I wait for the first trigger with contract number "01599324" and amc "0000401166" to be "ProcessedWithErrors"
    Given I abandon the trigger

    And I create manual TP card parameters from file "parametrageCarteTPManuel2022"
    And I renew the rights today with mode "RDO"

    Then I wait for 1 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | OPAU    | 2022-01-01 | 2022-12-31 | null      |
      | AXASCCGDIM | HOSP    | 2022-01-01 | 2022-12-31 | null      |

    And I wait for 0 contract histo for this contract 01599324

    And I update product element with "OFFER", "PDT_4883", "HOSPITALISATION", "BALOO" to the contract element with "CASSA", "AXASCCGDIM", "AXA"

    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2023"
    And I renew the rights today with mode "RDO"

    Then I wait for 3 declarations
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  | periodeFermetureDebut | periodeFermetureFin |
      | AXASCCGDIM | OPAU    | 2022-01-01 | 2022-12-31 | 2022-12-31 | 2023-01-01            | 2022-12-31          |
      | AXASCCGDIM | HOSP    | 2022-01-01 | 2022-12-31 | 2022-12-31 | 2023-01-01            | 2022-12-31          |

    Then there is 1 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | null      |

    And I wait for 1 contract histo for this contract 01599324

    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"

    And I update product element with "CAS2_JUIN", "PDT_JUIN", "HOSPITALISATION", "BALOO" to the contract element with "CASSA", "AXASCCGDIM", "AXA"
    And I wait "3" seconds in order to consume the data
    And I renew the rights today with mode "RDO"

    Then I wait for the last renewal trigger with contract number "01599324" and amc "0000401166" to be "ProcessedWithErrors"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut           | Error                                                                        |
      | nir              | 2730781111112                                                                |
      | numeroPersonne   | 2124744                                                                      |
      | derniereAnomalie | Le paramétrage de l'atelier produit ne couvre pas la globalité des droits TP |

    Then I drop the collection SasContract
    When I remove all TP card parameters from database
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"

    And I update product element with "OFFER", "PDT_4883", "HOSPITALISATION", "BALOO" to the contract element with "CASSA", "AXASCCGDIM", "AXA"
    And I wait "3" seconds in order to consume the data
    And I renew the rights today with mode "RDO"

    Then I wait for 6 declarations

    And I wait for 2 contract histo for this contract 01599324

    Then I wait for 1 contract
    Then the expected contract TP is identical to "4883/contrattp-1" content
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 0 and refCouverture 0 and the different domaineDroits has this values
#      | code | codeGarantie | codeProduit | referenceCouverture | naturePrestation | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2022/01/01 | 2022/12/31 | ONLINE      | null         |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2022/01/01 | 2022/12/31 | OFFLINE     | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2022/01/01 | 2022/12/31 | ONLINE      | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2022/01/01 | 2022/12/31 | OFFLINE     | null         |
#
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 1 and refCouverture 0 and the different domaineDroits has this values
#      | code | codeGarantie | codeProduit | referenceCouverture | naturePrestation | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | AXASCCGDIM   | PDT_4883    | HOSP-022-100        | HOSPITALISATION  | 2023/01/01 | 2023/12/31 | ONLINE      | null         |
#      | HOSP | AXASCCGDIM   | PDT_4883    | HOSP-022-100        | HOSPITALISATION  | 2023/01/01 | 2023/12/31 | OFFLINE     | null         |
#
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 1 and refCouverture 1 and the different domaineDroits has this values
#      | code | codeGarantie | codeProduit | referenceCouverture | naturePrestation | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | AXASCCGDIM   | PDT_4883    | HOSP-032-120        | HOSPITALISATION  | 2024/01/01 | null       | ONLINE      | null         |
#      | HOSP | AXASCCGDIM   | PDT_4883    | HOSP-032-120        | HOSPITALISATION  | 2024/01/01 | 2024/12/31 | OFFLINE     | null         |

    When I remove all TP card parameters from database
    And I create TP card parameters from file "parametrageTPBaloo"
    And I create a contract element from a file "gtaxa"
    When I send a test contract from file "contratV5/4883-resil"

    Then I wait for 8 declarations


    Then I wait for 1 contract
    Then the expected contract TP is identical to "4883/contrattp-2" content
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 0 and refCouverture 0 and the different domaineDroits has this values
#      | code | codeGarantie | codeProduit | referenceCouverture | naturePrestation | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2022/01/01 | 2022/12/31 | ONLINE      | null         |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2022/01/01 | 2022/12/31 | OFFLINE     | null         |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2025/01/01 | 2026/08/01 | ONLINE      | null         |
#      | HOSP | AXASCCGDIM   | AXASC_CGDIM | HOSP-099            | HOSPITALISATION  | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2022/01/01 | 2022/12/31 | ONLINE      | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2022/01/01 | 2022/12/31 | OFFLINE     | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2025/01/01 | 2026/08/01 | ONLINE      | null         |
#      | OPAU | AXASCCGDIM   | AXASC_CGDIM | OPAU-099            | OPTIQUEAUDIO     | 2025/01/01 | 2025/12/31 | OFFLINE     | null         |
#
#    Then there is 2 domaineDroits on benef 0 and warranty 0 and product 1 and refCouverture 0 and the different domaineDroits has this values
#      | code | codeGarantie | codeProduit | referenceCouverture | naturePrestation | debut      | fin        | typePeriode | finFermeture |
#      | HOSP | AXASCCGDIM   | PDT_4883    | HOSP-022-100        | HOSPITALISATION  | 2023/01/01 | 2023/12/31 | ONLINE      | null         |
#      | HOSP | AXASCCGDIM   | PDT_4883    | HOSP-022-100        | HOSPITALISATION  | 2023/01/01 | 2023/12/31 | OFFLINE     | null         |

    And I wait for 3 contract histo for this contract 01599324
    And I delete the contract histo for this contract 01599324
