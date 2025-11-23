import logging
from os import environ
from pymongo import MongoClient

def main():
    """
    Ajout de nouvelles valeurs dans le référentiel des rejets en base
    """
    logging.basicConfig(format='%(asctime)s %(levelname)s %(message)s', datefmt='%Y-%m-%d %H:%M:%S', level=logging.INFO)
    logger = logging.getLogger()

    uri_mongo = environ.get('MONGODB_URL_SERVICEELIGIBILITY-CORE-API')

    try:
        db = MongoClient(uri_mongo).get_default_database()
        logger.debug("Connexion réussie à la base de données")
    except Exception as e:
        logger.error(f"Problème lors de la connexion à MongoDB : {e}")
        raise

    logger.info("Début du traitement init-referentiel-rejets")

    # Référentiel des rejets à ajouter
    nouveaux_rejets = [
        {
            "code": "P20",
            "libelle": "GARANTIE NON PARAMETREE DANS LE PARAMETRAGE DE PRODUIT ALMERYS",
            "motif": "La garantie n’est pas paramétrée dans au moins une combinaison de paramétrage de produit Almerys",
            "typeErreur": "S",
            "niveauErreur": "B"
        },
        {
            "code": "P21",
            "libelle": "DOUBLON DE PRODUIT ALMERYS",
            "motif": "Les garanties Almerys souscrites par l’assuré permettent de déterminer plusieurs produits Almerys",
            "typeErreur": "S",
            "niveauErreur": "B"
        },
        {
            "code": "O01",
            "libelle": "ORGANISATION PRINCIPALE NON TROUVEE",
            "motif": "Impossible de déterminer une Organisation Principale liée à l’AMC",
            "typeErreur": "R",
            "niveauErreur": "B"
        },
        {
            "code": "O02",
            "libelle": "ORGANISATION SECONDAIRE NON TROUVEE",
            "motif": "Impossible de déterminer une Organisation Secondaire liée à l’AMC",
            "typeErreur": "R",
            "niveauErreur": "B"
        },
        {
            "code": "O03",
            "libelle": "ORGANISATION SECONDAIRE NON RATTACHEE A L ORGANISATION PRINCIPALE",
            "motif": "L’Organisation Secondaire n’est pas une Organisation Secondaire de l’Organisation Principale",
            "typeErreur": "R",
            "niveauErreur": "B"
        },
        {
            "code": "O04",
            "libelle": "PROBLEME TECHNIQUE LORS DE L APPEL A ORGANISATION",
            "motif": "Erreur de la récupération des informations du module Organisation",
            "typeErreur": "R",
            "niveauErreur": "B"
        }
    ]

    parametres_collection = db["parametres"]

    # Recherche du document ayant pour code "rejets"
    doc_rejets = parametres_collection.find_one({"code": "rejets"})

    if doc_rejets:
        logger.info("Mise à jour en cours du referentiel de rejets...")

        # Obtenir les codes rejets déjà présents
        rejets_actuels = {r["code"] for r in doc_rejets.get("listeValeurs", [])}

        # Filtrer les nouveaux rejets à ajouter
        rejets_a_ajouter = [r for r in nouveaux_rejets if r["code"] not in rejets_actuels]

        if rejets_a_ajouter:
            parametres_collection.update_one(
                {"code": "rejets"},
                {"$push": {"listeValeurs": {"$each": rejets_a_ajouter}}}
            )
            logger.info(f"Ajouté {len(rejets_a_ajouter)} nouveaux codes de rejet.")
        else:
            logger.info("Aucun nouveau code à ajouter, tout est déjà en base.")
    else:
        logger.info("Le referentiel de rejets n'existe pas, création en cours...")
        parametres_collection.insert_one({
            "code": "rejets",
            "listeValeurs": nouveaux_rejets
        })
        logger.info("Le referentiel de rejets a été créé avec les nouveaux rejets.")

    logger.info("Fin du traitement init-referentiel-rejets")
