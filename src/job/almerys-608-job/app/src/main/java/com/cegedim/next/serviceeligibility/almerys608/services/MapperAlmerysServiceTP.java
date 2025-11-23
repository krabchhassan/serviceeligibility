package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.ServiceTP;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.CodeActivation;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.entity.HistoriqueExecution608;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarationService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MapperAlmerysServiceTP {
  private final DeclarationService declarationService;

  public void mapServiceTP(
      TmpObject2 tmpObject2,
      HistoriqueExecution608 lastPilotageHisto,
      Map<String, BulkObject> serviceTPs) {

    if (Constants.QUALITE_A.equals(tmpObject2.getBeneficiaire().getAffiliation().getQualite())
        || (lastPilotageHisto == null
            || !tmpObject2.getEffetDebut().before(lastPilotageHisto.getDateExecution()))) {
      String serviceTpKey = tmpObject2.getBeneficiaire().getNumeroPersonne();
      ServiceTP serviceTP = (ServiceTP) serviceTPs.get(serviceTpKey);
      String minDate = getMinDate(tmpObject2);

      PeriodeSuspensionServiceTP periodeSuspensionServiceTP =
          getPeriodeSuspensionServiceTP(tmpObject2, minDate);

      if (serviceTP == null) {
        initializeServiceTP(tmpObject2, serviceTPs, serviceTpKey, periodeSuspensionServiceTP);
      } else {
        updateServiceTP(tmpObject2, serviceTP, periodeSuspensionServiceTP);
      }
    }
  }

  private static void updateServiceTP(
      TmpObject2 tmpObject2,
      ServiceTP serviceTP,
      PeriodeSuspensionServiceTP periodeSuspensionServiceTP) {
    if (Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration())) {
      serviceTP.setDateDebutValidite(
          DateUtils.getMinDate(
              serviceTP.getDateDebutValidite(),
              tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut()));
      if (serviceTP.getDateFinValidite() == null) {
        String nouvelleDateFin =
            DateUtils.getMinDate(
                tmpObject2.getContrat().getDateResiliation(),
                tmpObject2.getBeneficiaire().getDateRadiation());
        if (nouvelleDateFin == null) {
          serviceTP.setDateFinValidite(null);
        } else {
          serviceTP.setDateFinValidite(
              DateUtils.getMinDate(serviceTP.getDateFinValidite(), nouvelleDateFin));
        }
      }
    } else {
      serviceTP.setDateDebutValidite(
          DateUtils.getMinDate(
              serviceTP.getDateDebutValidite(),
              Objects.requireNonNullElse(
                  tmpObject2.getDomaineDroit().getDateAdhesionCouverture(),
                  tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut())));
      serviceTP.setDateFinValidite(
          DateUtils.getMaxDate(
              serviceTP.getDateFinValidite(),
              tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeFin()));
    }
    serviceTP.setDateDebutSuspension(
        DateUtils.getMaxDate(
            serviceTP.getDateDebutSuspension(), periodeSuspensionServiceTP.debutSuspension()));
    serviceTP.setDateFinSuspension(
        DateUtils.getMinDate(
            serviceTP.getDateFinSuspension(), periodeSuspensionServiceTP.finSuspension()));
  }

  private String getMinDate(TmpObject2 tmpObject2) {
    if (tmpObject2.isFlag()) {
      return Objects.requireNonNullElse(
          declarationService.getMinDateFromDeclarations(tmpObject2.getIdDeclarations()),
          tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut());
    }
    return tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut();
  }

  private static void initializeServiceTP(
      TmpObject2 tmpObject2,
      Map<String, BulkObject> serviceTPs,
      String serviceTpKey,
      PeriodeSuspensionServiceTP periodeSuspensionServiceTP) {
    ServiceTP serviceTP;
    serviceTP = new ServiceTP();

    serviceTP.setNumeroContrat(UtilService.mapRefNumContrat(tmpObject2));
    serviceTP.setRefInterneOS(serviceTpKey);
    if (Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration())) {
      serviceTP.setDateDebutValidite(
          tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut());
      serviceTP.setDateFinValidite(
          DateUtils.getMinDate(
              tmpObject2.getContrat().getDateResiliation(),
              tmpObject2.getBeneficiaire().getDateRadiation()));
    } else {
      serviceTP.setDateDebutValidite(
          Objects.requireNonNullElse(
              tmpObject2.getDomaineDroit().getDateAdhesionCouverture(),
              tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeDebut()));
      serviceTP.setDateFinValidite(tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeFin());
    }

    serviceTP.setDateDebutSuspension(periodeSuspensionServiceTP.debutSuspension());
    serviceTP.setDateFinSuspension(periodeSuspensionServiceTP.finSuspension());

    if (Constants.ORIGINE_DECLARATIONEVT.equals(tmpObject2.getOrigineDeclaration())) {
      serviceTP.setActivationDesactivation(
          tmpObject2.getContrat().getDateResiliation() == null
                  && tmpObject2.getBeneficiaire().getDateRadiation() == null
              ? CodeActivation.AC.value()
              : CodeActivation.DE.value());
    } else {
      serviceTP.setActivationDesactivation(
          "O".equals(tmpObject2.getDomaineDroit().getPeriodeDroit().getModeObtention())
                  || "M".equals(tmpObject2.getDomaineDroit().getPeriodeDroit().getModeObtention())
              ? CodeActivation.AC.value()
              : CodeActivation.DE.value());
    }
    serviceTP.setEnvoi(tmpObject2.getContrat().getDestinataire());
    serviceTPs.put(serviceTpKey, serviceTP);
  }

  private record PeriodeSuspensionServiceTP(String debutSuspension, String finSuspension) {}

  private static PeriodeSuspensionServiceTP getPeriodeSuspensionServiceTP(
      TmpObject2 tmpObject2, String minDate) {
    String debutSuspension;
    String finSuspension;
    if (tmpObject2.getDomaineDroit().getPeriodeSuspension() != null) {
      debutSuspension = tmpObject2.getDomaineDroit().getPeriodeSuspension().getDebut();
      finSuspension = tmpObject2.getDomaineDroit().getPeriodeSuspension().getFin();
    } else {
      debutSuspension =
          Boolean.TRUE.equals(tmpObject2.getDomaineDroit().getIsSuspension())
              ? DateUtils.datePlusOneDay(
                  tmpObject2.getDomaineDroit().getPeriodeDroit().getPeriodeFin(),
                  DateUtils.SLASHED_FORMATTER)
              : null;
      finSuspension =
          Boolean.FALSE.equals(tmpObject2.getDomaineDroit().getIsSuspension())
                  && tmpObject2.isFlag()
              ? DateUtils.dateMinusOneDay(minDate)
              : null;
    }
    return new PeriodeSuspensionServiceTP(debutSuspension, finSuspension);
  }
}
