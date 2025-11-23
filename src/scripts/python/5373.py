#!/usr/bin/env python
import argparse

parser = argparse.ArgumentParser(description='Change _class to the news with endpoint version')

parser.add_argument('--user', type=str, help='db user', required=False)
parser.add_argument('--password', type=str, help='db password', required=False)
parser.add_argument('--dburl', type=str, help='mongodb url', required=True)
parser.add_argument('--dbname', type=str, help='mongodb url', required=True)
parser.add_argument('--replicaset', type=str, help='mongodb name', required=False)
parser.add_argument('--local', action='store_true', help='add this arg if you want use local db and kafka')
parser.add_argument('--kafka_server', type=str, help='kafka server', required=True)
parser.add_argument('--kafka_topic', type=str, help='topic kafka', required=True)
parser.add_argument('--instance', type=str, help='cibled instance', required=True)
parser.add_argument('--customer', type=str, help='customer', required=True)
parser.add_argument('--kafka_configuration_file', type=str, help='path to kafka configuration file',
                    default="/etc/kafka/sasl/kafka.configuration.json")

args = parser.parse_args()
user = args.user
password = args.password
dburl = args.dburl
dbname = args.dbname
local = args.local
replicaset = args.replicaset
kafka_server = args.kafka_server
kafka_topic = args.kafka_topic
instance = args.instance
customer = args.customer
kafka_configuration_file = args.kafka_configuration_file

SECURITY_PROTOCOL = "SASL_SSL"


def create_producer():
    from confluent_kafka import Producer

    print("Connect kafka (local: ", local, ") : ", kafka_server)

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
    with open(kafka_configuration_file) as kafka_conf_file:
        configuration = load(kafka_conf_file)
    return configuration


def put_message_in_destination_topic(producer, message: str):
    from cloudevents.http import CloudEvent
    from cloudevents.conversion import to_structured

    print("Send to kafka topic ", kafka_topic)

    event_types = ["payment", "benefit"]
    for event_type in event_types:
        attribute = {
            "type": "Insurance.serviceeligibility." + event_type + ".recipient.reception.event",
            "source": "Insurance/serviceeligibility",
            "customer": customer,
            "instance": instance,
            "datacontenttype": "application/json",
            "traceid": "",
            "dataschema": "https://documentation.beyond-by-cegedim.com/schemas/schemas/release/pi13/events/Insurance"
                          "/serviceeligibility/0.0.1/" + event_type + "-recipient-reception-event.schema.json"
        }
        event = CloudEvent(attribute, message)
        headear, body = to_structured(event)

        producer.produce(kafka_topic, value=body, headers=headear)
        producer.flush()


def query_all_services_prestations_ids(db):
    service_prestation_collection = db["servicePrestation"]
    return list(
        service_prestation_collection.aggregate([{"$project": {"contractId": {"$toString": "$_id"}, "_id": 0}}]))


def send_to_kafka(services_prestations: list):
    print("Process services prestations and send to kafka")
    producer = create_producer()
    for service in services_prestations:
        # send to Kafka
        put_message_in_destination_topic(producer, service)


def process():
    from pymongo import MongoClient
    if replicaset:
        uri = "mongodb://" + user + ":" + password + "@" + dburl + "/" + dbname + '?replicaSet=' + replicaset
        client = MongoClient(uri)
    else:
        if local:
            client = MongoClient('mongodb://' + dburl + "/" + dbname)
        else:
            client = MongoClient('mongodb://' + user + ':' + password + '@' + dburl + ":27017/" + dbname, 27017)
    db = client[dbname]

    print("connected to mongodb : ", dburl, "/", dbname)
    services_prestations_ids = query_all_services_prestations_ids(db)
    print("Services prestations queried : ", len(services_prestations_ids))

    send_to_kafka(services_prestations_ids)

    client.close()


process()
