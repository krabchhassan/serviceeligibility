package com.cegedim.next.serviceeligibility.core.bobb.contract;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContratAIDto {

  @Getter
  @Setter
  public static class BeyondPeriodeDTO implements Serializable {
    private String debut;
    private String fin;
  }

  @Getter
  @Setter
  public static class Assure implements Serializable {
    private List<Droit> droits = new ArrayList<>();
  }

  @Getter
  @Setter
  public static class Droit implements Serializable {
    private String code;
    private String libelle;
    private String codeAssureur;
    private BeyondPeriodeDTO periode = new BeyondPeriodeDTO();
    private List<Carence> carences;
  }

  @Getter
  @Setter
  public static class Carence implements Serializable {
    private String code;
    private DroitRemplacement droitRemplacement = new DroitRemplacement();
    private BeyondPeriodeDTO periode = new BeyondPeriodeDTO();
  }

  @Getter
  @Setter
  public static class DroitRemplacement implements Serializable {
    private String codeAssureur;
    private String libelle;
    private String code;
  }

  private List<Assure> assures = new ArrayList<>();
}
