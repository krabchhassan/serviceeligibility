import os
import sys
import logging
from datetime import datetime
from pymongo import MongoClient
from uuid import uuid4

SERVICE_ELIGIBILITY_DB = 'MONGODB_DATABASE_NAME_SERVICEELIGIBILITY-CORE-API'

MONGODB_DATABASE_NAME = 'MONGODB_DATABASE_NAME'

SERVICE_ELIGIBILITY_MONGO_URI = 'MONGODB_URL_SERVICEELIGIBILITY-CORE-API'

MONGODB_URL = 'MONGODB_URL'

VERSIONS_COLLECTION = "versions"

CONTRACT_ELEMENT_COLLECTION = "contractElement"

CREATED_BY = "script-version"

logging.basicConfig(
    format="%(asctime)s %(levelname)s %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
    level=logging.INFO,
)
log = logging.getLogger(__name__)


def get_db():
    uri = os.environ.get(MONGODB_URL, os.environ.get(SERVICE_ELIGIBILITY_MONGO_URI))
    dbname = os.environ.get('%s' % MONGODB_DATABASE_NAME, os.environ.get(SERVICE_ELIGIBILITY_DB))
    if not uri:
        log.error(f"Env var {MONGODB_URL} or {SERVICE_ELIGIBILITY_MONGO_URI} are not set.")
        sys.exit(2)
    if not dbname:
        log.error(f"Env var {MONGODB_DATABASE_NAME} or {SERVICE_ELIGIBILITY_DB} are not set.")
        sys.exit(2)
    try:
        client = MongoClient(uri)
        db = client[dbname]
        return db
    except Exception as e:
        log.error(f"Cannot connect to MongoDB server: {e}")
        sys.exit(3)


def ensure_version_zero(db):
    # Doit exister : une seule version, avec number=0. Sinon on reset et on recrée v0.
    coll_names = db.list_collection_names()
    versions = db[VERSIONS_COLLECTION]

    if VERSIONS_COLLECTION in coll_names:
        total = versions.estimated_document_count()
        existing_v0 = versions.find_one({"number": 0})

        if total == 1 and existing_v0 is not None:
            log.info(
                f"STEP 1/3: '{VERSIONS_COLLECTION}' is valid (only version 0 present)."
            )
            return existing_v0

        log.warning(
            f"STEP 1/3: '{VERSIONS_COLLECTION}' invalid (total={total}, has_v0={existing_v0 is not None}). "
            "Resetting collection to a single version 0."
        )
        versions.delete_many({})

        v0_doc = generate_v0_doc()
        versions.insert_one(v0_doc)
        log.info(
            f"Recreated '{VERSIONS_COLLECTION}' with version 0 _id={v0_doc['_id']}."
        )
        return v0_doc

    # Collection doesn't exist -> create v0
    log.info(
        f"STEP 1/3: '{VERSIONS_COLLECTION}' does not exist. Creating version 0."
    )
    v0_doc = generate_v0_doc()
    versions.insert_one(v0_doc)
    log.info(
        f"Created collection '{VERSIONS_COLLECTION}' and inserted version 0 _id={v0_doc['_id']}."
    )
    return v0_doc

def generate_v0_doc():
    v0_doc = {
        "_id": str(uuid4()),
        "number": 0,
        "label": "Version 0",
        "creationDate": datetime.now(),
        "createdBy": CREATED_BY,
        "status": "ACTIVE",
        "purgeDate": None,
        "purgedBy": None
    }
    return v0_doc

def check_missing_contract_links(db, v0_id):
    # Bilan des liens et filtre des docs à corriger (manquants ou mauvais id)
    contracts = db[CONTRACT_ELEMENT_COLLECTION]
    total = contracts.estimated_document_count()

    missing_filter = {"$or": [
        {"versionId": {"$exists": False}},
        {"versionId": None}
    ]}
    wrong_filter = {"versionId": {"$exists": True, "$ne": v0_id}}

    missing = contracts.count_documents(missing_filter)
    wrong = contracts.count_documents(wrong_filter)
    correct = contracts.count_documents({"versionId": v0_id})

    fix_filter = {"$or": [missing_filter, wrong_filter]}

    log.info(
        f"STEP 2/3: contractElement stats -> total={total}, "
        f"correct={correct}, missing={missing}, wrong_old_id={wrong}"
    )
    return (missing + wrong), fix_filter


def assign_missing_links(db, fix_filter, v0_id):
    # Applique versionId = v0_id aux docs à corriger
    contracts = db[CONTRACT_ELEMENT_COLLECTION]
    res = contracts.update_many(fix_filter, {"$set": {"versionId": v0_id}})
    log.info(f"STEP 3/3: Linked/Relinked {res.modified_count} contract(s) to v0.")
    return res.modified_count


def create_version_index(db):
    """Crée un index sur le champ versionId pour optimiser les requêtes de purge"""
    contracts = db[CONTRACT_ELEMENT_COLLECTION]

    try:
        # Création de l'index ascendant sur le champ versionId
        index_name = contracts.create_index([("versionId", 1)])
        log.info(f"Index créé avec succès: {index_name}")
        return index_name
    except Exception as e:
        log.error(f"Erreur lors de la création de l'index: {e}")
        return None
def main():
    db = get_db()
    v0 = ensure_version_zero(db)
    v0_id = v0["_id"]

    create_version_index(db)

    to_fix_count, fix_filter = check_missing_contract_links(db, v0_id)
    if to_fix_count > 0:
        assign_missing_links(db, fix_filter, v0_id)
    else:
        log.info("STEP 3/3: All contracts already point to the current v0.")
    log.info("DONE.")
