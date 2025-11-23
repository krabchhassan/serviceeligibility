package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.parametragecartetp;

import com.cegedim.next.serviceeligibility.core.model.enumeration.DateRenouvellementCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.DureeValiditeDroitsCarteTP;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import lombok.Data;

@Data
public class ParametrageRenouvellementDto {
  private String debutValidite;
  private DateRenouvellementCarteTP dateRenouvellementCarteTP;
  private String debutEcheance;
  private DureeValiditeDroitsCarteTP dureeValiditeDroitsCarteTP;
  private Integer delaiDeclenchementCarteTP;
  private String dateExecutionBatch;
  private String dateDebutDroitTP;
  private Integer seuilSecurite;
  private ModeDeclenchementCarteTP modeDeclenchement;
  private Integer delaiRenouvellement;
  private String annulDroitsOffline;
}
