package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AdresseDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.AffiliationDto;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class BeneficiaireContractDto {
  private String dateRadiation;
  private String dateNaissance;
  private String rangNaissance;
  private String nirBeneficiaire;
  private String cleNirBeneficiaire;
  private String nirOd1;
  private String cleNirOd1;
  private String nirOd2;
  private String cleNirOd2;
  private String numeroPersonne;
  private String refExternePersonne;
  private String rangAdministratif;
  private String categorieSociale;
  private String situationParticuliere;
  private String modePaiementPrestations;
  private String dateAdhesionMutuelle;
  private String dateDebutAdhesionIndividuelle;
  private String numeroAdhesionIndividuelle;
  private AffiliationDto affiliation;
  private List<AdresseDto> adresses;
  private Map<String, List<DomaineDroitContractDto>> maillesDomaineDroits;
  private List<CodePeriodeContractDto> regimesParticuliers;
  private List<PeriodeContractDto> periodesMedecinTraitant;
  private List<CodePeriodeContractDto> situationsParticulieres;
  private List<NirRattachementRODto> affiliationsRO;
  private List<TeletransmissionDto> teletransmissions;
}
