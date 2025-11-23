Feature: Test contract V5 with rights with/without corresponding entry in PW

  @smokeTests @caseNoPw
  Scenario: I send a contract with one right without corresponding entry in PW and a correct one
    And I create a contract element from a file "gtaxa"
    And I create a contract element from a file "gtaxa_not_in_pw"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/5019-01"
    Then I wait for 0 declarations
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status    | ProcessedWithErrors |
      | amc       | 0000401166          |
      | nbBenef   | 1                   |
      | nbBenefKO | 1                   |
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut           | Error                                                                                                        |
      | derniereAnomalie | Pas de version d'offre disponible pour la société émettrice CONTRAT_GROUPE_AXA et le code produit INEXISTANT |
      | nir              | 2730781111112                                                                                                |
      | numeroPersonne   | 2124744                                                                                                      |

  @smokeTests @caseNoPw
  Scenario: I send a contract with one right without TP OFFLINE PW and a correct one
    And I create a contract element from a file "gtaxa"
    And I create a contract element from a file "gtaxa_no_tpoffline"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/5019-02"
    Then I wait for 0 declarations
    When I get triggers with contract number "01599324" and amc "0000401166"
    Then the trigger of indice "0" has this values
      | status    | ProcessedWithErrors |
      | amc       | 0000401166          |
      | nbBenef   | 1                   |
      | nbBenefKO | 1                   |
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut           | Error                                                                                                                                                                        |
      | derniereAnomalie | Une ou plusieurs erreurs lors de l'appel au Product Workshop : Aucune offre avec le TP Offline paramétré disponible pour la société émettrice BALOO et le code produit 18875 |
      | nir              | 2730781111112                                                                                                                                                                |
      | numeroPersonne   | 2124744                                                                                                                                                                      |
