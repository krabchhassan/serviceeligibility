package com.cegedim.next.serviceeligibility.core.business.contrat.dao;

import com.cegedim.next.serviceeligibility.core.dao.IMongoGenericDao;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import java.util.List;

/** Contract DAO interface to access Contract entities from the database */
public interface ContratDao extends IMongoGenericDao<ContractTP> {

  /**
   * Search in the database a contract which corresponds to the technical id
   *
   * @param id Contract technical id
   * @return The contract
   */
  ContractTP findById(String id);

  /**
   * Search in the database all contracts
   *
   * @return A contracts list. If there is no contract, returns an empty list
   */
  List<ContractTP> findAll();

  /**
   * Returns a Beneficiary valid contracts list for a declarer and an effect date<br>
   * <br>
   *
   * @param dateNaissance The beneficiary birth date
   * @param rangNaissance Beneficiary birth order
   * @param nirBeneficiaire Beneficiary NIR
   * @param cleNirBeneficiare Beneficairy NIR Key
   * @param numAMC AMC number
   * @param isRechercheCarteFamille boolean which indicates if we have to filter on contract types
   * @param isSearchByIdPrefectoral boolean, true if we search by numeroPrefectoral, false if we
   *     search by numeroAMCEchange
   * @return Contracts list
   */
  List<ContractTP> findContractsTPByBeneficiary(
      String dateNaissance,
      String rangNaissance,
      String nirBeneficiaire,
      String cleNirBeneficiare,
      String numAMC,
      boolean isRechercheCarteFamille,
      boolean isSearchByIdPrefectoral,
      boolean isSearchByAdherent,
      String numeroAdherent);

  List<ContractTP> findContractsForCarteFamille(
      String dateNaissance,
      String rangNaissance,
      String nirBeneficiaire,
      String cleNirBeneficiare,
      String numAMC,
      boolean isRechercheCarteFamille,
      boolean isSearchByIdPrefectoral,
      boolean isSearchByAdherent,
      String numeroAdherent);
}
