Feature: Generate TP rights with multiple Bobb products

  Background:
    Given I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @gtMultiProducts
  Scenario: Warrantie with multiple products ko
    Given I create a contract element from a file "gtmultiproduits_ko"
    When I send a test contract from file "contractForBobb/createServicePrestationNoTP"
    When I wait for 0 declarations

  @smokeTests @gtMultiProducts
  Scenario: Warrantie with multiple products ok
    Given I create a contract element from a file "gtmultiproduits_withSameStartDates"
    When I send a test contract from file "contractForBobb/createServicePrestationWithTP"
    Then I wait for 1 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | produit        | domaine | debut                  | fin                    |
      | Multi_OK | ProduitMulti01 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti01 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti01 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti03 | HOSP    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti03 | MEDG    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |

  @smokeTests @gtMultiProducts
  Scenario: Warrantie with multiple products but products have different start dates
    Given I create a contract element from a file "gtmultiproduits_withDiffStartDates"
    When I send a test contract from file "contractForBobb/createServicePrestationWithTP"
    Then I wait for 1 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | produit        | domaine | debut                  | fin                    |
      | Multi_OK | ProduitMulti01 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti01 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti01 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti03 | HOSP    | %%CURRENT_YEAR%%-06-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti03 | MEDG    | %%CURRENT_YEAR%%-06-01 | %%CURRENT_YEAR%%-12-31 |

  @smokeTests @gtMultiProducts
  Scenario: Warrantie with multiple products but products have different dates
    Given I create a contract element from a file "gtmultiproduits_withDiffDates"
    When I send a test contract from file "contractForBobb/createServicePrestationWithTP"
    Then I wait for 1 declarations
    Then there is 5 rightsDomains and the different rightsDomains has this values
      | garantie | produit        | domaine | debut                  | fin                    |
      | Multi_OK | ProduitMulti01 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-04-01 |
      | Multi_OK | ProduitMulti01 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-04-01 |
      | Multi_OK | ProduitMulti01 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-04-01 |
      | Multi_OK | ProduitMulti03 | HOSP    | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti03 | MEDG    | %%CURRENT_YEAR%%-04-02 | %%CURRENT_YEAR%%-12-31 |

  @smokeTests @gtMultiProducts
  Scenario: Warrantie with multiple products, a product is canceled and another product is a replacement
    # GT avec un produit annulé (= ProduitMulti03) et un produit de remplacement (= ProduitMulti01)
    Given I create a contract element from a file "gtmultiproduits_canceledAndReplacement"
    When I send a test contract from file "contractForBobb/createServicePrestationWithTP"
    Then I wait for 1 declarations
    Then there is 3 rightsDomains and the different rightsDomains has this values
      | garantie | produit        | domaine | debut                  | fin                    |
      | Multi_OK | ProduitMulti01 | OPTI    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti01 | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
      | Multi_OK | ProduitMulti01 | PHAR    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-12-31 |
