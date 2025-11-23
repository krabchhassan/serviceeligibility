## Enchainement des steps

Cette étape :

- récupération du paramètre d'entrée (--mode)
- si on est en mode "SYNC" :
    - suppression du cache Redis actuel
    - recharge depuis MongoDB les contractElement actifs et les réinsère dans Redis
- si on est en mode "CLEAR" :
    - suppression du cache Redis uniquement

## Liste des répertoires d'entrée/sortie

N/A

## Fonctionnement technique

Lors d'un plantage sur cet OMU, il n'y a pas de manipulation de fichiers à effectuer.

Etapes de relance :

- Relancer le traitement

Si replantage, suite à 1ère relance, contacter support niveau 1 équipe BDDS (Hors prod: communication par Zoom, en prod:
ouverture d'une néocase)
