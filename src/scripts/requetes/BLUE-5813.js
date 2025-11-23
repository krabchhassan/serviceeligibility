const rejetsToAdd = [
    {
        "code": "C18",
        "libelle": "AUCUN DROIT OUVERT POUR L ENSEMBLE DES DECLARATIONS CONSOLIDEES DU CONTRAT X",
        "motif": "Aucun droit ouvert sur l'ensemble des déclarations consolidées",
        "typeErreur": "S",
        "niveauErreur": "B"
    },
    {
        "code": "C19",
        "libelle": "IMPOSSIBLE DE DEFINIR UNE ADRESSE POUR LE CONTRAT X",
        "motif": "Pas d'adresse valide définie dans le contrat (beneficiaire.adresses)",
        "typeErreur": "S",
        "niveauErreur": "B"
    },
    {
        "code": "C20",
        "libelle": "LES DOMAINES TP ONT DES CODES RENVOIS, CODES RENVOIS ADDITIONNELS, UNITES DE TAUX OU CONVENTIONNEMENTS DIFFERENTS",
        "motif": "Codes renvois, codes renvois additionnels, unites de taux ou conventionnements différents dans les domaines TP",
        "typeErreur": "S",
        "niveauErreur": "B"
    },
    {
        "code": "C21",
        "libelle": "LE PARAMETRAGE DE REGROUPEMENT DOIT ETRE IDENTIQUE MAIS LA NATURE Y EST ABSENTE",
        "motif": "Impossible de regrouper les domaines : Nature absente pour le paramétrage de regroupement",
        "typeErreur": "S",
        "niveauErreur": "B"
    },
    {
        "code": "C22",
        "libelle": "LE PARAMÉTRAGE DE REGROUPEMENT DOIT ÊTRE IDENTIQUE MAIS LES TAUX NE SONT PAS EGAUX",
        "motif": "Impossible de regrouper les domaines : Taux différents pour le paramétrage de regroupement",
        "typeErreur": "S",
        "niveauErreur": "B"
    },
    {
        "code": "C23",
        "libelle": "LE PARAMETRAGE S3 NE PERMET PAS D EXTRAIRE LA CARTE PAPIER",
        "motif": "Pas de fichier de paramétrage valide sur S3",
        "typeErreur": "S",
        "niveauErreur": "B"
    },
    {
        "code": "C24",
        "libelle": "LES DOMAINES TP NE SONT PAS LES MEMES POUR TOUS LES BENEFICIAIRES",
        "motif": "Les domaines TP ne sont pas les mêmes pour tous les bénéficiaires alors que le paramétrage indique un niveau de remboursement identique"
    },
    {
        "code": "C25",
        "libelle": "LE CODE OFFRE ITELIS N EST PAS LE MEME POUR TOUS LES BENEFICIAIRES",
        "motif": "Le code offre ITELIS n'est pas le même pour l'ensemble des bénéficiaires du contrat"
    }
]

db.parametres.updateMany(
    {
        "code": "rejets"
    },
    {
        $addToSet: {
            "listeValeurs": {
                $each: rejetsToAdd
            }
        }
    }
)
