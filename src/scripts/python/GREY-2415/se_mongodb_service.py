import pandas as pd
from beyond_analysis_resource_client.services import MongoDBService
from beyond_analysis_resource_client.utils import BaseLogging
from pymongo import MongoClient


class SEMongoDBService(object):
    def __init__(self, nature):
        self.logger = BaseLogging().get_logger()
        self.mongodb_service = MongoDBService(nature)
        if nature == "prod":
            self.client = MongoClient(self.mongodb_service.get_connection_uri("admin02"))
        else:
            self.client = MongoClient(self.mongodb_service.get_connection_uri("default"))

    def deduplicate(self, list_of_dict):
        seen = set()
        new_l = []
        for d in list_of_dict:
            t = tuple(d.items())
            if t not in seen:
                seen.add(t)
                new_l.append(d)
        return new_l
    def execute(self, database, collection, query):
        return self.client[database][collection].find(query)

    def get_page(self, database, collection, projection, last_id, page_size):
        """Function returns `page_size` number of documents after last_id
        and the new last_id.
        """
        if last_id is None:
            # When it is first page
            cursor = self.client[database][collection].find({}, projection).sort("_id", 1).limit(page_size)
        else:
            cursor = self.client[database][collection].find({'_id': {'$gt': last_id}}, projection).sort("_id", 1).limit(page_size)

        # Get the data
        data = [x for x in cursor]

        if not data:
            # No documents left
            return None, None

        # Since documents are naturally ordered with _id, last document will
        # have max id.
        last_id = data[-1]['_id']

        # Return data and last_id
        return data, last_id

    def get_contrat(self, database, page_size):
        result = list()
        collection = "contrats"
        fields_service_prestations = [
            "idDeclarant",
            "numeroContrat",
            "numeroAdherent"
        ]
        projection_service_prestations = dict()
        fields_service_prestations_description = dict()
        for f in fields_service_prestations:
            projection_service_prestations[f] = 1
            fields_service_prestations_description[f] = dict(
                level=f.count(".") - 1,
                path=f.split(".")[1:]
            )
        data, last_id = self.get_page(database, collection, projection_service_prestations, None, page_size)
        while last_id is not None:
            self.logger.info(f"Nombre de documents retournés: {len(data) }")
            for d in data:
                contrat = dict(
                    idDeclarant=d.get("idDeclarant"),
                    numeroContrat=d.get("numeroContrat"),
                    numeroAdherent=d.get("numeroAdherent")
                )
                result.append(contrat)
                if len(result) % page_size == 0:
                    self.logger.info(f"Avancée: {len(result)}")

            data, last_id = self.get_page(database, collection, projection_service_prestations, last_id, page_size)
        return self.deduplicate(result)

    def get_service_prestation_contrat(self, database, page_size):
        result = list()
        collection = "servicePrestation"
        fields_service_prestations = [
            "idDeclarant",
            "numero",
            "numeroAdherent"
        ]
        projection_service_prestations = dict()
        fields_service_prestations_description = dict()
        for f in fields_service_prestations:
            projection_service_prestations[f] = 1
            fields_service_prestations_description[f] = dict(
                level=f.count(".") - 1,
                path=f.split(".")[1:]
            )
        data, last_id = self.get_page(database, collection, projection_service_prestations, None, page_size)
        while last_id is not None:
            self.logger.info(f"Nombre de documents retournés: {len(data) }")
            for d in data:
                contrat = dict(
                    idDeclarant=d.get("idDeclarant"),
                    numeroContrat=d.get("numero"),
                    numeroAdherent=d.get("numeroAdherent")
                )
                result.append(contrat)
                if len(result) % page_size == 0:
                    self.logger.info(f"Avancée: {len(result)}")

            data, last_id = self.get_page(database, collection, projection_service_prestations, last_id, page_size)
        return self.deduplicate(result)

    def anti_join(self, x, y, on):
        """Return rows in x which are not present in y"""
        ans = pd.merge(left=x, right=y, how='left', indicator=True, on=on)
        ans = ans.loc[ans._merge == 'left_only', :].drop(columns='_merge')
        return ans

    def anti_join_all_cols(self, x, y):
        """Return rows in x which are not present in y"""
        assert set(x.columns.values) == set(y.columns.values)
        return self.anti_join(x, y, x.columns.tolist())
