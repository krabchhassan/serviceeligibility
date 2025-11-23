package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.Conventionnement;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Prestation;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeAssemblage;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class DomaineDroitBuffer {
  private DomaineDroit produit;
  private List<String> unitesDomaine = new ArrayList<>();
  private List<String> tauxDomaine = new ArrayList<>();
  private List<String> prioritesDomaine = new ArrayList<>();
  private List<Conventionnement> conventionnementsDomaine = new ArrayList<>();
  private List<Prestation> prestations = new ArrayList<>();
  private ModeAssemblage modeAssemblagePlusPrioritaire;

  public DomaineDroitBuffer(DomaineDroit produit) {
    this.produit = produit;
  }
}
