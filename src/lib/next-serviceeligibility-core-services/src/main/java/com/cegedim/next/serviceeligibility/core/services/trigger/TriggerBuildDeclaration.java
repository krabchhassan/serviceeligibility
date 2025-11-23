package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dao.RestitutionCarteDao;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.sascontrat.SasContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.RestitutionCarte;
import com.cegedim.next.serviceeligibility.core.services.ParametrageCarteTPService;
import com.cegedim.next.serviceeligibility.core.services.SuspensionService;
import com.cegedim.next.serviceeligibility.core.services.bdd.SasContratService;
import com.cegedim.next.serviceeligibility.core.services.bdd.TriggerService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.TriggerUtils;
import com.mongodb.client.ClientSession;
import jakarta.annotation.Nullable;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class TriggerBuildDeclaration {

  protected static final String TYPE_EXCEPTION = "Type d'exception: %s";

  private final SasContratService sasContratService;

  protected final ParametrageCarteTPService paramCarteTPService;

  protected final TriggerService triggerService;

  private final SuspensionService suspensionService;

  private final RestitutionCarteDao restitutionCarteDao;

  protected TriggerBuildDeclaration(
      SasContratService sasContratService,
      ParametrageCarteTPService paramCarteTPService,
      TriggerService triggerService,
      SuspensionService suspensionService,
      RestitutionCarteDao restitutionCarteDao) {
    this.sasContratService = sasContratService;
    this.paramCarteTPService = paramCarteTPService;
    this.triggerService = triggerService;
    this.suspensionService = suspensionService;
    this.restitutionCarteDao = restitutionCarteDao;
  }

  /**
   * Attention, la création de déclaration se fait avec la session mongo car elle peut être annulée
   * (rollback) s'il y a un benef en erreur, tous les benefs du contrat sont aussi en erreur
   *
   * @param trigger : trigger courant
   * @param manageBenefsContract objet contenant les informations du déclencheur courant
   * @param notManual : utile pour récupérer les paramétrages de carte tp
   */
  public void manageBenefs(
      Trigger trigger,
      ManageBenefsContract manageBenefsContract,
      boolean notManual,
      ClientSession session) {
    // transactionnal false sert pour les tests
    boolean ok = true;

    long nbDistinctPersonNumber =
        manageBenefsContract.getBenefs().stream()
            .map(TriggeredBeneficiary::getNumeroPersonne)
            .distinct()
            .count();
    // BLUE-7296 pour les contrats ayant plusieurs benefs avec le même n° personne, on veut traiter
    // les sansEffets en premier
    if (nbDistinctPersonNumber != manageBenefsContract.getBenefs().size()) {
      manageBenefsContract
          .getBenefs()
          .sort(
              Comparator.comparing(TriggeredBeneficiary::getNumeroPersonne)
                  .thenComparing(
                      (triggeredBeneficiary -> triggeredBeneficiary.getNewContract() != null)));
    }
    for (TriggeredBeneficiary benef : manageBenefsContract.getBenefs()) {
      manageBenefsContract.setWarningBenef(
          false); // le warning benef est par beneficiaire, les autres ne passent
      // pas en sasContrat (comme l'erreur benef).
      if (benef.getNewContract() == null
          || Constants.DELETE_ENDPOINT.equals(benef.getParametreAction())) {
        // triggered benef généré par endpoint delete contrat
        buildDeclarationsForBenefInDeleteContrat(trigger, manageBenefsContract, benef, session);
      } else {
        updateSuspensionsBenefs(benef, trigger.getOrigine());
        // If there is no sas or sas...
        manageSasContratInTriggerBenef(manageBenefsContract, benef);

        if (manageBenefsContract.getSasContratRecyclage() == null
            && manageBenefsContract.getSasContrat() != null) {
          log.debug(
              "Sas trouvé pour l'amc {}, le contrat {} et l'adhérent {}",
              benef.getAmc(),
              benef.getNumeroContrat(),
              benef.getNumeroAdherent());
          manageBenefsContract.setErreurBenef(true);
        } else {
          log.debug("Création de la déclaration du benef");
          // recherche systèmatique du paramétrage sans tenir compte du précédent stocké
          ParametrageCarteTP parametrageCarteTP =
              paramCarteTPService.getParametrageCarteTP(benef, notManual);
          ok =
              ok
                  && buildDeclarationsForBenef(
                      trigger, manageBenefsContract, benef, parametrageCarteTP, session);
        }
      }
    }

    boolean isError = manageBenefsContract.isErreurBenef();
    if (!isError && ok) {
      saveRestitutionsCartes(manageBenefsContract, session);
    }
  }

  private void saveRestitutionsCartes(
      ManageBenefsContract manageBenefsContract, ClientSession session) {
    for (Declaration declaration : manageBenefsContract.getDeclarations()) {
      if (Constants.CODE_ETAT_INVALIDE.equals(declaration.getCodeEtat())
          && declaration.getDateRestitution() != null) {
        RestitutionCarte restitutionCarte = createRestitutionCarte(declaration);
        restitutionCarteDao.saveRestitutionCarte(restitutionCarte, session);
      }
    }
  }

  private static RestitutionCarte createRestitutionCarte(Declaration declaration) {
    RestitutionCarte restitutionCarte = new RestitutionCarte();
    restitutionCarte.setEffetDebut(declaration.getEffetDebut());
    restitutionCarte.setIdDeclarant(declaration.getIdDeclarant());
    restitutionCarte.setNumeroPersonne(declaration.getBeneficiaire().getNumeroPersonne());
    restitutionCarte.setNumeroAdherent(declaration.getContrat().getNumeroAdherent());
    restitutionCarte.setNumeroContrat(declaration.getContrat().getNumero());
    restitutionCarte.setDateNaissance(declaration.getBeneficiaire().getDateNaissance());
    restitutionCarte.setRangNaissance(declaration.getBeneficiaire().getRangNaissance());
    restitutionCarte.setNirOd1(declaration.getBeneficiaire().getNirOd1());
    restitutionCarte.setCleNirOd1(declaration.getBeneficiaire().getCleNirOd1());
    restitutionCarte.setDateRestitutionCarte(declaration.getDateRestitution());
    restitutionCarte.setDateCreation(new Date());
    restitutionCarte.setUserCreation(declaration.getUserCreation());
    return restitutionCarte;
  }

  private void manageSasContratInTriggerBenef(
      ManageBenefsContract manageBenefsContract, TriggeredBeneficiary benef) {
    if (!manageBenefsContract.isSasCree()
        && manageBenefsContract.getSasContrat() == null
        && manageBenefsContract.getSasContratRecyclage() == null) {
      log.debug(
          String.format(
              "Recherche de sasContrat pour l'amc %s et le contrat %s",
              benef.getAmc(), benef.getNumeroContrat()));

      manageBenefsContract.setSasContrat(
          sasContratService.getByFunctionalKey(
              benef.getAmc(), benef.getNumeroContrat(), benef.getNumeroAdherent()));
    }
  }

  private void updateSuspensionsBenefs(TriggeredBeneficiary benef, TriggerEmitter emitter) {
    ServicePrestationTriggerBenef newContract = benef.getNewContract();
    ServicePrestationTriggerBenef oldContract = benef.getOldContract();

    // If the trigger emitter is an event...
    if (TriggerEmitter.Event.equals(emitter) && oldContract != null && newContract != null) {
      String etatSuspension = suspensionService.suspensionCalculation(oldContract, newContract);
      benef.setEtatSuspension(etatSuspension);
    }
  }

  protected abstract void buildDeclarationsForBenefInDeleteContrat(
      Trigger trigger,
      ManageBenefsContract manageBenefsContract,
      TriggeredBeneficiary benef,
      ClientSession session);

  protected abstract boolean buildDeclarationsForBenef(
      Trigger trigger,
      ManageBenefsContract manageBenefsContract,
      TriggeredBeneficiary benef,
      ParametrageCarteTP param,
      ClientSession session);

  protected void handleBuildDeclarationError(
      ManageBenefsContract manageBenefsContract,
      TriggeredBeneficiary benef,
      @Nullable TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly,
      String message,
      boolean overrideLastMotif) {
    manageBenefsContract.setErreurBenef(true);
    manageBenefsContract.setSasCree(true);
    log.error(message + " " + benef.getId() + " gestion du sasContrat");

    String saveToSAS =
        triggeredBeneficiaryAnomaly != null
            ? triggeredBeneficiaryAnomaly.getDescription()
            : message;
    SasContrat sasToUpdate =
        manageBenefsContract.getSasContratRecyclage() != null
            ? manageBenefsContract.getSasContratRecyclage()
            : manageBenefsContract.getSasContrat();
    manageBenefsContract.setSasContrat(
        sasContratService.manageSasContrat(sasToUpdate, benef, saveToSAS));
    log.error(message + " " + benef.getId() + " modification du benef");
    TriggerUtils.manageBenefError(true, benef, triggeredBeneficiaryAnomaly, overrideLastMotif);
    manageBenefsContract.setNbBenefKO(manageBenefsContract.getNbBenefKO() + 1);
  }

  protected void handleBuildDeclarationWarning(
      ManageBenefsContract manageBenefsContract,
      TriggeredBeneficiary benef,
      List<TriggeredBeneficiaryAnomaly> triggeredBeneficiaryAnomalies) {
    manageBenefsContract.setWarningBenef(true);
    manageBenefsContract.setNbBenefWarning(manageBenefsContract.getNbBenefWarning() + 1);
    triggeredBeneficiaryAnomalies.forEach(
        triggeredBeneficiaryAnomaly -> {
          log.error(triggeredBeneficiaryAnomaly.getDescription());
          TriggerUtils.manageBenefWarning(benef, triggeredBeneficiaryAnomaly, true);
        });
  }
}
