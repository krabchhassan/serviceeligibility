package com.cegedim.next.serviceeligibility.core.webservices.idb_clc;

import com.cegedimassurances.norme.base_de_droit.TypeTypeAdresse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import lombok.Data;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

@Data
public class TypeAdresse implements Serializable, Cloneable, CopyTo {

  @Size(max = 45)
  protected String ligne1;

  @Size(max = 45)
  protected String ligne2;

  @Size(max = 45)
  protected String ligne3;

  @Size(max = 45)
  protected String ligne4;

  @Size(max = 45)
  protected String ligne5;

  @Size(max = 45)
  protected String ligne6;

  @Size(max = 45)
  protected String ligne7;

  @Size(max = 45)
  protected String codePostal;

  @Size(max = 45)
  protected String pays;

  @Size(max = 45)
  protected String telephone;

  @Size(max = 45)
  protected String email;

  @NotNull @Valid protected TypeTypeAdresse typeAdresse;

  public Object clone() {
    return copyTo(createNewInstance());
  }

  public Object copyTo(Object target) {
    final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
    return copyTo(null, target, strategy);
  }

  public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
    final Object draftCopy = ((target == null) ? createNewInstance() : target);
    if (draftCopy instanceof TypeAdresse) {
      final TypeAdresse copy = ((TypeAdresse) draftCopy);
      if (this.ligne1 != null) {
        String sourceLigne1;
        sourceLigne1 = this.getLigne1();
        String copyLigne1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne1", sourceLigne1), sourceLigne1));
        copy.setLigne1(copyLigne1);
      } else {
        copy.ligne1 = null;
      }
      if (this.ligne2 != null) {
        String sourceLigne2;
        sourceLigne2 = this.getLigne2();
        String copyLigne2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne2", sourceLigne2), sourceLigne2));
        copy.setLigne2(copyLigne2);
      } else {
        copy.ligne2 = null;
      }
      if (this.ligne3 != null) {
        String sourceLigne3;
        sourceLigne3 = this.getLigne3();
        String copyLigne3 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne3", sourceLigne3), sourceLigne3));
        copy.setLigne3(copyLigne3);
      } else {
        copy.ligne3 = null;
      }
      if (this.ligne4 != null) {
        String sourceLigne4;
        sourceLigne4 = this.getLigne4();
        String copyLigne4 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne4", sourceLigne4), sourceLigne4));
        copy.setLigne4(copyLigne4);
      } else {
        copy.ligne4 = null;
      }
      if (this.ligne5 != null) {
        String sourceLigne5;
        sourceLigne5 = this.getLigne5();
        String copyLigne5 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne5", sourceLigne5), sourceLigne5));
        copy.setLigne5(copyLigne5);
      } else {
        copy.ligne5 = null;
      }
      if (this.ligne6 != null) {
        String sourceLigne6;
        sourceLigne6 = this.getLigne6();
        String copyLigne6 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne6", sourceLigne6), sourceLigne6));
        copy.setLigne6(copyLigne6);
      } else {
        copy.ligne6 = null;
      }
      if (this.ligne7 != null) {
        String sourceLigne7;
        sourceLigne7 = this.getLigne7();
        String copyLigne7 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "ligne7", sourceLigne7), sourceLigne7));
        copy.setLigne7(copyLigne7);
      } else {
        copy.ligne7 = null;
      }
      if (this.codePostal != null) {
        String sourceCodePostal;
        sourceCodePostal = this.getCodePostal();
        String copyCodePostal =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "codePostal", sourceCodePostal),
                    sourceCodePostal));
        copy.setCodePostal(copyCodePostal);
      } else {
        copy.codePostal = null;
      }
      if (this.pays != null) {
        String sourcePays;
        sourcePays = this.getPays();
        String copyPays =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "pays", sourcePays), sourcePays));
        copy.setPays(copyPays);
      } else {
        copy.pays = null;
      }
      if (this.telephone != null) {
        String sourceTelephone;
        sourceTelephone = this.getTelephone();
        String copyTelephone =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "telephone", sourceTelephone), sourceTelephone));
        copy.setTelephone(copyTelephone);
      } else {
        copy.telephone = null;
      }
      if (this.email != null) {
        String sourceEmail;
        sourceEmail = this.getEmail();
        String copyEmail =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "email", sourceEmail), sourceEmail));
        copy.setEmail(copyEmail);
      } else {
        copy.email = null;
      }
      if (this.typeAdresse != null) {
        TypeTypeAdresse sourceTypeAdresse;
        sourceTypeAdresse = this.getTypeAdresse();
        TypeTypeAdresse copyTypeAdresse =
            ((TypeTypeAdresse)
                strategy.copy(
                    LocatorUtils.property(locator, "typeAdresse", sourceTypeAdresse),
                    sourceTypeAdresse));
        copy.setTypeAdresse(copyTypeAdresse);
      } else {
        copy.typeAdresse = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeAdresse();
  }
}
