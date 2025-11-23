package com.cegedim.next.serviceeligibility.core.services.pojo;

import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.Produit;
import com.cegedim.next.serviceeligibility.core.model.domain.pw.DetailsByDomain;
import java.util.List;
import lombok.Data;

@Data
public class GeneratedDomains {

  private String domainCode;

  private Produit product;

  private String offerCode;

  private DetailsByDomain detailsByDomain;

  private List<ParametrageCarence> waitingPeriodSettings;

  private String codeCarence;

  private String codeAssureurOrigine;

  private String codeOrigine;

  private String codeAssureur;

  private String code;

  private String dateDebutCarence;

  private String dateFinCarence;

  /** 0 standard 1 carence 2 remplacement */
  private int stateProduct;
}
