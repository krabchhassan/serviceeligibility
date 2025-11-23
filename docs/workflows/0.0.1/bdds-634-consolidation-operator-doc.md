## Enchainement des steps 

Cette étape : 
- parcourt les déclarations une par une dans l'ordre d'insertion et crée une entrée dans la collection historiqueExecution.
- réalise une consolidation sur les périodes de droits ouvertes
- crée le contrat ou met à jour un contrat existant
- met à jour le champs "idDerniereDeclarationTraitee" de la collection "historiqueExecution" avec l'identifiant de la déclaration.

## Liste des répertoires d'entrée/sortie

**Répertoire d'entrée sur le serveur de fichier**

N/A

**Répertoire de sortie sur le serveur de fichier** 

files/out/RapportAlmerys/

**Répertoire d'archives sur le serveur de fichier** 

archives/out/RapportAlmerys/

## Fonctionnement technique

La date de synchro est obligatoire au premier lancement du batch. Pour les suivants, il ne faut par contre pas la passer.


### Bloquer la consolidation sur une ou plusieurs AMCs

Le batch prend en paramètre via jenkins une liste d'AMC devant être ignorées pour les prochaines exécutions du batch jusqu'à ce qu'elles soient explicitement notées comme à reprendre (voir paragraphe suivant).

Le paramètre a la forme : listeAMCstop=numeroAMC1,numeroAMC2,...
La liste comporte 1 à N numéro d'AMC séparés par des virgules.
Les déclarations ignorées seront comptées en tant que telles dans l'historique exécution du batch.
Le batch stocke en base l'id de déclaration où l'AMC a été bloquée pour une reprise future.

### Reprendre la consolidation sur une ou plusieurs AMCs

Le batch prend en paramètre via jenkins une liste d'AMC devant être reprises pour les prochaines exécutions du batch.

Le paramètre a la forme : listeAMCreprise=numeroAMC1,numeroAMC2,...
La liste comporte 1 à N numéro d'AMC séparés par des virgules.
La reprise s'effectuera en reprenant les déclarations de l'AMC mises en pause depuis le début de son blocage (puis le batch continuera avec son exécution normale).

N.B.: Si une AMC est dans les deux listes (bloquer et reprise), la priorité ira au blocage.

### Reprise après plantage

Lors d'un plantage sur cet OMU, il n'y a pas de manipulation de fichiers à effectuer.

Etapes de relance :

- Relancer le traitement

Si replantage, suite à 1ère relance, contacter support niveau 1 équipe BDDS (Hors prod: communication par Zoom, en prod: ouverture d'une néocase)