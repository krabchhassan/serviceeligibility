Feature: Test consolidation des contrats BLUE-5727

  @smokeTests @pauv5 @caseConsolidation @ToBeContinue
  Scenario: Cas 1 : Changement de réseaux de soins (voir pw_baloo_5727.json)
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | SC |
      | libelle | SC |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | SC/IS |
      | libelle | SC/IS |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | SE |
      | libelle | SE |
    And I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | SE/IS |
      | libelle | SE/IS |
    And I create a contract element from a file "gt_5727"
    And I create TP card parameters from file "parametrageTPBaloo"
    And I create a declarant from a file "declarantbaloo"
    And I create a beneficiaire from file "beneficiaire5727"
    When I send a test contract v6 from file "contratV6/servicePrestation-5727"

    Then I wait for 1 contract
#    Then the expected contract TP is identical to "contrattp-5727" content
#
#    When I send a test contract v6 from file "contratV6/servicePrestation-5727-prio"
#
#    Then I wait for 1 contract
#    Then the expected contract TP is identical to "contrattp-5727-prio" content

#    Then there is 1 domaineDroits on benef 0 and warranty 0 and refCouverture 0 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | LABO | 2024/01/01 | 2024/06/30 | ONLINE      | null         |
#      | LABO | 2024/12/01 | null       | ONLINE      | null         |
#      | LABO | 2024/01/01 | 2024/06/30 | OFFLINE     | null         |
#      | LABO | 2024/12/01 | 2024/12/31 | OFFLINE     | null         |
#
#    Then In NaturePrestation 0 of RefCouverture 0 of Produit 0 of Garantie 0 of DomaineDroit 0 of Benef 0 of Contrat 0 there is conventionnements equal to
#      | priorite | typeConventionnement.code | typeConventionnement.libelle | periodes.0.debut | periodes.0.fin | periodes.1.debut |
#      | 0        | SC                        | Santéclair                   | 2024/01/01       | 2024/06/30     | 2024/12/01       |
#      | 1        | IS                        | iSante                       | 2024/01/01       | 2024/06/30     | 2024/12/01       |
#
#    Then In NaturePrestation 0 of RefCouverture 0 of Produit 0 of Garantie 0 of DomaineDroit 0 of Benef 0 of Contrat 0 there is prestations equal to
#      | code | formule.numero | periodes.0.debut | periodes.0.fin | periodes.1.debut |
#      | DEF  | 052            | 2024/01/01       | 2024/06/30     | 2024/12/01       |
#
#    Then In NaturePrestation 0 of RefCouverture 0 of Produit 0 of Garantie 0 of DomaineDroit 0 of Benef 0 of Contrat 0 there is remboursements equal to
#      | tauxRemboursement | uniteTauxRemboursement | periodes.0.debut | periodes.0.fin | periodes.1.debut |
#      | 100               | PO                     | 2024/01/01       | 2024/06/30     | 2024/12/01       |
#
#    Then In NaturePrestation 0 of RefCouverture 0 of Produit 0 of Garantie 0 of DomaineDroit 0 of Benef 0 of Contrat 0 there is prioritesDroit equal to
#      | code | libelle | typeDroit | prioriteBO | periodes.0.debut | periodes.0.fin | periodes.1.debut |
#      | 01   | 01      | 01        | 01         | 2024/01/01       | 2024/06/30     | 2024/12/01       |
#
#    Then there is 1 domaineDroits on benef 0 and warranty 0 and refCouverture 1 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | LABO | 2024/07/01 | 2024/09/30 | ONLINE      | null         |
#      | LABO | 2024/07/01 | 2024/09/30 | OFFLINE     | null         |
#
#    Then In NaturePrestation 0 of RefCouverture 1 of Produit 0 of Garantie 0 of DomaineDroit 0 of Benef 0 of Contrat 0 there is conventionnements equal to
#      | priorite | typeConventionnement.code | typeConventionnement.libelle | periodes.0.debut | periodes.0.fin |
#      | 0        | IS                        | Sévéane                      | 2024/07/01       | 2024/09/30     |
#      | 1        | IS                        | iSante                       | 2024/07/01       | 2024/09/30     |
#
#
#    Then there is 1 domaineDroits on benef 0 and warranty 0 and refCouverture 2 and the different domaineDroits has this values
#      | code | debut      | fin        | typePeriode | finFermeture |
#      | LABO | 2024/10/01 | 2024/11/30 | ONLINE      | null         |
#      | LABO | 2024/10/01 | 2024/11/30 | OFFLINE     | null         |
#
#    Then In NaturePrestation 0 of RefCouverture 1 of Produit 0 of Garantie 0 of DomaineDroit 0 of Benef 0 of Contrat 0 there is conventionnements equal to
#      | priorite | typeConventionnement.code | typeConventionnement.libelle | periodes.0.debut | periodes.0.fin |
#      | 0        | IS                        | Sévéane                      | 2024/10/01       | 2024/11/30     |
#      | 1        | IS                        | iSante                       | 2024/10/01       | 2024/11/30     |



