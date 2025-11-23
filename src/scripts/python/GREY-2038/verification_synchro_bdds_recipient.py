import argparse
from deepdiff import DeepDiff
import json
import os
import sys
from bson.objectid import ObjectId
import pandas
from serviceeligibility_postgresql_service import ServiceEligibilityPostgreSQLService
from serviceeligibility_mongodb_service import ServiceEligibilityMongoDBService
from beyond_analysis_resource_client.utils import BaseLogging

def get_distinct(list_of_dicts):
    result = []
    for d in list_of_dicts:
        # On met nue valeur 'bidon' pour que ce soit TRUE en gardant le type dict
        found = False
        for r in result:
            diff = DeepDiff(d, r)
            if not diff:
                found = True
                break
        if not found:
            result.append(d)
    return result

def flatten(xss):
    return [x for xs in xss for x in xs]

if __name__ == '__main__':
    logger = BaseLogging().get_logger()
    logger.info("Début du traitement")
    parser = argparse.ArgumentParser(
        description='Vérification de la synchronisation des bénéficiaires',
        usage='''python verification_synchro_bdds_recipient.py [<args>]                         
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
    logger.info("Exécution dela commande 'verification_synchro_bdds_recipient' avec les paramètres")
    logger.info("Paramètres spécifiques à l'action")
    logger.info(f"Argument: a.nature           -> {args.nature}")
    logger.info(f"Argument: a.instance         -> {args.instance}")
    logger.info(f"Argument: a.instance-version -> {args.instance_version}")
    logger.info(f"Argument: a.client           -> {args.client}")
    logger.info(f"Argument: a.adm-user         -> {args.adm_user}")
    logger.info("Argument: a.adm-password     -> ********")
    logger.info(f"Argument: a.dry-run          -> {args.dry_run}")
    nature = args.nature
    instance = args.instance
    instance_version = args.instance_version
    client = args.client
    if nature == "dev":
      vault_paas_name = "dev_replicaset"
    else:
      vault_paas_name = 'default'
    if instance in ["gp00", "bp00"]:
        bdds_database = f"bdd-next-serviceeligibility-core-api-{instance}"
        recipient_database_name = f"recipient-core-api-{instance}"
        vault_paas_name = 'admin02'
    elif instance in ["gh00"]:
        bdds_database = f"bdd-next-serviceeligibility-core-api-{instance}"
        recipient_database_name = f"recipient-core-api-{instance}"
    else:
        bdds_database = f"bdd-next-serviceeligibility-core-api-{instance_version}"
        recipient_database_name = f"recipient-core-api-{instance_version}"
    recipient_database = dict(
        paas="pg14",
        name=recipient_database_name
    )
    # Necessaire pour récupérer les credentials
    if os.environ.get("VAULT_USERNAME") is None and args.adm_user is not None:
        os.environ['VAULT_USERNAME'] = args.adm_user
    if os.environ.get("VAULT_PASSWORD") is None and args.adm_password is not None:
        os.environ['VAULT_PASSWORD'] = args.adm_password
    dry_run = args.dry_run.lower() != "false"
    postgresql = ServiceEligibilityPostgreSQLService(nature)
    mongodb = ServiceEligibilityMongoDBService(nature, vault_paas_name)
    page_size = 1000
    collection = "servicePrestation"
    fields_beneficiaire = [
        "assures.data.destinatairesPaiements.idBeyondDestinatairePaiements",
        "assures.data.destinatairesPaiements.periode.debut",
        "assures.data.destinatairesPaiements.periode.fin",
        "assures.data.destinatairesRelevePrestations.idBeyondDestinataireRelevePrestations",
        "assures.data.destinatairesRelevePrestations.periode.debut",
        "assures.data.destinatairesRelevePrestations.periode.fin",
    ]
    projection_beneficiaire = dict()
    fields_service_prestations_description = dict()
    for f in fields_beneficiaire:
        projection_beneficiaire[f] = 1
        fields_service_prestations_description[f] = dict(
            level=f.count("."),
            path=f.split(".")
        )
    data, last_id = mongodb.get_page(bdds_database, collection, projection_beneficiaire, None, page_size)
    total_nb_document_mongodb = mongodb.client.get_database(bdds_database).get_collection(collection).count_documents(
        {})
    logger.info(f"Nombre de documents à vérifier à partir de la collection {collection}: {total_nb_document_mongodb}")
    logger.info("Début de boucle...")
    missings_ids_payment_recipients = None
    missings_ids_service_statement_recipients = None
    nb_document_mongodb = 0
    nb_document_postgresql = 0
    all_serviceeligibility_payment_recipients = None
    all_serviceeligibility_service_statement_recipients = None
    all_recipient_payment_recipients = None
    all_recipient_service_statement_recipients = None
    while last_id is not None:
        nb_document_mongodb = nb_document_mongodb + len(data)
        serviceeligibility_payment_recipients = list()
        serviceeligibility_service_statement_recipients = list()
        for d in data:
            documents_payment_recipients, documents_service_statement_recipients = mongodb.get_data_from_fields(d,
                                                                                                                fields_service_prestations_description)
            serviceeligibility_payment_recipients.extend(documents_payment_recipients)
            serviceeligibility_service_statement_recipients.extend(documents_service_statement_recipients)
        serviceeligibility_payment_recipients = mongodb.deduplicate(serviceeligibility_payment_recipients)
        serviceeligibility_service_statement_recipients = mongodb.deduplicate(
            serviceeligibility_service_statement_recipients)
        logger.info(
            f"Nombre de destinataires de paiement dans ServiceEligibility: {len(serviceeligibility_payment_recipients)}")
        logger.info(
            f"Nombre de destinataires de relevé de prestations dans ServiceEligibility: {len(serviceeligibility_service_statement_recipients)}")

        ids_payment_recipients = list(set(map(lambda d: (d.get("contractId"), d.get("idBeyondDestinataire")),
                                              serviceeligibility_payment_recipients)))
        ids_service_statement_recipients = list(set(map(lambda d: (d.get("contractId"), d.get("idBeyondDestinataire")),
                                                        serviceeligibility_service_statement_recipients)))
        recipient_payment_recipients = postgresql.get_all_recipient(
            recipient_database,
            "payment_recipient",
            "id_beyond_payment_recipient",
            ids_payment_recipients
        )
        recipient_service_statement_recipients = postgresql.get_all_recipient(
            recipient_database,
            "service_statement_recipient",
            "id_beyond_service_statement_recipient",
            ids_service_statement_recipients
        )
        logger.info(f"Nombre de destinataires de paiement dans Recipient: {len(recipient_payment_recipients)}")
        logger.info(
            f"Nombre de destinataires de relevé de prestations dans Recipient: {len(recipient_service_statement_recipients)}")
        df_serviceeligibility_payment_recipients = pandas.DataFrame(serviceeligibility_payment_recipients)
        df_recipient_payment_recipients = pandas.DataFrame(recipient_payment_recipients)
        missing_recipient_payment_recipients = postgresql.anti_join(
            df_serviceeligibility_payment_recipients,
            df_recipient_payment_recipients,
            on=['contractId', 'periodeDebut', 'periodeFin', 'idBeyondDestinataire']
        )
        nb_missing_payment_recipients = missing_recipient_payment_recipients.shape[0]
        if missings_ids_payment_recipients is None:
            missings_ids_payment_recipients = missing_recipient_payment_recipients
        else:
            missings_ids_payment_recipients = pandas.concat(
                [missings_ids_payment_recipients, missing_recipient_payment_recipients], ignore_index=True)
        logger.info(
            f"Nombre de destinataires de paiements manquants dans recipient sur le lot de {len(ids_payment_recipients)}: {nb_missing_payment_recipients} / {len(serviceeligibility_payment_recipients)}")
        df_serviceeligibility_service_statement_recipients = pandas.DataFrame(
            serviceeligibility_service_statement_recipients)
        df_recipient_service_statement_recipients = pandas.DataFrame(recipient_service_statement_recipients)
        missing_recipient_service_statement_recipients = postgresql.anti_join(
            df_serviceeligibility_service_statement_recipients,
            df_recipient_service_statement_recipients,
            on=['contractId', 'periodeDebut', 'periodeFin', 'idBeyondDestinataire']
        )
        nb_missing_service_statement_recipients = missing_recipient_service_statement_recipients.shape[0]
        if missings_ids_service_statement_recipients is None:
            missings_ids_service_statement_recipients = missing_recipient_service_statement_recipients
        else:
            missings_ids_service_statement_recipients = pandas.concat(
                [missings_ids_service_statement_recipients, missing_recipient_service_statement_recipients], ignore_index=True)
        logger.info(
            f"Nombre de destinataires de relevés de prestations manquants dans recipient sur le lot de {len(ids_service_statement_recipients)}: {nb_missing_service_statement_recipients} / {len(serviceeligibility_service_statement_recipients)}")

        if all_serviceeligibility_payment_recipients is None:
            all_serviceeligibility_payment_recipients = df_serviceeligibility_payment_recipients
        else:
            all_serviceeligibility_payment_recipients = pandas.concat(
                [all_serviceeligibility_payment_recipients, df_serviceeligibility_payment_recipients],
                ignore_index=True)
        if all_serviceeligibility_service_statement_recipients is None:
            all_serviceeligibility_service_statement_recipients = df_serviceeligibility_service_statement_recipients
        else:
            all_serviceeligibility_service_statement_recipients = pandas.concat(
                [all_serviceeligibility_service_statement_recipients,
                 df_serviceeligibility_service_statement_recipients], ignore_index=True)

        if all_recipient_payment_recipients is None:
            all_recipient_payment_recipients = df_recipient_payment_recipients
        else:
            all_recipient_payment_recipients = pandas.concat(
                [all_recipient_payment_recipients, df_recipient_payment_recipients], ignore_index=True)
        if all_recipient_service_statement_recipients is None:
            all_recipient_service_statement_recipients = df_recipient_service_statement_recipients
        else:
            all_recipient_service_statement_recipients = pandas.concat(
                [all_recipient_service_statement_recipients, df_recipient_service_statement_recipients],
                ignore_index=True)
        logger.info(f"Avancée: {nb_document_mongodb} / {total_nb_document_mongodb}")
        data, last_id = mongodb.get_page(bdds_database, collection, projection_beneficiaire, last_id, page_size)

    logger.info(
        f"Nombre de destinataires de paiements manquants dans recipient : {missings_ids_payment_recipients.shape[0]}")
    logger.info(
        f"Nombre de destinataires de relevés de prestations manquants dans recipient: {missings_ids_service_statement_recipients.shape[0]}")
    all_serviceeligibility_payment_recipients.to_csv(f"/tmp/all_serviceeligibility_payment_recipients_{instance}.csv")
    all_serviceeligibility_service_statement_recipients.to_csv(
        f"/tmp/all_serviceeligibility_service_statement_recipients_{instance}.csv")
    all_recipient_payment_recipients.to_csv(f"/tmp/all_recipient_payment_recipients_{instance}.csv")
    all_recipient_service_statement_recipients.to_csv(f"/tmp/all_recipient_service_statement_recipients_{instance}.csv")
    if len(missings_ids_payment_recipients) > 0:
        missings_ids_path = f"/tmp/missings_ids_destinataire_de_paiements_{instance}.csv"
        logger.info(f"Sauvegarde des destinataires de paiements manquants dans le fichier : {missings_ids_path}")
        pandas.DataFrame(missings_ids_payment_recipients).to_csv(missings_ids_path)
    else:
        logger.info("Aucun destinataires de paiements n'est manquant dans Recipient}")
    if len(missings_ids_service_statement_recipients) > 0:
        missings_ids_path = f"/tmp/missings_ids_destinataire_de_releves_de_prestations_{instance}.csv"
        logger.info(
            f"Sauvegarde des destinataires de relevés de prestations manquants dans le fichier : {missings_ids_path}")
        pandas.DataFrame(missings_ids_service_statement_recipients).to_csv(missings_ids_path)
    else:
        logger.info("Aucun destinataires de relevés de prestations n'est manquant dans Recipient")
    if not dry_run:
        payments_contract_ids = missings_ids_payment_recipients["contractId"].tolist()
        service_statement_contract_ids = missings_ids_service_statement_recipients["contractId"].tolist()
        ids = list()
        ids.extend(payments_contract_ids)
        ids.extend(service_statement_contract_ids)
        ids = list(set(ids))
        logger.info(f"Génération du fichier d'entrée de l'OMU de reprise des Recipients contenant {len(ids)} contrats")
        fields_beneficiaire_export = [
            "_id",
            "idDeclarant",
            "societeEmettrice",
            "numero",
            "assures.data.destinatairesPaiements",
            "assures.data.destinatairesRelevePrestations"
        ]
        projection_beneficiaire_export = dict()
        fields_beneficiaire_export_description = dict()
        for f in fields_beneficiaire_export:
            projection_beneficiaire_export[f] = 1
            fields_beneficiaire_export_description[f] = dict(
                level=f.count("."),
                path=f.split(".")
            )
            nb_document_mongodb = 0
        with open(f"/tmp/BDDS_recipient_extract_{instance}.json", "w") as recipient_extract:
            recipient_extract.write("[")
            data, last_id = mongodb.get_page(bdds_database,
                                             collection,
                                             projection_beneficiaire_export,
                                             None,
                                             len(ids),
                                             _filter={"_id": {"$in": list(map(lambda i: ObjectId(i), ids))}}
                                             )
            logger.info("Début de l'écriture du fichier...")
            if data is not None:
                nb_document_mongodb = nb_document_mongodb + len(data)
                first_element = True
                for d in data:
                    contract_id = str(d.get("_id"))
                    destinatairesPaiements = get_distinct(
                        flatten(
                            list(map(lambda x: x.get("data").get("destinatairesPaiements"), d.get("assures")))
                        )
                    )
                    destinatairesRelevePrestations = get_distinct(
                        flatten(
                            list(map(lambda x: x.get("data").get("destinatairesRelevePrestations"), d.get("assures")))
                        )
                    )
                    beneficiaire = dict(
                        id=contract_id,
                        idDeclarant=d.get("idDeclarant"),
                        societeEmettrice=d.get("societeEmettrice"),
                        numero=d.get("numero"),
                        assures=[dict(
                                    data=dict(
                                        destinatairesPaiements=destinatairesPaiements,
                                        destinatairesRelevePrestations=destinatairesRelevePrestations
                                    )
                                )
                            ]
                        )
                    recipient_extract.write(("," if not first_element else "") + json.dumps(beneficiaire))
                    first_element = False
                logger.info(f"Fin d'écriture du fichier: {nb_document_mongodb}")
                recipient_extract.write("]")
    logger.info("Fin du traitement")



