package com.cegedim.next.serviceeligibility.core.webservices.idb_clc;

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
public class TypeContrat implements Serializable, Cloneable, CopyTo {

  @Size(max = 45)
  protected String numero;

  @NotNull protected LocalDate dateSouscription;

  protected LocalDate dateResiliation;

  @Size(max = 15)
  protected String type;

  @Size(max = 50)
  protected String nomPorteur;

  @Size(max = 50)
  protected String prenomPorteur;

  @Size(max = 45)
  protected String civilitePorteur;

  @Size(max = 15)
  protected String numeroAdherent;

  @Size(max = 50)
  protected String qualification;

  @Size(max = 45)
  protected String numeroContratCollectif;

  @Size(max = 2)
  protected String rangAdministratif;

  protected Boolean isContratResponsable;

  protected Boolean isContratCMU;

  @Size(max = 45)
  protected String destinataire;

  @Size(max = 2)
  protected String individuelOuCollectif;

  protected LocalDate situationDebut;

  protected LocalDate situationfin;

  @Size(max = 45)
  protected String motifFinSituation;

  @NotNull
  @Size(max = 10)
  protected String lienFamilial;

  @Size(max = 12)
  protected String categorieSociale;

  @Size(max = 4)
  protected String situationParticuliere;

  public Object clone() {
    return copyTo(createNewInstance());
  }

  public Object copyTo(Object target) {
    final CopyStrategy strategy = JAXBCopyStrategy.INSTANCE;
    return copyTo(null, target, strategy);
  }

  public Object copyTo(ObjectLocator locator, Object target, CopyStrategy strategy) {
    final Object draftCopy = ((target == null) ? createNewInstance() : target);
    if (draftCopy instanceof TypeContrat) {
      final TypeContrat copy = ((TypeContrat) draftCopy);
      if (this.numero != null) {
        String sourceNumero;
        sourceNumero = this.getNumero();
        String copyNumero =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numero", sourceNumero), sourceNumero));
        copy.setNumero(copyNumero);
      } else {
        copy.numero = null;
      }
      if (this.dateSouscription != null) {
        LocalDate sourceDateSouscription;
        sourceDateSouscription = this.getDateSouscription();
        LocalDate copyDateSouscription =
            ((LocalDate)
                strategy.copy(
                    LocatorUtils.property(locator, "dateSouscription", sourceDateSouscription),
                    sourceDateSouscription));
        copy.setDateSouscription(copyDateSouscription);
      } else {
        copy.dateSouscription = null;
      }
      if (this.dateResiliation != null) {
        LocalDate sourceDateResiliation;
        sourceDateResiliation = this.getDateResiliation();
        LocalDate copyDateResiliation =
            ((LocalDate)
                strategy.copy(
                    LocatorUtils.property(locator, "dateResiliation", sourceDateResiliation),
                    sourceDateResiliation));
        copy.setDateResiliation(copyDateResiliation);
      } else {
        copy.dateResiliation = null;
      }
      if (this.type != null) {
        String sourceType;
        sourceType = this.getType();
        String copyType =
            ((String)
                strategy.copy(LocatorUtils.property(locator, "type", sourceType), sourceType));
        copy.setType(copyType);
      } else {
        copy.type = null;
      }
      if (this.nomPorteur != null) {
        String sourceNomPorteur;
        sourceNomPorteur = this.getNomPorteur();
        String copyNomPorteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "nomPorteur", sourceNomPorteur),
                    sourceNomPorteur));
        copy.setNomPorteur(copyNomPorteur);
      } else {
        copy.nomPorteur = null;
      }
      if (this.prenomPorteur != null) {
        String sourcePrenomPorteur;
        sourcePrenomPorteur = this.getPrenomPorteur();
        String copyPrenomPorteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "prenomPorteur", sourcePrenomPorteur),
                    sourcePrenomPorteur));
        copy.setPrenomPorteur(copyPrenomPorteur);
      } else {
        copy.prenomPorteur = null;
      }
      if (this.civilitePorteur != null) {
        String sourceCivilitePorteur;
        sourceCivilitePorteur = this.getCivilitePorteur();
        String copyCivilitePorteur =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "civilitePorteur", sourceCivilitePorteur),
                    sourceCivilitePorteur));
        copy.setCivilitePorteur(copyCivilitePorteur);
      } else {
        copy.civilitePorteur = null;
      }
      if (this.numeroAdherent != null) {
        String sourceNumeroAdherent;
        sourceNumeroAdherent = this.getNumeroAdherent();
        String copyNumeroAdherent =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "numeroAdherent", sourceNumeroAdherent),
                    sourceNumeroAdherent));
        copy.setNumeroAdherent(copyNumeroAdherent);
      } else {
        copy.numeroAdherent = null;
      }
      if (this.qualification != null) {
        String sourceQualification;
        sourceQualification = this.getQualification();
        String copyQualification =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "qualification", sourceQualification),
                    sourceQualification));
        copy.setQualification(copyQualification);
      } else {
        copy.qualification = null;
      }
      if (this.numeroContratCollectif != null) {
        String sourceNumeroContratCollectif;
        sourceNumeroContratCollectif = this.getNumeroContratCollectif();
        String copyNumeroContratCollectif =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "numeroContratCollectif", sourceNumeroContratCollectif),
                    sourceNumeroContratCollectif));
        copy.setNumeroContratCollectif(copyNumeroContratCollectif);
      } else {
        copy.numeroContratCollectif = null;
      }
      if (this.rangAdministratif != null) {
        String sourceRangAdministratif;
        sourceRangAdministratif = this.getRangAdministratif();
        String copyRangAdministratif =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "rangAdministratif", sourceRangAdministratif),
                    sourceRangAdministratif));
        copy.setRangAdministratif(copyRangAdministratif);
      } else {
        copy.rangAdministratif = null;
      }
      if (this.isContratResponsable != null) {
        Boolean sourceIsContratResponsable;
        sourceIsContratResponsable = this.getIsContratResponsable();
        Boolean copyIsContratResponsable =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "isContratResponsable", sourceIsContratResponsable),
                    sourceIsContratResponsable));
        copy.setIsContratResponsable(copyIsContratResponsable);
      } else {
        copy.isContratResponsable = null;
      }
      if (this.isContratCMU != null) {
        Boolean sourceIsContratCMU;
        sourceIsContratCMU = this.getIsContratCMU();
        Boolean copyIsContratCMU =
            ((Boolean)
                strategy.copy(
                    LocatorUtils.property(locator, "isContratCMU", sourceIsContratCMU),
                    sourceIsContratCMU));
        copy.setIsContratCMU(copyIsContratCMU);
      } else {
        copy.isContratCMU = null;
      }
      if (this.destinataire != null) {
        String sourceDestinataire;
        sourceDestinataire = this.getDestinataire();
        String copyDestinataire =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "destinataire", sourceDestinataire),
                    sourceDestinataire));
        copy.setDestinataire(copyDestinataire);
      } else {
        copy.destinataire = null;
      }
      if (this.individuelOuCollectif != null) {
        String sourceIndividuelOuCollectif;
        sourceIndividuelOuCollectif = this.getIndividuelOuCollectif();
        String copyIndividuelOuCollectif =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "individuelOuCollectif", sourceIndividuelOuCollectif),
                    sourceIndividuelOuCollectif));
        copy.setIndividuelOuCollectif(copyIndividuelOuCollectif);
      } else {
        copy.individuelOuCollectif = null;
      }
      if (this.situationDebut != null) {
        LocalDate sourceSituationDebut;
        sourceSituationDebut = this.getSituationDebut();
        LocalDate copySituationDebut =
            ((LocalDate)
                strategy.copy(
                    LocatorUtils.property(locator, "situationDebut", sourceSituationDebut),
                    sourceSituationDebut));
        copy.setSituationDebut(copySituationDebut);
      } else {
        copy.situationDebut = null;
      }
      if (this.situationfin != null) {
        LocalDate sourceSituationfin;
        sourceSituationfin = this.getSituationfin();
        LocalDate copySituationfin =
            ((LocalDate)
                strategy.copy(
                    LocatorUtils.property(locator, "situationfin", sourceSituationfin),
                    sourceSituationfin));
        copy.setSituationfin(copySituationfin);
      } else {
        copy.situationfin = null;
      }
      if (this.motifFinSituation != null) {
        String sourceMotifFinSituation;
        sourceMotifFinSituation = this.getMotifFinSituation();
        String copyMotifFinSituation =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "motifFinSituation", sourceMotifFinSituation),
                    sourceMotifFinSituation));
        copy.setMotifFinSituation(copyMotifFinSituation);
      } else {
        copy.motifFinSituation = null;
      }
      if (this.lienFamilial != null) {
        String sourceLienFamilial;
        sourceLienFamilial = this.getLienFamilial();
        String copyLienFamilial =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "lienFamilial", sourceLienFamilial),
                    sourceLienFamilial));
        copy.setLienFamilial(copyLienFamilial);
      } else {
        copy.lienFamilial = null;
      }
      if (this.categorieSociale != null) {
        String sourceCategorieSociale;
        sourceCategorieSociale = this.getCategorieSociale();
        String copyCategorieSociale =
            ((String)
                strategy.copy(
                    LocatorUtils.property(locator, "categorieSociale", sourceCategorieSociale),
                    sourceCategorieSociale));
        copy.setCategorieSociale(copyCategorieSociale);
      } else {
        copy.categorieSociale = null;
      }
      if (this.situationParticuliere != null) {
        String sourceSituationParticuliere;
        sourceSituationParticuliere = this.getSituationParticuliere();
        String copySituationParticuliere =
            ((String)
                strategy.copy(
                    LocatorUtils.property(
                        locator, "situationParticuliere", sourceSituationParticuliere),
                    sourceSituationParticuliere));
        copy.setSituationParticuliere(copySituationParticuliere);
      } else {
        copy.situationParticuliere = null;
      }
    }
    return draftCopy;
  }

  public Object createNewInstance() {
    return new TypeContrat();
  }
}
