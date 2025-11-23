Feature: Forçage du PAU - Feature BLUE-6133

  Background:
    Given I create a service prestation from a file "servicePrestation1-7015"
    Given I create a beneficiaire from file "benef-7015"
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS |
      | libelle | IS |

  @smokeTests @smokeTestsWithoutKafka @pauv5 @forcePAU @7015
  Scenario: BLUE-7015 : Forçage HTP avec paramétrage PW qui ne couvre pas la globalité des droits + Appel PAU après le contrat existant
      # Appel HTP
    When I get contrat PAUV5 without endDate for 1112233445566 '19110224' 1 '2025-02-24' 0000401166 HTP for issuingCompanyCode 000ER00955 and isForced true
    Then we found 1 contracts
    Then the contract with indice 0 is forced
    Then the contract and the right 0 data has this period on the product 0
      | debut | 2010-01-01 |
      | fin   | 2024-12-31 |
