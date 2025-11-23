Feature: Get one declaration

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test Nominal no contract
    Given I create a declarant from a file "consultationdroits/1_nominal_no_contrat_declarant"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration1"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration2"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration3"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration4"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/visio_droit_nominal_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/visio_droit_nominal_expected_response.xml" content
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/carte_nominal_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/carte_nominal_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test Nominal no contract with soap version 2
    Given I create a declarant from a file "consultationdroits/1_nominal_no_contrat_declarant"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration1"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration2"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration3"
    Given I create a declaration from a file "consultationdroits/1_nominal_no_contrat_declaration4"
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/visio_droit_nominal_request_soap12.xml"
    Then the expected soap response is identical to "consultationDroits/v3/visio_droit_nominal_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test Damiani
    Given I create a declarant from a file "consultationdroits/2_damiani_declarant"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration1"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration2"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration3"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration4"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration5"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration6"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration7"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration8"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration9"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration10"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration11"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration12"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration13"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration14"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration15"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration16"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration17"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration18"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration19"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration20"
    Given I create a declaration from a file "consultationdroits/2_damiani_declaration21"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/carte_mobile_damiani_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/carte_mobile_demiani_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test Chopin
    Given I create a declarant from a file "consultationdroits/3_chopin_declarant"
    Given I create a declaration from a file "consultationdroits/3_chopin_declaration1"
    Given I create a declaration from a file "consultationdroits/3_chopin_declaration2"
    Given I create a declaration from a file "consultationdroits/3_chopin_declaration3"
    Given I create a declaration from a file "consultationdroits/3_chopin_declaration4"
    Given I create a declaration from a file "consultationdroits/3_chopin_declaration5"
    Given I create a declaration from a file "consultationdroits/3_chopin_declaration6"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/3_chopin_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/3_chopin_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 1
    Given I create a declarant from a file "consultationdroits/4_benef_1_declarant"
    Given I create a declaration from a file "consultationdroits/4_benef_1_declaration1"
    Given I create a declaration from a file "consultationdroits/4_benef_1_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/4_benef_1_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/4_benef_1_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 2
    Given I create a declarant from a file "consultationdroits/5_benef_2_declarant"
    Given I create a declaration from a file "consultationdroits/5_benef_2_declaration1"
    Given I create a declaration from a file "consultationdroits/5_benef_2_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/5_benef_2_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/5_benef_2_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 5
    Given I create a declarant from a file "consultationdroits/6_benef_5_declarant"
    Given I create a declaration from a file "consultationdroits/6_benef_5_declaration1"
    Given I create a declaration from a file "consultationdroits/6_benef_5_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/6_benef_5_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/6_benef_5_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 6
    Given I create a declarant from a file "consultationdroits/7_benef_6_declarant"
    Given I create a declaration from a file "consultationdroits/7_benef_6_declaration1"
    Given I create a declaration from a file "consultationdroits/7_benef_6_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/7_benef_6_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/7_benef_6_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 7
    Given I create a declarant from a file "consultationdroits/8_benef_7_declarant"
    Given I create a declaration from a file "consultationdroits/8_benef_7_declaration1"
    Given I create a declaration from a file "consultationdroits/8_benef_7_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/8_benef_7_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/8_benef_7_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 8
    Given I create a declarant from a file "consultationdroits/9_benef_8_declarant"
    Given I create a declaration from a file "consultationdroits/9_benef_8_declaration1"
    Given I create a declaration from a file "consultationdroits/9_benef_8_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/9_benef_8_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/9_benef_8_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 9
    Given I create a declarant from a file "consultationdroits/10_benef_9_declarant"
    Given I create a declaration from a file "consultationdroits/10_benef_9_declaration1"
    Given I create a declaration from a file "consultationdroits/10_benef_9_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/10_benef_9_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/10_benef_9_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire 10
    Given I create a declarant from a file "consultationdroits/11_benef_10_declarant"
    Given I create a declaration from a file "consultationdroits/11_benef_10_declaration1"
    Given I create a declaration from a file "consultationdroits/11_benef_10_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV2" request from file "consultationDroits/v2/11_benef_10_request.xml"
    Then the expected soap response is identical to "consultationDroits/v2/11_benef_10_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test card nominal
    Given I create a declarant from a file "carteDemat/1_nominal_declarant"
    Given I create a card from a file "1_nominal_card1"
    Given I create a card from a file "1_nominal_card2"
    Given I create a card from a file "1_nominal_card3"
    Given I create a card from a file "1_nominal_card4"
    Given I create a card from a file "1_nominal_card5"
    Given I create a card from a file "1_nominal_card6"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/1_nominal/_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/1_nominal_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test card nominal with Version 2
    Given I create a declarant from a file "carteDemat/2_nominal_V2_declarant"
    Given I create a card from a file "2_nominal_V2_card1"
    Given I create a card from a file "2_nominal_V2_card2"
    Given I create a card from a file "2_nominal_V2_card3"
    Given I create a card from a file "2_nominal_V2_card4"
    Given I create a card from a file "2_nominal_V2_card5"
    Given I create a card from a file "2_nominal_V2_card6"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/2_nominal_V2_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/2_nominal_V2_expected_response.xml" content


  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test card nominal with two cards
    Given I create a declarant from a file "carteDemat/2_nominal_2_declarant"
    Given I create a card from a file "2_nominal_2_card1"
    Given I create a card from a file "2_nominal_2_card2"
    Given I create a card from a file "2_nominal_2_card3"
    Given I create a card from a file "2_nominal_2_card4"
    Given I create a card from a file "2_nominal_2_card5"
    Given I create a card from a file "2_nominal_2_card6"
    Given I create a card from a file "2_nominal_2_card7"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/2_nominal_2_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/2_nominal_2_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test error date parameter
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/3_error_date_request.xml" with delta reference date -5
    Then the expected soap response is identical to "carteDematerialisee/v1/3_error_date_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test error missing contrat
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/4_error_contract_request.xml" with delta reference date -5
    Then the expected soap response is identical to "carteDematerialisee/v1/4_error_contract_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test card not found
    Given I create a declarant from a file "carteDemat/5_card_not_found_declarant"
    Given I create a card from a file "5_card_not_found_card1"
    Given I create a card from a file "5_card_not_found_card2"
    Given I create a card from a file "5_card_not_found_card3"
    Given I create a card from a file "5_card_not_found_card4"
    Given I create a card from a file "5_card_not_found_card5"
    Given I create a card from a file "5_card_not_found_card6"
    Given I create a card from a file "5_card_not_found_card7"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/5_card_not_found_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/5_card_not_found_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test multiple beneficiaires
    Given I create a declarant from a file "carteDemat/6_multiple_beneficiaires_declarant"
    Given I create a card from a file "6_multiple_beneficiaires_card1"
    Given I create a card from a file "6_multiple_beneficiaires_card2"
    Given I create a card from a file "6_multiple_beneficiaires_card3"
    Given I create a card from a file "6_multiple_beneficiaires_card4"
    Given I create a card from a file "6_multiple_beneficiaires_card5"
    Given I create a card from a file "6_multiple_beneficiaires_card6"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/6_multiple_beneficiaires_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/6_multiple_beneficiaires_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test two cards
    Given I create a declarant from a file "carteDemat/7_two_cards_declarant"
    Given I create a card from a file "7_two_cards_card1"
    Given I create a card from a file "7_two_cards_card2"
    Given I create a card from a file "7_two_cards_card3"
    Given I create a card from a file "7_two_cards_card4"
    Given I create a card from a file "7_two_cards_card5"
    Given I create a card from a file "7_two_cards_card6"
    Given I create a card from a file "7_two_cards_card7"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/7_two_cards_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/7_two_cards_expected_response.xml" content
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/7_two_cards_request2.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/7_two_cards_expected_response2.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test search by adherant
    Given I create a declarant from a file "carteDemat/8_recherche_par_adherent_declarant1"
    Given I create a card from a file "8_recherche_par_adherent_card1"
    Given I create a card from a file "8_recherche_par_adherent_card2"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/8_recherche_par_adherent_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/8_recherche_par_adherent_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test search by contract
    Given I create a declarant from a file "carteDemat/9_recherche_par_contrat_declarant"
    Given I create a card from a file "9_recherche_par_contrat_card1"
    Given I create a card from a file "9_recherche_par_contrat_card2"
    When I send a soap version "1.1" to the endpoint "carteDematerialisee" request from file "carteDematerialisee/v1/9_recherche_par_contrat_request.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v1/9_recherche_par_contrat_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap @CarteDematV2
  Scenario: Call SOAP test search by contract for CarteDematV2
    Given I create a declarant from a file "carteDemat/8_recherche_par_adherent_declarant1"
    Given I create a card from a file "8_recherche_par_adherent_card1"
    Given I create a card from a file "8_recherche_par_adherent_card2"
    When I send a soap version "1.1" to the endpoint "carteDematerialiseeV2" request from file "carteDematerialisee/v2/8_recherche_par_adherent_requestV2.xml" with delta reference date 5
    Then the expected soap response is identical to "carteDematerialisee/v2/8_recherche_par_adherent_expected_response2.xml" content

    # limitation des periodes de fin de droits via paramétrage du déclarant
  @todosmokeTests @smokeTestsWithoutKafka @soap @CarteDematV2
  Scenario: Call SOAP test search by contract for CarteDematV2 with limitation
    Given I create a declarant from a file "carteDemat/8_recherche_par_adherent_declarant2"
    Given I create a card from a file "8_recherche_par_adherent_card3"
    When I send a soap version "1.1" to the endpoint "carteDematerialiseeV2" request from file "carteDematerialisee/v2/8_recherche_par_adherent_requestV2.xml" with reference date "2042-10-10"
    Then the expected soap response is identical to "carteDematerialisee/v2/8_recherche_par_adherent_expected_response3.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test search Carte TP
    Given I create a declarant from a file "consultationdroits/carteTP_test_declarant"
    Given I create a declaration from a file "consultationDrois/carteTP_test_declaration"
    When I send a soap version "1.1" to the endpoint "consultationDroits" request from file "consultationDroits/v1/carteTP_test_request.xml"
    Then the expected soap response is identical to "consultationDroits/v1/carteTP_test_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Test correct soap response for version 4 when requested periode is within periode droit
    #Periode Droit        Start|.................|END
    #Soap Request               Start|......|END
    Given I create a contrat from file "consultationdroits/soap_contrat"
    Given I create a declarant from a file "consultationdroits/soap_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/soapContrat_request.xml"
    Then the expected soap response is identical to "consultationDroits/v4/soapContrat_expectedResponseContrat.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Test correct soap response for version 4 when requested periode is covers start of periode droit
    #Periode Droit              Start|..........|END
    #Soap Request        Start|.........|END
    Given I create a contrat from file "soapContrat/contratStartsAfter"
    Given I create a declarant from a file "consultationdroits/soap_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/soapContrat_request.xml"
    Then the expected soap response is identical to "consultationDroits/v4/soapContrat_expectedResponseContratStartsAfter.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Test correct soap response for version 4 when requested periode covers end of periode droit
    #Periode Droit        Start|..............|END
    #Soap Request                     Start|.........|END
    Given I create a contrat from file "consultationdroits/soapContrat_contratEndsBefore"
    Given I create a declarant from a file "consultationdroits/soap_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/soapContrat_request.xml"
    Then the expected soap response is identical to "consultationDroits/v4/soapContrat_expectedResponseContratEndsBefore.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Test correct soap response for version 4 when requested periode touches two periode droits
    #Periode Droit        Start|..............|END  Start|..............|END
    #Soap Request                 Start|....................|END
    Given I create a contrat from file "consultationdroits/soapContrat_contratWithBreak"
    Given I create a declarant from a file "consultationdroits/soap_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/soapContrat_request.xml"
    Then the expected soap response is identical to "consultationDroits/v4/soapContrat_expectedResponseContratWithBreak.xml" content


  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call get info V3 with the correction for retroactive resiliation
    Given I create a declarant from a file "consultationdroits/retroactiveResiliation_declarant"
    Given I create a declaration from a file "consultationdroits/retroactiveResiliation_declaration1"
    Given I create a declaration from a file "consultationdroits/retroactiveResiliation_declaration2"
    Given I create a declaration from a file "consultationdroits/retroactiveResiliation_declaration3"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/retroactiveResiliation_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/retroactiveResiliation_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test beneficiaire multi contrat
    Given I create a declarant from a file "consultationdroits/18_benef_multi_contrat_declarant"
    Given I create a declaration from a file "consultationdroits/18_benef_multi_contrat_declaration1"
    Given I create a declaration from a file "consultationdroits/18_benef_multi_contrat_declaration2"
    Given I create a declaration from a file "consultationdroits/18_benef_multi_contrat_declaration3"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/18_benef_multi_contrat_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/18_benef_multi_contrat_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test with multidomaine
    Given I import a complete file for parametrage
    Given I create a declarant from a file "consultationdroits/droits_multidomaine_declarant"
    Given I create a declaration from a file "consultationdroits/droits_multidomaine_declaration"
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/droits_multidomaine_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/droits_multidomaine_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test with multidomaine priorise par parametrage
    Given I import a complete file for parametrage
    Given I create a declarant from a file "consultationdroits/droits_multidomaine_priorises_declarant"
    Given I create a declaration from a file "consultationdroits/droits_multidomaine_priorises_declaration"
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/droits_multidomaine_priorises_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/droits_multidomaine_priorises_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call SOAP test with multidomaine priorise par parametrage (inverse)
    Given I import a complete file for parametrage
    Given I create a declarant from a file "consultationdroits/droits_multidomaine_priorises_declarant"
    Given I create a declaration from a file "consultationdroits/droits_multidomaine_priorises_declaration_inversee"
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/droits_multidomaine_priorises_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/droits_multidomaine_priorises_expected_response_inversee.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @testCumul
  Scenario: Call SOAP test Nominal with cumul
    Given I import the file "cumulParametrage" for parametrage
    Given I create a declarant from a file "consultationdroits/19_cumul_declarant"
    Given I create a declaration from a file "consultationdroits/19_cumul_declaration_cumul"
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/19_cumul_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/19_cumul_expected_response.xml" content
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/19_cumul_request2.xml"
    Then the expected soap response is identical to "consultationDroits/v3/19_cumul_expected_response2.xml" content
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/19_cumul_request3.xml"
    Then the expected soap response is identical to "consultationDroits/v3/19_cumul_expected_response3.xml" content


  @todosmokeTests@sansEffet
  Scenario: Call SOAP test Nominal with sansEffet
    Given I import a complete file for parametrage
    Given I create a declarant from a file "consultationdroits/24_sansEffet_declarant"
    Given I create a declaration from a file "consultationdroits/24_sansEffet_declaration1"
    Given I create a declaration from a file "consultationdroits/24_sansEffet_declaration2"
    Given I create a declaration from a file "consultationdroits/24_sansEffet_declaration3"
    Given I create a declaration from a file "consultationdroits/24_sansEffet_declaration4"
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/24_sansEffet_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/24_sansEffet_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call get info V3 of a beneficiary having rights for the period indicated
    Given I create a declarant from a file "consultationdroits/25_droit_non_ouvert_6302_declarant"
    Given I create a declaration from a file "consultationdroits/25_droit_non_ouvert_6302_declaration1"
    Given I create a declaration from a file "consultationdroits/25_droit_non_ouvert_6302_declaration2"
    Given I create a declaration from a file "consultationdroits/25_droit_non_ouvert_6302_declaration3"
    Given I create a declaration from a file "consultationdroits/25_droit_non_ouvert_6302_declaration4"
    Given I create a declaration from a file "consultationdroits/25_droit_non_ouvert_6302_declaration5"
    When I send a soap version "1.2" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/25_droit_non_ouvert_6302_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/25_droit_non_ouvert_6302_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka
  Scenario: call V4/consultationDroits without numAdherent
    Given I import the file "cumulParametrage" for parametrage
    Given I create a declarant from a file "consultationdroits/20_num_adherent_absent_6005_declarant1"
    Given I create a declarant from a file "consultationdroits/20_num_adherent_absent_6005_declarant2"
    Given I create a declaration from a file "consultationdroits/20_num_adherent_absent_6005_declaration1"
    Given I create a declaration from a file "consultationdroits/20_num_adherent_absent_6005_declaration2"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/20_num_adherent_absent_6005_request.xml"
    Then the expected soap response is identical to "consultationDroits/v4/20_num_adherent_absent_6005_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @testGaranties
  Scenario: Test error 6002 on contract consultation
    Given I create a contrat from file "23_lower_warranty_error_contrat"
    Given I create a declarant from a file "consultationdroits/23_lower_warranty_error_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/23_lower_warranty_error_request.xml"
    Then the expected soap response is identical to "consultationDroits/v4/23_lower_warranty_error_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Test correct soap response for version 4 when requested periode is within periode droit but on two year
    #Periode Droit        Start|.................|END
    #Soap Request               Start|......|END
    Given I create a contrat from file "soap_contrat2"
    Given I create a declarant from a file "consultationdroits/soap_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/soapContrat_request2.xml"
    Then the expected soap response is identical to "consultationDroits/v4/soapContrat_expectedResponseContrat2.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Test correct soap response for version 4 when requested periode is within periode droit but with older benef (different numeroPersonne) with close rights
    #Periode Droit Benef 1                    Start|.................|END
    #Periode Droit Benef 2     Start|.....|END
    #Soap Request                                    Start|.....|END
    Given I create a contrat from file "soap_contrat3"
    Given I create a declarant from a file "consultationdroits/soap_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/soapContrat_request3.xml"
    Then the expected soap response is identical to "consultationDroits/v4/soapContrat_expectedResponseContrat3.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Test correct soap response for version 4 with periodeFin empty (online period)
    #Periode Droit Benef 1                    Start|.................|END
    #Periode Droit Benef 2     Start|.....|END
    #Soap Request                                    Start|.....|END
    Given I create a contrat from file "soap_contrat4"
    Given I create a declarant from a file "consultationdroits/soap_declarant"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV4" request from file "consultationDroits/v4/soapContrat_request3.xml"
    Then the expected soap response is identical to "consultationDroits/v4/soapContrat_expectedResponseContrat4.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call get info V3 with the declaration not closed properly
    Given I create a declarant from a file "consultationdroits/soapFermetureRate_declarant"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration1"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration4"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration5"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration6"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration7"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration8"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration9"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration10"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration11"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration12"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration13"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration14"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration15"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration16"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration17"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration18"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/soapFermetureRate_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/soapFermetureRate_expected_response.xml" content

  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call get info V3 on inexistent domain and receive error 6003
    Given I create a declarant from a file "consultationdroits/soapFermetureRate_declarant"
    Given I create a declaration from a file "consultationdroits/soapFermetureRateDeclaration1"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/soapContrat_requestDomaineInexistant.xml"
    Then the expected soap response is identical to "consultationDroits/v3/soapContrat_expectedResponseDomaineInexistant.xml" content


  @todosmokeTests @smokeTestsWithoutKafka @soap
  Scenario: Call get info V3 with declaration with rights on several years
    Given I create a declarant from a file "consultationdroits/25_droit_non_ouvert_6302_declarant"
    Given I create a declaration from a file "consultationdroits/declarationsConsultDroitsV3_6610_declarationV"
    Given I create a declaration from a file "consultationdroits/declarationsConsultDroitsV3_6610_declarationR"
    When I send a soap version "1.1" to the endpoint "consultationDroitsV3" request from file "consultationDroits/v3/declarationsConsultDroitsV3_6610_request.xml"
    Then the expected soap response is identical to "consultationDroits/v3/declarationsConsultDroitsV3_6610_response.xml" content
