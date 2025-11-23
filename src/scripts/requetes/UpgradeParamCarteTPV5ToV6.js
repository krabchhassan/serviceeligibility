// BLUE-5109

// Update parametrageCarteTP avec champ 'college' existant
db.parametragesCarteTP.updateMany(
    {
        $and: [
            { "college": { $exists: true }}
        ]
    },
    [
        {
            $set: {
                "identifiantCollectivite": "",
                "groupePopulation": ""
            }
        },
        {
            $unset: [ "collectivite", "college" ]
        }
    ]
)
