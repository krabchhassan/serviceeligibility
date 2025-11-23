package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PeriodeDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.DomaineDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.mapper.declaration.MapperDomaineDroit;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperPeriodeDroitImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperPeriodeDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.PeriodeDroit;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.text.SimpleDateFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
class MapperPeriodeDroitImplTest {

  private MapperPeriodeDroit mapper;

  private PeriodeDroit periodeDroit;
  private PeriodeDroitDto periodeDroitDto;

  /** Methode permettant d'initialiser les tests. */
  @BeforeAll
  public void initTests() {
    mapper = new MapperPeriodeDroitImpl();

    prepareData();
  }

  private void prepareData() {
    periodeDroit = new PeriodeDroit();
    periodeDroit.setLibelleEvenement("libelleEvenement");
    periodeDroit.setMotifEvenement("motifEvenement");
    periodeDroit.setDateEvenement("2022/01/01");
    periodeDroit.setPeriodeDebut("2023/01/01");
    periodeDroit.setPeriodeFin("2023/12/31");
    periodeDroit.setPeriodeFinInitiale("2022/12/31");
    periodeDroit.setModeObtention("modeObtention");
    periodeDroit.setPeriodeFermetureDebut("2023/12/31");
    periodeDroit.setPeriodeFermetureFin("2024/01/01");

    periodeDroitDto = new PeriodeDroitDto();
    periodeDroitDto.setLibelleEvenement("libelleEvenement");
    periodeDroitDto.setMotifEvenement("motifEvenement");
    periodeDroitDto.setDateEvenemnt(DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    periodeDroitDto.setPeriodeDebut(DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    periodeDroitDto.setPeriodeFin(DateUtils.parseDate("2021/12/31", DateUtils.FORMATTERSLASHED));
    periodeDroitDto.setPeriodeFinInitiale(
        DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    periodeDroitDto.setModeObtention("modeObtention");
    periodeDroitDto.setPeriodeFermetureDebut(
        DateUtils.parseDate("2021/12/31", DateUtils.FORMATTERSLASHED));
    periodeDroitDto.setPeriodeFermetureFin(
        DateUtils.parseDate("2022/01/01", DateUtils.FORMATTERSLASHED));
  }

  @Test
  void should_create_entity_from_dto() {
    final PeriodeDroit entity = mapper.dtoToEntity(periodeDroitDto);

    Assertions.assertNotNull(entity);
    Assertions.assertEquals(periodeDroitDto.getLibelleEvenement(), entity.getLibelleEvenement());
    Assertions.assertEquals(periodeDroitDto.getMotifEvenement(), entity.getMotifEvenement());
    Assertions.assertEquals(
        periodeDroitDto.getDateEvenemnt(),
        DateUtils.parseDate(entity.getDateEvenement(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        periodeDroitDto.getPeriodeDebut(),
        DateUtils.parseDate(entity.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        periodeDroitDto.getPeriodeFin(),
        DateUtils.parseDate(entity.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        periodeDroitDto.getPeriodeFinInitiale(),
        DateUtils.parseDate(entity.getPeriodeFinInitiale(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(periodeDroitDto.getModeObtention(), entity.getModeObtention());
    Assertions.assertEquals(
        periodeDroitDto.getPeriodeFermetureDebut(),
        DateUtils.parseDate(entity.getPeriodeFermetureDebut(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        periodeDroitDto.getPeriodeFermetureFin(),
        DateUtils.parseDate(entity.getPeriodeFermetureFin(), DateUtils.FORMATTERSLASHED));
  }

  @Test
  void restitution_periodOnline() {
    MapperDomaineDroit mapperDomaineDroit = new MapperDomaineDroit();
    DomaineDroitDto domaineDroitDto = new DomaineDroitDto();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfDefault = new SimpleDateFormat("dd/MM/yyyy");

    DomaineDroit dd = new DomaineDroit();
    PeriodeDroit pd = new PeriodeDroit();
    pd.setPeriodeDebut("2022/01/01");
    pd.setPeriodeFin("2031/12/31");
    dd.setPeriodeDroit(pd);
    pd.setPeriodeDebut("2022/01/01");
    pd.setPeriodeFin("2031/12/31");
    dd.setPeriodeOnline(pd);
    mapperDomaineDroit.extractDates(dd, sdf, sdfDefault, domaineDroitDto, null, false);
    Assertions.assertEquals("01/01/2022", domaineDroitDto.getPeriodeOnlineDebut());
    Assertions.assertEquals("31/12/2031", domaineDroitDto.getPeriodeOnlineFin());

    PeriodeDroit pd2 = new PeriodeDroit();
    pd2.setPeriodeDebut("2022/01/02");
    dd.setPeriodeOnline(pd2);
    mapperDomaineDroit.extractDates(dd, sdf, sdfDefault, domaineDroitDto, null, false);
    Assertions.assertEquals("02/01/2022", domaineDroitDto.getPeriodeOnlineDebut());
    Assertions.assertEquals("--/--/----", domaineDroitDto.getPeriodeOnlineFin());

    pd2.setPeriodeFin("2022/03/31");
    mapperDomaineDroit.extractDates(dd, sdf, sdfDefault, domaineDroitDto, null, false);
    Assertions.assertEquals("02/01/2022", domaineDroitDto.getPeriodeOnlineDebut());
    Assertions.assertEquals("31/03/2022", domaineDroitDto.getPeriodeOnlineFin());
  }

  @Test
  void restitution_periodOffline() {
    MapperDomaineDroit mapperDomaineDroit = new MapperDomaineDroit();
    DomaineDroitDto domaineDroitDto = new DomaineDroitDto();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    SimpleDateFormat sdfDefault = new SimpleDateFormat("dd/MM/yyyy");

    DomaineDroit dd = new DomaineDroit();
    PeriodeDroit pd = new PeriodeDroit();
    pd.setPeriodeDebut("2023/01/01");
    pd.setPeriodeFin("2023/12/20");
    dd.setPeriodeDroit(pd);
    String dateRestitution = "2023/12/25";

    mapperDomaineDroit.extractDates(dd, sdf, sdfDefault, domaineDroitDto, null, false);
    Assertions.assertEquals("01/01/2023", domaineDroitDto.getPeriodeDebut());
    Assertions.assertEquals("20/12/2023", domaineDroitDto.getPeriodeOfflineFin());

    mapperDomaineDroit.extractDates(dd, sdf, sdfDefault, domaineDroitDto, dateRestitution, false);
    Assertions.assertEquals("01/01/2023", domaineDroitDto.getPeriodeDebut());
    Assertions.assertEquals("25/12/2023", domaineDroitDto.getPeriodeOfflineFin());
  }

  @Test
  void should_create_dto_from_entity() {

    PeriodeDroitDto periodeDroitDto = mapper.entityToDto(periodeDroit, null, false, false, null);

    Assertions.assertNotNull(periodeDroitDto);
    Assertions.assertEquals(
        periodeDroit.getLibelleEvenement(), periodeDroitDto.getLibelleEvenement());
    Assertions.assertEquals(periodeDroit.getMotifEvenement(), periodeDroitDto.getMotifEvenement());
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getDateEvenement(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getDateEvenemnt());
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getPeriodeDebut());
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getPeriodeFin());
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getPeriodeFinInitiale(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getPeriodeFinInitiale());
    Assertions.assertEquals(periodeDroit.getModeObtention(), periodeDroitDto.getModeObtention());
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getPeriodeFermetureDebut(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getPeriodeFermetureDebut());
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getPeriodeFermetureFin(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getPeriodeFermetureFin());
  }
}
