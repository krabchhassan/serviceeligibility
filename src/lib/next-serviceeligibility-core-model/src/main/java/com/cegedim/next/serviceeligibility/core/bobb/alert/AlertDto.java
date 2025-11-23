package com.cegedim.next.serviceeligibility.core.bobb.alert;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlertDto {

  private String id;

  private String message;

  private LocalDate deadline;

  private String sourceId;

  private boolean resolved = false;

  private String link;
}
