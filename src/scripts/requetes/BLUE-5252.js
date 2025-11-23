lpad = function (str) {
    result = "";
    if (str!=""){
        for (var idx = str.length; idx < 10; idx++) {
            result ="0"+result;
        }
        result+=str;
    }
    else {
        return str;
    }
    return result;
}
      
db.servicePrestation.find({}).forEach(function( doc ) {
    if (doc.societeEmettrice!=""){
        doc.societeEmettrice = lpad(doc.societeEmettrice);
        db.servicePrestation.save(doc)
    }
});

db.declarations.find({}).forEach(function( doc ) {
    if (doc.contrat.gestionnaire!=""){
        doc.contrat.gestionnaire = lpad(doc.contrat.gestionnaire);
        db.declarations.save(doc)
    }
});

db.contrats.find({}).forEach(function( doc ) {
    if (doc.gestionnaire!=""){
        doc.gestionnaire = lpad(doc.gestionnaire);
        db.contrats.save(doc)
    }
});

db.beneficiaires.find({}).forEach(function( doc ) {
    for (var idz = 0; idz < doc.contrats.length; idz++) {
        doc.contrats[idz].societeEmettrice = lpad(doc.contrats[idz].societeEmettrice);
    }
    db.beneficiaires.save(doc)
});