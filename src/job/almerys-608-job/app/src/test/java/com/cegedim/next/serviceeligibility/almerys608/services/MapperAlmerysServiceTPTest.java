package com.cegedim.next.serviceeligibility.almerys608.services;

import com.cegedim.next.serviceeligibility.almerys608.model.ServiceTP;
import com.cegedim.next.serviceeligibility.batch.almerys.normev3.CodeActivation;
import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.AdresseAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.entity.almv3.TmpObject2;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import config.Test608Configuration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = Test608Configuration.class)
public class MapperAlmerysServiceTPTest {

  @Autowired MapperAlmerysServiceTP mapperAlmerysServiceTP;

  @Test
  void serviceTPTestTdbSuspendu() {
    TmpObject2 tmpObject2 = createTmpObject2(Constants.ORIGINE_DECLARATIONTDB, null, null, true);
    Map<String, BulkObject> serviceTPs = new HashMap<>();
    mapperAlmerysServiceTP.mapServiceTP(tmpObject2, null, serviceTPs);

    Assertions.assertEquals(1, serviceTPs.size());
    ServiceTP serviceTP = (ServiceTP) serviceTPs.get("NumeroPersonne");
    Assertions.assertEquals("2025/01/01", serviceTP.getDateDebutValidite());
    Assertions.assertEquals("2025/12/31", serviceTP.getDateFinValidite());
    Assertions.assertEquals(CodeActivation.DE.value(), serviceTP.getActivationDesactivation());
    Assertions.assertEquals("2026/01/01", serviceTP.getDateDebutSuspension());
    Assertions.assertNull(serviceTP.getDateFinSuspension());
    Assertions.assertEquals("AD", serviceTP.getEnvoi());
  }

  @Test
  void serviceTPTestTdb() {
    TmpObject2 tmpObject2 = createTmpObject2(Constants.ORIGINE_DECLARATIONTDB, null, null, false);
    Map<String, BulkObject> serviceTPs = new HashMap<>();
    mapperAlmerysServiceTP.mapServiceTP(tmpObject2, null, serviceTPs);

    Assertions.assertEquals(1, serviceTPs.size());
    ServiceTP serviceTP = (ServiceTP) serviceTPs.get("NumeroPersonne");
    Assertions.assertEquals("2025/01/01", serviceTP.getDateDebutValidite());
    Assertions.assertEquals("2025/12/31", serviceTP.getDateFinValidite());
    Assertions.assertEquals(CodeActivation.DE.value(), serviceTP.getActivationDesactivation());
    Assertions.assertNull(serviceTP.getDateDebutSuspension());
    Assertions.assertNull(serviceTP.getDateFinSuspension());
  }

  @Test
  void serviceTPTestEvt() {
    TmpObject2 tmpObject2 = createTmpObject2(Constants.ORIGINE_DECLARATIONEVT, null, null, true);
    Map<String, BulkObject> serviceTPs = new HashMap<>();
    mapperAlmerysServiceTP.mapServiceTP(tmpObject2, null, serviceTPs);

    Assertions.assertEquals(1, serviceTPs.size());
    ServiceTP serviceTP = (ServiceTP) serviceTPs.get("NumeroPersonne");
    Assertions.assertEquals("2025/01/01", serviceTP.getDateDebutValidite());
    Assertions.assertNull(serviceTP.getDateFinValidite());
    Assertions.assertEquals(CodeActivation.AC.value(), serviceTP.getActivationDesactivation());
    Assertions.assertEquals("2025/04/01", serviceTP.getDateDebutSuspension());
    Assertions.assertEquals("2025/04/30", serviceTP.getDateFinSuspension());
  }

  @Test
  void serviceTPTestEvt2() {
    TmpObject2 tmpObject2 =
        createTmpObject2(Constants.ORIGINE_DECLARATIONEVT, "2025/01/10", "2025/01/08", false);
    Map<String, BulkObject> serviceTPs = new HashMap<>();
    mapperAlmerysServiceTP.mapServiceTP(tmpObject2, null, serviceTPs);

    Assertions.assertEquals(1, serviceTPs.size());
    ServiceTP serviceTP = (ServiceTP) serviceTPs.get("NumeroPersonne");
    Assertions.assertEquals("2025/01/01", serviceTP.getDateDebutValidite());
    Assertions.assertEquals("2025/01/08", serviceTP.getDateFinValidite());
    Assertions.assertEquals(CodeActivation.DE.value(), serviceTP.getActivationDesactivation());
    Assertions.assertNull(serviceTP.getDateDebutSuspension());
    Assertions.assertNull(serviceTP.getDateFinSuspension());
  }

  @Test
  void serviceTPTestEvtComplete() {
    TmpObject2 tmpObject2 =
        createTmpObject2(Constants.ORIGINE_DECLARATIONEVT, "2025/01/10", "2025/01/08", false);
    Map<String, BulkObject> serviceTPs = new HashMap<>();
    ServiceTP serviceTP = new ServiceTP();
    serviceTP.setDateDebutValidite("2024/01/10");
    serviceTP.setDateFinValidite(null);
    serviceTP.setActivationDesactivation(CodeActivation.DE.value());
    serviceTPs.put("NumeroPersonne", serviceTP);
    mapperAlmerysServiceTP.mapServiceTP(tmpObject2, null, serviceTPs);

    Assertions.assertEquals(1, serviceTPs.size());
    serviceTP = (ServiceTP) serviceTPs.get("NumeroPersonne");
    Assertions.assertEquals("2024/01/10", serviceTP.getDateDebutValidite());
    Assertions.assertEquals("2025/01/08", serviceTP.getDateFinValidite());
    Assertions.assertEquals(CodeActivation.DE.value(), serviceTP.getActivationDesactivation());
  }

  @Test
  void serviceTPTestEvtComplete2() {
    TmpObject2 tmpObject2 = createTmpObject2(Constants.ORIGINE_DECLARATIONEVT, null, null, false);
    Map<String, BulkObject> serviceTPs = new HashMap<>();
    ServiceTP serviceTP = new ServiceTP();
    serviceTP.setDateDebutValidite("2024/01/01");
    serviceTP.setDateFinValidite("2024/12/31");
    serviceTP.setActivationDesactivation(CodeActivation.DE.value());
    serviceTPs.put("NumeroPersonne", serviceTP);
    mapperAlmerysServiceTP.mapServiceTP(tmpObject2, null, serviceTPs);

    Assertions.assertEquals(1, serviceTPs.size());
    serviceTP = (ServiceTP) serviceTPs.get("NumeroPersonne");
    Assertions.assertEquals("2024/01/01", serviceTP.getDateDebutValidite());
    Assertions.assertEquals("2024/12/31", serviceTP.getDateFinValidite());
    Assertions.assertEquals(CodeActivation.DE.value(), serviceTP.getActivationDesactivation());
  }

  @Test
  void serviceTPTestChangementSuspension() {
    TmpObject2 tmpObject2 = createTmpObject2(Constants.ORIGINE_DECLARATIONEVT, null, null, true);
    Map<String, BulkObject> serviceTPs = new HashMap<>();
    ServiceTP serviceTP = new ServiceTP();
    serviceTP.setDateDebutSuspension("2025/03/01");
    serviceTP.setDateFinSuspension("2025/04/15");
    serviceTP.setActivationDesactivation(CodeActivation.DE.value());
    serviceTPs.put("NumeroPersonne", serviceTP);
    mapperAlmerysServiceTP.mapServiceTP(tmpObject2, null, serviceTPs);

    Assertions.assertEquals(1, serviceTPs.size());
    serviceTP = (ServiceTP) serviceTPs.get("NumeroPersonne");
    Assertions.assertEquals("2025/04/01", serviceTP.getDateDebutSuspension());
    Assertions.assertEquals("2025/04/15", serviceTP.getDateFinSuspension());
  }

  public TmpObject2 createTmpObject2(
      String origine, String dateResiliation, String dateRadiation, boolean isSuspension) {
    TmpObject2 tmpObject2 = new TmpObject2();
    tmpObject2.setCodeEtat("V");
    tmpObject2.setOrigineDeclaration(origine);

    BeneficiaireAlmerys benef = new BeneficiaireAlmerys();
    benef.setRefExternePersonne("RefExternePersonne");
    benef.setNumeroPersonne("NumeroPersonne");
    AdresseAlmerys adresse = new AdresseAlmerys();
    TypeAdresse typeAdresse = new TypeAdresse();
    typeAdresse.setType("AD");
    adresse.setLigne1("Ligne1");
    adresse.setEmail("toto@gmail.com");
    adresse.setTypeAdresse(typeAdresse);
    benef.setAdresses(List.of(adresse));
    Affiliation aff = new Affiliation();
    aff.setNomPatronymique("Patronymique");
    aff.setNom("Nom");
    aff.setPrenom("Prenom");
    aff.setQualite("A");
    aff.setIsTeleTransmission(true);
    aff.setTypeAssure("A");
    benef.setAffiliation(aff);
    benef.setRangNaissance("1");
    benef.setDateNaissance("20000101");
    benef.setDateRadiation(dateRadiation);

    tmpObject2.setBeneficiaire(benef);

    Contrat contrat = new Contrat();
    contrat.setNumero("NumeroContrat");
    contrat.setDestinataire("AD");
    contrat.setDateResiliation(dateResiliation);
    tmpObject2.setContrat(contrat);

    DomaineDroitAlmerys dom = new DomaineDroitAlmerys();
    dom.setCodeExterneProduit("PH");
    PeriodeDroit periode = new PeriodeDroit();
    periode.setPeriodeDebut("2025/01/01");
    periode.setPeriodeFin("2025/12/31");
    periode.setModeObtention("C");
    dom.setPeriodeDroit(periode);
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    prioriteDroit.setCode("03");
    dom.setPrioriteDroit(prioriteDroit);
    if (isSuspension) {
      if (Constants.ORIGINE_DECLARATIONEVT.equals(origine)) {
        PeriodeSuspensionDeclaration periodeSuspensionDeclaration =
            new PeriodeSuspensionDeclaration();
        periodeSuspensionDeclaration.setDebut("2025/04/01");
        periodeSuspensionDeclaration.setFin("2025/04/30");
        dom.setPeriodeSuspension(periodeSuspensionDeclaration);
      } else {
        dom.setIsSuspension(Boolean.TRUE);
      }
    }
    tmpObject2.setDomaineDroit(dom);

    tmpObject2.setIdDeclarations(List.of("IdDeclaration"));

    return tmpObject2;
  }
}
