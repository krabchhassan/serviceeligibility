Feature: Test adresse carte batch 620

  Background:
    Given I create a declarant from a file "declarantbaloo_5813"

  @smokeTests @batch620 @adresseCarte
  Scenario: Test Assure principal sans objet adresse
    Given I create a contract element from a file "gtaxa"
    And I create an automatic TP card parameters on next year from file "parametrageCarteTPBaloo_5813"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "5813-CasAssPrincSansAdresse"
    Then I wait for 2 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    # on génère la carte demat si pas d'adresse
    Then I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    Then 1 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"

  @smokeTests @batch620 @adresseCarte
  Scenario: Test Rang Administratif sans objet adresse
    Given I create a contract element from a file "gtaxa"
    And I create an automatic TP card parameters on next year from file "parametrageCarteTPBaloo_5813"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "5813-CasRangAdminSansAdresse"
    Then I wait for 2 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    # on génère la carte demat si pas d'adresse
    Then I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    Then 1 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"

  @smokeTests @batch620 @adresseCarte
  Scenario: Test Assure Principal avec l'objet adresse mais tous les champs null
    Given I create a contract element from a file "gtaxa"
    And I create an automatic TP card parameters on next year from file "parametrageCarteTPBaloo_5813"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a test contract from file "5813-CasAssPrincChampsNull"
    Then I wait for 2 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    # on génère la carte demat si pas d'adresse
    Then I wait for 1 card
    When I get flux with parameters
      | fichierEmis  | true       |
      | amc          | 0000401166 |
      | numberByPage | 10         |
    Then 1 flux is returned
    Then I received a flux with the type file "ARL", the declarant id "0000401166" and the processus "CARTE-TP"

  @smokeTests @batch620 @adresseCarte
  Scenario: Test Rang Administratif avec adresse
    And I create a declaration from a file "batch620/declarationRangAdminWithAdresse1"
    And I create a declaration from a file "batch620/declarationRangAdminWithAdresse2"
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 cartes papier
    Then the carte papier at index 0 has this adresse
      | ligne1         | ligne4                      | ligne6            | codePostal |
      | MONSIEUR BEN 1 | 2 RUE JEAN JACQUES ROUSSEAU | 78130 LES MUREAUX | 78130      |

  @smokeTests @batch620 @adresseCarte
  Scenario: Test Assure Principal avec adresse
    And I create a declaration from a file "batch620/declarationAssPrincWithAdresse1"
    And I create a declaration from a file "batch620/declarationEnfantAssPrinc"
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    Then I wait for 1 cartes papier
    Then the carte papier at index 0 has this adresse
      | ligne1          | ligne4                      | ligne6            | codePostal |
      | MONSIEUR ASSPRI | 2 RUE JEAN JACQUES ROUSSEAU | 78130 LES MUREAUX | 78130      |
