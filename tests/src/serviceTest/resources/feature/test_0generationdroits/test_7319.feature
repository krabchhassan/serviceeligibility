Feature: Test génération droits v6 BLUE-7319

  @smokeTests @radiationDansLePasse @7319
  Scenario: BLUE-7319 declarations avec DENTAIRE, reprise avec DENT_AIRE sur les memes dates ce qui doit ecrasr l acienne periode online
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I create a declaration from a file "declaration_7319_1"
    And I create a declaration from a file "declaration_7319_2"
    And I create a declaration from a file "declaration_7319_3"
    And I create a declaration from a file "declaration_7319_4"
    And I create a declaration from a file "declaration_7319_5"
    Then I process all declaration for idDeclarant "0000401166", numContrat "44269CA001", numAdhérent "44269CA001"
    And I create a service prestation from a file "7319"
    And I create a contract element from a file "gt7319"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP parameters from file "parametrageCarteTPManuel2024" with date "2024-10-01"
    #Renew on the starting date of the product
    When I renew the rights on "2024-10-01" with mode "RDO"
    Then I wait for 9 declarations
    And I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-7319" content

