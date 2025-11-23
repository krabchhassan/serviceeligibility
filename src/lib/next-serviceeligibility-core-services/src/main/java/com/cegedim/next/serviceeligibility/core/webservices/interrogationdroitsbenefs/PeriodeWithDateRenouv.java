package com.cegedim.next.serviceeligibility.core.webservices.interrogationdroitsbenefs;

import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import lombok.Data;

@Data
public class PeriodeWithDateRenouv extends Periode {
  private String dateRenouvellement;
}
