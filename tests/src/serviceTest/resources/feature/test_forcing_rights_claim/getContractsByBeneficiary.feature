Feature: Get Contracts By Beneficiary
  Parametres obligatoires lors de l appel au WS :
  - beneficiaryId : concaténation de l'idDeclarant et du n° personne (séparé par un tiret)
  - context : HTP, TP_OFFLINE, TP_ONLINE
  - contractList: liste des contrats (liste composé du n° adhérent et du n° de contrat)
  - nir : code du nir

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @smokeTests @contractsByBeneficiary @release
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test1
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 2 declaration
    Then I wait for 1 contract
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contractsByBeneficiary/servicePrestation2"
    And I wait for 3 declaration
    Then I wait for 2 contracts

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    # Same call but on a different nir (nir principal)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1041062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    # Same call but on a different nir (nir affiliationsRO)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1791062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    # Same call but on a different nir (nir principal)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    # Same call but on a different nir (nir affiliationsRO)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1791062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
      | MBA0004      | MBA0004        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    # Same call but on a different nir (nir principal)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    # Same call but on a different nir (nir affiliationsRO)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1791062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test2
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 2 declaration
    Then I wait for 1 contract
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contractsByBeneficiary/servicePrestation2"
    When I send a test contract from file "contractsByBeneficiary/servicePrestation2Radiation"
    And I wait for 4 declarations
    Then I wait for 2 contracts

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    Then the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |


    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
      | MBA0004      | MBA0004        |
      | MBA0005      | MBA0005        |
    Then the result in contractright has 2 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
      | MBA0004      | MBA0004        |
      | MBA0005      | MBA0005        |
    Then the result in contractright has 2 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test3
    And I create a contract element from a file "gtbasebalooCase1B"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contractsByBeneficiary/send/servicePrestation1"
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contractsByBeneficiary/servicePrestation2"
    And I wait for 1 declaration
    Then I wait for 1 contract

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2024-03-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
      | MBA0004      | MBA0004        |
    Then the result in contractright has 2 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
    Then the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
    Then the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test4 - Sans effet assure
    And I create a contract element from a file "gtbasebalooCase1B"
    Given I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I create a beneficiaire from file "contractsByBeneficiary/benef1"
    And I create a beneficiaire from file "contractsByBeneficiary/benef2"
    When I send a test contract from file "contractsByBeneficiary/servicePrestation2avec2benefs"
    And I wait for 2 declaration
    Then I wait for 1 contract
    # Sans effet assuré
    Then I send a test contract from file "contractsByBeneficiary/servicePrestation2"
    # une V et une R (pour l'assuré qui n'existe plus)
    And I wait for 4 declarations
    Then I get contracts for beneficiaryID "0000401166-MBA0008-002" and context "HTP"
    Then an error "404" is returned with message "Pas de contrat trouvé"

    Then I get contracts for beneficiaryID "0000401166-MBA0008-002" and context "TP_ONLINE"
    Then an error "404" is returned with message "Pas de contrat trouvé"

    Then I get contracts for beneficiaryID "0000401166-MBA0008-002" and context "TP_OFFLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 2041062498044 |
      | nir.key              | 84            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0008-002", context "TP_OFFLINE", nir "2041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
    Then the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test4 - Sans effet contrat
    And I create a contract element from a file "gtbasebalooCase1B"
    Given I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I create a beneficiaire from file "contractsByBeneficiary/benef1"
    Then I send a test contract from file "contractsByBeneficiary/servicePrestation2"
    And I wait for 1 declaration
    Then I wait for 1 contract
    # Sans effet contrat
    Then I delete a contract "MBA0004" for amc "0000401166"
    And I wait for 2 declaration
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    Then an error "404" is returned with message "Pas de contrat trouvé"
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    Then an error "404" is returned with message "Pas de contrat trouvé"
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test5
    And I create a contract element from a file "gtbasebalooCase1"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 2 declaration
    Then I wait for 1 contract
    And I create TP card parameters from file "parametrageTPBaloo"
    Then I send a test contract from file "contractsByBeneficiary/servicePrestation1GTannul"
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    Then an error "404" is returned with message "Pas de contrat trouvé"

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    Then an error "404" is returned with message "Pas de contrat trouvé"

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test6
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1-2GTs"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 3 declaration
    Then I wait for 1 contract
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test7
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1-2GTsNonContigues"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 3 declaration
    Then I wait for 1 contract
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2025-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2024-11-30    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2025-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2024-11-30    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2025-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2024-11-30    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test7Bis
    And I create a contrat from file "contractsByBeneficiary/contratTP"
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test8
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1-2GTsSuperposees"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 3 declaration
    Then I wait for 1 contract
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
    And the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-6946 : Test9 - Tri sur nir avec affiliations annulés
    And I create a contrat from file "contractsByBeneficiary/contratTP"
    And I create a contrat from file "contractsByBeneficiary/contratTPAutreNir"
    And I create a contrat from file "contractsByBeneficiary/contratTPAutreNir2"
    And I create a contrat from file "contractsByBeneficiary/contratTPAutreNir2AffiliationAnnule"
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_OFFLINE"
    And the result has 4 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0005       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 2 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0006       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 3 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0007       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-12-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1041062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
      | MBA0003      | MBA0005        |
      | MBA0003      | MBA0006        |
      | MBA0003      | MBA0007        |
    And the result in contractright has 4 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0005    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 2 has values
      | insurerId    | 0000401166 |
      | number       | MBA0006    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 3 has values
      | insurerId    | 0000401166 |
      | number       | MBA0007    |
      | subscriberId | MBA0003    |

    # Same call but on a different nir (affiliationRO)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1791062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
      | MBA0003      | MBA0005        |
      | MBA0003      | MBA0006        |
      | MBA0003      | MBA0007        |
    And the result in contractright has 4 contracts
    # contract with the nir 1791062498045
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0005    |
      | subscriberId | MBA0003    |
    # rest of contracts
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 2 has values
      | insurerId    | 0000401166 |
      | number       | MBA0006    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 3 has values
      | insurerId    | 0000401166 |
      | number       | MBA0007    |
      | subscriberId | MBA0003    |

    # Same call but on a different nir (affiliationRO)
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_OFFLINE", nir "1791062498046" and contract list
      | subscriberId | contractNumber |
      | MBA0003      | MBA0003        |
      | MBA0003      | MBA0005        |
      | MBA0003      | MBA0006        |
      | MBA0003      | MBA0007        |
    And the result has 4 contracts
    # contract MBA0006 and MBA0007 have the nir 1791062498046, but MBA0006 have an invalid affiliationsRO
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0007    |
      | subscriberId | MBA0003    |
    # rest of contracts
    And the result in contractright with indice 1 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |
    And the result in contractright with indice 2 has values
      | insurerId    | 0000401166 |
      | number       | MBA0005    |
      | subscriberId | MBA0003    |
    # the contract MBA0006 is the last one returned cause the nir 1791062498046 is related to an invalid affiliationsRO
    And the result in contractright with indice 3 has values
      | insurerId    | 0000401166 |
      | number       | MBA0006    |
      | subscriberId | MBA0003    |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - BLUE-7106
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 2 declaration
    Then I wait for 1 contract
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contractsByBeneficiary/servicePrestation3"
    And I wait for 4 declaration
    Then I wait for 1 contract

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2024-03-31    |
      | nir.code             | 1791062498044 |
      | nir.key              | 48            |
      | subscriber.lastname  | null          |
      | subscriber.firstname | null          |
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "HTP", nir "1791062498044" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0003      | MBA0003        |
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0003    |
      | subscriberId | MBA0003    |


  @smokeTests @contractsByBeneficiary
  Scenario: BLUE-7205 Cas 3
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1-2GTsSuperposeesRadie"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 3 declarations
    Then I wait for 1 contract
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2025-06-30    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |


  @smokeTests @contractsByBeneficiary
  Scenario: BLUE-7205 Cas 4
    And I create a contract element from a file "gtbasebalooCase1"
    And I create a contract element from a file "gtbasebalooCase1B"
    And I create manual TP card parameters from file "parametrageCarteTPManuel2024"
    Given I create a declarant from a file "declarantbaloo"
    When I create a service prestation from a file "contractsByBeneficiary/servicePrestation1-2GTsNonContiguesRadie"
    And I renew the rights today with mode "RDO"
    And I wait for 1 declaration
    And I create manual TP card parameters from file "parametrageCarteTPManuel2025"
    And I renew the rights today with mode "RDO"
    And I wait for 3 declarations
    Then I wait for 1 contract
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "HTP"
    And the result has 2 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2025-01-01    |
      | period.end           | 2025-06-30    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    And the result with indice 1 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0003       |
      | subscriberId         | MBA0003       |
      | issuingCompanyCode   | 000ER00212    |
      | period.start         | 2024-01-01    |
      | period.end           | 2024-03-31    |
      | nir.code             | 1041062498044 |
      | nir.key              | 37            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |

  @smokeTests @contractsByBeneficiary
  Scenario: Get Contract by beneficiary - Call with the same contract twice
    And I create a contract element from a file "gtbasebalooCase1B"
    Given I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a test contract from file "contractsByBeneficiary/servicePrestation2"
    And I wait for 1 declaration
    Then I wait for 1 contract

    Then I get contracts for beneficiaryID "0000401166-MBA0003-002" and context "TP_ONLINE"
    And the result has 1 contracts
    And the result with indice 0 has values
      | insurerId            | 0000401166    |
      | contractNumber       | MBA0004       |
      | subscriberId         | MBA0004       |
      | issuingCompanyCode   | 000ER00693    |
      | period.start         | 2025-01-01    |
      | period.end           | null          |
      | nir.code             | 1041062498045 |
      | nir.key              | 36            |
      | subscriber.lastname  | LOLO          |
      | subscriber.firstname | LUCIEN        |
    # Envoi appel de la requete avec 2 fois le meme contrat => doit renvoyer le contrat dans la réponse 1 fois
    Then I get contracts for beneficiaryID "0000401166-MBA0003-002", context "TP_ONLINE", nir "1041062498045" and contract list
      | subscriberId | contractNumber |
      | MBA0004      | MBA0004        |
      | MBA0004      | MBA0004        |
    Then the result in contractright has 1 contracts
    And the result in contractright with indice 0 has values
      | insurerId    | 0000401166 |
      | number       | MBA0004    |
      | subscriberId | MBA0004    |
