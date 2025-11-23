package com.cegedim.next.serviceeligibility.core.model.domain.cartepapier;

import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** Classe qui mappe le document ConventionCartePapier */
@EqualsAndHashCode(callSuper = true)
@Data
public class ConventionCartePapier extends Convention {

  private static final long serialVersionUID = 1L;

  private String libelle;
}
