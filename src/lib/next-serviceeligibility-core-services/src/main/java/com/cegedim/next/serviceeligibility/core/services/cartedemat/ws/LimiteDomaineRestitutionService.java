package com.cegedim.next.serviceeligibility.core.services.cartedemat.ws;

import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.UniteDomainesDroitsEnum;
import com.cegedim.next.serviceeligibility.core.model.domain.ValiditeDomainesDroits;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LimiteDomaineRestitutionService {

  @Autowired private DeclarantService declarantService;

  public void limiteDomainListeCarteByParam(List<CarteDemat> cartes, String dateRef) {
    for (CarteDemat carte : cartes) {
      this.limiteDomainByParam(carte, dateRef);
    }
  }

  private void limiteDomainByParam(CarteDemat carte, String dateRef) {
    Pilotage pilotage = getCarteDematPilotage(carte);
    if (pilotage == null) {
      return;
    }
    List<ValiditeDomainesDroits> validites =
        pilotage.getCaracteristique().getValiditesDomainesDroits();
    for (BenefCarteDemat benef : carte.getBeneficiaires()) {
      if (CollectionUtils.isNotEmpty(validites)) {
        limitBenefDomain(dateRef, benef, validites);
      }
    }
  }

  private void limitBenefDomain(
      String dateRef, BenefCarteDemat benef, List<ValiditeDomainesDroits> validites) {
    if (benef.getDomainesRegroup() != null) {
      for (DomaineCarte domaineCarte : benef.getDomainesRegroup()) {
        ValiditeDomainesDroits validite = this.getValidite(validites, domaineCarte.getCode());
        if (validite != null) {
          domaineCarte.setPeriodeFin(
              computePeriodeFinDomain(validite, domaineCarte.getPeriodeFin(), dateRef));
        }
      }
    } else {
      for (DomaineDroit domaineDroit : benef.getDomainesCouverture()) {
        ValiditeDomainesDroits validite = this.getValidite(validites, domaineDroit.getCode());
        if (validite != null) {
          domaineDroit
              .getPeriodeDroit()
              .setPeriodeFin(
                  computePeriodeFinDomain(
                      validite, domaineDroit.getPeriodeDroit().getPeriodeFin(), dateRef));
        }
      }
    }
  }

  private Pilotage getCarteDematPilotage(CarteDemat carte) {
    // Recuperation parametrage
    Declarant declarant = this.declarantService.findById(carte.getIdDeclarant());
    if (declarant == null || CollectionUtils.isEmpty(declarant.getPilotages())) {
      return null;
    }
    Pilotage pilotage =
        declarant.getPilotages().stream()
            .filter(pil -> Constants.CARTE_DEMATERIALISEE.equals(pil.getCodeService()))
            .findFirst()
            .orElse(null);
    if (pilotage == null || pilotage.getCaracteristique() == null) {
      return null;
    }
    return pilotage;
  }

  private ValiditeDomainesDroits getValidite(
      final List<ValiditeDomainesDroits> list, final String code) {
    for (final ValiditeDomainesDroits v : list) {
      if (code.equals(v.getCodeDomaine())) {
        return v;
      }
    }
    return null;
  }

  private String computePeriodeFinDomain(
      ValiditeDomainesDroits validite, String dateFin, String dateDemande) {
    List<LocalDate> dateList = new ArrayList<>();
    if (StringUtils.isNotBlank(dateFin)) {
      dateList.add(DateUtils.parse(dateFin, DateUtils.SLASHED_FORMATTER));
    }
    LocalDate localDateRef = DateUtils.parse(dateDemande, DateUtils.FORMATTER);
    LocalDate dateRefPlus;

    if (UniteDomainesDroitsEnum.Mois.equals(validite.getUnite())) {
      dateRefPlus = localDateRef.plusMonths(validite.getDuree());
      if (validite.isPositionnerFinDeMois()) {
        dateRefPlus = YearMonth.from(dateRefPlus).atEndOfMonth();
      }
    } else {
      dateRefPlus = localDateRef.plusDays(validite.getDuree());
    }
    dateList.add(dateRefPlus);

    final LocalDate dateFinNew =
        dateList.stream().min(LocalDate::compareTo).orElse(LocalDate.now(ZoneOffset.UTC));
    return dateFinNew.format(DateUtils.SLASHED_FORMATTER);
  }
}
