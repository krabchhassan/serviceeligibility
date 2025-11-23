package com.cegedim.next.serviceeligibility.core.soap.consultation.exception;

public class ExceptionMetier extends RuntimeException {

  /** generated serial version UID. */
  private static final long serialVersionUID = 5707037679756244985L;

  /** Objet contenant les elements de reponses. */
  private final CodeReponse codeReponse;

  /** Parametre du message d'exception. */
  private final transient Object[] params;

  /**
   * Constructeur public d'une exception metier.
   *
   * @param theCodeReponse signification fonctionnel de l'exception
   */
  public ExceptionMetier(final CodeReponse theCodeReponse) {
    super();
    codeReponse = theCodeReponse;
    params = null;
  }

  /**
   * Constructeur public d'une exception metier.
   *
   * @param theCodeReponse signification fonctionnel de l'exception
   * @param theParams parametre de l'exception
   */
  public ExceptionMetier(final CodeReponse theCodeReponse, final Object... theParams) {
    super();
    codeReponse = theCodeReponse;
    params = theParams;
  }

  /**
   * Getter de l'attribut codeReponse.
   *
   * @return la valeur de l'attribut codeReponse
   */
  public CodeReponse getCodeReponse() {
    return codeReponse;
  }

  /**
   * Getter de l'attribut params.
   *
   * @return la valeur de l'attribut params
   */
  public Object[] getParams() {
    return params;
  }
}
