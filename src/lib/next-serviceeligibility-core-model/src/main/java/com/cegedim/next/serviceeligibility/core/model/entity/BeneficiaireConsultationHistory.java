package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Classe qui enregistre l'historique des consultations des beneficiaires dans la base de donnees.
 */
@Document(collection = "beneficiaireConsultationHistory")
public class BeneficiaireConsultationHistory extends DocumentEntity
    implements GenericDomain<BeneficiaireConsultationHistory> {

  private static final long serialVersionUID = 1L;

  @Getter @Setter private String idElasticBeneficiaire;

  @Getter @Setter private boolean externalOrigin;

  @Getter @Setter private String user;

  @Getter @Setter private Date dateConsultation;

  @Override
  public int compareTo(BeneficiaireConsultationHistory consultationHistory) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.user, consultationHistory.user);
    compareToBuilder.append(this.idElasticBeneficiaire, consultationHistory.idElasticBeneficiaire);

    return compareToBuilder.toComparison();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BeneficiaireConsultationHistory that)) {
      return false;
    }
    return user.equals(that.user) && idElasticBeneficiaire.equals(that.idElasticBeneficiaire);
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, idElasticBeneficiaire);
  }
}
