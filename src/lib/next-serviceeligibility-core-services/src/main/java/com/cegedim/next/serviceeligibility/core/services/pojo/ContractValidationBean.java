package com.cegedim.next.serviceeligibility.core.services.pojo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ContractValidationBean {

  public static final String NON_RENSEIGNE = " non renseigné";
  private static final String NON_RENSEIGNEE = "non renseignée";

  List<ErrorValidationBean> errorValidationBeans = new ArrayList<>();

  private String numeroContrat = NON_RENSEIGNE;
  private String numeroAdherent = NON_RENSEIGNE;
  private String idDeclarant = NON_RENSEIGNEE;
  private String numeroPersonne = NON_RENSEIGNE;
}
