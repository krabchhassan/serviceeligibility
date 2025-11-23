Feature: Test extraction carte

  @smokeTests @batch620 @extractionCarte
  Scenario: Test extraction d'une carte 2024
    Given I create a declarant from a file "declarantWithCartePapier"
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPTI     |
      | libelle | Opticien |
    Then I create a declaration from a file "batch620/declaration2024"
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-CONSOW0 |
      | isLastCarteDemat | true               |
      | periodeDebut     | 2024/01/01         |
      | periodeFin       | 2024/12/31         |
    Then I wait for 1 cartes papier
    And the carte papier at index 0 has these values
      | numeroAMC                    | 0000452433       |
      | nomAMC                       | AMC              |
      | libelleAMC                   | KLESIA - CARCEPT |
      | periodeDebut                 | 2024/01/01       |
      | periodeFin                   | 2024/12/31       |
      | dateTraitement               | 2024-01-01       |
      | codeConvention               | IS               |
      | libelleConvention            | iSante           |
      | domaineConventionLibelle     | Opticien         |
      | domaineConventionLibelleConv | iSante           |

  @smokeTests @batch620 @extractionCarte
  Scenario: Test extraction de deux cartes
    Given I create a declarant from a file "declarantWithCartePapier"
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPTI     |
      | libelle | Opticien |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | AMM                                 |
      | libelle | Auxiliaire Masseur Kinésithérapeute |
    Then I create a declaration from a file "batch620/declaration2024"
    Then I create a declaration from a file "batch620/declaration2025"
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 2 cartes papier
    And the carte papier at index 0 has these values
      | numeroAMC                    | 0000452433       |
      | nomAMC                       | AMC              |
      | libelleAMC                   | KLESIA - CARCEPT |
      | periodeDebut                 | 2024/01/01       |
      | periodeFin                   | 2024/12/31       |
      | dateTraitement               | 2024-01-01       |
      | codeConvention               | IS               |
      | libelleConvention            | iSante           |
      | domaineConventionLibelle     | Opticien         |
      | domaineConventionLibelleConv | iSante           |
    And the carte papier at index 1 has these values
      | numeroAMC                    | 0000452433                          |
      | nomAMC                       | AMC                                 |
      | libelleAMC                   | KLESIA - CARCEPT                    |
      | periodeDebut                 | 2025/01/01                          |
      | periodeFin                   | 2025/12/31                          |
      | dateTraitement               | 2024-01-01                          |
      | codeConvention               | IS                                  |
      | libelleConvention            | iSante                              |
      | domaineConventionLibelle     | Auxiliaire Masseur Kinésithérapeute |
      | domaineConventionLibelleConv | iSante                              |

  @smokeTests @batch620 @extractionCarte
  Scenario: Test pas de demande de carte papier
    Given I create a declarant from a file "declarantWithCartePapier"
    Then I create a declaration from a file "batch620/declaration2024-sans-cartePapier"
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-CONSOW0 |
      | isLastCarteDemat | true               |
      | periodeDebut     | 2024/01/01         |
      | periodeFin       | 2024/12/31         |
    Then I wait for 0 cartes papier

  @smokeTests @batch620 @extractionCarte
  Scenario: Test pas de demande de carte demat mais demande de carte papier
    Given I create a declarant from a file "declarantWithCartePapier"
    Then I create a declaration from a file "batch620/declaration2024-sans-carteDigitale"
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Given I try to create a parameter for type "domaine" in version "V2" with parameters
      | code    | OPTI     |
      | libelle | Opticien |
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then I wait for 1 cartes papier
    And the carte papier at index 0 has these values
      | numeroAMC                    | 0000452433       |
      | nomAMC                       | AMC              |
      | libelleAMC                   | KLESIA - CARCEPT |
      | periodeDebut                 | 2024/01/01       |
      | periodeFin                   | 2024/12/31       |
      | dateTraitement               | 2024-01-01       |
      | codeConvention               | IS               |
      | libelleConvention            | iSante           |
      | domaineConventionLibelle     | Opticien         |
      | domaineConventionLibelleConv | iSante           |
      | contexte                     | Q                |

  @smokeTests @batch620 @extractionCarte
  Scenario: Test creation carte papier avec declaration v2 avec 1 domaine droit editable et 1 non editable
    Given I create a declarant from a file "declarantWithCartePapier"
    Then I create a declaration from a file "batch620/declaration-v2-domaineEditable"
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    Then there are 1 domainesConventions on the card at index 0 has this values
      | code | rang |
      | OPAU | 0    |
    And the card for the benef has 1 domainesCouverture
      | code | tauxRemboursement |
      | OPAU | PEC               |
    And the card for the benef has 1 domainesRegroup
      | code | taux |
      | OPAU | PEC  |
    Then I wait for 1 cartes papier
    And the carte papier at index 0 has these values
      | numeroAMC      | 0000452433       |
      | nomAMC         | AMC              |
      | libelleAMC     | KLESIA - CARCEPT |
      | periodeDebut   | 2024/01/01       |
      | periodeFin     | 2024/12/31       |
      | dateTraitement | 2024-01-01       |
      | contexte       | A                |
    Then there are 1 domainesConventions on the carte papier at index 0 has this values
      | code | rang |
      | OPAU | 0    |
    And the carte papier for the benef has 1 domainesCouverture
      | code | tauxRemboursement |
      | OPAU | PEC               |

  @smokeTests @batch620 @validationCarte
  Scenario: Test creation carte papier avec declaration sans versionDeclaration et domaine droit non editable
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-sansVersion-sansDomaineEditable"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 cards
    Then I wait for 0 cartes papier

  @smokeTests @batch620 @validationCarte
  Scenario: Test creation carte papier avec declaration v3.1 mais domaines droit non editables
    Given I create a declarant from a file "declarant_5497"
    And I create a declaration from a file "batch620/declaration-sansDomainesEditables"
    And I process declarations for carteDemat the "2024-01-01"
    Then I wait for 0 cards
    Then I wait for 0 cartes papier

  @smokeTests @batch620 @extractionCarte
  Scenario: Test extraction d'une carte 2024 avec verification des tri des domaines TP
    Given I create a declarant from a file "declarantWithCartePapier"
    Then I create a declaration from a file "batch620/declaration-domainsWithDiffNoOrdre"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    Then I process declarations for carteDemat the "2024-01-01"
    Then I wait for 1 cards
    Then the card at index 0 has this values
      | AMC_contrat      | 0000452433-02826911 |
      | isLastCarteDemat | true                |
      | periodeDebut     | 2024/01/01          |
      | periodeFin       | 2024/12/31          |
    Then there are 3 domainesConventions on the card at index 0 has this values
      | code | rang |
      | MEDE | 0    |
      | HOSP | 1    |
      | OPAU | 2    |
    And the card for the benef has 3 domainesCouverture
      | code | noOrdreDroit |
      | MEDE | 0            |
      | HOSP | 1            |
      | OPAU | 2            |
    And the card for the benef has 3 domainesRegroup
      | code | rang |
      | MEDE | 0    |
      | HOSP | 1    |
      | OPAU | 2    |
    Then I wait for 1 cartes papier
    And the carte papier at index 0 has these values
      | numeroAMC         | 0000452433       |
      | nomAMC            | AMC              |
      | libelleAMC        | KLESIA - CARCEPT |
      | periodeDebut      | 2024/01/01       |
      | periodeFin        | 2024/12/31       |
      | dateTraitement    | 2024-01-01       |
      | codeConvention    | IS               |
      | libelleConvention | iSante           |
    And the carte papier for the benef has 3 domainesCouverture
      | code | noOrdreDroit |
      | MEDE | 0            |
      | HOSP | 1            |
      | OPAU | 2            |
    Then there are 3 domainesConventions on the carte papier at index 0 has this values
      | code | rang |
      | MEDE | 0    |
      | HOSP | 1    |
      | OPAU | 2    |
