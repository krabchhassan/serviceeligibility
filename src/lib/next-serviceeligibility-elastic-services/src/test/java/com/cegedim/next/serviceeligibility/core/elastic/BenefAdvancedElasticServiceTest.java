package com.cegedim.next.serviceeligibility.core.elastic;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.elast.BenefAdvancedSearchRequest;
import com.cegedim.next.serviceeligibility.core.elast.BenefElasticService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.opensearch.search.builder.SearchSourceBuilder;
import org.opensearch.search.sort.SortOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class BenefAdvancedElasticServiceTest {

  @Autowired private BenefElasticService benefElasticService;

  @Test
  void should_generate_query_for_all_fields() throws IOException {
    BenefAdvancedSearchRequest request = getDummyRequest();
    request.setName("Robert");
    request.setFirstName("Oscar");
    request.setNir("1880312454169");
    request.setSubscriberId("009831004");
    request.setContractNumber("112831296");
    request.setBirthDate("19880310");
    request.setPostalCode("31000");
    request.setIssuingCompany("SUD");
    request.setBirthRank("1");
    request.setInsurerId("0000441166");

    SearchSourceBuilder searchQuery = benefElasticService.buildSearchQuery(request);

    assertNotNull(searchQuery);

    String expectedQuery =
        new String(Files.readAllBytes(Paths.get("src/test/resources/queries/allFieldsQuery.json")));

    assertNotNull(searchQuery.query());
    JSONAssert.assertEquals(expectedQuery, searchQuery.query().toString(), true);
  }

  @Test
  void should_generate_query_for_one_field() throws IOException {
    BenefAdvancedSearchRequest request = getDummyRequest();
    request.setName("Robert");

    SearchSourceBuilder searchQuery = benefElasticService.buildSearchQuery(request);
    assertNotNull(searchQuery);

    String expectedQuery =
        new String(Files.readAllBytes(Paths.get("src/test/resources/queries/oneFieldQuery.json")));

    assertNotNull(searchQuery.query());
    JSONAssert.assertEquals(expectedQuery, searchQuery.query().toString(), true);
  }

  @Test
  void should_generate_sort_when_sort_field_is_not_null() throws IOException {
    BenefAdvancedSearchRequest request = getDummyRequest();
    List<BenefAdvancedSearchRequest.Sort> sorts = new ArrayList<>();

    sorts.add(new BenefAdvancedSearchRequest.Sort("field.name", SortOrder.DESC.toString()));
    sorts.add(
        new BenefAdvancedSearchRequest.Sort(
            "contrats.data.nom.prenom.keyword", SortOrder.DESC.toString()));
    sorts.add(
        new BenefAdvancedSearchRequest.Sort(
            "contrats.data.nom.nomUsage.keyword", SortOrder.DESC.toString()));

    request.setSorts(sorts);

    SearchSourceBuilder searchQuery = benefElasticService.buildSearchQuery(request);
    assertNotNull(searchQuery);

    String expectedSort =
        new String(
            Files.readAllBytes(Paths.get("src/test/resources/queries/sortFieldIsNotNull.json")));

    assertNotNull(searchQuery.sorts());
    JSONAssert.assertEquals(expectedSort, searchQuery.sorts().toString(), true);
  }

  @Test
  void should_not_generate_sort_when_sort_field_is_null() {
    BenefAdvancedSearchRequest request = getDummyRequest();

    SearchSourceBuilder searchQuery = benefElasticService.buildSearchQuery(request);
    assertNotNull(searchQuery);

    assertNull(searchQuery.sorts());
  }

  private BenefAdvancedSearchRequest getDummyRequest() {
    BenefAdvancedSearchRequest request = new BenefAdvancedSearchRequest();
    request.setPage(0);
    request.setPerPage(10);

    return request;
  }
}
