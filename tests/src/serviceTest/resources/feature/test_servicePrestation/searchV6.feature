Feature: Get ServicePrestation
  Parametres obligatoires lors de l appel au WS :
  - AMC : idDeclarant
  - Adherent : numeroAdherent
  - Date de naissance : dateNaissance
  - Periode de soin : debutPeriodeSoin et finPeriodeSoin
  Parametres optionnels lors de l appel au WS :
  - rang de naissance : rangNaissance
  - Numero de Secu : nir


  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @servicePrestationV6
  Scenario: Get an existing service prestation V6 from one contract without end date on droits, and query it during the period
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationDroitsOuverts"
    When I search service prestation in version "6" for amc "929405823444", adherent "83747438", nir "1701062498046", birth date "19791006", birth rank "1" from "2020-03-01" to "2020-06-30"
    Then the servicePrestation number "0" has values
      | idDeclarant                           | 929405823444                |
      | numeroAdherent                        | 83747438                    |
      | numero                                | 8343484392                  |
      | assureNirCode                         | 1701062498046               |
      | assureNumeroPersonne                  | 288939000                   |
      | assureDateNaissance                   | 19791006                    |
      | assureRangNaissance                   | 1                           |
      | codeDroit001                          | ATRDENT                     |
      | debutDroit001                         | 2020-01-01                  |
      | finDroit001                           |                             |
      | codeDroit002                          | ATRPHAR                     |
      | debutDroit002                         | 2020-01-01                  |
      | finDroit002                           |                             |
      | destPrestationsV4Nom                  | TEST2                       |
      | destPrestationsV4Rib                  | FR7613106001004578945612336 |
      | destPrestV4ModePaiement               | V                           |
      | destPrestV4PeriodeDebut               | 2020-01-01                  |
      | destRelevePrestNom                    | TEST3                       |
      | destRelevePrestDebut                  | 2020-01-01                  |
      | idDestinatairePaiements               | bob                         |
      | idBeyondDestinatairePaiements         | tim                         |
      | idDestinataireRelevePrestations       | bob2                        |
      | idBeyondDestinataireRelevePrestations | tim2                        |

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @servicePrestationV6
  Scenario: Get an existing service prestation V6 from one contract without end date on droits, and query it during the period
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationDroitsOuvertsV6"
    When I search service prestation in version "6" for amc "929405823444", adherent "83747438", nir "1701062498046", birth date "19791006", birth rank "1" from "2020-03-01" to "2020-06-30"
    Then the servicePrestation number "0" has values
      | idDeclarant                           | 929405823444                |
      | numeroAdherent                        | 83747438                    |
      | numero                                | 8343484392                  |
      | assureNirCode                         | 1701062498046               |
      | assureNumeroPersonne                  | 288939000                   |
      | assureDateNaissance                   | 19791006                    |
      | assureRangNaissance                   | 1                           |
      | codeDroit001                          | ATRDENT                     |
      | debutDroit001                         | 2020-01-01                  |
      | finDroit001                           |                             |
      | codeDroit002                          | ATRPHAR                     |
      | debutDroit002                         | 2020-01-01                  |
      | finDroit002                           |                             |
      | destPrestationsV4Nom                  | TEST2                       |
      | destPrestationsV4Rib                  | FR7613106001004578945612336 |
      | destPrestV4ModePaiement               | V                           |
      | destPrestV4PeriodeDebut               | 2020-01-01                  |
      | destRelevePrestNom                    | TEST3                       |
      | destRelevePrestDebut                  | 2020-01-01                  |
      | idDestinatairePaiements               | bob                         |
      | idBeyondDestinatairePaiements         | tim                         |
      | idDestinataireRelevePrestations       | bob2                        |
      | idBeyondDestinataireRelevePrestations | tim2                        |
      | periodesDroitsCarteTPDebut            | 2020-01-01                  |
      | periodesDroitsCarteTPFin              | 2023-12-31                  |
      | collegeTP                             | Cadre                       |
      | collectiviteTP                        | 362 521 879 00034           |
