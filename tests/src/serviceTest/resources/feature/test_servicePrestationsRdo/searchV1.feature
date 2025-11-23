Feature: Get insured with his contracts
  Parametres obligatoires lors de l appel :
  - AMC : idDeclarant
  - Adherent : numeroAdherent
  - Nir : nir
  - Date de naissance : dateNaissance
  - Rang de naissance : rangNaissance

  @smokeTests @servicePrestationsRdo @release
  Scenario: Get an existing service prestation
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationDroitsOuverts"
    When I search service prestation for amc "929405823444", adherent "83747438", nir "1701062498046", birth date "19791006" and birth rank "1"
    Then the servicePrestationsRdo number 0 has values
      | idDeclarant                           | 929405823444  |
      | numero                                | 8343484392    |
      | numeroAdherent                        | 83747438      |
      | nir                                   | 1701062498046 |
      | dateNaissance                         | 19791006      |
      | rangNaissance                         | 1             |
      | codeDroit001                          | ATRDENT       |
      | nomUsage                              | TEST 5568     |
      | idBeyondDestinataireRelevePrestations | tim2          |

  @smokeTests @servicePrestationsRdo
  Scenario: Get service prestation not found
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationDroitsOuverts"
    When I search service prestation for amc "929405823499", adherent "83747438", nir "1701062498046", birth date "19791006" and birth rank "1"
    Then the response has an HTTP code "204"

  @smokeTests @servicePrestationsRdo
  Scenario: Get an existing service prestation and return a nomUsage for an insured
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationWithNomUsage"
    When I search service prestation for amc "0097810998", adherent "9999280601", nir "1701062498046", birth date "19791006" and birth rank "1"
    Then the servicePrestationsRdo number 0 has values
      | idDeclarant                           | 0097810998                |
      | numero                                | 9999280601                |
      | numeroAdherent                        | 9999280601                |
      | nir                                   | 1701062498046             |
      | dateNaissance                         | 19791006                  |
      | rangNaissance                         | 1                         |
      | codeDroit001                          | KC_PlatineBase            |
      | nomUsage                              | TEST 5568                 |
      | idBeyondDestinataireRelevePrestations | DRP-9999280601-0097810998 |
