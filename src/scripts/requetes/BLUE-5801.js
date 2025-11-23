db.parametres.find({ "code": "processus" }).forEach(function (doc) {
    var newArray = [];
    doc.listeValeurs.forEach(function (doc2) {
        var aNewItem = {
            "code" : doc2.code,
            "libelle" : doc2.libelle,
        }
        newArray.push(aNewItem);
    })
    var aNewItem = {
        "code" : "CARTE-TP",
        "libelle" :"Carte Papier"
    };
    newArray.push(aNewItem);
    db.parametres.update({
        _id: doc._id
    }, {
        "$set": {
            "listeValeurs": newArray
        }
    });
});
