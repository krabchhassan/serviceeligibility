package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import java.util.Iterator;
import java.util.List;

public interface BeneficiaryDao {
  BenefAIV5 getBeneficiaryByKey(final String key);

  List<BenefAIV5> getBeneficiariesByDateReference(
      final String idDeclarant, String numeroAdherent, String numeroContrat, String dateReference);

  List<BenefAIV5> getBeneficiaries(
      final String idDeclarant, String numeroAdherant, String numeroContrat, String numeroPersonne);

  long deleteBeneficiaryById(final String id);

  long deleteBeneficiariesByAmc(final String idDeclarant);

  BenefAIV5 getBeneficiaryByNirAndDateNaissanceAndRangNaissance(
      String nir, String dateNaissance, String rangNaissance);

  Iterator<BenefAIV5> getBenefMultiOS();

  BenefAIV5 save(BenefAIV5 beneficiary);

  List<BenefAIV5> getAll();

  void dropCollections();
}
