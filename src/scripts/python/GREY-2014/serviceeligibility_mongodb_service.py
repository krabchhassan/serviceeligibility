import pandas as pd
from beyond_analysis_resource_client.services import MongoDBService
from beyond_analysis_resource_client.utils import BaseLogging
from pymongo import MongoClient


class ServiceEligibilityMongoDBService(object):
    def __init__(self, nature, vault_paas_name):
        self.logger = BaseLogging().get_logger()
        self.mongodb_service = MongoDBService(nature)
        self.client = MongoClient(self.mongodb_service.get_connection_uri(vault_paas_name))

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
            cursor = self.client[database][collection].find({'_id': {'$gt': last_id}}, projection).sort("_id", 1).limit(
                page_size)

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

    def get_flat_list(self, data, path):
        result = list()
        try:
            if isinstance(data, list):
                for d in data:
                    result.extend(self.get_flat_list(d, path))
            elif path[0] in data:
                if len(path) == 1:
                    if data[path[0]] is list:
                        result.extend(data[path[0]])
                    else:
                        result.append(data[path[0]])
                else:
                    result.extend(self.get_flat_list(data[path[0]], path[1:]))
        except Exception as e:
            self.logger.info(f"Erreur lors du traitement du path {path} ")
            self.logger.info(f"Erreur lors du traitement de la data {data} ")
            raise e
        return result

    def deduplicate(self, list_of_dict):
        seen = set()
        new_l = []
        for d in list_of_dict:
            t = tuple(d.items())
            if t not in seen:
                seen.add(t)
                new_l.append(d)
        return new_l

    def get_data_from_fields(self, data, fields):
        result = dict()
        # result["_id"] = data["_id"]
        for f in fields:
            if fields[f]["level"] == 0:
                if f in data:
                    result[f] = data.get(f)
            else:
                tmp = self.get_flat_list(data, fields.get(f, {}).get("path"))
                result[f] = tmp
        return result

    def first_or_none(self, array):
        if len(array) > 0:
            return array[0]
        else:
            return None
