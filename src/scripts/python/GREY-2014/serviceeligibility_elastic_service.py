import pandas as pd
from beyond_analysis_resource_client.services import ElasticService
from beyond_analysis_resource_client.utils import BaseLogging


class ServiceEligibilityElasticService(object):
    def __init__(self, nature):
        self.logger = BaseLogging().get_logger()
        self.elastic_service = ElasticService(nature)

    def execute(self, database, collection, query):
        return

    def get_all_ids(self, paas, index, page_size: int = None, search_timeout: str = None, query_filter: dict = None):
        ids = list()
        if page_size is None:
            page_size = 1000
        if search_timeout is None:
            search_timeout = "10m"
        query = {
            "size": page_size,
            "stored_fields": ["_id"],
            "_source": False
        }
        if query_filter is None:
            query["query"] = {"match_all": {}}
        else:
            query["query"] = query_filter

        result = self.elastic_service.execute_get(paas,
                                                  f"/{index}/_search?scroll={search_timeout}",
                                                  query
                                                  )

        if (result.status_code == 200) and ("_scroll_id" in result.json()):
            scroll_id = result.json().get("_scroll_id")
            self.logger.info(f"Scroll_Id de la recherche: {scroll_id}")
        else:
            self.logger.error(f"Impossible de lancer la query: {result.text}")
            raise Exception(f"Impossible de lancer la query, le scroll_id n'est pas généré: {result.text}")

        page_number = 0
        continue_scrolling = True
        while continue_scrolling:
            self.logger.info(f"Récupération de la page {page_number}")
            page_number = page_number + 1
            ids.extend(list(map(lambda r: dict(_id=r.get("_id")), result.json().get("hits", []).get("hits", []))))
            result = self.elastic_service.execute_get(paas, "/_search/scroll",
                                                      {
                                                          "scroll": "10m",
                                                          "scroll_id": scroll_id
                                                      })
            continue_scrolling = len(result.json().get("hits", []).get("hits", [])) > 0

        self.logger.info(f"Nombre de pages récupérées: {page_number}")
        self.logger.info(f"Nombre d'id récupérés: {len(ids)}")
        self.logger.info("Suppression du scroll..")
        result = self.elastic_service.execute_delete(paas, f"/_search/scroll/{scroll_id}", payload=None)
        if (result.status_code == 200) and (result.json().get("succeeded", False) == True):
            self.logger.info("Job de scrolling effacé d'Opensearch")
        else:
            self.logger.error(f"Erreur lors de l'effacement du job de scrolling d'Opensearch: {result.text}")
        return ids

    def get_all_documents_with_ids(self, paas, index, ids: list, page_size: int = None, search_timeout: str = None):
        documents = list()
        if page_size is None:
            page_size = 1000
        if search_timeout is None:
            search_timeout = "10m"
        query = {
            "size": page_size,
            "query": {
                "ids": {
                    "values": ids
                }
            }
        }
        result = self.elastic_service.execute_get(paas,
                                                  f"/{index}/_search?scroll={search_timeout}",
                                                  query
                                                  )
        if (result.status_code == 200) and ("_scroll_id" in result.json()):
            scroll_id = result.json().get("_scroll_id")
            self.logger.info(f"Scroll_Id de la recherche: {scroll_id}")
        else:
            self.logger.error(f"Impossible de lancer la query: {result.text}")
            raise Exception(f"Impossible de lancer la query, le scroll_id n'est pas généré: {result.text}")

        page_number = 0
        continue_scrolling = True
        while continue_scrolling:
            self.logger.info(f"Récupération de la page {page_number}")
            page_number = page_number + 1
            documents.extend(list(map(lambda r: r.get("_source"), result.json().get("hits", []).get("hits", []))))
            result = self.elastic_service.execute_get(paas, "/_search/scroll",
                                                      {
                                                          "scroll": "10m",
                                                          "scroll_id": scroll_id
                                                      })
            continue_scrolling = len(result.json().get("hits", []).get("hits", [])) > 0

        self.logger.info(f"Nombre de pages récupérées: {page_number}")
        self.logger.info(f"Nombre d'id récupérés: {len(documents)}")
        self.logger.info("Suppression du scroll..")
        result = self.elastic_service.execute_delete(paas, f"/_search/scroll/{scroll_id}", payload=None)
        if (result.status_code == 200) and (result.json().get("succeeded", False) is True):
            self.logger.info("Job de scrolling effacé d'Opensearch")
        else:
            self.logger.error(f"Erreur lors de l'effacement du job de scrolling d'Opensearch: {result.text}")
        return documents

    def anti_join(self, x, y, on):
        """Return rows in x which are not present in y"""
        ans = pd.merge(left=x, right=y, how='left', indicator=True, on=on)
        ans = ans.loc[ans._merge == 'left_only', :].drop(columns='_merge')
        return ans

    def anti_join_all_cols(self, x, y):
        """Return rows in x which are not present in y"""
        assert set(x.columns.values) == set(y.columns.values)
        return self.anti_join(x, y, x.columns.tolist())
