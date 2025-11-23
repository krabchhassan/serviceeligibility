#!/usr/bin/python

import argparse
from pymongo import MongoClient, ASCENDING

# === INIT ARGS ===
parser = argparse.ArgumentParser(description='Add field index')
parser.add_argument('--uri', type=str, help='mongodb uri', required=True)
parser.add_argument('--dbname', type=str, help='mongodb database', required=True)
parser.add_argument('--col', type=str, help='collection name', default='bdds-to-blb-tracking')
parser.add_argument('--field', type=str, help='field name', default='status')
args = parser.parse_args()
uri = args.uri
dbname = args.dbname
col = args.col
field = args.field

# === RUN ===
print('--------------------')
print("GREEN-7788")
print(f"Ajout d'index sur {col} / {field}")
print('--------------------')

client = MongoClient(uri)
db = client[dbname]

print("-- Check index")
index_list = db[col].index_information()
print(index_list)

print("-- Create index")
res = db[col].create_index([(field, ASCENDING)], name=field, background=True)
print(f"result: {res}")
print('--------------------')
