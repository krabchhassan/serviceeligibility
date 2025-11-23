Feature: Insert triggers
#  | Nom      | AMC  | status              | emitters      | dateDebut  | dateFin    | numeroContrat | nir  |
#  | Trigger1 | AMC1 | StandBy             | Request       | 2021-02-06 | 2021-02-07 | null          | null |
#  | Trigger2 | AMC2 | Processed           | Event         | 2021-03-06 | 2021-03-07 | null          | null |
#  | Trigger3 | AMC3 | ProcessedWithErrors | Renewal       | 2021-04-06 | 2021-04-07 | null          | null |
# lorsqu'il y a plusieurs résultats, ils sont triés par date de création décroissante
  Background:
    Given I remove all triggers

  @noSmokeTests @triggers
  Scenario Outline: Insert a trigger
    Given I import a complete file for parametrage
    And I insert a new trigger from "trigger1"
    And I insert a new trigger from "trigger2"
    And I insert a new trigger from "trigger3"
    And I insert a new trigger beneficiary from "triggerBeneficiary1"
    And I insert a new trigger beneficiary from "triggerBeneficiary2"
    When I search a trigger with values <amcs>, <status>, <emitters>, <dateDebut>, <dateFin>, <numeroContrat> and <nir>
    Then The trigger's search response is identical to "<response>"
    Examples:
      | amcs      | status    | emitters | dateDebut  | dateFin    | numeroContrat | nir  | response                   |
      | AMC1      |           |          |            |            |               |      | search_trigger1_response   |
      |           | Processed |          |            |            |               |      | search_trigger2_response   |
      |           |           | Renewal  |            |            |               |      | search_trigger3_response   |
      |           |           |          | 2021-04-01 |            |               |      | search_trigger3_response   |
      |           |           |          |            | 2021-02-08 |               |      | search_trigger1_response   |
      |           |           |          |            |            | Contrat1      |      | search_trigger2_response   |
      |           |           |          |            |            |               | NIR2 | search_trigger3_response   |
      |           |           |          |            |            |               |      | search_trigger123_response |
      |           |           |          | 2021-02-01 | 2021-03-08 |               |      | search_trigger12_response  |
      | AMC1,AMC2 |           |          |            |            |               |      | search_trigger12_response  |
