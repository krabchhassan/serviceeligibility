package com.cegedim.next.serviceeligibility.core.model.domain.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@NoArgsConstructor
public class ProduitCommun implements GenericDomain<ProduitCommun>, Mergeable {
  @Field(order = 1)
  private String codeProduit;

  @Field(order = 2)
  private String libelleProduit;

  @Field(order = 3)
  private String codeExterneProduit;

  @Field(order = 4)
  private String libelleExterneProduit;

  @Field(order = 5)
  private String codeOffre;

  @Field(order = 6)
  private String codeOc;

  @Override
  public int compareTo(ProduitCommun o) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(codeProduit, o.codeProduit);
    compareToBuilder.append(libelleProduit, o.libelleProduit);
    compareToBuilder.append(codeExterneProduit, o.codeExterneProduit);
    compareToBuilder.append(libelleExterneProduit, o.libelleExterneProduit);
    compareToBuilder.append(this.codeOffre, o.codeOffre);
    compareToBuilder.append(this.codeOc, o.codeOc);
    return compareToBuilder.toComparison();
  }

  public ProduitCommun(ProduitCommun source) {
    this.codeProduit = source.codeProduit;
    this.libelleProduit = source.libelleProduit;
    this.codeExterneProduit = source.codeExterneProduit;
    this.libelleExterneProduit = source.libelleExterneProduit;
    this.codeOffre = source.getCodeOffre();
    this.codeOc = source.getCodeOc();
  }

  @Override
  public String mergeKey() {
    return codeProduit + codeExterneProduit;
  }

  @Override
  public String conflictKey() {
    return "";
  }
}
