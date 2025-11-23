package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperDomaineDroitImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
public class MapperDomaineDroitTest {

  @Autowired private MapperDomaineDroitImpl mapperDomaineDroit;

  private DomaineDroit domaineDroit;

  private DomaineDroitDto domaineDroitDto;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    domaineDroit = new DomaineDroit();
    domaineDroit.setCode("HOSP");
    domaineDroit.setCategorie("HOSP");
    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut("2022/01/01");
    periodeDroit.setPeriodeFin("2022/12/31");
    domaineDroit.setPeriodeDroit(periodeDroit);
    domaineDroit.setPeriodeOnline(periodeDroit);
    domaineDroit.setConventionnements(List.of(new Conventionnement()));
    domaineDroit.setPrioriteDroit(new PrioriteDroit());
    domaineDroit.setPrestations(List.of(new Prestation()));
    domaineDroit.setOrigineDroits("test");
    domaineDroit.setNoOrdreDroit(10);
    domaineDroit.setCodeAssureurGarantie("HOSP");
    domaineDroit.setCodeAssureurOrigine("HOSP");
    domaineDroit.setCodeCarence("HOSPC");
    domaineDroit.setCodeExterne("HOSP");
    domaineDroit.setCodeExterneProduit("AXACEGACT");
    domaineDroit.setCodeGarantie("AXASCCGDIM");
    domaineDroit.setCodeOc("AXASCCGDIM");
    domaineDroit.setCodeOffre("AXASCCGDIM");
    domaineDroit.setCodeOptionMutualiste("AXASCCGDIM");
    domaineDroit.setCodeOrigine("HOSP");
    domaineDroit.setCodeProduit("AXACEGACT");
    domaineDroit.setCodeProfil("HOSPPECIS");
    domaineDroit.setCodeExterneProduit("AXACEGACT");
    domaineDroit.setCodeRenvoi("IS");
    domaineDroit.setDateAdhesionCouverture("2014/02/01");
    domaineDroit.setFormulaMask("HOSPPECIS");
    domaineDroit.setIsSuspension(true);
    domaineDroit.setLibelle("HOSP");
    domaineDroit.setLibelleExterne("HOSP");
    domaineDroit.setLibelleProduit("CEGEDIM ACTIFS");
    domaineDroit.setLibelleCodeRenvoi("IS");
    domaineDroit.setLibelleGarantie("CEGEDIM CONFORT");
    domaineDroit.setLibelleOptionMutualiste("AXASCCGDIM");
    domaineDroit.setNaturePrestation("AXASCCGDIM");
    domaineDroit.setPeriodeProductElement(new Periode());
    domaineDroit.setReferenceCouverture("NoSEL");
    domaineDroit.setTauxRemboursement("PEC");
    domaineDroit.setUniteTauxRemboursement("XX");
    domaineDroit.setVersionOffre("2");

    domaineDroitDto = new DomaineDroitDto();
    domaineDroitDto.setCode("HOSP");
    domaineDroitDto.setCategorie("HOSP");
    PeriodeDroitDto periodeDroitDto = new PeriodeDroitDto();
    periodeDroitDto.setPeriodeDebut(DateUtils.parseDate("2023/01/01", DateUtils.FORMATTERSLASHED));
    periodeDroitDto.setPeriodeFin(DateUtils.parseDate("2023/12/31", DateUtils.FORMATTERSLASHED));
    domaineDroitDto.setPeriodeDroit(periodeDroitDto);
    domaineDroitDto.setPeriodeOnline(periodeDroitDto);
    domaineDroitDto.setConventionnements(List.of(new ConventionnementDto()));
    domaineDroitDto.setPrioriteDroit(new PrioriteDroitDto());
    domaineDroitDto.setPrestations(List.of(new PrestationDto()));
    domaineDroitDto.setOrigineDroits("line 2");
    domaineDroitDto.setNoOrdreDroit(10);
    domaineDroitDto.setCodeExterne("HOSP");
    domaineDroitDto.setCodeExterneProduit("AXACEGACT");
    domaineDroitDto.setCodeGarantie("AXASCCGDIM");
    domaineDroitDto.setCodeOptionMutualiste("AXASCCGDIM");
    domaineDroitDto.setCodeProduit("AXACEGACT");
    domaineDroitDto.setCodeProfil("HOSPPECIS");
    domaineDroitDto.setCodeExterneProduit("AXACEGACT");
    domaineDroitDto.setCodeRenvoi("IS");
    domaineDroitDto.setDateAdhesionCouverture("2014/02/01");
    domaineDroitDto.setIsSuspension(true);
    domaineDroitDto.setLibelle("HOSP");
    domaineDroitDto.setLibelleExterne("HOSP");
    domaineDroitDto.setLibelleProduit("CEGEDIM ACTIFS");
    domaineDroitDto.setLibelleCodeRenvoi("IS");
    domaineDroitDto.setLibelleGarantie("CEGEDIM CONFORT");
    domaineDroitDto.setLibelleOptionMutualiste("AXASCCGDIM");
    domaineDroitDto.setPrestations(new ArrayList<>());
    domaineDroitDto.setReferenceCouverture("NoSEL");
    domaineDroitDto.setTauxRemboursement("PEC");
    domaineDroitDto.setUniteTauxRemboursement("XX");
  }

  @Test
  void should_create_dto_from_entity() {
    final DomaineDroitDto domaineDroitDto =
        mapperDomaineDroit.entityToDto(domaineDroit, null, true, false, null);

    Assertions.assertNotNull(domaineDroitDto);
    Assertions.assertEquals(domaineDroit.getCode(), domaineDroitDto.getCode());
    Assertions.assertEquals(domaineDroit.getCategorie(), domaineDroitDto.getCategorie());
    Assertions.assertEquals(domaineDroit.getOrigineDroits(), domaineDroitDto.getOrigineDroits());
    Assertions.assertEquals(domaineDroit.getNoOrdreDroit(), domaineDroitDto.getNoOrdreDroit());
    Assertions.assertEquals(domaineDroit.getCodeExterne(), domaineDroitDto.getCodeExterne());
    Assertions.assertEquals(
        domaineDroit.getCodeExterneProduit(), domaineDroitDto.getCodeExterneProduit());
    Assertions.assertEquals(domaineDroit.getCodeGarantie(), domaineDroitDto.getCodeGarantie());
    Assertions.assertEquals(
        domaineDroit.getCodeOptionMutualiste(), domaineDroitDto.getCodeOptionMutualiste());
    Assertions.assertEquals(domaineDroit.getCodeProduit(), domaineDroitDto.getCodeProduit());
    Assertions.assertEquals(domaineDroit.getCodeProfil(), domaineDroitDto.getCodeProfil());
    Assertions.assertEquals(
        domaineDroit.getCodeExterneProduit(), domaineDroitDto.getCodeExterneProduit());
    Assertions.assertEquals(domaineDroit.getCodeRenvoi(), domaineDroitDto.getCodeRenvoi());
    Assertions.assertEquals(
        domaineDroit.getDateAdhesionCouverture(), domaineDroitDto.getDateAdhesionCouverture());
    Assertions.assertEquals(domaineDroit.getIsSuspension(), domaineDroitDto.getIsSuspension());
    checkLibelles(domaineDroitDto);
    checkPeriodeDto(domaineDroit.getPeriodeOnline(), domaineDroitDto.getPeriodeOnline());
    checkPeriodeDto(domaineDroit.getPeriodeDroit(), domaineDroitDto.getPeriodeDroit());
    Assertions.assertEquals(
        domaineDroit.getReferenceCouverture(), domaineDroitDto.getReferenceCouverture());
    Assertions.assertEquals(
        domaineDroit.getTauxRemboursement(), domaineDroitDto.getTauxRemboursement());
    Assertions.assertEquals(
        domaineDroit.getUniteTauxRemboursement(), domaineDroitDto.getUniteTauxRemboursement());
  }

  private void checkLibelles(DomaineDroitDto domaineDroitDto) {
    Assertions.assertEquals(domaineDroit.getLibelle(), domaineDroitDto.getLibelle());
    Assertions.assertEquals(domaineDroit.getLibelleExterne(), domaineDroitDto.getLibelleExterne());
    Assertions.assertEquals(domaineDroit.getLibelleProduit(), domaineDroitDto.getLibelleProduit());
    Assertions.assertEquals(
        domaineDroit.getLibelleCodeRenvoi(), domaineDroitDto.getLibelleCodeRenvoi());
    Assertions.assertEquals(
        domaineDroit.getLibelleGarantie(), domaineDroitDto.getLibelleGarantie());
    Assertions.assertEquals(
        domaineDroit.getLibelleOptionMutualiste(), domaineDroitDto.getLibelleOptionMutualiste());
    Assertions.assertEquals(
        domaineDroit.getLibelleCodeRenvoiAdditionnel(),
        domaineDroitDto.getLibelleCodeRenvoiAdditionnel());
  }

  private void checkPeriode(PeriodeDroitDto periodeDroitDto, PeriodeDroit periodeDroit) {
    Assertions.assertEquals(
        periodeDroitDto.getPeriodeDebut(),
        DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        periodeDroitDto.getPeriodeFin(),
        DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
  }

  @Test
  void should_create_entity_from_dto() {
    final DomaineDroit domaineDroit = mapperDomaineDroit.dtoToEntity(domaineDroitDto);

    Assertions.assertNotNull(domaineDroit);
    Assertions.assertEquals(domaineDroitDto.getCode(), domaineDroit.getCode());
    Assertions.assertEquals(domaineDroitDto.getCategorie(), domaineDroit.getCategorie());
    Assertions.assertEquals(domaineDroitDto.getOrigineDroits(), domaineDroit.getOrigineDroits());
    Assertions.assertEquals(domaineDroitDto.getNoOrdreDroit(), domaineDroit.getNoOrdreDroit());
    Assertions.assertEquals(domaineDroitDto.getCodeExterne(), domaineDroit.getCodeExterne());
    Assertions.assertEquals(
        domaineDroitDto.getCodeExterneProduit(), domaineDroit.getCodeExterneProduit());
    Assertions.assertEquals(domaineDroitDto.getCodeGarantie(), domaineDroit.getCodeGarantie());
    Assertions.assertEquals(
        domaineDroitDto.getCodeOptionMutualiste(), domaineDroit.getCodeOptionMutualiste());
    Assertions.assertEquals(domaineDroitDto.getCodeProduit(), domaineDroit.getCodeProduit());
    Assertions.assertEquals(domaineDroitDto.getCodeProfil(), domaineDroit.getCodeProfil());
    Assertions.assertEquals(
        domaineDroitDto.getCodeExterneProduit(), domaineDroit.getCodeExterneProduit());
    Assertions.assertEquals(domaineDroitDto.getCodeRenvoi(), domaineDroit.getCodeRenvoi());
    Assertions.assertEquals(
        domaineDroitDto.getDateAdhesionCouverture(), domaineDroit.getDateAdhesionCouverture());
    Assertions.assertEquals(domaineDroitDto.getIsSuspension(), domaineDroit.getIsSuspension());
    checkLibelles(domaineDroit);
    checkPeriode(domaineDroitDto.getPeriodeOnline(), domaineDroit.getPeriodeOnline());
    checkPeriode(domaineDroitDto.getPeriodeDroit(), domaineDroit.getPeriodeDroit());
    Assertions.assertEquals(
        domaineDroitDto.getReferenceCouverture(), domaineDroit.getReferenceCouverture());
    Assertions.assertEquals(
        domaineDroitDto.getTauxRemboursement(), domaineDroit.getTauxRemboursement());
    Assertions.assertEquals(
        domaineDroitDto.getUniteTauxRemboursement(), domaineDroit.getUniteTauxRemboursement());
  }

  private void checkLibelles(DomaineDroit domaineDroit) {
    Assertions.assertEquals(domaineDroitDto.getLibelle(), domaineDroit.getLibelle());
    Assertions.assertEquals(domaineDroitDto.getLibelleExterne(), domaineDroit.getLibelleExterne());
    Assertions.assertEquals(domaineDroitDto.getLibelleProduit(), domaineDroit.getLibelleProduit());
    Assertions.assertEquals(
        domaineDroitDto.getLibelleCodeRenvoi(), domaineDroit.getLibelleCodeRenvoi());
    Assertions.assertEquals(
        domaineDroitDto.getLibelleGarantie(), domaineDroit.getLibelleGarantie());
    Assertions.assertEquals(
        domaineDroitDto.getLibelleOptionMutualiste(), domaineDroit.getLibelleOptionMutualiste());
    Assertions.assertEquals(
        domaineDroitDto.getLibelleCodeRenvoiAdditionnel(),
        domaineDroit.getLibelleCodeRenvoiAdditionnel());
  }

  private void checkPeriodeDto(PeriodeDroit periodeDroit, PeriodeDroitDto periodeDroitDto) {
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getPeriodeDebut());
    Assertions.assertEquals(
        DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED),
        periodeDroitDto.getPeriodeFin());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
