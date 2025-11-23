package com.cegedim.next.serviceeligibility.core.mapper;

import com.cegedim.next.serviceeligibility.core.bobb.ProductElementLight;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.bobb.services.ProductElementService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.mapper.trigger.TriggerMapper;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.ParametrageDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.TpOfflineRightsDetails;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.Variable;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.TriggerDataForTesting;
import com.cegedim.next.serviceeligibility.core.services.pojo.DroitsTPExtended;
import com.cegedim.next.serviceeligibility.core.services.pojo.WaitingExtendedOffreProduits;
import com.cegedim.next.serviceeligibility.core.utils.TauxConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class TriggerMapperTest {
  @Autowired private TriggerMapper triggerMapper;

  @Autowired ContractElementService contractElementService;

  @Autowired ProductElementService productElementService;

  @Test
  void mapContratTest()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    TriggeredBeneficiary tb = TriggerDataForTesting.getTriggeredBenef1("UUID");
    ParametrageDroitsCarteTP pdc = TriggerDataForTesting.getParametrageDroitsCarteTP();
    LocalDate dateJour = LocalDate.now();
    String dateDebutDroit = "2024-09-06";
    List<DroitsTPExtended> droitsTPExtendedList = new ArrayList<>();
    Object[] params = new Object[5];
    params[0] = tb;
    params[1] = pdc;
    params[2] = dateJour;
    params[3] = dateDebutDroit;
    params[4] = droitsTPExtendedList;

    Method method;
    method =
        triggerMapper
            .getClass()
            .getDeclaredMethod(
                "mapContrat",
                tb.getClass(),
                pdc.getClass(),
                dateJour.getClass(),
                dateDebutDroit.getClass(),
                List.class);
    method.setAccessible(true);
    Contrat contrat = (Contrat) method.invoke(triggerMapper, params);
    Assertions.assertEquals("005830210", contrat.getNumeroAdherentComplet());
  }

  @Test
  void shouldMapRightToOfferAndProduct() {
    DroitAssure droit = new DroitAssure();
    droit.setCode("code");
    droit.setCodeAssureur("codeAssureur");
    List<CarenceDroit> carenceList = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CARGLACE");
    carenceList.add(carence);
    droit.setCarences(carenceList);
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    Periode periode = new Periode();
    periode.setDebut(currentdate.getYear() + "-01-01");
    droit.setPeriode(periode);
    TriggerDataForTesting.initializeProductElementLight(productElementService);
    ReflectionTestUtils.setField(triggerMapper, "contractElementService", contractElementService);

    List<WaitingExtendedOffreProduits> extendedOffreProduits =
        triggerMapper.mapRightToOfferAndProduct(droit, periode);
    Assertions.assertNotNull(extendedOffreProduits);
  }

  @Test
  void shouldNotMapRightToOfferAndProduct() {
    DroitAssure droit = new DroitAssure();
    droit.setCode("code");
    droit.setCodeAssureur("codeAssureur");
    List<CarenceDroit> carenceList = new ArrayList<>();
    CarenceDroit carence = new CarenceDroit();
    carence.setCode("CARGLACE");
    carenceList.add(carence);
    droit.setCarences(carenceList);
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);
    Periode periode = new Periode();
    periode.setDebut(currentdate.getYear() + "-01-01");
    periode.setFin(currentdate.getYear() + "-12-31");
    droit.setPeriode(periode);
    TriggerDataForTesting.initializeProductElementLight(productElementService);

    ReflectionTestUtils.setField(triggerMapper, "contractElementService", contractElementService);

    List<WaitingExtendedOffreProduits> extendedOffreProduits =
        triggerMapper.mapRightToOfferAndProduct(droit, periode);

    Assertions.assertEquals(1, extendedOffreProduits.size());
  }

  @Test
  void shouldMapRightToMultipleOfferAndProduct() {
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    LocalDate currentdate = LocalDate.now(ZoneOffset.UTC);

    DroitAssure droit = new DroitAssure();
    droit.setCode("code");
    droit.setCodeAssureur("codeAssureur");
    Periode periode = new Periode();
    periode.setDebut(currentdate.getYear() + "-01-01");
    droit.setPeriode(periode);

    DroitAssure droit2 = new DroitAssure();
    droit2.setCode("code2");
    droit2.setCodeAssureur("codeAssureur");
    Periode periode2 = new Periode();
    periode2.setDebut(currentdate.getYear() + "-01-01");
    droit2.setPeriode(periode2);

    ServicePrestationTriggerBenef c = new ServicePrestationTriggerBenef();

    c.setDroitsGaranties(List.of(droit, droit2));
    benef.setNewContract(c);

    List<ProductElementLight> listPel1 = new ArrayList<>();
    ProductElementLight pel1 = new ProductElementLight();
    pel1.setCodeProduct("code");
    pel1.setCodeOffer("offre1");
    pel1.setCodeAmc("amc");
    listPel1.add(pel1);
    List<ProductElementLight> listPel2 = new ArrayList<>();
    ProductElementLight pel2 = new ProductElementLight();
    pel2.setCodeProduct("code2");
    pel2.setCodeOffer("offre1");
    pel2.setCodeAmc("amc");
    listPel2.add(pel2);
    Mockito.when(
            productElementService.getOfferAndProduct(
                droit.getCodeAssureur(),
                droit.getCode(),
                droit.getPeriode().getDebut(),
                droit.getPeriode().getFin()))
        .thenReturn(listPel1);
    Mockito.when(
            productElementService.getOfferAndProduct(
                droit2.getCodeAssureur(),
                droit2.getCode(),
                droit2.getPeriode().getDebut(),
                droit2.getPeriode().getFin()))
        .thenReturn(listPel2);

    ReflectionTestUtils.setField(triggerMapper, "contractElementService", contractElementService);

    List<ExtendedOffreProduits> extendedOffreProduits =
        triggerMapper.getExtendedOffreProduits(benef);
    Assertions.assertNotNull(extendedOffreProduits);
    Assertions.assertEquals(1, extendedOffreProduits.size());
    Assertions.assertEquals(2, extendedOffreProduits.get(0).getProduits().size());
  }

  @Test
  void defaultTauxUnite() {
    DomaineDroit domaineDroit = new DomaineDroit();
    TpOfflineRightsDetails details = new TpOfflineRightsDetails();
    triggerMapper.mapTaux(domaineDroit, details);
    Assertions.assertEquals(TauxConstants.T_100, domaineDroit.getTauxRemboursement());
    Assertions.assertEquals(TauxConstants.U_POURCENTAGE, domaineDroit.getUniteTauxRemboursement());
  }

  @Test
  void setVariableValueNum() {
    checkVariables("50,5", 1, TauxConstants.U_NUMERIQUE);
    checkVariables("50.5", 1, TauxConstants.U_NUMERIQUE);

    checkVariables("50,5", 7, TauxConstants.U_NUMERIQUE);
    checkVariables("50.5", 7, TauxConstants.U_NUMERIQUE);
  }

  @Test
  void setVariableValueForfait() {
    checkVariables("50", 1, TauxConstants.U_FORFAIT);
    checkVariables("PIPO", 1, TauxConstants.U_FORFAIT);

    checkVariables("50", 7, TauxConstants.U_FORFAIT);
    checkVariables("PIPO", 7, TauxConstants.U_FORFAIT);
  }

  @Test
  void setVariableNot1Or7Default() {
    checkVariables("PIPO", 0, TauxConstants.U_POURCENTAGE);
  }

  @Test
  void notSetVariableFormula099() {
    DomaineDroit domaineDroit = new DomaineDroit();
    TpOfflineRightsDetails details = new TpOfflineRightsDetails();
    details.setStsFormulaCode(TauxConstants.F_099);
    triggerMapper.mapTaux(domaineDroit, details);
    Assertions.assertEquals(TauxConstants.T_PRISE_EN_CHARGE, domaineDroit.getTauxRemboursement());
    Assertions.assertEquals(TauxConstants.U_TEXT, domaineDroit.getUniteTauxRemboursement());
  }

  @Test
  void notSetVariableFormulafGl_NC() {
    DomaineDroit domaineDroit = new DomaineDroit();
    TpOfflineRightsDetails details = new TpOfflineRightsDetails();
    details.setFormulaCode(TauxConstants.F_FGL_NC);
    triggerMapper.mapTaux(domaineDroit, details);
    Assertions.assertEquals(TauxConstants.T_NON_COUVERT, domaineDroit.getTauxRemboursement());
    Assertions.assertEquals(TauxConstants.U_TEXT, domaineDroit.getUniteTauxRemboursement());
  }

  private void checkVariables(String value, int cases, String unite) {
    DomaineDroit domaineDroit = new DomaineDroit();
    TpOfflineRightsDetails details = new TpOfflineRightsDetails();
    Variable variable = new Variable();
    variable.setStsVariableNumber(cases);
    variable.setValue(value);
    details.setVariables(List.of(variable));

    triggerMapper.mapTaux(domaineDroit, details);
    Assertions.assertEquals(value, domaineDroit.getTauxRemboursement());
    Assertions.assertEquals(unite, domaineDroit.getUniteTauxRemboursement());
  }

  @Test
  void shouldGetSamePeriod() {
    Periode periode1 = new Periode("2022-01-01", "2022-10-31");
    Periode periode2 = new Periode("2023-01-01", null);
    ServicePrestationTriggerBenef servicePrestationTriggerBenef =
        new ServicePrestationTriggerBenef();

    DroitAssure droitAssure1 = new DroitAssure();
    droitAssure1.setPeriode(periode1);

    List<Periode> periodesBobb = triggerMapper.getPeriodesBobb(null, null, null, droitAssure1);

    Assertions.assertEquals(1, periodesBobb.size());
    Assertions.assertEquals(periode1, periodesBobb.get(0));

    droitAssure1 = new DroitAssure();
    droitAssure1.setPeriode(periode2);

    periodesBobb = triggerMapper.getPeriodesBobb(null, null, null, droitAssure1);

    Assertions.assertEquals(1, periodesBobb.size());
    Assertions.assertEquals(periode2, periodesBobb.get(0));

    droitAssure1 = new DroitAssure();
    Periode periode3 = new Periode("2024-01-01", "2024-10-31");
    droitAssure1.setPeriode(periode3);

    periodesBobb = triggerMapper.getPeriodesBobb(null, null, null, droitAssure1);

    Assertions.assertEquals(1, periodesBobb.size());
    Assertions.assertEquals(periode3, periodesBobb.get(0));

    Periode periode4 = new Periode("2022-01-01", "2024-10-31");

    droitAssure1 = new DroitAssure();
    droitAssure1.setPeriode(periode4);

    periodesBobb = triggerMapper.getPeriodesBobb(null, null, null, droitAssure1);

    Assertions.assertEquals(1, periodesBobb.size());
    Assertions.assertEquals(new Periode("2022-01-01", "2024-10-31"), periodesBobb.get(0));
  }
}
