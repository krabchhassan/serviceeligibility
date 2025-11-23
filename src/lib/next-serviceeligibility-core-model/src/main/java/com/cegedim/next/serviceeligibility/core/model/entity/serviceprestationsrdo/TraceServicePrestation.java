package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import com.cegedim.next.serviceeligibility.core.model.kafka.TraceCommun;
import java.util.List;
import lombok.Data;

@Data
public class TraceServicePrestation extends TraceCommun {
  private List<ErrorData> errorMessages;
}
