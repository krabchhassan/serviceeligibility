package com.cegedim.next.serviceeligibility.core.model.domain.contract;

public interface Mergeable {
  /** Clé qui permet de dire si deux objets peuvent être merger */
  String mergeKey();

  /** Clé qui permet de dire si deux objets rentrent en conflit */
  String conflictKey();
}
