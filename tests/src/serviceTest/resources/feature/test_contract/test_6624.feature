Feature: Test contract v6 BLUE-6624

  Background:
    Given I try to create a parameter for type "conventionnement" in version "V2" with parameters
      | code    | IS     |
      | libelle | iSante |

  @todosmokeTests @sansEffetContrat @6624
  Scenario: BLUE-6624 création d'un contrat + génération + sans effet contrat + benef a toujours le contrat
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "benef1-6624"
    When I send a contract from file "contratV6/6624-1assure" to version "V6"
    Then I wait for 1 declarations
    Then I wait for 1 contract
    When I get a beneficiary with adherent number "1234567895894-01" and amc "0000401166"
    Given the beneficiary has services "ServicePrestation,Service_TP"
    Then the beneficiary has "1" contracts with codes "5894-01"
    When I delete a contract "5894-01" for amc "0000401166"
    When I get a beneficiary with adherent number "1234567895894-01" and amc "0000401166"
    Then the beneficiary has services "Service_TP"
    Then the beneficiary has "1" contracts with codes "5894-01"

  @todosmokeTests @sansEffetAssure @6624
  Scenario: BLUE-6624 création d'un contrat + génération + sans effet assure + benef a toujours le contrat
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "benef1-6624"
    Given I create a beneficiaire from file "benef2-6624"
    When I send a contract from file "contratV6/6624-2assures" to version "V6"
    Then I wait for 2 declarations
    Then I wait for 1 contract
    When I get a beneficiary with adherent number "1234567895894-02" and amc "0000401166"
    Then the beneficiary has services "ServicePrestation,Service_TP"
    Then the beneficiary has "1" contracts with codes "5894-01"
    When I send a contract from file "contratV6/6624-1assure" to version "V6"
    When I get a beneficiary with adherent number "1234567895894-02" and amc "0000401166"
    Then the beneficiary has services "Service_TP"
    Then the beneficiary has "1" contracts with codes "5894-01"

  @todosmokeTests @sansEffetContrat @6624
  Scenario: BLUE-6624 assure avec 2 contrats : 1 HTP et 1 HTP+TP
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "benef4-6624"
    When I send a contract from file "contratV6/6624-1assure" to version "V6"
    Then I wait for 1 declarations
    Then I send a contract from file "contratV6/6624-1assureSansTP" to version "V6"
    Then I wait for 1 contract
    When I get a beneficiary with adherent number "1234567895894-01" and amc "0000401166"
    Given the beneficiary has services "ServicePrestation,Service_TP"
    Then the beneficiary has "2" contracts with codes "5894-01,5894-02"
    When I delete a contract "5894-02" for amc "0000401166"
    When I get a beneficiary with adherent number "1234567895894-01" and amc "0000401166"
    Then the beneficiary has services "ServicePrestation,Service_TP"
    Then the beneficiary has "1" contracts with codes "5894-01"

  @todosmokeTests @sansEffetContrat @6624
  Scenario: BLUE-6624 assure avec 2 contrats : 2 HTP
    Given I create a contract element from a file "gtaxa"
    Given I create TP card parameters from file "parametrageTPBaloo"
    Given I create a declarant from a file "declarantbaloo"
    Given I create a beneficiaire from file "benef3-6624"
    When I send a contract from file "contratV6/6624-1assureSansTP2" to version "V6"
    Then I send a contract from file "contratV6/6624-1assureSansTP" to version "V6"
    When I get a beneficiary with adherent number "1234567895894-01" and amc "0000401166"
    Given the beneficiary has services "ServicePrestation"
    Then the beneficiary has "2" contracts with codes "5894-02,5894-03"
    When I delete a contract "5894-03" for amc "0000401166"
    When I get a beneficiary with adherent number "1234567895894-01" and amc "0000401166"
    Then the beneficiary has services "ServicePrestation"
    Then the beneficiary has "1" contracts with codes "5894-02"
