package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ParametrageRenouvellement implements Serializable {
  private DateRenouvellementCarteTP dateRenouvellementCarteTP;
  private String debutEcheance;
  private DureeValiditeDroitsCarteTP dureeValiditeDroitsCarteTP;
  private Integer delaiDeclenchementCarteTP;
  private String dateExecutionBatch;
  private String dateDebutDroitTP;
  private Integer seuilSecurite;
  private ModeDeclenchementCarteTP modeDeclenchement;
  private LocalDateTime derniereExecution;

  private Integer
      delaiRenouvellement; // utilisé sur le paramétrage anniversaire manuel : voir BLUE-5476 pour
  // plus
  // d'informations
  private Boolean annulDroitsOffline;

  /**
   * @deprecated
   */
  @Deprecated(forRemoval = true)
  private String dateDeclenchementManuel;
}
