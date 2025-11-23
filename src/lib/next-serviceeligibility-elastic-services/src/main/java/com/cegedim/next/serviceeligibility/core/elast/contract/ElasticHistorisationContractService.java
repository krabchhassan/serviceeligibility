package com.cegedim.next.serviceeligibility.core.elast.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ElasticIndexAliasException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.opensearch.OpenSearchException;
import org.opensearch.action.bulk.BulkRequest;
import org.opensearch.action.index.IndexRequest;
import org.opensearch.client.RequestOptions;
import org.opensearch.client.RestHighLevelClient;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticHistorisationContractService {

  private final HistoriqueContratRepository histoRepository;

  private final IndexHistoContrat indexHistoContrat;

  private final ObjectMapper objectMapper;

  private final RestHighLevelClient opensearchClient;

  public void putContractHistoryOnElastic(ContractTP contract) {
    if (contract != null) {
      log.debug(
          "Historisation du Contrat {} - adherent {} declarant {}",
          contract.getNumeroContrat(),
          contract.getNumeroAdherent(),
          contract.getIdDeclarant());

      ContratElastic contratElastic = mapContractToElastic(contract);
      try {
        if (histoRepository != null) {
          histoRepository.save(contratElastic);
        } else {
          log.debug("Bean HistoriqueContratRepository manquant - Impossible de sauvegarder");
        }
      } catch (DataAccessResourceFailureException e) {
        log.debug(
            "Erreur ignorée temporairement pour assurer la compatibilité entre le driver Elastic 7.12.1 et les versions d'Opensearch 1.3 et 2.6",
            e);
      }
    } else {
      log.debug("Aucun ancien contrat de trouvé, historisation impossible");
    }
  }

  public void putContractsHistoryOnElastic(List<ContractTP> contracts) {
    if (CollectionUtils.isNotEmpty(contracts)) {
      List<ContratElastic> contratElastics = new ArrayList<>();
      for (ContractTP contract : contracts) {
        contratElastics.add(mapContractToElastic(contract));
      }
      BulkRequest bulkRequest = new BulkRequest();
      Optional.of(contratElastics).orElse(List.of()).stream()
          .filter(Objects::nonNull)
          .forEach(
              contract -> {
                final Map<String, Object> contractPropertiesMap =
                    this.objectMapper.convertValue(contract, new TypeReference<>() {});
                bulkRequest.add(
                    new IndexRequest(indexHistoContrat.getIndexAlias())
                        .id(contract.getId())
                        .source(contractPropertiesMap));
              });
      try {
        opensearchClient.bulk(bulkRequest, RequestOptions.DEFAULT);
      } catch (IOException e) {
        throw new ElasticIndexAliasException(e.getMessage());
      }
    } else {
      log.debug("Aucun ancien contrat de trouvé, historisation impossible");
    }
  }

  private ContratElastic mapContractToElastic(ContractTP contract) {
    ContratElastic contratElastic = new ContratElastic();
    contratElastic.setContrat(contract);
    contratElastic.setDateSauvegarde(LocalDateTime.now(ZoneOffset.UTC));

    return contratElastic;
  }

  /**
   * utilisé seulement pour les tests
   *
   * @param numeroContrat
   * @return
   */
  // TODO à supprimer
  public List<ContratElastic> findByNumeroContrat(String numeroContrat) {
    return histoRepository.findByNumeroContrat(numeroContrat);
  }

  @ContinueSpan(log = "findByIdDeclarantContratAdherentAndNumeroPersonne List<ContratElastic>")
  public List<ContratElastic> findByIdDeclarantContratAdherentAndNumeroPersonne(
      String idDeclarant,
      String numeroContrat,
      String numeroAdherent,
      String numeroPersonne,
      Pageable pageable) {
    try {
      return histoRepository.findByIdDeclarantContratAdherentAndNumeroPersonne(
          idDeclarant, numeroContrat, numeroAdherent, numeroPersonne, pageable);
    } catch (OpenSearchException | DataAccessException e) {
      log.error(e.getMessage(), e);
    }
    return new ArrayList<>();
  }

  public List<ContratElastic> findToPurge(String limitDate) {
    return histoRepository.findLTDate(limitDate);
  }

  public void deleteHistoContrats(List<ContratElastic> toDelete) {
    histoRepository.deleteAll(toDelete);
  }
}
