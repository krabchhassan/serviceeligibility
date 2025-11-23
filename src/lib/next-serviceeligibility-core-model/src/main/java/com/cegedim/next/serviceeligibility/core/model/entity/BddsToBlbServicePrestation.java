package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "servicePrestation")
@Data
public class BddsToBlbServicePrestation {
  @NotNull @Valid private List<Assure> assures;

  @Data
  public static class Assure {
    @NotNull @Valid private Identite identite;

    @Data
    public static class Identite {
      @Valid private Nir nir;
      @Valid private List<NirRattachementRO> affiliationsRO;
      @NotEmpty private String dateNaissance;
      @NotEmpty private String rangNaissance;

      // UTILS
      private String key(String nir, String dateNaissance, String rang) {
        return String.format("%s%s%s", nir, dateNaissance, rang);
      }

      public String key() {
        return key(nirCode(), dateNaissance, rangNaissance);
      }

      public String key(NirRattachementRO attachementRO) {
        return key(attachementRO.nirCode(), dateNaissance, rangNaissance);
      }

      public String nirCode() {
        return (nir != null) ? nir.getCode() : null;
      }
    }
  }
}
