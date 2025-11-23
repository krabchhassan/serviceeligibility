package com.cegedim.next.serviceeligibility.core.soap.consultation.ws;

import com.cegedim.next.serviceeligibility.core.TestConfig;
import com.cegedim.next.serviceeligibility.core.model.domain.TranscoDomainesTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.PauUtils;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.BenefitType;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Domain;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.GenericRightDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfig.class)
class BddsPriorityTpContractToBlbControllerTestTP {

  @Autowired BddsPriorityTpContractToBlbController bddsPriorityTpContractToBlbController;

  @Autowired MongoTemplate mongoTemplate;

  @Test
  void testEmpty() {
    List<GenericRightDto> rightDtos = new ArrayList<>();
    GenericRightDto genericRightDto = PauUtils.getContratDeBase();
    rightDtos.add(genericRightDto);

    List<GenericRightDto> returnRightDtos =
        bddsPriorityTpContractToBlbController.filterDomain(rightDtos, "OPAU");
    Assertions.assertTrue(returnRightDtos.isEmpty());
  }

  @Test
  void testNotEmpty() {
    List<GenericRightDto> rightDtos = new ArrayList<>();
    GenericRightDto genericRightDto = PauUtils.getContratDeBase();
    rightDtos.add(genericRightDto);
    Declarant declarant = new Declarant();
    List<TranscoDomainesTP> transcoDomainesTPList = new ArrayList<>();
    TranscoDomainesTP transcoDomainesTP = new TranscoDomainesTP();
    transcoDomainesTP.setDomaineSource("OPAU");
    transcoDomainesTP.setDomainesCible(List.of("OPTI", "AUDI"));
    transcoDomainesTPList.add(transcoDomainesTP);
    declarant.setTranscodageDomainesTP(transcoDomainesTPList);
    final Criteria criteria = Criteria.where("numeroPrefectoral").is("0000401166");
    final Query query = Query.query(criteria);

    Mockito.when(mongoTemplate.findOne(query, Declarant.class)).thenReturn(declarant);
    List<GenericRightDto> returnRightDtos =
        bddsPriorityTpContractToBlbController.filterDomain(rightDtos, "OPAU");
    Assertions.assertFalse(returnRightDtos.isEmpty());
  }

  @Test
  void testNotEmptyWith2Contracts() {
    List<GenericRightDto> rightDtos = new ArrayList<>();
    GenericRightDto genericRightDto = PauUtils.getContratDeBase();
    GenericRightDto genericRightDto2 = PauUtils.getContratDeBase();
    Set<BenefitType> benefitTypeSet =
        genericRightDto2.getInsured().getRights().get(0).getProducts().get(0).getBenefitsType();
    BenefitType benefitType = benefitTypeSet.iterator().next();
    benefitType.setDomains(List.of(new Domain("HOSP", "")));
    rightDtos.add(genericRightDto);
    rightDtos.add(genericRightDto2);
    Declarant declarant = new Declarant();
    List<TranscoDomainesTP> transcoDomainesTPList = new ArrayList<>();
    TranscoDomainesTP transcoDomainesTP = new TranscoDomainesTP();
    transcoDomainesTP.setDomaineSource("OPAU");
    transcoDomainesTP.setDomainesCible(List.of("OPTI", "AUDI"));
    transcoDomainesTPList.add(transcoDomainesTP);
    declarant.setTranscodageDomainesTP(transcoDomainesTPList);
    final Criteria criteria = Criteria.where("numeroPrefectoral").is("0000401166");
    final Query query = Query.query(criteria);

    Mockito.when(mongoTemplate.findOne(query, Declarant.class)).thenReturn(declarant);
    List<GenericRightDto> returnRightDtos =
        bddsPriorityTpContractToBlbController.filterDomain(rightDtos, "OPAU");
    Assertions.assertEquals(1, returnRightDtos.size());
  }

  @Test
  void testNotEmptyWith2Contracts2() {
    List<GenericRightDto> rightDtos = new ArrayList<>();
    GenericRightDto genericRightDto = PauUtils.getContratDeBase();
    GenericRightDto genericRightDto2 = PauUtils.getContratDeBase();
    Set<BenefitType> benefitTypeSet =
        genericRightDto2.getInsured().getRights().get(0).getProducts().get(0).getBenefitsType();
    BenefitType benefitType = benefitTypeSet.iterator().next();
    benefitType.setDomains(List.of(new Domain("HOSP", "")));
    BenefitType benefitType2 = new BenefitType();
    benefitType2.setDomains(List.of(new Domain("AUDI", "")));
    benefitTypeSet.add(benefitType2);
    rightDtos.add(genericRightDto);
    rightDtos.add(genericRightDto2);
    Declarant declarant = new Declarant();
    List<TranscoDomainesTP> transcoDomainesTPList = new ArrayList<>();
    TranscoDomainesTP transcoDomainesTP = new TranscoDomainesTP();
    transcoDomainesTP.setDomaineSource("OPAU");
    transcoDomainesTP.setDomainesCible(List.of("OPTI", "AUDI"));
    transcoDomainesTPList.add(transcoDomainesTP);
    declarant.setTranscodageDomainesTP(transcoDomainesTPList);
    final Criteria criteria = Criteria.where("numeroPrefectoral").is("0000401166");
    final Query query = Query.query(criteria);

    Mockito.when(mongoTemplate.findOne(query, Declarant.class)).thenReturn(declarant);
    List<GenericRightDto> returnRightDtos =
        bddsPriorityTpContractToBlbController.filterDomain(rightDtos, "OPAU");
    Assertions.assertEquals(2, returnRightDtos.size());
  }

  @Test
  void testNotEmptyWith2ContractsWithProductEmpty() {
    List<GenericRightDto> rightDtos = new ArrayList<>();
    GenericRightDto genericRightDto = PauUtils.getContratDeBase();
    GenericRightDto genericRightDto2 = PauUtils.getContratDeBase();
    genericRightDto2.getInsured().getRights().get(0).setProducts(new ArrayList<>());
    rightDtos.add(genericRightDto);
    rightDtos.add(genericRightDto2);
    Declarant declarant = new Declarant();
    List<TranscoDomainesTP> transcoDomainesTPList = new ArrayList<>();
    TranscoDomainesTP transcoDomainesTP = new TranscoDomainesTP();
    transcoDomainesTP.setDomaineSource("OPAU");
    transcoDomainesTP.setDomainesCible(List.of("APTI", "AUDI"));
    transcoDomainesTPList.add(transcoDomainesTP);
    declarant.setTranscodageDomainesTP(transcoDomainesTPList);
    final Criteria criteria = Criteria.where("numeroPrefectoral").is("0000401166");
    final Query query = Query.query(criteria);

    Mockito.when(mongoTemplate.findOne(query, Declarant.class)).thenReturn(declarant);
    List<GenericRightDto> returnRightDtos =
        bddsPriorityTpContractToBlbController.filterDomain(rightDtos, "OPAU");
    Assertions.assertEquals(0, returnRightDtos.size());
  }
}
