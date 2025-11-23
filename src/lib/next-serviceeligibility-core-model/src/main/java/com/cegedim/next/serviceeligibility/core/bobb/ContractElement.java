package com.cegedim.next.serviceeligibility.core.bobb;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class ContractElement implements Serializable {
  @Id private String id;

  private String codeAMC;

  @NotNull @NotEmpty private String codeInsurer;

  @NotNull @NotEmpty private String codeContractElement;

  private String label;

  private String alertId;

  private LocalDate deadline;

  private List<ProductElement> productElements = new ArrayList<>();

  private boolean ignored = false;

  private String status;
  private String origine;
  private String user;
  private String versionId;
}
