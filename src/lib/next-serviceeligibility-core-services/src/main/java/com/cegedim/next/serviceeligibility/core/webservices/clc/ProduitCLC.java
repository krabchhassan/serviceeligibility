package com.cegedim.next.serviceeligibility.core.webservices.clc;

import java.util.List;
import lombok.Data;

@Data
public class ProduitCLC {
  private String codeProduit;
  private List<ReferenceCouvertureCLC> referencesCouverture;
}
