package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DestinatairePrestations;
import com.cegedim.next.serviceeligibility.core.services.pojo.DataForEventRibModification;
import java.util.List;
import java.util.Objects;
import org.springframework.util.CollectionUtils;

public class DestinatairePrestationsEventUtil {
  private DestinatairePrestationsEventUtil() {}

  public static boolean newRecipientNeedEvent(
      final DestinatairePrestations newDestinataire,
      final DestinatairePrestations oldDestinataire) {
    boolean oldNotVir =
        !Constants.MODE_PAIEMENT_VIR.equals(oldDestinataire.getModePaiementPrestations().getCode());
    boolean newVir =
        Constants.MODE_PAIEMENT_VIR.equals(newDestinataire.getModePaiementPrestations().getCode());
    String newIban = newDestinataire.getRib() != null ? newDestinataire.getRib().getIban() : null;
    String oldIban = oldDestinataire.getRib() != null ? oldDestinataire.getRib().getIban() : null;
    return oldNotVir && newVir || !Objects.equals(newIban, oldIban);
  }

  public static void addNewEvent(
      final String idDeclarant,
      final List<DataForEventRibModification> dataForEventRibModificationList,
      final String numeroPersonne,
      final DestinatairePrestations newdestinatairePrestations,
      final DestinatairePrestations oldDestinatairePrestations) {
    final boolean exist =
        dataForEventRibModificationList.stream()
            .allMatch(
                data ->
                    data.getNewDestinataire()
                            .getIdDestinatairePaiements()
                            .equals(newdestinatairePrestations.getIdDestinatairePaiements())
                        && data.getOldDestinataire()
                            .getIdDestinatairePaiements()
                            .equals(oldDestinatairePrestations.getIdDestinatairePaiements()));
    if (CollectionUtils.isEmpty(dataForEventRibModificationList) || !exist) {
      dataForEventRibModificationList.add(
          new DataForEventRibModification(
              idDeclarant, oldDestinatairePrestations, newdestinatairePrestations, numeroPersonne));
    }
  }

  public static void manageRecipientEventChange(
      List<DestinatairePrestations> newDestinatairePrestationsList,
      List<DestinatairePrestations> oldDestinatairePrestationsList,
      List<DataForEventRibModification> dataForEventRibModificationList,
      String idDeclarant,
      String numeroPersonne) {
    oldDestinatairePrestationsList.sort(
        (destinataire1, destinataire2) ->
            -DateUtils.compareDate(
                destinataire1.getPeriode().getFin(), destinataire2.getPeriode().getFin()));
    if (!CollectionUtils.isEmpty(newDestinatairePrestationsList)) {
      newDestinatairePrestationsList.sort(
          (destinataire1, destinataire2) ->
              -DateUtils.compareDate(
                  destinataire1.getPeriode().getFin(), destinataire2.getPeriode().getFin()));
      for (DestinatairePrestations newDestinatairePrestations : newDestinatairePrestationsList) {
        final DestinatairePrestations oldDestinataireCandidate;
        if (newDestinatairePrestations.getPeriode().getFin() == null) {
          // oldDestinataireList NotEmpty
          oldDestinataireCandidate = oldDestinatairePrestationsList.get(0);
          if (newRecipientNeedEvent(newDestinatairePrestations, oldDestinataireCandidate)) {
            addNewEvent(
                idDeclarant,
                dataForEventRibModificationList,
                numeroPersonne,
                newDestinatairePrestations,
                oldDestinataireCandidate);
          }
        } else {
          oldDestinataireCandidate =
              oldDestinatairePrestationsList.stream()
                  .filter(
                      destinataire1 ->
                          destinataire1
                                  .getIdDestinatairePaiements()
                                  .equals(newDestinatairePrestations.getIdDestinatairePaiements())
                              && destinataire1
                                  .getPeriode()
                                  .getDebut()
                                  .equals(newDestinatairePrestations.getPeriode().getDebut()))
                  .findFirst()
                  .orElse(null);
          if (oldDestinataireCandidate != null
              && newRecipientNeedEvent(newDestinatairePrestations, oldDestinataireCandidate)) {
            addNewEvent(
                idDeclarant,
                dataForEventRibModificationList,
                numeroPersonne,
                newDestinatairePrestations,
                oldDestinataireCandidate);
          }
        }
      }
    }
  }

  public static void extractAndFilterListDestinataire(
      final List<DestinatairePrestations> listDestinataire,
      final DataAssure dataAssure,
      final boolean control) {
    if (dataAssure != null && dataAssure.getDestinatairesPaiements() != null) {
      for (final DestinatairePrestations destinataire : dataAssure.getDestinatairesPaiements()) {
        if (!control
            || (destinataire.getRib() != null
                && destinataire.getRib().getIban() != null
                && Constants.MODE_PAIEMENT_VIR.equals(
                    destinataire.getModePaiementPrestations().getCode()))) {
          listDestinataire.add(destinataire);
        }
      }
    }
  }
}
