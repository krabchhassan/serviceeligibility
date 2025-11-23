package com.cegedim.next.serviceeligibility.core.elast.contract;

import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(value = "CI_COVERAGE_ENABLED", havingValue = "false", matchIfMissing = true)
public interface HistoriqueContratRepository
    extends ElasticsearchRepository<ContratElastic, String> {

  @Query(
      """
            {
                "match":
                {
                    "contrat.numeroContrat":
                    {
                        "query": "?0"
                    }
                }
            }
            """)
  List<ContratElastic> findByNumeroContrat(String numeroContrat);

  @Query(
      """
            {
              "bool": {
                "must": [
                  {
                   "match": {
                      "contrat.idDeclarant": {
                        "query": "?0"
                      }
                    }
                   },
                   {
                     "match": {
                      "contrat.numeroContrat": {
                        "query": "?1"
                      }
                    }
                    },
                    {
                    "match": {
                      "contrat.numeroAdherent": {
                        "query": "?2"
                      }
                    }
                  },
                  {
                    "nested": {
                      "path": "contrat.beneficiaires",
                      "query": {
                        "bool": {
                          "must": [
                            {
                              "match_phrase": {
                                "contrat.beneficiaires.numeroPersonne": "?3"
                              }
                            }
                          ]
                        }
                      }
                    }
                  }
                ]
              }
            }
            """)
  List<ContratElastic> findByIdDeclarantContratAdherentAndNumeroPersonne(
      String idDeclarant,
      String numeroContrat,
      String numeroAdherent,
      String numeroPersonne,
      Pageable pageable);

  @Query(
      """
            {
                "bool":
                {
                    "must":
                    {
                        "range":
                        {
                            "dateSauvegarde":
                            {
                                "lte": "?0"
                            }
                        }
                    }
                }
            }
            """)
  List<ContratElastic> findLTDate(String date);
}
