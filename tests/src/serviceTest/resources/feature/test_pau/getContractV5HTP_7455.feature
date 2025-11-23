Feature: Search contract PAU V5 HTP BLUE-7455
  Quand un contrat ouvert existe mais que l'offre PW ne couvre pas totalement la periode de la GT => le PAU doit
  répondre en limitant la période envoyée aux droits PW récupérés

  Background:
    Given I create a service prestation from a file "servicePrestation_7455"
    And I create a beneficiaire from file "contractForPauV5/beneficiaire-7455"

  @smokeTests @pauv5 @7455
  Scenario: PAU V5 HTP - Cas 7455 - Demande en 2020 + Offre PW débute en 2021
    Given I create a contract element from a file "gt_7455"
    When I get contrat PAUV5 without endDate for 2810631555499 '19900619' 1 '2020-01-01' 0000401166 HTP
    Then the pau is identical to "pau/v5/pau-HTP-7455" content

  @smokeTests @pauv5 @7455
  Scenario: PAU V5 HTP - Cas 7455 - Demande en 2020/2021 + Offre PW débute en 2021
    Given I create a contract element from a file "gt_7455"
    When I get contrat PAUV5 for 2810631555499 '19900619' 1 '2020-06-01' '2021-02-01' 0000401166 HTP
    Then the pau is identical to "pau/v5/pau-HTP-7455-3" content

  @smokeTests @pauv5 @7455
  Scenario: PAU V5 HTP - Cas 7455 - Demande en 2021 + Offre PW débute en 2021
    Given I create a contract element from a file "gt_7455"
    When I get contrat PAUV5 without endDate for 2810631555499 '19900619' 1 '2021-01-01' 0000401166 HTP
    Then the pau is identical to "pau/v5/pau-HTP-7455" content

  @smokeTests @pauv5 @7455
  Scenario: PAU V5 HTP - Cas 7455 - Demande en 2022 + Offre PW débute en 2021
    Given I create a contract element from a file "gt_7455"
    When I get contrat PAUV5 without endDate for 2810631555499 '19900619' 1 '2022-01-01' 0000401166 HTP
    Then the pau is identical to "pau/v5/pau-HTP-7455-2" content

  @smokeTests @pauv5 @7455
  Scenario: PAU V5 HTP - Cas 7455 - Demande en 2020/2021 + Bobb avec 2 produits + Offres PW débutent en 2021
    Given I create a contract element from a file "gt_7455_1"
    When I get contrat PAUV5 for 2810631555499 '19900619' 1 '2020-01-01' '2021-02-01' 0000401166 HTP
    Then the pau is identical to "pau/v5/pau-HTP-7455-4" content

  @smokeTests @pauv5 @7455
  Scenario: PAU V5 HTP - Cas 7455 - Demande en 2021/2022 + Bobb avec 2 produits + Offres PW débutent en 2021
    Given I create a contract element from a file "gt_7455_2"
    When I get contrat PAUV5 for 2810631555499 '19900619' 1 '2021-01-01' '2022-02-01' 0000401166 HTP
    Then the pau is identical to "pau/v5/pau-HTP-7455-5" content