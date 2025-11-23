import logging
from os import environ
from pymongo import MongoClient, ASCENDING

logging.basicConfig(format='%(asctime)s %(levelname)s %(message)s', datefmt='%Y-%m-%d %H:%M:%S', level=logging.INFO)
logger = logging.getLogger()

uri_mongo = environ.get('MONGODB_URL')
clientType = environ.get('CLIENT_TYPE')

try:
    db = MongoClient(uri_mongo).get_default_database()
    logger.debug("Connexion réussie à la base de données")
except Exception as e:
    logger.error(f"Problème lors de la connexion à MongoDB : {e}")
    raise

logger.info("Début du traitement creation des index retention")
logger.info(f"Client Type : {clientType}")
if clientType == 'INSURER':
    retention_collection = db["retention"]
    keys=[('insurerId', ASCENDING), ('subscriberNumber', ASCENDING), ('contractNumber', ASCENDING),
          ('personNumber', ASCENDING),('status', ASCENDING)]
    retention_collection.create_index( keys=keys, name="retention01")
    keys=[('insurerId', ASCENDING), ('receptionDate', ASCENDING),('status', ASCENDING)]
    retention_collection.create_index( keys=keys, name="retention02")

logger.info("Fin du traitement creation des index retention")
