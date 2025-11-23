Feature: Get insured with his contracts

  @todosmokeTests @servicePrestationsRdoV2
  Scenario: Send a v2 servicePrestationsRdo request with existing and non-existent keys
    # Création du document ayant la clé "0000410993#92861CA001#1990297414825#19990203#1"
    Given I create a rdoServicePrestation from file "./features/resources/servicePrestationsRdo/rdoServicePrestation1.json"
    When I post a request from file "./features/resources/servicePrestationsRdo/request1.json" to the v2 servicePrestationsRdo endpoint

    # Réponse pour la requête "0000410993#92861CA001#1990297414825#19990203#1#549726010101,549726010102,549726010103"
    Then the result with index "0" has "3" contracts
    And the contract at index "0" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010101 |
      | numeroAdherent | 92861CA001   |
    And the contract at index "1" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010102 |
      | numeroAdherent | 92861CA001   |
    And the contract at index "2" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010103 |
      | numeroAdherent | 92861CA001   |

    # Réponse pour la requête "0000410993#92861CA001#1990297414825#19990203#1#549726010101,549726010102"
    Then the result with index "1" has "2" contracts
    And the contract at index "0" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010101 |
      | numeroAdherent | 92861CA001   |
    And the contract at index "1" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010102 |
      | numeroAdherent | 92861CA001   |

    # Réponse pour la requête "0000410993#92861CA001#1990297414825#19990203#1#549726010103"
    Then the result with index "2" has "1" contracts
    And the contract at index "0" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010103 |
      | numeroAdherent | 92861CA001   |

    # Réponse pour la requête "0000410993#92861CA001#1990297414825#19990203#1#123,456,789"
    Then the result with index "3" has "0" contracts

    # Réponse pour la requête "0000410993#92861CA001#1990297414825#19990203"
    Then the result with index "4" has "0" contracts

    # Réponse pour la requête "0000410993#92861CA001#1990297414825#19990203#1#aaa#aaa"
    Then the result with index "5" has "0" contracts

    # Réponse pour la requête "0000410993/92861CA001/1990297414825/19990203/1"
    Then the result with index "6" has "0" contracts

    # Réponse pour la requête "0000410993#92861CA001#1990297414825#19990203#1"
    Then the result with index "7" has "3" contracts
    And the contract at index "0" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010101 |
      | numeroAdherent | 92861CA001   |
    And the contract at index "1" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010102 |
      | numeroAdherent | 92861CA001   |
    And the contract at index "2" has these values
      | idDeclarant    | 0000410993   |
      | numero         | 549726010103 |
      | numeroAdherent | 92861CA001   |
