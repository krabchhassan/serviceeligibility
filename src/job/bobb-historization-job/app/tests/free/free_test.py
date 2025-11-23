from os import environ
import beyondeventmanager

environ['OMU_STEP_NAME'] = 'bobb-historization-job'
environ['INPUT_FOLDER'] = 'C:/PEFB/BOBB/OUTPUT'
environ['POD_NAME'] = 'is-bdds-bobb-import-ccxkz-4238269182'

# environ[beyondeventmanager.AUDIT_SCHEMA_PATH_ENV_VAR] = "/var/audits"
# environ[beyondeventmanager.EVE] = "/var/audits/versions.def.yaml"
environ[beyondeventmanager.CALLER_SOCLE_CODE_ENV_VAR] = "Insurance"
environ[beyondeventmanager.CALLER_SERVICE_CODE_ENV_VAR] = "serviceeligibility"

environ[beyondeventmanager.EVENT_BEYOND_INSTANCE_ENV_VAR] = 'dev-evol2'
environ[beyondeventmanager.EVENT_BEYOND_CUSTOMER_ENV_VAR] = 'editeur'
environ[
    beyondeventmanager.EVENTS_DIR_ENV_VAR] = r"C:\Users\zarchane\Workspace\serviceeligibility\schemas\events"
environ[
    beyondeventmanager.KAFKA_SERVERS_ENV_VAR] = "detcisk01.hosting.cegedim.cloud:9094,detcisk02.hosting.cegedim.cloud:9094,detcisk03.hosting.cegedim.cloud:9094"
environ[beyondeventmanager.KAFKA_SECURITY_PROTOCOL_ENV_VAR] = beyondeventmanager.KAFKA_SASL_SSL_ENV_VALUE
environ[
    beyondeventmanager.KAFKA_CONFIGURATION_FILE_ENV_VAR] = r"C:\Users\zarchane\kafka-conf.json"
environ[beyondeventmanager.KAFKA_TOPIC_BEYOND_EVENTS_ENV_VAR] = 'beyond-events'
# environ[beyondeventmanager.KAFKA_TOPIC_BEYOND_AUDITS_ENV_VAR] = "beyond-audits"


environ['ELASTICSEARCH_URL'] = "https://beyond-data-dev.es.cegedim.cloud:443"
environ['ELASTICSEARCH_LOGIN'] = "admin"
environ['ELASTICSEARCH_PASSWORD'] = "bPVjay9MR5123+"
environ["SCHEMA_URL"] = r"C:\Users\zarchane\Workspace\serviceeligibility\schemas"

if __name__ == '__main__':
    from bobbhistorizationjob import main

    main()
