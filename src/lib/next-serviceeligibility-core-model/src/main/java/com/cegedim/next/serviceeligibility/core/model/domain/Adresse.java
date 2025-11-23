package com.cegedim.next.serviceeligibility.core.model.domain;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Field;

/** Classe qui mappe le document Adresse */
@Data
public class Adresse implements GenericDomain<Adresse> {

  private static final long serialVersionUID = 1L;

  /* PROPRIETES */
  @Field(order = 1)
  private String ligne1;

  @Field(order = 2)
  private String ligne2;

  @Field(order = 3)
  private String ligne3;

  @Field(order = 4)
  private String ligne4;

  @Field(order = 5)
  private String ligne5;

  @Field(order = 6)
  private String ligne6;

  @Field(order = 7)
  private String ligne7;

  @Field(order = 8)
  private String codePostal;

  @Field(order = 9)
  private String pays;

  @Field(order = 10)
  private String telephone;

  @Field(order = 12)
  private String email;

  /* DOCUMENTS EMBEDDED */
  @Field(order = 13)
  private TypeAdresse typeAdresse;

  public Adresse() {
    /* empty constructor */ }

  public Adresse(Adresse source) {
    this.codePostal = source.getCodePostal();
    this.email = source.getEmail();
    this.ligne1 = source.getLigne1();
    this.ligne2 = source.getLigne2();
    this.ligne3 = source.getLigne3();
    this.ligne4 = source.getLigne4();
    this.ligne5 = source.getLigne5();
    this.ligne6 = source.getLigne6();
    this.ligne7 = source.getLigne7();
    this.pays = source.getPays();
    this.telephone = source.getTelephone();
    if (source.getTypeAdresse() != null) {
      this.typeAdresse = new TypeAdresse(source.getTypeAdresse());
    }
  }

  public Adresse(AdresseAvecFixe source) {
    this.codePostal = source.getCodePostal();
    this.email = source.getEmail();
    this.ligne1 = source.getLigne1();
    this.ligne2 = source.getLigne2();
    this.ligne3 = source.getLigne3();
    this.ligne4 = source.getLigne4();
    this.ligne5 = source.getLigne5();
    this.ligne6 = source.getLigne6();
    this.ligne7 = source.getLigne7();
    this.pays = source.getPays();
    this.telephone = StringUtils.defaultIfBlank(source.getTelephone(), source.getFixe());
    if (source.getTypeAdresse() != null) {
      this.typeAdresse = new TypeAdresse(source.getTypeAdresse());
    }
  }

  @Override
  public int compareTo(final Adresse adresse) {
    final CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.codePostal, adresse.codePostal);
    compareToBuilder.append(this.email, adresse.email);
    compareToBuilder.append(this.ligne1, adresse.ligne1);
    compareToBuilder.append(this.ligne2, adresse.ligne2);
    compareToBuilder.append(this.ligne3, adresse.ligne3);
    compareToBuilder.append(this.ligne4, adresse.ligne4);
    compareToBuilder.append(this.ligne5, adresse.ligne5);
    compareToBuilder.append(this.ligne6, adresse.ligne6);
    compareToBuilder.append(this.ligne7, adresse.ligne7);
    compareToBuilder.append(this.pays, adresse.pays);
    compareToBuilder.append(this.telephone, adresse.telephone);
    compareToBuilder.append(this.typeAdresse, adresse.typeAdresse);
    return compareToBuilder.toComparison();
  }

  public void setLigne1(String newLigne1) {
    if (newLigne1 != null) {
      this.ligne1 = newLigne1;
    }
  }

  public void setLigne2(String newLigne2) {
    if (newLigne2 != null) {
      this.ligne2 = newLigne2;
    }
  }

  public void setLigne3(String newLigne3) {
    if (newLigne3 != null) {
      this.ligne3 = newLigne3;
    }
  }

  public void setLigne4(String newLigne4) {
    if (newLigne4 != null) {
      this.ligne4 = newLigne4;
    }
  }

  public void setLigne5(String newLigne5) {
    if (newLigne5 != null) {
      this.ligne5 = newLigne5;
    }
  }

  public void setLigne6(String newLigne6) {
    if (newLigne6 != null) {
      this.ligne6 = newLigne6;
    }
  }

  public void setLigne7(String newLigne7) {
    if (newLigne7 != null) {
      this.ligne7 = newLigne7;
    }
  }

  public void setCodePostal(String newCodePostal) {
    if (newCodePostal != null) {
      this.codePostal = newCodePostal;
    }
  }

  public void setPays(String newPays) {
    if (newPays != null) {
      this.pays = newPays;
    }
  }

  public void setTelephone(String newTelephone) {
    if (newTelephone != null) {
      this.telephone = newTelephone;
    }
  }

  public void setEmail(String newEmail) {
    if (newEmail != null) {
      this.email = newEmail;
    }
  }

  public void setTypeAdresse(TypeAdresse newTypeAdresse) {
    if (newTypeAdresse != null) {
      this.typeAdresse = newTypeAdresse;
    }
  }
}
