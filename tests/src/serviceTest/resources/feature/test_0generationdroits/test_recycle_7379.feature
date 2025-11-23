Feature: Test recycle 7379

  Background:
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"

  @smokeTests @recycle
  Scenario: Recycling a contract and delete sasContract
    When I send a test contract from file "contratV5/7379_1"
    # Vérifie si le contrat n'a pas créé de déclaration car parametrage conventionnement manquant
    And I wait "2" seconds in order to consume the data
    And I wait for 0 declaration
    Then I get one more trigger with contract number "BLUE7379_2" and amc "0000401166" and indice "0" for benef
    # vérifie si les sasContrat existe
    Then The SasContrat for the current Trigger contains 1 triggerBenefs
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I recycle the trigger
    Then I wait for the last trigger with contract number "BLUE7379_2" and amc "0000401166" to be "Processed"
    And I wait for 1 declaration
    # vérifie si les sasContrat a été supprimé
    Then No sasContrat found for the contract "BLUE7379_2"

  @smokeTests @recycle
  Scenario: Recycling contract after invalid GT
    # Envoi du contrat avec GT ouvert
    And I send a test contract from file "contratV5/7379_GT_ouvert"
    Then I get one more trigger with contract number "C7379-01-MBA" and amc "0000401166" and indice "0" for benef
    # Vérifie si le contrat n'a pas créé de déclaration car IS introuvable dans le paramétrage de convention de la BDDS
    And I wait "2" seconds in order to consume the data
    And I wait for 0 declaration

    # Envoi du contrat avec GT inversée
    And I send a test contract from file "contratV5/7379_GT_fermee"
    Then I get one more trigger with contract number "C7379-01-MBA" and amc "0000401166" and indice "1" for benef
    # Vérifie si le contrat n'a pas créé de déclaration car sasContrat trouvé pour ce contrat
    And I wait "2" seconds in order to consume the data
    And I wait for 0 declaration

    # Envoi du contrat avec GT inversée
    And I send a test contract from file "contratV5/7379_GT_fermee"
    Then I get one more trigger with contract number "C7379-01-MBA" and amc "0000401166" and indice "0" for benef
    # Vérifie si le contrat n'a pas créé de déclaration car sasContrat trouvé pour ce contrat
    And I wait "2" seconds in order to consume the data
    And I wait for 0 declaration

    # Rnvoi le contrat avec GT ouvert
    And I send a test contract from file "contratV5/7379_GT_ouvert"
    # Ajout de IS dans le paramétrage de convention
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

    # RECYCLAGE => triggers Processed
    When I recycle the trigger
    Then I wait for the first trigger with contract number "C7379-01-MBA" and amc "0000401166" to be "Processed"
    Then I wait for the last trigger with contract number "C7379-01-MBA" and amc "0000401166" to be "Processed"