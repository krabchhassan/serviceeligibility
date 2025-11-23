from os import environ
import beyondeventmanager

environ['OMU_STEP_NAME'] = 'bobb-import-job'
environ['INPUT_FOLDER'] = 'C:/PEFB/BOBB/IMPORT'
environ['POD_NAME'] = 'is-bdds-bobb-import-ccxkz-4238269182'

environ[beyondeventmanager.AUDIT_SCHEMA_PATH_ENV_VAR] = "/var/audits"
environ[beyondeventmanager.AUDITS_VERSION_DEF_PATH_ENV_VAR] = "/var/audits/versions.def.yaml"
environ[beyondeventmanager.CALLER_SOCLE_CODE_ENV_VAR] = "Insurance"
environ[beyondeventmanager.CALLER_SERVICE_CODE_ENV_VAR] = "serviceeligibility"
environ[
    beyondeventmanager.CALLER_SCHEMA_URL_ENV_VAR] = r"C:\Users\aelbadsi\Desktop\Workspace\serviceeligibility\schemas\events"
environ[beyondeventmanager.EVENT_BEYOND_INSTANCE_ENV_VAR] = 'dev-evol2'
environ[beyondeventmanager.EVENT_BEYOND_CUSTOMER_ENV_VAR] = 'editeur'
environ[
    beyondeventmanager.EVENTS_DIR_ENV_VAR] = r"C:\Users\aelbadsi\Desktop\Workspace\serviceeligibility\schemas\events"
environ[
    beyondeventmanager.KAFKA_SERVERS_ENV_VAR] = "detcisk01.hosting.cegedim.cloud:9094,detcisk02.hosting.cegedim.cloud:9094,detcisk03.hosting.cegedim.cloud:9094"
environ[beyondeventmanager.KAFKA_SECURITY_PROTOCOL_ENV_VAR] = beyondeventmanager.KAFKA_SASL_SSL_ENV_VALUE
environ[
    beyondeventmanager.KAFKA_CONFIGURATION_FILE_ENV_VAR] = r"C:\Users\aelbadsi\Desktop\Workspace\serviceeligibility\src\job\bobb-import-job\app\tests\free\kafka-conf.json"
environ[beyondeventmanager.KAFKA_TOPIC_BEYOND_EVENTS_ENV_VAR] = 'beyond-events'
environ[beyondeventmanager.KAFKA_TOPIC_BEYOND_AUDITS_ENV_VAR] = "beyond-audits"

environ["REDIS_ADMIN_PASSWORD"] = "Ratp4(WazuVupa"
environ["REDIS_ADMIN_USER"] = "redis"
environ["REDIS_AGENT_NODE_0"] = "detcisedtrd001.hosting.cegedim.cloud"
environ["REDIS_AGENT_NODE_1"] = "detcisedtrd002.hosting.cegedim.cloud"
environ["REDIS_AGENT_NODE_2"] = "detcisedtrd003.hosting.cegedim.cloud"
environ["REDIS_DATABASE_BOBB"] = "tenant-t-orange-4171-bobb"
environ[
    "REDIS_CRT_PATH"] = r"C:\Users\aelbadsi\Desktop\Workspace\serviceeligibility\src\job\bobb-import-job\app\tests\free\redis.truststore.crt"
environ["REDIS_MASTER"] = "detcisedtrd01"
environ["REDIS_PASSWORD"] = "Ratp4(WazuVupa"
environ["REDIS_PORT"] = "26379"
environ["REDIS_USER"] = "redis"

environ['ELASTICSEARCH_URL'] = "https://beyond-data-dev.es.cegedim.cloud:443"
environ['ELASTICSEARCH_LOGIN'] = "admin"
environ['ELASTICSEARCH_PASSWORD'] = "bPVjay9MR5123+"

if __name__ == '__main__':
    from bobbimportjob import main

    main("COMPLET", "OUI")
