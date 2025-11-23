package com.cegedim.next.serviceeligibility.core.model.entity;

import java.util.List;
import lombok.Data;

@Data
public class DCLBENChamp {
  private String libelle;
  private List<DCLBENVersion> versions;
}
