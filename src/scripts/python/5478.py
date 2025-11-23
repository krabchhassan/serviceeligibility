#!/usr/bin/env python

###########
# IMPORTS #
###########
import argparse

from .common import get_mongo_client, get_list_from_pipeline

##############
# ATTRIBUTES #
##############
parser = argparse.ArgumentParser(description='Change _class to the news with endpoint version')

parser.add_argument('--user', type=str, help='db user', required=False)
parser.add_argument('--password', type=str, help='db password', required=False)
parser.add_argument('--dburl', type=str, help='mongodb url', required=True)
parser.add_argument('--dbname', type=str, help='mongodb url', required=True)
parser.add_argument('--replicaset', type=str, help='mongodb name', required=False)
parser.add_argument('--local', action='store_true', help='add this arg if you want use local db and kafka')

args = parser.parse_args()
user = args.user
password = args.password
dburl = args.dburl
dbname = args.dbname
replicaset = args.replicaset
local = args.local

#############
# CONSTANTS #
#############
COLLECTION_BENEFICIAIRES = "beneficiaires"

#############
# FUNCTIONS #
#############
def get_aggregation() -> dict:
    return [
        {
            "$match": {
                "identite.affiliationsRO.1" : {
                    "$exists" : True
                }
            }
        },
        {
            "$unset": [
                "identite.affiliationsRO"
            ]
        }
    ]

def process():
    get_list_from_pipeline(
        get_mongo_client(
            user,
            password,
            dburl,
            dbname,
            replicaset,
            local
        ),
        COLLECTION_BENEFICIAIRES,
        get_aggregation()
    )

##########
# SCRIPT #
##########
process()