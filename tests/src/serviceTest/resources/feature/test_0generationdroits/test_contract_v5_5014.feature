Feature: Test contract V5 avec un contrat résilié par anticipation et avec des garanties se suivant

  @smokeTests @caseVip
  Scenario: I send a contract with anticipation
    And I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "contratV5/5014-vip"
    When I get triggers with contract number "93000808" and amc "0000401166"
    Then I wait for 1 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut                  | fin                    | finOnline              |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-02-22 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-02-21    |
      | AXASCCGDIM | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-02-21 | %%CURRENT_YEAR%%-02-21 |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-02-22 | %%CURRENT_YEAR%%-12-31 | %%NEXT_YEAR%%-02-21    |
      | AXASCCGDIM | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-02-21 | %%CURRENT_YEAR%%-02-21 |
    Then I wait for 1 contract
