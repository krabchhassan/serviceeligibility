Feature: Test recycle

  @smokeTests @recycle @abandon
  Scenario: Abandon the triggers
    And I create a contract element from a file "gtaxa_ok"
    And I create a contract element from a file "gtaxa_bad_axo"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/5678_1"
    When I send a test contract from file "contratV5/5678_2"
    # Vérifie si les deux contrats en erreurs n'ont pas créé de déclaration
    And I wait "2" seconds in order to consume the data
    And I wait for 0 declaration

    # pour le renouvellement
    And I create a service prestation from a file "5678_SP_for_renouv"
    When I renew the rights on "2024-01-01" with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "99992004003" and amc "0000401166" to be "ProcessedWithErrors"
    # vérifie si les sasContrat du renouvellement contiennent bien le nombre de triggerBenef en erreurs
    Then The SasContrat for the current Trigger contains 11 triggerBenefs
    # 10 benefs pour le contrat 99992004002 et 1 benef pour le contrat 01599324
    # test recyclage sans rien changer -> toujours en erreur
    And I wait for 1 declaration
    Then I get one more trigger with contract number "99992004002" and amc "0000401166" and indice "0" for benef
    When I recycle the trigger
    # verifie si toujours en erreur
    Then I wait for the last trigger with contract number "99992004002" and amc "0000401166" to be "ProcessedWithErrors"
    And I wait for 1 declaration
    # vérifie si les sasContrat du contrat 99992004002 n'ont pas évolué (les memes 10 triggerBenef sont toujours en erreur) sur le trigger event du 1er contrat
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
    # vérifie si sasContrat ne contient plus ces triggerBenefs passants
    Then The SasContrat for the current Trigger contains 0 triggerBenefs

    # abandon du renouvellement contrat 2
    Then I get the first renewal trigger with contract number "01599324" and amc "0000401166"
    # vérifie si sasContrat contient toujours les triggerBenef en erreur du contrat 2
    Then The SasContrat for the current Trigger contains 1 triggerBenefs
    When I abandon the trigger
    Then I wait for the last renewal trigger with contract number "01599324" and amc "0000401166" to be "Abandonned"
    # vérifie si sasContrat supprime pour le trigger de renouvellement
    Then The SasContrat for the current Trigger contains 0 triggerBenefs

    # verifie si sasContrat du contrat 2 pour le trigger d event existe toujours
    Then I get one more trigger with contract number "01599324" and amc "0000401166" and indice "0" for benef
    Then The SasContrat for the current Trigger contains 1 triggerBenefs
    When I recycle the trigger
    Then I wait for the last trigger with contract number "01599324" and amc "0000401166" to be "Processed"
    And I wait for 22 declarations
    Then The SasContrat for the current Trigger contains 0 triggerBenefs
    And I wait for 3 contracts




