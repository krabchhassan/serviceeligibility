#!/usr/bin/env python

###########
# IMPORTS #
###########
import argparse

from common.common import get_mongo_client_short, get_list_from_pipeline

##############
# ATTRIBUTES #
##############
parser = argparse.ArgumentParser(description='Delete garantieTechniques duplicated in lot collection')

parser.add_argument('--dburl', type=str, help='mongodb url', required=True)
parser.add_argument('--dbname', type=str, help='mongodb url', required=True)

args = parser.parse_args()
dburl = args.dburl
dbname = args.dbname

#############
# CONSTANTS #
#############
COLLECTION_LOT = "lot"

#############
# FUNCTIONS #
#############

def get_aggregation_to_find_duplicates_by_lot() -> dict:
    return [
        {
            "$unwind": {
                "path": "$garantieTechniques"
            }
        },
        {
            "$match": {
                "garantieTechniques.dateSuppressionLogique": {
                    "$exists": False
                }
            }
        },
        {
            "$group": {
                "_id": {
                    "idLot": "$_id",
                    "codeAssureur": "$garantieTechniques.codeAssureur",
                    "codeGarantie": "$garantieTechniques.codeGarantie"
                },
                "nb": {
                    "$sum": 1
                }
            }
        },
        {
            "$match": {
                "nb": {
                    "$gt": 1
                }
            }
        },
        {
            "$group": {
                "_id": "$_id.idLot",
                "duplicates": {
                    "$push": {
                        "codeAssureur": "$_id.codeAssureur",
                        "codeGarantie": "$_id.codeGarantie",
                        "nb": "$nb"
                    }
                }
            }
        }
    ]

def gt_equals(gt1, gt2):
    return gt1["codeAssureur"] == gt2["codeAssureur"] and gt1["codeGarantie"] == gt2["codeGarantie"]

def gt_exists_in_list(gt, gt_list):
    for gt_from_list in gt_list:
        if "dateSuppressionLogique" not in gt_from_list and gt_equals(gt, gt_from_list):
            return True
    return False

def process():
    database = get_mongo_client_short(
        dburl,
        dbname
    )

    # Get by lot, a list of duplicated gt
    gt_duplicated_by_lot = get_list_from_pipeline(
        database,
        COLLECTION_LOT,
        get_aggregation_to_find_duplicates_by_lot()
    )

    for lot_and_duplicates in gt_duplicated_by_lot:
        duplicated_gt_list = lot_and_duplicates["duplicates"]
        collection = database[COLLECTION_LOT]
        find_query = {"_id": lot_and_duplicates["_id"]}
        lot_in_db = list(collection.find(find_query))
        existing_garantie_techniques = lot_in_db[0]["garantieTechniques"]

        new_garantie_techniques = list()
        for existing_gt in existing_garantie_techniques:
            # Si la gt a une date de suppression logique, on la garde dans le lot
            if "dateSuppressionLogique" in existing_gt:
                new_garantie_techniques.append(existing_gt)
            else:
                # Si la gt est valide et qu'elle n'est pas dans la liste des gt dupliquées, on la garde dans le lot
                if not gt_exists_in_list(existing_gt, duplicated_gt_list):
                    new_garantie_techniques.append(existing_gt)
                else:
                    # Si la gt est valide et qu'elle fait partie des dupliquées, on l'ajoute dans le lot si elle n'est pas déjà dans la liste à l'état valide
                    if not gt_exists_in_list(existing_gt, new_garantie_techniques):
                        new_garantie_techniques.append(existing_gt)

        update_one_query = {"$set": {"garantieTechniques": new_garantie_techniques}}
        collection.update_one(find_query, update_one_query)

##########
# SCRIPT #
##########
process()