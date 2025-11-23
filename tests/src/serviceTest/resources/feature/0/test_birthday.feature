Feature: Test contract V5 with consolidation on birthday

  @smokeTests @caseConsolidation @caseRenouvellement @release
  Scenario: I renew a contract on a birthday
    Given I change GMT TimeZone
    Given I create a contract element from a file "gtaxa_cgdiv"
    And I create a declarant from a file "declarantbaloo"
    And I create manual TP card parameters from file "parametrageTPAxaAnniversaire"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I create a service prestation from a file "servicePrestationGIBAnniversaire"
    When I renew the rights on "%%LAST_YEAR%%-07-20" with mode "NO_RDO"

    Then I wait for the last renewal trigger with contract number "NUMBERCONTRAT" and amc "0000401166" to be "Processed"
    When I get the triggerBenef on the trigger
    Then the triggerBenef has this values
      | statut         | Processed     |
      | nir            | 1560832135165 |
      | numeroPersonne | 2122706       |

    Then I wait for 2 declarations
    Then there is 4 rightsDomains and the different rightsDomains has this values
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | %%LAST_YEAR%%-07-25 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | AXASCCGDIV | HOSP    | %%LAST_YEAR%%-07-25 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | AXASCCGDIV | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-24 | null       |
      | AXASCCGDIV | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-07-24 | null       |
    Then there is 4 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIV | OPAU    | %%LAST_YEAR%%-07-25 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | AXASCCGDIV | HOSP    | %%LAST_YEAR%%-07-25 | %%LAST_YEAR%%-12-31 | %%LAST_YEAR%%-12-31 |
      | AXASCCGDIV | OPAU    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-04-28 | %%CURRENT_YEAR%%-04-28 |
      | AXASCCGDIV | DENT    | %%CURRENT_YEAR%%-01-01 | %%CURRENT_YEAR%%-04-28 | %%CURRENT_YEAR%%-04-28 |

    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-renewbirthday" content
