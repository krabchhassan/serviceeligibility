Feature: Import an Excel file and transform it in JSON

  @todosmokeTests @smokeTestsWithoutKafka
  Scenario: Import an Excel file
    When I import a file "Liste_Codes_NAF.xlsx" with Excel format
    Then the file is correctly imported
    And the imported file has "2" columns and "10" lines
    And the imported file has "1" sheet
    And sheet "1" has name "Code NAF"
    And content of imported file is :
      | Code   | Libelle                                          |
      | 86.10Z | Activités hospitalières                          |
      | 86.21Z | Activité des médecins généralistes               |
      | 86.22A | Activités de radiodiagnostic et de radiothérapie |
      | 86.22B | Activités chirurgicales                          |
      | 86.22C | Autres activités des médecins spécialistes       |
      | 86.23Z | Pratique dentaire                                |
      | 86.90A | Ambulances                                       |
      | 86.90B | Laboratoires d'analyses médicales                |
      | 86.90C | Centres de collecte et banques d'organes         |
      | 86.90D | Activités des infirmiers et des sages-femmes     |

  @todosmokeTests @smokeTestsWithoutKafka
  Scenario: Import a file with an invalid format
    When I import a file with invalid format
    Then an error "400" is returned

  @todosmokeTests @smokeTestsWithoutKafka
  Scenario: Import an Empty Excel file with Excel format
    When I import a file "Empty_Excel_File.xlsx" with Excel format
    Then an error "400" is returned
