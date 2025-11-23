package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

public class AttestationDetailDto implements GenericDto {

  /** */
  private static final long serialVersionUID = -1253231512366820395L;

  private Integer numOrdreEdition;

  private String codeDomaine;

  private List<ConventionDto> conventions;

  private String taux;

  private String uniteTaux;

  private String codeRenvois;

  private String libelleRenvois;

  @JsonIgnore private Boolean isDroitEditable;

  @Getter @Setter private String codeRenvoisAdditionnel;
  @Getter @Setter private String libelleRenvoisAdditionnel;

  public Integer getNumOrdreEdition() {
    return numOrdreEdition;
  }

  public void setNumOrdreEdition(Integer numOrdreEdition) {
    this.numOrdreEdition = numOrdreEdition;
  }

  public String getCodeDomaine() {
    return codeDomaine;
  }

  public void setCodeDomaine(String codeDomaine) {
    this.codeDomaine = codeDomaine;
  }

  public List<ConventionDto> getConventions() {
    return conventions;
  }

  public void setConventions(List<ConventionDto> conventions) {
    this.conventions = conventions;
  }

  public String getTaux() {
    return taux;
  }

  public String getUniteTaux() {
    return uniteTaux;
  }

  public void setTaux(String taux) {
    this.taux = taux;
  }

  public void setUniteTaux(String uniteTaux) {
    this.uniteTaux = uniteTaux;
  }

  public String getCodeRenvois() {
    return codeRenvois;
  }

  public void setCodeRenvois(String codeRenvois) {
    this.codeRenvois = codeRenvois;
  }

  public String getLibelleRenvois() {
    return libelleRenvois;
  }

  public void setLibelleRenvois(String libelleRenvois) {
    this.libelleRenvois = libelleRenvois;
  }

  public Boolean getIsDroitEditable() {
    return isDroitEditable;
  }

  public void setIsDroitEditable(Boolean isDroitEditable) {
    this.isDroitEditable = isDroitEditable;
  }
}
