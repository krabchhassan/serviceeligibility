// Le script est disponible sous Gitlab et permet de modifier les contrat HTP dont le motif de suspension ne respecte pas les règles suivantes :
//      Motif différent de
//          - NON_PAIEMENT_COTISATIONS
//          - PORTABILITE_NON_JUSTIFIEE
//      Motif valorisé à PORTABILITE_NON_JUSTIFIEE et type de suspension valorisé à Definitif
// Le script positionne le motif de suspension à NON_PAIEMENT_COTISATIONS

const NON_PAIEMENT_COTISATIONS = "NON_PAIEMENT_COTISATIONS";
const PORTABILITE_NON_JUSTIFIEE = "PORTABILITE_NON_JUSTIFIEE";
const DEFINITIF = "Definitif";

db.servicePrestation.updateMany(
    { "periodesSuspension.0": { $exists: true } },
    {
        $set: { "periodesSuspension.$[periode].motifSuspension": NON_PAIEMENT_COTISATIONS }
    },
    {
        arrayFilters: [{
            $or: [
                { "periode.motifSuspension": { $nin: [NON_PAIEMENT_COTISATIONS, PORTABILITE_NON_JUSTIFIEE] } },
                {
                    $and: [
                        { "periode.motifSuspension": PORTABILITE_NON_JUSTIFIEE },
                        { "periode.typeSuspension": DEFINITIF }
                    ]
                }
            ]
        }]
    }
);
