package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.cartedemat;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import lombok.Data;

/** Classe qui mappe le document BenefCarteDemat */
@Data
public class BenefCarteDematDto implements GenericDto {

  private static final long serialVersionUID = 1L;

  private String nomBeneficiaire;
  private String nomPatronymique;
  private String nomMarital;
  private String prenom;
  private String qualite;
  private String typeAssure;
  private String lienFamilial;
  private String rangAdministratif;
  private String nirOd1;
  private String cleNirOd1;
  private String nirOd2;
  private String cleNirOd2;
  private String nirBeneficiaire;
  private String cleNirBeneficiaire;
  private String dateNaissance;
  private String rangNaissance;
  private String numeroPersonne;
  private String refExternePersonne;
  private String regimeOD1;
  private String caisseOD1;
  private String centreOD1;
  private String regimeOD2;
  private String caisseOD2;
  private String centreOD2;
  private Boolean hasMedecinTraitant;
  private String regimeParticulier;
  private Boolean isBeneficiaireACS;
  private Boolean isTeleTransmission;
  private XMLGregorianCalendar debutAffiliation;
  private String modePaiementPrestations;
  private List<BeneficiaireCouvertureDto> couverture;
}
