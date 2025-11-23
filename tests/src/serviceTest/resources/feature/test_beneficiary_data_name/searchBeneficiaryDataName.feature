Feature: Get Beneficiary data name
  Parametres obligatoires lors de l appel au WS :
  - personNumber (Identifiant Beyond contenant le n° de l’AMC) : personNumber
  - contractNumber : contractNumber

  @smokeTests @beneficiaryDataName
  Scenario: Get Beneficiary data name
    Given I create a beneficiaire from file "beneficiary_data_name/beneficiaire"
    When I search for the beneficiary data name for "0000401166-MBA01bis3", "MBA01bis"
    Then the result with beneficiaryDataName has values
      | lastName   | NOMTEST3    |
      | commonName | NOMTEST2    |
      | firstName  | PRENOMTEST3 |
      | civility   | MME         |

  @smokeTests @beneficiaryDataName
  Scenario: Get Beneficiary data name benef not found
    Given I create a beneficiaire from file "beneficiary_data_name/beneficiaire"
    When I try to search for the beneficiary data name for "0000401166-MBA01bis3", "MBA03bis"
    Then an error "404" is returned with message "No beneficiary found for this request"


