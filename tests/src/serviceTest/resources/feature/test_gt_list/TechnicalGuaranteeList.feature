Feature: Get GT List
  Parametres obligatoires lors de l appel au WS :
  - Code de l'assureur : codeInsurer
  - Code de la GT : codeContractElement

  @smokeTests @gtlist @release
  Scenario: GT exists without product
    Given I create a contract element from a file "gtbase_for_list_without_product"
    When I request to gtlist with this codeInsurer "HSPRMP" and codeContractElement "HOSPR"
    Then The response element 0 has the following details and 0 products
      | codeInsurer         | HSPRMP |
      | codeContractElement | HOSPR  |
      | gtExist             | true   |
      | label               | HOSPR  |
      | ignored             | false  |
      | productElements     | []     |

  @smokeTests @gtlist
  Scenario: GT exists with product
    Given I create a contract element from a file "gtbase_for_list"
    When I request to gtlist with this codeInsurer "FAKE1006" and codeContractElement "FAKE1006"
    Then The response element 0 has the following details and 1 products
      | codeInsurer         | FAKE1006                                                                                                                      |
      | codeContractElement | FAKE1006                                                                                                                      |
      | gtExist             | true                                                                                                                          |
      | label               | FAKE1006                                                                                                                      |
      | ignored             | false                                                                                                                         |
      | productElements     | [{"codeOffer":"FAKE1006","codeProduct":"FAKE1006","codeBenefitNature":"","codeAmc":"FAKE1006","from":"2020-01-01","to":null}] |


  @smokeTests @gtlist
  Scenario: GT does not exist
    Given I create a contract element from a file "gtbase_for_list"
    When I request to gtlist with this codeInsurer "test" and codeContractElement "test"
    Then The response element 0 has the following details and 0 products
      | codeInsurer         | test  |
      | codeContractElement | test  |
      | gtExist             | false |
      | ignored             | false |
      | productElements     | []    |


  @smokeTests @gtlist
  Scenario: Several GTs
    Given I create a contract element from a file "gtbase_for_list"
    When I request to gtlist with this codeInsurer "FAKE1006,FAKE1007" and codeContractElement "FAKE1006,FAKE1007"
    Then The response element 0 has the following details and 1 products
      | codeInsurer         | FAKE1006                                                                                                                      |
      | codeContractElement | FAKE1006                                                                                                                      |
      | gtExist             | true                                                                                                                          |
      | label               | FAKE1006                                                                                                                      |
      | ignored             | false                                                                                                                         |
      | productElements     | [{"codeOffer":"FAKE1006","codeProduct":"FAKE1006","codeBenefitNature":"","codeAmc":"FAKE1006","from":"2020-01-01","to":null}] |
    And The response element 1 has the following details and 0 products
      | codeInsurer         | FAKE1007 |
      | codeContractElement | FAKE1007 |
      | gtExist             | false    |
      | ignored             | false    |
      | productElements     | []       |
