package com.cegedim.next.serviceeligibility.core.bobb.util;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ContractElementServiceUtil {

  private ContractElementServiceUtil() {
    // UTIL
  }

  public static List<ProductElementLight> getProductElementLights(
      ContractElement contractElement, LocalDateTime dateDebut, LocalDateTime dateFin) {
    if (isValidContractElement(contractElement)) {
      List<ProductElement> productElementsOverlapAndSorted =
          contractElement.getProductElements().stream()
              .filter(
                  productElement ->
                      isPeriodeProductElementValid(productElement)
                          && DateUtils.isOverlapping(
                              productElement.getFrom(), productElement.getTo(), dateDebut, dateFin))
              .sorted(Comparator.comparing(ProductElement::getFrom))
              .toList();
      List<ProductElementLight> productElementLights = new ArrayList<>();
      LocalDateTime previousEnd = null;
      for (ProductElement productElement : productElementsOverlapAndSorted) {
        if (!isContiguous(productElementLights, productElement, dateDebut, previousEnd)) {
          // Si le premier ProductElement ne couvre pas la dateDebut ou que deux
          // ProductElement qui se suivent ne sont pas contigues alors on retourne une
          // liste vide
          return Collections.emptyList();
        }
        ProductElementLight pel = new ProductElementLight();
        pel.setCodeProduct(productElement.getCodeProduct());
        pel.setCodeOffer(productElement.getCodeOffer());
        pel.setCodeAmc(productElement.getCodeAmc());
        productElementLights.add(pel);
        previousEnd = productElement.getTo();
      }

      if (previousEnd == null
          || (dateFin != null && !previousEnd.isBefore(dateFin.minusDays(1)))
          || doubleProductElement(productElementLights)) {
        // Si le dernier ProductElement couvre la dateFin alors on retourne la liste des
        // ProductElementLight couvrant la periode dateDebut->dateFin en continue
        // (periodes contigues)
        return productElementLights;
      }

      return Collections.emptyList();
    }
    return Collections.emptyList();
  }

  private static boolean doubleProductElement(List<ProductElementLight> productElementLights) {
    if (productElementLights.size() > 1) {
      ProductElementLight reference = productElementLights.get(0);

      return productElementLights.stream()
          .allMatch(
              element ->
                  reference.getCodeProduct().equals(element.getCodeProduct())
                      && reference.getCodeOffer().equals(element.getCodeOffer())
                      && reference.getCodeAmc().equals(element.getCodeAmc()));
    }
    return false;
  }

  public static boolean isPeriodeProductElementValid(ProductElement productElement) {
    return productElement.getTo() == null
        || productElement.getTo().isAfter(productElement.getFrom().minusDays(1));
  }

  private static boolean isValidContractElement(ContractElement contractElement) {
    return contractElement != null && !contractElement.isIgnored();
  }

  private static boolean isContiguous(
      List<ProductElementLight> productElementLights,
      ProductElement productElement,
      LocalDateTime dateDebut,
      LocalDateTime previousEnd) {
    return !(productElementLights.isEmpty()
            && productElement.getFrom().isAfter(dateDebut.plusDays(1))
        || (previousEnd != null && previousEnd.plusDays(1).isBefore(productElement.getFrom())));
  }
}
