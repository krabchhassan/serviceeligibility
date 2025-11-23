Feature: Test recycle

  Background:
    Given I create a contract element from a file "gtaxa_ok"
    And I create a contract element from a file "gtaxa_bad_axo"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @recycle @release
  Scenario: Recycle the triggers
    When I send a test contract from file "contratV5/5678_1"
    When I send a test contract from file "contratV5/5678_2"
    # Vérifie si les deux contrats en erreurs n'ont pas créé de déclaration
    And I wait for 0 declaration

    # pour le renouvellement
    And I create a service prestation from a file "5678_SP_for_renouv"
    And I renew the rights on "2024-01-01" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "99992004003" and amc "0000401166" to be "ProcessedWithErrors"
    # vérifie si les sasContrat du renouvellement contiennent bien le nombre de triggerBenef en erreurs
    Then The SasContrat for the current Trigger contains 11 triggerBenefs

    # test recyclage sans rien changer -> toujours en erreur
    And I wait for 1 declaration
    Then I get one more trigger with contract number "99992004002" and amc "0000401166" and indice "0" for benef
    When I recycle the trigger
    # verifie si toujours en erreur
    Then I wait for the last trigger with contract number "99992004002" and amc "0000401166" to be "ProcessedWithErrors"
    And I wait for 1 declaration
    # vérifie si les sasContrat de l'event contrat 1 contiennent bien le nombre de triggerBenef en erreurs
    Then The SasContrat for the current Trigger contains 10 triggerBenefs

    # changement de gt avec un pw correct

    And I create a contract element from a file "gtaxa_ok"
    And I create a contract element from a file "gtaxa"

    # recyclage de de l'event du contrat 1
    Then I get one more trigger with contract number "99992004002" and amc "0000401166" and indice "0" for benef
    When I recycle the trigger
     # vérifie contrat 1 ok
    Then I wait for the last trigger with contract number "99992004002" and amc "0000401166" to be "Processed"
    And I wait for 21 declarations
    # vérifie si les sasContrat de l'event du contrat 1 sont bien supprime
    Then The SasContrat for the current Trigger contains 0 triggerBenefs

    # recyclage du renouvellement pour faire passer le contrat 2
    Then I get the first renewal trigger with contract number "01599324" and amc "0000401166"
    When I recycle the trigger
    # vérifie contrat 2 ok
    Then I wait for the last renewal trigger with contract number "01599324" and amc "0000401166" to be "Processed"
    And I wait for 23 declarations
    # vérifie si les sasContrat du renouvellement contiennent bien le nombre de triggerBenef en erreurs
    Then The SasContrat for the current Trigger contains 0 triggerBenefs
    And I wait for 3 contracts

  @smokeTests @recycle @resetRecycling
  Scenario: Recycling reset after failed recycle
    When I send a test contract from file "contratV5/5871"
    And I renew the rights on "2024-01-01" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "99992004002" and amc "0000401166" to be "ProcessedWithErrors"
    Then The SasContrat for the current Trigger contains 1 triggerBenefs
    When I recycle the trigger
    Then I wait for the SasContrat of the current Trigger not to be recycling
    And I wait for 1 declaration
    And I wait for 1 contract

  @smokeTests @recycle @resetRecycling
  Scenario: Recycling a resiliated contract
    When I send a test contract from file "contratV5/6476_base"
    Then I wait for 1 declaration
    Then I send a test contract from file "contratV5/6476_recycling"
    Then I wait for the first trigger with contract number "01599324" and amc "0000401166" to be "ProcessedWithErrors"
    And I create a contract element from a file "gtaxa_ok"
    And I create a contract element from a file "gtaxa"
    Then I recycle the trigger
    Then I wait for 4 declarations
    Then The declaration number 0 has codeEtat "V"
    And The declaration number 1 has codeEtat "R"
    And The declaration number 2 has codeEtat "V"
    And The declaration number 3 has codeEtat "R"
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut                  | fin                    | finOnline              | periodeFermetureDebut  | periodeFermetureFin    |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-10-31 | %%CURRENT_YEAR%%-10-31 | %%CURRENT_YEAR%%-11-01 | %%CURRENT_YEAR%%-12-31 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-10-31 | %%CURRENT_YEAR%%-10-31 | %%CURRENT_YEAR%%-11-01 | %%CURRENT_YEAR%%-12-31 |
    And I wait for 1 contract

  @smokeTests @recycle @resetRecycling
  Scenario: Recycling a removed contract (sans effet)
    When I create a service prestation from a file "6620_contrat1"
    And I create a service prestation from a file "6620_contrat2"
    And I create a contract element from a file "gt6620"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    And I renew the rights today with mode "RDO"
    Then I wait for the first renewal trigger with contract number "02939322" and amc "0000401166" to be "ProcessedWithErrors"
    Then I wait for 0 declaration
    When I create manual TP card parameters from file "parametrageTP6620-2025"
    And I renew the rights today with mode "RDO"
    Then I wait for the first renewal trigger with contract number "02939322" and amc "0000401166" to be "ProcessedWithErrors"
    Then I wait for 0 declaration
    When I delete a contract "02939322" for amc "0000401166"
    And I recycle the trigger
    Then I wait for the SasContrat of the current Trigger to be recycling
