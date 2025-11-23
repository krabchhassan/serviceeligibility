package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat.BeneficiaireCouvertureDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.cartedemat.MapperBeneficiaireCouvertureImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
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
class MapperBenefCouvertureImplTest {
  @Autowired private MapperBeneficiaireCouvertureImpl mapperBeneficiaireCouverture;

  private BeneficiaireCouvertureDto beneficiaireCouvertureDto;
  private DomaineDroit domaineDroit;

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

    beneficiaireCouvertureDto = new BeneficiaireCouvertureDto();
    beneficiaireCouvertureDto.setCategorieDomaine("");
    beneficiaireCouvertureDto.setCodeProduit("");
    beneficiaireCouvertureDto.setCodeGarantie("");
    beneficiaireCouvertureDto.setCodeDomaine("");
    beneficiaireCouvertureDto.setLibelleGarantie("");
    beneficiaireCouvertureDto.setTauxRemboursement("80");
    beneficiaireCouvertureDto.setUniteTauxRemboursement("%");
    beneficiaireCouvertureDto.setPeriodeDebut(
        DateUtils.stringToXMLGregorianCalendar("2023/01/01", DateUtils.FORMATTERSLASHED));
    beneficiaireCouvertureDto.setPeriodeFin(
        DateUtils.stringToXMLGregorianCalendar("2023/12/31", DateUtils.FORMATTERSLASHED));
    beneficiaireCouvertureDto.setCodeExterneProduit("");
    beneficiaireCouvertureDto.setCodeOptionMutualiste("");
    beneficiaireCouvertureDto.setLibelleOptionMutualiste("");
    beneficiaireCouvertureDto.setLibelleProduit("");
    beneficiaireCouvertureDto.setLibelleGarantie("");
    beneficiaireCouvertureDto.setPrioriteDroits("");
    beneficiaireCouvertureDto.setOrigineDroits("");
    beneficiaireCouvertureDto.setDateAdhesionCouverture(
        DateUtils.stringToXMLGregorianCalendar("2023/01/01", DateUtils.FORMATTERSLASHED));
    beneficiaireCouvertureDto.setLibelleCodeRenvoi("");
    beneficiaireCouvertureDto.setPrestationDtos(new ArrayList<>());
  }

  @Test
  void should_create_dto_from_entity() {
    final BeneficiaireCouvertureDto dto =
        mapperBeneficiaireCouverture.entityToDto(domaineDroit, null, false, false, null);

    Assertions.assertNotNull(dto);
    Assertions.assertEquals(domaineDroit.getCode(), dto.getCodeDomaine());
    Assertions.assertEquals(domaineDroit.getCategorie(), dto.getCategorieDomaine());
    Assertions.assertEquals(domaineDroit.getOrigineDroits(), dto.getOrigineDroits());
    Assertions.assertEquals(domaineDroit.getCodeExterneProduit(), dto.getCodeExterneProduit());
    Assertions.assertEquals(domaineDroit.getCodeGarantie(), dto.getCodeGarantie());
    Assertions.assertEquals(domaineDroit.getCodeOptionMutualiste(), dto.getCodeOptionMutualiste());
    Assertions.assertEquals(domaineDroit.getCodeProduit(), dto.getCodeProduit());
    Assertions.assertEquals(domaineDroit.getCodeExterneProduit(), dto.getCodeExterneProduit());
    Assertions.assertEquals(
        domaineDroit.getDateAdhesionCouverture(),
        DateUtils.convertXmlGregorianToString(
            dto.getDateAdhesionCouverture(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(domaineDroit.getLibelleProduit(), dto.getLibelleProduit());
    Assertions.assertEquals(domaineDroit.getLibelleCodeRenvoi(), dto.getLibelleCodeRenvoi());
    Assertions.assertEquals(domaineDroit.getLibelleGarantie(), dto.getLibelleGarantie());
    Assertions.assertEquals(
        domaineDroit.getLibelleOptionMutualiste(), dto.getLibelleOptionMutualiste());
    Assertions.assertEquals(
        domaineDroit.getPeriodeDroit().getPeriodeDebut(),
        DateUtils.convertXmlGregorianToString(dto.getPeriodeDebut(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(
        domaineDroit.getPeriodeDroit().getPeriodeFin(),
        DateUtils.convertXmlGregorianToString(dto.getPeriodeFin(), DateUtils.FORMATTERSLASHED));
    Assertions.assertEquals(domaineDroit.getTauxRemboursement(), dto.getTauxRemboursement());
    Assertions.assertEquals(
        domaineDroit.getUniteTauxRemboursement(), dto.getUniteTauxRemboursement());
    Assertions.assertEquals(
        domaineDroit.getLibelleCodeRenvoiAdditionnel(), dto.getLibelleCodeRenvoiAdditionnel());
  }

  @Test
  void should_create_entity_from_dto() {
    DomaineDroit domaineDroit = mapperBeneficiaireCouverture.dtoToEntity(beneficiaireCouvertureDto);
    Assertions.assertNull(domaineDroit);
  }
}
