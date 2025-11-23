db.servicePrestation.find({
    "periodesContratResponsableOuvert": {
        $exists: true,
        $ne: []
    }
}).forEach(function (servicePrestation) {
    let idDeclarant = servicePrestation.idDeclarant;
    let numeroContrat = servicePrestation.numero;
    let periodesContratResponsableOuvert = servicePrestation.periodesContratResponsableOuvert.map(periode => ({
        debut: formatDate(periode.debut),
        fin: periode.fin ? formatDate(periode.fin) : null
    }));

    // Traitement pour les declarations
    db.declarations.find({
        "idDeclarant": idDeclarant,
        "contrat.numero": numeroContrat
    }).forEach(function (declaration) {
        // Trouver periodeDroitDebut
        let periodeDroitDebut = declaration.domaineDroits
            .map(droit => formatDate(droit.periodeDroit.periodeDebut))
            .reduce((min, date) => date < min ? date : min, formatDate(declaration.domaineDroits[0].periodeDroit.periodeDebut));

        // On vérifie si "periodeDroitDebut" se trouve dans une des periodesContratResponsableOuvert
        let isInContratResponsable = isDateInPeriod(periodeDroitDebut, periodesContratResponsableOuvert);

        // Mise à jour des declarations
        if (isInContratResponsable !== declaration.contrat.isContratResponsable) {
            db.declarations.update(
                { _id: declaration._id },
                { $set: { "contrat.isContratResponsable": isInContratResponsable } }
            );
        }
    });

    // Traitement pour les contrats
    db.contrats.find({
        "idDeclarant": idDeclarant,
        "numeroContrat": numeroContrat
    }).forEach(function (contrat) {
        // Trouver periodeDroitDebut
        let allPeriodeDebuts = contrat.beneficiaires.flatMap(beneficiaire =>
            beneficiaire.domaineDroits.flatMap(domaineDroit =>
                domaineDroit.periodesDroit.map(periode => formatDate(periode.periodeDebut))
            )
        );
        let periodeDroitDebut = allPeriodeDebuts.reduce((min, date) => date < min ? date : min, allPeriodeDebuts[0]);

        // Vérifier si "periodeDroitDebut" se trouve dans l'une des periodesContratResponsableOuvert
        let isInContratResponsable = isDateInPeriod(periodeDroitDebut, periodesContratResponsableOuvert);

        // Mise à jour des contrats
        if (isInContratResponsable !== contrat.isContratResponsable) {
            db.contrats.update(
                { _id: contrat._id },
                { $set: { "isContratResponsable": isInContratResponsable } }
            );
        }
    });

    // Traitement pour les cartesDemat
    db.cartesDemat.find({
        "idDeclarant": idDeclarant,
        "contrat.numero": numeroContrat
    }).forEach(function (carteDemat) {
        let periodeDroitDebut = formatDate(carteDemat.periodeDebut);

        // Vérifier si "periodeDroitDebut" se trouve dans l'une des periodesContratResponsableOuvert
        let isInContratResponsable = isDateInPeriod(periodeDroitDebut, periodesContratResponsableOuvert);

        // Mise à jour des cartesDemat
        if (isInContratResponsable !== carteDemat.contrat.isContratResponsable) {
            db.cartesDemat.update(
                { _id: carteDemat._id },
                { $set: { "contrat.isContratResponsable": isInContratResponsable } }
            );
        }
    });
});

function isDateInPeriod(date, periods) {
    return periods.some(function (periode) {
        let debutContrat = new Date(periode.debut);
        let finContrat = periode.fin ? new Date(periode.fin) : null;
        return debutContrat <= date && (finContrat === null || date <= finContrat);
    });
}

function formatDate(dateString) {
    return new Date(dateString.replace(/\//g, '-'));
}
