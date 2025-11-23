#!/usr/bin/env python

###########
# IMPORTS #
###########
from pymongo import MongoClient

#############
# FUNCTIONS #
#############
def get_mongo_client_short(dburl: str, dbname: str) -> MongoClient:
    return MongoClient(dburl)[dbname]

def get_mongo_client(user: str, password: str, dburl: str, dbname: str, replicaset: str, local: bool) -> MongoClient:
    if replicaset:
        client = MongoClient(
            "mongodb://" +
            user + ":" + password + "@" +
            dburl + "/" + dbname +
            "?replicaSet=" + replicaset
        )
    else:
        if local:
            client = MongoClient(
                "mongodb://" +
                dburl + "/" + dbname
            )
        else:
            client = MongoClient(
                "mongodb://" +
                user + ":" + password + "@" +
                dburl + ":27017/" + dbname,
                27017
            )

    return client[dbname]

def get_list_from_pipeline(database: MongoClient, collection_name: str, pipeline: dict) -> list:
    collection = database[collection_name]
    return list(collection.aggregate(aggregate=collection_name, pipeline=pipeline))

def delete_documents_in_collection_by_id_list(database: MongoClient, collection_name: str, id_to_delete: []) -> None:
    collection = database[collection_name]

    delete_query={ "_id": { "$in": id_to_delete } }
    collection.delete_many(delete_query)