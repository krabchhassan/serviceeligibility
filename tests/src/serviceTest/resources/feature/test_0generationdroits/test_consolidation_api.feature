Feature: Consolidation api

  @smokeTests @apiConso @caseConsolidation @elasticHistoContrat @release
  Scenario: Demande de consolidation d'un semble de declaration via idDeclarant, numContrat et numAdherent
    And I delete the contract histo for this contract A0002
    And I create a declaration from a file "consolidation_droits_api/02-declaration-1-conso"
    Then I process all declaration for idDeclarant "0000401166", numContrat "A0002", numAdhérent "A0002"
    Then I wait for 1 contract
    Then the expected contract TP with indice 0 is identical to "consolidation/contrat1" content
    And I wait for 0 contract histo for this contract A0002

    Given I create a declaration from a file "consolidation_droits_api/02-declaration-2-conso"
    Then I process all declaration for idDeclarant "0000401166", numContrat "A0002", numAdhérent "A0002"
    Then I wait for 1 contract
    Then the consolidated contract has values
      | numeroContrat        | A0002         |
      | idDeclarant          | 0000401166    |
      | nirBeneficiaire      | 1700131111112 |
      | cleNirBeneficiaire   | 17            |
      | rangAdministratif    | 2             |
      | dernierMouvementRecu | R             |
    Then the expected contract TP is identical to "contrattpapiconsolidate" content
#    Then there is 1 domaineDroits and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode |
#      | AUXM | 2023/01/01 | 2024/12/31 | OFFLINE     |

    And I wait for 1 contract histo for this contract A0002

    And I delete the contract histo for this contract A0002


  @todosmokeTests @smokeTestsWithKafka @apiConso @elasticHistoContrat
    # on prend une 404 au niveau du org.springframework.web.client.RestTemplate.exchange(RestTemplate.java:676)
    # peut être faire une méthode spécifique ?
  Scenario: Demande de consolidation d'un semble de declaration via idDeclarant, numContrat et numAdherent en erreur
    And I delete the contract histo for this contract A0002
    And I create a declaration from a file "consolidation_droits_api/decl-404-conso"
    Then I process all declaration for idDeclarant "0000401166", numContrat "A0002", numAdhérent "A0002"
    And I wait "2" seconds in order to consume the data
    And The error is "404"
