package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.Contrat;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.BeneficiaireAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.declarationsconsolideesalmerys.DomaineDroitAlmerys;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "declarationsConsolideesAlmerys")
public class DeclarationsConsolideesAlmerys extends DocumentEntity
    implements GenericDomain<DeclarationsConsolideesAlmerys> {
  String referenceExterne;
  String codeEtat;
  Date effetDebut;
  String idDeclarant;
  boolean isCarteTPaEditer;
  BeneficiaireAlmerys beneficiaire;
  Contrat contrat;
  List<String> infosCarteTP = new ArrayList<>();
  DomaineDroitAlmerys domaineDroit;
  String codeService;
  List<String> idDeclarations;
  Date dateConsolidation;
  String AMC_contrat_CSD; // NOSONAR
  String origineDeclaration;

  @Override
  public int compareTo(@NotNull DeclarationsConsolideesAlmerys declaration) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.referenceExterne, declaration.referenceExterne);
    compareToBuilder.append(this.codeEtat, declaration.codeEtat);
    compareToBuilder.append(this.effetDebut, declaration.effetDebut);
    compareToBuilder.append(this.idDeclarant, declaration.idDeclarant);
    compareToBuilder.append(this.isCarteTPaEditer, declaration.isCarteTPaEditer);
    compareToBuilder.append(this.beneficiaire, declaration.beneficiaire);
    compareToBuilder.append(this.contrat, declaration.contrat);
    compareToBuilder.append(this.domaineDroit, declaration.domaineDroit);
    compareToBuilder.append(this.codeService, declaration.codeService);
    compareToBuilder.append(this.idDeclarations, declaration.idDeclarations);
    compareToBuilder.append(this.dateConsolidation, declaration.dateConsolidation);
    compareToBuilder.append(this.AMC_contrat_CSD, declaration.AMC_contrat_CSD);
    compareToBuilder.append(this.origineDeclaration, declaration.origineDeclaration);
    return compareToBuilder.toComparison();
  }
}
