// BLUE-5109

// Update contrats avec numero de contrat collectif
db.contrats.updateMany(
    {
        $and: [
            { "numeroContratCollectif": { $exists: true }},
            { "numeroContratCollectif": { $ne: null }},
            { "numeroContratCollectif": { $ne: "" }}
        ]
    },
    [
        {
            $set: {
                "identifiantCollectivite": "N/A",
                "raisonSociale": "N/A",
                "groupePopulation": "N/A"
            }
        }
    ]
)
