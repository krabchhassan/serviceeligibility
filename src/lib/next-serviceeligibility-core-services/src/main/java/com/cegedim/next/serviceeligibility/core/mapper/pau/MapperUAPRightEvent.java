package com.cegedim.next.serviceeligibility.core.mapper.pau;

import static java.util.stream.Collectors.groupingBy;

import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.services.pojo.ProductsForRight;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.BenefitType;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Domain;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Product;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Right;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

@Service
public class MapperUAPRightEvent extends AbstractMapperUAPRight {

  protected List<Right> manageRightDomains(
      final TreeMap<String, List<ProductsForRight>> gtProductsForRightV4Map,
      final String startDate,
      final String endDate,
      List<String> domains,
      final boolean isTpOffline,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      final boolean foundContractsWithForce) {
    final List<Right> rights = new ArrayList<>();

    for (final Map.Entry<String, List<ProductsForRight>> entryProductForRightV4 :
        gtProductsForRightV4Map.entrySet()) {
      final Right right = new Right();
      final List<Product> productV4List = new ArrayList<>();
      final TreeMap<Pair<String, String>, List<ProductsForRight>> product =
          entryProductForRightV4.getValue().stream()
              .collect(
                  groupingBy(
                      productsForRight ->
                          new ImmutablePair<>(
                              productsForRight.getCodeProduit(), productsForRight.getCodeOffre()),
                      TreeMap::new,
                      Collectors.toList()));
      for (Map.Entry<Pair<String, String>, List<ProductsForRight>> entryProduct :
          product.entrySet()) {
        manageRightDomainForEvent(
            isTpOffline,
            startDate,
            endDate,
            right,
            productV4List,
            entryProduct,
            dateRestitution,
            isExclusiviteCarteDematerialise,
            foundContractsWithForce);
      }
      if (CollectionUtils.isNotEmpty(productV4List)) {
        right.setProducts(productV4List);
        rights.add(right);
      }
    }
    return rights;
  }

  private void manageRightDomainForEvent(
      boolean isTpOffline,
      String startDate,
      String endDate,
      Right right,
      List<Product> productV4List,
      Map.Entry<Pair<String, String>, List<ProductsForRight>> mapEntry,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      boolean foundContractsWithForce) {
    Product productV4;
    Optional<Product> optionalProductV4 =
        productV4List.stream()
            .filter(
                product ->
                    product.getProductCode().equals(mapEntry.getKey().getLeft())
                        && product.getOfferCode().equals(mapEntry.getKey().getRight()))
            .findFirst();
    if (optionalProductV4.isPresent()) {
      for (final ProductsForRight productsForRight : mapEntry.getValue()) {
        final Period period =
            this.controlDate(
                startDate,
                endDate,
                productsForRight.getPeriodeDroitContractTP(),
                isTpOffline,
                dateRestitution,
                isExclusiviteCarteDematerialise,
                foundContractsWithForce);
        if (period != null) {
          final BenefitType benefitType = new BenefitType();
          benefitType.setPeriod(period);
          benefitType.setBenefitType(productsForRight.getCodeNaturePrestation());

          this.setDomains(productsForRight, benefitType);

          if (optionalProductV4.get().getBenefitsType() == null) {
            optionalProductV4.get().setBenefitsType(new HashSet<>());
          }
          optionalProductV4.get().getBenefitsType().add(benefitType);
        }
      }
    } else {
      productV4 =
          this.getProductForEvent(
              isTpOffline,
              startDate,
              endDate,
              right,
              mapEntry,
              dateRestitution,
              isExclusiviteCarteDematerialise,
              foundContractsWithForce);
      if (productV4 != null) {
        productV4List.add(productV4);
      }
    }
  }

  private Product getProductForEvent(
      boolean isTpOffline,
      String startDate,
      String endDate,
      Right right,
      Map.Entry<Pair<String, String>, List<ProductsForRight>> mapEntry,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      boolean foundContractsWithForce) {
    Product productV4 = new Product();
    Map<BenefitType, BenefitType> benefitTypeMap = new HashMap<>();
    Period periodProduct = new Period();
    periodProduct.setStart(startDate.replace("-", "/"));
    if (endDate != null) {
      periodProduct.setEnd(endDate.replace("-", "/"));
    }
    for (ProductsForRight productsForRight : mapEntry.getValue()) {
      PeriodeDroitContractTP periodeDroitContractTP =
          fillRightAndGetPeriodeDroit(productsForRight, right);
      periodProduct.setStart(
          DateUtils.getMaxDate(periodProduct.getStart(), periodeDroitContractTP.getPeriodeDebut()));
      if (isTpOffline) {
        String dateFinOffline =
            Util.getDateFinOffline(
                periodeDroitContractTP.getPeriodeFin(),
                periodeDroitContractTP.getPeriodeFinFermeture(),
                dateRestitution,
                isExclusiviteCarteDematerialise);
        periodProduct.setEnd(DateUtils.getMinDate(periodProduct.getEnd(), dateFinOffline));
      } else {
        periodProduct.setEnd(
            DateUtils.getMinDate(periodProduct.getEnd(), periodeDroitContractTP.getPeriodeFin()));
      }

      productV4 =
          getProduct(
              productsForRight.getCodeProduit(),
              productsForRight.getCodeOffre(),
              productsForRight.getOc(),
              startDate,
              endDate);
      Period period =
          controlDate(
              startDate,
              endDate,
              periodeDroitContractTP,
              isTpOffline,
              dateRestitution,
              isExclusiviteCarteDematerialise,
              foundContractsWithForce);
      if (period != null) {
        final BenefitType benefitType = new BenefitType();
        benefitType.setPeriod(period);
        benefitType.setBenefitType(productsForRight.getCodeNaturePrestation());

        this.setDomains(productsForRight, benefitType);
        if (!benefitTypeMap.containsKey(benefitType)) {
          benefitTypeMap.put(benefitType, benefitType);
        } else if (CollectionUtils.isNotEmpty(benefitType.getDomains())) {
          List<Domain> domains =
              Objects.requireNonNullElse(
                  benefitTypeMap.get(benefitType).getDomains(), new ArrayList<>());
          domains.addAll(benefitType.getDomains());
          benefitTypeMap.get(benefitType).setDomains(domains);
        }

        if (foundContractsWithForce) {
          String endForcedDate =
              DateUtils.getMaxDateOrNull(period.getEnd(), periodProduct.getEnd());
          if (endForcedDate != null) {
            periodProduct.setEnd(endForcedDate.replace("-", "/"));
          } else {
            periodProduct.setEnd(null);
          }
        }
      }
    }
    if (CollectionUtils.isNotEmpty(benefitTypeMap.values())) {
      Set<BenefitType> benefitTypeList = new HashSet<>(benefitTypeMap.values());
      productV4.setBenefitsType(benefitTypeList);
      fillProductPeriod(periodProduct, benefitTypeList);
      periodProduct.setStart(
          DateUtils.formatDate(DateUtils.stringToDate(periodProduct.getStart())));
      periodProduct.setEnd(DateUtils.formatDate(DateUtils.stringToDate(periodProduct.getEnd())));
      productV4.setPeriod(periodProduct);

      return productV4;
    }
    return null;
  }

  @Override
  protected Product initializeProductV4(
      final String codeProduct, final String codeOffre, final Oc oc) {
    final Product productV4 = new Product();
    productV4.setProductCode(codeProduct);
    productV4.setOfferCode(codeOffre);
    productV4.setIssuingCompanyCode(oc.getCode());
    productV4.setIssuingCompanyName(oc.getLibelle());
    return productV4;
  }
}
