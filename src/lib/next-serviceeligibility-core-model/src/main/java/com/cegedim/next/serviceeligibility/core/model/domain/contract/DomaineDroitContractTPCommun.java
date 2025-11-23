package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class DomaineDroitContractTPCommun implements GenericDomain<DomaineDroitContractTPCommun> {
  @Field(order = 1)
  private String code;

  @Field(order = 2)
  private String codeExterne;

  @Field(order = 3)
  private String libelle;

  @Field(order = 4)
  private String libelleExterne;

  @Field(order = 5)
  private String codeProfil;

  public DomaineDroitContractTPCommun() {
    /* empty constructor */ }

  public DomaineDroitContractTPCommun(DomaineDroitContractTPCommun source) {
    this.code = source.getCode();
    this.codeExterne = source.getCodeExterne();
    this.libelle = source.getLibelle();
    this.libelleExterne = source.getLibelleExterne();
    this.codeProfil = source.getCodeProfil();
  }

  @Override
  public int compareTo(DomaineDroitContractTPCommun domaineDroitContract) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.code, domaineDroitContract.code);
    compareToBuilder.append(this.codeExterne, domaineDroitContract.codeExterne);
    compareToBuilder.append(this.libelle, domaineDroitContract.libelle);
    compareToBuilder.append(this.libelleExterne, domaineDroitContract.libelleExterne);
    compareToBuilder.append(this.codeProfil, domaineDroitContract.codeProfil);
    return compareToBuilder.toComparison();
  }
}
