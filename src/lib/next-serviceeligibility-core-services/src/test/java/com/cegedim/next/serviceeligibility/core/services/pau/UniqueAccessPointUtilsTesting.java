package com.cegedim.next.serviceeligibility.core.services.pau;

import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.kafka.Amc;
import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.BenefAIV5;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.OcService;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.RestConnector;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public abstract class UniqueAccessPointUtilsTesting {

  @Autowired protected MongoTemplate mongoTemplate;

  @Autowired protected RestConnector restConnector;

  @MockBean OcService ocService;

  protected void mockOfferStructurePW(
      final String fileName,
      final String startDate,
      final String endDate,
      String context,
      String productCode)
      throws IOException {
    MultiValueMap<String, String> urlVariables = new LinkedMultiValueMap<>();

    String url = "http://next-engine-core-api:8080/v4/offerStructure";
    urlVariables.put(Constants.PW_PARAM_ISSUER_COMPANY, List.of("codeOc"));
    urlVariables.put(Constants.PW_PARAM_PRODUCT_CODE, List.of(productCode));
    urlVariables.put(Constants.PW_PARAM_DATE, List.of(startDate));
    urlVariables.put(Constants.PW_PARAM_DATE_END, List.of(endDate));
    urlVariables.put(Constants.PW_CONTEXT, List.of(context));
    JSONObject jsonObject = parseJSONFile(fileName);
    JSONArray retourPW = jsonObject.getJSONArray("data");
    Mockito.when(this.restConnector.fetchArray(url, urlVariables)).thenReturn(retourPW);
  }

  void mockOc() {
    Oc oc = new Oc();
    oc.setCode("codeOc");
    oc.setLibelle("libelleOc");

    Mockito.when(ocService.getOC(Mockito.anyString())).thenReturn(oc);
  }

  Oc returnOc() {
    final Oc oc = new Oc();
    oc.setCode("codeOc");
    oc.setLibelle("libelleOc");
    return oc;
  }

  void mockListBenef() {
    final List<BenefAIV5> listeBenef = new ArrayList<>();
    final BenefAIV5 benefAIV5 = new BenefAIV5();
    final IdentiteContrat identite = new IdentiteContrat();
    identite.setNumeroPersonne("1213");
    final Amc amc = new Amc();
    amc.setIdDeclarant("1");
    benefAIV5.setAmc(amc);
    benefAIV5.setIdentite(identite);
    listeBenef.add(benefAIV5);

    Mockito.doReturn(new AggregationResults<>(listeBenef, new Document()))
        .when(this.mongoTemplate)
        .aggregate(Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.any());
  }

  void mockListContract(Collection<ContractTP> contractTPList) {
    Mockito.doReturn(contractTPList)
        .when(this.mongoTemplate)
        .find(Mockito.any(), Mockito.eq(ContractTP.class), Mockito.anyString());
  }

  void mockListContratAIV5(Collection<ContratAIV6> contractList) {
    Mockito.doReturn(contractList)
        .when(this.mongoTemplate)
        .find(Mockito.any(), Mockito.eq(ContratAIV6.class), Mockito.anyString());
  }

  void mockListBenefWithContract(final AggregationResults<ContractTP> otherReturn) {
    this.mockListBenefWithContract(otherReturn, "123456");
  }

  void mockListBenefWithContract(
      final AggregationResults<ContractTP> otherReturn, final String idDeclarant) {
    final List<BenefAIV5> listeBenef = new ArrayList<>();
    final BenefAIV5 benefAIV5 = new BenefAIV5();
    final IdentiteContrat identite = getIdentiteContrat();
    benefAIV5.setIdentite(identite);
    final Amc amc = new Amc();
    amc.setIdDeclarant(idDeclarant);
    benefAIV5.setAmc(amc);
    listeBenef.add(benefAIV5);

    Mockito.doReturn(new AggregationResults<>(listeBenef, new Document()), otherReturn)
        .when(this.mongoTemplate)
        .aggregate(Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.any());
    this.mockListContract(otherReturn.getMappedResults());
  }

  void mock2BenefResearchWithContract(final AggregationResults<ContractTP> otherReturn) {
    final List<BenefAIV5> listeBenef = new ArrayList<>();
    final BenefAIV5 benefAIV5 = new BenefAIV5();
    final IdentiteContrat identite = getIdentiteContrat();
    benefAIV5.setIdentite(identite);
    final Amc amc = new Amc();
    amc.setIdDeclarant("123456");
    benefAIV5.setAmc(amc);
    listeBenef.add(benefAIV5);

    Mockito.doReturn(
            new AggregationResults<>(new ArrayList<>(), new Document()),
            new AggregationResults<>(listeBenef, new Document()),
            otherReturn)
        .when(this.mongoTemplate)
        .aggregate(Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.any());
    this.mockListContract(otherReturn.getMappedResults());
  }

  void mock3BenefResearchWithContract(final AggregationResults<ContractTP> otherReturn) {
    final List<BenefAIV5> listeBenef = new ArrayList<>();
    final BenefAIV5 benefAIV5 = new BenefAIV5();
    final IdentiteContrat identite = getIdentiteContrat();
    benefAIV5.setIdentite(identite);
    final Amc amc = new Amc();
    amc.setIdDeclarant("123456");
    benefAIV5.setAmc(amc);
    listeBenef.add(benefAIV5);

    Mockito.doReturn(
            new AggregationResults<>(new ArrayList<>(), new Document()),
            new AggregationResults<>(new ArrayList<>(), new Document()),
            new AggregationResults<>(listeBenef, new Document()),
            otherReturn)
        .when(this.mongoTemplate)
        .aggregate(Mockito.any(Aggregation.class), Mockito.anyString(), Mockito.any());
    this.mockListContract(otherReturn.getMappedResults());
  }

  private static IdentiteContrat getIdentiteContrat() {
    final IdentiteContrat identite = new IdentiteContrat();
    identite.setNumeroPersonne("1213");
    Nir nir = new Nir();
    nir.setCode("12345678910");
    nir.setCle("12");
    identite.setDateNaissance("19880131");
    identite.setRangNaissance("1");
    identite.setNir(nir);
    return identite;
  }

  public static JSONObject parseJSONFile(final String filename) throws JSONException, IOException {
    final String content = new String(Files.readAllBytes(Paths.get(filename)));
    return new JSONObject(content);
  }
}
