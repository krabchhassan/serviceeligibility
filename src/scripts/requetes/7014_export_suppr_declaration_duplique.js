/**
 * BLUE-7014
 * Sauvegarde dans une base temporaire "declarations_pb" les declarations qui sont dupliquees pour une amc et un dateEffet donnee.
 * Puis a partir de la base "declarations_pb" supprime ces declarations selectionnees dans la collection principale "declarations"
 */

let declCount = 0;
db.getCollection("triggeredBeneficiary").find({
    "amc": "0000401166",
    "historiqueStatuts.4": {
        $exists: true
    },
    "statut": "Processed",
    "historiqueStatuts.dateEffet": { $gte: ISODate("2025-04-01T00:00:00.000+0000") }
}).forEach(tb => {
    console.log(tb._id);
    let declTrig = 0;
    let declTrigRemov = 0;
    let declR = 0;
    let waitNext = false;
    let vFound = false;
    let rDone = false;
    let effetDebut = "";
    let debut = "";
    db.getCollection("declarations").find({
        "idDeclarant": tb.amc,
        "contrat.numero": tb.numeroContrat,
        "contrat.numeroAdherent": tb.numeroAdherent,
        "beneficiaire.numeroPersonne": tb.numeroPersonne,
        "idTrigger": tb.idTrigger
    }).sort({ "_id": 1 }).forEach(decl => {
        debut = decl.effetDebut.getTime();
        if (effetDebut == "") {
            effetDebut = debut;
        } else if (effetDebut !== debut) {
             db.getCollection("declarations_pb").insertOne(decl)
             declTrigRemov++;
        }
        declCount++;
        declTrig++;

    })
    console.log("To remove " + declTrigRemov + "/" + declTrig);
});

console.log("declarations lues : " +declCount);


let deleting = 0;
db.getCollection("declarations_pb").find({}).forEach(decl => {
    console.log("Deleting count"+(deleting++));
    db.getCollection("declarations").deleteOne({ "_id": decl._id });
});