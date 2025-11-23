function getServicePrestationNb(beneficiaire) {
    return db.servicePrestation.countDocuments({
        "idDeclarant": beneficiaire.amc.idDeclarant,
        "assures.identite.numeroPersonne": beneficiaire.identite.numeroPersonne
    });
}

function getContratsTPNb(beneficiaire) {
    return db.contratsTP.countDocuments({
        "idDeclarant": beneficiaire.amc.idDeclarant,
        "beneficiaires.numeroPersonne": beneficiaire.identite.numeroPersonne
    });
}

function removeService(benefServices, service) {
    var index = benefServices.indexOf(service);
    if (index !== -1) {
        benefServices.splice(index, 1);
    }
}

function pushService(benefServices, service) {
    var index = benefServices.indexOf(service);
    if (index === -1) {
        benefServices.push(service);
    }
}

db.beneficiaires.find({}).forEach(function (beneficiaire) {
    var services = beneficiaire.services;
    var servicePrestationNb = getServicePrestationNb(beneficiaire);
    var contratsTPNb = getContratsTPNb(beneficiaire);
    if (servicePrestationNb === undefined || servicePrestationNb === 0) {
        removeService(services, "ServicePrestation");
    }
    if (servicePrestationNb !== undefined && servicePrestationNb >= 1) {
        pushService(services, "ServicePrestation");
    }
    if (contratsTPNb === undefined || contratsTPNb === 0) {
        removeService(services, "Service_TP");
    }
    if (contratsTPNb !== undefined && contratsTPNb >= 1) {
        pushService(services, "Service_TP");
    }
    db.beneficiaires.updateOne({
            _id: beneficiaire._id
        },
        {"$set": {"services": services}}
    )
});