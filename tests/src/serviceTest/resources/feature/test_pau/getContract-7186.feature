Feature: [Forçage] Appel du PAU en mode forçage des droits KO hotfix-7186

  #  CAS 1 Faire un appel sur le père Morice G :
  #  NirCode: 1745023135112, conjoint sur le contrat de subscriberId: S2274751, "dateRadiation" : "2023/10/31"

  # OBJECTIF : tester les différentes périodes d'appel autour de la date de radiation

  @smokeTests @7186
  Scenario: S1 - Get pau d'un bénéficaire radié
    Given I create a beneficiaire from file "benef-7186-Conjoint"
    Given I create a contrat from file "contrattp-7186"

    # 1. APPEL APRES LA DATE DE RADIATION ("2023/10/31")
    # EXPECTED OUTCOME : online avec forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-11-10' '2023-12-31' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-postRadiation" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint" content
    # EXPECTED OUTCOME : online sans forcage => pas trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-11-10' '2023-12-31' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then an error "400" is returned with message "Droits non ouverts"
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then an error "404" is returned with message "Contrat résilié"
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-11-01' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then an error "404" is returned with message "Contrat résilié"
    # EXPECTED OUTCOME : offline avec forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-11-10' '2023-12-31' 0000401166 TP_OFFLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-TP_OFFLINE" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-11-10' 0000401166 TP_OFFLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-TP_OFFLINE" content
    # EXPECTED OUTCOME : offline sans forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-11-10' '2023-12-31' 0000401166 TP_OFFLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-TP_OFFLINE" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-11-10' 0000401166 TP_OFFLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-TP_OFFLINE" content

    # 2. APPEL AVANT LA date de radiation ("2023/10/31")
    # EXPECTED OUTCOME : online avec forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-03-10' '2023-03-31' 0000401166 TP_ONLINE S2274751 for domains PHAR and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-anteRadiation" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-03-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-anteRadiation" content
    # EXPECTED OUTCOME : online sans forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-03-10' '2023-03-31' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-anteRadiation" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-03-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-anteRadiation" content

    # 3. AUTOUR DE LA DATE DE RADIATION
    # EXPECTED OUTCOME : ONline sans forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-01-01' '2023-11-30' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-AutourRadiation" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-10-31' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then  the pau is identical to "pau/v5/pau-7186-benefConjoint-jourRadiation" content
    # EXPECTED OUTCOME : ONline avec forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-01-01' '2023-11-30' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-AutourRadiation" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-10-31' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then  the pau is identical to "pau/v5/pau-7186-benefConjoint-jourRadiation" content
    # EXPECTED OUTCOME : offline sans forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-01-01' '2023-12-31' 0000401166 TP_OFFLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-AutourRadiation-TP_OFFLINE" content
    # EXPECTED OUTCOME : offline avec forcage => trouvé
    When I get contrat PAUV5 for 1745023135112 '19741225' 1 '2023-01-01' '2023-12-31' 0000401166 TP_OFFLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-benefConjoint-surPeriode-AutourRadiation-TP_OFFLINE" content
    When I get contrat PAUV5 without endDate for 1745023135112 '19741225' 1 '2023-10-31' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then  the pau is identical to "pau/v5/pau-7186-benefConjoint-jourRadiation" content

   # CAS 2  Faire un appel sur la mère LOISE :
   # nir: 2870535238001

  @smokeTests @7186
  Scenario: S2 - Get pau sur une souscriptrice active
    Given I create a beneficiaire from file "benef-7186-Souscriptrice"
    Given I create a contrat from file "contrattp-7186"

    # OBJECTIF : vérifier les valeurs de contexts
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice" content
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_OFFLINE S2274751 for domains PHAR
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice-TP_OFFLINE" content
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 HTP S2274751 for domains PHAR
    Then an error "404" is returned with message "Contrat non trouvé"
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-01-10' '2023-12-01' 0000401166 XXXXX S2274751 for domains PHAR
    Then an error "400" is returned with message "Le contexte 'XXXXX' est inconnu. Les contextes permis sont 'HTP', 'TP_ONLINE' et 'TP_OFFLINE'."

    # OBJECTIF : vérifier le changement de domaine TP
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-11-10' '2024-11-10' 0000401166 TP_ONLINE S2274751 for domains OPTI
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice-opti" content
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-11-10' '2024-11-10' 0000401166 TP_ONLINE S2274751 for domains HOSP
    Then an error "400" is returned with message "Type de dépense non ouvert"
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-11-10' '2024-11-10' 0000401166 TP_ONLINE S2274751 for domains XXXX
    Then an error "400" is returned with message "Type de dépense non ouvert"

    # OBJECTIF : tester l'appel avec tous les params
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for beneficiaryId 0000401166-2529639 for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice" content
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for beneficiaryId 0000401166-2529639 for issuingCompanyCode 0000401166
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice" content
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for beneficiaryId 0000401166-2529639
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice" content
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice" content
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice" content
    When I get contrat PAUV5 without endDate for 2870535238001 '19790212' 1 '2023-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice" content
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-11-10' '2024-11-10' 0000401166 TP_ONLINE S2274751 for domains PHAR for issuingCompanyCode 0000401166 and isForced true
    Then the pau is identical to "pau/v5/pau-7186-Souscriptrice-surPeriode" content

    # OBJECTIF : vérifier les différents formats de date
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-11-10' '2024-31-31' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then an error "400" is returned with message "'endDate' avec la valeur '2024-31-31' ne respecte pas le format 'yyyy-MM-dd' ."
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-31-10' '2024-31-31' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then an error "400" is returned with message "'startDate' avec la valeur '2023-31-10' ne respecte pas le format 'yyyy-MM-dd' ."
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '20233110' '2024-31-31' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then an error "400" is returned with message "'startDate' avec la valeur '20233110' ne respecte pas le format 'yyyy-MM-dd' ."
    When I get contrat PAUV5 for 2870535238001 '19790212' 1 '2023-01-10' '2023-01-01' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then an error "400" is returned with message "La date de début d'interrogation est supérieure à la date de fin."
    When I get contrat PAUV5 for 2870535238001 '1979-02-12' 1 '2023-01-10' '2023-12-01' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then an error "400" is returned with message "La date de naissance 1979-02-12 n'est pas valide"
    When I get contrat PAUV5 for 2870535238001 '19791212' 1 '2023-01-10' '2023-12-01' 0000401166 TP_ONLINE S2274751 for domains PHAR
    Then an error "404" is returned with message "Bénéficiaire non trouvé"


  # CAS 3  Faire un appel sur l'enfant LOIC qui a le nir de sa mère
   # nir: 2870535238001
  @smokeTests @7186
  Scenario: S3 - Get pau TP_ONLINE sans date de Fin avec forcage sur une souscriptrice active
    Given I create a beneficiaire from file "benef-7186-EnfantSansNir"
    Given I create a contrat from file "contrattp-7186"
    When I get contrat PAUV5 for 2870535238001 '20070624' 1 '2023-01-01' '2023-12-31' 0000401166 TP_OFFLINE S2274751 for domains PHAR
    Then the pau is identical to "pau/v5/pau-7186-benefEnfantSansNir-TP_OFFLINE" content
