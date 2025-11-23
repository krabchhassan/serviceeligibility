#!/usr/bin/env python

###########
# IMPORTS #
###########
import argparse

from common.common import get_mongo_client_short, get_list_from_pipeline, delete_documents_in_collection_by_id_list

##############
# ATTRIBUTES #
##############
parser = argparse.ArgumentParser(description='Delete duplicates in contractElement collection')

parser.add_argument('--dburl', type=str, help='mongodb url', required=True)
parser.add_argument('--dbname', type=str, help='mongodb url', required=True)

args = parser.parse_args()
dburl = args.dburl
dbname = args.dbname

#############
# CONSTANTS #
#############
COLLECTION_CONTRACT_ELEMENT = "contractElement"

#############
# FUNCTIONS #
#############
def get_aggregation() -> dict:
    return [
        {
            "$group": {
                "_id": {
                    "codeInsurer": "$codeInsurer",
                    "codeContractElement": "$codeContractElement",
                    "codeAMC": "$codeAMC"
                },
                "docsEntries": {
                    "$sum": 1
                },
                "documentsId": {
                    "$push": "$_id"
                }
            }
        }, 
        {
            "$match": {
                "docsEntries": {
                    "$gte": 2
                }
            }
        }
    ]

def process():
    database = get_mongo_client_short(
            dburl,
            dbname
        )

    # Get every contractElement that contains a double on its unique key
    doubledContractElements = get_list_from_pipeline(
        database,
        COLLECTION_CONTRACT_ELEMENT,
        get_aggregation()
    )

    # For each double, delete these elements but one
    idsToDelete = []

    for contractElement in doubledContractElements:
        # We will keep the first element, so we get it out of the list
        contractElement["documentsId"].pop(0)

        # We add each remaining elements to the list
        for id in contractElement["documentsId"]:
            idsToDelete.append(id)
            
    # Now that we have a list of contractElements to delete, we request Mongo
    delete_documents_in_collection_by_id_list(
        database,
        COLLECTION_CONTRACT_ELEMENT,
        idsToDelete
        )

##########
# SCRIPT #
##########
process()