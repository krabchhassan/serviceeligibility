package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttestationsContractDto implements GenericDto {

  private static final long serialVersionUID = 6580071121938630301L;

  private List<AttestationContractDto> certifications;
  private boolean searchNextCertifications;
}
