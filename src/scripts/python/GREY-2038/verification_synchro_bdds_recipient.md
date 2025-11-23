# Objectif

Le but du script est d'identifier les destinatires de paiement et de relevés de prestation présent dans BDDS et pas dans Recipient

# Algorithme

1- Charge les documents de BDDS
2- Charges les destinataire de Recipient
3- Effectue la différence entre les deux dataset via un anti-join
4- Ecrit les destinataires manquent dans un fichier JSON au format d'input de l'OMU is-recipt-synchronizer

# Prérequis

Installer les requirements python dans un venv 3.11

Dépendance Lib Beyond:
pip install beyond-analysis-resource-client==14.0.5 -i https://nexus-forge.dev.beyond.cegedim.cloud/repository/pypi-all/simple/ --extra-index-url https://registry-library.beyond.cegedim.cloud/repository/pypi-snapshot/simple --extra-index-url https://registry-library.beyond.cegedim.cloud/repository/pypi-release/simple

# Exécution

1) python verification_synchro_bdds_recipient.py --nature preprod --adm-user adm-ndardenne --adm-password ** --dry-run false --instance gh00 --instance-version gh00-es14 --client igestion

2) Récupérer le fichier JSON généré dans le dossier temporaire => BDDS_recipient_extract_gh00.json

3) Uploader le fichier sur le workir de l'instance dans le dossier d'input de Recipient: 
https://workdir-edition.preprod.beyond.cegedim.cloud/files/Igestion/GH00/IGE/EG/PG/recipient/input/

4) Lancer l'OMU: : is-recipt-synchronizer

5) Relancer le script qui doit afficher
- Aucun destinataires de paiement n'est manquant dans Recipient
- Aucun destinataires de relevés de prestations n'est manquant dans Recipient
 