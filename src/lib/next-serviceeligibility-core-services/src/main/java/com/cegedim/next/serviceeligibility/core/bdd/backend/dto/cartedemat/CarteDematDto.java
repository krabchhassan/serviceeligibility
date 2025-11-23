package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.ContratDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.Getter;
import lombok.Setter;

/** Classe DTO de l'entite {@code CarteDemat}. */
public class CarteDematDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  @Getter @Setter private String numeroAmc;

  @Getter @Setter private String nomAmc;

  @Getter @Setter private String libelleAmc;

  @Getter @Setter private XMLGregorianCalendar periodeDebut;

  @Getter @Setter private XMLGregorianCalendar periodeFin;

  /* DOCUMENTS EMBEDDED */
  @Getter @Setter private ContratDto contrat;

  @Getter @Setter private AdresseDto adresse;

  @Setter private List<BenefCarteDematDto> benefCarteDematDtos;

  @Setter private List<DomaineConventionDto> domaineConventionDtos;

  public List<DomaineConventionDto> getDomaineConventionDtos() {
    if (this.domaineConventionDtos == null) {
      this.domaineConventionDtos = new ArrayList<>();
    }
    return this.domaineConventionDtos;
  }

  public List<BenefCarteDematDto> getBenefCarteDematDtos() {
    if (this.benefCarteDematDtos == null) {
      this.benefCarteDematDtos = new ArrayList<>();
    }
    return this.benefCarteDematDtos;
  }
}
