package com.cegedim.next.serviceeligibility.core.bdd.backend.dto;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

/** Classe DTO de l'entite {@code Formule}. */
public class FormuleDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  @Getter @Setter private String numero;

  @Getter @Setter private String libelle;

  /* CLES ETRANGERES */
  @Setter private List<ParametreDto> parametres;

  public FormuleDto() {
    /* empty constructor */ }

  public FormuleDto(FormuleDto source) {
    this.numero = source.getNumero();
    this.libelle = source.getLibelle();
    if (!CollectionUtils.isEmpty(source.getParametres())) {
      this.parametres = new ArrayList<>();
      for (ParametreDto param : source.getParametres()) {
        this.parametres.add(new ParametreDto(param));
      }
    }
  }

  /**
   * Renvoie une liste de {@code ParametreDto}.
   *
   * @return La liste de {@code ParametreDto}.
   */
  public List<ParametreDto> getParametres() {
    if (parametres == null) {
      parametres = new ArrayList<>();
    }
    return parametres;
  }
}
