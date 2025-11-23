Feature: Test invalidation carte batch 620


  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas renouvellement n+1
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create an automatic TP card parameters on next year from file "parametrageBalooGenerique"
    And I create a card from a file "10_invalidation620/carteDemat2024"
    And I create a card from a file "10_invalidation620/carteDemat2025"
    And I create a card from a file "10_invalidation620/carteDemat2026"
    When I send a test contract from file "5726-Contrat1"
    Then I wait for 1 declarations
    Then The declaration has this adresse
      | ligne1              | ligne2                | ligne3 | ligne4  | ligne5 | ligne6 | ligne7  | codePostal |
      | 18 RUE DES LAGARDES | RESIDENCE DES COTEAUX | 59250  | HALLUIN | F      | FRANCE | CEDEX41 | 5920       |
    When I renew the rights today with mode "NO_RDO"
    Then I wait for the last renewal trigger with contract number "CONSOW0" and amc "0000401166" to be "Processed"
    Then I wait for 2 declarations
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "contrattp-invalidation620" content
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2024"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2025"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2026"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas regeneration droit sans changement de garantie
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I create a card from a file "10_invalidation620/carteDemat2024"
    And I create a card from a file "10_invalidation620/carteDemat2025"
    And I create a card from a file "10_invalidation620/carteDemat2026"
    When I send a test contract from file "5726-Contrat2"
    Then I wait for 1 declarations
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2024"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2025"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2026"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation cas changement de garantie
    Given I create a contract element from a file "gtaxa"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create a contract element from a file "gtaxa_cgdiv-5726"
    And I create TP card parameters from file "parametrageBalooGenerique"
    And I create a card from a file "10_invalidation620/carteDemat2024"
    And I create a card from a file "10_invalidation620/carteDemat2025"
    And I create a card from a file "10_invalidation620/carteDemat2026"
    And I create a card from a file "10_invalidation620/carteDemat2023"
    When I send a test contract from file "5726-Contrat3_changement_garantie"
    Then I wait for 1 declarations
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2023"
    Then The value of the field isLastCarteDemat is "true"
    Then I get card with id "CARTEDEMAT2024"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2025"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2026"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas changement de garantie en 2023, 2 cartes invalides
    Given I create a card from a file "10_invalidation620/carteDemat2023"
    And I create a card from a file "10_invalidation620/carteDemat2024"
    When I create a declaration from a file "batch620/declaration2023_changement_garantie"
    Then I wait for 1 declarations
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2023"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2024"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas changement de garantie en 2024, carte 2024 invalide
    Given I create a card from a file "10_invalidation620/carteDemat2023"
    And I create a card from a file "10_invalidation620/carteDemat2024"
    When I create a declaration from a file "batch620/declaration2024_changement_garantie"
    Then I wait for 1 declarations
    Then I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2023"
    Then The value of the field isLastCarteDemat is "true"
    Then I get card with id "CARTEDEMAT2024"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas regeneration droits 2023, carte 2023 invalide
    And I create a card from a file "10_invalidation620/carteDemat2024"
    And I create a card from a file "10_invalidation620/carteDemat2023"
    When I create a declaration from a file "batch620/declaration2023"
    Then I wait for 1 declarations
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2023"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2024"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas anticipe 1
    And I create a card from a file "10_invalidation620/carteDemat2023a"
    And I create a card from a file "10_invalidation620/carteDemat2023b"
    When I create a declaration from a file "batch620/declaration2023_changement_garantie_a"
    Then I wait for 1 declarations
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2023A"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2023B"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas anticipe 2
    And I create a card from a file "10_invalidation620/carteDemat2023a"
    And I create a card from a file "10_invalidation620/carteDemat2023b"
    When I create a declaration from a file "batch620/declaration2023_changement_garantie_b"
    Then I wait for 1 declarations
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2023A"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMAT2023B"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas anticipe 3
    And I create a card from a file "10_invalidation620/carteDemat2023a"
    And I create a card from a file "10_invalidation620/carteDemat2023b"
    When I create a declaration from a file "batch620/declaration2023_changement_garantie_c"
    Then I wait for 1 declarations
    And I invalid cards with amc "0000401166" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMAT2023A"
    Then The value of the field isLastCarteDemat is "true"
    Then I get card with id "CARTEDEMAT2023B"
    Then The value of the field isLastCarteDemat is "false"

  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : cas changement de nom
    And I create a card from a file "10_invalidation620/carteDematCas5-1"
    And I create a card from a file "10_invalidation620/carteDematCas5-2"
    When I create a declaration from a file "batch620/declarationCas5-2"
    Then I wait for 1 declarations
    And I invalid cards with amc "0097810998" and contractNumber "CONSOW0"
    Then I get card with id "CARTEDEMATCAS51"
    Then The value of the field isLastCarteDemat is "false"
    Then I get card with id "CARTEDEMATCAS52"
    Then The value of the field isLastCarteDemat is "false"


  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : resiliation n+1, carte demat invalide n/n+1
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPAU     |
      | libelle | Opticien |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | HOSP    |
      | libelle | Hopital |
    And I create a declarant from a file "declarantbalooDematPapier"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I create a contract element from a file "gt10"
    When I send a contract from file "batch620/invalidationsComplexes/oneBenef" to version "V6"
    And I wait "5" seconds in order to consume the data
    And I wait for 2 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    And I wait for 2 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    When I send a contract from file "batch620/invalidationsComplexes/oneBenefResiliation2026" to version "V6"
    And I wait "5" seconds in order to consume the data
    Then I wait for 4 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-03"
    And I wait for 4 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | false                  |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | false               |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    Then the card at index 2 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 3 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/20 |


  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : radiation 1 benef n+1, carte demat invalide n/n+1
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPAU     |
      | libelle | Opticien |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | HOSP    |
      | libelle | Hopital |
    And I create a declarant from a file "declarantbalooDematPapier"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I create a contract element from a file "gt10"
    When I send a contract from file "batch620/invalidationsComplexes/twoBenefs" to version "V6"
    And I wait "5" seconds in order to consume the data
    Then I wait for 4 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-12-02"
    And I wait for 2 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    When I send a contract from file "batch620/invalidationsComplexes/twoBenefRadiationOneBenef2026" to version "V6"
    And I wait "5" seconds in order to consume the data
    Then I wait for 8 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-12-03"
    And I wait for 5 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | false                  |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | false               |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    Then the card at index 2 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 3 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/20 |
    Then the card at index 4 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/12/21 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |


  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : radiation 1 benef année en cours, INV03
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPAU     |
      | libelle | Opticien |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | HOSP    |
      | libelle | Hopital |
    And I create a declarant from a file "declarantbalooDematPapier"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I create a contract element from a file "gt10"
    When I send a contract from file "batch620/invalidationsComplexes/twoBenefs" to version "V6"
    And I wait "5" seconds in order to consume the data
    And I wait for 4 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    And I wait for 2 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    When I send a contract from file "batch620/invalidationsComplexes/twoBenefRadiationOneBenef2025" to version "V6"
    And I wait "5" seconds in order to consume the data
    Then I wait for 10 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-12-03"
    And I wait for 5 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | false                  |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | false               |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    Then the card at index 2 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/20 |
    Then the card at index 3 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/12/21 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 4 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |


  @smokeTests @batch620 @invalidationCarte
  Scenario: Test invalidation : resiliation année en cours, carte demat invalide n/n+1 INV03
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPAU     |
      | libelle | Opticien |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | HOSP    |
      | libelle | Hopital |
    And I create a declarant from a file "declarantbalooDematPapier"
    And I create an automatic TP card parameters on next year from file "parametrageBalooGeneriqueDematPapier"
    And I create a contract element from a file "gt10"
    When I send a contract from file "batch620/invalidationsComplexes/oneBenef" to version "V6"
    And I wait "5" seconds in order to consume the data
    And I wait for 2 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    And I wait for 2 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | true                |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    When I send a contract from file "batch620/invalidationsComplexes/oneBenefResiliation2025" to version "V6"
    And I wait "5" seconds in order to consume the data
    Then I wait for 6 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-02"
    And I wait for 3 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | false                  |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/31 |
    Then the card at index 1 has this values
      | AMC_contrat      | 0000401166-5894-01  |
      | isLastCarteDemat | false               |
      | periodeDebut     | %%NEXT_YEAR%%/01/01 |
      | periodeFin       | %%NEXT_YEAR%%/12/31 |
    Then the card at index 2 has this values
      | AMC_contrat      | 0000401166-5894-01     |
      | isLastCarteDemat | true                   |
      | periodeDebut     | %%CURRENT_YEAR%%/01/01 |
      | periodeFin       | %%CURRENT_YEAR%%/12/20 |


  @smokeTests @batch620 @invalidationCarte @toChangeEveryBirthday
    # si test qui plante en début d'année, c'est normal, il faut reprendre l'historique du fichier pour remettre en commentaire
  Scenario: Test invalidation : anniversaire contrat INV04-1 && INV04-02
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPAU     |
      | libelle | Opticien |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | HOSP    |
      | libelle | Hopital |
    And I create a declarant from a file "declarantbalooDematPapier"
    And I create an automatic TP card parameters from file "parametrageBalooGeneriqueAnniversairePapierDemat"
    And I create a contract element from a file "gt10"
    When I send a contract from file "batch620/invalidationsComplexes/oneBenefAnniv" to version "V6"
    And I wait "5" seconds in order to consume the data
    And I wait for 1 declaration
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-01-01"
    And I wait for 1 card
    Then On the birthday "06/01" the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01 |
      | isLastCarteDemat | true               |
      | periodeDebut     | startBirthday      |
      | periodeFin       | endBirthday        |
    When I create a trigger by UI from "trigger_by_ui_anniv_papier_demat"
    And I wait for 2 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-06-01"
    And I wait for 2 cards
    Then On the birthday "06/01" the card at index 0 has this values
      | AMC_contrat      | 0000401166-5894-01 |
      | isLastCarteDemat | false              |
      | periodeDebut     | startBirthday      |
      | periodeFin       | endBirthday        |
#    Then On the birthday "06/01" the card at index 1 has this values
#      | AMC_contrat      | 0000401166-5894-01 |
#      | isLastCarteDemat | true               |
#      | periodeDebut     | startBirthday      |
#      | periodeFin       | endBirthday        |
    When I send a contract from file "batch620/invalidationsComplexes/oneBenefAnnivResil" to version "V6"
    And I wait for 4 declarations
    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-06-02"
    When I create a trigger by UI from "trigger_by_ui_anniv_papier_demat"
#    And I wait for 7 declarations
#    And I process declarations for carteDemat the "%%CURRENT_YEAR%%-06-03"
#    And I wait for 2 cards
#    Then On the birthday "06/01" the card at index 0 has this values
#      | AMC_contrat      | 0000401166-5894-01 |
#      | isLastCarteDemat | false              |
#      | periodeDebut     | startBirthday      |
#      | periodeFin       | endBirthday        |
#    Then On the birthday "06/01" the card at index 1 has this values
#      | AMC_contrat      | 0000401166-5894-01 |
#      | isLastCarteDemat | true               |
#      | periodeDebut     | startBirthday      |
#      | periodeFin       | endBirthday        |
#    Then On the birthday "06/01" the card at index 2 has this values
#      | AMC_contrat      | 0000401166-5894-01 |
#      | isLastCarteDemat | true               |
#      | periodeDebut     | startBirthday      |
#      | periodeFin       | %%CURRENT_YEAR%%/05/04         |
#    Then On the birthday "06/01" the card at index 3 has this values
#      | AMC_contrat      | 0000401166-5894-01 |
#      | isLastCarteDemat | true               |
#      | periodeDebut     | startBirthday      |
#      | periodeFin       | %%CURRENT_YEAR%%/05/04         |
