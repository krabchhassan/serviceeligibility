# Database and Collection Constants
MONGODB_URL_ENV_VAR = "MONGODB_URL"
COLLECTION_CONTRACT_ELEMENT = "contractElement"
COLLECTION_VERSIONS = "versions"
TRANSACTION_FAILED = 'Database transaction failed'

# Field Names
STATUS = "status"
STATUS_ACTIVE = "ACTIVE"
VERSION_ID_FIELD = "versionId"
KEEP_COUNT = "KEEP_COUNT"
BATCH_LIMIT = 1000

# Index Names
UNICITY_INDEX_NAME = "contractElementUnicity"
AGGREGATION_INDEX_NAME = "purge_aggregation_index"
DELETE_INDEX_NAME = "purge_delete_index"

# Job Configuration
JOB_NAME = "bobb-import-job"
LOGGER_NAME = "bobbpurge"
OMU_ID_ENV_VAR = "OMU_ID"

# Purge Job Defaults
DEFAULT_KEEP_COUNT = 2

# Report Constants
PURGED_CONTRACT_ELEMENTS_COUNT = "purged_contract_elements"
UPDATED_VERSIONS_COUNT = "updated_versions"
PRE_PURGE = "pre_purge_statistics"
POST_PURGE = "post_purge_statistics"
