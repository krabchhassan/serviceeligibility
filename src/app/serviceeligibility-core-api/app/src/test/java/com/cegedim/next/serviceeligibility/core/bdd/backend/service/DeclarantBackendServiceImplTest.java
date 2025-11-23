package com.cegedim.next.serviceeligibility.core.bdd.backend.service;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.DeclarantRequestDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.InfoPilotageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.PilotageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.TranscoDomainesTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.utils.DeclarantTestUtils;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoPilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.TranscoDomainesTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DeclarantBackendServiceImplTest {

  @Autowired private DeclarantBackendServiceImpl declarantBackendService;

  @Test
  void createDeclarantTest() {

    Date now = new Date();

    DeclarantRequestDto declarantRequestDto = getDeclarantRequestDto(now);
    Declarant declarant = declarantBackendService.createDeclarant(declarantRequestDto);
    Assertions.assertNotNull(declarant);
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarant.getNom());
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarant.getLibelle());

    Assertions.assertEquals(DeclarantTestUtils.CODE_CIRCUIT, declarant.getCodeCircuit());
    Assertions.assertEquals(DeclarantTestUtils.SIRET, declarant.getSiret());
    Assertions.assertEquals(DeclarantTestUtils.CODE_PARTENAIRE, declarant.getCodePartenaire());
    Assertions.assertEquals(DeclarantTestUtils.EMETTEUR_DROITS, declarant.getEmetteurDroits());
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarant.getNumeroPrefectoral());
    Assertions.assertEquals(DeclarantTestUtils.USER, declarant.getIdClientBO());
    Assertions.assertEquals(List.of(DeclarantTestUtils.AMC), declarant.getNumerosAMCEchanges());
    Assertions.assertEquals(
        DeclarantTestUtils.OPERATEUR_PRINCIPAL, declarant.getOperateurPrincipal());
    checkPilotages(now, declarant);
    checkTranscodages(declarant);
  }

  private static void checkTranscodages(Declarant declarant) {
    List<TranscoDomainesTP> transcodageDomainesTP = declarant.getTranscodageDomainesTP();
    Assertions.assertNotNull(transcodageDomainesTP);
    Assertions.assertEquals(2, transcodageDomainesTP.size());

    Assertions.assertEquals(
        DeclarantTestUtils.DOMAINE_SOURCE1, transcodageDomainesTP.get(0).getDomaineSource());
    Assertions.assertNotNull(transcodageDomainesTP.get(0).getDomainesCible().get(0));
    Assertions.assertEquals(
        DeclarantTestUtils.DOMAINE_CIBLE_1, transcodageDomainesTP.get(0).getDomainesCible().get(0));
    Assertions.assertEquals(
        DeclarantTestUtils.DOMAINE_CIBLE_2, transcodageDomainesTP.get(0).getDomainesCible().get(1));

    Assertions.assertEquals(
        DeclarantTestUtils.DOMAINE_SOURCE2, transcodageDomainesTP.get(1).getDomaineSource());
    Assertions.assertNotNull(transcodageDomainesTP.get(0).getDomainesCible().get(1));
    Assertions.assertEquals(
        DeclarantTestUtils.DOMAINE_CIBLE_11,
        transcodageDomainesTP.get(1).getDomainesCible().get(0));
    Assertions.assertEquals(
        DeclarantTestUtils.DOMAINE_CIBLE_21,
        transcodageDomainesTP.get(1).getDomainesCible().get(1));
  }

  private static void checkPilotages(Date now, Declarant declarant) {
    Assertions.assertNotNull(declarant.getPilotages());
    Pilotage pilotage = declarant.getPilotages().get(0);
    Assertions.assertNotNull(pilotage);

    InfoPilotage infoPilotage = pilotage.getCaracteristique();
    Assertions.assertNotNull(infoPilotage);
    Assertions.assertEquals(DeclarantTestUtils.CODE_CLIENT, infoPilotage.getCodeClient());
    Assertions.assertEquals(DeclarantTestUtils.COULOIR_CLIENT, pilotage.getCouloirClient());
    Assertions.assertEquals(now, pilotage.getDateOuverture());
    Assertions.assertEquals(
        DeclarantTestUtils.CRITERE_REGROUPEMENT, pilotage.getCritereRegroupement());
    Assertions.assertEquals(true, pilotage.getServiceOuvert());
    Assertions.assertEquals(DeclarantTestUtils.CODE_SERVICE, pilotage.getCodeService());
  }

  private static DeclarantRequestDto getDeclarantRequestDto(Date now) {
    DeclarantRequestDto declarant = new DeclarantRequestDto();
    declarant.setNumero(DeclarantTestUtils.AMC);
    declarant.setNom(DeclarantTestUtils.AMC);
    declarant.setLibelle(DeclarantTestUtils.AMC);
    declarant.setCodeCircuit(DeclarantTestUtils.CODE_CIRCUIT);
    declarant.setSiret(DeclarantTestUtils.SIRET);
    declarant.setCodePartenaire(DeclarantTestUtils.CODE_PARTENAIRE);
    declarant.setEmetteurDroits(DeclarantTestUtils.EMETTEUR_DROITS);
    declarant.setIdClientBO(DeclarantTestUtils.USER);
    declarant.setNumerosAMCEchanges(List.of(DeclarantTestUtils.AMC));
    declarant.setOperateurPrincipal(DeclarantTestUtils.OPERATEUR_PRINCIPAL);
    PilotageDto pilotage = getPilotage(now);
    declarant.setPilotages(List.of(pilotage));
    List<TranscoDomainesTPDto> transcoDomainesTPList = new ArrayList<>();

    TranscoDomainesTPDto transcoTP1 =
        getTranscoDomainesTP(
            DeclarantTestUtils.DOMAINE_SOURCE1,
            DeclarantTestUtils.DOMAINE_CIBLE_1,
            DeclarantTestUtils.DOMAINE_CIBLE_2);
    transcoDomainesTPList.add(transcoTP1);
    TranscoDomainesTPDto transcoTP2 =
        getTranscoDomainesTP(
            DeclarantTestUtils.DOMAINE_SOURCE2,
            DeclarantTestUtils.DOMAINE_CIBLE_11,
            DeclarantTestUtils.DOMAINE_CIBLE_21);
    transcoDomainesTPList.add(transcoTP2);
    declarant.setTranscodageDomainesTP(List.of(transcoTP1, transcoTP2));
    return declarant;
  }

  private static PilotageDto getPilotage(Date now) {
    PilotageDto pilotage = new PilotageDto();
    pilotage.setNom(DeclarantTestUtils.CODE_SERVICE);
    pilotage.setServiceOuvert(true);
    InfoPilotageDto infoPilotage = new InfoPilotageDto();
    infoPilotage.setCodeClient(DeclarantTestUtils.CODE_CLIENT);
    infoPilotage.setCouloirClient(DeclarantTestUtils.COULOIR_CLIENT);
    infoPilotage.setDateTimeOuverture(now);
    infoPilotage.setCritereRegroupement(DeclarantTestUtils.CRITERE_REGROUPEMENT);
    pilotage.setRegroupements(List.of(infoPilotage));
    return pilotage;
  }

  private static TranscoDomainesTPDto getTranscoDomainesTP(
      String domaineSource, String domaineCible1, String domaineCible2) {
    TranscoDomainesTPDto transcoTP1 = new TranscoDomainesTPDto();
    transcoTP1.setDomaineSource(domaineSource);
    List<String> domainesCible = new ArrayList<>();
    domainesCible.add(domaineCible1);
    domainesCible.add(domaineCible2);
    transcoTP1.setDomainesCible(domainesCible);
    return transcoTP1;
  }
}
