## Enchainement des steps

Cette étape :

- lit la collection servicePrestation de la BDDS, 10.000 contrats HTP à la fois (paramétrable par la variable BATCH_SIZE)
- crée pour chaque contrat HTP un objet JSON contenant des informations liées au contrat, à l'assuré, et ses destinataires de relevé de prestation et de paiement
- enregistre ces objets dans un tableau JSON sauvegardé dans un fichier au fil de l'eau sur le workdir

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur le workdir**

Aucune valeur par défaut par le paramétrage d'environnement WORKDIR_PATH.

## Fonctionnement technique

**Format du fichier de sortie :**

BDDS_recipient_extract_<date_traitement>.json

**Paramétrage d'environnement :**

- le répertoire de sortie sur le workdir : WORKDIR_PATH
- la quantité de contrats chargés en mémoire en même temps : BATCH_SIZE
