db.getCollection("declarations").find({ "codeEtat" : "R", "domaineDroits.periodeDroit.periodeFermetureDebut" : { $exists : false } }).forEach(function (doc) {
    //console.log(doc._id);
    let listDomaine = new Array();
    doc.domaineDroits.forEach(function (doc2) {
        if (doc2.periodeDroit.periodeFermetureDebut === undefined || doc2.periodeDroit.periodeFermetureDebut === null) {
            let currentYear1 = doc2.periodeDroit.periodeDebut.substring(0,4);
            let currentYear2 = 0;
            if (doc2.periodeDroit.periodeFin !== undefined && doc2.periodeDroit.periodeFin !== null) {
                currentYear2 = doc2.periodeDroit.periodeFin.substring(0,4);
            }
            if (currentYear2 > currentYear1) {
                currentYear1 = currentYear2;
            }
            let toto = doc2.periodeDroit.periodeFin.replace("/","-");
            toto = toto.replace("/","-");
            let futureDate = new Date(toto);
            //console.log(futureDate);
            futureDate.setDate(futureDate.getDate()+1)

            let month = ""
            if (futureDate.getMonth() + 1 <10) {
                month = "0"+(futureDate.getMonth() + 1);
            }
            else {
                month = futureDate.getMonth() + 1;
            }

            let day = ""
            if (futureDate.getDate()<10) {
                day = "0"+futureDate.getDate();
            }
            else {
                day = futureDate.getDate();
            }

            doc2.periodeDroit.periodeFermetureDebut = futureDate.getFullYear()+"/"+month+"/"+day;
            doc2.periodeDroit.periodeFermetureFin = currentYear1 + "/12/31";
        }
        listDomaine.push(doc2);
    });
    doc.domaineDroits = listDomaine;
    db.getCollection("declarations").save(doc);
})
