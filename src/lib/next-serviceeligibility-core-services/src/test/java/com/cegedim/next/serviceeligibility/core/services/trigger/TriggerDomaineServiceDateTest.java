package com.cegedim.next.serviceeligibility.core.services.trigger;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Trigger;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggerEmitter;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.pojo.DatesForDomaine;
import com.cegedim.next.serviceeligibility.core.services.pojo.ParametrageTrigger;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerDomaineServiceDateTest {

  final LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
  final String currentYear = String.valueOf(currentdate.getYear());

  @Test
  void getDatesForDomaineOuvertureEvent() {
    String dateSouscription = "2020-06-05";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(null, droitHTP, parametrageTrigger, false);

    Assertions.assertEquals(currentYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(currentYear + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertNull(datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineFermetureEvent() {
    String dateSouscription = "2020-06-05";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    String dateFermeture = currentYear + "-03-01";
    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(
            dateFermeture, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(currentYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(currentYear + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFermeture, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineFermetureFromTheBeginningEvent() {
    String dateSouscription = "2020-06-05";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    String dateFermeture = currentYear + "-03-01";
    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(
            dateFermeture, droitHTP, parametrageTrigger, true);
    Assertions.assertEquals(currentYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(dateFermeture, datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFermeture, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineFermetureAfterFinEvent() {
    String dateSouscription = "2020-06-05";
    final String nextYear = String.valueOf(currentdate.getYear() + 1);
    String dateFermeture = nextYear + "-03-01";
    String dateFin = currentYear + "-06-01";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    periodeDroit.setFin(dateFin);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(
            dateFermeture, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(currentYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(dateFin, datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFin, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineFermetureBeforeFinEvent() {
    String dateSouscription = "2020-06-05";
    String dateFermeture = currentYear + "-06-01";
    String dateFin = currentYear + "-07-01";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    periodeDroit.setFin(dateFin);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(
            dateFermeture, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(currentYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(currentYear + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFermeture, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineFermetureAfterEndFromTheBeginningEvent() {
    String dateSouscription = "2020-06-05";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    String dateFermeture = "2099-03-01";
    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(
            dateFermeture, droitHTP, parametrageTrigger, true);
    Assertions.assertEquals(currentYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(currentYear + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFermeture, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineEventReprise() {
    String dateSouscription = "2020-06-05";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(true);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    final String nextYear = String.valueOf(currentdate.getYear() + 1);
    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(null, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(nextYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(nextYear + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertNull(datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineEventRepriseWarning() {
    String dateSouscription = "2035-06-05";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(true);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    final String nextYear = String.valueOf(currentdate.getYear() + 1);
    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(null, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(dateSouscription, datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(nextYear + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertNull(datesForDomaine.getDateFinOnline());
    Assertions.assertTrue(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineEventNotExistFinOnOldPeriodeAssure() {
    String dateSouscription = "2020-06-05";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParam(
            ModeDeclenchementCarteTP.Automatique,
            DateRenouvellementCarteTP.DebutEcheance,
            null,
            "01/01",
            31);
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(false);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Event);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    periodeDroit.setDebut(dateSouscription);
    String dateFinAssure = "2025-08-05";
    periodeDroit.setFin(dateFinAssure);

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(null, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(currentYear + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(dateFinAssure, datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFinAssure, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineRenewalRdo() {
    String dateSouscription = "2020-06-05";
    String anneeRdo = "2022";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParamRdo(
            ModeDeclenchementCarteTP.Manuel,
            DateRenouvellementCarteTP.DebutEcheance,
            anneeRdo + "-01-01");
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(true);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    periodeDroit.setDebut(dateSouscription);
    String dateFinAssure = currentYear + "-08-05";
    periodeDroit.setFin(dateFinAssure);

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(null, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(anneeRdo + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(anneeRdo + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFinAssure, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineRenewalRdoFermeAvant() {
    String dateSouscription = "2020-06-05";
    String anneeRdo = "2022";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParamRdo(
            ModeDeclenchementCarteTP.Manuel,
            DateRenouvellementCarteTP.DebutEcheance,
            anneeRdo + "-01-01");
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(true);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    periodeDroit.setDebut(dateSouscription);

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());
    String dateFermeture = anneeRdo + "-03-01";

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(
            dateFermeture, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(anneeRdo + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(anneeRdo + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFermeture, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineRenewalRdoFermeApres() {
    String dateSouscription = "2020-06-05";
    String anneeRdo = "2022";
    String dateFermeture = "2023-03-01";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParamRdo(
            ModeDeclenchementCarteTP.Manuel,
            DateRenouvellementCarteTP.DebutEcheance,
            anneeRdo + "-01-01");
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(true);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    periodeDroit.setDebut(dateSouscription);

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(
            dateFermeture, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(anneeRdo + "-01-01", datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(anneeRdo + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertEquals(dateFermeture, datesForDomaine.getDateFinOnline());
    Assertions.assertFalse(datesForDomaine.isWarning());
  }

  @Test
  void getDatesForDomaineRenewalWarning() {
    String dateSouscription = "2023-06-05";
    String anneeRdo = "2022";
    ParametrageCarteTP parametrageCarteTp =
        TriggerDataForTesting.getParamRdo(
            ModeDeclenchementCarteTP.Manuel,
            DateRenouvellementCarteTP.DebutEcheance,
            anneeRdo + "-01-01");
    DroitAssure droitHTP = new DroitAssure();
    Periode periodeDroit = new Periode();
    periodeDroit.setDebut(dateSouscription);
    droitHTP.setPeriode(periodeDroit);
    Trigger trigger = new Trigger();
    trigger.setRdo(true);
    trigger.setEventReprise(false);
    trigger.setOrigine(TriggerEmitter.Renewal);
    trigger.setDateEffet(currentYear + "-06-05");

    TriggeredBeneficiary triggeredBeneficiary = new TriggeredBeneficiary();
    ServicePrestationTriggerBenef newContract = new ServicePrestationTriggerBenef();

    periodeDroit.setDebut(dateSouscription);

    triggeredBeneficiary.setNewContract(newContract);

    ParametrageTrigger parametrageTrigger = new ParametrageTrigger();
    parametrageTrigger.setTriggeredBeneficiary(triggeredBeneficiary);
    parametrageTrigger.setParametrageCarteTP(parametrageCarteTp);
    parametrageTrigger.setOrigine(trigger.getOrigine());
    parametrageTrigger.setDateEffet(trigger.getDateEffet());
    parametrageTrigger.setRdo(trigger.isRdo());
    parametrageTrigger.setEventReprise(trigger.isEventReprise());

    DatesForDomaine datesForDomaine =
        TriggerDomaineServiceImpl.getDatesForDomaine(null, droitHTP, parametrageTrigger, false);
    Assertions.assertEquals(dateSouscription, datesForDomaine.getRequeteStartDate());
    Assertions.assertEquals(anneeRdo + "-12-31", datesForDomaine.getRequeteEndDate());
    Assertions.assertNull(datesForDomaine.getDateFinOnline());
    Assertions.assertTrue(datesForDomaine.isWarning());
  }
}
