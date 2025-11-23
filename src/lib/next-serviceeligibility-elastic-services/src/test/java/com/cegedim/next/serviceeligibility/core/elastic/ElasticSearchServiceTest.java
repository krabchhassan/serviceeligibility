package com.cegedim.next.serviceeligibility.core.elastic;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import com.cegedim.next.serviceeligibility.core.elast.BenefSearchRequest;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opensearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class ElasticSearchServiceTest {

  @Autowired BenefElasticService benefElasticService;

  @Test
  void should_create_request() {
    BenefSearchRequest request = new BenefSearchRequest();
    request.setBic("bic");
    request.setBirthDate("2002");
    request.setBirthRank("1");
    request.setCity("city");
    request.setContractNumber("number");
    request.setDeclarantId("amcId");
    request.setDeclarantIdOrLabel("declarantIdOrLabel");
    request.setDeclarantLabel("declarantLabel");
    request.setEmail("email");
    request.setFirstName("firstName");
    request.setIban("iban");
    request.setName("name");
    request.setNir("nir");
    request.setPage(1);
    request.setPerPage(2);
    request.setPostalCode("postalCode");
    request.setStreet("street");
    request.setSubscriberId("subscriberId");
    request.setSubscriberIdOrContractNumber("subscriberIdOrContractNumber");
    request.setSubscriberNumber("subscriberNumber");
    request.setIssuingCompany("issuingCompany");
    request.setInsurerExchangeId("insurerExchangeId");
    var search = benefElasticService.generateSearchRequest(request);
    List<QueryBuilder> musts = search.must();
    Assertions.assertEquals(5, musts.size());
  }
}
