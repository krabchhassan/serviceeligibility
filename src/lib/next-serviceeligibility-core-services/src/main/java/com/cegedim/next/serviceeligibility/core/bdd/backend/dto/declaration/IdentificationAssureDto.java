package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.declaration;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.CodePeriodeContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.NirRattachementRODto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.PeriodeContractDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.TeletransmissionDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;

public class IdentificationAssureDto implements GenericDto {

  /** */
  private static final long serialVersionUID = 4575459748692063626L;

  private String dateNaissance;

  private String rangNaissance;

  private AssureDto assure;

  private AffiliationInfoDto affiliation;

  private List<NirRattachementRODto> affiliationsRO;

  private List<PeriodeContractDto> periodesMedecinTraitant;
  private List<TeletransmissionDto> teletransmissions;

  private List<CodePeriodeContractDto> regimesParticuliers;
  private List<CodePeriodeContractDto> situationsParticulieres;

  public String getDateNaissance() {
    return dateNaissance;
  }

  public void setDateNaissance(String dateNaissance) {
    this.dateNaissance = dateNaissance;
  }

  public String getRangNaissance() {
    return rangNaissance;
  }

  public void setRangNaissance(String rangNaissance) {
    this.rangNaissance = rangNaissance;
  }

  public AssureDto getAssure() {
    return assure;
  }

  public void setAssure(AssureDto assure) {
    this.assure = assure;
  }

  public AffiliationInfoDto getAffiliation() {
    return affiliation;
  }

  public void setAffiliation(AffiliationInfoDto affiliation) {
    this.affiliation = affiliation;
  }

  public List<NirRattachementRODto> getAffiliationsRO() {
    return affiliationsRO;
  }

  public void setAffiliationsRO(List<NirRattachementRODto> affiliationsRO) {
    this.affiliationsRO = affiliationsRO;
  }

  public List<PeriodeContractDto> getPeriodesMedecinTraitant() {
    return periodesMedecinTraitant;
  }

  public void setPeriodesMedecinTraitant(List<PeriodeContractDto> periodesMedecinTraitant) {
    this.periodesMedecinTraitant = periodesMedecinTraitant;
  }

  public List<TeletransmissionDto> getTeletransmissions() {
    return teletransmissions;
  }

  public void setTeletransmissions(List<TeletransmissionDto> teletransmissions) {
    this.teletransmissions = teletransmissions;
  }

  public List<CodePeriodeContractDto> getRegimesParticuliers() {
    return regimesParticuliers;
  }

  public void setRegimesParticuliers(List<CodePeriodeContractDto> regimesParticuliers) {
    this.regimesParticuliers = regimesParticuliers;
  }

  public List<CodePeriodeContractDto> getSituationsParticulieres() {
    return situationsParticulieres;
  }

  public void setSituationsParticulieres(List<CodePeriodeContractDto> situationsParticulieres) {
    this.situationsParticulieres = situationsParticulieres;
  }
}
