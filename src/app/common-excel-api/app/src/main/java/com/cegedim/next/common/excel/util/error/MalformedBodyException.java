package com.cegedim.next.common.excel.util.error;

import java.text.ParseException;
import java.util.List;
import org.springframework.validation.ObjectError;

public class MalformedBodyException extends RuntimeException {

  public MalformedBodyException(List<ObjectError> allErrors) {
    super(String.valueOf(allErrors));
  }

  public MalformedBodyException(String msg) {
    super(msg);
  }

  public MalformedBodyException(String integerRangeValue, ParseException e) {
    super(integerRangeValue, e);
  }
}
