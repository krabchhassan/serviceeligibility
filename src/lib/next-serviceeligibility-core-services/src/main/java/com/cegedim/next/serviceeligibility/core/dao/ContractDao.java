package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.query.ContractRequest;
import com.cegedim.next.serviceeligibility.core.services.contracttp.BulkContratTP;
import com.cegedim.next.serviceeligibility.core.services.pojo.BillingResult;
import java.time.LocalDate;
import java.util.List;

/** Interface de la classe d'accès aux {@code contracts} de la base de donnees. */
public interface ContractDao {
  /**
   * Recherche dans la base de donnees un contrat en fonction de l'AMC et du numéro de contrat
   *
   * @param idDeclarant le numéro de l'AMC
   * @param numeroContrat le numéro du contrat
   * @param numeroAdherent le numéro d'adherent
   * @return le contrat
   */
  ContractTP getContract(String idDeclarant, String numeroContrat, String numeroAdherent);

  void saveContract(ContractTP contractTP);

  void deleteContract(String id);

  ContractTP getContract(
      String idDeclarant, String numeroContrat, String numeroAdherent, String collection);

  void saveContract(ContractTP contractTP, String collection);

  void deleteContract(String id, String collection);

  long deleteContractByAmc(String idDeclarant);

  List<BillingResult> getContractsForBillingJob(LocalDate date);

  List<BillingResult> getContractsForBillingOTPJob(LocalDate date, String amc);

  int bulkInsert(List<ContractTP> contrats, String collection);

  void bulkOp(BulkContratTP bulkContratTP, String collection);

  /**
   * Recherche un contrat en fonction de plusieurs critères
   *
   * @param request contient les différents critères de recherche
   * @return la liste des contrats qui correspondent à la recherche
   */
  List<ContractTP> findBy(ContractRequest request);

  /**
   * Compte le nombre de contrats en fonction de plusieurs critères
   *
   * @param request contient les différents critères de recherche
   * @return le nombre de contrats qui correspondent à la recherche
   */
  long countBy(ContractRequest request);

  List<ContractTP> findBy(String amc, String personNumber);
}
