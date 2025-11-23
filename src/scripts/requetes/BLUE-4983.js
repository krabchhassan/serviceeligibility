var reduceFunction = function(str){
    result = "";
    source = str.trim().split(" ");
    for (var idx = 0; idx < source.length; idx++) {
        if (result!=""){
            result+="_";
        }
       result += source[idx];
   }
   return result;
};

db.servicePrestation.find({ "codeOc": { $regex: " " } }).forEach(function( doc ) {
    let codeOc = doc.codeOc;
    doc.codeOc = reduceFunction(codeOc);
    db.servicePrestation.save(doc)
});

db.declarations.find({}).forEach(function( doc ) {
    for (var idz = 0; idz < doc.domaineDroits.length; idz++) {
        let codeOc = doc.domaineDroits[idz].codeOc;
        doc.domaineDroits[idz].codeOc = reduceFunction(codeOc);
    }
    db.declarations.save(doc)
});

db.contrats.find({}).forEach(function( doc ) {
    for (var idx = 0; idx < doc.beneficiaires.length; idx++) {
        for (var idy = 0; idy < doc.beneficiaires[idx].domaineDroits.length; idy++) {
            for (var idz = 0; idz < doc.beneficiaires[idx].domaineDroits[idy].periodesDroit.length; idz++) {
                let codeOc = doc.beneficiaires[idx].domaineDroits[idy].periodesDroit[idz].codeOc;
                doc.beneficiaires[idx].domaineDroits[idy].periodesDroit[idz].codeOc = reduceFunction(codeOc);
            }
        }
    }
    db.contrats.save(doc)
});