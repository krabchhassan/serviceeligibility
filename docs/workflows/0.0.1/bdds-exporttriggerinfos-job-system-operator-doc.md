## Enchainement des steps

Cette étape :

- Récupère les triggers "Renewal" ayant le flag "exported" différent de "true"
- Récupère les triggers benefs associés
- Envoi d'un event "triggered-beneficiary-finished-event" par trigger benef
- Génère 1 fichier csv par trigger sur le bucket s3 cible contenant les informations des triggers benefs associés

## Liste des répertoires d'entrée/sortie

**Répertoire de sortie sur s3**

Configurable par le paramétrage d'environnement s3_export_trigger_benef_path.

## Fonctionnement technique

**Format du fichier de sortie :**

<idTrigger>.csv

**Paramétrage d'environnement :**

- Le répertoire de sauvegarde sur s3 : s3_export_trigger_benef_path

## Format des fichiers de sortie :

**Entête**
- Numero Contrat
- NIR Bénéficiaire
- Date Naissance
- Garanties
- Collectivité
- Collège
- Critère Secondaire Détaillé
- Détail Anomalie