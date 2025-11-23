import logging
from os import environ
from pymongo import MongoClient, ASCENDING


def main():
    """
    insère les index beneficiaires13 et carteDemat06 en base si non présent
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

    logger.info("Début du traitement init_index_benef_et_carte_demat")

    beneficiaires_collection = db["beneficiaires"]
    keys = [('amc.idDeclarant', ASCENDING), ('numeroAdherent', ASCENDING), ('contrats.numeroContrat', ASCENDING),
            ('identite.numeroPersonne', ASCENDING)]
    try:
        beneficiaires_collection.create_index(keys=keys, name="beneficiaires13")
    except Exception as e:
        logger.error(f"Problème lors de la connexion à MongoDB : {e}")

    cartes_demat_collection = db["cartesDemat"]
    keys = [('AMC_contrat', ASCENDING), ('idDeclarant', ASCENDING),
            ('beneficiaires.beneficiaire.numeroPersonne', ASCENDING)]
    try:
        cartes_demat_collection.create_index(keys=keys, name="carteDemat06")
    except Exception as e:
        logger.error(f"Problème lors de la connexion à MongoDB : {e}")

    logger.info("Fin du traitement init_index_benef_et_carte_demat")
