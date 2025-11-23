let amcs = [];

function findAmc(value){
    let count = amcs.length;
    for(var x = 0 ; x < count ; x++){
        print(amcs[x]._id);
        if(amcs[x]._id == value){
            // print("déclarant trouvé à l indice " + x);
            return x;
        }
    }
    return -1;
}

db.servicePrestation.find().forEach( function(sp) {
    print(sp.idDeclarant);
    let indiceAmc = findAmc(sp.idDeclarant);
    print(indiceAmc);
    if (indiceAmc == -1) {
        // print('AMC Inexistante');
        let amc = {};
        amc._id = sp.idDeclarant;
        if ( sp.contexteTiersPayant && sp.contexteTiersPayant.collectivite && sp.contexteTiersPayant.collectivite.trim() !== "") {
            amc.collectivites = [sp.contexteTiersPayant.collectivite];
        } else {
            amc.collectivites = [];
        }
        if ( sp.contexteTiersPayant && sp.contexteTiersPayant.college && sp.contexteTiersPayant.college.trim() !== "") {
            amc.colleges = [sp.contexteTiersPayant.college];
        } else {
            amc.colleges = [];
        }
        if ( sp.critereSecondaireDetaille && sp.critereSecondaireDetaille.trim() !== "") {
            amc.criteresSecondaireDetaille = [sp.critereSecondaireDetaille];
        } else {
            amc.criteresSecondaireDetaille = [];
        }
        amcs.push(amc);
    } else {
        // print('AMC Existante');
        let indiceParam;
        
        if ( sp.contexteTiersPayant && sp.contexteTiersPayant.collectivite && sp.contexteTiersPayant.collectivite.trim() !== "") {
            indiceParam = amcs[indiceAmc].collectivites.indexOf(sp.contexteTiersPayant.collectivite);
            if (indiceParam == -1) {
                    amcs[indiceAmc].collectivites.push(sp.contexteTiersPayant.collectivite);
            }            
        }
        if ( sp.contexteTiersPayant && sp.contexteTiersPayant.college && sp.contexteTiersPayant.college.trim() !== "") {
            indiceParam = amcs[indiceAmc].colleges.indexOf(sp.contexteTiersPayant.college);
            if (indiceParam == -1) {
                    amcs[indiceAmc].colleges.push(sp.contexteTiersPayant.college);
            }            
        }
        if ( sp.critereSecondaireDetaille && sp.critereSecondaireDetaille.trim() !== "") {
            indiceParam = amcs[indiceAmc].criteresSecondaireDetaille.indexOf(sp.critereSecondaireDetaille);
            if (indiceParam == -1) {
                    amcs[indiceAmc].criteresSecondaireDetaille.push(sp.critereSecondaireDetaille);
            }            
        }
    }
  });
  
db.referentielParametragesCarteTP.insertMany(amcs);
print(JSON.stringify(amcs));
  