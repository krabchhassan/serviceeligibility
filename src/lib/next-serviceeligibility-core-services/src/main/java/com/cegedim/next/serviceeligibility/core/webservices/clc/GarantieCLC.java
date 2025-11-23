package com.cegedim.next.serviceeligibility.core.webservices.clc;

import java.util.List;
import lombok.Data;

@Data
public class GarantieCLC {
  private String codeGarantie;
  private List<ProduitCLC> produits;
}
