package com.cegedim.next.serviceeligibility.core.utils.exceptions;

import com.cegedim.next.serviceeligibility.core.services.pojo.ErrorValidationBean;
import java.util.List;
import lombok.Getter;

@Getter
public class ValidationContractException extends RuntimeException {

  private final transient List<ErrorValidationBean> errorValidationBeans;

  public ValidationContractException(
      List<ErrorValidationBean> errorValidationBeans, String message) {
    super(message);
    this.errorValidationBeans = errorValidationBeans;
  }
}
