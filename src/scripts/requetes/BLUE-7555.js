console.log(new Date());

db.getCollection("declarations").aggregate(
    [
        {
            $group: {
                _id : {
                    idDeclarant : "$idDeclarant",
                    numeroContrat : "$contrat.numero",
                    numeroAdherent : "$contrat.numeroAdherent",
                    numeroPersonne : "$beneficiaire.numeroPersonne",
                    idTrigger : "$idTrigger"
                },
                idDeclarations : {
                    $push : "$_id"
                },
                count : {
                    $sum : 1
                }
            }
        },
        {
            $match: {
                count : {
                    $gt : 20
                }
            }
        },
        {
            $out: "BLUE-7555-rv"
        }
    ],
    {
        allowDiskUse: true
    }
);

console.log(new Date());

db.getCollection("BLUE-7555-rv").find({}).forEach(doc => {
    let previousDeclTrigger = "";
    let previousDeclOrigine = "";
    let firstDecl = true;
    console.log("debut - contrat: " + doc._id.numeroContrat + ", adherent: " + doc._id.numeroAdherent + ", numeroPersonne: " + doc._id.numeroPersonne);
    db.getCollection("declarations").aggregate([{
        $match:
            {
                "idDeclarant": doc._id.idDeclarant,
                "contrat.numero": doc._id.numeroContrat,
                "contrat.numeroAdherent": doc._id.numeroAdherent,
                "beneficiaire.numeroPersonne": doc._id.numeroPersonne,
                "codeEtat": "R"
            }
    }, {
        $sort: {
            idOrigine: 1,
            idTrigger: 1,
            effetDebut: 1
        }
    }], { allowDiskUse: true }).forEach(decl => {
        if (previousDeclTrigger == decl.idTrigger && previousDeclOrigine == decl.idOrigine) {
            firstDecl = false;
        }
        else {
            previousDeclTrigger = decl.idTrigger;
            previousDeclOrigine = decl.idOrigine;
            firstDecl = true;
        }
        if (!firstDecl || db.getCollection("declarations").countDocuments({ "_id": ObjectId(decl.idOrigine) }) == 0) {
            db.getCollection("declarationsFbackup7555").insertOne(decl);
            db.getCollection("declarations").deleteOne({ "_id": decl._id });

            var trace = db.getCollection("traces").findOne({ idDeclaration: decl._id.toString() })
            if (trace != null) {
                db.getCollection("tracesBackup7555").insertOne(trace);
                db.getCollection("traces").deleteOne({ "_id": trace._id });
            }
        }
    });
    console.log("fait");
})
console.log(new Date());
