package com.cegedim.next.serviceeligibility.core.webservices.idb_clc;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.Data;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

@Data
public class TypeHistoriqueAffiliation implements Serializable, Cloneable, CopyTo {

  @Size(max = 50)
  protected String nom;

  @NotNull
  @Size(max = 50)
  protected String nomPatronymique;

  @Size(max = 50)
  protected String nomMarital;

  @Size(max = 50)
  protected String prenom;

  @Size(max = 45)
  protected String civilite;

  @NotNull protected LocalDate periodeDebut;

  protected LocalDate periodeFin;

  @Size(max = 1)
  protected String qualite;

  @NotNull
  @Size(max = 2)
  protected String regimeOD1;

  @NotNull
  @Size(max = 3)
  protected String caisseOD1;

  @Size(max = 4)
  protected String centreOD1;

  @Size(max = 2)
  protected String regimeOD2;

  @Size(max = 3)
  protected String caisseOD2;

  @Size(max = 4)
  protected String centreOD2;

  @Size(max = 15)
  protected String medecinTraitant;

  @Size(max = 2)
  protected String regimeParticulier;

  @NotNull
  @JsonProperty("isBeneficiaireACS")
  protected boolean isBeneficiaireACS;

  public Object clone() {
    return copyTo(createNewInstance());
  }

  public Object copyTo(Object target) {
    final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
    return copyTo(null, target, strategy);
  }

  public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
    final Object draftCopy = ((target == null) ? createNewInstance() : target);
    if (draftCopy instanceof TypeHistoriqueAffiliation) {
      final TypeHistoriqueAffiliation copy = ((TypeHistoriqueAffiliation) draftCopy);
      if (this.nom != null) {
        String sourceNom;
        sourceNom = this.getNom();
        String copyNom =
            ((String) strategy.copy(LocatorUtils.property(locator, "nom", sourceNom), sourceNom));
        copy.setNom(copyNom);
      } else {
        copy.nom = null;
      }
      if (this.nomPatronymique != null) {
        String sourceNomPatronymique;
        sourceNomPatronymique = this.getNomPatronymique();
        String copyNomPatronymique =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomPatronymique", sourceNomPatronymique),
                    sourceNomPatronymique));
        copy.setNomPatronymique(copyNomPatronymique);
      } else {
        copy.nomPatronymique = null;
      }
      if (this.nomMarital != null) {
        String sourceNomMarital;
        sourceNomMarital = this.getNomMarital();
        String copyNomMarital =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomMarital", sourceNomMarital),
                    sourceNomMarital));
        copy.setNomMarital(copyNomMarital);
      } else {
        copy.nomMarital = null;
      }
      if (this.prenom != null) {
        String sourcePrenom;
        sourcePrenom = this.getPrenom();
        String copyPrenom =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "prenom", sourcePrenom), sourcePrenom));
        copy.setPrenom(copyPrenom);
      } else {
        copy.prenom = null;
      }
      if (this.civilite != null) {
        String sourceCivilite;
        sourceCivilite = this.getCivilite();
        String copyCivilite =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "civilite", sourceCivilite), sourceCivilite));
        copy.setCivilite(copyCivilite);
      } else {
        copy.civilite = null;
      }
      if (this.periodeDebut != null) {
        LocalDate sourcePeriodeDebut;
        sourcePeriodeDebut = this.getPeriodeDebut();
        LocalDate copyPeriodeDebut =
            ((LocalDate)
                strategy.copy(
                    LocatorUtils.property(locator, "periodeDebut", sourcePeriodeDebut),
                    sourcePeriodeDebut));
        copy.setPeriodeDebut(copyPeriodeDebut);
      } else {
        copy.periodeDebut = null;
      }
      if (this.periodeFin != null) {
        LocalDate sourcePeriodeFin;
        sourcePeriodeFin = this.getPeriodeFin();
        LocalDate copyPeriodeFin =
            ((LocalDate)
                strategy.copy(
                    LocatorUtils.property(locator, "periodeFin", sourcePeriodeFin),
                    sourcePeriodeFin));
        copy.setPeriodeFin(copyPeriodeFin);
      } else {
        copy.periodeFin = null;
      }
      if (this.qualite != null) {
        String sourceQualite;
        sourceQualite = this.getQualite();
        String copyQualite =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "qualite", sourceQualite), sourceQualite));
        copy.setQualite(copyQualite);
      } else {
        copy.qualite = null;
      }
      if (this.regimeOD1 != null) {
        String sourceRegimeOD1;
        sourceRegimeOD1 = this.getRegimeOD1();
        String copyRegimeOD1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "regimeOD1", sourceRegimeOD1), sourceRegimeOD1));
        copy.setRegimeOD1(copyRegimeOD1);
      } else {
        copy.regimeOD1 = null;
      }
      if (this.caisseOD1 != null) {
        String sourceCaisseOD1;
        sourceCaisseOD1 = this.getCaisseOD1();
        String copyCaisseOD1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "caisseOD1", sourceCaisseOD1), sourceCaisseOD1));
        copy.setCaisseOD1(copyCaisseOD1);
      } else {
        copy.caisseOD1 = null;
      }
      if (this.centreOD1 != null) {
        String sourceCentreOD1;
        sourceCentreOD1 = this.getCentreOD1();
        String copyCentreOD1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "centreOD1", sourceCentreOD1), sourceCentreOD1));
        copy.setCentreOD1(copyCentreOD1);
      } else {
        copy.centreOD1 = null;
      }
      if (this.regimeOD2 != null) {
        String sourceRegimeOD2;
        sourceRegimeOD2 = this.getRegimeOD2();
        String copyRegimeOD2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "regimeOD2", sourceRegimeOD2), sourceRegimeOD2));
        copy.setRegimeOD2(copyRegimeOD2);
      } else {
        copy.regimeOD2 = null;
      }
      if (this.caisseOD2 != null) {
        String sourceCaisseOD2;
        sourceCaisseOD2 = this.getCaisseOD2();
        String copyCaisseOD2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "caisseOD2", sourceCaisseOD2), sourceCaisseOD2));
        copy.setCaisseOD2(copyCaisseOD2);
      } else {
        copy.caisseOD2 = null;
      }
      if (this.centreOD2 != null) {
        String sourceCentreOD2;
        sourceCentreOD2 = this.getCentreOD2();
        String copyCentreOD2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "centreOD2", sourceCentreOD2), sourceCentreOD2));
        copy.setCentreOD2(copyCentreOD2);
      } else {
        copy.centreOD2 = null;
      }
      if (this.medecinTraitant != null) {
        String sourceMedecinTraitant;
        sourceMedecinTraitant = this.getMedecinTraitant();
        String copyMedecinTraitant =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "medecinTraitant", sourceMedecinTraitant),
                    sourceMedecinTraitant));
        copy.setMedecinTraitant(copyMedecinTraitant);
      } else {
        copy.medecinTraitant = null;
      }
      if (this.regimeParticulier != null) {
        String sourceRegimeParticulier;
        sourceRegimeParticulier = this.getRegimeParticulier();
        String copyRegimeParticulier =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "regimeParticulier", sourceRegimeParticulier),
                    sourceRegimeParticulier));
        copy.setRegimeParticulier(copyRegimeParticulier);
      } else {
        copy.regimeParticulier = null;
      }
      boolean sourceIsBeneficiaireACS;
      sourceIsBeneficiaireACS = (true ? this.isBeneficiaireACS() : false);
      boolean copyIsBeneficiaireACS =
          strategy.copy(
              LocatorUtils.property(locator, "isBeneficiaireACS", sourceIsBeneficiaireACS),
              sourceIsBeneficiaireACS);
      copy.setBeneficiaireACS(copyIsBeneficiaireACS);
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeHistoriqueAffiliation();
  }
}
