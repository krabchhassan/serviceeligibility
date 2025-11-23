Feature: Test contract deletion

  #beneficiary with two contracts for servicePrestation
  # 8343484392 and MBA002 and prestij contract MKU
  # if we delete all contracts that come from the collection servicePrestation the services in the beneficiary
  # need to remove also the servicePrestation
  @todosmokeTests @smokeTestsWithoutKafka @contractV5 @deleteContrat
  Scenario: I delete both contracts for service prestation
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "consumer_worker/v5Delete/case1/declarant"
    Given I create a benef from file "consumer_worker/v5Delete/case1/benef"
    Given I send a contract from file "consumer_worker/v5Delete/case1/contract1" to version "V5"
    Given the trace is created and contains the contract id for contract number "8343484392"
    Given I send a contract from file "consumer_worker/v5Delete/case1/contract2" to version "V5"
    Given the trace is created and contains the contract id for contract number "8343484392"
    Given I get a beneficiary with adherent number "001" and amc "0008400004"
    Given the beneficiary has services "ServicePrestation,PrestIJ"
    Then the beneficiary has "3" contracts with codes "MBA02,8343484392,MKU"
    When I delete a contract "8343484392" for amc "0008400004"
    When I get a beneficiary with adherent number "001" and amc "0008400004"
    Then the beneficiary has "2" contract with code "MBA02,MKU"
    Then the beneficiary has services "ServicePrestation,PrestIJ"
    When I delete a contract "MBA02" for amc "0008400004"
    When I get a beneficiary with adherent number "001" and amc "0008400004"
    Then the beneficiary has "1" contracts with codes "MKU"
    Then the beneficiary has services "PrestIJ"
    #adding timeout as running the two tests consecutively failed
    Given I wait "2" seconds in order to consume the data

    #here the benef has only 1 service -> servicePrestation
  @todosmokeTests @smokeTestsWithoutKafka @contractV5 @deleteContrat
  Scenario: I delete a contract service prestation
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "consumer_worker/v5Delete/case1/declarant"
    Given I create a beneficiaire from file "consumer_worker/v5Delete/case2/benef"
    When I send a contract from file "consumer_worker/v5Delete/case1/contract1" to version "V5"
    Given the trace is created and contains the contract id for contract number "8343484392"
    When I send a contract from file "consumer_worker/v5Delete/case1/contract2" to version "V5"
    Given the trace is created and contains the contract id for contract number "8343484392"
    Given I get a beneficiary with adherent number "001" and amc "0008400004"
    Given the beneficiary has services "ServicePrestation"
    Then the beneficiary has "2" contracts with codes "MBA02,8343484392"
    When I delete a contract "8343484392" for amc "0008400004"
    When I delete a contract "MBA02" for amc "0008400004"
    When I get a beneficiary with adherent number "001" and amc "0008400004"
    Then the beneficiary has no services
