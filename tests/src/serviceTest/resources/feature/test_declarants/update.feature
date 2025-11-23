Feature: Update declarant

  Background:
    Given I import a complete file for parametrage

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Update declarant's validiteDomainesDroits
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I update the declarant with the file "correct_update"
    Then the response has an HTTP code "200"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with a codeDomaine missing
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "update_missing_codeDomaine"
    Then an error "400" is returned with message "Le codeDomaine est absent"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with a duree missing
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "update_missing_duree"
    Then an error "400" is returned with message "La durée doit exister et être positive"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with a unite missing
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "update_missing_unite"
    Then an error "400" is returned with message "L'unite doit exister et ne peut être que 'Jours' ou 'Mois'"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with an incorrect unite
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "update_incorrect_unite"
    Then an error "400" is returned with message "L'unite doit exister et ne peut être que 'Jours' ou 'Mois'"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with Jours as unite and positionnerFinDeMois at true
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "update_incorrect_positionnerFinDeMois"
    Then an error "400" is returned with message "Le booléen positionnerFinDeMois ne peut être 'true' que si unite == 'Mois'"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with more than 365 days
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "update_too_much_days"
    Then an error "400" is returned with message "La durée ne peut pas excéder 365 jours ou 12 mois"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with more than 12 months
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "update_too_much_months"
    Then an error "400" is returned with message "La durée ne peut pas excéder 365 jours ou 12 mois"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with duplicate ConventionTP
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "../declarant/declarantWithDuplicateConventionsTP"
    Then an error "400" is returned with message "Erreur: Convention TP non unique"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with duplicate CodeRenvoi
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "../declarant/declarantWithDuplicateCodesRenvoi"
    Then an error "400" is returned with message "Erreur: Code renvoi non unique"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with duplicate RegroupementDomainesTP
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "../declarant/declarantWithDuplicateRegroupements"
    Then an error "400" is returned with message "Erreur: Regroupement des domaines TP non unique"

  @todosmokeTests @smokeTestsWithoutKafka @declarant @update
  Scenario: Try to update with duplicate FondCarteTP
    When I get a declarant for UI with ID "0000733931"
    Then the declarant for UI with id "0000733931" exists
    When I try to update the declarant with the file "../declarant/declarantWithDuplicateFondsCarte"
    Then an error "400" is returned with message "Erreur: Fond de carte non unique"
