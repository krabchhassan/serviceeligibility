package com.cegedim.next.serviceeligibility.core.model.kafka.prestij;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicePrestIJ")
@Data
@EqualsAndHashCode(callSuper = false)
public class PrestIJ extends DocumentEntity implements GenericDomain<PrestIJ> {

  private String traceId;

  @NotNull(message = "Le contrat est obligatoire")
  @Valid
  private ContratIJ contrat;

  @NotNull(message = "L'OC est obligatoire")
  @Valid
  private Oc oc;

  @NotNull(message = "L'entreprise est obligatoire")
  @Valid
  private Entreprise entreprise;

  @NotEmpty(message = "La liste d'assurés est obligatoire")
  @Valid
  private List<Assure> assures;

  @Override
  public int compareTo(final PrestIJ prestij) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.contrat, prestij.contrat);
    compareToBuilder.append(this.oc, prestij.oc);
    compareToBuilder.append(this.entreprise, prestij.entreprise);
    compareToBuilder.append(this.assures, prestij.assures);
    return compareToBuilder.toComparison();
  }
}
