package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp.Produit;
import java.util.List;
import lombok.Data;

@Data
public class OffreProduits {
  private String id;
  private String code;
  private String libelle;
  private List<Produit> produits;

  // task/blue-4636 : carences
  private String codeCarence;
  private String codeOrigine;
  private String codeAssureurOrigine;

  // BLUE-4848 - PAU TP ONLINE : Restitution des contrats - limiter les natures de
  // prestations liées aux domaines d'interrogation
  private List<BenefitNature> benefitNatures;
}
