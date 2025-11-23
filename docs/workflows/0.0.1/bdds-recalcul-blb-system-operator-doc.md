### Enchaînement des étapes

L’OMU ne contient 1 étape :

- Traitement “step-recalculblb-job”

### Liste des répertoires d'entrée/sortie

Aucun

### Fonctionnement technique

Le traitement se lance à la demande. Déroulement du traitement :

- Envoi un message pour vider la BLB (Base de Localisation des Bénéficiaires) existante.
- Attente d'une réponse OK
    - Si pas de réponse ou réponse KO : le traitement se termine en erreur.

- Parcours les contrats présents sur BDDS et pour chaque triplet (NIR ou NIR affiliation / Date de naissance / rang de naissance) de chaque assuré lance le traitement de génération de message à la BLB.
- Vérifie que pour chaque message envoyé, on a bien un retour
    - Si pas de retour ou retour manquant : le traitement se termine en erreur.
    - NB : si un des retours est en KO : le traitement se termine en succès et le nombre de retours KO est indiqué dans le CREX.


### Procédure de reprise

Lors d'un plantage du batch, la relance du batch peut s'effectuer directement sans manipulation de fichiers.

