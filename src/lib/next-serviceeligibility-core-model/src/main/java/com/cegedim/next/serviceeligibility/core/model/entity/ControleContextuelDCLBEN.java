package com.cegedim.next.serviceeligibility.core.model.entity;

import java.util.List;
import lombok.Data;

@Data
public class ControleContextuelDCLBEN {
  private List<DCLBENChamp> debutFichier;
  private List<DCLBENChamp> beneficiaire;
  private List<DCLBENChamp> finFichier;
}
