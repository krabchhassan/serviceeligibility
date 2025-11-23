import copy
import logging
from os import environ
from collections import defaultdict
from datetime import datetime
from pymongo import MongoClient

logging.basicConfig(format='%(asctime)s %(levelname)s %(message)s', datefmt='%Y-%m-%d %H:%M:%S', level=logging.INFO)
logger = logging.getLogger()

db_url = environ.get('MONGODB_URL', environ.get('MONGODB_URL_SERVICEELIGIBILITY-CORE-API'))
dbname = environ.get('MONGODB_DATABASE_NAME', environ.get('MONGODB_DATABASE_NAME_SERVICEELIGIBILITY-CORE-API'))

if not db_url:
    logger.error("db_url n'est pas défini ou est vide.")
if not dbname:
    logger.error("dbname n'est pas défini ou est vide.")

#connect to BDD
try:
    client = MongoClient(db_url)
    db = client[dbname]
    logger.debug(f"Connexion réussie à la base de données {dbname}")
except Exception as e:
    logger.error(f"Erreur lors de la connexion à MongoDB : {e}")
    raise e

declarants_collection = db["declarants"]
parametres_collection = db["parametres"]
services_collection = db["services"]
transcodage_collection = db["transcodage"]
historique_executions_collection = db["historiqueExecutions"]


def update_declarants():
    logger.debug(f"Mise à jour des declarants...")
    documents = declarants_collection.find({"pilotages.codeService": "DCLBEN"})
    count = 0

    for doc in documents:
        modified = False
        for pilotage in doc["pilotages"]:
            if pilotage["codeService"] == "DCLBEN":
                type_conventionnement = pilotage.get("typeConventionnement", "IS")
                pilotage["codeService"] = "DCLBEN-IS" if type_conventionnement == "IS" else "DCLBEN-SP"
                modified = True

        if modified:
            declarants_collection.update_one({"_id": doc["_id"]}, {"$set": {"pilotages": doc["pilotages"]}})
            count += 1

    logger.info(f"{count} documents modifiés dans 'declarants'.")


def update_parametres():
    logger.debug(f"Mise à jour des parametres...")
    documents = parametres_collection.find({"listeValeurs.code": "DCLBEN"})
    count = 0

    for doc in documents:
        nouvelle_liste = [val for val in doc["listeValeurs"] if val["code"] != "DCLBEN"]
        nouvelle_liste.append({"code": "DCLBEN-IS", "libelle": "DCLBEN-IS"})
        nouvelle_liste.append({"code": "DCLBEN-SP", "libelle": "DCLBEN-SP"})

        parametres_collection.update_one({"_id": doc["_id"]}, {"$set": {"listeValeurs": nouvelle_liste}})
        count += 1

    logger.info(f"{count} documents mis à jour dans 'parametres'.")


def update_services():
    logger.debug(f"Mise à jour des services...")
    document_service = services_collection.find_one({"code": "DCLBEN"})

    if document_service:
        service_IS = copy.deepcopy(document_service)
        service_SP = copy.deepcopy(document_service)

        service_IS.pop("_id", None)
        service_SP.pop("_id", None)

        service_IS["code"] = "DCLBEN-IS"
        service_SP["code"] = "DCLBEN-SP"

        services_collection.insert_many([service_IS, service_SP])
        services_collection.delete_one({"code": "DCLBEN"})

        logger.info(f"Service 'DCLBEN' remplacé par 'DCLBEN-IS' et 'DCLBEN-SP'.")
    else:
        logger.debug(f"Aucun service 'DCLBEN' trouvé.")


def update_transcodage():
    logger.debug(f"Mise à jour des transcodages...")
    documents = transcodage_collection.find({"codeService": "DCLBEN"})
    new_transcodages = []
    count = 0

    for doc in documents:
        transcodage_IS = copy.deepcopy(doc)
        transcodage_SP = copy.deepcopy(doc)

        transcodage_IS.pop("_id", None)
        transcodage_SP.pop("_id", None)

        transcodage_IS["codeService"] = "DCLBEN-IS"
        transcodage_SP["codeService"] = "DCLBEN-SP"

        new_transcodages.append(transcodage_IS)
        new_transcodages.append(transcodage_SP)

    if new_transcodages:
        transcodage_collection.insert_many(new_transcodages)
        delete_result = transcodage_collection.delete_many({"codeService": "DCLBEN"})
        count = delete_result.deleted_count
        logger.info(f"{count} anciens transcodages 'DCLBEN' supprimés et remplacés.")
    else:
        logger.debug(f"Aucun transcodage 'DCLBEN' trouvé.")


def duplicate_last_execution():
    logger.debug(f"Duplication des dernières exécutions du batch 610 par typeConventionnement...")

    all_executions = list(historique_executions_collection.find(
        {"Batch": "610", "codeService": "DCLBEN"}
    ))

    grouped_by_declarant = defaultdict(list)

    for execution in all_executions:
        id_declarant = execution.get("idDeclarant")
        grouped_by_declarant[id_declarant].append(execution)

    all_executions = list(grouped_by_declarant.values())

    if not all_executions:
        logger.debug(f"Aucune exécution du batch 610 avec 'DCLBEN' trouvée.")
        return

    for executions in all_executions:
        for execution in executions:
            if isinstance(execution["dateExecution"], str):
                try:
                    execution["dateExecution"] = datetime.fromisoformat(execution["dateExecution"])
                except ValueError:
                    logger.error(f"Erreur de format de date : {execution['dateExecution']}")
                    return

        executions.sort(key=lambda x: x["dateExecution"], reverse=True)

        last_execution_IS = next((e for e in executions if e.get("typeConventionnement") == "IS"), None)
        last_execution_SP = next((e for e in executions if e.get("typeConventionnement") == "SP"), None)

        def duplicate_execution(last_execution, new_code_service):
            if not last_execution:
                return

            existing_duplicate = historique_executions_collection.find_one({
                "Batch": "610",
                "codeService": new_code_service,
                "dateExecution": last_execution["dateExecution"],
                "idDeclarant": last_execution["idDeclarant"],
                "numeroFichier": last_execution["numeroFichier"]
            })

            if existing_duplicate:
                logger.info(f"L'exécution du {last_execution['dateExecution']} pour l'amc {last_execution['idDeclarant']} avec {new_code_service} a déjà été dupliquée.")
                return

            new_execution = copy.deepcopy(last_execution)
            new_execution.pop("_id", None)
            new_execution["codeService"] = new_code_service

            historique_executions_collection.insert_one(new_execution)

            logger.info(f"Exécution du {last_execution['dateExecution']} pour l'amc {last_execution['idDeclarant']} dupliquée en {new_code_service}.")

        duplicate_execution(last_execution_IS, "DCLBEN-IS")
        duplicate_execution(last_execution_SP, "DCLBEN-SP")


def main():
    logger.info(f"\nDébut du script de mise à jour...\n")
    update_declarants()
    update_parametres()
    update_services()
    update_transcodage()
    duplicate_last_execution()
    logger.info(f"\nScript terminé avec succès.\n")
    client.close()
