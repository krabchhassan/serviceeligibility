import logging
from os import environ
from pymongo import MongoClient

def main():
    """
    Positionne la donnée "delaiRetention" à "3" sur les déclarants existant en base
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

    logger.info("Début du traitement init-delai-retention")

    declarants_collection = db["declarants"]
    update_results = declarants_collection.update_many( {"delaiRetention" : None }, { "$set": {"delaiRetention" : "3"} })
    logger.info(f"Nombre de declarants mis à jour : {update_results.modified_count}")

    logger.info("Fin du traitement init-delai-retention")