import json

DATA_PATH = "unit/data/"


def load_from_file(file_name) -> dict:
    with open(f"{DATA_PATH}{file_name}.json") as f:
        return json.load(f)
