package com.cegedim.next.serviceeligibility.core.model.kafka;

import java.util.List;
import lombok.Data;

@Data
public class Trace extends TraceCommun {
  private List<String> errorMessages;
}
