package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class ErrorData {
  private String numeroPersonne;
  private List<String> messages = new ArrayList<>();
}
