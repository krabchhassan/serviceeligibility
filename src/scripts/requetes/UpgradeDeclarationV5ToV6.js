// BLUE-5109

// WARNING : Pour utiliser correctement ce script, il faut :
//     - Scale à 0 pods le déploiement de kconnect
//     - Supprimer les connecteurs Kafka
//     - Lancer le script
//     - Supprimer le topic offset
//     - Scale à 1 pods le déploiement de kconnect

// Update declarations avec numero de contrat collectif
db.declarations.updateMany(
    {
        $and: [
            { "contrat.numeroContratCollectif": { $exists: true }},
            { "contrat.numeroContratCollectif": { $ne: null }},
            { "contrat.numeroContratCollectif": { $ne: "" }}
        ]
    },
    [
        {
            $set: {
                "contrat.identifiantCollectivite": "N/A",
                "contrat.raisonSociale": "N/A",
                "contrat.groupePopulation": "N/A"
            }
        }
    ]
)
