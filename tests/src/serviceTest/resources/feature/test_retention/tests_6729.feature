Feature: Les différents cas de tests recensés dans la BLUE-6729

  Background:
    Given I create a contract element from a file "gtdnt"
    Given I create a declarant from a file "declarantTestBlue"

  @smokeTests @retention @6729
  Scenario: Cas 01 - Reception contrat ouvert puis contrat résilié au 01/02/2025 puis contrat résilié au 01/01/2025
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat résilié au 2025-02-01 => Ajout d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas1-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Contrat résilié au 2025-01-01 => Mise à jour d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas1-b"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-01-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 02 - Reception contrat ouvert puis contrat résilié au 01/02/2025 puis contrat résilié au 11/02/2025
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat résilié au 2025-02-01 => Ajout d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas1-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Contrat résilié au 2025-02-11 => Mise à jour d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas2-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-11 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 03 - Reception contrat ouvert puis contrat résilié au 01/02/2025 puis contrat résilié au 11/02/2099
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat résilié au 2025-02-01 => Ajout d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas1-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Contrat résilié au 2099-02-11 => Passage en CANCELLED de la rétention existante
    When I send a test contract v6 from file "retention/servicePrestationCas3-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2099-02-11 |
      | originalEndDate | null       |
      | status          | CANCELLED  |

  @smokeTests @retention @6729
  Scenario: Cas 04 - Réception contrat résilié au 01/06/2099 puis contrat résilié 01/02/2025
    # Contrat résilié au 2099-06-01
    When I send a test contract v6 from file "retention/servicePrestationCas4-a"
    Then I wait for 0 retention
    # Contrat résilié au 2025-02-01 => Ajout d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas1-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-01 |
      | originalEndDate | 2099-06-01 |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 05 - Réception contrat ouvert puis contrat résilié 28/02/2025 puis passage de l'OMU puis de nouvelles résil
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat résilié au 2025-02-28 => Ajout d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas5-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-28 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Simulation du passage de l'OMU is-bdds-retention
    Then I update retention with the indice 0 to status "PROCESSED"
    # Contrat résilié au 2025-03-08 => Pas de nouvelle rétention
    When I send a test contract v6 from file "retention/servicePrestationCas5-b"
    # Pas de nouvelle rétention créées car la nouvelle date de résiliation est > à l'ancienne et on ne met pas à jour la rétention car elle n'est plus en TO_PROCESS
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-28 |
      | originalEndDate | null       |
      | status          | PROCESSED  |
    # Contrat résilié au 2025-03-05 => Ajout d'une entrée en base rétention
    When I send a test contract v6 from file "retention/servicePrestationCas5-c"
    Then I wait for 2 retention
    Then The retention with the indice 1 has these values
      | currentEndDate  | 2025-03-05 |
      | originalEndDate | 2025-03-08 |
      | status          | TO_PROCESS |
    # Contrat résilié au 2025-03-08 => Mise à jour de la rétention en base en CANCELLED
    When I send a test contract v6 from file "retention/servicePrestationCas5-b"
    Then I wait for 2 retention
    Then The retention with the indice 1 has these values
      | currentEndDate  | 2025-03-08 |
      | originalEndDate | 2025-03-08 |
      | status          | CANCELLED  |

  @smokeTests @retention @6729
  Scenario: Cas 06 - Réception contrat résilié au 01/06/2024 puis contrat résilié 01/02/2025
    # Contrat résilié au 2024-06-01
    When I send a test contract v6 from file "retention/servicePrestationCas6-a"
    Then I wait for 0 retention
    # Contrat résilié au 2025-02-01 => Agrandissement des droits, on ne crée pas de rétention
    When I send a test contract v6 from file "retention/servicePrestationCas1-a"
    Then I wait for 0 retention

  @smokeTests @retention @6729
  Scenario: Cas 07 - Réception droits ouverts au 01/01/2024 puis sans effet contrat puis réception d'un contrat ouvert au 01/01/2024
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Réception d'un sans effet contrat => création d'une rétention
    When I delete a contract "8343484392" for amc "0008400004"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Contrat droits ouverts au 2024-01-01 => mise à jour de la rétention à CANCELLED
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | null       |
      | originalEndDate | null       |
      | status          | CANCELLED  |

  @smokeTests @retention @6729
  Scenario: Cas 08 - Réception droits ouverts au 01/01/2024 puis sans effet contrat puis passage de l'OMU et réception d'un contrat ouvert
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Réception d'un sans effet contrat => création d'une rétention
    When I delete a contract "8343484392" for amc "0008400004"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Simulation du passage de l'OMU is-bdds-retention
    Then I update retention with the indice 0 to status "PROCESSED"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | null       |
      | status          | PROCESSED  |
    # Contrat droits ouverts au 2024-01-01 => pas de nouvelle rétention créée, on ne fait rien
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 1 retention
    Then The reception date for the retention 0 does not change

  @todosmokeTests @retention @6729
  Scenario: Cas 09 - Réception droits ouverts au 01/01/2024 puis sans effet contrat puis réception d'un contrat ouvert au 15/03/2024
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Réception d'un sans effet contrat => création d'une rétention
    When I delete a contract "8343484392" for amc "0008400004"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Contrat droits ouverts au 2024-03-15 => mise à jour de la rétention
    When I send a test contract v6 from file "retention/servicePrestationOuvert2"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | 2024-03-15 |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 10 - Réception droits ouverts au 01/01/2024 puis sans effet contrat puis réception d'un contrat ouvert au 01/01/2023
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Réception d'un sans effet contrat => création d'une rétention
    When I delete a contract "8343484392" for amc "0008400004"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Contrat droits ouverts au 2023-01-01 => mise à jour de la rétention à CANCELLED
    When I send a test contract v6 from file "retention/servicePrestationOuvert3"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | null       |
      | originalEndDate | null       |
      | status          | CANCELLED  |

  @smokeTests @retention @6729
  Scenario: Cas 12 - Réception droits ouverts puis résil dans le futur
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat résilié au 2099-06-01 => pas de rétention
    When I send a test contract v6 from file "retention/servicePrestationCas4-a"
    Then I wait for 0 retention

  @smokeTests @retention @6729
  Scenario: Cas 13 - Réception droits ouverts au 01/01/2024 puis résil au 01/03/2025 puis sans effet contrat
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat résilié au 2025-03-01 => Création d'une rétention
    When I send a test contract v6 from file "retention/servicePrestationCas13-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Réception d'un sans effet contrat => Mise à jour de la rétention
    When I delete a contract "8343484392" for amc "0008400004"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 14 - Réception droits ouverts au 01/01/2024 puis résil au 01/03/2025 puis passage de l'OMU puis sans effet contrat
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat résilié au 2025-03-01 => Création d'une rétention
    When I send a test contract v6 from file "retention/servicePrestationCas13-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Simulation du passage de l'OMU is-bdds-retention
    Then I update retention with the indice 0 to status "PROCESSED"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | PROCESSED  |
    # Réception d'un sans effet contrat => Mise à jour de la rétention
    When I delete a contract "8343484392" for amc "0008400004"
    Then I wait for 2 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | PROCESSED  |
    Then The retention with the indice 1 has these values
      | currentEndDate  | 2024-01-01 |
      | originalEndDate | 2025-03-01 |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 15 - Réception droits ouverts au 01/01/2024 avec 2 bénefs puis radiation au 01/02/2025 du 1er bénef puis radiation au 01/03/2025 du 2ème bénef
    When I send a test contract v6 from file "retention/servicePrestationOuvert2benefs"
    Then I wait for 0 retention
    # Contrat avec bénef 1 radié au 2025-02-01 => Création d'une entrée
    When I send a test contract v6 from file "retention/servicePrestationCas15-a"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # Contrat avec ajout de la radiation pour le bénef 2 radié au 2025-03-01 => Création d'une nouvelle entrée
    When I send a test contract v6 from file "retention/servicePrestationCas15-b"
    Then I wait for 2 retention
    # 1ère rétention => pas de changement
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-02-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    Then The reception date for the retention 0 does not change
    Then The retention with the indice 1 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 16 - Réception droits ouverts au 01/01/2024 puis résil au 31/03/2024 + radiation au 30/03/2024
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat avec résiliation au 2024-03-31 et radiation au 2024-03-30 => Création d'une entrée
    When I send a test contract v6 from file "retention/servicePrestationCas16"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-03-30 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 17 - Réception droits ouverts au 01/01/2024 puis résil au 31/03/2024 + radiation au 30/03/2024
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat avec résiliation au 2024-03-31 et radiation au 2024-03-30 et fin de période assuré au 2024-03-15 => Création d'une entrée
    When I send a test contract v6 from file "retention/servicePrestationCas17"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-03-30 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |

  @todosmokeTests @retention @6729
  Scenario: Cas 18 - Réception droits ouverts au 01/01/2023 avec 2 periodes assurées + pas mal de changements dans les différentes périodes
    # Contrat ouverts au 01/01/2023 et 1 periode assuré 01/01/2023 au 31/12/2023 et 1 periode assuré au 01/01/2024
    When I send a test contract v6 from file "retention/servicePrestationCas18Ouvert"
    Then I wait for 0 retention
    # Contrat avec résiliation au 2025-03-01 et radiation au 2025-03-01 et périodes assurés 2023-01-01 au 2023-10-31 et 2024-01-01 au 2025-03-01 => Création de 2 entrées
    When I send a test contract v6 from file "retention/servicePrestationCas18-a"
    Then I wait for 2 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    Then The retention with the indice 1 has these values
      | currentEndDate  | 2023-10-31 |
      | originalEndDate | 2023-12-31 |
      | status          | TO_PROCESS |
    # Contrat avec résiliation au 2025-03-01 et radiation au 2025-03-01 et périodes assurés 2023-01-01 au 2023-12-31 et 2024-01-01 au 2025-03-01 => Mise à jour des 2 entrées
    When I send a test contract v6 from file "retention/servicePrestationCas18-b"
    Then I wait for 2 retention
    # 1ère rétention => on ne change rien
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    Then The reception date for the retention 0 does not change
    # 2ème rétention => on la passe à CANCELLED
    Then The retention with the indice 1 has these values
      | currentEndDate  | 2023-10-31 |
      | originalEndDate | 2023-12-31 |
      | status          | CANCELLED  |

  @todosmokeTests @retention @6729
  Scenario: Cas 19 - Réception droits ouverts au 01/01/2023 avec 2 periodes assurées + pas mal de changements dans les différentes périodes
    # Contrat ouverts au 01/01/2023 et 1 periode assuré 01/01/2023 au 31/12/2023 et 1 periode assuré au 01/01/2024
    When I send a test contract v6 from file "retention/servicePrestationCas18Ouvert"
    Then I wait for 0 retention
    # Contrat avec résiliation au 2025-03-01 et radiation au 2025-03-01 et périodes assurés 2023-01-01 au 2023-12-31 et 2024-01-01 au 2025-03-01 => Création d'une entrée
    When I send a test contract v6 from file "retention/servicePrestationCas18-b"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2025-03-01 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |

  @smokeTests @retention @6729
  Scenario: Cas 20 - Réception droits ouverts au 01/01/2024 puis résiliation au 31/03/2024 puis résiliation au 31/03/2024
    When I send a test contract v6 from file "retention/servicePrestationOuvert"
    Then I wait for 0 retention
    # Contrat avec résiliation au 2024-03-31 => Création d'une entrée
    When I send a test contract v6 from file "retention/servicePrestationCas20"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-03-31 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    # On reçoit le même contrat avec résiliation au 2024-03-31 => pas de changement
    When I send a test contract v6 from file "retention/servicePrestationCas20"
    Then I wait for 1 retention
    Then The retention with the indice 0 has these values
      | currentEndDate  | 2024-03-31 |
      | originalEndDate | null       |
      | status          | TO_PROCESS |
    Then The reception date for the retention 0 does not change
