Feature: Consume a message from differents sources and verify value is contained in db in the correct format
  linked to BLUE-5587

  Background:
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo_5458"
    And I create TP card parameters from file "parametrageTPBaloo_5458"
    And I send a contract from file "contratV5/5516_one_benef" to version "V6"
    And I wait "3" seconds in order to consume the data


  @todosmokeTests @smokeTestsWithoutKafka @sources
  Scenario: I send declaration from Service Prestation reception case 1
    When I create a declaration from a file "5587/case1"
    And I get all benef from the database
    Then The benef is identical to "5587/retour_case1" content


  @todosmokeTests @smokeTestsWithoutKafka @sources @benefworker
  Scenario: I send declaration from TDB/TFD reception case 2
    When I create a declaration from a file "5587/case2"
    And I get all benef from the database
    Then The benef is identical to "5587/retour_case2" content


  @todosmokeTests @smokeTestsWithoutKafka @sources @benefworker
  Scenario: I send benefai from Prestij case 3
    When I send a message from source PrestIJ file 5587/case3 to the kafka topic benefai
    And I get all benef from the database
    Then The benef is identical to "5587/retour_case3" content


  @todosmokeTests @smokeTestsWithoutKafka @sources @benefworker
  Scenario: I send benefai from Service Prestation case 4
    When I send a message from source ServicePrestation file 5587/case4 to the kafka topic benefai
    And I get all benef from the database
    Then The benef is identical to "5587/retour_case4" content
