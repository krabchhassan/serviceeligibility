Feature: Test creation / modification prestij
  Listes champs obligatoires :
  - contrat
  - numero
  - numeroAdherent
  - dateSouscription
  - isContratIndividuel
  - oc
  - identifiant
  - denomination
  - assures
  - nir
  - code
  - cle
  - numeroPersonne
  - dateNaissance
  - rangNaissance
  - data
  - nom
  - nomFamille
  - prenom
  - civilite
  - droits
  - type
  - dateDebut
  Tous les champs sont envoyés sous forme de String
  Toutes les dates doivent être envoyées au format yyyy-MM-dd. Sauf la date de naissance celle-ci peut contenir une date de naissance lunaire et qui est au format YYYYMMDD.

  Pour les tests en local :
  - il faut lancer le worker PrestIJ sur le port 8080, et le serviceeligibility sur le port 8081
  - l'idclientBO à positionner lors de l'étape de création du déclarant est Unidentified
  Pour les tests sur les autres environnements :
  - l'idclientBO à positionner lors de l'étape de création du déclarant est service-account-test-es16


  @smokeTests @smokeTestsWithoutKafka @prestijV2 @release
  Scenario: Send a prestIJ with all fields, verify that the trace references the prestIJ
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    When I send a prestIJ in version "V2" from file "prestIJ_cas1"
    Then the trace is created and contains the status "SentToKafka"

  @prestijV2
  Scenario: Send a prestIJ with all fields, verify that the trace references the prestIJ and that the stored PrestIJ has the correct values
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case2 : Création d'une prestIJ non connue du SI et contenant toutes les données du flux
    When I send a prestIJ in version "V2" from file "prestIJ_cas2"
    Then the trace is created and contains the prestIJ id
    Then I get the detail of the prestIJ
    Then the expected prestIJ is identical to "expectedPrestIJ_cas2" content

  @prestijV2
  Scenario: Send several prestIJ for simulating the life cycle of a PrestIJ
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case3a : Création d'une prestIJ non connue du SI
    When I send a prestIJ in version "V2" from file "prestIJ_cas3_a"
    Then the trace is created and contains the prestIJ id
    # case3b : Ajout d'un assuré et d'un droit pour l'assuré existant
    When I send a prestIJ in version "V2" from file "prestIJ_cas3_b"
    Then the trace is created and contains the prestIJ id
    # case3c : Fermeture de la prestIJ
    When I send a prestIJ in version "V2" from file "prestIJ_cas3_c"
    Then the trace is created and contains the prestIJ id
    Then I get the detail of the prestIJ
    Then the expected prestIJ is identical to "expectedPrestIJ_cas3" content

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2 @stepv2_1
  Scenario: Send a prestIJ with invalid date for the field contrat.dateSouscription
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case4 : Création d'une prestIJ avec une date contenant un caractère
    When I try to send a prestIJ in version "V2" from file "prestIJ_cas4_a"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "dateSouscription n'est pas une date au format yyyy-MM-dd"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ with invalid date for the field contrat.dateResiliation
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case4 : Création d'une prestIJ avec une date invalide
    When I try to send a prestIJ in version "V2" from file "case4/prestIJb"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "dateResiliation n'est pas une date au format yyyy-MM-dd"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ with invalid date for the field assures.droits.dateDebut
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case4 : Création d'une prestIJ avec une date invalide
    When I try to send a prestIJ in version "V2" from file "case4/prestIJc"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "dateDebut n'est pas une date au format yyyy-MM-dd"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ with invalid date for the field assures.droits.dateFin
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case4 : Création d'une prestIJ avec une date invalide
    When I try to send a prestIJ in version "V2" from file "case4/prestIJd"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "dateFin n'est pas une date au format yyyy-MM-dd"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ with invalid date for the field assures.dateNaissance
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case4 : Création d'une prestIJ avec une date invalide
    When I try to send a prestIJ in version "V2" from file "case4/prestIJe"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "dateNaissance n'est pas une date au format yyyyMMdd"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field contrat.numero
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJa"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le numéro de contrat est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field contrat.numeroAdherent
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJb"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le numéro d'adhérent est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field contrat.dateSouscription
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJc"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "La date de souscription est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field contrat.isContratIndividuel
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJd"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "L'information contrat individuel est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field oc.identifiant
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJe"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "L'identifiant de l'OC est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field oc.denomination
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJf"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "La dénomination de l'OC est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.nir.code
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJg"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le code du NIR est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.nir.cle
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJh"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "La clé du NIR est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.numeroPersonne
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJi"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le numéro de personne de l'assuré est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.dateNaissance
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJj"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "La date de naissance de l'assuré est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.rangNaissance
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJk"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le rang de naissance est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.data.nom.nomFamille
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJl"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le nom de famille est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.data.nom.prenom
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJm"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le prénom est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.data.nom.civilite
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJn"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "La civilité est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.droits.type
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJo"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le type de droit est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required field assures.droits.dateDebut
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case5 : Création d'une prestIJ avec une information obligatoire manquante
    When I try to send a prestIJ in version "V2" from file "case5/prestIJp"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "La date de début de droit est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block contrat
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case6 : Création d'une prestIJ avec un bloc d' information obligatoire manquant
    When I try to send a prestIJ in version "V2" from file "case6/prestIJa"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le contrat est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block oc
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case6 : Création d'une prestIJ avec un bloc d' information obligatoire manquant
    When I try to send a prestIJ in version "V2" from file "case6/prestIJb"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "L'OC est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block assures
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case6 : Création d'une prestIJ avec un bloc d' information obligatoire manquant
    When I try to send a prestIJ in version "V2" from file "case6/prestIJc"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "La liste d'assurés est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block assures.nir
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case6 : Création d'une prestIJ avec un bloc d' information obligatoire manquant
    When I try to send a prestIJ in version "V2" from file "case6/prestIJd"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le NIR de l'assuré est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block assures.data
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case6 : Création d'une prestIJ avec un bloc d' information obligatoire manquant
    When I try to send a prestIJ in version "V2" from file "case6/prestIJe"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "L'information data de l'assuré est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block assures.data.nom
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case6 : Création d'une prestIJ avec un bloc d' information obligatoire manquant
    When I try to send a prestIJ in version "V2" from file "case6/prestIJf"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le nom est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block assures.droits
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case6 : Création d'une prestIJ avec un bloc d' information obligatoire manquant
    When I try to send a prestIJ in version "V2" from file "case6/prestIJg"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "L'information droit de l'assuré est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ with no body
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case7 : Création d'une prestIJ sans body
    When I try to send a prestIJ in version "V2" from file "case7/prestIJ"
    Then the response has an HTTP code "400"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required informations block entreprise
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a prestIJ in version "V2" from file "case6/prestIJh"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "L'entreprise est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ without the required fields Siren and Siret
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a prestIJ in version "V2" from file "case6/prestIJi"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le SIREN ou SIRET de l'entreprise est obligatoire"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @IdClientBOInvalid
  Scenario: Send a prestIJ with an inexisting AMC
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    When I try to send a prestIJ in version "V2" from file "case8/prestIJInexistingAMC"
    Then the trace is created and contains the status "IdClientBOInvalid"
    Then the trace has the error message "Le déclarant 9999999999 n'existe pas !"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @IdClientBOInvalid
  Scenario: Send a prestIJ with an idClientBO without rigth for the AMC
    Given I empty the prestij database
    Given I create a declarant from a file "declarantWithoutAcess"
    When I try to send a prestIJ in version "V2" from file "case8/prestIJWithoutAccess"
    Then the trace is created and contains the status "IdClientBOInvalid"
    Then the trace has the error message "L'identifiant Back Office service-account-test-es16 ne permet pas d'accéder aux données du déclarant 0008400004"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ with assures.droits.dateDebut higher than assures.droits.dateFin
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case : Création d'une prestIJ avec une dateDebut supérieure à dateFin
    When I try to send a prestIJ in version "V2" from file "case9/prestIJ"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "dateDebut n'est pas une date inférieure à dateFin"
    Then the error provider is "BDDS"

  @todosmokeTests @smokeTestsWithoutKafka @prestijV2
  Scenario: Send a prestIJ with invalid NIR
    Given I empty the prestij database
    Given I create a declarant from a file "declarantTestBlue"
    # case : Création d'une prestIJ avec un NIR invalide
    When I try to send a prestIJ in version "V2" from file "case9/prestIJb"
    Then the trace is created and contains the status "ValidationFailed"
    Then the trace has the error message "Le NIR n'est pas valide"
    Then the error provider is "BDDS"
