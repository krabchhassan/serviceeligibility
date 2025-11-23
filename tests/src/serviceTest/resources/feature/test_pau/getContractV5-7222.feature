Feature: Search contract PAU V5 BLUE-7222 - Couple affilié chacun a une adhésion active en tant qu'assuré principal et conjoint

  @smokeTests @smokeTestsWithoutKafka @pauv5 @7222
  Scenario: PAU V5 HTP - Priorisation des contrats - Doit retourner en priorité le contrat ayant l'assuré principal correspondant au NIR de la requête
    Given I create a beneficiaire from file "contractForPauV5/benef-7222-femme"
    Given I create a beneficiaire from file "contractForPauV5/benef-7222-homme"

    Given I create a service prestation from a file "servicePrestation_7222-femmeSouscr"
    Given I create a service prestation from a file "servicePrestation_7222-hommeSouscr"

    When I get contrat PAUV5 for 2950111111111 '19950125' 1 '2025-01-01' '2025-01-01' 0000401166 HTP
    Then we found 2 contracts
    # Le premier contrat est celui de la femme
    Then the contract number 0 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ET0001     |
    # Le deuxième contrat est celui de l'homme
    Then the contract number 1 data has values
      | insurerId    | 0000401166 |
      | subscriberId | ET0002     |
