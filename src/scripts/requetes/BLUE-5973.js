const copyName = "_BLUE-5973";
db.declarations.aggregate([{$out:"declarations"+copyName}]);
db.contrats.aggregate([{$out:"contrats"+copyName}]);
db.cartesDemat.aggregate([{$out:"cartesDemat"+copyName}]);
db.declarationsConsolideesCarteDemat.aggregate([{$out:"declarationsConsolideesCarteDemat"+copyName}]);
db.parametragesCarteTP.aggregate([{$out:"parametragesCarteTP"+copyName}]);


const is = "IS";
db.declarations.updateMany(
    {
        "contrat.typeConvention": { $ne: is }
    },
    {
        $set: { "contrat.typeConvention": is }
    });

db.contrats.updateMany(
    {
        "typeConvention": { $ne: is }
    },
    {
        $set: { "typeConvention": is }
    });

db.cartesDemat.updateMany(
    {
        "contrat.typeConvention": { $ne: is }
    },
    {
        $set: { "contrat.typeConvention": is }
    });

db.declarationsConsolideesCarteDemat.updateMany(
    {
        "contrat.typeConvention": { $ne: is }
    },
    {
        $set: { "contrat.typeConvention": is }
    });

const detailsToRemove = ["RADL", "AUXM", "SAGE", "LABO"];
db.parametragesCarteTP.updateMany(
    {
        "parametrageDroitsCarteTP.codeConventionTP": { $ne: is }
    }, {
        $set: { "parametrageDroitsCarteTP.codeConventionTP": is },
        $pull: { "parametrageDroitsCarteTP.detailsDroit": { "codeDomaineTP": { $in: detailsToRemove } } }
    });