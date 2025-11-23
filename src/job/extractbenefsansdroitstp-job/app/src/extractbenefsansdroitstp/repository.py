import pymongo

from extractbenefsansdroitstp.read_aggregation_files import get_json_aggregation_with_date_from_file, \
    get_json_aggregation_other_contracts_from_file

HISTORIQUE_EXECUTIONS_COLLECTION = "historiqueExecutions"
SERVICE_PRESTATION_COLLECTION = "servicePrestation"


class Repository:

    def __init__(self):
        from extractbenefsansdroitstp.mongodb_manager import MongoDBManager

        mongodb_manager = MongoDBManager()
        self.db = mongodb_manager.get_db()

    def execute_aggregation(self, pipeline, collection_name, batch_size=None, allow_disk_use=False):
        collection = self.db[collection_name]
        return collection.aggregate(pipeline, allowDiskUse=allow_disk_use, batchSize=batch_size)

    def get_benefs_without_tp(self, year):
        return self.execute_aggregation(
            pipeline=get_json_aggregation_with_date_from_file("benefSansDroitsTP.json", f"{year}-01-01",
                                                              f"{year - 1}-12-31"),
            collection_name=SERVICE_PRESTATION_COLLECTION, batch_size=1000)

    def get_others_contracts(self, benef_without_rights, year):
        id_declarant = benef_without_rights["idDeclarant"]
        numero_personne = benef_without_rights["numeroPersonne"]
        pipeline = get_json_aggregation_other_contracts_from_file("benefSansDroitsTPAvecAutresContrats.json",
                                                                  id_declarant,
                                                                  numero_personne,
                                                                  f"{year}-01-01",
                                                                  f"{year - 1}-12-31")
        pipeline.insert(1, {
            "$match": {
                "_id": {
                    "$ne": benef_without_rights["_id"]
                }
            }
        })
        return self.execute_aggregation(pipeline=pipeline, collection_name=SERVICE_PRESTATION_COLLECTION)

    def save_histo_exec(self, histo_exec):
        histo_exec_collection = self.db[HISTORIQUE_EXECUTIONS_COLLECTION]
        histo_exec_collection.insert_one(histo_exec)

    def get_last_histo_exec(self):
        histo_exec_collection = self.db[HISTORIQUE_EXECUTIONS_COLLECTION]
        cursor = histo_exec_collection.find({'Batch': 'benefsanstp'}).sort('_id', pymongo.DESCENDING).limit(1)
        for histo_exec in cursor:
            return histo_exec
        return None
