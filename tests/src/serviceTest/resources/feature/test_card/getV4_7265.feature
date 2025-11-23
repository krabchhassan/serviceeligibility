Feature: WS CarteDematerialisee v4

  @todosmokeTests @card @cardV4 @7265
  Scenario: Get CarteDemat v4 and CarteDemat v3 and check new fields
    Given I create a card from a file "card/card_v4/carteDemat-7265"
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I create a declarant from a file "declarant/declarant_0000452433"
    When I try to get cards v4 with request
      | numeroAmc     | 0000452433 |
      | dateReference | 2050-06-03 |
      | numeroContrat | ET0306     |
    Then Card at index 0 is equal to Json from file "card/card_v4/responseV4-7265-1"
    When I try to get cards v3 with request
      | numeroAmc     | 0000452433 |
      | dateReference | 2050-06-03 |
      | numeroContrat | ET0306     |
    Then Card at index 0 is equal to Json from file "card/card_v3/responseV3-7265-1"
