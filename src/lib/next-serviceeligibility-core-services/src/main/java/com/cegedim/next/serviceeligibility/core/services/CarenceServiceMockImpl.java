package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CarenceServiceMockImpl implements CarenceService {
  private final Logger logger = LoggerFactory.getLogger(CarenceServiceMockImpl.class);

  /**
   * Appelle l'atelier produit pour récupérer la liste des paramétrages des carences, consolide
   * cette liste en y ajoutant le codeOffre et le codeProduit, puis renvoie cette liste.
   *
   * @param issuerCompany code AMC
   * @param offerCode code offre
   * @param productCode code produit
   * @return Liste des parametrages consolidés
   */
  @ContinueSpan(log = "getParametragesCarence")
  public List<ParametrageCarence> getParametragesCarence(
      String issuerCompany, String offerCode, String productCode) throws CarenceException {
    List<ParametrageCarence> parametrageCarenceList = new ArrayList<>();
    try {

      JSONObject productWorkshopResponse2;
      URL resource = this.getClass().getClassLoader().getResource("carences.json");
      Map<String, String> env = new HashMap<>();
      env.put("create", "true");
      FileSystem zipfs = FileSystems.newFileSystem(resource.toURI(), env);
      Path path = Paths.get(resource.toURI());
      String content = new String(Files.readAllBytes(path));
      productWorkshopResponse2 = new JSONObject(content);
      JSONArray pwResponse = productWorkshopResponse2.getJSONArray("carences");

      // For each object in ProductWorkshop's response...
      for (int i = 0; i < pwResponse.length(); i++) {
        JSONObject parametrageJson = pwResponse.getJSONObject(i);
        ParametrageCarence parametrageCarence =
            new ParametrageCarence(
                offerCode,
                productCode,
                parametrageJson.getString(BENEFIT_TYPE_FIELD),
                parametrageJson.getString(WAITING_CODE_FIELD),
                parametrageJson.getString(START_DATE_FIELD),
                parametrageJson.isNull(END_DATE_FIELD)
                    ? null
                    : parametrageJson.getString(END_DATE_FIELD));
        parametrageCarenceList.add(parametrageCarence);
      }
      zipfs.close();
    } catch (IOException e) {
      String message = "Problème d ouverture du fichier carences.json";
      logger.error(e.getMessage(), e);
      throw new GenericNotFoundException(message);
    } catch (JSONException | GenericNotFoundException e) {
      String message = String.format("Le service Settings UI ne répond pas : %s", e.getMessage());
      logger.error(message, e);
      throw new GenericNotFoundException(message);
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }

    return parametrageCarenceList;
  }
}
