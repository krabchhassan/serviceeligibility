PERTE DE DROITS EN 2024 suite à un envoi de contrats via AI le 07/12/2023.

1 Executer la requete sur les triggers (BLUE-7011-trigger).
Cela permet de récupérer les id de triggers pour trouver les déclarations

2 si l'on recherche les déclarations avec ces idTriggers on trouve toutes les déclarations problématiques
On ne peut pas se baser sur cette liste car des évènements sur les contrats ont été envoyés, dans cas le problème n'est pas constaté.

3 recherche des contrats TP problématiques (BLUE-7011-contrattp)

4 Avec cette liste de contrats (à transformer avec objectID) et des triggers, lancement de la requete des déclarations (BLUE-7011-declarations) suppression des 6005 déclarations avec deleteMany

5 Lancer la suppression des 6005 déclarations (sauvegarder dans la table declarations)
db.getCollection("declarations").deleteMany({ "_id": { $in: [ObjectId("657243ea3d7ba86325e035d1"),

6  relance des consolidations avec script python avec la liste des contrats issus de la requête BLUE-7011-contrattp
