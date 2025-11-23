import os
import pandas

from beyondpythonframework.config.logging import get_beyond_logger

logger = get_beyond_logger()

USER_ID = 'USER_ID'
OUTPUT_FILE_NAME = 'benefs_sans_droits_tp_{year}.csv'
OMU_ID = 'OMU_ID'


def get_personal_workdir_path():
    from commonpersonalworkdir.common_personal_workdir import build_personal_workdir_path

    logger.debug('Récupération du chemin de l\'espace personnel')
    user_id = os.getenv(USER_ID, "test")
    return build_personal_workdir_path(user_id)


def generate_output_file_path(year):
    filename = os.getenv(OMU_ID, "benefsansdroitstp") + "_" + OUTPUT_FILE_NAME.format(year=year)
    personal_workdir_path = get_personal_workdir_path()
    logger.debug(f'Chemin récupéré : {personal_workdir_path}')
    if not os.path.exists(personal_workdir_path):
        os.makedirs(personal_workdir_path)
    output_file_path = os.path.join(personal_workdir_path, filename)
    return output_file_path, filename


def extract_mongodb_results(repository, cursor_benefs_without_tp, year):
    results_series = pandas.DataFrame()
    for benef in cursor_benefs_without_tp:
        num_contrat_num_personne = f"{benef['numero']} - {benef['numeroPersonne']}"
        logger.debug(f"Benef sans droits tp : {num_contrat_num_personne}")
        others_contracts = repository.get_others_contracts(benef, year)
        benef["_id"] = num_contrat_num_personne
        serie = pandas.Series(benef, name=benef["_id"])
        for idx, other_contract in enumerate(others_contracts):
            logger.debug(f"Benef sans droits tp : {num_contrat_num_personne} - "
                         f"Autre contrat n°{other_contract['numeroContrat']}")
            other_contract_format = pandas.json_normalize(other_contract).add_prefix(f'autresContrats_{idx}_')
            serie = serie.append(other_contract_format.squeeze())
        results_series = results_series.append(serie, ignore_index=True)

    file_path, filename = generate_output_file_path(year)
    results_series.to_csv(file_path, sep=",", index=False)
    logger.info(f"Le fichier csv {file_path} est généré")
    return filename
