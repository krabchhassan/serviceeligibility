import logging
from os import environ
from pymongo import MongoClient, ASCENDING

def main():
    """
    insère l'index tracesFlux01 en base si non présent
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

    logger.info("Début du traitement init_index_traces_flux")

    traces_flux_collection = db["tracesFlux"]
    keys = [('infoFichierEmis.nomFichier', ASCENDING)]
    try:
        traces_flux_collection.create_index(keys=keys, name="tracesFlux03")
    except Exception as e:
        logger.error(f"Problème lors de la connexion à MongoDB : {e}")


    logger.info("Fin du traitement init_index_traces_flux")
