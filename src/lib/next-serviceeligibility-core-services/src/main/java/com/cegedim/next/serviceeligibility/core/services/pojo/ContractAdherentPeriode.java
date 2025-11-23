package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import lombok.Data;

@Data
public class ContractAdherentPeriode {

  String numeroAdherent;
  String numeroContrat;
  String idDeclarant;

  PeriodeDroitContractTP periodeDroitContractTP;
}
