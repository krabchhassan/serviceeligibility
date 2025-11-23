db.getCollection("servicePrestation").aggregate(
    [
        {
            $unwind: {
                path: "$assures"
            }
        },
        {
            $group: {
                _id: "$assures.identite.refExternePersonne",
                idDeclarant:{$first:"$idDeclarant"},
                numeroPersonne:{$first:"$assures.identite.numeroPersonne"},
                contrats : { $push:{numero:"$numero", numeroExterne:"$numeroExterne"}}
            }
        },
        {
            $project: {
                idBeneficiaire: {$concat: ["$idDeclarant","-","$numeroPersonne"]},
                contrats : 1
            }
        }
    ],
    {
        allowDiskUse: true
    }
);