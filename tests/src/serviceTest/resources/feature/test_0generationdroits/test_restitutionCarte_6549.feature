Feature:  Alimentation de la collection restitutionCarte lors de la génération des droits

  Background:
    And I create a contract element from a file "gt_5660"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
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

  @smokeTests @restitutionCarte @6549
  Scenario: Envoi d'un contrat puis envoi du contrat avec une dateRestitutionCarte => alimentation de la collection restitutionCarte
    When I send a test contract from file "6549-Contrat_noRestitutionCarte"
    Then I wait for 1 declaration
    Then The declaration number 0 has codeEtat "V"
    When I send a test contract from file "6549-Contrat_restitutionCarte1"
    Then I wait for 4 declaration
    Then The declaration number 3 has codeEtat "R"
    Then The declaration with indice 3 has dateRestitution "%%CURRENT_YEAR%%/11/26"
    And I wait for 1 restitutionCarte with idDeclarant "0000401166", numeroPersonne "59202601011101", numeroContrat "592026010111" and numeroAdherent "592026010111"
    And The restitutionCarte has these values
      | idDeclarant          | 0000401166             |
      | numeroPersonne       | 59202601011101         |
      | numeroAdherent       | 592026010111           |
      | numeroContrat        | 592026010111           |
      | dateNaissance        | 19791006               |
      | rangNaissance        | 1                      |
      | nirOd1               | 1592026010111          |
      | cleNirOd1            | 49                     |
      | dateRestitutionCarte | %%CURRENT_YEAR%%/11/26 |
      | userCreation         | Event                  |
      | userModification     | null                   |
    Then I send a test contract from file "6549-Contrat_restitutionCarte2"
    Then I wait for 7 declarations
    Then The declaration number 6 has codeEtat "R"
    Then The declaration with indice 6 has dateRestitution "%%CURRENT_YEAR%%/11/20"
    And I wait for 3 restitutionCarte with idDeclarant "0000401166", numeroPersonne "59202601011101", numeroContrat "592026010111" and numeroAdherent "592026010111"
    And The restitutionCarte has these values
      | idDeclarant          | 0000401166             |
      | numeroPersonne       | 59202601011101         |
      | numeroAdherent       | 592026010111           |
      | numeroContrat        | 592026010111           |
      | dateNaissance        | 19791006               |
      | rangNaissance        | 1                      |
      | nirOd1               | 1592026010111          |
      | cleNirOd1            | 49                     |
      | dateRestitutionCarte | %%CURRENT_YEAR%%/11/20 |
      | userCreation         | Event                  |
