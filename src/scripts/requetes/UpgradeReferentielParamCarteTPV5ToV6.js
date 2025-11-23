// BLUE-5109

var listWithOnlyNA = ["N/A"];

// Update referentielParametrageCarteTP avec list 'colleges' existante
db.referentielParametragesCarteTP.updateMany(
    {
        $and: [
            { "colleges": { $exists: true }}
        ]
    },
    [
        {
            $set: {
                "identifiantsCollectivite": listWithOnlyNA,
                "groupesPopulation": listWithOnlyNA
            }
        },
        {
            $unset: [ "collectivites", "colleges" ]
        }
    ]
)
