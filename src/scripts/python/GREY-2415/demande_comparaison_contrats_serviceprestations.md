# Objectif

Le but du script est d'envoyer dans le topic **_extract-contract_** les bénéficiaires présents dans la collection **_servicePrestation_** mais pas dans la collection **_bdds-to-blb-tracking_**

# Algorythme

1- Charge les **idDeclarant**/**numeroContrat**/**numeroAdherent** de la collection **servicePrestation**
2- Charge les **idDeclarant**/**numero**/**numeroAdherent** de la collection **contrats**
3- Effectue la différence entre les deux dataset via un anti-join sur toutes les colonnes
4- Sauvegarde un fichier de delta

# prérequis

Installer les requirements python dans un venv 3.11

Dépendance Lib Beyond:
pip install beyond-analysis-resource-client==14.0.1rc4 -i https://nexus-forge.dev.beyond.cegedim.cloud/repository/pypi-all/simple/ --extra-index-url https://registry-library.beyond.cegedim.cloud/repository/pypi-snapshot/simple --extra-index-url https://registry-library.beyond.cegedim.cloud/repository/pypi-release/simple

# Exécution

python demande_comparaison_contrats_serviceprestations.py --nature dev --adm-user adm-xxxxxxx --adm-password ***** --dry-run true --instance htp2-es14

