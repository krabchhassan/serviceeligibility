import logging
from os import environ

from pymongo import MongoClient


def main():
    """
    Ajout de nouvelles valeurs dans le transcodage en base
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

    logger.info("Début du traitement init-mode-paiement")

    transcodage_collection = db["transcodage"]

    transcodage_collection.insert_one({
                                          "codeService": "ALMV3",
                                          "codeObjetTransco": "Mode_Paiement",
                                          "cle":
                                          [
                                              "E"
                                          ],
                                          "codeTransco": "NM"
                                      })
