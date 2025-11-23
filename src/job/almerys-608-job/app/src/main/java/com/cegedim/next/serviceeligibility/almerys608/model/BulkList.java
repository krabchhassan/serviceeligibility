package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import java.util.*;
import lombok.Data;

@Data
public class BulkList<T extends BulkObject> {

  private Map<String, String> tempTables;
  private Map<String, List<T>> bulks = new HashMap<>();

  public void addMulti(String key, Collection<T> bulk) {
    List<T> copy = new ArrayList<>(bulk);
    if (!bulks.containsKey(key)) {
      bulks.put(key, copy);
    } else {
      bulks.get(key).addAll(copy);
    }
  }

  public void addOne(String key, T objet) {
    addMulti(key, List.of(objet));
  }
}
