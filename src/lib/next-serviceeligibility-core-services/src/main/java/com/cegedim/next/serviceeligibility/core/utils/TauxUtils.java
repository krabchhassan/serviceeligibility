package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TauxUtils {
  public static String getTauxRemboursement(
      List<String> listTaux, String unite, ModeAssemblage modeAssemblage) {
    if (TauxConstants.U_TEXT.equals(unite)) {
      for (String taux : listTaux) {
        if (!TauxConstants.T_NON_COUVERT.equals(taux)) {
          return taux;
        }
      }
      return TauxConstants.T_NON_COUVERT;
    }
    float newTaux = 0f;
    for (String taux : listTaux) {
      if (ModeAssemblage.DELTA.equals(modeAssemblage)) {
        // Les taux ne sont ranges par ordre de priorite, ici le dernier taux est donc
        // celui du
        // mode delta
        newTaux = NumberUtils.isParsable(taux) ? Float.parseFloat(taux) : newTaux;
      } else if (NumberUtils.isParsable(taux)) {
        float fTaux = Float.parseFloat(taux);
        newTaux = newTaux + fTaux;
      }
    }
    return formatTauxFloat(newTaux);
  }

  private static String formatTauxFloat(float taux) {
    if (taux % 1.0 != 0) {
      return String.format("%s", taux);
    }
    return String.format("%.0f", taux);
  }
}
