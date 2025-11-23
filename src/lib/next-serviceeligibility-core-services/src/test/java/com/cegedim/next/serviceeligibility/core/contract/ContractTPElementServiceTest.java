package com.cegedim.next.serviceeligibility.core.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ContractTPElementServiceTest {
  @Autowired private ContractElementRepository contractElementRepository;

  @Autowired ContractElementRepository repository;

  @Autowired MongoTemplate mongoTemplate;

  @Test
  void testMapProductElement() {
    ContractElementService contractElementService =
        new ContractElementService(contractElementRepository);

    ContractElement ce = new ContractElement();
    ce.setCodeAMC("CE_CODE_AMC");
    ce.setCodeContractElement("CE_CODE_CONTRACT");
    ce.setCodeInsurer("CE_CODE_INSURER");

    List<ProductElement> productElements = new ArrayList<>();
    ProductElement pe = new ProductElement();
    pe.setCodeAmc("PE_AMC");
    pe.setCodeBenefitNature("PE_CODE_BENEFIT_NATURE");
    pe.setCodeOffer("PE_CODE_OFFER");
    pe.setCodeProduct("PE_CODE_PRODUCT");
    productElements.add(pe);
    ce.setProductElements(productElements);

    contractElementService.create(ce);

    Mockito.when(mongoTemplate.findOne(Mockito.any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(ce);
    Mockito.when(mongoTemplate.find(Mockito.any(Query.class), Mockito.eq(ContractElement.class)))
        .thenReturn(List.of(ce));
    List<ContractElement> oce = repository.get("CE_CODE_CONTRACT", "CE_CODE_INSURER", true);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(oce));

    ContractElement gce = oce.get(0);
    assertEquals("CE_CODE_AMC", gce.getCodeAMC());
    assertEquals("CE_CODE_CONTRACT", gce.getCodeContractElement());
    assertEquals("CE_CODE_INSURER", gce.getCodeInsurer());

    contractElementService.setIgnored(gce.getId());
    oce = repository.get("CE_CODE_CONTRACT", "CE_CODE_INSURER", true);
    Assertions.assertTrue(CollectionUtils.isNotEmpty(oce));

    gce = oce.get(0);
    assertEquals("CE_CODE_AMC", gce.getCodeAMC());
    assertEquals("CE_CODE_CONTRACT", gce.getCodeContractElement());
    assertEquals("CE_CODE_INSURER", gce.getCodeInsurer());

    // Mapping
    ProductElement productElement = new ProductElement();
    productElement.setCodeAmc("PE_AMC");
    productElement.setCodeBenefitNature("PE_CODE_BENEFIT_NATURE");
    productElement.setCodeOffer("PE_CODE_OFFER");
    productElement.setCodeProduct("PE_CODE_PRODUCT");
    ContractElement oceMapping =
        contractElementService.get("CE_CODE_CONTRACT", "CE_CODE_INSURER", false);
    Assertions.assertNotNull(oceMapping);
    contractElementService.map(oceMapping.getId(), productElement);
    contractElementService.endMapping(oceMapping.getId(), productElement);
    oceMapping = contractElementService.get("CE_CODE_CONTRACT", "CE_CODE_INSURER", false);
    Assertions.assertNotNull(oceMapping);

    Collection<ContractElement> collCe = contractElementService.findAll();
    Assertions.assertNotNull(collCe);
    collCe = contractElementService.get("CE_CODE_AMC");
    Assertions.assertNotNull(collCe);
    Assertions.assertTrue(
        contractElementService.isMappingActive(productElement, LocalDateTime.now(ZoneOffset.UTC)));
  }
}
