Feature: Get Payment Recipients
  Parametres obligatoires lors de l appel au WS :
  - AMC : insurerId
  - Identifiant du bénéficiaire (concaténation du n° d’AMC et du n° de personne) : beneficiaryId
  - Liste des contrats (liste composé du n° adhérent et du n° de contrat) : contracts
  - Date de référence : referenceDate

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @paymentRecipients
  Scenario: Get Payment Recipients for one contract
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationContract01"

    # cas normal
    When I search a payment recipients with request from file "request"
    Then the expected payment recipients response is identical to "response" content

    # cas où le benef n'a pas de destinataire de paiement valide
    When I search a payment recipients with request from file "request_noPaymentRecipientsValid"
    Then the expected payment recipients response is identical to "response_noPaymentRecipientsValid" content

    # cas où le benef n'existe pas
    When I search a payment recipients with request from file "request_unknownPersonNumber"
    Then the expected payment recipients response is identical to "response_unknownPersonNumber" content

    # cas où le contrat n'existe pas
    When I search a payment recipients with request from file "request_unknownContract"
    Then the expected payment recipients response is identical to "response_unknownContract" content

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @paymentRecipients
  Scenario: Get Payment Recipients for one contract and several payment recipients
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationContract01WithMultiplePaymentRecipients"

    # cas pl dest prest valide
    When I search a payment recipients with request from file "request_multiplePaymentRecipients"
    Then the expected payment recipients response is identical to "response_multiplePaymentRecipients" content

    # cas aucun dest prest valide du coup renvoit dest prest avec la plus petite date de début après la date de référence
    When I search a payment recipients with request from file "request_paymentRecipientsAfterDate"
    Then the expected payment recipients response is identical to "response_paymentRecipientsAfterDate" content

    # cas aucun dest prest valide du coup renvoit dest prest avec la plus grande date de fin avant la date de référence
    When I search a payment recipients with request from file "request_paymentRecipientsBeforeDate"
    Then the expected payment recipients response is identical to "response_paymentRecipientsBeforeDate" content

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @paymentRecipients
  Scenario: Get Payment Recipients for two contracts
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationContract01"
    Given I create a service prestation from a file "createServicePrestationContract02"

    # cas plusieurs contrats valides
    When I search a payment recipients with request from file "request_severalContracts"
    Then the expected payment recipients response is identical to "response_severalContracts" content

    # cas un contrat valide, mais le deuxième contrat n'existe pas
    When I search a payment recipients with request from file "request_severalContractsButOneUnknown"
    Then the expected payment recipients response is identical to "response_severalContractsButOneUnknown" content

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @paymentRecipients
  Scenario: Get Payment Recipients with missing parameter
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationContract01"
    # cas paramètre manquant
    When I try to search a payment recipients with request from file "request_missingParameter"
    Then an error "400" is returned with message "The number contracts[0].number is required in the body"

  @todosmokeTests @smokeTestsWithoutKafka @servicePrestation @paymentRecipients
  Scenario: Get Payment Recipients with wrong date format
    Given I drop the collection for Service Prestation
    Given I create a service prestation from a file "createServicePrestationContract01"
    # cas paramètre date mal formatté
    When I try to search a payment recipients with request from file "request_wrongDateFormat"
    Then an error "400" is returned with message "referenceDate must be in the format YYYY-MM-DD"
