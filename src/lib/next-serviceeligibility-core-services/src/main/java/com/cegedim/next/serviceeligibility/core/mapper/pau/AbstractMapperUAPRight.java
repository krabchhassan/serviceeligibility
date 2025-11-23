package com.cegedim.next.serviceeligibility.core.mapper.pau;

import static java.util.stream.Collectors.groupingBy;

import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.TypePeriode;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.services.pojo.ProductsForRight;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RestErrorConstants;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

public abstract class AbstractMapperUAPRight {

  @Autowired OcService ocService;

  public List<Right> manageRightDomains(
      final BeneficiaireContractTP benef,
      final UniqueAccessPointRequest requete,
      final boolean isTpOffline,
      final Oc oc,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      boolean foundContractsWithForce) {
    final String startDate = DateUtils.formatDate(requete.getStartDate());
    final String endDate = DateUtils.formatDate(requete.getEndDate());

    // récupération des périodes de droits avec son code domaine
    final List<ProductsForRight> productsForRightList =
        this.fillProductsForRightV4(benef.getDomaineDroits(), oc, isTpOffline);

    final TreeMap<String, List<ProductsForRight>> gtProductsForRightV4Map =
        productsForRightList.stream()
            .collect(
                groupingBy(ProductsForRight::getCodeGarantie, TreeMap::new, Collectors.toList()));

    return this.manageRightDomains(
        gtProductsForRightV4Map,
        startDate,
        endDate,
        requete.getDomains(),
        isTpOffline,
        dateRestitution,
        isExclusiviteCarteDematerialise,
        foundContractsWithForce);
  }

  protected abstract List<Right> manageRightDomains(
      TreeMap<String, List<ProductsForRight>> gtProductsForRightV4Map,
      String startDate,
      String endDate,
      List<String> domains,
      boolean isTpOffline,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      boolean foundContractsWithForce);

  protected List<ProductsForRight> fillProductsForRightV4(
      final List<DomaineDroitContractTP> domaineDroitContracts, final Oc oc, boolean isTpOffline) {
    final List<ProductsForRight> productsForRightsOffline = new ArrayList<>();
    final List<ProductsForRight> productsForRightsOnline = new ArrayList<>();
    domaineDroitContracts.forEach(
        domaineDroitContract ->
            domaineDroitContract
                .getGaranties()
                .forEach(
                    garantie ->
                        garantie
                            .getProduits()
                            .forEach(
                                produit ->
                                    produit
                                        .getReferencesCouverture()
                                        .forEach(
                                            referenceCouverture ->
                                                referenceCouverture
                                                    .getNaturesPrestation()
                                                    .forEach(
                                                        naturePrestation ->
                                                            naturePrestation
                                                                .getPeriodesDroit()
                                                                .forEach(
                                                                    periodeDroitContract -> {
                                                                      final ProductsForRight
                                                                          productsForRight =
                                                                              new ProductsForRight();
                                                                      productsForRight
                                                                          .setCodeGarantie(
                                                                              garantie
                                                                                  .getCodeGarantie());
                                                                      productsForRight
                                                                          .setCodeAssureurGarantie(
                                                                              garantie
                                                                                  .getCodeAssureurGarantie());
                                                                      productsForRight
                                                                          .setDateAdhesionCouverture(
                                                                              garantie
                                                                                  .getDateAdhesionCouverture());
                                                                      productsForRight
                                                                          .setCodeCarence(
                                                                              garantie
                                                                                  .getCodeCarence());
                                                                      productsForRight
                                                                          .setCodeAssureurOrigine(
                                                                              garantie
                                                                                  .getCodeAssureurOrigine());
                                                                      productsForRight
                                                                          .setCodeOrigine(
                                                                              garantie
                                                                                  .getCodeOrigine());
                                                                      productsForRight
                                                                          .setCodeProduit(
                                                                              produit
                                                                                  .getCodeProduit());
                                                                      productsForRight
                                                                          .setCodeDomaine(
                                                                              domaineDroitContract
                                                                                  .getCode());
                                                                      productsForRight.setCodeOffre(
                                                                          produit.getCodeOffre());
                                                                      productsForRight
                                                                          .setCodeNaturePrestation(
                                                                              naturePrestation
                                                                                  .getNaturePrestation());
                                                                      productsForRight
                                                                          .setConventionnementContrats(
                                                                              naturePrestation
                                                                                  .getConventionnements());
                                                                      productsForRight
                                                                          .setPrioriteDroitContrats(
                                                                              naturePrestation
                                                                                  .getPrioritesDroit());
                                                                      productsForRight
                                                                          .setPeriodeDroitContractTP(
                                                                              periodeDroitContract);
                                                                      if (StringUtils.isNotBlank(
                                                                          produit.getCodeOc())) {
                                                                        productsForRight.setOc(
                                                                            this.getOcByCode(
                                                                                produit
                                                                                    .getCodeOc()));
                                                                      } else {
                                                                        productsForRight.setOc(oc);
                                                                      }
                                                                      if (TypePeriode.ONLINE.equals(
                                                                          periodeDroitContract
                                                                              .getTypePeriode())) {
                                                                        productsForRightsOnline.add(
                                                                            productsForRight);
                                                                      } else {
                                                                        productsForRightsOffline
                                                                            .add(productsForRight);
                                                                      }
                                                                    }))))));
    if (!isTpOffline && !productsForRightsOnline.isEmpty()) {
      return productsForRightsOnline;
    }

    return productsForRightsOffline;
  }

  protected Oc getOc(final String idDeclarant) {
    final Oc oc;
    try {
      oc = this.ocService.getOC(idDeclarant);
    } catch (final Exception e) {
      throw new RequestValidationException(
          "Erreur lors de l’appel à un service externe - Impossible de déterminer l’Organisation liée à l’Organisme Complémentaire identifié par le numéro d’AMC "
              + idDeclarant,
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CALL_OC_OR_PW);
    }
    if (oc == null) {
      throw new RequestValidationException(
          "Impossible de déterminer l’Organisation liée à l’Organisme Complémentaire identifié par le numéro d’AMC "
              + idDeclarant,
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CALL_OC_OR_PW);
    }
    return oc;
  }

  protected Oc getOcByCode(final String codeOc) {
    final Oc oc;
    try {
      oc = this.ocService.getOCByCode(codeOc);
    } catch (final Exception e) {
      throw new RequestValidationException(
          "Erreur lors de l’appel à un service externe - Impossible de déterminer l’Organisation liée à l’Organisme Complémentaire identifié par le numéro d’OC "
              + codeOc,
          HttpStatus.BAD_REQUEST,
          RestErrorConstants.ERROR_CALL_OC_OR_PW);
    }
    if (oc == null) {
      throw new RequestValidationException(
          "Impossible de déterminer l’Organisation liée à l’Organisme Complémentaire identifié par le numéro d’OC "
              + codeOc,
          HttpStatus.NOT_FOUND,
          RestErrorConstants.ERROR_CALL_OC_OR_PW);
    }
    return oc;
  }

  protected Period controlDateForce(
      final PeriodeDroit period, final boolean isTpOffline, String dateRestitution) {
    final Period periode = new Period();
    final LocalDate periodeDebut;
    LocalDate periodeFin;
    LocalDate periodeFermetureFin = null;

    try {
      periodeDebut = DateUtils.stringToDate(period.getPeriodeDebut());
    } catch (final DateTimeParseException e) {
      return null;
    }

    if (period.getPeriodeFermetureFin() != null) {
      periodeFermetureFin = DateUtils.stringToDate(period.getPeriodeFermetureFin());
    }
    periodeFin = DateUtils.stringToDate(period.getPeriodeFin());
    periode.setStart(DateUtils.formatDate(periodeDebut));
    if (isTpOffline) {
      LocalDate dateFinOffline =
          Util.getDateFinOffline(
              periodeFin, periodeFermetureFin, DateUtils.stringToDate(dateRestitution), false);
      periode.setEnd(DateUtils.formatDate(dateFinOffline));
    } else {
      periode.setEnd(DateUtils.formatDate(periodeFin));
    }
    if (periode.getEnd() != null) {
      return periode.getStart().compareTo(periode.getEnd()) > 0 ? null : periode;
    } else {
      return periode;
    }
  }

  protected Period controlDate(
      final String reqStartDate,
      final String reqEndDate,
      final PeriodeDroit period,
      final boolean isTpOffline,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise) {
    final Period periode = new Period();
    final LocalDate startDate;
    final LocalDate endDate;
    final LocalDate perdiodeDebut;
    LocalDate periodeFin;
    LocalDate periodeFermetureFin = null;

    try {
      startDate = DateUtils.stringToDate(reqStartDate);
      endDate = DateUtils.stringToDate(reqEndDate);
      perdiodeDebut = DateUtils.stringToDate(period.getPeriodeDebut());
    } catch (final DateTimeParseException e) {
      return null;
    }

    try {
      periodeFin = DateUtils.stringToDate(period.getPeriodeFin());
    } catch (final DateTimeParseException ignored) {
      periodeFin = endDate;
    }

    try {
      if (period.getPeriodeFermetureFin() != null) {
        periodeFermetureFin = DateUtils.stringToDate(period.getPeriodeFermetureFin());
      }
    } catch (final DateTimeParseException ignored) {
      periodeFermetureFin = endDate;
    }

    periode.setStart(DateUtils.formatDate(DateUtils.getMaxDate(startDate, perdiodeDebut)));
    if (isTpOffline) {
      LocalDate dateFinOffline =
          Util.getDateFinOffline(
              periodeFin,
              periodeFermetureFin,
              DateUtils.stringToDate(dateRestitution),
              isExclusiviteCarteDematerialise);
      periode.setEnd(DateUtils.formatDate(DateUtils.getMinDate(endDate, dateFinOffline)));
    } else {
      periode.setEnd(DateUtils.formatDate(DateUtils.getMinDate(endDate, periodeFin)));
    }
    if (periode.getEnd() != null) {
      return periode.getStart().compareTo(periode.getEnd()) > 0 ? null : periode;
    } else {
      return periode;
    }
  }

  protected Period controlDate(
      final String reqStartDate,
      final String reqEndDate,
      final PeriodeDroitContractTP period,
      final boolean isTpOffline,
      String dateRestitution,
      boolean isExclusiviteCarteDematerialise,
      boolean foundContractsWithForce) {
    final PeriodeDroit periode = new PeriodeDroit();
    periode.setPeriodeDebut(period.getPeriodeDebut());
    periode.setPeriodeFin(period.getPeriodeFin());
    if (isTpOffline) {
      periode.setPeriodeFermetureFin(period.getPeriodeFinFermeture());
    }
    if (foundContractsWithForce) {
      return this.controlDateForce(periode, isTpOffline, dateRestitution);
    }
    return this.controlDate(
        reqStartDate,
        reqEndDate,
        periode,
        isTpOffline,
        dateRestitution,
        isExclusiviteCarteDematerialise);
  }

  protected Product getProduct(
      final String codeProduct,
      final String codeOffre,
      final Oc oc,
      final String startDate,
      final String endDate) {
    final Product productV4 = this.initializeProductV4(codeProduct, codeOffre, oc);
    final Period periodProduit = new Period();
    periodProduit.setStart(startDate);
    periodProduit.setEnd(endDate);
    productV4.setPeriod(periodProduit);

    return productV4;
  }

  protected abstract Product initializeProductV4(String codeProduct, String codeOffre, Oc oc);

  protected void setDomains(ProductsForRight productsForRight, BenefitType benefitType) {
    List<Domain> domainsList =
        Objects.requireNonNullElse(benefitType.getDomains(), new ArrayList<>());
    if (productsForRight.getPeriodeDroitContractTP() != null
        && CollectionUtils.isNotEmpty(productsForRight.getConventionnementContrats())) {
      String convention =
          productsForRight.getConventionnementContrats().stream()
              .sorted()
              .map(conv -> conv.getTypeConventionnement().getCode())
              .collect(Collectors.joining("/"));
      domainsList.add(new Domain(productsForRight.getCodeDomaine(), convention));
    }

    benefitType.setDomains(domainsList);
  }

  protected PeriodeDroitContractTP fillRightAndGetPeriodeDroit(
      ProductsForRight productsForRight, Right right) {
    PeriodeDroitContractTP periodeDroitContractTP = productsForRight.getPeriodeDroitContractTP();
    if (CollectionUtils.isNotEmpty(productsForRight.getPrioriteDroitContrats())) {
      String codePriorite = null;
      for (PrioriteDroitContrat prioriteDroit : productsForRight.getPrioriteDroitContrats()) {
        if (codePriorite == null) {
          codePriorite = prioriteDroit.getCode();
        } else {
          int compare = StringUtils.compare(codePriorite, prioriteDroit.getCode());
          if (compare > 0) {
            codePriorite = prioriteDroit.getCode();
          }
        }
      }
      right.setPrioritizationOrder(codePriorite);
    }
    right.setInsurerCode(productsForRight.getCodeAssureurGarantie());
    right.setCode(productsForRight.getCodeGarantie());
    right.setGuaranteeAgeDate(DateUtils.formatDate(productsForRight.getDateAdhesionCouverture()));
    right.setWaitingCode(productsForRight.getCodeCarence());
    right.setOriginInsurerCode(productsForRight.getCodeAssureurOrigine());
    right.setOriginCode(productsForRight.getCodeOrigine());

    return periodeDroitContractTP;
  }

  protected static void fillProductPeriod(Period periodProduct, Set<BenefitType> benefitTypeList) {
    if (CollectionUtils.isNotEmpty(benefitTypeList)) {
      periodProduct.setStart(
          benefitTypeList.stream()
              .map(benefitType -> benefitType.getPeriod().getStart())
              .filter(Objects::nonNull)
              .min(String::compareTo)
              .orElse(periodProduct.getStart()));
      List<String> endDates =
          benefitTypeList.stream()
              .map(benefitType -> benefitType.getPeriod().getEnd())
              .collect(Collectors.toList());
      if (endDates.contains(null)) {
        periodProduct.setEnd(null);
      } else {
        periodProduct.setEnd(
            endDates.stream()
                .filter(Objects::nonNull)
                .max(String::compareTo)
                .orElse(periodProduct.getEnd()));
      }
    }
  }
}
