package com.cegedim.next.serviceeligibility.core.bobb;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class WarrantyBobb {
  @Id private String id;
  private LocalDate deadline;
  private String codeInsurer;
  private String codeContractElement;
}
