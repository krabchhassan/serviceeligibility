import argparse
import json
import os
import sys

import pandas
from serviceeligibility_elastic_service import ServiceEligibilityElasticService
from serviceeligibility_mongodb_service import ServiceEligibilityMongoDBService
from beyond_analysis_resource_client.utils import BaseLogging

if __name__ == '__main__':
    logger = BaseLogging().get_logger()
    logger.info("Début du traitement")
    parser = argparse.ArgumentParser(
        description='Vérification de l indexation des bénéficiaires',
        usage='''python verification_indexation_beneficiare.py [<args>]                         
            ''')
    parser.add_argument('--nature', action='store', required=True, help='Nature: dev/qa/preprod/prod> ')
    parser.add_argument('--instance', action='store', required=True, help='Instance <bh00, htp2...> ')
    parser.add_argument('--instance-version', action='store', required=True,
                        help='Instance-version <bh00-es14, htp2-es14...> ')
    parser.add_argument('--client', action='store', required=True, help='igestion, cetip... ')
    parser.add_argument('--dry-run', action='store', required=True, help='Dry run: true/false')
    parser.add_argument('--adm-user', action='store', required=True,
                        help='utilisateur ADM pour récupérer les informations de connexion dans Vault')
    parser.add_argument('--adm-password', action='store', required=True,
                        help="mot de passe de l'utilisateur ADM pour récupérer les informations de connexion dans Vault")
    args = parser.parse_args(sys.argv[1:])
    logger.info("Exécution dela commande 'build_artifact' avec les paramètres")
    logger.info("Paramètres spécifiques à l'action")
    logger.info(f"Argument: a.nature           -> {args.nature}")
    logger.info(f"Argument: a.instance         -> {args.instance}")
    logger.info(f"Argument: a.instance-version -> {args.instance_version}")
    logger.info(f"Argument: a.client           -> {args.client}")
    logger.info(f"Argument: a.adm-user         -> {args.adm_user}")
    logger.info("Argument: a.adm-password     -> ********")
    logger.info(f"Argument: a.dry-run          -> {args.dry_run}")

    paas = "DATA"
    nature = args.nature
    instance = args.instance
    instance_version = args.instance_version
    client = args.client
    vault_paas_name = 'default'
    if instance in ["gp00", "bp00"]:
        index = f"itcare_{instance}_bdd-benef"
        vault_paas_name = 'admin02'
    elif instance in ["c4", "gh00"]:
        index = f"itcare_{instance}_bdd-benef"
        vault_paas_name = 'admin'
    elif instance in ["c6"]:
        index = f"itcare_{instance}_bdd-benef"
        vault_paas_name = 'admin07'
    else:
        index = f"itcare_{instance_version}_bdd-benef"
    if instance in ["c4", "c6"]:
        database = "PXCHB2D01"
    elif instance in ["gp00", "gh00", "bp00"]:
        database = f"bdd-next-serviceeligibility-core-api-{instance}"
    else:
        database = f"bdd-next-serviceeligibility-core-api-{instance_version}"
    # Necessaire pour récupérer les credentials
    if os.environ.get("VAULT_USERNAME") is None and args.adm_user is not None:
        os.environ['VAULT_USERNAME'] = args.adm_user
    if os.environ.get("VAULT_PASSWORD") is None and args.adm_password is not None:
        os.environ['VAULT_PASSWORD'] = args.adm_password
    dry_run = args.dry_run.lower() != "false"
    elastic = ServiceEligibilityElasticService(nature)
    mongodb = ServiceEligibilityMongoDBService(nature, vault_paas_name=vault_paas_name)
    page_size = 10000
    collection = "beneficiaires"
    fields_beneficiaire = [
        "key"
    ]
    projection_beneficiaire = dict()
    fields_service_prestations_description = dict()
    for f in fields_beneficiaire:
        projection_beneficiaire[f] = 1
        fields_service_prestations_description[f] = dict(
            level=f.count("."),
            path=f.split(".")
        )
    data, last_id = mongodb.get_page(database, collection, projection_beneficiaire, None, page_size)
    total_nb_document_mongodb = mongodb.client.get_database(database).get_collection(collection).count_documents({})
    logger.info(f"Nombre de documents à vérifier à partir de la collection {collection}: {total_nb_document_mongodb}")
    missings_ids = list()
    nb_document_mongodb = 0
    nb_document_elastic = 0
    while last_id is not None:
        result = list()
        for d in data:
            document = mongodb.get_data_from_fields(d, fields_service_prestations_description)
            result.append(dict(_id=document.get("key")))
        nb_document_mongodb = nb_document_mongodb + len(result)
        ids = list(map(lambda d: d.get("_id"), result))
        query_filter = {"ids": {
            "values": ids
            }
        }
        index_ids = elastic.get_all_ids("DATA", index, page_size, query_filter=query_filter)
        nb_document_elastic = nb_document_elastic + len(index_ids)
        if len(index_ids) > 0:
            page_missings_ids = elastic.anti_join(
                pandas.DataFrame(result),
                pandas.DataFrame(index_ids),
                on=["_id"]
            )
            nb_missing = page_missings_ids.shape[0]
            missings_ids.extend(page_missings_ids["_id"].tolist())
        else:
            df_result = pandas.DataFrame(result)
            nb_missing = df_result.shape[0]
            missings_ids.extend(df_result["_id"].tolist())
        logger.info(f"Nombre d'ID manquants dans l'index {index} sur le lot de {len(ids)}: {nb_missing}")
        logger.info(f"Nombre d'ids dans l'index {index}: {len(index_ids)}")
        logger.info(f"Avancée: {nb_document_mongodb} / {total_nb_document_mongodb}")
        data, last_id = mongodb.get_page(database, collection, projection_beneficiaire, last_id, page_size)


    nb_missing = len(missings_ids)
    logger.info(f"Nombre d'ID manquants dans l'index {index}: {nb_missing}")
    if nb_missing > 0:
        missings_ids_path = f"/tmp/missings_ids_{index}_{instance}.csv"
        logger.info(f"Sauvegarde des IDs manquants dans le fichier : {missings_ids_path}")
        pandas.DataFrame(missings_ids).to_csv(missings_ids_path)
    else:
        logger.info(f"Aucun document n'est manquant dans l'index {index}")
    assert nb_missing == 0
    logger.info("Fin du traitement")
