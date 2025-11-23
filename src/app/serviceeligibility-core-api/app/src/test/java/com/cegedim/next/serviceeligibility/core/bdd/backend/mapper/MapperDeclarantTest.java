package com.cegedim.next.serviceeligibility.core.bdd.backend.mapper;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.DeclarantBackendDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.InfoPilotageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.PilotageDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declarant.TranscoDomainesTPDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.utils.DeclarantTestUtils;
import com.cegedim.next.serviceeligibility.core.model.domain.InfoPilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.TranscoDomainesTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import java.text.SimpleDateFormat;
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
class MapperDeclarantTest {

  @Autowired private MapperDeclarant mapperDeclarant;

  @Test
  void entityToDtoTest() {

    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    Date now = new Date();
    Declarant declarant = getDeclarant(now);

    DeclarantBackendDto declarantBackendDto = mapperDeclarant.entityToDto(declarant);
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarantBackendDto.getNom());
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarantBackendDto.getLibelle());

    checkPilotages(sdf, now, declarantBackendDto);

    Assertions.assertEquals(DeclarantTestUtils.CODE_CIRCUIT, declarantBackendDto.getCodeCircuit());
    Assertions.assertEquals(DeclarantTestUtils.SIRET, declarantBackendDto.getSiret());
    Assertions.assertEquals(
        DeclarantTestUtils.CODE_PARTENAIRE, declarantBackendDto.getCodePartenaire());
    Assertions.assertEquals(
        DeclarantTestUtils.EMETTEUR_DROITS, declarantBackendDto.getEmetteurDroits());
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarantBackendDto.getNumero());
    Assertions.assertEquals(DeclarantTestUtils.USER, declarantBackendDto.getIdClientBO());
    Assertions.assertEquals(
        List.of(DeclarantTestUtils.AMC), declarantBackendDto.getNumerosAMCEchanges());
    Assertions.assertEquals(
        DeclarantTestUtils.OPERATEUR_PRINCIPAL, declarantBackendDto.getOperateurPrincipal());

    checkTranscodages(declarantBackendDto);
  }

  private static void checkPilotages(
      SimpleDateFormat sdf, Date now, DeclarantBackendDto declarantBackendDto) {
    Assertions.assertNotNull(declarantBackendDto.getPilotages());
    Assertions.assertNotNull(declarantBackendDto.getPilotages().get(0));
    Assertions.assertNotNull(declarantBackendDto.getPilotages().get(0).getRegroupements());

    InfoPilotageDto infoPilotageDto =
        declarantBackendDto.getPilotages().get(0).getRegroupements().get(0);
    Assertions.assertNotNull(infoPilotageDto);
    Assertions.assertEquals(DeclarantTestUtils.CODE_CLIENT, infoPilotageDto.getCodeClient());
    Assertions.assertEquals(DeclarantTestUtils.COULOIR_CLIENT, infoPilotageDto.getCouloirClient());
    Assertions.assertEquals(sdf.format(now), infoPilotageDto.getDateOuverture());
    Assertions.assertEquals(
        DeclarantTestUtils.CRITERE_REGROUPEMENT, infoPilotageDto.getCritereRegroupement());
    Assertions.assertEquals(true, declarantBackendDto.getPilotages().get(0).getServiceOuvert());
    Assertions.assertEquals(
        DeclarantTestUtils.CODE_SERVICE, declarantBackendDto.getPilotages().get(0).getNom());
  }

  private static void checkTranscodages(DeclarantBackendDto declarantBackendDto) {
    List<TranscoDomainesTPDto> transcodageDomainesTP =
        declarantBackendDto.getTranscodageDomainesTP();
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

  private static Declarant getDeclarant(Date now) {
    Declarant declarant = new Declarant();
    declarant.setNom(DeclarantTestUtils.AMC);
    declarant.setLibelle(DeclarantTestUtils.AMC);
    declarant.setCodeCircuit(DeclarantTestUtils.CODE_CIRCUIT);
    declarant.setSiret(DeclarantTestUtils.SIRET);
    declarant.setCodePartenaire(DeclarantTestUtils.CODE_PARTENAIRE);
    declarant.setEmetteurDroits(DeclarantTestUtils.EMETTEUR_DROITS);
    declarant.setNumeroPrefectoral(DeclarantTestUtils.AMC);
    declarant.setIdClientBO(DeclarantTestUtils.USER);
    declarant.setNumerosAMCEchanges(List.of(DeclarantTestUtils.AMC));
    declarant.setOperateurPrincipal(DeclarantTestUtils.OPERATEUR_PRINCIPAL);
    Pilotage pilotage = getPilotage(now);
    declarant.setPilotages(List.of(pilotage));
    List<TranscoDomainesTP> transcoDomainesTPList = new ArrayList<>();

    TranscoDomainesTP transcoTP1 =
        getTranscoDomainesTP(
            DeclarantTestUtils.DOMAINE_SOURCE1,
            DeclarantTestUtils.DOMAINE_CIBLE_1,
            DeclarantTestUtils.DOMAINE_CIBLE_2);
    transcoDomainesTPList.add(transcoTP1);
    TranscoDomainesTP transcoTP2 =
        getTranscoDomainesTP(
            DeclarantTestUtils.DOMAINE_SOURCE2,
            DeclarantTestUtils.DOMAINE_CIBLE_11,
            DeclarantTestUtils.DOMAINE_CIBLE_21);
    transcoDomainesTPList.add(transcoTP2);
    declarant.setTranscodageDomainesTP(List.of(transcoTP1, transcoTP2));
    return declarant;
  }

  private static TranscoDomainesTP getTranscoDomainesTP(
      String domaineSource, String domaineCible1, String domaineCible2) {
    TranscoDomainesTP transcoTP1 = new TranscoDomainesTP();
    transcoTP1.setDomaineSource(domaineSource);
    List<String> domainesCible = new ArrayList<>();
    domainesCible.add(domaineCible1);
    domainesCible.add(domaineCible2);
    transcoTP1.setDomainesCible(domainesCible);
    return transcoTP1;
  }

  private static Pilotage getPilotage(Date now) {
    Pilotage pilotage = new Pilotage();
    InfoPilotage infoPilotage = new InfoPilotage();
    infoPilotage.setCodeClient(DeclarantTestUtils.CODE_CLIENT);
    pilotage.setCaracteristique(infoPilotage);
    pilotage.setCodeService(DeclarantTestUtils.CODE_SERVICE);
    pilotage.setCouloirClient(DeclarantTestUtils.COULOIR_CLIENT);
    pilotage.setDateOuverture(now);
    pilotage.setCritereRegroupement(DeclarantTestUtils.CRITERE_REGROUPEMENT);
    pilotage.setServiceOuvert(true);
    pilotage.setTriRestitution(2);
    return pilotage;
  }

  @Test
  void dtoToEntityTest() {
    Date now = new Date();
    DeclarantBackendDto declarantBackendDto = getDeclarantBackendDto(now);
    Declarant declarant = mapperDeclarant.dtoToEntity(declarantBackendDto);
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarant.getNom());
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarant.getLibelle());

    checkPilotagesDeclarant(now, declarant);

    Assertions.assertEquals(DeclarantTestUtils.CODE_CIRCUIT, declarant.getCodeCircuit());
    Assertions.assertEquals(DeclarantTestUtils.SIRET, declarant.getSiret());
    Assertions.assertEquals(DeclarantTestUtils.CODE_PARTENAIRE, declarant.getCodePartenaire());
    Assertions.assertEquals(DeclarantTestUtils.EMETTEUR_DROITS, declarant.getEmetteurDroits());
    Assertions.assertEquals(DeclarantTestUtils.AMC, declarant.getNumeroPrefectoral());
    Assertions.assertEquals(DeclarantTestUtils.USER, declarant.getIdClientBO());
    Assertions.assertEquals(List.of(DeclarantTestUtils.AMC), declarant.getNumerosAMCEchanges());
    Assertions.assertEquals(
        DeclarantTestUtils.OPERATEUR_PRINCIPAL, declarant.getOperateurPrincipal());

    checkTranscodagesDeclarant(declarant);
  }

  private static void checkPilotagesDeclarant(Date now, Declarant declarant) {
    Assertions.assertNotNull(declarant.getPilotages());
    Pilotage pilotage = declarant.getPilotages().get(0);
    Assertions.assertNotNull(pilotage);
    Assertions.assertEquals(DeclarantTestUtils.CODE_SERVICE, pilotage.getCodeService());

    InfoPilotage infoPilotage = pilotage.getCaracteristique();
    Assertions.assertNotNull(infoPilotage);
    Assertions.assertEquals(DeclarantTestUtils.CODE_CLIENT, infoPilotage.getCodeClient());
    Assertions.assertEquals(DeclarantTestUtils.COULOIR_CLIENT, pilotage.getCouloirClient());
    Assertions.assertEquals(now, pilotage.getDateOuverture());
    Assertions.assertEquals(
        DeclarantTestUtils.CRITERE_REGROUPEMENT, pilotage.getCritereRegroupement());
    Assertions.assertEquals(true, pilotage.getServiceOuvert());
  }

  private static void checkTranscodagesDeclarant(Declarant declarant) {
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

  private static DeclarantBackendDto getDeclarantBackendDto(Date now) {
    DeclarantBackendDto declarantBackendDto = new DeclarantBackendDto();
    declarantBackendDto.setNom(DeclarantTestUtils.AMC);
    declarantBackendDto.setLibelle(DeclarantTestUtils.AMC);
    declarantBackendDto.setCodeCircuit(DeclarantTestUtils.CODE_CIRCUIT);
    declarantBackendDto.setSiret(DeclarantTestUtils.SIRET);
    declarantBackendDto.setCodePartenaire(DeclarantTestUtils.CODE_PARTENAIRE);
    declarantBackendDto.setEmetteurDroits(DeclarantTestUtils.EMETTEUR_DROITS);
    declarantBackendDto.setNumero(DeclarantTestUtils.AMC);
    declarantBackendDto.setIdClientBO(DeclarantTestUtils.USER);
    declarantBackendDto.setNumerosAMCEchanges(List.of(DeclarantTestUtils.AMC));
    declarantBackendDto.setOperateurPrincipal(DeclarantTestUtils.OPERATEUR_PRINCIPAL);
    PilotageDto pilotage = getPilotageDto(now);
    declarantBackendDto.setPilotages(List.of(pilotage));

    TranscoDomainesTPDto transcoTP1 =
        getTranscoDomainesTPDto(
            DeclarantTestUtils.DOMAINE_SOURCE1,
            DeclarantTestUtils.DOMAINE_CIBLE_1,
            DeclarantTestUtils.DOMAINE_CIBLE_2);
    TranscoDomainesTPDto transcoTP2 =
        getTranscoDomainesTPDto(
            DeclarantTestUtils.DOMAINE_SOURCE2,
            DeclarantTestUtils.DOMAINE_CIBLE_11,
            DeclarantTestUtils.DOMAINE_CIBLE_21);
    declarantBackendDto.setTranscodageDomainesTP(List.of(transcoTP1, transcoTP2));
    return declarantBackendDto;
  }

  private static PilotageDto getPilotageDto(Date now) {
    PilotageDto pilotage = new PilotageDto();
    InfoPilotageDto infoPilotageDto = new InfoPilotageDto();
    infoPilotageDto.setCodeClient(DeclarantTestUtils.CODE_CLIENT);
    pilotage.setNom(DeclarantTestUtils.CODE_SERVICE);
    infoPilotageDto.setCouloirClient(DeclarantTestUtils.COULOIR_CLIENT);
    infoPilotageDto.setDateTimeOuverture(now);
    infoPilotageDto.setCritereRegroupement(DeclarantTestUtils.CRITERE_REGROUPEMENT);
    List<InfoPilotageDto> infoPilotageDtoList = new ArrayList<>();
    infoPilotageDtoList.add(infoPilotageDto);
    pilotage.setRegroupements(infoPilotageDtoList);
    pilotage.setServiceOuvert(true);
    return pilotage;
  }

  private static TranscoDomainesTPDto getTranscoDomainesTPDto(
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
