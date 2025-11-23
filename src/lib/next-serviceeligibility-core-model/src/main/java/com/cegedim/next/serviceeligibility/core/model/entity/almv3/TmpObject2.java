package com.cegedim.next.serviceeligibility.core.model.entity.almv3;

import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
public class TmpObject2 extends DocumentEntity implements GenericDomain<TmpObject2> {
  private String referenceExterne;
  private String codeEtat;
  private Date effetDebut;
  private String idDeclarant;
  private boolean isCarteTPaEditer;
  private Date dateCreation;
  private String userCreation;
  private Date dateModification;
  private String userModification;
  private BeneficiaireAlmerys beneficiaire;
  private Contrat contrat;
  List<String> infosCarteTP = new ArrayList<>();
  private DomaineDroitAlmerys domaineDroit;
  private List<String> idDeclarations;
  private boolean flag;
  private String origineDeclaration;

  @Override
  public int compareTo(TmpObject2 tmpObject2) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.referenceExterne, tmpObject2.referenceExterne);
    compareToBuilder.append(this.codeEtat, tmpObject2.codeEtat);
    compareToBuilder.append(this.effetDebut, tmpObject2.effetDebut);
    compareToBuilder.append(this.idDeclarant, tmpObject2.idDeclarant);
    compareToBuilder.append(this.isCarteTPaEditer, tmpObject2.isCarteTPaEditer);
    compareToBuilder.append(this.dateCreation, tmpObject2.dateCreation);
    compareToBuilder.append(this.userCreation, tmpObject2.userCreation);
    compareToBuilder.append(this.dateModification, tmpObject2.dateModification);
    compareToBuilder.append(this.userModification, tmpObject2.userModification);
    compareToBuilder.append(this.beneficiaire, tmpObject2.beneficiaire);
    compareToBuilder.append(this.contrat, tmpObject2.contrat);
    compareToBuilder.append(this.domaineDroit, tmpObject2.domaineDroit);
    compareToBuilder.append(this.origineDeclaration, tmpObject2.origineDeclaration);
    return compareToBuilder.toComparison();
  }
}
