package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.droitstp;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Data;

@Data
public class BeneficiaireDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */

  private String dateNaissance;

  private String rangNaissance;

  private String nirBeneficiaire;

  private String cleNirBeneficiaire;

  private String nirOd1;

  private String cleNirOd1;

  private String nirOd2;

  private String cleNirOd2;

  private String numeroPersonne;

  private List<AffiliationDto> affiliations;

  private ContactDto contact;
}
