package com.cegedim.next.serviceeligibility.core.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdresseAvecFixe extends Adresse {
  @Field(order = 11)
  private String fixe;

  public AdresseAvecFixe() {
    /* empty constructor */ }

  public AdresseAvecFixe(AdresseAvecFixe source) {
    super(source);
    this.fixe = source.getFixe();
  }

  public String getTelephone() {
    return StringUtils.defaultIfBlank(super.getTelephone(), fixe);
  }
}
