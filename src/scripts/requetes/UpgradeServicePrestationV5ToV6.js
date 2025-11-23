// BLUE-5109

// Update serviceprestation V5 avec un numero de contrat collectif
db.servicePrestation.updateMany(
    {
        "version": { $eq: "5" },
        $and: [
            { "contratCollectif.numero": { $exists: true }},
            { "contratCollectif.numero": { $ne: null }},
            { "contratCollectif.numero": { $ne: "" }}
        ]
    },
    [
        {
            $set: {
                "version": "6",
                "contratCollectif.identifiantCollectivite": "N/A",
                "contratCollectif.raisonSociale": "N/A",
                "contratCollectif.groupePopulation": "N/A"
            }
        },
        {
            $unset: [ "contexteTiersPayant.college", "contexteTiersPayant.collectivite", "_class" ]
        }
    ]
)

// Update serviceprestation V5 sans numero de contrat collectif
db.servicePrestation.updateMany(
    {
        "version": { $eq: "5" },
        $or: [
            { "contratCollectif.numero": { $exists: false }},
            { "contratCollectif.numero": { $eq: null }},
            { "contratCollectif.numero": { $eq: "" }}
        ]
    },
    [
        {
            $set: { "version": "6" }
        },
        {
            $unset: [ "contexteTiersPayant.college", "contexteTiersPayant.collectivite", "_class" ]
        }
    ]
)
