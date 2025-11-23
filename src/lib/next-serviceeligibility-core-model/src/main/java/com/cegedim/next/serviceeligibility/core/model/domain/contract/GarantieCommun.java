package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class GarantieCommun implements GenericDomain<GarantieCommun>, Mergeable {
  @Field(order = 1)
  private String codeGarantie;

  @Field(order = 2)
  private String libelleGarantie;

  @Field(order = 3)
  private String codeAssureurGarantie;

  @Field(order = 4)
  private String codeCarence;

  @Field(order = 5)
  private String codeOrigine;

  @Field(order = 6)
  private String codeAssureurOrigine;

  @Field(order = 7)
  private String dateAdhesionCouverture;

  @Override
  public int compareTo(GarantieCommun o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(codeGarantie, o.codeGarantie);
    compareToBuilder.append(libelleGarantie, o.libelleGarantie);
    compareToBuilder.append(codeAssureurGarantie, o.codeAssureurGarantie);
    compareToBuilder.append(codeCarence, o.codeCarence);
    compareToBuilder.append(codeOrigine, o.codeOrigine);
    compareToBuilder.append(codeAssureurOrigine, o.codeAssureurOrigine);
    compareToBuilder.append(this.dateAdhesionCouverture, o.dateAdhesionCouverture);
    return compareToBuilder.toComparison();
  }

  public GarantieCommun(GarantieCommun source) {
    this.codeGarantie = source.codeGarantie;
    this.libelleGarantie = source.libelleGarantie;
    this.codeAssureurGarantie = source.codeAssureurGarantie;
    this.codeCarence = source.codeCarence;
    this.codeOrigine = source.codeOrigine;
    this.codeAssureurOrigine = source.codeAssureurOrigine;
    this.dateAdhesionCouverture = source.dateAdhesionCouverture;
  }

  @Override
  public String mergeKey() {
    return codeGarantie + codeAssureurGarantie;
  }

  @Override
  public String conflictKey() {
    return "";
  }
}
