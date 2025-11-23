package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.Role;
import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

/** Classe qui mappe la collection fonctionnalites dans la base de donnees. */
@Document(collection = "fonctionnalites")
@Data
@EqualsAndHashCode(callSuper = false)
public class Fonctionnalite extends DocumentEntity implements GenericDomain<Fonctionnalite> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  private String code;
  private String libelle;
  private List<Role> roles;

  @Override
  public int compareTo(Fonctionnalite fonctions) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.libelle, fonctions.libelle);
    compareToBuilder.append(this.code, fonctions.code);
    compareToBuilder.append(this.roles, fonctions.roles);
    return compareToBuilder.toComparison();
  }
}
