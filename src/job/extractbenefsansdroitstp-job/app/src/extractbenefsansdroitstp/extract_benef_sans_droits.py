import sys
import os

from datetime import datetime

from beyondpythonframework.config.logging import get_beyond_logger


PERSONAL_WORKDIR = 'personal-workdir'
SYSTEM_ERROR_CODE = 1
USER_ID = 'USER_ID'
YEAR = 'year'

user_id = os.getenv(USER_ID)
logger = get_beyond_logger()


def parse_arg():
    from argparse import ArgumentParser
    from commonomuhelper.omuhelper import omu_parse_args
    parser = ArgumentParser()
    parser.add_argument('--year', type=str, required=True)
    args = omu_parse_args(parser, None)
    return args


def generate_crex(file_name):
    from commonomuhelper.omuhelper import produce_output_parameters

    produce_output_parameters({
        "file_name": file_name
    })


def send_event_omu_finished(user_events_producer, year):
    send_event(user_events_producer, {
        "message": f"L'extraction des personnes n'ayant pas de droits TP pour l'annnée {year} est terminée. Le "
                   f"fichier est disponible sur votre espace personnel.",
        "detail": "Traitement terminé",
        "level": "info"
    })


def send_event(user_events_producer, user_event_data):
    if user_events_producer is not None:
        logger.debug("Envoi user event")
        user_events_producer.send_user_event(user_event_data, user_id.replace("_AT_", "@"))


def extract_benefs_without_tp_rights(year):
    from pymongo.errors import PyMongoError
    from extractbenefsansdroitstp.historique_execution import HistoriqueExecution
    from extractbenefsansdroitstp.repository import Repository
    from extractbenefsansdroitstp.write_workdir_file import extract_mongodb_results
    from beyondpythonframework.messaging import get_user_events_producer

    if user_id is None:
        logger.error("USER_ID label was not provided by the launcher")
        sys.exit(SYSTEM_ERROR_CODE)

    logger.debug("Try to get_user_events_producer()")
    user_events_producer = get_user_events_producer()
    logger.debug(f"user_event_producer: {user_events_producer}")
    send_event(user_events_producer, {
        "message": f"L'extraction des personnes n'ayant pas de droits TP pour l'annnée {year} est un traitement long. "
                   f"Vous serez notifié lorsqu'elle sera terminée.",
        "detail": "Traitement long",
        "level": "info"
    })

    try:
        repository = Repository()

        last_histo_exec = repository.get_last_histo_exec()
        if last_histo_exec and last_histo_exec['dateExecution']:
            if datetime.date(last_histo_exec['dateExecution']) == datetime.now().date():
                logger.info(f"Le traitement a déjà été lancé aujourd'hui ({last_histo_exec['dateExecution']})")
                send_event_omu_finished(user_events_producer, year)
                generate_crex(None)
                sys.exit(0)
        repository.save_histo_exec(vars(HistoriqueExecution("benefsanstp", datetime.now())))

        logger.info("1 - Récupération des bénéficiaires sans droits TP")
        cursor_benefs_without_tp = repository.get_benefs_without_tp(year)
        logger.info("Bénéficiaires sans droits TP récupérés")

        logger.info("2 - Extraction des résultats dans un fichier csv sur le workdir")
        filename = extract_mongodb_results(repository, cursor_benefs_without_tp, year)

        send_event_omu_finished(user_events_producer, year)
        generate_crex(filename)
        sys.exit()

    except PyMongoError as pymongo_error:
        logger.error(f"Database error: {pymongo_error}")
        sys.exit(1)


def main():
    from beyondpythonframework.securitycontext import init_execution_context

    init_execution_context()
    arg = parse_arg()
    year = arg.get(YEAR)
    logger.info(f'Lancement du traitement pour l\'année {year}')
    extract_benefs_without_tp_rights(datetime.strptime(year, '%Y').date().year)


if __name__ == '__main__':
    main()
