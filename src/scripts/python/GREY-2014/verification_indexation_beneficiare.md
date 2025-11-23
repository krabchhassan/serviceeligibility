# Objectif

Le but du script est d'identifier les documents bénéficiaire présent dans mon

# Algorithme

1- Charge les documents Id de l'index
2- Charges les documents Id de la collection béneficiaires de MongoDB
3- Effectue la différence entre les deux dataset via un anti-join
4- Ecrit les IDs dans un fichier CSV

# Prérequis

Installer les requirements python dans un venv 3.11

Dépendance Lib Beyond:
pip install beyond-analysis-resource-client==14.0.1rc9 -i https://nexus-forge.dev.beyond.cegedim.cloud/repository/pypi-all/simple/ --extra-index-url https://registry-library.beyond.cegedim.cloud/repository/pypi-snapshot/simple --extra-index-url https://registry-library.beyond.cegedim.cloud/repository/pypi-release/simple

# Exécution

python verification_indexation_beneficiare.py --nature preprod --adm-user adm-ndardenne --adm-password ****** --dry-run true --instance gh00 --instance-version gh00-es14 --client igestion

Temps de traitement ~10 min pour 16M de benef (testé sur C4 sur site/bastion - gros ralentissement si VPN)