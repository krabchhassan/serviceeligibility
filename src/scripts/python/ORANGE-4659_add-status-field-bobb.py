import argparse
from pymongo import MongoClient

parser = argparse.ArgumentParser(description='Add status field in contractElement')
parser.add_argument('--uri', type=str, help='mongodb url', required=True)
parser.add_argument('--dbname', type=str, help='mongodb database', required=True)
args = parser.parse_args()
uri = args.uri
dbname = args.dbname


print('###########################')
print("ORANGE-4659")
print('--------------------')
print("Ajout du champ status pour contractElement")


# Se connecter à la base de données MongoDB
client = MongoClient(uri)

# Sélectionner la base de données
db = client[dbname]

# Sélectionner la collection
collection = db['contractElement']

# Mettre à jour tous les documents pour ajouter le champ "status" avec la valeur "active"
update_result = collection.update_many(
    {},  # Filtre vide pour sélectionner tous les documents
    {"$set": {"status": "ACTIVE"}}
)

# Afficher le nombre de documents mis à jour
print(f"Documents mis à jour : {update_result.modified_count}")

# Fermer la connexion
client.close()
print('###########################')
