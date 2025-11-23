package com.cegedim.next.serviceeligibility.core.model.kafka.contract;

import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
public class IdentiteContrat {
  @Valid private Nir nir;
  @Valid private List<NirRattachementRO> affiliationsRO;

  @NotBlank(message = "L'information dateNaissance est obligatoire")
  private String dateNaissance;

  @NotBlank(message = "L'information rangNaissance est obligatoire")
  private String rangNaissance;

  @NotBlank(message = "L'information numeroPersonne est obligatoire")
  private String numeroPersonne;

  private String refExternePersonne;
  private List<HistoriqueDateRangNaissance> historiqueDateRangNaissance;

  public void setNir(Nir newNir) {
    if (newNir != null) {
      this.nir = newNir;
    }
  }

  public void setAffiliationsRO(List<NirRattachementRO> newAffiliationsRO) {
    if (newAffiliationsRO != null) {
      this.affiliationsRO = newAffiliationsRO;
    }
  }

  public void setDateNaissance(String newDateNaissance) {
    if (newDateNaissance != null) {
      this.dateNaissance = newDateNaissance;
    }
  }

  public void setRangNaissance(String newRangNaissance) {
    if (newRangNaissance != null) {
      this.rangNaissance = newRangNaissance;
    }
  }

  public void setNumeroPersonne(String newNumeroPersonne) {
    if (newNumeroPersonne != null) {
      this.numeroPersonne = newNumeroPersonne;
    }
  }

  public void setRefExternePersonne(String newRefExternePersonne) {
    if (newRefExternePersonne != null) {
      this.refExternePersonne = newRefExternePersonne;
    }
  }

  public void setHistoriqueDateRangNaissance(
      List<HistoriqueDateRangNaissance> newHistoriqueDateRangNaissance) {
    if (newHistoriqueDateRangNaissance != null) {
      this.historiqueDateRangNaissance = newHistoriqueDateRangNaissance;
    }
  }

  public void setHistoriqueDateRangNaissanceToNull() {
    this.historiqueDateRangNaissance = null;
  }

  public String nirCode() {
    return (nir != null) ? nir.getCode() : null;
  }
}
