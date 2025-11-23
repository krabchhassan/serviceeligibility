package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.util.CollectionUtils;

@Data
@EqualsAndHashCode
public class Formule implements GenericDomain<Formule> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String numero;
  private String libelle;
  private String masqueFormule;

  /* DOCUMENTS EMBEDDED */
  private List<Parametre> parametres = new ArrayList<>();

  public Formule() {
    /* empty constructor */ }

  public Formule(Formule source) {
    this.numero = source.getNumero();
    this.libelle = source.getLibelle();
    this.masqueFormule = source.getMasqueFormule();

    if (!CollectionUtils.isEmpty(source.getParametres())) {
      for (Parametre param : source.getParametres()) {
        this.parametres.add(new Parametre(param));
      }
    }
  }

  /**
   * Renvoi une liste de parametres. Si il n'y a aucun parametre, une liste vide est renvoyee.
   *
   * @return La liste des parametres.
   */
  public List<Parametre> getParametres() {
    if (this.parametres == null) {
      this.parametres = new ArrayList<>();
    }
    return this.parametres;
  }

  /** (non-Javadoc) //@see java.lang.Comparable#compareTo(java.lang.Object) */
  @Override
  public int compareTo(final Formule formule) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.libelle, formule.libelle);
    compareToBuilder.append(this.numero, formule.numero);
    compareToBuilder.append(this.masqueFormule, formule.masqueFormule);
    compareToBuilder.append(this.parametres, formule.parametres);
    return compareToBuilder.toComparison();
  }
}
