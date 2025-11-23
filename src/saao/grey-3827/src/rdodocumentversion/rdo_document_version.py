import logging
from time import sleep

from opensearchpy import OpenSearch
logging.basicConfig(format='%(asctime)s %(levelname)s %(message)s', datefmt='%Y-%m-%d %H:%M:%S', level=logging.INFO)
logger = logging.getLogger()

def launch():
    """
    This script operates many operations in order to reinit document's version
    Get settings and mappings from the source index
    Create new index from source settings and mapping
    Block write for source index
    Reindex documents from source to destination
    Switch alias from source to destination
    Delete old index if we have the same document's number in source and destination
    """
    from os import environ
    from urllib.parse import urlparse
    from opensearchpy import OpenSearch

    auth = (
        environ.get('ELASTICSEARCH_LOGIN', None),
        environ.get('ELASTICSEARCH_PASSWORD', None),
    )
    common_index_base_name = environ.get('COMMON_INDEX_BASENAME', None)
    instance = environ.get('BEYOND_COULOIR', None)
    instance_version = environ.get('BEYOND_INSTANCE', None)
    urlparsed = urlparse(environ.get('ELASTICSEARCH_URL', None))

    try:
        client = OpenSearch(
            hosts = [{ 'host': urlparsed.hostname, 'port': urlparsed.port }],
            http_auth = auth,
            use_ssl = True
        )

        logger.info(f"Searching the index covered by the alias itcare_{common_index_base_name}_bdd-benef")
        index_name = get_index_name_covered_by_alias(client, common_index_base_name)
        logger.info(f"Index to reindex is : {index_name}")

        docs_count = client.count(index=index_name).get('count')

        logger.info("Computing new index name")
        new_index_name = compute_new_index_name(index_name, common_index_base_name)
        logger.info(f"New index on which data will be reindex is : {new_index_name}")

        logger.info(f"Retrieving settings and mapping from {index_name}")
        settings = get_index_settings(client, index_name)
        mappings = client.indices.get_mapping(index=index_name).get(index_name)
        create_new_index_with_same_settings_and_mapping(client, mappings, new_index_name, settings)

        logger.info(f"Blocking writes from {index_name}")
        block_write_to_old_index(client, index_name)

        logger.info(f"Launching reindex from {index_name} to {new_index_name}")
        task = reindex_wait_for_completion_and_check_failures(client, index_name, new_index_name)
        task_response = task.get('response')

        logger.info("Checking if documents number from source is equal to destination")
        logger.info(f"Source docs count: {docs_count}")
        logger.info(f"Destination docs count {task_response.get('total')}")
        if int(docs_count) == int(task_response.get('total')):
            logger.info("Will switch new index to alias and remove alias from the old one")
            add_alias_to_new_index_and_remove_the_old_one(
                client, common_index_base_name, instance, instance_version, index_name, new_index_name)
            client.indices.delete(index=index_name)
        else:
            logger.error("Documents count from source and destination are not equals")
            logger.info(f"Unblocking writes from {index_name}")
            unblock_write_to_old_index(client, index_name)

        logger.info("End of the RDO")
        logger.info(f"Number of element migrated : {task_response.get('total')}")

    except Exception as ex:
        logger.error(f'An unexpected error occured')
        exit(1)


def block_write_to_old_index(client: OpenSearch, index_name: str) -> None:
    """
    Block write for old index
    :param client: Opensearch client
    :param index_name: index name
    """
    body = {
        'index.blocks.write': True
    }
    client.indices.put_settings(index=index_name, body=body)

def unblock_write_to_old_index(client: OpenSearch, index_name: str) -> None:
    """
    Unblocks write for old index
    :param client: Opensearch client
    :param index_name: index name
    """
    body = {
        'index.blocks.write': None
    }
    client.indices.put_settings(index=index_name, body=body)


def add_alias_to_new_index_and_remove_the_old_one(
        client: OpenSearch, common_index_base_name: str, instance: str, instance_version: str, index_name: str, new_index_name: str) -> None:
    """
    Add alias to new index and remove alias from the old one
    :param client: Opensearch client
    :param common_index_base_name: Index base name
    :param instance: Beyond instance name
    :param instance_version: Beyond instance version
    :param index_name: Source index name
    :param new_index_name: New index name
    """
    body = {
        'actions': [
            {
                'add': {
                    'index': new_index_name,
                    'alias': f'bdd-benef-{instance_version}'
                }
            },
            {
                'remove': {
                    'index': index_name,
                    'alias': f'itcare_{common_index_base_name}_bdd-benef'
                }
            }
        ]
    }

    if common_index_base_name != instance_version:
        body['actions'].append(
            {
                'add': {
                    'index': new_index_name,
                    'alias': f'bdd-benef-{instance}'
                }
            }
        )
    client.indices.update_aliases(body=body)


def create_new_index_with_same_settings_and_mapping(
        client: OpenSearch, mappings: dict, new_index_name: str, settings: dict) -> None:
    """
    Creates new index with the settings and mapping from the old one
    :param client: Opensearch client
    :param mappings: Existing mapping
    :param new_index_name: New index name
    :param settings: Existing settings
    """
    body = {}
    body.update(settings)
    body.update(mappings)
    client.indices.create(index=new_index_name, body=body)


def get_index_settings(client: OpenSearch, index_name: str) -> dict:
    """
    Get index settings and remove unauthorized properties for new index
    :param client: Opensearch client
    :param index_name: Index name
    :return: Index settings ready for creating a new index
    """
    settings = client.indices.get_settings(index=index_name).get(index_name)
    index_settings = settings.get('settings').get('index')
    remove_setting_from_index_settings_if_it_exists(index_settings, 'provided_name')
    remove_setting_from_index_settings_if_it_exists(index_settings, 'creation_date')
    remove_setting_from_index_settings_if_it_exists(index_settings, 'uuid')
    remove_setting_from_index_settings_if_it_exists(index_settings, 'version')
    remove_setting_from_index_settings_if_it_exists(index_settings, 'replication')
    return settings

def remove_setting_from_index_settings_if_it_exists(settings: dict, setting_name: str) -> None:
    """
    Remove setting from index settings if it exists
    :param settings: Index settings
    :param setting_name: setting name
    """
    if settings.get(setting_name, None):
        del settings[setting_name]


def reindex_wait_for_completion_and_check_failures(client: OpenSearch, index_name: str, new_index_name: str) -> dict:
    """
    Reindex documents from source index to destination index
    Checks failures and exist if it is the case
    :param client: Opensearch client
    :param index_name: Index name
    :param new_index_name: New index name
    :return: The task generated for the reindex action
    """
    body = {
        'source': {
            'index': index_name
        },
        'dest': {
            'index': new_index_name,
            'op_type': 'create'
        }
    }
    response = client.reindex(body=body, wait_for_completion=False)
    task_id = response.get('task')
    task_completed = False
    task = None
    while not task_completed:
        sleep(10)
        task = client.tasks.get(task_id=task_id)
        task_completed = task.get('completed')
    failures = task.get('response').get('failures')
    task_has_failures = len(failures) > 0
    if task_has_failures:
        logger.info(f"Tasks has failures :\n")
        for failure in failures:
            logger.info(f"{failure}\n")
        exit(1)
    return task


def compute_new_index_name(index_name: str, common_index_base_name: str) -> str:
    """
    Compute the new index from the old one
    :param index_name: Source index name
    :param common_index_base_name: Index base name
    :return: The new index which has been computed
    """
    index_sequence_number_as_string: str = index_name.split("-")[-1]
    index_sequence_number: int = int(index_sequence_number_as_string)
    new_sequence_number: int = index_sequence_number + 1
    new_sequence_number_as_string: str = str(new_sequence_number).rjust(6, '0')
    new_index_name = f'itcare_{common_index_base_name}_bdd-benef-{new_sequence_number_as_string}'
    return new_index_name


def get_index_name_covered_by_alias(client: OpenSearch, common_index_base_name: str) -> str:
    """
    Get index name covered by alias
    :param client: Opensearch client
    :param common_index_base_name: Index base name
    :return: Index name from alias
    """
    infos_indices = client.cat.indices(index=f"itcare_{common_index_base_name}_bdd-benef", format='json')
    index_infos = infos_indices[0]
    return index_infos.get('index')