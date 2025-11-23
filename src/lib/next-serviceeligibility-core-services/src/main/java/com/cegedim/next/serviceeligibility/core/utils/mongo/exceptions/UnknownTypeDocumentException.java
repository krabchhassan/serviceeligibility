package com.cegedim.next.serviceeligibility.core.utils.mongo.exceptions;

import org.springframework.dao.DataAccessException;

/** Exception technique levée lorsqu'un type n'est pas reconnu comme étant un document. */
public class UnknownTypeDocumentException extends DataAccessException {

  private static final long serialVersionUID = -7462185126247317759L;

  /**
   * Constructor
   *
   * @param msg le message d'erreur.
   * @param parameters les parametres du message d'erreur.
   */
  public UnknownTypeDocumentException(String msg, Object... parameters) {
    super(String.format(msg, parameters));
  }
}
