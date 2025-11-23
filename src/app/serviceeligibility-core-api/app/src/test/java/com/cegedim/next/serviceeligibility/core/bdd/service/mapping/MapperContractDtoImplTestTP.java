package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperContratImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat.MapperBenefCarteDematImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
class MapperContractDtoImplTestTP {

  private MapperBenefCarteDematImpl mapperCarte;

  @Autowired private MapperContratImpl mapperContrat;

  private ContratDto dto;
  private Contrat contrat;

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    contrat = new Contrat();
    contrat.setCategorieSociale("categorieSociale");
    contrat.setNomPorteur("nomPorteur");
    contrat.setAnnexe1Carte("annexe1");
    contrat.setAnnexe2Carte("annexe2");
    contrat.setDestinataire("destinataire");
    contrat.setCivilitePorteur("Mme");
    contrat.setGestionnaire("gestionnaire");
    contrat.setCritereSecondaire("critere secondaire");
    contrat.setCritereSecondaireDetaille("critere secondaire detaille");
    contrat.setFondCarte("fond");
    contrat.setEditeurCarte("editeur");
    contrat.setDateSouscription("2021/01/01");
    contrat.setDateResiliation("2021/12/31");
    contrat.setSituationDebut("2021/03/01");
    contrat.setSituationFin("2021/05/31");
    contrat.setSituationParticuliere("AM");
    contrat.setMotifFinSituation("RE");
    contrat.setGroupeAssures("Cadres");
    contrat.setIndividuelOuCollectif("individuel");
    contrat.setIsContratCMU(true);
    contrat.setIsContratResponsable(true);
    contrat.setNumeroExterneContratIndividuel("SLOK4983989");
    contrat.setNumero("8343484392");
    contrat.setLienFamilial("Mere");
    contrat.setModePaiementPrestations("VIR");
    contrat.setNumeroAdherent("83747438");
    contrat.setNumeroAdherentComplet("TBT83747438");
    contrat.setNumAMCEchange("0097810998");
    contrat.setNumeroContratCollectif("0");
    contrat.setNumeroExterneContratCollectif("01");
    contrat.setNumOperateur("022");

    dto = new ContratDto();
    dto.setCategorieSociale("categorieSociale");
    dto.setNomPorteur("nomPorteur");
    dto.setAnnexe1Carte("annexe1");
    dto.setAnnexe2Carte("annexe2");
    dto.setDestinataire("destinataire");
    dto.setCivilitePorteur("Mme");
    dto.setGestionnaire("gestionnaire");
    dto.setCritereSecondaire("critere secondaire");
    dto.setCritereSecondaireDetaille("critere secondaire detaille");
    dto.setFondCarte("fond");
    dto.setEditeurCarte("editeur");
    dto.setDateSouscription(DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    dto.setDateResiliation(DateUtils.parseDate("2021/12/31", DateUtils.FORMATTERSLASHED));
    dto.setSituationDebut(DateUtils.parseDate("2021/03/01", DateUtils.FORMATTERSLASHED));
    dto.setSituationfin(DateUtils.parseDate("2021/05/31", DateUtils.FORMATTERSLASHED));
    dto.setSituationParticuliere("AM");
    dto.setMotifFinSituation("RE");
    dto.setGroupeAssures("Cadres");
    dto.setIndividuelOuCollectif("individuel");
    dto.setIsContratCMU(true);
    dto.setIsContratResponsable(true);
    dto.setNumeroExterneContratIndividuel("SLOK4983989");
    dto.setNumero("8343484392");
    dto.setLienFamilial("Mere");
    dto.setModePaiementPrestations("VIR");
    dto.setNumeroAdherent("83747438");
    dto.setNumeroAdherentComplet("TBT83747438");
    dto.setNumeroAMCEchanges("0097810998");
    dto.setNumeroContratCollectif("0");
    dto.setNumeroExterneContratCollectif("01");
    dto.setNumeroOperateur("022");
  }

  @Test
  void should_create_entity_from_dto() {
    final Contrat entity = mapperContrat.dtoToEntity(dto);

    Assertions.assertNotNull(entity);
    Assertions.assertEquals(dto.getCategorieSociale(), entity.getCategorieSociale());
    Assertions.assertEquals(dto.getNomPorteur(), entity.getNomPorteur());
    checkCardData(entity);
    Assertions.assertEquals(dto.getDestinataire(), entity.getDestinataire());
    Assertions.assertEquals(dto.getCivilitePorteur(), entity.getCivilitePorteur());
    Assertions.assertEquals(dto.getGestionnaire(), entity.getGestionnaire());
    Assertions.assertEquals(dto.getCritereSecondaire(), entity.getCritereSecondaire());
    Assertions.assertEquals(
        dto.getCritereSecondaireDetaille(), entity.getCritereSecondaireDetaille());
    checkDates(entity);
    Assertions.assertEquals(dto.getSituationParticuliere(), entity.getSituationParticuliere());
    Assertions.assertEquals(dto.getMotifFinSituation(), entity.getMotifFinSituation());
    Assertions.assertEquals(dto.getGroupeAssures(), entity.getGroupeAssures());
    Assertions.assertEquals(dto.getIndividuelOuCollectif(), entity.getIndividuelOuCollectif());
    Assertions.assertEquals(dto.getIsContratCMU(), entity.getIsContratCMU());
    Assertions.assertEquals(dto.getIsContratResponsable(), entity.getIsContratResponsable());
    Assertions.assertEquals(
        dto.getNumeroExterneContratIndividuel(), entity.getNumeroExterneContratIndividuel());
    Assertions.assertEquals(dto.getNumero(), entity.getNumero());
    Assertions.assertEquals(dto.getLienFamilial(), entity.getLienFamilial());
    Assertions.assertEquals(dto.getModePaiementPrestations(), entity.getModePaiementPrestations());
    Assertions.assertEquals(dto.getNumeroAdherent(), entity.getNumeroAdherent());
    Assertions.assertEquals(dto.getNumeroAdherentComplet(), entity.getNumeroAdherentComplet());
    Assertions.assertEquals(dto.getNumeroAMCEchanges(), entity.getNumAMCEchange());
    checkContratColl(entity);
    Assertions.assertEquals(dto.getNumeroOperateur(), entity.getNumOperateur());
  }

  private void checkContratColl(Contrat entity) {
    Assertions.assertEquals(dto.getNumeroContratCollectif(), entity.getNumeroContratCollectif());
    Assertions.assertEquals(
        dto.getNumeroExterneContratCollectif(), entity.getNumeroExterneContratCollectif());
  }

  private void checkCardData(Contrat entity) {
    Assertions.assertEquals(dto.getAnnexe1Carte(), entity.getAnnexe1Carte());
    Assertions.assertEquals(dto.getAnnexe2Carte(), entity.getAnnexe2Carte());
    Assertions.assertEquals(dto.getFondCarte(), entity.getFondCarte());
    Assertions.assertEquals(dto.getEditeurCarte(), entity.getEditeurCarte());
  }

  private void checkDates(Contrat entity) {
    Assertions.assertEquals(
        dto.getDateSouscription(),
        DateUtils.parseDate(entity.getDateSouscription(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        dto.getDateResiliation(),
        DateUtils.parseDate(entity.getDateResiliation(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        dto.getSituationDebut(),
        DateUtils.parseDate(entity.getSituationDebut(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        dto.getSituationfin(),
        DateUtils.parseDate(entity.getSituationFin(), DateUtils.FORMATTERSLASHED));
  }

  @Test
  void should_create_dto_from_entity() {
    final ContratDto contratDto = mapperContrat.entityToDto(contrat, null, false, false, null);

    Assertions.assertNotNull(contratDto);
    Assertions.assertEquals(contrat.getCategorieSociale(), contratDto.getCategorieSociale());
    Assertions.assertEquals(contrat.getNomPorteur(), contratDto.getNomPorteur());
    checkCardDataDto(contratDto);
    Assertions.assertEquals(contrat.getDestinataire(), contratDto.getDestinataire());
    Assertions.assertEquals(contrat.getCivilitePorteur(), contratDto.getCivilitePorteur());
    Assertions.assertEquals(contrat.getGestionnaire(), contratDto.getGestionnaire());
    Assertions.assertEquals(contrat.getCritereSecondaire(), contratDto.getCritereSecondaire());
    Assertions.assertEquals(
        contrat.getCritereSecondaireDetaille(), contratDto.getCritereSecondaireDetaille());
    checkDatesDto(contratDto);
    Assertions.assertEquals(
        contrat.getSituationParticuliere(), contratDto.getSituationParticuliere());
    Assertions.assertEquals(contrat.getMotifFinSituation(), contratDto.getMotifFinSituation());
    Assertions.assertEquals(contrat.getGroupeAssures(), contratDto.getGroupeAssures());
    Assertions.assertEquals(
        contrat.getIndividuelOuCollectif(), contratDto.getIndividuelOuCollectif());
    Assertions.assertEquals(contrat.getIsContratCMU(), contratDto.getIsContratCMU());
    Assertions.assertEquals(
        contrat.getIsContratResponsable(), contratDto.getIsContratResponsable());
    Assertions.assertEquals(
        contrat.getNumeroExterneContratIndividuel(),
        contratDto.getNumeroExterneContratIndividuel());
    Assertions.assertEquals(contrat.getNumero(), contratDto.getNumero());
    Assertions.assertEquals(contrat.getLienFamilial(), contratDto.getLienFamilial());
    Assertions.assertEquals(
        contrat.getModePaiementPrestations(), contratDto.getModePaiementPrestations());
    Assertions.assertEquals(contrat.getNumeroAdherent(), contratDto.getNumeroAdherent());
    Assertions.assertEquals(
        contrat.getNumeroAdherentComplet(), contratDto.getNumeroAdherentComplet());
    Assertions.assertEquals(contrat.getNumAMCEchange(), contratDto.getNumeroAMCEchanges());
    checkContratCollDto(contratDto);
    Assertions.assertEquals(contrat.getNumOperateur(), contratDto.getNumeroOperateur());
  }

  private void checkContratCollDto(ContratDto contratDto) {
    Assertions.assertEquals(
        contrat.getNumeroContratCollectif(), contratDto.getNumeroContratCollectif());
    Assertions.assertEquals(
        contrat.getNumeroExterneContratCollectif(), contratDto.getNumeroExterneContratCollectif());
  }

  private void checkCardDataDto(ContratDto contratDto) {
    Assertions.assertEquals(contrat.getAnnexe1Carte(), contratDto.getAnnexe1Carte());
    Assertions.assertEquals(contrat.getAnnexe2Carte(), contratDto.getAnnexe2Carte());
    Assertions.assertEquals(contrat.getFondCarte(), contratDto.getFondCarte());
    Assertions.assertEquals(contrat.getEditeurCarte(), contratDto.getEditeurCarte());
  }

  private void checkDatesDto(ContratDto contratDto) {
    Assertions.assertEquals(
        DateUtils.parseDate(contrat.getDateSouscription(), DateUtils.FORMATTERSLASHED),
        contratDto.getDateSouscription());
    Assertions.assertEquals(
        DateUtils.parseDate(contrat.getDateResiliation(), DateUtils.FORMATTERSLASHED),
        contratDto.getDateResiliation());
    Assertions.assertEquals(
        DateUtils.parseDate(contrat.getSituationDebut(), DateUtils.FORMATTERSLASHED),
        contratDto.getSituationDebut());
    Assertions.assertEquals(
        DateUtils.parseDate(contrat.getSituationFin(), DateUtils.FORMATTERSLASHED),
        contratDto.getSituationfin());
  }

  @Test
  void should_return_null() {
    mapperCarte = new MapperBenefCarteDematImpl();
    BenefCarteDemat result = mapperCarte.dtoToEntity(null);
    Assertions.assertNull(result);
  }
}
