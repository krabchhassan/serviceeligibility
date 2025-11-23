package com.cegedim.next.serviceeligibility.core.model.kafka;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Nir {
  @NotBlank(message = "Le code du NIR est obligatoire")
  private String code;

  @NotBlank(message = "La clé du NIR est obligatoire")
  @Min(value = 1, message = "La clé du NIR doit être comprise entre 1 et 99")
  @Max(value = 99, message = "La clé du NIR doit être comprise entre 1 et 99")
  @EqualsAndHashCode.Exclude
  private String cle;

  public Nir(
      @NotBlank(message = "Le code du NIR est obligatoire") String code,
      @NotBlank(message = "La clé du NIR est obligatoire")
          @Min(value = 1, message = "La clé du NIR doit être comprise entre 1 et 99")
          @Max(value = 99, message = "La clé du NIR doit être comprise entre 1 et 99")
          String cle) {
    this.code = code;
    this.cle = cle;
  }

  public Nir() {}

  public void setCode(String newCode) {
    if (newCode != null) {
      this.code = newCode;
    }
  }

  public void setCle(String newCle) {
    if (newCle != null) {
      this.cle = newCle;
    }
  }
}
