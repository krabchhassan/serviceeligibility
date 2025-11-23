import argparse
import os
import sys

import pandas
from beyond_analysis_resource_client.services import KafkaService

from se_mongodb_service import SEMongoDBService
from beyond_analysis_resource_client.utils import BaseLogging

if __name__ == '__main__':
    logger = BaseLogging().get_logger()
    parser = argparse.ArgumentParser(
        description='Demande client de comparaison des contrats et service prestations',
        usage='''python demande_comparaison_contrats_serviceprestations.py [<args>]                         
            ''')
    parser.add_argument('--nature', action='store', required=True, help='Nature: dev/qa/preprod/prod> ')
    parser.add_argument('--instance', action='store', required=True, help='Instance <bh00-es1, htp2-es14...> ')
    parser.add_argument('--dry-run', action='store', required=True, help='Dry run: true/false')
    parser.add_argument('--adm-user', action='store', required=True, help='utilisateur ADM pour récupérer les informations de connexion dans Vault')
    parser.add_argument('--adm-password', action='store', required=True, help="mot de passe de l'utilisateur ADM pour récupérer les informations de connexion dans Vault")
    args = parser.parse_args(sys.argv[1:])
    logger.info("Exécution dela commande 'build_artifact' avec les paramètres")
    logger.info("Paramètres spécifiques à l'action")
    logger.info(f"Argument: a.nature         -> {args.nature}")
    logger.info(f"Argument: a.instance       -> {args.instance}")
    logger.info(f"Argument: a.adm-user       -> {args.adm_user}")
    logger.info("Argument: a.adm-password   -> ********")
    logger.info(f"Argument: a.dry-run        -> {args.dry_run}")

    nature = args.nature
    instance = args.instance
    se_database = f"bdd-next-serviceeligibility-core-api-{instance}"
    # Necessaire pour récupérer les credentials
    if os.environ.get("VAULT_USERNAME") is None and args.adm_user is not None:
        os.environ['VAULT_USERNAME'] = args.adm_user
    if os.environ.get("VAULT_PASSWORD") is None and args.adm_password is not None:
        os.environ['VAULT_PASSWORD'] = args.adm_password
    dry_run = args.dry_run.lower() != "false"
    mongo = SEMongoDBService(nature)
    page_size = 1000
    sp_contrat = mongo.get_service_prestation_contrat(se_database, page_size)
    logger.info(f"Nombre de contrats dans ServicePrestation: {len(sp_contrat)}")

    ct_contrat = mongo.get_contrat(se_database, page_size)
    logger.info(f"Nombre de contrat dans contrats: {len(ct_contrat)}")

    logger.info("Calcul des nir à envoyer")
    missings_contrats = mongo.anti_join_all_cols(
        pandas.DataFrame(sp_contrat),
        pandas.DataFrame(ct_contrat)
    )
    nb_missing = missings_contrats.shape[0]
    logger.info(f"Nombre de contrat manquants dans : {nb_missing}")
    pandas.DataFrame(sp_contrat).to_csv(f"/tmp/sp_contrat_{instance}.csv")
    pandas.DataFrame(ct_contrat).to_csv(f"/tmp/ct_contrat_{instance}.csv")
    missings_contrats.to_csv(f"/tmp/missings_benefiaires_{instance}.csv")
    counter = 0
    assert len(missings_contrats) > 0
