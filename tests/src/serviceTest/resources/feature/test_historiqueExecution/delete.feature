Feature: Delete HistoriqueExecution

  Background:
    Given I import a complete file for parametrage

  @@todosmokeTests @smokeTestsWithoutKafka @historiqueExecution
  Scenario: Delete History Execution by Batch
    When I delete history for batch "610"
    Then the next history for the batch "610" has values
      | id                   | 5e3927873e896949177d5d03     |
      | batch                | 610                          |
      | codeService          | DCLBEN                       |
      | idDeclarant          | 0000401026                   |
      | typeConventionnement | 000                          |
      | dateExecution        | 2019-09-18T08:12:14.309+0000 |

  @@todosmokeTests @smokeTestsWithoutKafka @historiqueExecution @he01
  Scenario: Delete History Execution by Batch And AMC
    When I delete history for batch "610" and AMC "0000401026"
    Then the next history for the batch "610" and AMC "0000401026" has values
      | id                   | 5e39199c3e896949177d5c25     |
      | batch                | 610                          |
      | codeService          | ALMV3                        |
      | idDeclarant          | 0000401026                   |
      | typeConventionnement | 000                          |
      | dateExecution        | 2019-09-15T08:12:14.309+0000 |
    Then the next history for the batch "610" has values
      | id                   | 5e39199c3e896949177d5c2f     |
      | batch                | 610                          |
      | codeService          | ALMV3                        |
      | idDeclarant          | 0970401026                   |
      | typeConventionnement | 000                          |
      | dateExecution        | 2019-09-15T18:12:14.309+0000 |

  @@todosmokeTests @smokeTestsWithoutKafka @historiqueExecution
  Scenario: Delete History Execution for an inexisting Batch
    When I delete an inexisting history for batch "123"
    Then the response has an HTTP code "404"


  @@todosmokeTests @smokeTestsWithoutKafka @historiqueExecution
  Scenario: Delete History Execution for a Batch and an inexisting AMC
    When I delete an inexisting history for batch "610" and AMC "999888777"
    Then the response has an HTTP code "404"
