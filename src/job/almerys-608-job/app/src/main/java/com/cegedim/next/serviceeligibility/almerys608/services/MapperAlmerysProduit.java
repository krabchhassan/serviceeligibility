package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.*;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejet;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsExtractions;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class MapperAlmerysProduit {
  private final UtilService utilService;

  public void mapProduitStep1(
      TmpObject2 tmpObject2,
      List<ProduitToCheck> produitsToCheckList,
      List<ProduitsAlmerysExclus> produitsAlmerysExclus,
      List<Rejet> rejetNonBloquantList,
      List<Rejet> rejetsProduitsExclusList) {

    String numContrat = UtilService.mapRefNumContrat(tmpObject2);
    if (StringUtils.isBlank(tmpObject2.getDomaineDroit().getCodeExterneProduit())) {
      // rejet non bloquant
      String errorMessage =
          "Produit : "
              + tmpObject2.getDomaineDroit().getCodeProduit()
              + ", garantie : "
              + tmpObject2.getDomaineDroit().getCodeGarantie();
      Rejet r =
          utilService.getRejet(
              tmpObject2,
              numContrat,
              ConstantesRejetsExtractions.REJET_C12.getCode(),
              errorMessage);

      rejetNonBloquantList.add(r);
    }
    if (produitsAlmerysExclus.stream()
        .map(ProduitsAlmerysExclus::getCodeExterneProduit)
        .toList()
        .contains(tmpObject2.getDomaineDroit().getCodeExterneProduit())) {
      // rejet produit exclu
      String errorMessage =
          String.format(
              "AMC : %s, CSD : %s, Produit Externe : %s",
              tmpObject2.getIdDeclarant(),
              tmpObject2.getContrat().getCritereSecondaireDetaille(),
              tmpObject2.getDomaineDroit().getCodeExterneProduit());
      Rejet r =
          utilService.getRejet(
              tmpObject2,
              numContrat,
              ConstantesRejetsExtractions.REJET_A08.getCode(),
              errorMessage);

      rejetsProduitsExclusList.add(r);
    } else {
      ProduitToCheck produitToCheck = new ProduitToCheck();
      // mapping vers produitsToCheck for step2
      Rejet r =
          utilService.getRejet(
              tmpObject2, numContrat, null, null); // rejet préparé tant qu'on a accès aux data
      produitToCheck.setNumContrat(numContrat);
      produitToCheck.setRefInterneOS(tmpObject2.getBeneficiaire().getNumeroPersonne());
      produitToCheck.setOrdre(tmpObject2.getDomaineDroit().getPrioriteDroit().getCode());
      produitToCheck.setReferenceProduit(
          UtilService.resizeField(tmpObject2.getDomaineDroit().getCodeExterneProduit(), 80));
      String dateEntreeProduit;
      if (Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration())) {
        dateEntreeProduit =
            tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut(); // yyyy/MM/dd
      } else {
        dateEntreeProduit =
            Objects.requireNonNullElse(
                tmpObject2.getDomaineDroit().getDateAdhesionCouverture(),
                tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut()); // yyyy/MM/dd
      }
      String dateSortieProduit =
          tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeFin(); // yyyy/MM/dd
      produitToCheck.setDateEntreeProduit(dateEntreeProduit);
      produitToCheck.setDateSortieProduit(dateSortieProduit);
      produitToCheck.setRejet(r);

      produitsToCheckList.add(produitToCheck);
    }
  }

  public void mapProduitStep2(
      List<ProduitToCheck> produitsToCheckList,
      List<Produit> produitsCheckedList,
      List<Rejet> rejetNonBloquantList) {
    produitsToCheckList.sort(
        Comparator.comparing(ProduitToCheck::getDateSortieProduit)
            .thenComparing(ProduitToCheck::getDateEntreeProduit, Comparator.reverseOrder())
            .thenComparing(ProduitToCheck::getOrdre)); /* dateSortie, dateEntree desc, ordre*/
    String dateFinMax = null;
    String oldId = null;
    String texteProduitOld = null;
    for (ProduitToCheck produitToCheck : produitsToCheckList) {
      String numContrat = produitToCheck.getNumContrat();
      String refInterneOS = produitToCheck.getRefInterneOS();
      String ordre = Objects.requireNonNullElse(produitToCheck.getOrdre(), "99");
      String dateEntreeProduit = produitToCheck.getDateEntreeProduit();
      String dateSortieProduit = produitToCheck.getDateSortieProduit();
      String referenceProduit = produitToCheck.getReferenceProduit();
      String id = numContrat + refInterneOS + ordre;
      boolean isProduitAnnule =
          StringUtils.isNotBlank(dateSortieProduit)
              && DateUtils.before(
                  dateSortieProduit, dateEntreeProduit, DateUtils.SLASHED_FORMATTER);
      boolean isError =
          dateFinMax != null
              && id.equals(oldId)
              && !DateUtils.after(dateEntreeProduit, dateFinMax, DateUtils.SLASHED_FORMATTER)
              && !isProduitAnnule;
      String texteProduit =
          referenceProduit
              + " du "
              + dateEntreeProduit.replace("/", "-") // "yyyy-MM-dd"
              + (StringUtils.isNotBlank(dateSortieProduit)
                  ? (" au " + dateSortieProduit.replace("/", "-"))
                  : ""); // "yyyy-MM-dd"
      String messageError = isError ? texteProduitOld + " et " + texteProduit : "";
      if (!isProduitAnnule) {
        dateFinMax = dateSortieProduit;
        texteProduitOld = texteProduit;
      } else if (!id.equals(oldId)) {
        dateFinMax = null;
        oldId = id;
      }
      if (isError) {
        // rejet non bloquant
        Rejet r = produitToCheck.getRejet();
        r.setError(messageError);
        r.setCodeRejetTraces(ConstantesRejetsExtractions.REJET_A07.getCode());
        rejetNonBloquantList.add(r);
      }
      Produit produit = new Produit();
      // mapping vers Produit
      produit.setReferenceProduit(UtilService.resizeField(referenceProduit, 80));
      produit.setDateSortieProduit(dateSortieProduit);
      produit.setDateEntreeProduit(dateEntreeProduit);
      produit.setOrdre(UtilService.resizeNumericField(Integer.parseInt(ordre), 2));
      produit.setProduitAnnule(isProduitAnnule);
      produit.setNumeroContrat(numContrat);
      produit.setRefInterneOS(refInterneOS);
      produitsCheckedList.add(produit);
    }
  }
}
