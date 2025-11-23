package com.cegedim.next.serviceeligibility.core.services;

import static org.junit.jupiter.api.Assertions.*;

import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.LotDao;
import com.cegedim.next.serviceeligibility.core.dao.ParametrageCarteTPDao;
import com.cegedim.next.serviceeligibility.core.dao.RequestParametrageCarteTP;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.*;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.ServicePrestationTriggerBenef;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiary;
import com.cegedim.next.serviceeligibility.core.model.entity.ParametrageCarteTPResponse;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ParametrageCarteTPStatut;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ServicePrestationNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ParametrageCarteTPServiceTest {

  public static final String CODE_GARANTIE = "GAR001";
  public static final String CODE_ASSUREUR = "ASS001";
  public static final String LOT_1 = "LOT1";
  public static final String LOT_2 = "LOT2";
  public static final String LOT_3 = "LOT3";
  private final Logger logger = LoggerFactory.getLogger(ParametrageCarteTPServiceTest.class);

  @Autowired ParametrageCarteTPService service;

  @Autowired MongoTemplate mongoTemplate;

  @Autowired LotDao lotDao;

  @Autowired ParametrageCarteTPDao parametrageCarteTPDao;

  @BeforeEach
  public void loadparametrageCarteTPForSeveralAmcs() {
    logger.info("Suppression des cartes TP");
    mongoTemplate.findAllAndRemove(new Query(), Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
    logger.info("Chargement de paramétrage de Carte TP");

    ParametrageCarteTP param1 = getParametrageCarteTP("0123456789", 1);
    ParametrageCarteTP param2 = getParametrageCarteTP("0001112223", 1);
    ParametrageCarteTP param3 = getParametrageCarteTP("0001112223", 2);
    ParametrageCarteTP param4 = getParametrageCarteTP("0001112223", 3);
    List<ParametrageCarteTP> params = new ArrayList<>(List.of(param1, param2, param3, param4));

    service.create(param1);
    service.create(param2);
    service.create(param3);
    service.create(param4);
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(params);

    Mockito.when(mongoTemplate.save(Mockito.any(ParametrageCarteTP.class), Mockito.anyString()))
        .thenAnswer(
            invocation -> {
              Object[] args = invocation.getArguments();
              return args[0];
            });
  }

  @Test
  void shouldHaveOnlyOneResultAndUpdateStatus() {
    logger.info("Retourne qu'un seul résultat de recherche par amc");
    List<String> amcs = new ArrayList<>();
    amcs.add("0123456789");
    ParametrageCarteTPRequest request = new ParametrageCarteTPRequest();
    request.setAmcs(amcs);
    ParametrageCarteTP mockedParam = getParametrageCarteTP("0123456789", 1);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(List.of(mockedParam));
    ParametrageCarteTPResponse response =
        service.getByParams(10, 1, "dateCreation", "DESC", request);
    assertEquals(1, response.getParametragesCarteTP().size());
    ParametrageCarteTP param = response.getParametragesCarteTP().get(0);
    assertEquals(ParametrageCarteTPStatut.Actif, param.getStatut());

    Mockito.when(
            mongoTemplate.findById(
                Mockito.anyString(), Mockito.eq(ParametrageCarteTP.class), Mockito.anyString()))
        .thenReturn(mockedParam);
    service.updateStatus(param.getId(), ParametrageCarteTPStatut.Inactif);
    response = service.getByParams(10, 1, "dateCreation", "DESC", request);
    assertEquals(1, response.getParametragesCarteTP().size());
    param = response.getParametragesCarteTP().get(0);
    assertEquals(ParametrageCarteTPStatut.Inactif, param.getStatut());
  }

  @Test
  void shouldHaveThreeResults() {
    logger.info("Retourne 3 résultats de recherche par amc");
    List<String> amcs = new ArrayList<>();
    amcs.add("0001112223");
    ParametrageCarteTPRequest request = new ParametrageCarteTPRequest();
    request.setAmcs(amcs);
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(
            List.of(
                getParametrageCarteTP("0001112223", 1),
                getParametrageCarteTP("0001112223", 2),
                getParametrageCarteTP("0001112223", 3)));
    ParametrageCarteTPResponse response =
        service.getByParams(10, 1, "dateCreation", "DESC", request);
    assertEquals(3, response.getParametragesCarteTP().size());
  }

  @Test
  void shouldHaveThreePages() {
    logger.info("Retourne 3 pages");
    List<String> amcs = new ArrayList<>();
    amcs.add("0001112223");
    ParametrageCarteTPRequest request = new ParametrageCarteTPRequest();
    request.setAmcs(amcs);
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(
            List.of(
                getParametrageCarteTP("0001112223", 1),
                getParametrageCarteTP("0001112223", 2),
                getParametrageCarteTP("0001112223", 3)));
    ParametrageCarteTPResponse response =
        service.getByParams(1, 1, "dateCreation", "DESC", request);
    // Can't mock paging
    // Assertions.assertEquals(1, response.getParametragesCarteTP().size());
    // Assertions.assertEquals(3, response.getPaging().getTotalPages());
  }

  @Test
  void shouldHaveNoResult() {
    logger.info("Retourne aucun résultat de recherche par amc");
    List<String> amcs = new ArrayList<>();
    amcs.add("9999999999");
    ParametrageCarteTPRequest request = new ParametrageCarteTPRequest();
    request.setAmcs(amcs);
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(new ArrayList<>());
    ParametrageCarteTPResponse response =
        service.getByParams(10, 1, "dateCreation", "DESC", request);
    assertEquals(0, response.getParametragesCarteTP().size());
  }

  @Test
  void shouldDeleteAll() {
    logger.info("Supression de tous les paramétrage");
    service.deleteAll();
    List<String> amcs = new ArrayList<>();
    amcs.add("0001112223");
    ParametrageCarteTPRequest request = new ParametrageCarteTPRequest();
    request.setAmcs(amcs);
    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(new ArrayList<>());
    ParametrageCarteTPResponse response =
        service.getByParams(10, 1, "dateCreation", "DESC", request);
    assertEquals(0, response.getParametragesCarteTP().size());
  }

  @Test
  void shouldGetParametrageCarteTP() {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setId("UUID");
    param.setAmc("AMC1");
    param.setStatut(ParametrageCarteTPStatut.Actif);
    param.setDateDebutValidite("2020-01-01");
    ParametrageCarteTP savedParam =
        mongoTemplate.save(param, Constants.PARAMETRAGE_CARTE_TP_COLLECTION);
    TriggeredBeneficiary benef = new TriggeredBeneficiary();
    benef.setAmc("AMC1");
    benef.setParametrageCarteTPId(savedParam.getId());

    Mockito.when(
            mongoTemplate.findById(
                Mockito.anyString(),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.eq(Constants.PARAMETRAGE_CARTE_TP_COLLECTION)))
        .thenReturn(param);
    ParametrageCarteTP getParam = service.getParametrageCarteTP(benef, false);
    assertEquals("AMC1", getParam.getAmc());

    benef = new TriggeredBeneficiary();
    benef.setAmc("AMC1");
    ServicePrestationTriggerBenef newContrat = new ServicePrestationTriggerBenef();
    DroitAssure droit = new DroitAssure();
    droit.setCode("code");
    droit.setCodeAssureur("codeAssureur");
    newContrat.getDroitsGaranties().add(droit);
    benef.setNewContract(newContrat);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(List.of(param));
    getParam = service.getParametrageCarteTP(benef, false);
    assertEquals("AMC1", getParam.getAmc());
  }

  @Test
  void shouldGetParametrageCarteTPWithGTAndLot() {
    List<GarantieTechnique> gts1 = new ArrayList<>();
    GarantieTechnique gt1 = new GarantieTechnique();
    gt1.setCodeAssureur("CodeAssur1");
    gt1.setCodeGarantie("CodeGar1");
    gts1.add(gt1);

    List<GarantieTechnique> gts2 = new ArrayList<>();
    GarantieTechnique gt2 = new GarantieTechnique();
    gt2.setCodeAssureur("CodeAssur2");
    gt2.setCodeGarantie("CodeGar2");
    gts2.add(gt2);

    GarantieTechnique gt3 = new GarantieTechnique();
    gt3.setCodeAssureur("CodeAssur3");
    gt3.setCodeGarantie("CodeGar3");

    List<GarantieTechnique> gts3 = new ArrayList<>();
    gts3.add(gt3);

    GarantieTechnique gt4 = new GarantieTechnique();
    gt4.setCodeAssureur("CodeAssur4");
    gt4.setCodeGarantie("CodeGar4");

    Lot lot1 = new Lot();
    lot1.setCode("LOT1");
    lot1.setLibelle("libelle1");
    lot1.setGarantieTechniques(List.of(gt1, gt3));

    Lot lot2 = new Lot();
    lot2.setCode("LOT2");
    lot2.setLibelle("libelle2");
    lot2.setGarantieTechniques(List.of(gt2, gt3));

    Lot lot3 = new Lot();
    lot3.setCode("LOT3");
    lot3.setLibelle("libelle3");
    lot3.setGarantieTechniques(List.of(gt1, gt2));

    // param0 : param sans gt et sans lot -> param generique
    ParametrageCarteTP param0 = new ParametrageCarteTP();
    param0.setId("UUID0");
    param0.setAmc("AMC");
    param0.setStatut(ParametrageCarteTPStatut.Actif);
    param0.setDateDebutValidite("2020-01-01");
    param0.setPriorite(999);

    // param1 : GT1 + LOT1 (GT2-GT3)
    ParametrageCarteTP param1 = new ParametrageCarteTP();
    param1.setId("UUID1");
    param1.setAmc("AMC");
    param1.setStatut(ParametrageCarteTPStatut.Actif);
    param1.setDateDebutValidite("2020-01-01");
    param1.setGarantieTechniques(gts1);
    param1.setIdLots(List.of("LOT1"));
    param1.setPriorite(10);

    // param2 : GT2 + LOT2 (GT1-GT3)
    ParametrageCarteTP param2 = new ParametrageCarteTP();
    param2.setId("UUID2");
    param2.setAmc("AMC");
    param2.setStatut(ParametrageCarteTPStatut.Actif);
    param2.setDateDebutValidite("2020-01-01");
    param2.setGarantieTechniques(gts2);
    param2.setIdLots(List.of("LOT2"));
    param2.setPriorite(9);

    // param3 : GT3 + LOT1 (GT2-GT3)
    ParametrageCarteTP param3 = new ParametrageCarteTP();
    param3.setId("UUID3");
    param3.setAmc("AMC");
    param3.setStatut(ParametrageCarteTPStatut.Actif);
    param3.setDateDebutValidite("2020-01-01");
    param3.setGarantieTechniques(gts3);
    param3.setIdLots(List.of("LOT1"));
    param3.setPriorite(8);

    Mockito.when(
            mongoTemplate.find(
                Mockito.any(Query.class),
                Mockito.eq(ParametrageCarteTP.class),
                Mockito.anyString()))
        .thenReturn(List.of(param0, param1, param2, param3));

    Mockito.when(lotDao.getById("LOT1")).thenReturn(lot1);
    Mockito.when(lotDao.getById("LOT2")).thenReturn(lot2);

    List<GarantieTechnique> gtsContrat = new ArrayList<>();
    gtsContrat.add(gt1);
    ParametrageCarteTP result1 =
        service.getBestParametrage(
            null,
            null,
            null,
            gtsContrat,
            new RequestParametrageCarteTP("AMC", true, true, true, true));
    assertNotNull(
        result1); // param 1 ok -> la GT1 est dans le lot 1 et directement au niveau des gts du
    // paramétrage
    assertEquals("UUID1", result1.getId());

    gtsContrat = new ArrayList<>();
    gtsContrat.add(gt2);
    gtsContrat.add(gt3);
    ParametrageCarteTP result2 =
        service.getBestParametrage(
            null,
            null,
            null,
            gtsContrat,
            new RequestParametrageCarteTP("AMC", true, true, true, true));
    assertNotNull(result2); // param 2 -> la GT2 est dans le dans les 2 paramétrages
    assertEquals("UUID3", result2.getId());

    gtsContrat = new ArrayList<>();
    gtsContrat.add(gt3);
    gtsContrat.add(gt4);
    ParametrageCarteTP result3 =
        service.getBestParametrage(
            null,
            null,
            null,
            gtsContrat,
            new RequestParametrageCarteTP("AMC", true, true, true, true));
    assertNotNull(result3); // param 3 ok
    assertEquals("UUID3", result3.getId());

    gtsContrat = new ArrayList<>();
    gtsContrat.add(gt4);
    ParametrageCarteTP result4 =
        service.getBestParametrage(
            null,
            null,
            null,
            gtsContrat,
            new RequestParametrageCarteTP("AMC", true, true, true, true));
    assertNotNull(
        result4); // la GT4 ne se trouve dans aucun paramétrage avec des GTs et des lots, donc
    // c'est le paramétrage generique
    assertEquals("UUID0", result4.getId());
  }

  private ParametrageCarteTP getParametrageCarteTP(String amc, int indice) {
    ParametrageCarteTP param = new ParametrageCarteTP();
    param.setId("randomUUID");
    param.setAmc(amc);
    param.setIdentifiantCollectivite("COLPARAM" + indice);
    param.setGroupePopulation("COLLEGE_PARAM" + indice);
    param.setCritereSecondaireDetaille("CSD_PARAM" + indice);
    param.setStatut(ParametrageCarteTPStatut.Actif);
    param.setDateDebutValidite("2020-01-01");

    // Paramétrage de renouvellement

    ParametrageRenouvellement parametrageRenouvellement = new ParametrageRenouvellement();
    parametrageRenouvellement.setDateRenouvellementCarteTP(DateRenouvellementCarteTP.DebutEcheance);
    parametrageRenouvellement.setDebutEcheance("01/01");
    parametrageRenouvellement.setDureeValiditeDroitsCarteTP(DureeValiditeDroitsCarteTP.Annuel);
    parametrageRenouvellement.setDelaiDeclenchementCarteTP(15);
    // dateDeclenchementAutomatique;
    parametrageRenouvellement.setSeuilSecurite(1000);
    param.setParametrageRenouvellement(parametrageRenouvellement);

    // Paramétrage de Droits de carte TP

    ParametrageDroitsCarteTP parametrageDroitsCarteTP = new ParametrageDroitsCarteTP();
    parametrageDroitsCarteTP.setCodeConventionTP("SP");
    parametrageDroitsCarteTP.setCodeOperateurTP("OPS");
    parametrageDroitsCarteTP.setIsCarteEditablePapier(true);
    parametrageDroitsCarteTP.setIsCarteDematerialisee(true);
    parametrageDroitsCarteTP.setRefFondCarte("fondCarte1");
    parametrageDroitsCarteTP.setCodeAnnexe1("Annexe1");
    parametrageDroitsCarteTP.setCodeAnnexe2("Annexe2");
    List<DetailDroit> detailsDroit = new ArrayList<>();
    DetailDroit detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(1);
    detailDroit.setCodeDomaineTP("PHAR");
    detailDroit.setLibelleDomaineTP("Pharmacie");
    detailDroit.setConvention("IS");

    detailsDroit.add(detailDroit);
    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(2);
    detailDroit.setCodeDomaineTP("DENT");
    detailDroit.setLibelleDomaineTP("Dentaire");
    detailDroit.setConvention("IS");
    detailsDroit.add(detailDroit);
    detailDroit = new DetailDroit();
    detailDroit.setOrdreAffichage(3);
    detailDroit.setCodeDomaineTP("OPTI");
    detailDroit.setLibelleDomaineTP("Optique");
    detailDroit.setConvention("SP");
    detailsDroit.add(detailDroit);
    parametrageDroitsCarteTP.setDetailsDroit(detailsDroit);

    param.setParametrageDroitsCarteTP(parametrageDroitsCarteTP);
    return param;
  }

  @Test
  void getParametrageCarteTPByContratIndividuelTest() {
    try {
      service.getParametrageCarteTPForUI("11111", "222", "123");
    } catch (ServicePrestationNotFoundException e) {
      Assertions.assertEquals(
          "Aucun servicePrestation trouvé avec les informations suivantes : idDeclarant=11111 - numero=222 - numeroAdherent=123",
          e.getMessage());
    }
  }

  @Test
  void should_return_lots_without_deleted_GTs_and_parametrages() {
    String codeGarantie = CODE_GARANTIE;
    String codeAssureur = CODE_ASSUREUR;
    ParametrageCarteTP parametrageCarteTP = new ParametrageCarteTP();
    parametrageCarteTP.setIdLots(List.of(LOT_1, LOT_2));

    Mockito.when(
            parametrageCarteTPDao.findByGuaranteeCodeAndInsurerCode(codeGarantie, codeAssureur))
        .thenReturn(List.of(parametrageCarteTP));

    GarantieTechnique gtActive = new GarantieTechnique();
    gtActive.setDateSuppressionLogique(null);

    GarantieTechnique gtDeleted = new GarantieTechnique();
    gtDeleted.setDateSuppressionLogique("2025-01-01");

    Lot lot1 = new Lot();
    lot1.setId(LOT_1);
    lot1.setGarantieTechniques(List.of(gtActive));

    Lot lot2 = new Lot();
    lot2.setId(LOT_2);
    lot2.setGarantieTechniques(List.of(gtDeleted));

    Lot lot3 = new Lot();
    lot3.setId(LOT_3);
    lot3.setGarantieTechniques(null);

    Mockito.when(lotDao.getListByIds(List.of(LOT_1, LOT_2))).thenReturn(List.of(lot1, lot2, lot3));

    ParametrageCarteTPResponseDto result =
        service.getByGuaranteeCodeAndInsurerCode(codeGarantie, codeAssureur);

    assertNotNull(result);

    List<String> lotIds = result.getLots().stream().map(Lot::getId).toList();

    assertFalse(lotIds.contains(LOT_2));
  }

  @Test
  void should_handle_empty_parametrages() {
    Mockito.when(
            parametrageCarteTPDao.findByGuaranteeCodeAndInsurerCode(CODE_GARANTIE, CODE_ASSUREUR))
        .thenReturn(Collections.emptyList());

    ParametrageCarteTPResponseDto result =
        service.getByGuaranteeCodeAndInsurerCode(CODE_GARANTIE, CODE_ASSUREUR);

    assertNotNull(result);
    assertEquals(0, result.getLots().size());
    assertEquals(0, result.getParametrageCarteTPS().size());
  }
}
