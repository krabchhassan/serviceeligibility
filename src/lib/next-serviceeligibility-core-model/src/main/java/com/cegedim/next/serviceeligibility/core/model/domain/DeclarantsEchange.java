package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.List;
import lombok.Data;

/** Classe qui mappe la liste consolidee des declarant-echanges stockes dans chaque declarant. */
@Data
public class DeclarantsEchange implements GenericDomain<DeclarantsEchange> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private List<String> numerosAMCEchanges;

  @Override
  public int compareTo(DeclarantsEchange o) {
    // TODO Auto-generated method stub
    return 0;
  }
}
