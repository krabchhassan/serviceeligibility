package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration.AttestationDetailDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttestationContractDto implements GenericDto {

  private static final long serialVersionUID = 6580071121938630301L;

  private Boolean isLastCarteDemat;
  private Boolean isCarteDemat;
  private Boolean isCartePapier;
  private String annexe1Carte;
  private String annexe2Carte;
  private String modeleCarte;
  private String codeRenvoi;
  private String codeItelis;
  private String libelleRenvoi;
  private String periodeDebut;
  private String periodeFin;
  private Date dateCreation;
  private List<AttestationDetailDto> details;
}
