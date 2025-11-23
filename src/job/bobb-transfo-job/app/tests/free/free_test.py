from os import environ

import beyondeventmanager

environ['OMU_STEP_NAME'] = 'bobb-transfo-job'
environ['INPUT_FOLDER'] = 'C:/PEFB/BOBB/INPUT'
# environ['PERSONAL_WORKDIR'] = 'C:/personal-workdir/editeur'
# environ['USER_ID'] = 'TEST@TEST.COM'
environ['OUTPUT_FOLDER'] = 'C:/PEFB/BOBB/OUTPUT'
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
    beyondeventmanager.KAFKA_CONFIGURATION_FILE_ENV_VAR] = r"C:\Users\aelbadsi\Desktop\Workspace\serviceeligibility\src\job\bobb-transfo-job\app\tests\free\kafka-conf.json"
environ[beyondeventmanager.KAFKA_TOPIC_BEYOND_EVENTS_ENV_VAR] = 'beyond-events'
environ[beyondeventmanager.KAFKA_TOPIC_BEYOND_AUDITS_ENV_VAR] = "beyond-audits"

if __name__ == '__main__':
    from bobbtransfojob import main
    input_file = "import_test_3.xlsx"
    main(input_file)
