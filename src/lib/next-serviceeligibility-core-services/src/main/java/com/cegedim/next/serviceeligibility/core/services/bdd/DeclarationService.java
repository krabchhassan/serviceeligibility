package com.cegedim.next.serviceeligibility.core.services.bdd;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.CODE_ETAT_INVALIDE;
import static com.cegedim.next.serviceeligibility.core.utils.Constants.SLASHED_YYYY_MM_DD;

import com.cegedim.next.serviceeligibility.core.dao.DeclarationDao;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationLight;
import com.cegedim.next.serviceeligibility.core.services.pojo.ConsolidationDeclarationsContratTrigger;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.DeclarationConsolideUtils;
import com.mongodb.client.ClientSession;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class DeclarationService {

  private final DeclarationDao dao;

  @ContinueSpan(log = "createDeclaration")
  public Declaration createDeclaration(Declaration declaration, ClientSession session) {
    return dao.createDeclaration(declaration, session);
  }

  public List<Declaration> findDeclarationsByNomFichierOrigine(String nomFichierOrigine) {
    return dao.findDeclarationsByNomFichierOrigine(nomFichierOrigine);
  }

  @ContinueSpan(log = "findDeclarationsOfBenef")
  public List<Declaration> findDeclarationsOfBenef(
      String amc,
      String numeroContrat,
      String numeroPersonne,
      String dateNaissance,
      String rangNaissance,
      String rangAdministratif,
      ClientSession session) {
    return dao.findDeclarationsOfBenef(
        amc,
        numeroContrat,
        numeroPersonne,
        dateNaissance,
        rangNaissance,
        rangAdministratif,
        session);
  }

  @ContinueSpan(log = "anotherContractExists")
  public boolean anotherContractExists(
      String amc, String numeroPersonne, String dateNaissance, String rangNaissance) {
    return !CollectionUtils.isEmpty(
        dao.findDeclarationsOfBenef(amc, numeroPersonne, dateNaissance, rangNaissance));
  }

  @ContinueSpan(log = "deleteDeclarationByAmc")
  public long deleteDeclarationByAmc(final String idDeclarant) {
    return dao.deleteDeclarationByAmc(idDeclarant);
  }

  @ContinueSpan(log = "deleteDeclarationById")
  public long deleteDeclarationById(final String id) {
    return dao.deleteDeclarationById(id);
  }

  @ContinueSpan(log = "getDeclarationById")
  public Declaration getDeclarationById(final String id) {
    return dao.getDeclarationById(id);
  }

  /**
   * Méthode utilisée dans le batch de purge. Récupère toutes les déclarations du bénéficiaire et
   * les rejoue de la plus ancienne a la plus récente pour que le batch de consolidation corrige les
   * contrats.
   *
   * @param amc AMC à laquelle le bénéficiaire est rattaché
   * @param numeroPersonne Numéro de personne du bénéficiaire
   * @param dateNaissance Date de naissance du bénéficiaire
   * @param rangNaissance Rang de naissance du bénéficiaire
   * @return L'existance d'au moins une déclaration pour ce bénéficiaire
   */
  @ContinueSpan(log = "replayDeclarations")
  public long replayDeclarations(
      String amc, String numeroPersonne, String dateNaissance, String rangNaissance) {
    List<DeclarationLight> declarations =
        dao.findDeclarationsLightOfBenef(amc, numeroPersonne, dateNaissance, rangNaissance);

    if (CollectionUtils.isEmpty(declarations)) {
      return 0;
    }

    long replayedDeclarations = 0;

    for (DeclarationLight declaration : declarations) {
      replayedDeclarations += dao.replayDeclaration(declaration.get_id());
    }

    return replayedDeclarations;
  }

  @ContinueSpan(log = "deleteHistory")
  public long deleteHistory() {
    return dao.deleteAllDeclarationConsultationHistories();
  }

  @ContinueSpan(log = "findDeclarationsByAMCandCarteDemat")
  public Stream<Declaration> findDeclarationsByAMCandCarteDemat(
      String idDeclarant, Date dateSynchro) {
    return dao.findDeclarationsByAMCandCarteEditable(idDeclarant, dateSynchro);
  }

  public List<Declaration> filterLatestDeclarationsForConso(
      List<Declaration> declarations, Date today, String clientType) {
    boolean isInsurer = Constants.CLIENT_TYPE_INSURER.equals(clientType);
    List<Declaration> latestDeclarations = new ArrayList<>();
    List<PeriodeDroit> periodes = new ArrayList<>();
    String dateJour = new SimpleDateFormat(SLASHED_YYYY_MM_DD).format(today);
    for (Declaration declaration : declarations) {
      PeriodeDroit periode =
          DeclarationConsolideUtils.getMinMaxPeriodesDomaineDroit(declaration, isInsurer);
      if (periode.getPeriodeDebut() != null
          && periode.getPeriodeFin() != null
          && DateUtils.isPeriodeNonPresente(periode, periodes)
          && (CODE_ETAT_INVALIDE.equals(declaration.getCodeEtat())
              || DeclarationConsolideUtils.isDroitsOuverts(
                  declaration.getDomaineDroits(), dateJour, isInsurer))) {
        latestDeclarations.add(declaration);
        periodes.add(periode);
      }
    }
    return latestDeclarations;
  }

  public Stream<Declaration> findDeclarationsByAMCandCarteEditable(
      String idDeclarant, Date dateSynchro) {
    return dao.findDeclarationsByAMCandCarteEditable(idDeclarant, dateSynchro);
  }

  public Stream<Declaration> findDeclarationsByAMCandCarteEditableAndContracts(
      String idDeclarant, String fromContrat, String toContrat, String fromAdherent) {
    return dao.findDeclarationsByAMCandCarteEditableAndContracts(
        idDeclarant, fromContrat, toContrat, fromAdherent);
  }

  @ContinueSpan(log = "findDeclarationsOfContratAndTrigger")
  public List<Declaration> findDeclarationsOfContratAndTrigger(
      ConsolidationDeclarationsContratTrigger consolidationDeclarationsContratTrigger) {
    return dao.findDeclarationsOfContratAndTrigger(consolidationDeclarationsContratTrigger);
  }

  public List<Declaration> findDeclarationsOfContratBenefAMC(
      String idDeclarant, String numAdherent, String numContrat) {
    return dao.findDeclarationsOfContratBenefAMC(idDeclarant, numAdherent, numContrat);
  }

  public String getMinDateFromDeclarations(List<String> idDeclarations) {
    return dao.getMinDateFromDeclarations(idDeclarations);
  }
}
