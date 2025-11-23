package com.cegedim.next.serviceeligibility.core.bdd.webservice;

import com.cegedimassurances.norme.base_de_droit.TypeHistoriquePeriodeDroit;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TypeHistoriquePeriodeDroitV4 extends TypeHistoriquePeriodeDroit {
  private Date dateRenouvellement;
}
