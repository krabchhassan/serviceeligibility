// 5623 change la liste collectivites en identifiantsCollectivite
db.referentielParametragesCarteTP.updateMany(
    {
        "collectivites": { $exists: true }
    },
    {
        $rename: {
            "collectivites": "identifiantsCollectivite"
        }
    }
)