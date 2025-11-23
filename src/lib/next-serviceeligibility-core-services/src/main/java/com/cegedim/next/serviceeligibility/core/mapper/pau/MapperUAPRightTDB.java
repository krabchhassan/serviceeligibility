package com.cegedim.next.serviceeligibility.core.mapper.pau;

import static java.util.stream.Collectors.groupingBy;

import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.services.IPwService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ProductsForRight;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.BenefitType;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Period;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Product;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Right;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service
@Slf4j
public class MapperUAPRightTDB extends AbstractMapperUAPRight {

  final IPwService pwService;

  public MapperUAPRightTDB(IPwService pwService) {
    this.pwService = pwService;
  }

  private static final Logger LOGGER = LoggerFactory.getLogger(MapperUAPRightTDB.class);

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

    for (Map.Entry<String, List<ProductsForRight>> entryProductForRightV4 :
        gtProductsForRightV4Map.entrySet()) {
      Right right = new Right();
      List<Product> productV4List = new ArrayList<>();

      final TreeMap<Pair<String, String>, List<ProductsForRight>> product =
          entryProductForRightV4.getValue().stream()
              .collect(
                  groupingBy(
                      productsForRight ->
                          new ImmutablePair<>(
                              productsForRight.getCodeGarantie(), productsForRight.getCodeOffre()),
                      TreeMap::new,
                      Collectors.toList()));

      for (final Map.Entry<Pair<String, String>, List<ProductsForRight>> entryProduct :
          product.entrySet()) {
        this.manageRightDomainForTDB(
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

  @Override
  protected Product initializeProductV4(String codeProduct, String codeOffre, Oc oc) {
    return initializeProductV4(codeProduct, codeOffre, oc, "");
  }

  private void manageRightDomainForTDB(
      boolean isTpOffline,
      String startDate,
      String endDate,
      Right right,
      List<Product> productV4List,
      Map.Entry<Pair<String, String>, List<ProductsForRight>> mapEntry,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      boolean foundContractsWithForce) {
    // regroupement par offre/garantie, les natures de prestations sont récupérés
    // ensuite, c'est pour ça que l'on prend le 1er.
    final ProductsForRight productsForRight = mapEntry.getValue().get(0);
    fillRightAndGetPeriodeDroit(productsForRight, right);
    Period period;
    boolean periodOK = false;
    for (ProductsForRight productsForRightV41 : mapEntry.getValue()) {
      period =
          controlDate(
              startDate,
              endDate,
              productsForRightV41.getPeriodeDroitContractTP(),
              isTpOffline,
              dateRestitution,
              isExclusiviteCarteDematerialise,
              foundContractsWithForce);
      if (period != null) {
        periodOK = true;
        break;
      }
    }
    if (periodOK) {
      productV4List.add(
          getProductForTDB(
              productsForRight.getOc(),
              isTpOffline,
              startDate,
              endDate,
              mapEntry.getValue(),
              dateRestitution,
              isExclusiviteCarteDematerialise,
              foundContractsWithForce));
    }
  }

  private Product getProductForTDB(
      Oc oc,
      boolean isTpOffline,
      String startDate,
      String endDate,
      List<ProductsForRight> productsForRights,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      boolean foundContractsWithForce) {
    // même garantie
    Product productV4 =
        initializeProductV4(
            productsForRights.get(0).getCodeGarantie(),
            productsForRights.get(0).getCodeOffre(),
            oc,
            productsForRights.get(0).getCodeProduit());
    LinkedHashSet<BenefitType> benefitTypeList = new LinkedHashSet<>();
    Period periodProduct = new Period();
    Map<String, List<Period>> periodsByDomain = new HashMap<>();
    for (ProductsForRight productsForRight : productsForRights) {
      PeriodeDroitContractTP periodeDroitContractTP = productsForRight.getPeriodeDroitContractTP();
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
        Period periodPrestation = new Period();
        periodPrestation.setStart(DateUtils.formatDate(DateUtils.stringToDate(period.getStart())));
        periodPrestation.setEnd(DateUtils.formatDate(DateUtils.stringToDate(period.getEnd())));

        if (!periodsByDomain.containsKey(productsForRight.getCodeDomaine())) {
          periodsByDomain.put(productsForRight.getCodeDomaine(), new ArrayList<>());
        }

        periodsByDomain.get(productsForRight.getCodeDomaine()).add(periodPrestation);
        periodProduct.setStart(
            DateUtils.getMinDate(periodProduct.getStart(), period.getStart().replace("-", "/")));
        if (period.getEnd() != null) {
          periodProduct.setEnd(
              DateUtils.getMaxDate(periodProduct.getEnd(), period.getEnd().replace("-", "/")));
        }
      }
    }

    // call pw to retrieve the prestation nature and the offer code
    Period periodBenefit = new Period();
    periodBenefit.setStart(periodProduct.getStart().replace("/", "-"));
    if (periodProduct.getEnd() != null) {
      periodBenefit.setEnd(periodProduct.getEnd().replace("/", "-"));
    }

    productV4.setOfferCode(
        fillListBenefitTypeAndReturnOfferCode(
            oc.getCode(),
            productsForRights.get(0).getCodeGarantie(),
            periodBenefit,
            benefitTypeList,
            isTpOffline));

    Map<String, List<ProductsForRight>> productsByNature =
        productsForRights.stream()
            .filter(pfr -> CollectionUtils.isNotEmpty(pfr.getConventionnementContrats()))
            .collect(
                groupingBy(
                    pfr ->
                        Objects.requireNonNullElse(
                            pfr.getCodeNaturePrestation(), Constants.NATURE_PRESTATION_VIDE_BOBB)));
    for (BenefitType benefitType : benefitTypeList) {
      String benefitToSearch =
          Objects.requireNonNullElse(
              benefitType.getBenefitType(), Constants.NATURE_PRESTATION_VIDE_BOBB);
      if (productsByNature.containsKey(benefitToSearch))
        productsByNature
            .get(benefitToSearch)
            .forEach(productsForRight -> setDomains(productsForRight, benefitType));
    }

    fillProductPeriod(periodProduct, benefitTypeList);
    productV4.setBenefitsType(benefitTypeList);
    Period periodProduit = new Period();
    periodProduit.setStart(DateUtils.formatDate(DateUtils.stringToDate(periodProduct.getStart())));
    periodProduit.setEnd(DateUtils.formatDate(DateUtils.stringToDate(periodProduct.getEnd())));
    productV4.setPeriod(periodProduit);
    return productV4;
  }

  protected Product initializeProductV4(
      String codeGarantie, String codeOffre, final Oc oc, final String otpProductCode) {
    final Product productV4 = new Product();
    productV4.setProductCode(codeGarantie);
    // pas d'offre en TDB
    productV4.setIssuingCompanyCode(oc.getCode());
    productV4.setIssuingCompanyName(oc.getLibelle());
    productV4.setOtpProductCode(otpProductCode);
    return productV4;
  }

  private String fillListBenefitTypeAndReturnOfferCode(
      String issuerCompany,
      String productCode,
      Period period,
      Set<BenefitType> benefitTypeList,
      boolean isTpOffline) {

    JSONArray offerStructures =
        getOfferStructure(
            issuerCompany,
            period,
            productCode,
            isTpOffline ? ContextConstants.TP_OFFLINE : ContextConstants.TP_ONLINE,
            "V4");
    String offerCode = null;
    if (offerStructures != null) {
      for (int i = 0; i < offerStructures.length(); i++) {
        JSONObject offerStructure = (JSONObject) offerStructures.get(i);

        boolean extractBenefitTypes = containsProductCode(offerStructure, productCode);

        if (extractBenefitTypes) {
          LocalDate offerStart =
              DateUtils.stringToDate(offerStructure.getString(Constants.PW_VALIDITY_DATE));
          LocalDate offerEnd =
              offerStructure.has(Constants.PW_END_VALIDITY_DATE)
                  ? DateUtils.stringToDate(offerStructure.getString(Constants.PW_END_VALIDITY_DATE))
                  : null;

          LocalDate validityDateStart =
              DateUtils.getMaxDate(DateUtils.stringToDate(period.getStart()), offerStart);
          LocalDate validityDateEnd =
              DateUtils.getMinDate(DateUtils.stringToDate(period.getEnd()), offerEnd);
          Period validityPeriod = new Period();
          validityPeriod.setStart(DateUtils.formatDate(validityDateStart));
          validityPeriod.setEnd(DateUtils.formatDate(validityDateEnd));

          JSONArray natures = offerStructure.getJSONArray("natures");

          mapNaturesToBenefitTypeList(natures, validityPeriod, benefitTypeList);
          offerCode = offerStructure.getString(Constants.PW_OFFER_CODE);
        }
      }
    }
    return offerCode;
  }

  private boolean containsProductCode(JSONObject offerStructure, String productCode) {
    JSONArray products = offerStructure.getJSONArray("products");

    if (products != null) {
      for (int i = 0; i < products.length(); i++) {
        if (products.getString(i).equals(productCode)) {
          return true;
        }
      }
    }

    return false;
  }

  private void mapNaturesToBenefitTypeList(
      JSONArray natures, Period period, Set<BenefitType> benefitTypeList) {

    for (int compteur = 0; compteur < natures.length(); compteur++) {
      String codeBenefitType;
      List<String> tags = new ArrayList<>();
      codeBenefitType = natures.getString(compteur);

      if (benefitTypeList.stream().noneMatch(b -> codeBenefitType.equals(b.getBenefitType()))) {
        addBenefitType(period, benefitTypeList, codeBenefitType, tags);
      } else {
        List<BenefitType> sameBenefitType =
            benefitTypeList.stream()
                .filter(b -> codeBenefitType.equals(b.getBenefitType()))
                .toList();
        boolean found = false;
        for (BenefitType benefitType : sameBenefitType) {
          LocalDate endDatePlus1 =
              LocalDate.parse(benefitType.getPeriod().getEnd(), DateUtils.FORMATTER).plusDays(1);
          LocalDate startDate = LocalDate.parse(period.getStart(), DateUtils.FORMATTER);
          // si chevauchement, on change la période
          if (endDatePlus1.equals(startDate)) {
            found = true;
            Period changedPeriod = new Period();
            changedPeriod.setStart(
                DateUtils.getMinDate(
                    benefitType.getPeriod().getStart(), period.getStart(), DateUtils.FORMATTER));
            changedPeriod.setEnd(
                DateUtils.getMaxDate(
                    benefitType.getPeriod().getEnd(), period.getEnd(), DateUtils.FORMATTER));
            benefitType.setPeriod(changedPeriod);
          }
        }
        if (!found) {
          addBenefitType(period, benefitTypeList, codeBenefitType, tags);
        }
      }
    }
  }

  private static void addBenefitType(
      Period period, Set<BenefitType> benefitTypeList, String codeBenefitType, List<String> tags) {
    BenefitType benefitType = new BenefitType();

    benefitType.setBenefitType(codeBenefitType);
    benefitType.setPeriod(period);

    benefitType.setTags(tags);

    benefitTypeList.add(benefitType);
  }

  public JSONArray getOfferStructure(
      String issuerCompany, Period period, String productCode, String context, String version) {
    JSONArray offerStructures = null;
    log.debug(
        "getOfferStructure issuerCompany: {} start {} end {}, product {}",
        issuerCompany,
        period.getStart(),
        period.getEnd(),
        productCode);
    try {
      offerStructures =
          pwService.getOfferStructure(
              issuerCompany,
              null,
              productCode,
              period.getStart(),
              period.getEnd(),
              context,
              version);
    } catch (HttpClientErrorException e) {
      return null;
    } catch (GenericNotFoundException e) {
      LOGGER.debug(
          String.format(
              "Aucun produit dans la base de produits sur l'organisation %s, le produit %s et la période de %s au %s",
              issuerCompany, productCode, period.getStart(), period.getEnd()),
          e);
    } catch (Exception e) {
      throw new RequestValidationException(
          "Erreur technique lors de l'appel à PW",
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CODE_PW_NOT_RESPONDING_EXCEPTION);
    }

    return offerStructures;
  }
}
