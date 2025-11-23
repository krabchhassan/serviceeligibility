Feature: Managing parameters in V2 for the several types

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for conventionnement
    Given I try to delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    When I create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter has values
      | code    | TSTB           |
      | libelle | Test Team BLUE |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for conventionnement
    Given I try to delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    And I create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I update a parameter for type "conventionnement" in version "V2" with parameters
      | code    | TSTB                   |
      | libelle | Test Team BLUE updated |
    And I get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter has values
      | code    | TSTB                   |
      | libelle | Test Team BLUE updated |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for conventionnement
    Given I try to delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    And I create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for conventionnement
    Given I try to delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    And I create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for conventionnement
    Given I try to delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    When  I try to update a parameter for type "conventionnement" in version "V2" with parameters
      | code    | TSTB                   |
      | libelle | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2 @parametresV2test
  Scenario: Managing delete error for conventionnement
    Given I try to delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    When  I try to delete the parameter for type "conventionnement" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for formules
    Given I try to delete the parameter for type "formules" with code "TSTB" in version "V2"
    When I create a parameter for type "formules" in version "V2" with parameters
      | code    | TSTB           |
      | param1  | true           |
      | param2  | false          |
      | param3  | true           |
      | param4  | true           |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | true           |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "formules" in version "V2"
    Then the parameter has values
      | code    | TSTB           |
      | param1  | true           |
      | param2  | false          |
      | param3  | true           |
      | param4  | true           |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | true           |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for formules
    Given I try to delete the parameter for type "formules" with code "TSTB" in version "V2"
    And I create a parameter for type "formules" in version "V2" with parameters
      | code    | TSTB           |
      | param1  | true           |
      | param2  | false          |
      | param3  | true           |
      | param4  | true           |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | true           |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |
    When I update a parameter for type "formules" in version "V2" with parameters
      | code    | TSTB           |
      | param1  | false          |
      | param2  | true           |
      | param3  | true           |
      | param4  | false          |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | false          |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "formules" in version "V2"
    Then the parameter has values
      | code    | TSTB           |
      | param1  | false          |
      | param2  | true           |
      | param3  | true           |
      | param4  | false          |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | false          |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting a parameter for formules
    Given I try to delete the parameter for type "formules" with code "TSTB" in version "V2"
    And I create a parameter for type "formules" in version "V2" with parameters
      | code    | TSTB           |
      | param1  | true           |
      | param2  | false          |
      | param3  | true           |
      | param4  | true           |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | true           |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |
    When I delete the parameter for type "formules" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for formules
    Given I try to delete the parameter for type "formules" with code "TSTB" in version "V2"
    And I create a parameter for type "formules" in version "V2" with parameters
      | code    | TSTB           |
      | param1  | true           |
      | param2  | false          |
      | param3  | true           |
      | param4  | true           |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | true           |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |
    When I try to create a parameter for type "formules" in version "V2" with parameters
      | code    | TSTB           |
      | param1  | true           |
      | param2  | false          |
      | param3  | true           |
      | param4  | true           |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | true           |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for formules
    Given I try to delete the parameter for type "formules" with code "TSTB" in version "V2"
    When I try to update a parameter for type "formules" in version "V2" with parameters
      | code    | TSTB           |
      | param1  | false          |
      | param2  | true           |
      | param3  | true           |
      | param4  | false          |
      | param5  | false          |
      | param6  | false          |
      | param7  | true           |
      | param8  | false          |
      | param9  | false          |
      | param10 | false          |
      | libelle | Test Team BLUE |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing delete error for formules
    Given I try to delete the parameter for type "formules" with code "TSTB" in version "V2"
    When I try to delete the parameter for type "formules" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"


  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for domaine_IS
    Given I try to delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    When I create a parameter for type "domaine_IS" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "domaine_IS" in version "V2"
    Then the parameter has values
      | code              | TSTB           |
      | libelle           | Test Team BLUE |
      | transcodification | BLUE           |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for domaine_IS
    Given I try to delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine_IS" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I update a parameter for type "domaine_IS" in version "V2" with parameters
      | code              | TSTB                   |
      | transcodification | BLUE                   |
      | libelle           | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "domaine_IS" in version "V2"
    Then the parameter has values
      | code              | TSTB                   |
      | libelle           | Test Team BLUE modifié |
      | transcodification | BLUE                   |


  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    When I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | priorite          | 1              |
      | libelle           | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "domaine" in version "V2"
    Then the parameter has values
      | code              | TSTB           |
      | libelle           | Test Team BLUE |
      | priorite          | 1              |
      | transcodification | BLUE           |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | priorite          | 1              |
      | libelle           | Test Team BLUE |
    When I update a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB                   |
      | transcodification | BLUE                   |
      | priorite          | 2                      |
      | libelle           | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "domaine" in version "V2"
    Then the parameter has values
      | code              | TSTB                   |
      | libelle           | Test Team BLUE modifié |
      | priorite          | 2                      |
      | transcodification | BLUE                   |


  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for domaine_IS
    Given I try to delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine_IS" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for domaine_IS
    Given I try to delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine_IS" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I try to create a parameter for type "domaine_IS" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for domaine_IS
    Given I try to delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    When I try to update a parameter for type "domaine_IS" in version "V2" with parameters
      | code              | TSTB                   |
      | transcodification | BLUE                   |
      | libelle           | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for domaine_IS
    Given I try to delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    When I try to delete the parameter for type "domaine_IS" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"


  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for domaine_SP
    Given I try to delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    When I create a parameter for type "domaine_SP" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "domaine_SP" in version "V2"
    Then the parameter has values
      | code              | TSTB           |
      | libelle           | Test Team BLUE |
      | transcodification | BLUE           |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for domaine_SP
    Given I try to delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine_SP" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I update a parameter for type "domaine_SP" in version "V2" with parameters
      | code              | TSTB                   |
      | transcodification | BLUE                   |
      | libelle           | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "domaine_SP" in version "V2"
    Then the parameter has values
      | code              | TSTB                   |
      | libelle           | Test Team BLUE modifié |
      | transcodification | BLUE                   |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for domaine_SP
    Given I try to delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine_SP" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for domaine_SP
    Given I try to delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine_SP" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I try to create a parameter for type "domaine_SP" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for domaine_SP
    Given I try to delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    When I try to update a parameter for type "domaine_SP" in version "V2" with parameters
      | code    | TSTB                   |
      | libelle | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing delete error for domaine_SP
    Given I try to delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    When I try to delete the parameter for type "domaine_SP" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Creating a parameter for motifEvenement
    Given I try to delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    When I create a parameter for type "motifEvenement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "motifEvenement" in version "V2"
    Then the parameter has values
      | code    | TSTB           |
      | libelle | Test Team BLUE |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for motifEvenement
    Given I try to delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    And I create a parameter for type "motifEvenement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I update a parameter for type "motifEvenement" in version "V2" with parameters
      | code    | TSTB                   |
      | libelle | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "motifEvenement" in version "V2"
    Then the parameter has values
      | code    | TSTB                   |
      | libelle | Test Team BLUE modifié |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for motifEvenement
    Given I try to delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    And I create a parameter for type "motifEvenement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for motifEvenement
    Given I try to delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    And I create a parameter for type "motifEvenement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I try to create a parameter for type "motifEvenement" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for motifEvenement
    Given I try to delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    When I try to update a parameter for type "motifEvenement" in version "V2" with parameters
      | code    | TSTB                   |
      | libelle | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing delete error for motifEvenement
    Given I try to delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    When I try to delete the parameter for type "motifEvenement" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    When I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "domaine" in version "V2"
    Then the parameter has values
      | code              | TSTB           |
      | libelle           | Test Team BLUE |
      | transcodification | BLUE           |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I update a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB                   |
      | transcodification | BLUE                   |
      | libelle           | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "domaine" in version "V2"
    Then the parameter has values
      | code              | TSTB                   |
      | libelle           | Test Team BLUE modifié |
      | transcodification | BLUE                   |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    When I try to create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    When I try to update a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB                   |
      | transcodification | BLUE                   |
      | libelle           | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing delete error for domaine
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    When I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for processus
    Given I try to delete the parameter for type "processus" with code "TSTB" in version "V2"
    When I create a parameter for type "processus" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "processus" in version "V2"
    Then the parameter has values
      | code    | TSTB           |
      | libelle | Test Team BLUE |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for typeFichiers
    Given I try to delete the parameter for type "typeFichiers" with code "TSTB" in version "V2"
    And I create a parameter for type "typeFichiers" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I update a parameter for type "typeFichiers" in version "V2" with parameters
      | code    | TSTB                   |
      | libelle | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "typeFichiers" in version "V2"
    Then the parameter has values
      | code    | TSTB                   |
      | libelle | Test Team BLUE modifié |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for typeFichiers
    Given I try to delete the parameter for type "typeFichiers" with code "TSTB" in version "V2"
    And I create a parameter for type "typeFichiers" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I delete the parameter for type "typeFichiers" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "typeFichiers" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for typeFichiers
    Given I try to delete the parameter for type "typeFichiers" with code "TSTB" in version "V2"
    And I create a parameter for type "typeFichiers" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    When I try to create a parameter for type "typeFichiers" in version "V2" with parameters
      | code    | TSTB           |
      | libelle | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for typeFichiers
    Given I try to delete the parameter for type "typeFichiers" with code "TSTB" in version "V2"
    When I try to update a parameter for type "typeFichiers" in version "V2" with parameters
      | code    | TSTB                   |
      | libelle | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing delete error for typeFichiers
    Given I try to delete the parameter for type "typeFichiers" with code "TSTB" in version "V2"
    When I try to delete the parameter for type "typeFichiers" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for servicesMetiers
    Given I try to delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    When I create a parameter for type "servicesMetiers" in version "V2" with parameters
      | code    | TSTB           |
      | ordre   | 4              |
      | icone   | BLUE           |
      | libelle | Test Team BLUE |
    And I get the parameter with code "TSTB", for type "servicesMetiers" in version "V2"
    Then the parameter has values
      | code    | TSTB           |
      | libelle | Test Team BLUE |
      | ordre   | 4              |
      | icone   | BLUE           |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for servicesMetiers
    Given I try to delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    And I create a parameter for type "servicesMetiers" in version "V2" with parameters
      | code    | TSTB           |
      | ordre   | 4              |
      | icone   | BLUE           |
      | libelle | Test Team BLUE |
    When I update a parameter for type "servicesMetiers" in version "V2" with parameters
      | code    | TSTB                   |
      | ordre   | 4                      |
      | icone   | BLUE                   |
      | libelle | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "servicesMetiers" in version "V2"
    Then the parameter has values
      | code    | TSTB                   |
      | libelle | Test Team BLUE modifié |
      | ordre   | 4                      |
      | icone   | BLUE                   |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for servicesMetiers
    Given I try to delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    And I create a parameter for type "servicesMetiers" in version "V2" with parameters
      | code    | TSTB           |
      | ordre   | 4              |
      | icone   | BLUE           |
      | libelle | Test Team BLUE |
    When I delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for servicesMetiers
    Given I try to delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    And I create a parameter for type "servicesMetiers" in version "V2" with parameters
      | code    | TSTB           |
      | ordre   | 4              |
      | icone   | BLUE           |
      | libelle | Test Team BLUE |
    When I try to create a parameter for type "servicesMetiers" in version "V2" with parameters
      | code    | TSTB           |
      | ordre   | 4              |
      | icone   | BLUE           |
      | libelle | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for servicesMetiers
    Given I try to delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    When I try to update a parameter for type "servicesMetiers" in version "V2" with parameters
      | code    | TSTB                   |
      | ordre   | 4                      |
      | icone   | BLUE                   |
      | libelle | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing delete error for servicesMetiers
    Given I try to delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    When I try to delete the parameter for type "servicesMetiers" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Inserting new parameter for prestations
    Given I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB2" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB2            |
      | transcodification | BLUE             |
      | libelle           | Test Team BLUE 2 |
    And I create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB           |
      | codeDomaine1  | TSTB           |
      | codeDomaine2  | TSTB2          |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    When I get the parameter with code "TSTB", for type "prestations" in version "V2"
    Then the parameter has values
      | code         | TSTB           |
      | libelle      | Test Team BLUE |
      | codeDomaine1 | TSTB           |
      | codeDomaine2 | TSTB2          |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Updating a parameter for prestations
    Given I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB2" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB2            |
      | transcodification | BLUE             |
      | libelle           | Test Team BLUE 2 |
    And I create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB           |
      | codeDomaine1  | TSTB           |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    When I update a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB                   |
      | codeDomaine1  | TSTB                   |
      | codeDomaine2  | TSTB2                  |
      | NoMoreDomaine |                        |
      | libelle       | Test Team BLUE modifié |
    And I get the parameter with code "TSTB", for type "prestations" in version "V2"
    Then the parameter has values
      | code         | TSTB                   |
      | libelle      | Test Team BLUE modifié |
      | codeDomaine1 | TSTB                   |
      | codeDomaine2 | TSTB2                  |

  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Deleting parameter for prestations
    Given I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB2" in version "V2"
    And I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB2            |
      | transcodification | BLUE             |
      | libelle           | Test Team BLUE 2 |
    And I create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB           |
      | codeDomaine1  | TSTB           |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    When I delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I try to get the parameter with code "TSTB", for type "conventionnement" in version "V2"
    Then the parameter doesn't exists

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing create error for prestations
    Given I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB2" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB           |
      | codeDomaine1  | TSTB           |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    When I try to create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB           |
      | codeDomaine1  | TSTB           |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    Then an error "400" is returned with message "Le paramètre TSTB existe déjà!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing update error for prestations
    Given I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB2" in version "V2"
    When I try to update a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB                   |
      | codeDomaine1  | TSTB                   |
      | codeDomaine2  | TSTB2                  |
      | NoMoreDomaine |                        |
      | libelle       | Test Team BLUE updated |
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"

  @todosmokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Managing delete error for prestations
    Given I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB2" in version "V2"
    When I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    Then an error "404" is returned with message "Le paramètre TSTB n'existe pas!"


  @smokeTests @smokeTestsWithoutKafka @parametresV2
  Scenario: Getting prestations for a domain
    Given I try to delete the parameter for type "prestations" with code "TSTB" in version "V2"
    And I try to delete the parameter for type "prestations" with code "TSTB2" in version "V2"
    And I try to delete the parameter for type "prestations" with code "TSTB3" in version "V2"
    And I try to delete the parameter for type "domaine" with code "TSTB" in version "V2"
    And I create a parameter for type "domaine" in version "V2" with parameters
      | code              | TSTB           |
      | transcodification | BLUE           |
      | libelle           | Test Team BLUE |
    And I create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB           |
      | codeDomaine1  | TSTB           |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    And I create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB2          |
      | codeDomaine1  | TSTB           |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    And I create a parameter for type "prestations" in version "V2" with parameters
      | code          | TSTB3          |
      | codeDomaine1  | TSTB           |
      | NoMoreDomaine |                |
      | libelle       | Test Team BLUE |
    When I get the prestation's list for the domain "TSTB"
    Then the list contains "3" entries
