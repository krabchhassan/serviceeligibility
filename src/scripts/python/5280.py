#!/usr/bin/env python
import argparse

parser = argparse.ArgumentParser(description='Change _class to the news with endpoint version')

parser.add_argument('--user', type=str, help='db user', required=False)
parser.add_argument('--password', type=str, help='db password', required=False)
parser.add_argument('--dburl', type=str, help='mongodb url', required=True)
parser.add_argument('--dbname', type=str, help='mongodb url', required=True)
parser.add_argument('--replicaset', type=str, help='mongodb name', required=False)
parser.add_argument('--local', action='store_true', help='add this arg if you want use local db and kafka')
parser.add_argument('--use_case', type=str, help='cas', required=False)
parser.add_argument('--kafka_server', type=str, help='kafka server', required=True)
parser.add_argument('--kafka_topic', type=str, help='topic kafka', required=True)

args = parser.parse_args()
user = args.user
password = args.password
dburl = args.dburl
dbname = args.dbname
local = args.local
replicaset = args.replicaset
use_case = args.use_case
kafka_server = args.kafka_server
kafka_topic = args.kafka_topic

SECURITY_PROTOCOL = "SASL_SSL"
KAFKA_CONFIGURATION_FILE = "/etc/kafka/sasl/kafka.configuration.json"


def create_producer():
    from confluent_kafka import Producer

    print("Connect kafka (local: ", local, ") : ", kafka_server)

    if local:
        return Producer(
            {
                'bootstrap.servers': kafka_server
            }
        )

    config = retrieve_kafka_configuration()
    sasl = config.get("sasl")
    ssl = config.get("ssl")
    return Producer(
        {
            'bootstrap.servers': kafka_server,
            'sasl.mechanisms': sasl.get("mechanism"),
            'security.protocol': SECURITY_PROTOCOL,
            'sasl.username': sasl.get("username"),
            'sasl.password': sasl.get("password"),
            'ssl.ca.pem': ssl.get("ca")
        }
    )


def retrieve_kafka_configuration():
    from json import load
    print("Load kafka configs")
    with open(KAFKA_CONFIGURATION_FILE) as kafka_conf_file:
        configuration = load(kafka_conf_file)
    return configuration


def put_message_in_destination_topic(producer, message: str, declarant: str, numero: str, version: str, id_bo: str):
    from json import dumps
    from cloudevents.http import CloudEvent
    from cloudevents.conversion import to_binary
    hdrs = {
        "contractVersion": version,
        "traceId": "",
        "idClientBo": id_bo,
        "idDeclarant": declarant
    }
    """
    On cree des attributes bidons car on en a besoin pour le CloudEvent 
    qui nous permet de perse le message au bon format
    """
    attribute = {"type": "com.example.sampletype1",
                 "source": "https://example.com/event-producer"}
    event = CloudEvent(attribute, dumps(message))
    headears, body = to_binary(event)

    print("Send to kafka topic ", kafka_topic)
    producer.produce(kafka_topic, key=declarant+"-"+numero, value=body, headers=hdrs)

    producer.flush()


def query_services_prestations_cas1(db):
    """
    Cas 1 : relancer la génération des droits pour les ajouts de nouvel assuré sur
    un contrat (BUG-5278 : ne pas prendre en compte la nouvelle date de période de carte)
    """
    triggered_beneficiary_collection = db["triggeredBeneficiary"]

    pipeline = [
        {
            "$group": {
                "_id": {
                    "idTrigger": {
                        "$toObjectId": "$idTrigger"
                    },
                    "servicePrestationId": {
                        "$toObjectId": "$servicePrestationId"
                    }
                },
                "count": {
                    "$sum": 1
                }
            }
        },
        {
            "$group": {
                "_id": "$_id.servicePrestationId",
                "countList": {
                    "$addToSet": "$count"
                }
            }
        },
        {
            "$match": {
                "countList.1": {
                    "$exists": True
                }
            }
        },
        {
            "$lookup": {
                "from": "servicePrestation",
                "localField": "_id",
                "foreignField": "_id",
                "as": "servicePrestation"
            }
        },
        {
            "$unwind": "$servicePrestation"
        },
        {
            "$replaceRoot": {
                "newRoot": "$servicePrestation"
            }
        },
        {
            "$match": {
                "$and": [
                    {
                        "contexteTiersPayant.periodesDroitsCarte.debut": {
                            "$gt": "2023-01-01"
                        }
                    },
                    {
                        "$expr": {
                            "$lt": [
                                "$contexteTiersPayant.periodesDroitsCarte.debut",
                                "$assures.droits.periode.debut"
                            ]
                        }
                    }
                ]
            }
        }
    ]

    return list(triggered_beneficiary_collection.aggregate(pipeline))


def query_services_prestations_cas2(db):
    """
    Cas 2 : relancer la génération des droits pour les cas où il y a une radiation dans le contrat
    """
    service_prestation_collection = db["servicePrestation"]
    query = {
        "$and": [
            {"assures.dateRadiation": {"$exists": True}},
            {"assures.dateRadiation": {"$gte": "2023-01-01"}},
            {"dateResiliation": {"$exists": False}}]
    }
    return list(service_prestation_collection.find(query))


def precess_and_send_to_kafka(db, services_prestations: list):
    print("Process services prestations and send to kafka")
    producer = create_producer()
    id_client_bo_map = {}
    for service in services_prestations:
        # send to Kafka
        service.pop("_id")
        service.pop("_class")
        declarant_id = service.get("idDeclarant")
        numero_service = service.get("numero")
        version = service.get("version")
        for assure in service.get("assures"):
            """
            Pop ces dates car elles posent un probleme de formattage 
            """
            if assure.get("dateModification"):
                assure.pop("dateModification")
            if assure.get("dateCreation"):
                assure.pop("dateCreation")

        if not id_client_bo_map.get(declarant_id):
            id_client_bo_map[declarant_id] = get_id_client_bo(db, declarant_id)

        put_message_in_destination_topic(producer, service, declarant_id, numero_service, version, id_client_bo_map[declarant_id])


def get_id_client_bo(db, declarant_id: str):
    print("Find declarant ", declarant_id, " for idClientBO")
    declarant_collection = db["declarants"]
    declarant = declarant_collection.find_one({"_id": declarant_id})
    if declarant:
        return declarant.get("idClientBO")
    return "DECLARANT_NOT_FOUND"


def process():
    from pymongo import MongoClient
    if replicaset:
        uri = "mongodb://"+user+":"+password+"@"+dburl+"/"+dbname + '?replicaSet=' + replicaset
        client = MongoClient(uri)
    else:
        if local:
            client = MongoClient('mongodb://'+dburl+"/"+dbname)
        else:
            client = MongoClient('mongodb://'+user+':'+password+'@'+dburl+":27017/"+dbname, 27017)
    db = client[dbname]

    print("connected to mongodb : ", dburl, "/", dbname)
    services_prestations = []

    print("use case n° ", use_case)
    if use_case == "1":
        services_prestations = query_services_prestations_cas1(db)
    elif use_case == "2":
        services_prestations = query_services_prestations_cas2(db)
    else:
        print("use case not implemented")

    print("Services prestations queried : ", len(services_prestations))

    precess_and_send_to_kafka(db, services_prestations)

    client.close()


process()
