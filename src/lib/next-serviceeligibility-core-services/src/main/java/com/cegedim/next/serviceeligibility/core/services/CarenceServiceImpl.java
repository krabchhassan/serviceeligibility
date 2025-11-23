package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GenericNotFoundException;
import io.micrometer.tracing.annotation.ContinueSpan;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class CarenceServiceImpl implements CarenceService {

  @Autowired private SettingsUIService settingsUIService;

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

      JSONArray suiResponse =
          settingsUIService.getWaitingPeriod(issuerCompany, offerCode, productCode);

      // For each object in SettingsUI's response...
      for (int i = 0; i < suiResponse.length(); i++) {
        JSONObject parametrageJson = suiResponse.getJSONObject(i);
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
    } catch (GenericNotFoundException e) {
      // on a découpé en 2 catchs parce que bizarrement les tests de consumer worker
      // ne passe pas (nested exception is java.lang.VerifyError: Stack map does not
      // match the one at exception handler 121)
      throw new CarenceException(
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.SERVICE_SETTINGS_UI_NOT_RESPONDING, e.getMessage()));
    } catch (JSONException e) {
      throw new CarenceException(
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.SERVICE_SETTINGS_UI_NOT_RESPONDING, e.getMessage()));
    }

    return parametrageCarenceList;
  }
}
