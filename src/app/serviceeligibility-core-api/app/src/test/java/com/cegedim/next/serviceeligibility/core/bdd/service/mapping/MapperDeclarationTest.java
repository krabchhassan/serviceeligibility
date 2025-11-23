package com.cegedim.next.serviceeligibility.core.bdd.service.mapping;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarantDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.impl.MapperDeclarationImpl;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantServiceImpl;
import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.PeriodeSuspensionDeclaration;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;

@ActiveProfiles("test")
@SpringBootTest(classes = {TestConfig.class})
@TestInstance(Lifecycle.PER_CLASS)
public class MapperDeclarationTest {

  @Autowired private MapperDeclarationImpl mapperDeclaration;

  private Declaration declaration;

  private DeclarationDto declarationDto;

  @Autowired public DeclarantServiceImpl declarantService;

  @BeforeTestClass
  public static void init() {
    TomcatURLStreamHandlerFactory.getInstance();
  }

  @BeforeAll
  public void initTests() {
    prepareData();
  }

  private void prepareData() {
    declaration = new Declaration();
    declaration.setIdDeclarant("0032165198");
    declaration.setDateRestitution("2023/12/15");

    PeriodeSuspensionDeclaration periodeSuspensionDeclaration = new PeriodeSuspensionDeclaration();
    periodeSuspensionDeclaration.setDebut("2023/03/15");
    periodeSuspensionDeclaration.setFin("2023/03/31");
    Contrat contrat = new Contrat();
    contrat.setPeriodeSuspensions(List.of(periodeSuspensionDeclaration));
    declaration.setContrat(contrat);
    declaration.setEtatSuspension("SUSPENDU");
    declaration.setCodeEtat("RE");
    declaration.setDateModification(DateUtils.parseDate("2021/03/01", DateUtils.FORMATTERSLASHED));
    declaration.setEffetDebut(DateUtils.parseDate("2021/01/01", DateUtils.FORMATTERSLASHED));
    declaration.setCarteTPaEditerOuDigitale("Digitale");
    declaration.setDateCreation(DateUtils.parseDate("2021/01/02", DateUtils.FORMATTERSLASHED));
    declaration.setIdOrigine("03");
    declaration.setIsCarteTPaEditer(false);
    declaration.setIdTrigger("04");
    declaration.setNomFichierOrigine("test.txt");
    declaration.setReferenceExterne("012345");
    declaration.setUserCreation("bob@gmail.com");
    declaration.setUserModification("bob@gmail.com");
    declaration.setVersionDeclaration("V5");

    declarationDto = new DeclarationDto();
    declarationDto.setCodeEtat("V");
    declarationDto.setEffetDebut(DateUtils.parseDate("2021/03/01", DateUtils.FORMATTERSLASHED));
    declarationDto.setIsCarteTPaEditer(true);
    declarationDto.setNomFichierOrigine("testDto.txt");
    declarationDto.setReferenceExterne("112345");
    DeclarantDto declarantDto = new DeclarantDto();
    declarantDto.setNumeroPrefectoral("0032165199");
    declarationDto.setDeclarantAmc(declarantDto);
    declarationDto.setDomaineDroits(new ArrayList<>());
  }

  @Test
  void should_create_dto_from_entity() {
    Declarant declarant = new Declarant();
    declarant.set_id("0032165198");
    Mockito.when(declarantService.findById(Mockito.anyString())).thenReturn(declarant);
    final DeclarationDto declarationDto =
        mapperDeclaration.entityToDto(
            declaration, TypeProfondeurRechercheService.AVEC_FORMULES, false, false, null);

    Assertions.assertNotNull(declarationDto);
    Assertions.assertEquals(declarationDto.getCodeEtat(), declaration.getCodeEtat());
    Assertions.assertEquals(declarationDto.getEffetDebut(), declaration.getEffetDebut());
    Assertions.assertEquals(
        declarationDto.getIsCarteTPaEditer(), declaration.getIsCarteTPaEditer());
    Assertions.assertEquals(
        declarationDto.getNomFichierOrigine(), declaration.getNomFichierOrigine());
    Assertions.assertEquals(
        declarationDto.getReferenceExterne(), declaration.getReferenceExterne());
    Assertions.assertEquals(
        declarationDto.getDeclarantAmc().getNumeroPrefectoral(), declaration.getIdDeclarant());
  }

  @Test
  void should_create_entity_from_dto() {
    final Declaration declaration = mapperDeclaration.dtoToEntity(declarationDto);

    Assertions.assertNotNull(declaration);
    Assertions.assertEquals(
        declaration.getIdDeclarant(), declarationDto.getDeclarantAmc().getNumeroPrefectoral());
    Assertions.assertEquals(declaration.getCodeEtat(), declarationDto.getCodeEtat());
    Assertions.assertEquals(declaration.getEffetDebut(), declarationDto.getEffetDebut());
    Assertions.assertEquals(
        declaration.getIsCarteTPaEditer(), declarationDto.getIsCarteTPaEditer());
    Assertions.assertEquals(
        declaration.getNomFichierOrigine(), declarationDto.getNomFichierOrigine());
    Assertions.assertEquals(
        declaration.getReferenceExterne(), declarationDto.getReferenceExterne());
  }

  @Test
  void shouldBeTheSame() {
    Assertions.assertEquals(
        Integer.compare(Integer.parseInt("1"), Integer.parseInt("2")), "1".compareTo("2"));
  }
}
