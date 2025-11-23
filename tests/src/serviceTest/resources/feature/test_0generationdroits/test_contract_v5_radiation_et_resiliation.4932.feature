Feature: Test contrat V5 with resiliation and radiation

  # Contrat résilié avec 5 assurés ayant des dates de radiation différentes : contrat inexistant en base
  #@smokeTests @contractv5 @caseRadiResil @casRadiationEtResiliation
  Scenario: I send a not already existing contract with a resiliation date and beneficiaries with a radiation date
    And I create a contract element from a file "gtaxa"
    And I create a contract element from a file "gtbaloo_multiversion"
    And I create a declarant from a file "declarantbaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    When I send a contract from file "generationDroitsTP_4943/radiation_et_resiliation/contrat_5_assure_resilie_avec_radiations" to version "V5"
    When I get triggers with contract number "0001" and amc "0000401166"
    Then I wait for 10 declarations
    Then I get 1 trigger with contract number "0001" and amc "0000401166"

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-12-31 | 2023-10-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | 2023-10-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-10-31 | 2023-10-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-10-31 | 2023-10-31 |
    Then the beneficiary has this values with indice 0
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20100819      | 1             | 5555555        |
    Then the beneficiary has this values with indice 1
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20100819      | 1             | 5555555        |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-12-31 | 2023-08-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | 2023-08-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
    Then the beneficiary has this values with indice 2
      | dateNaissance | rangNaissance | numeroPersonne |
      | 19810619      | 1             | 6666666        |
    Then the beneficiary has this values with indice 3
      | dateNaissance | rangNaissance | numeroPersonne |
      | 19810619      | 1             | 6666666        |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-12-31 | 2023-10-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | 2023-10-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 5
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-10-31 | 2023-10-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-10-31 | 2023-10-31 |
    Then the beneficiary has this values with indice 4
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 7777777        |
    Then the beneficiary has this values with indice 5
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 7777777        |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 6
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 7
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
    Then the beneficiary has this values with indice 6
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 88888888       |
    Then the beneficiary has this values with indice 7
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 88888888       |

    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 8
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | GT_BL_002  | PHCO    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | GT_BL_002  | OPTI    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHCN    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | DENT    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHNO    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHOR    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 9
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | GT_BL_002  | PHCO    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | GT_BL_002  | OPTI    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHCN    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | DENT    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHNO    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHOR    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
    Then the beneficiary has this values with indice 8
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 99999999       |
    Then the beneficiary has this values with indice 9
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 99999999       |
    And I wait for 1 contract

  # Contrat résilié avec 5 assurés ayant des dates de radiation différentes : contrat existant en base
  #@smokeTests @contractv5 @caseRadiResil @casRadiationEtResiliation
  Scenario: I send an existing contract with a resiliation date and beneficiaries with a radiation date
    And I create a contract element from a file "gtaxa"
    And I create a contract element from a file "gtbaloo_multiversion"
    And I create a declarant from a file "declarantbaloo"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    When I send a contract from file "generationDroitsTP_4943/radiation_et_resiliation/contrat_5_assure_sans_resiliation_ni_radiation" to version "V5"
    When I get triggers with contract number "0001" and amc "0000401166"
    Then I wait for 5 declarations
    Then I get 1 trigger with contract number "0001" and amc "0000401166"

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 0
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-12-31 | null      |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | null      |
    Then the beneficiary has this values with indice 0
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20100819      | 1             | 5555555        |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 1
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-12-31 | null      |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | null      |
    Then the beneficiary has this values with indice 1
      | dateNaissance | rangNaissance | numeroPersonne |
      | 19810619      | 1             | 6666666        |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 2
      | garantie   | domaine | debut      | fin        | finOnline |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-12-31 | null      |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-12-31 | null      |
    Then the beneficiary has this values with indice 2
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 7777777        |

    Then there is 2 rightsDomains and the different rightsDomains has this values with indice 3
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
    Then the beneficiary has this values with indice 3
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 88888888       |

    Then there is 8 rightsDomains and the different rightsDomains has this values with indice 4
      | garantie   | domaine | debut      | fin        | finOnline  |
      | AXASCCGDIM | OPAU    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | GT_BL_002  | PHCO    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | AXASCCGDIM | HOSP    | 2023-01-01 | 2023-08-31 | 2023-08-31 |
      | GT_BL_002  | OPTI    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHCN    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | DENT    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHNO    | 2023-01-01 | 2023-06-30 | 2023-06-30 |
      | GT_BL_002  | PHOR    | 2023-01-01 | 2023-06-30 | 2023-06-30 |

    Then the beneficiary has this values with indice 4
      | dateNaissance | rangNaissance | numeroPersonne |
      | 20130519      | 1             | 99999999       |
    And I wait for 1 contract



