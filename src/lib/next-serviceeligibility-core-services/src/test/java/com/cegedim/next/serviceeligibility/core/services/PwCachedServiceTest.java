package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.config.TestConfiguration;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = TestConfiguration.class)
class PwCachedServiceTest {

  @Autowired PwCachedService pwCachedService;

  @Test
  void shouldReturnOneElementOnGetDataInCache() {
    Map<String, JSONObject> dataInCache = new HashMap<>();
    JSONObject offerVersion = new JSONObject();
    offerVersion.put(Constants.PW_OFFER_CODE, "OFFRE");
    dataInCache.put("2021-01-01", offerVersion);
    JSONArray retour = pwCachedService.getDataInCache("2022-01-01", "2023-01-01", dataInCache);
    Assertions.assertEquals(1, retour.length());
    Assertions.assertEquals(offerVersion, retour.get(0));
  }

  @Test
  void shouldNotReturnOneElementOnGetDataInCache() {
    Map<String, JSONObject> dataInCache = new HashMap<>();
    JSONObject offerVersion = new JSONObject();
    offerVersion.put(Constants.PW_OFFER_CODE, "OFFRE");
    dataInCache.put("2021-01-01", offerVersion);
    JSONArray retour = pwCachedService.getDataInCache("2020-01-01", "2023-01-01", dataInCache);
    Assertions.assertEquals(0, retour.length());
  }

  @Test
  void shouldNotReturnOneElementOnGetDataInCache2() {
    Map<String, JSONObject> dataInCache = new HashMap<>();
    JSONObject offerVersion = new JSONObject();
    offerVersion.put(Constants.PW_OFFER_CODE, "OFFRE");
    dataInCache.put("2022-01-01/2024-01-01", offerVersion);
    JSONArray retour = pwCachedService.getDataInCache("2020-01-01", "2023-01-01", dataInCache);
    Assertions.assertEquals(0, retour.length());
  }

  @Test
  void shouldReturnTwoElementOnGetDoubleDataInCache() {
    Map<String, JSONObject> dataInCache = new HashMap<>();
    JSONObject offerVersion = new JSONObject();
    offerVersion.put(Constants.PW_OFFER_CODE, "OFFRE");
    dataInCache.put("2021-01-01/2023-01-01", offerVersion);
    JSONObject offerVersion2 = new JSONObject();
    offerVersion2.put(Constants.PW_OFFER_CODE, "OFFRE2");
    dataInCache.put("2023-01-01", offerVersion2);
    JSONArray retour = pwCachedService.getDataInCache("2022-01-01", "2023-01-01", dataInCache);
    Assertions.assertEquals(2, retour.length());
    Assertions.assertEquals(offerVersion, retour.get(0));
    Assertions.assertEquals(offerVersion2, retour.get(1));
  }

  @Test
  void shouldReturnOneElementOnGetDoubleDataInCache() {
    Map<String, JSONObject> dataInCache = new HashMap<>();
    JSONObject offerVersion = new JSONObject();
    offerVersion.put(Constants.PW_OFFER_CODE, "OFFRE");
    dataInCache.put("2021-01-01/2023-06-01", offerVersion);
    JSONObject offerVersion2 = new JSONObject();
    offerVersion2.put(Constants.PW_OFFER_CODE, "OFFRE2");
    dataInCache.put("2023-06-02", offerVersion2);
    JSONArray retour = pwCachedService.getDataInCache("2022-01-01", "2023-01-01", dataInCache);
    Assertions.assertEquals(1, retour.length());
    Assertions.assertEquals(offerVersion, retour.get(0));
  }

  @Test
  void shouldNotReturnOneElementOnGetDoubleDataInCache() {
    Map<String, JSONObject> dataInCache = new HashMap<>();
    JSONObject offerVersion = new JSONObject();
    offerVersion.put(Constants.PW_OFFER_CODE, "OFFRE");
    dataInCache.put("2021-01-01/2023-06-01", offerVersion);
    JSONObject offerVersion2 = new JSONObject();
    offerVersion2.put(Constants.PW_OFFER_CODE, "OFFRE2");
    dataInCache.put("2023-06-02", offerVersion2);
    JSONArray retour = pwCachedService.getDataInCache("2020-01-01", "2023-01-01", dataInCache);
    Assertions.assertEquals(0, retour.length());
  }
}
