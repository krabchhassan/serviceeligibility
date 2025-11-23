Feature: Get card

  @todosmokeTests @todosmokeTestsWithoutKafka @card @cardV3
  Scenario: Get existing card v3
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | AL      |
      | libelle | Almerys |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | SP                      |
      | libelle | Santé-Pharma / SP santé |
    Given I create a card with parameters
      | AMC_contrat | 0000401166-78478 |
    When I get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2034-06-01 |
      | numeroContrat | 78478      |
    Then 1 card is returned
    And Card at index 0 is equal to Json from file "card/card_v3/nominalCardV3"
    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2034-06-01 |
      | numeroContrat | 78478      |
    Then 1 card is returned
    And Card at index 0 is equal to Json from file "card/card_v4/nominalCardV4"

  @todosmokeTests @todosmokeTestsWithoutKafka @card @cardV3
  Scenario: Get non existing card v3
    Given I create a card with parameters
      | AMC_contrat | 0000000001-00000 |
    When I try to get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2034-06-01 |
      | numeroContrat | 78478      |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6010                                   |
      | libelle | Aucune carte TP valide pour ce contrat |
    When I try to get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2034-06-01 |
      | numeroContrat | 78478      |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6010                                   |
      | libelle | Aucune carte TP valide pour ce contrat |

  @todosmokeTests @todosmokeTestsWithoutKafka @card @cardV3
  Scenario: Get wrong date format card v3
    Given I create a card with parameters
      | AMC_contrat | 0000401166-78478 |
    When I try to get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2034/06/01 |
      | numeroContrat | 78478      |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6000                                                                                             |
      | libelle | Paramètres recherche BDD incorrects - Le format du champ 'dateReference' doit être 'yyyy-MM-dd'. |
    When I try to get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2034/06/01 |
      | numeroContrat | 78478      |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6000                                                                                             |
      | libelle | Paramètres recherche BDD incorrects - Le format du champ 'dateReference' doit être 'yyyy-MM-dd'. |

  @todosmokeTests @todosmokeTestsWithoutKafka @card @cardV3
  Scenario: Get date too old card v3
    Given I create a card with parameters
      | AMC_contrat | 0000401166-78478 |
    When I try to get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2004-06-01 |
      | numeroContrat | 78478      |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6009                                                                                                                           |
      | libelle | La date de référence doit être supérieure ou égale à la date du jour - 'dateReference' doit être supérieure à la date du jour. |
    When I try to get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2004-06-01 |
      | numeroContrat | 78478      |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6009                                                                                                                           |
      | libelle | La date de référence doit être supérieure ou égale à la date du jour - 'dateReference' doit être supérieure à la date du jour. |

  @todosmokeTests @todosmokeTestsWithoutKafka @card @cardV3
  Scenario: Get missing parameters card v3
    Given I create a card with parameters
      | AMC_contrat | 0000401166-78478 |
    When I try to get cards v3 with request
      | numeroAmc | 0000401166 |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6000                                                                            |
      | libelle | Paramètres recherche BDD incorrects - Le champ 'dateReference' est obligatoire. |
    When I try to get cards v4 with request
      | numeroAmc | 0000401166 |
    Then 0 cards are returned
    And Response contains this codeResponse
      | code    | 6000                                                                            |
      | libelle | Paramètres recherche BDD incorrects - Le champ 'dateReference' est obligatoire. |

  @todosmokeTests @card @cardV3 @domainRestitutionLimit
  Scenario: Get card with domain restitution limit of 1 month and positionnerFinDeMois for LARA
    Given I create a declarant from a file "declarant_5770_2"
    Given I create a card from a file "11_card_regroupement/card3benefsLARA"
    When I get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 24087      |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |
    And the card for the benef with indice 1 has 2 couvertures
      | codeDomaine | periodeFin |
      | LARA        | 2050/05/31 |
      | OPTI        | 2050/12/31 |
    And the card for the benef with indice 2 has 2 couvertures
      | codeDomaine | periodeFin |
      | LARA        | 2050/05/31 |
      | OPTI        | 2050/12/31 |

    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 24087      |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |
    And the card for the benef with indice 1 has 2 couvertures
      | codeDomaine | periodeFin |
      | LARA        | 2050/05/31 |
      | OPTI        | 2050/12/31 |
    And the card for the benef with indice 2 has 2 couvertures
      | codeDomaine | periodeFin |
      | LARA        | 2050/05/31 |
      | OPTI        | 2050/12/31 |

    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 24087      |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |
    And the card for the benef with indice 1 has 2 couvertures
      | codeDomaine | periodeFin |
      | LARA        | 2050/05/31 |
      | OPTI        | 2050/12/31 |
    And the card for the benef with indice 2 has 2 couvertures
      | codeDomaine | periodeFin |
      | LARA        | 2050/05/31 |
      | OPTI        | 2050/12/31 |

  @todosmokeTests @card @cardV3 @domainRestitutionLimit
  Scenario: Get card with domain restitution limit for OPAU but benef with OPTI only
    Given I create a declarant from a file "declarantbaloo_5770"
    Given I create a card from a file "11_card_regroupement/card1benefOPTI"
    When I get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 24087      |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |
    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 24087      |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |

  @todosmokeTests @card @cardV3 @domainRestitutionLimit
  Scenario: Get old card with domain restitution limit for OPAU but benef with OPTI only
    Given I create a declarant from a file "declarantbaloo_5770"
    Given I create a card from a file "11_card_regroupement/oldCardOPTI"
    When I get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | CONSOW0    |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |
    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | CONSOW0    |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |

  @todosmokeTests @card @cardV3 @domainRestitutionLimit
  Scenario: Get old card with domain restitution limit for OPAU
    Given I create a declarant from a file "declarantbaloo_5770"
    Given I create a card from a file "11_card_regroupement/oldCardWithOPAU"
    When I get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | CONSOW0    |
    Then 1 card is returned
    And the card for the benef with indice 0 has 2 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | CONSOW0    |
    Then 1 card is returned
    And the card for the benef with indice 0 has 2 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/12/31 |
      | AUDI        | 2050/12/31 |

  @todosmokeTests @card @cardV3 @domainRestitutionLimit
  Scenario: Get old card with domain restitution limit for OPTI and AUDI
    Given I create a declarant from a file "declarant_5900"
    Given I create a card from a file "11_card_regroupement/oldCardWithOPAU"
    When I get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | CONSOW0    |
    Then 1 card is returned
    And the card for the benef with indice 0 has 2 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/05/31 |
      | AUDI        | 2050/05/01 |
    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | CONSOW0    |
    Then 1 card is returned
    And the card for the benef with indice 0 has 2 couvertures
      | codeDomaine | periodeFin |
      | OPTI        | 2050/05/31 |
      | AUDI        | 2050/05/01 |

  @todosmokeTests @card @cardV3 @domainRestitutionLimit
  Scenario: Get old card CETIP (AON) with domain restitution limit
    Given I create a declarant from a file "declarantAon"
    Given I create a card from a file "11_card_regroupement/oldCardCetip"
    When I get cards v3 with request
      | numeroAmc     | 0000401182 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 10901516   |
    Then 1 card is returned
    And the card for the benef with indice 0 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |
    And the card for the benef with indice 1 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |
    And the card for the benef with indice 2 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |
    And the card for the benef with indice 3 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |

    When I get cards v4 with request
      | numeroAmc     | 0000401182 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 10901516   |
    Then 1 card is returned
    And the card for the benef with indice 0 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |
    And the card for the benef with indice 1 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |
    And the card for the benef with indice 2 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |
    And the card for the benef with indice 3 has 5 couvertures
      | codeDomaine | periodeFin |
      | PHAR        | 2050/04/11 |
      | OPTI        | 2050/12/31 |
      | DENT        | 2050/12/31 |
      | AUDI        | 2050/12/31 |
      | HOSP        | 2050/12/31 |

  @todosmokeTests @card @cardV3
  Scenario: Create card from declaration and get it to check information of the first domain (for PHAR regroupement)
    Given I create a declarant from a file "declarant_5900"
    And I create a declaration from a file "batch620/declaration-5895"
    And I process declarations for carteDemat the "2050-01-01"
    When I get cards v3 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 5497MBA003 |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeGarantie | libelleCodeRenvoiAdditionnel | codeRenvoiAdditionnel | categorieDomaine |
      | GT_5497_D1   | Mon libellé 2                | RV2                   | PHNO             |
    When I get cards v4 with request
      | numeroAmc     | 0000401166 |
      | dateReference | 2050-04-01 |
      | numeroContrat | 5497MBA003 |
    Then 1 card is returned
    And the card for the benef with indice 0 has 1 couvertures
      | codeGarantie | libelleCodeRenvoiAdditionnel | codeRenvoiAdditionnel | categorieDomaine |
      | GT_5497_D1   | Mon libellé 2                | RV2                   | PHNO             |
