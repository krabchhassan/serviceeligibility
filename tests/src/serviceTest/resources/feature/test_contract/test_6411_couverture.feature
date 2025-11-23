Feature: 6411

  @smokeTests @pwCouverture @caseConsolidation
  Scenario: Cas 6411 pw janvier et juillet -> couvre tout le contrat
    Given I create a contract element from a file "gtbasebaloo6411Janv"
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    When I send a test contract from file "contratV6/createServicePrestation6411"
    Then I wait for 1 declaration
    Then I wait for 1 contract
    Then the expected contract TP is identical to "contrattp-6411" content
#    Then there is 1 domaineDroits on benef 0 and warranty 0 and refCouverture 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | DENT | 2025/06/20 | 2025/07/02 | ONLINE      |
#      | DENT | 2025/06/20 | 2025/07/02 | OFFLINE     |
#
#    Then there is 1 domaineDroits on benef 0 and warranty 0 and refCouverture 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | DENT | 2025/07/03 | 2025/07/30 | ONLINE      |
#      | DENT | 2025/07/03 | 2025/07/30 | OFFLINE     |
