import os
import json

DIRECTORY_AGGREGATIONS = "aggregations"


def get_str_aggregation_from_json_file(json_file_name):
    script_dir = os.path.dirname(__file__)
    file_path = os.path.join(script_dir, DIRECTORY_AGGREGATIONS, json_file_name)
    with open(file_path, "r") as file:
        return file.read()


def get_json_aggregation_with_date_from_file(json_file_name, date_resiliation_tiret, date_radiation_tiret):
    from string import Template

    template_string = Template(get_str_aggregation_from_json_file(json_file_name))
    date_resiliation_slash = date_resiliation_tiret.replace("-", "/")
    date_radiation_slash = date_radiation_tiret.replace("-", "/")
    pipeline = template_string.safe_substitute(dateResiliationTiret=date_resiliation_tiret,
                                               dateRadiationTiret=date_radiation_tiret,
                                               dateResiliationSlash=date_resiliation_slash,
                                               dateRadiationSlash=date_radiation_slash,
                                               maxDateFinOffline=date_resiliation_slash)
    return json.loads(pipeline)


def get_json_aggregation_other_contracts_from_file(json_file_name, id_declarant, numero_personne,
                                                   date_resiliation_tiret, date_radiation_tiret):
    from string import Template

    template_string = Template(get_str_aggregation_from_json_file(json_file_name))
    pipeline = template_string.safe_substitute(idDeclarant=id_declarant,
                                               numeroPersonne=numero_personne,
                                               dateResiliationTiret=date_resiliation_tiret,
                                               dateRadiationTiret=date_radiation_tiret)
    return json.loads(pipeline)
