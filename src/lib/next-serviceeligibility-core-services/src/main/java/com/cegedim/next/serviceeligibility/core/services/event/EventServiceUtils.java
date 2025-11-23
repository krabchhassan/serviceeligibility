package com.cegedim.next.serviceeligibility.core.services.event;

import com.cegedim.beyond.schemas.CombiAjout;
import com.cegedim.beyond.schemas.CombiModif;
import com.cegedim.next.serviceeligibility.core.almerysProductRef.LotAlmerys;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ProductCombination;
import java.util.*;
import java.util.function.Function;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceUtils {

  public static List<CombiAjout> formatCombinationsForCreation(
      List<ProductCombination> productCombinations) {
    List<CombiBuffer> combinations = new ArrayList<>();
    for (int i = 0; i < productCombinations.size(); i++) {
      ProductCombination productCombination = productCombinations.get(i);
      CombiBuffer result = formatCombination(i, productCombination, null);
      combinations.add(result);
    }
    return mapCombiAjout(combinations);
  }

  private static List<CombiAjout> mapCombiAjout(List<CombiBuffer> combinations) {
    List<CombiAjout> result = new ArrayList<>();
    for (CombiBuffer combi : ListUtils.emptyIfNull(combinations)) {
      CombiAjout ajout = new CombiAjout();
      ajout.setNumero(combi.getNumero());
      ajout.setDebut(combi.getDebut());
      ajout.setFin(combi.getFin());
      ajout.setFinPrecedente(combi.getFinPrecedente());
      ajout.setGtAjoutees(combi.getGtAjoutees());
      ajout.setLotAjoutes(combi.getLotAjoutes());
      result.add(ajout);
    }
    return result;
  }

  public static List<CombiModif> formatCombinationsForModification(
      AlmerysProduct existingProduct, AlmerysProduct updatedProduct) {
    List<CombiBuffer> combinations = new ArrayList<>();
    List<ProductCombination> existingProductCombinations = existingProduct.getProductCombinations();
    List<ProductCombination> productCombinations = updatedProduct.getProductCombinations();

    boolean hasNewCombinations = productCombinations.size() > existingProductCombinations.size();
    for (int i = 0; i < existingProductCombinations.size(); i++) {
      StringBuilder diffOnExistingCombination = new StringBuilder();

      ProductCombination existingCombination = existingProductCombinations.get(i);
      ProductCombination newCombination = productCombinations.get(i);
      if (isUpdatedCombination(existingCombination, newCombination)) {
        CombiBuffer result = formatCombination(i, newCombination, existingCombination);
        combinations.add(result);
      }
    }

    for (int i = existingProductCombinations.size(); i < productCombinations.size(); i++) {
      ProductCombination newCombination = productCombinations.get(i);
      CombiBuffer result = formatCombination(i, newCombination, null);
      combinations.add(result);
    }

    return mapCombiModif(combinations);
  }

  private static List<CombiModif> mapCombiModif(List<CombiBuffer> combinations) {
    List<CombiModif> result = new ArrayList<>();
    for (CombiBuffer combi : ListUtils.emptyIfNull(combinations)) {
      CombiModif modif = new CombiModif();
      modif.setNumero(combi.getNumero());
      modif.setDebut(combi.getDebut());
      modif.setFin(combi.getFin());
      modif.setFinPrecedente(combi.getFinPrecedente());
      modif.setGtAjoutees(combi.getGtAjoutees());
      modif.setGtSupprimees(combi.getGtSupprimees());
      modif.setLotAjoutes(combi.getLotAjoutes());
      modif.setLotSupprimes(combi.getLotSupprimes());
      result.add(modif);
    }
    return result;
  }

  private static CombiBuffer formatCombination(
      int i, ProductCombination newCombination, ProductCombination existingCombination) {
    CombiBuffer result = new CombiBuffer();
    result.setNumero(String.valueOf(i + 1));
    formatStartDate(result, newCombination.getDateDebut());
    if (existingCombination != null) {
      formatEndDate(result, newCombination.getDateFin(), existingCombination.getDateFin(), true);
      formatGts(
          result,
          newCombination.getGarantieTechniqueList(),
          existingCombination.getGarantieTechniqueList(),
          true);
      formatLots(
          result,
          newCombination.getLotAlmerysList(),
          existingCombination.getLotAlmerysList(),
          true);
    } else {
      formatEndDate(result, newCombination.getDateFin(), null, false);
      formatGts(result, newCombination.getGarantieTechniqueList(), null, false);
      formatLots(result, newCombination.getLotAlmerysList(), null, false);
    }
    return result;
  }

  private static boolean isUpdatedCombination(
      ProductCombination existingCombination, ProductCombination newCombination) {

    return !StringUtils.equals(existingCombination.getDateFin(), newCombination.getDateFin())
        || isUpdatedLotAlmerysList(
            existingCombination.getLotAlmerysList(), newCombination.getLotAlmerysList())
        || isUpdatedGarantieTechniqueList(
            existingCombination.getGarantieTechniqueList(),
            newCombination.getGarantieTechniqueList());
  }

  private static boolean isUpdatedLotAlmerysList(
      List<LotAlmerys> existingLotAlmerys, List<LotAlmerys> newLotAlmerys) {
    List<LotAlmerys> existingLotAlmerysList = ListUtils.emptyIfNull(existingLotAlmerys);
    List<LotAlmerys> newLotAlmerysList = ListUtils.emptyIfNull(newLotAlmerys);

    return existingLotAlmerysList.size() != newLotAlmerysList.size()
        || existingLotAlmerysList.stream()
            .anyMatch(
                lotAlmerys ->
                    isUpdatedLotAlmerys(
                        lotAlmerys,
                        newLotAlmerysList.get(existingLotAlmerysList.indexOf(lotAlmerys))));
  }

  private static boolean isUpdatedLotAlmerys(
      LotAlmerys existingLotAlmerys, LotAlmerys newLotAlmerys) {
    return !StringUtils.equals(
        existingLotAlmerys.getDateSuppressionLogique(), newLotAlmerys.getDateSuppressionLogique());
  }

  private static boolean isUpdatedGarantieTechniqueList(
      List<GarantieTechnique> existingGarantieTechnique,
      List<GarantieTechnique> newGarantieTechnique) {
    List<GarantieTechnique> existingGarantieTechniqueList =
        ListUtils.emptyIfNull(existingGarantieTechnique);
    List<GarantieTechnique> newGarantieTechniqueList = ListUtils.emptyIfNull(newGarantieTechnique);

    return existingGarantieTechniqueList.size() != newGarantieTechniqueList.size()
        || newGarantieTechniqueList.stream()
            .anyMatch(
                garantieTechnique ->
                    isUpdatedGarantieTechnique(
                        garantieTechnique,
                        existingGarantieTechniqueList.get(
                            newGarantieTechniqueList.indexOf(garantieTechnique))));
  }

  private static boolean isUpdatedGarantieTechnique(
      GarantieTechnique existingGarantieTechnique, GarantieTechnique newGarantieTechnique) {

    return !StringUtils.equals(
        existingGarantieTechnique.getDateSuppressionLogique(),
        newGarantieTechnique.getDateSuppressionLogique());
  }

  private static void formatStartDate(CombiBuffer result, String startDate) {
    result.setDebut(startDate);
  }

  private static void formatEndDate(
      CombiBuffer result, String endDate, String previousEndDate, boolean update) {
    result.setFin(StringUtils.isNotBlank(endDate) ? endDate : "-");
    if (StringUtils.isNotBlank(endDate) && !endDate.equals(previousEndDate) && update) {
      result.setFinPrecedente(StringUtils.isNotBlank(previousEndDate) ? previousEndDate : "-");
    }
  }

  private static void formatGts(
      CombiBuffer result,
      List<GarantieTechnique> garantieTechniqueList,
      List<GarantieTechnique> previousGtList,
      boolean update) {
    if (update) {
      List<GarantieTechnique> addedGts =
          tUpdated(
              garantieTechniqueList,
              previousGtList,
              GarantieTechnique::getDateSuppressionLogique,
              true);
      if (CollectionUtils.isNotEmpty(addedGts)) {
        result.setGtAjoutees(addedGts.stream().map(EventServiceUtils::formatGt).toList());
      }

      List<GarantieTechnique> deletedGts =
          tUpdated(
              previousGtList,
              garantieTechniqueList,
              GarantieTechnique::getDateSuppressionLogique,
              false);
      if (CollectionUtils.isNotEmpty(deletedGts)) {
        result.setGtSupprimees(deletedGts.stream().map(EventServiceUtils::formatGt).toList());
      }
    } else if (CollectionUtils.isNotEmpty(garantieTechniqueList)) {
      result.setGtAjoutees(
          garantieTechniqueList.stream().map(EventServiceUtils::formatGt).toList());
    }
  }

  private static <T> List<T> tUpdated(
      List<T> fromTs, List<T> toTs, Function<T, String> getDate, boolean adding) {
    List<T> from = ListUtils.emptyIfNull(fromTs);
    List<T> to = ListUtils.emptyIfNull(toTs);
    return from.stream()
        .filter(
            t -> {
              int index = to.indexOf(t);
              if (index < 0) {
                // Si on trouve pas dans l autre liste alors changement
                return true;
              }

              T other = to.get(index);
              String dateSuppT = getDate.apply(t);
              String dateSuppOther = getDate.apply(other);
              if (Objects.equals(dateSuppOther, dateSuppT)) {
                // Si les dates de suppression sont les memes alors pas de changement
                return false;
              }

              // Si on est en AJOUT et que le nouvel objet n a pas de date supp alors changement
              // Ou
              // Si on est en SUPPRESSION et que le nouvel objet a une date supp alors changement
              return adding && dateSuppT == null || !adding && dateSuppOther != null;
            })
        .toList();
  }

  private static String formatGt(GarantieTechnique gt) {
    return gt.getCodeAssureur() + " - " + gt.getCodeGarantie();
  }

  private static void formatLots(
      CombiBuffer result,
      List<LotAlmerys> lotAlmerysList,
      List<LotAlmerys> previousLotList,
      boolean update) {
    if (update) {
      List<LotAlmerys> addedLots =
          tUpdated(lotAlmerysList, previousLotList, LotAlmerys::getDateSuppressionLogique, true);
      if (CollectionUtils.isNotEmpty(addedLots)) {
        result.setLotAjoutes(addedLots.stream().map(LotAlmerys::getCode).toList());
      }

      List<LotAlmerys> deletedLots =
          tUpdated(previousLotList, lotAlmerysList, LotAlmerys::getDateSuppressionLogique, false);
      if (CollectionUtils.isNotEmpty(deletedLots)) {
        result.setLotSupprimes(deletedLots.stream().map(LotAlmerys::getCode).toList());
      }
    } else if (CollectionUtils.isNotEmpty(lotAlmerysList)) {
      result.setLotAjoutes(lotAlmerysList.stream().map(LotAlmerys::getCode).toList());
    }
  }

  @Data
  private static class CombiBuffer {
    String numero;
    String debut;
    String fin;
    String finPrecedente;
    List<String> gtAjoutees;
    List<String> gtSupprimees;
    List<String> lotAjoutes;
    List<String> lotSupprimes;
  }
}
