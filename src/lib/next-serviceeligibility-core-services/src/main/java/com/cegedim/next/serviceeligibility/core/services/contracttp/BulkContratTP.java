package com.cegedim.next.serviceeligibility.core.services.contracttp;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import java.util.*;

public class BulkContratTP {

  public int compteur;

  public Set<String> contractIds = new HashSet<>();

  public Set<ContractTP> toInsert = new HashSet<>();
  public Set<ContractTP> toDelete = new HashSet<>();
  public Set<ContractTP> toUpdate = new HashSet<>();

  public void reinit() {
    compteur = 0;
    contractIds = new HashSet<>();
    toInsert = new HashSet<>();
    toDelete = new HashSet<>();
    toUpdate = new HashSet<>();
  }
}
