package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "historiqueExecutions")
@Data
@EqualsAndHashCode(callSuper = false)
public class HistoriqueExecution extends DocumentEntity
    implements GenericDomain<HistoriqueExecution> {
  private static final long serialVersionUID = 1L;

  // Ajout de l'annotation Field car la colonne en base commence par une
  // majuscule
  @Field("Batch")
  private String batch;

  private String codeService;
  private String idDeclarant;
  private String typeConventionnement;
  private String critereSecondaire;
  private String critereSecondaireDetaille;
  private Date dateExecution;

  @Override
  public int compareTo(final HistoriqueExecution historiqueExecution) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.batch, historiqueExecution.batch);
    compareToBuilder.append(this.codeService, historiqueExecution.codeService);
    compareToBuilder.append(this.idDeclarant, historiqueExecution.idDeclarant);
    compareToBuilder.append(this.typeConventionnement, historiqueExecution.typeConventionnement);
    compareToBuilder.append(this.critereSecondaire, historiqueExecution.critereSecondaire);
    compareToBuilder.append(
        this.critereSecondaireDetaille, historiqueExecution.critereSecondaireDetaille);
    compareToBuilder.append(this.dateExecution, historiqueExecution.dateExecution);
    return compareToBuilder.toComparison();
  }
}
