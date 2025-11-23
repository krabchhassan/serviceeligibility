package com.cegedim.next.serviceeligibility.core.model.crex;

import com.cegedim.common.omu.helper.parameters.ParameterValue;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class CompteRenduExtractRecipient implements CompteRenduGeneric {
  private long contratsExtraits;
  private long destinatairesRelevePrestation;
  private long destinatairesPaiement;
  private String nomFichier;

  public Map<String, ParameterValue> asMap() {
    Map<String, ParameterValue> map = new HashMap<>();

    map.put("contratsExtraits", ParameterValue.valueOf((int) this.contratsExtraits));
    map.put(
        "destinatairesRelevePrestation",
        ParameterValue.valueOf((int) this.destinatairesRelevePrestation));
    map.put("destinatairesPaiement", ParameterValue.valueOf((int) this.destinatairesPaiement));
    map.put("nomFichier", ParameterValue.valueOf(this.nomFichier));

    return map;
  }

  public void addContratsExtraits(long contratsExtraits) {
    this.contratsExtraits += contratsExtraits;
  }

  public void addDestinatairesRelevePrestation(long destinataireRelevePrestation) {
    this.destinatairesRelevePrestation += destinataireRelevePrestation;
  }

  public void addDestinatairesPaiement(long destinatairePaiement) {
    this.destinatairesPaiement += destinatairePaiement;
  }
}
