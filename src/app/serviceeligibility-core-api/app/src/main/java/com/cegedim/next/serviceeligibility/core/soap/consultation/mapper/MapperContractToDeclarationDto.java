package com.cegedim.next.serviceeligibility.core.soap.consultation.mapper;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.*;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAdresse;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperAffiliation;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.mapping.interfaces.MapperDeclarant;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.utils.TypeProfondeurRechercheService;
import com.cegedim.next.serviceeligibility.core.business.declarant.service.DeclarantService;
import com.cegedim.next.serviceeligibility.core.model.domain.Adresse;
import com.cegedim.next.serviceeligibility.core.model.domain.Affiliation;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.BeneficiaireContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.ContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.DomaineDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.PeriodeDroitContractTP;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.ExceptionServiceFormatDate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperContractToDeclarationDto {
  public static final String HOSP = "HOSP";
  public static final String SEL_ROC = "SEL-ROC";

  private final ContractToDeclarationMapper mapper;

  private final MapperAdresse mapperAdresse;

  private final MapperDeclarant mapperDeclarant;

  private final DeclarantService declarantService;

  @Getter @Setter
  private TypeProfondeurRechercheService profondeurRecherche =
      TypeProfondeurRechercheService.AVEC_FORMULES;

  @Getter @Setter private boolean formatV2;

  @Getter @Setter private boolean formatV3;

  @Getter @Setter private String numAmcRecherche;

  private final MapperAffiliation mapperAffiliation;

  private final MapperContratTPToContratDto mapperContratTPToContratDto;

  public List<DeclarationDto> entityListToDtoList(
      List<ContractTP> list,
      List<String> segmentRecherche,
      String typeRechercheSegment,
      Date dateFinDemande,
      String dateReference) {
    List<DeclarationDto> dtos = new ArrayList<>();
    for (ContractTP item : list) {
      dtos.addAll(
          mapDeclaration(
              item, segmentRecherche, typeRechercheSegment, dateFinDemande, dateReference));
    }
    return dtos;
  }

  List<DeclarationDto> mapDeclaration(
      ContractTP c,
      List<String> segmentRecherche,
      String typeRechercheSegment,
      Date dateFinDemande,
      String dateReference) {

    List<DeclarationDto> list = new ArrayList<>();
    Declarant declarant = declarantService.findById(c.getIdDeclarant());

    List<BeneficiaireContractTP> benefs = c.getBeneficiaires();
    for (BeneficiaireContractTP benef : benefs) {
      DeclarationDto d = new DeclarationDto();
      d.setEffetDebut(Date.from(benef.getDateModification().toInstant(ZoneOffset.UTC)));
      if (declarant != null) {
        d.setDeclarantAmc(
            mapperDeclarant.entityToDto(
                declarant, profondeurRecherche, formatV2, formatV3, numAmcRecherche));
      }

      d.setBeneficiaire(mapBenef(benef));

      // map contract
      ContratDto contratDto = mapperContratTPToContratDto.mapContrat(c, benef, dateReference);

      d.setContrat(contratDto);
      // map domaine droit
      boolean checkDomaines =
          !profondeurRecherche.equals(TypeProfondeurRechercheService.SANS_DOMAINES);

      if (checkDomaines) {
        List<DomaineDroitContractTP> domaines = benef.getDomaineDroits();
        List<DomaineDroitDto> domainesDto = new ArrayList<>();
        if (domaines != null && !domaines.isEmpty()) {
          // we need to create one DomaineDroitDto for each periode droit
          // ( there should
          // be only one because we have already exploded them in the
          // VisiodroitUtility.eclaterBeneficiaresParDomaine
          recrerDomaines(
              segmentRecherche,
              typeRechercheSegment,
              declarant,
              domaines,
              domainesDto,
              dateFinDemande);
        }
        d.setDomaineDroits(domainesDto);
      }
      list.add(d);
    }

    return list;
  }

  private void recrerDomaines(
      List<String> segmentRecherche,
      String typeRechercheSegment,
      Declarant declarant,
      List<DomaineDroitContractTP> domaines,
      List<DomaineDroitDto> domainesDto,
      Date dateFinDemande) {
    if (declarant != null) {
      Date dateRenouvellement =
          this.setDateRenouvellement(segmentRecherche, typeRechercheSegment, declarant);
      for (DomaineDroitContractTP domaine : domaines) {
        for (Garantie garantie : ListUtils.emptyIfNull(domaine.getGaranties())) {
          for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
            for (ReferenceCouverture referenceCouverture :
                ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
              for (NaturePrestation naturePrestation :
                  ListUtils.emptyIfNull(referenceCouverture.getNaturesPrestation())) {
                PrioriteDroitContrat prioriteDroitContrat =
                    naturePrestation.getPrioritesDroit().get(0);
                RemboursementContrat remboursementContrat =
                    naturePrestation.getRemboursements().get(0);
                for (PeriodeDroitContractTP periodeDroitContractTP :
                    naturePrestation.getPeriodesDroit()) {
                  domainesDto.add(
                      mapDomaine(
                          domaine,
                          periodeDroitContractTP,
                          dateRenouvellement,
                          prioriteDroitContrat,
                          produit,
                          garantie,
                          remboursementContrat,
                          referenceCouverture,
                          naturePrestation.getPrestations(),
                          naturePrestation.getConventionnements(),
                          dateFinDemande));
                }
              }
            }
          }
        }
      }
    }
  }

  private DomaineDroitDto mapDomaine(
      DomaineDroitContractTP domaine,
      PeriodeDroitContractTP periode,
      Date dateRenouvellement,
      PrioriteDroitContrat prioriteDroitContrat,
      Produit produit,
      Garantie garantie,
      RemboursementContrat remboursementContrat,
      ReferenceCouverture referenceCouverture,
      List<PrestationContrat> prestations,
      List<ConventionnementContrat> conventionnements,
      Date dateFinDemande) {
    DomaineDroitDto dto = mapper.domaineDroitContractTPToDomaineDroitDto(domaine);

    PrioriteDroitDto prioriteDroitDto =
        mapper.prioriteDroitToPrioriteDroitDto(prioriteDroitContrat);
    dto.setPrioriteDroit(prioriteDroitDto);

    dto.setCategorie(domaine.getCode());
    dto.setCodeExterneProduit(produit.getCodeExterneProduit());
    dto.setCodeProduit(produit.getCodeProduit());
    dto.setLibelleProduit(produit.getLibelleProduit());
    dto.setCodeGarantie(garantie.getCodeGarantie());
    dto.setLibelleGarantie(garantie.getLibelleGarantie());
    dto.setTauxRemboursement(remboursementContrat.getTauxRemboursement());
    dto.setUniteTauxRemboursement(remboursementContrat.getUniteTauxRemboursement());
    dto.setReferenceCouverture(referenceCouverture.getReferenceCouverture());
    dto.setPrestations(mapperPrestations(prestations));
    dto.setConventionnements(mapperConventionnements(conventionnements));
    if (HOSP.equals(domaine.getCode())) {
      dto.setPeriodeDroit(mapPeriode(periode, dateRenouvellement, dateFinDemande));
    } else {
      dto.setPeriodeDroit(mapPeriode(periode, null, null));
    }
    if (!formatV2 && !formatV3) {
      dto.setReferenceCouverture(null);
    }
    if (!profondeurRecherche.equals(TypeProfondeurRechercheService.AVEC_FORMULES)) {
      for (PrestationDto prest : dto.getPrestations()) {
        prest.setFormule(null);
      }
    }
    return dto;
  }

  public List<ConventionnementDto> mapperConventionnements(
      List<ConventionnementContrat> conventionnements) {
    List<ConventionnementDto> dtoList = new ArrayList<>();
    for (ConventionnementContrat conventionnement : conventionnements) {
      dtoList.add(mapper.conventionnementContratToConventionnementDto(conventionnement));
    }
    return dtoList;
  }

  public List<PrestationDto> mapperPrestations(final List<PrestationContrat> list) {
    List<PrestationDto> dtoList = new ArrayList<>();
    for (PrestationContrat prestation : list) {
      dtoList.add(mapper.prestationContratToPrestationDto(prestation));
    }
    return dtoList;
  }

  private PeriodeDroitDto mapPeriode(
      PeriodeDroitContractTP periode, Date dateRenouvellement, Date dateFinDemande) {

    String periodeDebut = periode.getPeriodeDebut();
    String periodeFin = periode.getPeriodeFin();
    String periodeFinFermeture = periode.getPeriodeFinFermeture();

    periode.setPeriodeDebut(null);
    periode.setPeriodeFin(null);
    periode.setPeriodeFinFermeture(null);

    PeriodeDroitDto dto = mapper.periodeDroitContractTPToPeriodeDroitDto(periode);

    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      if (StringUtils.isNotEmpty(periodeDebut)) {
        dto.setPeriodeDebut(sdf.parse(periodeDebut));
      }
      if (StringUtils.isNotEmpty(periodeFin)) {
        dto.setPeriodeFin(sdf.parse(periodeFin));
      }
      if (StringUtils.isNotEmpty(periodeFinFermeture)) {
        dto.setPeriodeFinInitiale(sdf.parse(periodeFinFermeture));
      }
    } catch (ParseException e) {
      throw new ExceptionServiceFormatDate();
    }

    if (dateRenouvellement != null) {
      if (dateFinDemande != null
          && dto.getPeriodeFin() != null
          && dateFinDemande.after(dto.getPeriodeFin())) {
        dto.setDateRenouvellement(dto.getPeriodeFin());
      } else {
        dto.setDateRenouvellement(dateRenouvellement);
      }
    }
    return dto;
  }

  private BeneficiaireDto mapBenef(BeneficiaireContractTP beneficiaireContract) {
    Affiliation affiliation = beneficiaireContract.getAffiliation();
    beneficiaireContract.setAffiliation(null);
    List<Adresse> adresses = beneficiaireContract.getAdresses();
    beneficiaireContract.setAdresses(null);
    BeneficiaireDto dto = mapper.beneficiaireContractTPToBeneficiaireDto(beneficiaireContract);
    dto.setAdresses(
        mapperAdresse.entityListToDtoList(
            adresses, profondeurRecherche, formatV2, formatV3, numAmcRecherche));
    dto.setAffiliation(
        mapperAffiliation.entityToDto(
            affiliation, profondeurRecherche, formatV2, formatV3, numAmcRecherche));
    beneficiaireContract.setAffiliation(affiliation);
    beneficiaireContract.setAdresses(adresses);
    return dto;
  }

  private Date setDateRenouvellement(
      List<String> domaineList, String typeRechercheSegment, Declarant declarant) {
    if (!domaineList.contains(HOSP) && !HOSP.equals(typeRechercheSegment)) {
      return null;
    }
    List<Pilotage> pilotages = declarant.getPilotages();
    Pilotage rocPilotage =
        pilotages.stream()
            .filter(pilotage -> SEL_ROC.equals(pilotage.getCodeService()))
            .findAny()
            .orElse(null);

    if (rocPilotage == null || rocPilotage.getCaracteristique() == null) {
      return null;
    }

    Integer dureeValidite = rocPilotage.getCaracteristique().getDureeValidite();
    String periodeValidite = rocPilotage.getCaracteristique().getPeriodeValidite();
    if (dureeValidite != null) {
      Date currentDate = new Date();
      Calendar c = Calendar.getInstance();
      c.setTime(currentDate);
      c.add(Calendar.DATE, dureeValidite);
      DateUtils.resetTimeInDate(c);
      return c.getTime();
    } else if (StringUtils.isNotBlank(periodeValidite)) {
      Calendar calendar = Calendar.getInstance();
      switch (periodeValidite) {
        case "debutMois":
          calendar.add(Calendar.MONTH, 1);
          calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
          DateUtils.resetTimeInDate(calendar);
          return calendar.getTime();
        case "milieuMois":
          Date currentDate = new Date();
          calendar.setTime(currentDate);
          calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 15);
          if (currentDate.after(calendar.getTime())) {
            calendar.add(Calendar.MONTH, 1);
          }
          DateUtils.resetTimeInDate(calendar);
          return calendar.getTime();
        // finMois
        default:
          calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
          DateUtils.resetTimeInDate(calendar);
          return calendar.getTime();
      }
    }
    return null;
  }
}
