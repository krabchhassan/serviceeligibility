Feature: Test contract V5 avec la nouvelle gestion des codes renvois

  Background:
    And I create TP card parameters from file "parametrageTPBaloo_5458"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT/IS |
      | libelle | IT/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IT |
      | libelle | IT |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | CB |
      | libelle | CB |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | OP/CB |
      | libelle | OP/CB |
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | OP |
      | libelle | OP |
    And I create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | Amc_opau_code_r      |
      | libelle | Code renvoi opau AMC |
    And I try to create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | Amc_hosp_code_r      |
      | libelle | Code renvoi hosp AMC |
    And I try to create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | Amc_phar_code_r      |
      | libelle | Code renvoi phar AMC |
    And I try to create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | Amc_dent_code_r      |
      | libelle | Code renvoi dent AMC |
    And I try to create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | Detail_hosp_code_r               |
      | libelle | Code renvoi hosp details domaine |
    And I try to create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | Detail_dent_code_r               |
      | libelle | Code renvoi dent details domaine |
    And I try to create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | Entete_code_r              |
      | libelle | Code renvoi entete domaine |

  @smokeTests @NewParamCodeRenvoi
  Scenario: I send a contract with code renvoi (without reseau de soin from PW)
    And I create a contract element from a file "gtaxa_5458"
    And I create a declarant from a file "declarantbaloo_5458"
    When I send a test contract from file "contratV5/5458"
    When I get triggers with contract number "93000808" and amc "0000401166"
    Then I wait for 1 declaration

    # Pas de réseau de soin retourné par PW => on ne prend pas en compte le paramétrage
    Then The right number 0 has codeRenvoi "null" and label codeRenvoi "null"

    # Pas de réseau de soin retourné par PW => HOSP est en COMPLETER, on positionne le code renvoi du paramétrageCarteTP et on ne positionne pas de codeRenvoi additionnel
    Then The right number 1 has codeRenvoi "Detail_hosp_code_r" and label codeRenvoi "Code renvoi hosp details domaine"
    Then The right number 1 has codeRenvoi additional "null" and label codeRenvoi "null"

    # PHAR est en INHIBER => pas de codeRenvoi
    Then The right number 2 has codeRenvoi "null" and label codeRenvoi "null"

    # DENT est en REMPLACER => on positionne le codeRenvoi du paramétrageCarteTP
    Then The right number 3 has codeRenvoi "Detail_dent_code_r" and label codeRenvoi "Code renvoi dent details domaine"

    And I wait for 1 contract
    Then the expected contract TP is identical to "5510/contrat1" content

  @smokeTests @NewParamCodeRenvoi
  Scenario: I send a contract with code renvoi (with reseau de soin from PW)
    And I create a contract element from a file "gtaxa_5458_ReseauSoin"
    And I create a declarant from a file "declarantbaloo_5458"
    When I send a test contract from file "contratV5/5458"
    When I get triggers with contract number "93000808" and amc "0000401166"
    Then I wait for 1 declarations

    # Réseau de soin "OP" retourné par PW => OPAU est en GARDER et le seul paramétrage de l'AMC pour OPAU indique le réseau de soin "OP"
    # On positionne le codeRenvoi défini dans le param de l'AMC
    Then The right number 0 has codeRenvoi "Amc_opau_code_r" and label codeRenvoi "Code renvoi opau AMC"

    # Réseau de soin "OP" retourné par PW => HOSP est en COMPLETER et le seul paramétrage de l'AMC pour HOSP indique le réseau de soin "OP"
    # On positionne le codeRenvoi défini dans le param de l'AMC
    Then The right number 1 has codeRenvoi "Amc_hosp_code_r" and label codeRenvoi "Code renvoi hosp AMC"
    Then The right number 1 has codeRenvoi additional "Detail_hosp_code_r" and label codeRenvoi "Code renvoi hosp details domaine"

    # PHAR est en INHIBER => pas de codeRenvoi
    Then The right number 2 has codeRenvoi "null" and label codeRenvoi "null"

    # DENT est en REMPLACER => on positionne le codeRenvoi du paramétrageCarteTP
    Then The right number 3 has codeRenvoi "Detail_dent_code_r" and label codeRenvoi "Code renvoi dent details domaine"

    And I wait for 1 contract
    Then the expected contract TP is identical to "5510/contrat2" content

  @smokeTests @NewParamCodeRenvoi
  Scenario: I send a contract with code renvoi (without reseau de soin from PW)
    And I create a contract element from a file "gtaxa_5458"
    And I create a declarant from a file "declarantbaloo_5458_withAndWithoutReseauDeSoin"
    And I create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | opau_code_renvoi_generique     |
      | libelle | Code renvoi opau AMC generique |
    And I create a parameter for type "codesRenvoi" in version "V2" with parameters
      | code    | hosp_code_renvoi_generique     |
      | libelle | Code renvoi hosp AMC generique |
    When I send a test contract from file "contratV5/5458"
    When I get triggers with contract number "93000808" and amc "0000401166"
    Then I wait for 1 declarations

    # Pas de réseau de soin retourné par PW => => on ne prend pas en compte le paramétrage
    Then The right number 0 has codeRenvoi "null" and label codeRenvoi "null"

    # Pas de réseau de soin retourné par PW => HOSP est en COMPLETER, on positionne le code renvoi du paramétrageCarteTP et on ne positionne pas de codeRenvoi additionnel
    Then The right number 1 has codeRenvoi "Detail_hosp_code_r" and label codeRenvoi "Code renvoi hosp details domaine"

    # PHAR est en INHIBER => pas de codeRenvoi
    Then The right number 2 has codeRenvoi "null" and label codeRenvoi "null"

    # DENT est en REMPLACER => on positionne le codeRenvoi du paramétrageCarteTP
    Then The right number 3 has codeRenvoi "Detail_dent_code_r" and label codeRenvoi "Code renvoi dent details domaine"

    And I wait for 1 contract
    Then the expected contract TP is identical to "5510/contrat3" content
