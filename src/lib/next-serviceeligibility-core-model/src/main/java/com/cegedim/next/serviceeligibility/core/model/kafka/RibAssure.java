package com.cegedim.next.serviceeligibility.core.model.kafka;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class RibAssure implements GenericDomain<RibAssure> {
  @NotBlank(message = "Le BIC est obligatoire")
  private String bic;

  @NotBlank(message = "L'IBAN est obligatoire")
  private String iban;

  public RibAssure(String bic, String iban) {
    this.bic = bic;
    this.iban = iban;
  }

  public RibAssure() {}

  public RibAssure(RibAssure source) {
    this.bic = source.getBic();
    this.iban = source.getIban();
  }

  public void setBic(String newBic) {
    if (newBic != null) {
      this.bic = newBic;
    }
  }

  public void setIban(String newIban) {
    if (newIban != null) {
      this.iban = newIban;
    }
  }

  @Override
  public int compareTo(RibAssure ribAssure) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.bic, ribAssure.bic);
    compareToBuilder.append(this.iban, ribAssure.iban);
    return compareToBuilder.toComparison();
  }
}
