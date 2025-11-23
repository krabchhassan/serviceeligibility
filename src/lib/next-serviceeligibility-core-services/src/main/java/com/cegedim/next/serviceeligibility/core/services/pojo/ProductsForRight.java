package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.Oc;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ConventionnementContrat;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PrioriteDroitContrat;
import java.util.List;
import lombok.Data;

@Data
public class ProductsForRight {

  private String codeGarantie;
  private String codeAssureurGarantie;
  private String dateAdhesionCouverture;
  private String codeProduit;
  private String codeOffre;
  private String codeDomaine;

  private String codeNaturePrestation;

  private PeriodeDroitContractTP periodeDroitContractTP;
  private List<ConventionnementContrat> conventionnementContrats;
  private List<PrioriteDroitContrat> prioriteDroitContrats;

  private Oc oc;

  private String codeCarence;
  private String codeAssureurOrigine;
  private String codeOrigine;
}
