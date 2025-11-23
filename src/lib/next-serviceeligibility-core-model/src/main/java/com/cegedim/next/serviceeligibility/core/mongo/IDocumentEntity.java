package com.cegedim.next.serviceeligibility.core.mongo;

import java.io.Serializable;

/** Interface de la classe la plus haute représentant un document */
public interface IDocumentEntity extends Serializable {

  public String get_id();

  public void set_id(String id);
}
