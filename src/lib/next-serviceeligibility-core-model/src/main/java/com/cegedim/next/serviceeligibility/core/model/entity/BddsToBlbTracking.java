package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bdds-to-blb-tracking")
@CompoundIndexes({
  @CompoundIndex(name = "nir_age_rang", def = "{'nir' : 1, 'dateNaissance': 1, 'rangNaissance': 1}")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BddsToBlbTracking {
  @Id private ObjectId id;
  @NotNull private String nir;
  @NotNull private String dateNaissance;
  @NotNull private String rangNaissance;
  @NotNull @Builder.Default private LocalDateTime date = LocalDateTime.now(ZoneOffset.UTC);
  @NotNull @Indexed @Builder.Default private BddsToBlbStatus status = BddsToBlbStatus.NO_RESPONSE;
  private String errorLabel;
  private String errorCode;

  // CONSTRUCTORS
  @Builder(builderMethodName = "builder2")
  public BddsToBlbTracking(
      String idHex,
      String nir,
      String dateNaissance,
      String rangNaissance,
      BddsToBlbStatus status,
      LocalDateTime date,
      String errorLabel,
      String errorCode) {
    this.idHex(idHex);
    this.nir = nir;
    this.dateNaissance = dateNaissance;
    this.rangNaissance = rangNaissance;
    this.status = status;
    this.date = date;
    this.errorLabel = errorLabel;
    this.errorCode = errorCode;
  }

  // UTILS
  public String idHex() {
    return (id != null) ? id.toHexString() : null;
  }

  public void idHex(String id) {
    if (ObjectId.isValid(id)) this.id = new ObjectId(id);
  }

  public String key() {
    return String.format("%s%s%s", nir, dateNaissance, rangNaissance);
  }
}
