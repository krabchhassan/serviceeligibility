package com.cegedim.next.serviceeligibility.core.referential;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Referential implements Serializable {
  private String code;
  private List<String> authorizedValues;
}
