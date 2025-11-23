package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui enregistre l'historique des consultations de droits dans la base de donnees. */
@Document(collection = "declarationConsultationHistory")
public class DeclarationConsultationHistory extends DocumentEntity
    implements GenericDomain<DeclarationConsultationHistory> {

  private static final long serialVersionUID = 1L;

  @Getter @Setter private String user;

  @Getter @Setter private Date dateConsultation;

  /* CLES ETRANGERES */
  @Getter @Setter private Declaration declaration;

  @Override
  public int compareTo(DeclarationConsultationHistory consultationHistory) {

    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.user, consultationHistory.user);
    compareToBuilder.append(this.declaration, consultationHistory.declaration);

    return compareToBuilder.toComparison();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof DeclarationConsultationHistory that)) {
      return false;
    }
    return user.equals(that.user) && declaration.get_id().equals(that.declaration.get_id());
  }

  @Override
  public int hashCode() {
    return Objects.hash(user, declaration);
  }
}
