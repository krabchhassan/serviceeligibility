import logging
from os import environ
from pymongo import MongoClient


def main():
    """
    Ajout du rejet B20 dans la collection parametres
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

    logger.info("Début du traitement ajout-rejet-b20")

    parametres_collection = db["parametres"]
    b20_reject = {
        "code": "B20",
        "libelle": "INCOHERENCE DES PRIORITES AU SEIN D’UN MEME CONTRAT",
        "motif": "Deux garanties ne peuvent pas avoir le même niveau de priorité au sein du même contrat",
        "typeErreur": "M",
        "niveauErreur": "B"
    }
    update_results = parametres_collection.update_one({"code": "rejets"}, {"$addToSet": {"listeValeurs": b20_reject}})
    logger.info(f"Nombre d'élément(s) mis à jour : {update_results.modified_count}")

    logger.info("Fin du traitement ajout-rejet-b20")
