Feature: Get Benefit Recipients
  Parametres obligatoires lors de l appel au WS :
  - idPersonne (Identifiant Beyond contenant le n° de l’AMC) : personNumber
  - contrat : contractNumber
  - AMC : insurerId
  Parametres optionnels lors de l appel au WS :
  - adherent : subscriberId

  @smokeTests @benefitRecipients @release
  Scenario: Get Benefit Recipients
    Given I create a beneficiaire from file "benefit_recipients/beneficiaire"
    When I search for the beneficit recipients for "0000401166-MBA01bis3", "MBA01bis", "MBA01bis", "0000401166"
    Then we found 2 benefitRecipients
    Then the result with benefitRecipient id "0" has values
      | idBeyond          | 212474A-0000401166 |
      | validityStartDate | 2020-03-10         |
      | validityEndDate   | 2021-03-09         |
    Then the result with benefitRecipient id "1" has values
      | idBeyond          | 2124743-0000401166 |
      | validityStartDate | 2018-01-01         |
      | validityEndDate   | 2020-03-09         |

  @smokeTests @benefitRecipients
  Scenario: Get Benefit Recipients benef not found
    Given I create a beneficiaire from file "benefit_recipients/beneficiaire"
    When I try to search for the beneficit recipients for "0000401166-MBA01bis3", "MBA02bis", "MBA01bis", "0000401166"
    Then an error "404" is returned with message "No beneficiary found for this request"

  @smokeTests @benefitRecipients
  Scenario: Get Benefit Recipients recipients not found
    Given I create a beneficiaire from file "benefit_recipients/beneficiaire_no_benefit"
    When I search for the beneficit recipients for "0000401166-MBA01bis3", "MBA01bis", "MBA01bis", "0000401166"
    Then we found 0 benefitRecipients

  @smokeTests @benefitRecipients
  Scenario: Get Benefit Recipients
    Given I create a beneficiaire from file "benefit_recipients/beneficiaireDesordre"
    When I search for the beneficit recipients for "0000401166-MBA01bis3", "MBA01bis", "MBA01bis", "0000401166"
    Then we found 4 benefitRecipients
    Then the result with benefitRecipient id "0" has values
      | idBeyond          | 2124743-0000401166 |
      | validityStartDate | 2023-03-10         |
      | validityEndDate   | 2024-03-09         |
    Then the result with benefitRecipient id "1" has values
      | idBeyond          | 2124742-0000401166 |
      | validityStartDate | 2022-03-10         |
      | validityEndDate   | 2023-03-09         |
    Then the result with benefitRecipient id "2" has values
      | idBeyond          | 2124741-0000401166 |
      | validityStartDate | 2021-03-10         |
      | validityEndDate   | 2022-03-09         |
    Then the result with benefitRecipient id "3" has values
      | idBeyond          | 2124744-0000401166 |
      | validityStartDate | 2018-01-01         |
      | validityEndDate   | 2020-03-09         |
