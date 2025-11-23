package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.builder.CompareToBuilder;

@Data
public class Parametre implements GenericDomain<Parametre> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String numero;
  private String libelle;
  private String valeur;

  public Parametre() {
    /* empty constructor */ }

  public Parametre(Parametre source) {
    this.numero = source.getNumero();
    this.libelle = source.getLibelle();
    this.valeur = source.getValeur();
  }

  @Override
  public int compareTo(final Parametre parametre) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.libelle, parametre.libelle);
    compareToBuilder.append(this.numero, parametre.numero);
    compareToBuilder.append(this.valeur, parametre.valeur);
    return compareToBuilder.toComparison();
  }
}
