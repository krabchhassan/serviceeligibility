package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import static com.cegedim.next.serviceeligibility.core.utils.Util.getFullLibelle;

import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDematExceptionCode;
import lombok.Getter;

public class CarteDematException extends RuntimeException {
  @Getter private final CarteDematExceptionCode exceptionCode;

  @Getter private final String commentaire;

  public CarteDematException(CarteDematExceptionCode exceptionCode, String commentaire) {
    super(getFullLibelle(exceptionCode.getLibelle(), commentaire));
    this.exceptionCode = exceptionCode;
    this.commentaire = commentaire;
  }

  public CarteDematException(CarteDematExceptionCode exceptionCode) {
    super(exceptionCode.getLibelle());
    this.exceptionCode = exceptionCode;
    this.commentaire = null;
  }
}
