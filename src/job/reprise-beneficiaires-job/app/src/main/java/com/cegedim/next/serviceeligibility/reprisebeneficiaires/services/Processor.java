package com.cegedim.next.serviceeligibility.reprisebeneficiaires.services;

import static com.cegedim.next.serviceeligibility.core.job.utils.Constants.*;

import com.cegedim.next.serviceeligibility.core.job.parameters.RepriseBeneficiaireParameters;
import com.cegedim.next.serviceeligibility.core.job.utils.ContractType;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRepriseBenefs;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Processor {
  @Autowired private CrexProducer crexProducer;

  @Autowired private RepriseServicePrestationService repriseServicePrestationService;

  @Autowired private RepriseDeclarationService repriseDeclarationService;

  @Autowired private ReprisePrestijService reprisePrestijService;

  private final Logger logger = LoggerFactory.getLogger(Processor.class);

  public void process(RepriseBeneficiaireParameters parameters) { // NOSONAR execution du batch, on
    // veut remonter
    // toutes les erreurs
    CompteRenduRepriseBenefs compteRendu = new CompteRenduRepriseBenefs();

    try {
      // Process en fonction des entrées (HTP, TP, PRESTIJ)
      // La liste est forcément non-vide car vérifiée avant l'appel a cette méthode
      for (ContractType contractType : parameters.getContractTypes()) {
        switch (contractType) {
          case HTP:
            logger.debug("Reprise des contrats HTP");
            parameters.setCodeRetour(repriseServicePrestationService.processHtp(compteRendu));
            break;
          case TP:
            logger.debug("Reprise des déclarations TP");
            parameters.setCodeRetour(
                repriseDeclarationService.processTp(parameters.getDateReprise(), compteRendu));
            break;
          case PRESTIJ:
            logger.debug("Reprise des contrats PrestIj");
            parameters.setCodeRetour(reprisePrestijService.processPrestij(compteRendu));
            break;
          // Ne devrait jamais arriver car la liste a été filtrée avant
          default:
            logger.error("Erreur lors de la lecture du paramètre {}", contractType);
            parameters.setCodeRetour(CODE_RETOUR_BAD_REQUEST);
        }

        // If we found an error, we get out of the loop
        if (parameters.getCodeRetour() != CODE_RETOUR_OK) {
          break;
        }
      }
    } catch (InterruptedException e) {
      logger.error("Erreur lors de l'envoi de beneficiaires dans Kafka : {}", e.getMessage(), e);
      parameters.setCodeRetour(CODE_RETOUR_UNEXPECTED_EXCEPTION);
      Thread.currentThread().interrupt();
    } catch (Exception e) {
      logger.error(
          "Erreur generique de type {} lors du traitement : {}", e.getClass(), e.getMessage(), e);
      parameters.setCodeRetour(CODE_RETOUR_UNEXPECTED_EXCEPTION);
    } finally {
      displayResult(compteRendu);
      crexProducer.generateCrex(compteRendu);
    }
  }

  private void displayResult(CompteRenduRepriseBenefs compteRendu) {
    logger.info("=== Resultat de la reprise des beneficiaires ===");

    // Benef
    logger.info("Nombre de contrats HTP repris : {}", compteRendu.getContratsHtpRepris());
    logger.info(
        "Nombre de beneficiaires reemis pour les contrats HTP : {}",
        compteRendu.getBenefsReemisViaHtp());
    logger.info("Nombre de déclarations TP reprises : {}", compteRendu.getContratsTpRepris());
    logger.info("Nombre de contrats PrestIj repris : {}", compteRendu.getContratsPrestijRepris());
    logger.info(
        "Nombre de beneficiaires reemis pour les contrats PrestIj : {}",
        compteRendu.getBenefsReemisViaPrestij());
  }
}
