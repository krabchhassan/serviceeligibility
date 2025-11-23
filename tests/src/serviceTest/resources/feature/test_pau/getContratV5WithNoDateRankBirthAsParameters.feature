Feature: Search contract PAU V5 with no date and rank of birth as parameters in HTP and TP_ONLINE

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get pau HTP without date and rank of birth as parameters
    Given I drop the collection for Service Prestation
    Given I drop the collection for Beneficiary

    Given I create a beneficiaire from file "contractForPauV5/beneficiaireHTP"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Adhesion_Cas01"
    When I get contrat PAUV5 for 1791062498044 '2022-01-10' '2022-01-10' 0000401166 HTP
#todotests
#    Then the contract data for Cas1et2 has values
#      | insurerId     | 0000401166 |
#      | periodDebut   | 2022-01-10 |
#      | periodFin     | 2022-01-10 |
#      | nombreBenefit | 7          |

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get pau TP_ONLINE without date and rank of birth as parameters
    Given I drop the collection for Service Prestation
    Given I drop the collection for Beneficiary

    Given I create a beneficiaire from file "contractForPauV5/beneficiaireHTP"
    Given I create a contract element from a file "ce_baloo"
    Given I create a service prestation from a file "Adhesion_Cas01"
    When I get contrat PAUV5 for 1791062498044 '2022-01-10' '2022-01-10' 0000401166 TP_ONLINE
    Then an error "400" is returned with message "Veuillez renseigner les critères de recherche du bénéficiaire"
