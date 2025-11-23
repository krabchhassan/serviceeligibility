Feature: Search contract PAU V5 with nature of domaine TP in TP_ONLINE

  @smokeTests @smokeTestsWithoutKafka @pauv5
  Scenario: Get nature prestation of domaines TP from PAU V5 event
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas1-contractTPEvent-nature-domaineTP"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00012"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-01-02' '2023-01-03' 0000401166 TP_ONLINE for domains PHAR for beneficiaryId P00012
    Then the product 0 of the right 0 has a benefitType PHARMACIE with a convention IS/IT/YOP in the domain 0
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2023-01-02' '2023-01-03' 0000401166 TP_ONLINE for beneficiaryId P00012
    Then the product 0 of the right 0 has a benefitType PHARMACIE with a convention IS/IT/YOP in the domain 0
    Then the product 0 of the right 0 has a benefitType PHARMACIE with a convention TEST in the domain 1

  @nosmokeTests
  # JIRA BLUE-5796 : on ne remonte plus les conventions dans le cas du TBD (limitation de offerstructure et aucun intérêt à ce stade pour le cetip)
  Scenario: Get nature prestation of domaines TP from PAU V5 TDB
    Given I drop the collection for Contract
    And I drop the collection for Beneficiary
    And I create a contrat from file "contractForPauV5/cas1-contractTDB-nature-domaineTP"
    And I create a service prestation from a file "pauv5-cas1-servicePrestation"
    And I create a beneficiaire from file "contractForPauV5/beneficiairePauP00013"
    When I get contrat PAUV5 for 1591275115460 '19621023' 1 '2018-01-02' '2023-01-03' 0000401166 TP_ONLINE for beneficiaryId P00013
    Then the product 0 of the right 0 has a benefitType PHARMACIE with a convention IS/IT/YOP in the domain 0
    Then the product 0 of the right 0 has a benefitType PHARMACIE with a convention TEST in the domain 1
