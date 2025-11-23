package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeclarationConsultationHistoryDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String user;
  private Date dateConsultation;

  /* CLES ETRANGERES */
  private DeclarantDto declarantAmc;
}
