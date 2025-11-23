package com.cegedim.next.serviceeligibility.core.dao;

import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationLight;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import com.mongodb.client.ClientSession;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

public interface DeclarationDao {

  Declaration createDeclaration(Declaration declaration, ClientSession session);

  List<Declaration> findDeclarationsByNomFichierOrigine(String nomFichierOrigine);

  List<Declaration> findDeclarationsOfBenef(
      String amc,
      String numeroContrat,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance,
      String rangAdministratif,
      ClientSession session);

  List<Declaration> findDeclarationsOfBenef(
      String amc, String numeroPersonne, String dateNaissance, String rangNaissance);

  List<DeclarationLight> findDeclarationsLightOfBenef(
      String amc, String numeroPersonne, String dateNaissance, String rangNaissance);

  long deleteDeclarationByAmc(String idDeclarant);

  long deleteDeclarationById(String id);

  long replayDeclaration(String idDeclaration);

  boolean existDeclarationsSuspendues(
      String amc,
      String numeroContrat,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance,
      String dateDebutSuspension);

  Declaration getDeclarationById(String idDeclaration);

  Declaration getNextDeclarationById(String idDeclaration);

  Declaration getNextDeclarationByIdAndAmc(String idDeclaration, String amc);

  Declaration getNextDeclarationByDateEffet(LocalDate dateEffet);

  String getLastDeclarationId();

  long deleteAllDeclarationConsultationHistories();

  Stream<Declaration> findDeclarationsByAMCandCarteEditable(String idDeclarant, Date dateSynchro);

  Stream<Declaration> findDeclarationsByAMCandCarteEditableAndContracts(
      String idDeclarant, String fromContrat, String toContrat, String fromAdherent);

  List<Declaration> findDeclarationsOfContratAndTrigger(
      ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger);

  Stream<Declaration> getNextSortedDeclarationsById(String id);

  List<Declaration> findDeclarationsOfContratBenefAMC(
      String idDeclarant, String numAdherent, String numContrat);

  Integer countDeclaration(String idDeclaration);

  Stream<Declaration> getNextSortedDeclarations(Aggregation aggregation);

  String getMinDateFromDeclarations(List<String> idDeclarations);

  boolean hasTPrightsForContract(
      String idDeclarant, String numeroAdherent, String numeroPersonne, String numeroContrat);

  void removeAll();

  List<Declaration> findAll();
}
