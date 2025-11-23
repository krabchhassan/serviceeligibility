package com.cegedim.next.beneficiary.worker.dao;

import com.cegedim.next.serviceeligibility.core.model.kafka.BenefAI;

/** Interface de la classe d'accès aux {@code beneficiary} de la base de donnees. */
public interface BeneficiaryDao {
  /**
   * Recherche dans la base de donnees un beneficiaire en fonction de l'AMC, l'Adhérent, son numéro
   * de personne et sa date naissance
   *
   * @param idDeclarant le numéro de l'AMC
   * @param numeroAdherent le numéro d'adhérent du bénéficiaire
   * @param numeroPersonne le numéro de personne du beneficiaire
   * @param dateNaissance la date de naissance du beneficiare
   * @param rangNaissance le rang de naissance du beneficiare
   * @return le beneficiaire
   */
  public BenefAI getBeneficiary(
      String idDeclarant,
      String numeroAdherent,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance);
}
