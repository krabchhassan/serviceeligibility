package com.cegedim.next.serviceeligibility.core.webservices.idb_clc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.jvnet.jaxb2_commons.lang.CopyStrategy;
import org.jvnet.jaxb2_commons.lang.CopyTo;
import org.jvnet.jaxb2_commons.lang.JAXBCopyStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;
import org.jvnet.jaxb2_commons.locator.util.LocatorUtils;

@Data
public class TypeBeneficiaire implements Serializable, Cloneable, CopyTo {

  @NotNull
  @Size(max = 8)
  protected String dateNaissance;

  @NotNull
  @Size(max = 1)
  protected String rangNaissance;

  @Size(max = 13)
  protected String nirBeneficiaire;

  @Size(max = 2)
  protected String cleNirBeneficiaire;

  @Size(max = 13)
  protected String nirOd1;

  @Size(max = 2)
  protected String cleNirOd1;

  @Size(max = 13)
  protected String nirOd2;

  @Size(max = 2)
  protected String cleNirOd2;

  @Size(max = 25)
  protected String insc;

  @Size(max = 15)
  protected String numeroPersonne;

  @Valid protected List<TypeHistoriqueAffiliation> historiqueAffiliations;

  @Valid protected List<TypeAdresse> adresses;

  /**
   * Gets the value of the historiqueAffiliations property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the historiqueAffiliations property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getHistoriqueAffiliations().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeHistoriqueAffiliation }
   */
  public List<TypeHistoriqueAffiliation> getHistoriqueAffiliations() {
    if (historiqueAffiliations == null) {
      historiqueAffiliations = new ArrayList<TypeHistoriqueAffiliation>();
    }
    return this.historiqueAffiliations;
  }

  /**
   * Gets the value of the adresses property.
   *
   * <p>This accessor method returns a reference to the live list, not a snapshot. Therefore any
   * modification you make to the returned list will be present inside the JAXB object. This is why
   * there is not a <CODE>set</CODE> method for the adresses property.
   *
   * <p>For example, to add a new item, do as follows:
   *
   * <pre>
   * getAdresses().add(newItem);
   * </pre>
   *
   * <p>Objects of the following type(s) are allowed in the list {@link TypeAdresse }
   */
  public List<TypeAdresse> getAdresses() {
    if (adresses == null) {
      adresses = new ArrayList<TypeAdresse>();
    }
    return this.adresses;
  }

  public Object clone() {
    return copyTo(createNewInstance());
  }

  public Object copyTo(Object target) {
    final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
    return copyTo(null, target, strategy);
  }

  public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
    final Object draftCopy = ((target == null) ? createNewInstance() : target);
    if (draftCopy instanceof TypeBeneficiaire) {
      final TypeBeneficiaire copy = ((TypeBeneficiaire) draftCopy);
      if (this.dateNaissance != null) {
        String sourceDateNaissance;
        sourceDateNaissance = this.getDateNaissance();
        String copyDateNaissance =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "dateNaissance", sourceDateNaissance),
                    sourceDateNaissance));
        copy.setDateNaissance(copyDateNaissance);
      } else {
        copy.dateNaissance = null;
      }
      if (this.rangNaissance != null) {
        String sourceRangNaissance;
        sourceRangNaissance = this.getRangNaissance();
        String copyRangNaissance =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "rangNaissance", sourceRangNaissance),
                    sourceRangNaissance));
        copy.setRangNaissance(copyRangNaissance);
      } else {
        copy.rangNaissance = null;
      }
      if (this.nirBeneficiaire != null) {
        String sourceNirBeneficiaire;
        sourceNirBeneficiaire = this.getNirBeneficiaire();
        String copyNirBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirBeneficiaire", sourceNirBeneficiaire),
                    sourceNirBeneficiaire));
        copy.setNirBeneficiaire(copyNirBeneficiaire);
      } else {
        copy.nirBeneficiaire = null;
      }
      if (this.cleNirBeneficiaire != null) {
        String sourceCleNirBeneficiaire;
        sourceCleNirBeneficiaire = this.getCleNirBeneficiaire();
        String copyCleNirBeneficiaire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirBeneficiaire", sourceCleNirBeneficiaire),
                    sourceCleNirBeneficiaire));
        copy.setCleNirBeneficiaire(copyCleNirBeneficiaire);
      } else {
        copy.cleNirBeneficiaire = null;
      }
      if (this.nirOd1 != null) {
        String sourceNirOd1;
        sourceNirOd1 = this.getNirOd1();
        String copyNirOd1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirOd1", sourceNirOd1), sourceNirOd1));
        copy.setNirOd1(copyNirOd1);
      } else {
        copy.nirOd1 = null;
      }
      if (this.cleNirOd1 != null) {
        String sourceCleNirOd1;
        sourceCleNirOd1 = this.getCleNirOd1();
        String copyCleNirOd1 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirOd1", sourceCleNirOd1), sourceCleNirOd1));
        copy.setCleNirOd1(copyCleNirOd1);
      } else {
        copy.cleNirOd1 = null;
      }
      if (this.nirOd2 != null) {
        String sourceNirOd2;
        sourceNirOd2 = this.getNirOd2();
        String copyNirOd2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nirOd2", sourceNirOd2), sourceNirOd2));
        copy.setNirOd2(copyNirOd2);
      } else {
        copy.nirOd2 = null;
      }
      if (this.cleNirOd2 != null) {
        String sourceCleNirOd2;
        sourceCleNirOd2 = this.getCleNirOd2();
        String copyCleNirOd2 =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "cleNirOd2", sourceCleNirOd2), sourceCleNirOd2));
        copy.setCleNirOd2(copyCleNirOd2);
      } else {
        copy.cleNirOd2 = null;
      }
      if (this.insc != null) {
        String sourceInsc;
        sourceInsc = this.getInsc();
        String copyInsc =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "insc", sourceInsc), sourceInsc));
        copy.setInsc(copyInsc);
      } else {
        copy.insc = null;
      }
      if (this.numeroPersonne != null) {
        String sourceNumeroPersonne;
        sourceNumeroPersonne = this.getNumeroPersonne();
        String copyNumeroPersonne =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroPersonne", sourceNumeroPersonne),
                    sourceNumeroPersonne));
        copy.setNumeroPersonne(copyNumeroPersonne);
      } else {
        copy.numeroPersonne = null;
      }
      if ((this.historiqueAffiliations != null) && (!this.historiqueAffiliations.isEmpty())) {
        List<TypeHistoriqueAffiliation> sourceHistoriqueAffiliations;
        sourceHistoriqueAffiliations =
            (((this.historiqueAffiliations != null) && (!this.historiqueAffiliations.isEmpty()))
                ? this.getHistoriqueAffiliations()
                : null);
        @SuppressWarnings("unchecked")
        List<TypeHistoriqueAffiliation> copyHistoriqueAffiliations =
            ((List<TypeHistoriqueAffiliation>)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "historiqueAffiliations", sourceHistoriqueAffiliations),
                    sourceHistoriqueAffiliations));
        copy.historiqueAffiliations = null;
        if (copyHistoriqueAffiliations != null) {
          List<TypeHistoriqueAffiliation> uniqueHistoriqueAffiliationsl =
              copy.getHistoriqueAffiliations();
          uniqueHistoriqueAffiliationsl.addAll(copyHistoriqueAffiliations);
        }
      } else {
        copy.historiqueAffiliations = null;
      }
      if ((this.adresses != null) && (!this.adresses.isEmpty())) {
        List<TypeAdresse> sourceAdresses;
        sourceAdresses =
            (((this.adresses != null) && (!this.adresses.isEmpty())) ? this.getAdresses() : null);
        @SuppressWarnings("unchecked")
        List<TypeAdresse> copyAdresses =
            ((List<TypeAdresse>)
                strategy.copy(
                    LocatorUtils.property(locator, "adresses", sourceAdresses), sourceAdresses));
        copy.adresses = null;
        if (copyAdresses != null) {
          List<TypeAdresse> uniqueAdressesl = copy.getAdresses();
          uniqueAdressesl.addAll(copyAdresses);
        }
      } else {
        copy.adresses = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeBeneficiaire();
  }
}
