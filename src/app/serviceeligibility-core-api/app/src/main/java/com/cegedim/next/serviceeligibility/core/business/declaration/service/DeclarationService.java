package com.cegedim.next.serviceeligibility.core.business.declaration.service;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.generic.GenericService;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.business.declaration.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import java.util.List;

/** Interface de la classe d'accès aux services lies aux {@code Declaration}. */
public interface DeclarationService extends GenericService<Declaration> {

  /**
   * @return La DAO des déclaration.
   */
  DeclarationDao getDeclarationDao();

  /**
   * Methode permettant de renvoyer la liste de declaration correspondant à la demande de
   * l'utilisateur.
   *
   * @param infoBenef demande reçue par web serice
   * @param isConsultationVersion2 true si web service consultation Version 2
   * @param isConsultationVersion3 true si le web service consultation est en version 3
   * @param isConsultationVersion4 true si c'est le web service consultation est en version 4
   * @param limitWaranties true si on récupére uniquement les garanties de priorité la plus basse
   * @return liste des déclarations qui matche avec les critères de selection en paramètre
   */
  List<DeclarationDto> getDroitsBeneficiaire(
      final DemandeInfoBeneficiaire infoBenef,
      boolean isConsultationVersion2,
      boolean isConsultationVersion3,
      boolean isConsultationVersion4,
      boolean limitWaranties);

  /**
   * Retourne la liste des déclarations d'une carte de tiers-payant en la triant dans l'ordre ASSURE
   * puis CONJOINT puis ENFANT(S).
   *
   * @param infoBenef les informations de recherches reçue par web serice
   * @param isConsultationVersion2 boolean a true si v2
   * @return la liste des déclaration de la carte.
   */
  List<DeclarationDto> getDeclarationsCarteTiersPayant(
      DemandeInfoBeneficiaire infoBenef,
      boolean isConsultationVersion2,
      boolean isConsultationVersion3);
}
