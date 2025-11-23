Feature: Test event before and after renewal

  Background:
    Given I create a declarant from a file "declarantbaloo"
    And I create a contract element from a file "gt10"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |

  @smokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent
  Scenario: JIRA-5894 : scénario simple, aucun changement
    Given I create TP card parameters from file "parametrageBalooGenerique"
    When I send a contract from file "contratV6/5894" to version "V6"
    And I wait for 1 declaration
    And I remove all TP card parameters from database
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    When I send a contract from file "contratV6/5894" to version "V6"

    Then I wait for 4 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "5894/contrat1" content


  @smokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent
  Scenario: JIRA-5894 : scénario simple, aucun changement en mode anniversaire le 05/06, date de la souscription
    Given I change GMT TimeZone
    And I create TP card parameters from file "parametrageBalooGeneriqueAnniversaire"
    When I send a contract from file "contratV6/5894-anniversaire" to version "V6"
    And I wait for 1 declaration
    And I remove all TP card parameters from database
    And I create a birthday TP card parameters on "06/05" on next year from file "parametrageBalooGeneriqueAnniversaire"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations

    Then On the birthday "06-05" there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut         | fin         | finOnline |
      | GT_10    | OPAU    | startBirthday | endBirthday | null      |
      | GT_10    | HOSP    | startBirthday | endBirthday | null      |

    Then On the birthday "06-05" there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut                   | fin                   | finOnline |
      | GT_10    | OPAU    | startBirthdayOnNextYear | endBirthdayOnNextYear | null      |
      | GT_10    | HOSP    | startBirthdayOnNextYear | endBirthdayOnNextYear | null      |

    When I send a contract from file "contratV6/5894-anniversaire" to version "V6"

    Then I wait for 4 declarations

    Then On the birthday "06-05" there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut         | fin         | finOnline |
      | GT_10    | OPAU    | startBirthday | endBirthday | null      |
      | GT_10    | HOSP    | startBirthday | endBirthday | null      |

    Then On the birthday "06-05" there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                   | fin                   | finOnline |
      | GT_10    | OPAU    | startBirthdayOnNextYear | endBirthdayOnNextYear | null      |
      | GT_10    | HOSP    | startBirthdayOnNextYear | endBirthdayOnNextYear | null      |

    Then I wait for 1 contract
    Then on the birthday "06/05" the expected contract TP with indice 0 is identical to "5894/contrat2" content


  @smokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent
  Scenario: JIRA-5894 : scénario simple, aucun changement en mode anniversaire le 28/02, date de la souscription
    Given I change GMT TimeZone
    And I create TP card parameters from file "parametrageBalooGeneriqueAnniversaire"
    When I send a contract from file "contratV6/5894-anniversairefinfevrier" to version "V6"
    Then I wait for 1 declaration
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut                  | fin                 | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-02-28 | %%NEXT_YEAR%%-02-27 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-02-28 | %%NEXT_YEAR%%-02-27 | null      |

    And I remove all TP card parameters from database
    And I create a birthday TP card parameters on "02/28" on next year from file "parametrageBalooGeneriqueAnniversaire"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut               | fin              | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-02-28 | %%2_YEAR%%-02-27 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-02-28 | %%2_YEAR%%-02-27 | null      |

    When I send a contract from file "contratV6/5894-anniversairefinfevrier" to version "V6"

    Then I wait for 4 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                 | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-02-28 | %%NEXT_YEAR%%-02-27 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-02-28 | %%NEXT_YEAR%%-02-27 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut               | fin              | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-02-28 | %%2_YEAR%%-02-27 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-02-28 | %%2_YEAR%%-02-27 | null      |

    Then I wait for 1 contract
    And the expected contract TP with indice 0 is identical to "5894/contrat3" content


  @todosmokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent @BLUE-7464
    # sous periode fausse, les déclarations ne sont pas dans l'ordre sur le second event (2 triggers dont 1 de reprise de l'année suivante)
  Scenario: JIRA-5894 : scénario avec radiation
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I send a contract from file "contratV6/5894-avecRadiation" to version "V6"
    And I wait for 1 declaration

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut                  | fin                    | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-05 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-05 |

    And I remove all TP card parameters from database
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut               | fin                 | finOnline           |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-06-05 | %%NEXT_YEAR%%-06-05 |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-06-05 | %%NEXT_YEAR%%-06-05 |

    And I send a contract from file "contratV6/5894-avecRadiationChange" to version "V6"
    # y a 2 triggers générés car l'un a le flag "eventReprise"

    Then I wait for 6 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline              | periodeFermetureDebut | periodeFermetureFin
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-01-01   | %%NEXT_YEAR%%-06-05
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-01-01   | %%NEXT_YEAR%%-06-05

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        | finOnline  |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-05-05 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-05-05 |

    # on ferme tout, la date de radiation a changé
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut                  | fin                    | finOnline              | periodeFermetureDebut | periodeFermetureFin
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-01-01   | %%NEXT_YEAR%%-12-31
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-01-01   | %%NEXT_YEAR%%-12-31

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut               | fin                 | finOnline           |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | %%NEXT_YEAR%%-05-05 |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | %%NEXT_YEAR%%-05-05 |

    Then I wait for 1 contract
    And the expected contract TP with indice 0 is identical to "5894/contrat4" content

  @smokeTests
  Scenario: 7168 : Scénario d'une réouverture après radiation
    And I create TP card parameters from file "parametrageBalooGenerique"
    # Envoi d’un contrat avec date de resiliation au 30/07/2025, 1 benef radié au 29/07/2025, et 1 benef sans dateRadiation
    And I send a contract from file "contratV6/7168-avecResilRadiation" to version "V6"
    Then I wait for 2 declarations
    # benef 1 radié au 29/07/2025
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut      | fin        | finOnline  |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-29 | %%CURRENT_YEAR%%-07-29 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-29 | %%CURRENT_YEAR%%-07-29 |
    # benef 2 résilié au 30/07/2025
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut      | fin        | finOnline  |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-30 | %%CURRENT_YEAR%%-07-30 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-30 | %%CURRENT_YEAR%%-07-30 |

    # Renvoi du contrat avec date de resiliation au 30/07/2025, 1 benef avec radiation supprimé, et l'autre benef tjs sans dateRadiation
    And I send a contract from file "contratV6/7168-avecResilNoRadiation" to version "V6"
    Then I wait for 5 declarations
    # Pour benef qui a la radiation supprimé => on génère une R avec annulation des droits 2025
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut      | fin        | finOnline  | periodeFermetureDebut | periodeFermetureFin
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01            | 2025-07-29
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 | %%CURRENT_YEAR%%-01-01            | 2025-07-29
    # Pour benef qui a la radiation supprimé => on génère ensuite une V pour réouvrir les droits 2025 (mais avec une date de fin au 30/07/2025)
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut      | fin        | finOnline  |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-30 | %%CURRENT_YEAR%%-07-30 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-30 | %%CURRENT_YEAR%%-07-30 |
    # Pour benef qui a la radiation supprimé => on génère une R
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut      | fin        | finOnline  |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-30 | %%CURRENT_YEAR%%-07-30 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-30 | %%CURRENT_YEAR%%-07-30 |


  @smokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent @BLUE-7464
  # sous periode fausse
  Scenario: JIRA-5894 : scénario avec radiation que seulement après le renouvellement
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I send a contract from file "contratV6/5894" to version "V6"
    And I wait for 1 declaration

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    And I remove all TP card parameters from database
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    And I send a contract from file "contratV6/5894-avecRadiation" to version "V6"

    Then I wait for 4 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline           |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-05 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-05 |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut               | fin                 | finOnline           |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-05 |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | %%NEXT_YEAR%%-06-05 |

    Then I wait for 1 contract
    # @Bug les sous periodes sont fausses
    # TODO And the expected contract TP with indice 0 is identical to "5894/contrat5" content

  @smokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent @BLUE-7464
    # sous periode fausse (pas sûr sir celui là, à vérifier)
  Scenario: JIRA-5894 : scénario avec radiation après le renouvellement mais pendant l'année actuelle
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I send a contract from file "contratV6/5894" to version "V6"
    And I wait for 1 declaration
    And I remove all TP card parameters from database
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    And I send a contract from file "contratV6/5894-avecRadiationAnneeEnCours" to version "V6"

    Then I wait for 6 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline              |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-05 |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-05 |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut                  | fin                    | finOnline              | periodeFermetureDebut  | periodeFermetureFin
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-06 | %%CURRENT_YEAR%%-12-31
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-06 | %%CURRENT_YEAR%%-12-31

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut               | fin                 | finOnline              |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-05 |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | %%CURRENT_YEAR%%-06-05 |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut               | fin                    | finOnline              | periodeFermetureDebut  | periodeFermetureFin
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-06 | %%NEXT_YEAR%%-12-31
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-05 | %%CURRENT_YEAR%%-06-06 | %%NEXT_YEAR%%-12-31

    Then I wait for 1 contract
    # @Bug sous periode ?
    And the expected contract TP with indice 0 is identical to "5894/contrat6" content


  @smokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent
  Scenario: JIRA-5894 : scénario avec ajout assuré aujourd'hui
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I send a contract from file "contratV6/5894" to version "V6"
    And I wait for 1 declaration
    And I remove all TP card parameters from database
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations
    When I send a contract from file "contratV6/5894-ajoutAssure" to version "V6" and change placeholders
      | %%TO_CHANGE%% | %%TODAY%% |

    Then I wait for 6 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut     | fin                    | finOnline |
      | GT_10    | OPAU    | %%TODAY%% | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%TODAY%% | %%CURRENT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    Then I wait for 1 contract
    And the expected contract TP with indice 0 is identical to "5894/contrat7" content


  @smokeTests @caseConsolidation @caseRenouvellement @caseRenouvellementEvent
  Scenario: JIRA-5894 : scénario avec ajout assuré l'année suivante
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I send a contract from file "contratV6/5894" to version "V6"
    And I wait for 1 declaration
    And I remove all TP card parameters from database
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    When I renew the rights today with mode "NO_RDO"
    Then I wait for 2 declarations
    When I send a contract from file "contratV6/5894-ajoutAssure" to version "V6" and change placeholders
      | %%TO_CHANGE%% | %%NEXT_YEAR%%-01-01 |

    Then I wait for 5 declarations

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie | domaine | debut                  | fin                    | finOnline |
      | GT_10    | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie | domaine | debut               | fin                 | finOnline |
      | GT_10    | OPAU    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |
      | GT_10    | HOSP    | %%NEXT_YEAR%%-01-01 | %%NEXT_YEAR%%-12-31 | null      |

    Then I wait for 1 contract
    And the expected contract TP with indice 0 is identical to "5894/contrat8" content
