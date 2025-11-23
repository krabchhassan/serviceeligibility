import logging
from pymongo import MongoClient, UpdateOne

SAS_CONTRAT_COLLECTION = "sasContrat"
TRIGGERED_BENEFICIARY_COLLECTION = "triggeredBeneficiary"
TRIGGERS_BENEFS = "triggersBenefs"
ID = "_id"
BENEFS_ID = "benefsId"
BENEFS_INFOS = "benefsInfos"
BATCH_SIZE = 100

logging.basicConfig(format='%(asctime)s %(levelname)s %(message)s', datefmt='%Y-%m-%d %H:%M:%S', level=logging.INFO)
logger = logging.getLogger()


def add_num_personne_to_existing_sas_contrats(db):
    """
    Pour chaque sasContrat existant, modifie la liste sasContrat.triggersBenefs.benefsId en
    sasContrat.triggersBenefs.benefsInfos en y ajoutant le numeroPersonne.
    Les modifications en base sont effectuées par lot de BATCH_SIZE

    :param db: client MongoDb
    """
    nb_sas_contrat_updated = 0
    batch_updates = []
    sas_contrat_collection = db[SAS_CONTRAT_COLLECTION]
    for sas_contrat in sas_contrat_collection.find():
        if TRIGGERS_BENEFS in sas_contrat:
            update_sas_contrat_with_num_personne(db, sas_contrat)
            batch_updates.append(UpdateOne({ID: sas_contrat[ID]},
                                           {"$set": {TRIGGERS_BENEFS: sas_contrat[TRIGGERS_BENEFS]}}))

        if len(batch_updates) == BATCH_SIZE:
            sas_contrat_collection.bulk_write(batch_updates)
            nb_sas_contrat_updated += BATCH_SIZE
            batch_updates = []

    if batch_updates:
        sas_contrat_collection.bulk_write(batch_updates)
        nb_sas_contrat_updated += len(batch_updates)

    logger.info(f"Nombre de sasContrat mis à jour : {nb_sas_contrat_updated}")


def update_sas_contrat_with_num_personne(db, sas_contrat):
    """
    Modifie la structure d'un sasContrat : l'objet sasContrat.triggersBenefs.benefsId contenant une liste
    d'identifiants technique de triggeredBeneficiary devient l'objet sasContrat.triggersBenefs.benefsInfos contenant
    une liste d'identifiants technique de triggeredBeneficiary et de numéros de personne

    :param db: client MongoDb
    :param sas_contrat: document de la collection sasContrat
    """
    for trigger_benef in sas_contrat[TRIGGERS_BENEFS]:
        if BENEFS_ID in trigger_benef:
            benefs_infos = []
            for triggered_beneficiary_id in trigger_benef[BENEFS_ID]:
                numero_personne = get_numero_personne(db, triggered_beneficiary_id)
                if numero_personne is None:
                    logger.warning(f"Le sasContrat {sas_contrat[ID]} est mis à jour avec un numeroPersonne à null")
                benef_infos = {
                    "benefId": triggered_beneficiary_id,
                    "numeroPersonne": numero_personne
                }
                benefs_infos.append(benef_infos)
            del trigger_benef[BENEFS_ID]
            trigger_benef[BENEFS_INFOS] = benefs_infos


def get_numero_personne(db, triggered_beneficiary_id):
    """
    Récupère la valeur du numeroPersonne contenu dans le triggeredBenificary ayant l'_id 'triggered_beneficiary_id'

    :param db: client MongoDb
    :param triggered_beneficiary_id: identifiant technique d'un triggeredBeneficiary
    :return: le numeroPersonne contenu dans le triggeredBeneficiary
    """
    from bson import ObjectId

    triggered_beneficiary_collection = db[TRIGGERED_BENEFICIARY_COLLECTION]
    cursor_trigger_benef = triggered_beneficiary_collection.find({ID: ObjectId(triggered_beneficiary_id)})
    for trigger_benef in cursor_trigger_benef:
        return trigger_benef["numeroPersonne"]
    logger.warning(f"Pas de triggeredBeneficiary trouvé pour l'_id {triggered_beneficiary_id}")
    return None


def create_new_index_for_sas_contrat(db):
    """
    Ajoute un nouvel index sur le triggersBenefs.benefsInfos.numeroPersonne pour la collection sasContrat

    :param db: client MongoDb
    """
    sas_contrat_collection = db[SAS_CONTRAT_COLLECTION]
    sas_contrat_collection.create_index("triggersBenefs.benefsInfos.numeroPersonne", name="sasContratNumPersonne")


def main():
    """
    Met à jour les documents de la collection sasContrat en ajoutant le numeroPersonne pour chaque triggerBenefs
    Modification de sasContrat.triggersBenefs.benefsId en sasContrat.triggersBenefs.benefsInfos
    Avec benefsInfos correspondant à une liste d'objet : { "benefId" : "xx", "numeroPersonne" : "xx" }
    Et crée un nouvel index sur la collection sasContrat
    """
    from os import environ

    uri_mongo = environ.get('MONGODB_URL_SERVICEELIGIBILITY-CORE-API')

    try:
        db = MongoClient(uri_mongo).get_default_database()
        logger.debug("Connexion réussie à la base de données")
    except Exception as e:
        logger.error(f"Problème lors de la connexion à MongoDB : {e}")
        raise

    logger.info("Début du traitement d'ajout des numeroPersonne dans les sasContrat existant")
    add_num_personne_to_existing_sas_contrats(db)
    logger.info("Fin du traitement d'ajout des numeroPersonne dans les sasContrat existant")

    logger.info("Création de l'index sur le numeroPersonne pour la collection sasContrat")
    create_new_index_for_sas_contrat(db)
    logger.info("Index créé - Saao terminé")
