package com.cegedim.next.serviceeligibility.core.bobb;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Versions {
  @Id private String id;
  private Integer number;
  private String label;
  private LocalDateTime creationDate;
  private String createdBy;
  private String status;
  private LocalDateTime purgeDate;
  private String purgedBy;
}
