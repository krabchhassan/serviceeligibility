package com.cegedim.next.serviceeligibility.core.bobb;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/** Objet de representation d'un lot dans la collection Lot */
@Document(collection = "lot")
@Data
public class Lot {
  @Id private String id;
  @NotNull private String code;
  @NotNull private String libelle;
  @NotEmpty private List<GarantieTechnique> garantieTechniques;
  private String dateAjout;
  private String dateSuppressionLogique;
}
