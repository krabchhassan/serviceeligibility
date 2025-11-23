package com.cegedim.next.serviceeligibility.core.services.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorValidationBean {
  private String error;
  private String level;
  private String personNumber;
}
