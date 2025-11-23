package com.cegedim.next.serviceeligibility.core.bdd.webservice.utils;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.DeclarationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.PrioriteDroitDto;
import com.cegedim.next.serviceeligibility.core.bdd.webservice.dto.data.DemandeInfoBeneficiaire;
import com.cegedim.next.serviceeligibility.core.model.domain.DomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.Pilotage;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;

public class VisiodroitUtils {

  private VisiodroitUtils() {}

  /**
   * Eclater une liste de declarations par domaine
   *
   * @param declarations liste des declaration à eclater
   * @return liste des declarations avec les domaines eclates
   */
  public static List<Declaration> eclaterDeclarationsParDomaine(List<Declaration> declarations) {
    List<Declaration> declarationsEclatees = new ArrayList<>();
    for (Declaration declaration : declarations) {
      List<DomaineDroit> domainesDroits = declaration.getDomaineDroits();
      for (DomaineDroit domaine : domainesDroits) {
        Declaration declEclatee = new Declaration(declaration);
        declEclatee.getDomaineDroits().clear();
        declEclatee.getDomaineDroits().add(domaine);
        declarationsEclatees.add(declEclatee);
      }
    }
    return declarationsEclatees;
  }

  /** Eclater une liste de beneficiaires contrat par domaine, mais avant */
  public static int eclaterBeneficiairesParDomaine(
      List<ContractTP> contrats,
      List<ContractTP> contratsEclates,
      boolean limitWaranties,
      DemandeInfoBeneficiaire infoBenef) {
    int exceptionDetected = -1;
    // 1 = ExceptionPriorisationGaranties
    // 2 = ExceptionServiceDroitNonOuvert - Detecter 6002 : Droits du bénéficiaire
    // non ouverts

    Set<String> segmentsRecherche = VisiodroitUtils.getSegmentRecherche(infoBenef);

    for (ContractTP contrat : contrats) {
      List<BeneficiaireContractTP> beneficiairesContract = contrat.getBeneficiaires();
      contrat.setBeneficiaires(null);
      for (BeneficiaireContractTP beneficiaireContrat : beneficiairesContract) {

        List<DomaineDroitContractTP> domainesDroits = beneficiaireContrat.getDomaineDroits();
        beneficiaireContrat.setDomaineDroits(null);
        List<DomaineDroitContractTP> domainesDroitsExploded = new ArrayList<>();
        // we have multiple periodeDroit in each domaine so we need to have
        // only one period in one domaine
        for (DomaineDroitContractTP domaine : domainesDroits) {
          if (segmentsRecherche.contains(domaine.getCode())) {
            List<Garantie> existingGaranties = eclateGaranties(domaine.getGaranties());
            domaine.setGaranties(null);
            if (!limitWaranties) {
              for (Garantie garantie : existingGaranties) {
                DomaineDroitContractTP domaineClone = new DomaineDroitContractTP(domaine);
                List<Garantie> garantieList = new ArrayList<>();
                garantieList.add(garantie);
                domaineClone.setGaranties(garantieList);
                domainesDroitsExploded.add(domaineClone);
              }
            } else {
              existingGaranties =
                  existingGaranties.stream()
                      .filter(
                          garantie -> {
                            PeriodeDroitContractTP firstPeriode =
                                getFirstPeriodeInGarantie(garantie);
                            return firstPeriode != null
                                && !TypePeriode.ONLINE.equals(firstPeriode.getTypePeriode())
                                && isPeriodeDroitValide(
                                    firstPeriode,
                                    infoBenef.getDateReference(),
                                    infoBenef.getDateFin());
                          })
                      .collect(Collectors.toList());
              existingGaranties.sort(
                  Comparator.comparing(VisiodroitUtils::getFirstPrioCodeInGarantie));
              DomaineDroitContractTP domaineClone = new DomaineDroitContractTP(domaine);
              if (CollectionUtils.isNotEmpty(existingGaranties)) {
                Garantie period = existingGaranties.getFirst();
                List<Garantie> garantieList = new ArrayList<>();
                garantieList.add(period);
                domaineClone.setGaranties(garantieList);
                domainesDroitsExploded.add(domaineClone);
                if (existingGaranties.size() > 1
                    && Objects.equals(
                        getFirstPrioCodeInGarantie(existingGaranties.get(1)),
                        getFirstPrioCodeInGarantie(period))) {
                  exceptionDetected = 1;
                }
              } else {
                exceptionDetected = 2;
              }
            }
          }
        }
        beneficiaireContrat.setDomaineDroits(domainesDroitsExploded);
        for (DomaineDroitContractTP domaine : domainesDroitsExploded) {
          BeneficiaireContractTP beneficiaireEclate =
              new BeneficiaireContractTP(beneficiaireContrat);
          beneficiaireEclate.getDomaineDroits().clear();
          beneficiaireEclate.getDomaineDroits().add(domaine);
          ContractTP contratEclate = new ContractTP(contrat);
          contratEclate.setBeneficiaires(List.of(beneficiaireEclate));
          contratsEclates.add(contratEclate);
        }
      }
    }

    return CollectionUtils.isNotEmpty(contratsEclates) ? -1 : exceptionDetected;
  }

  private static List<Garantie> eclateGaranties(List<Garantie> garanties) {
    List<Garantie> newGaranties = new ArrayList<>();
    for (Garantie garantie : ListUtils.emptyIfNull(garanties)) {
      for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
        for (ReferenceCouverture referenceCouverture :
            ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
          for (NaturePrestation naturePrestation :
              ListUtils.emptyIfNull(referenceCouverture.getNaturesPrestation())) {
            for (PeriodeDroitContractTP periodeDroitContractTP :
                naturePrestation.getPeriodesDroit()) {
              PeriodeDroitContractTP newPeriodeDroitContractTP =
                  new PeriodeDroitContractTP(periodeDroitContractTP);

              NaturePrestation newNaturePrestation = new NaturePrestation(naturePrestation);
              newNaturePrestation.setPeriodesDroit(
                  new ArrayList<>(List.of(newPeriodeDroitContractTP)));

              ReferenceCouverture newReferenceCouverture =
                  new ReferenceCouverture(referenceCouverture);
              newReferenceCouverture.setNaturesPrestation(
                  new ArrayList<>(List.of(newNaturePrestation)));

              Produit newProduit = new Produit(produit);
              newProduit.setReferencesCouverture(new ArrayList<>(List.of(newReferenceCouverture)));

              Garantie newGarantie = new Garantie(garantie);
              newGarantie.setProduits(new ArrayList<>(List.of(newProduit)));

              newGaranties.add(newGarantie);
            }
          }
        }
      }
    }
    return newGaranties;
  }

  private static PeriodeDroitContractTP getFirstPeriodeInGarantie(Garantie garantie) {
    for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
      for (ReferenceCouverture referenceCouverture :
          ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
        if (CollectionUtils.isNotEmpty(referenceCouverture.getNaturesPrestation())) {
          return referenceCouverture.getNaturesPrestation().getFirst().getPeriodesDroit().get(0);
        }
      }
    }
    return null;
  }

  private static String getFirstPrioCodeInGarantie(Garantie garantie) {
    for (Produit produit : ListUtils.emptyIfNull(garantie.getProduits())) {
      for (ReferenceCouverture referenceCouverture :
          ListUtils.emptyIfNull(produit.getReferencesCouverture())) {
        if (CollectionUtils.isNotEmpty(referenceCouverture.getNaturesPrestation())) {
          return referenceCouverture
              .getNaturesPrestation()
              .getFirst()
              .getPrioritesDroit()
              .getFirst()
              .getCode();
        }
      }
    }

    return Constants.CODE_DOMAINE_MULTIPLE;
  }

  private static boolean isPeriodeDroitValide(
      PeriodeDroitContractTP periodeDroit, Date dateReference, Date dateFin) {
    Date periodeDebut =
        DateUtils.parseDate(periodeDroit.getPeriodeDebut(), DateUtils.FORMATTERSLASHED);
    Date periodeFin = DateUtils.parseDate(periodeDroit.getPeriodeFin(), DateUtils.FORMATTERSLASHED);
    Date truncatedDateReference =
        org.apache.commons.lang3.time.DateUtils.truncate(dateReference, Calendar.DATE);
    if (dateFin == null) {
      return periodeDebut.compareTo(truncatedDateReference) <= 0
          && periodeFin.compareTo(truncatedDateReference) >= 0;
    } else {
      Date truncatedDateFin =
          org.apache.commons.lang3.time.DateUtils.truncate(dateFin, Calendar.DATE);
      return periodeDebut.compareTo(truncatedDateFin) <= 0
          && periodeFin.compareTo(truncatedDateReference) >= 0;
    }
  }

  /**
   * Recupere la priorité contrat d'un domaine si elle existe pour classer les déclarations entres
   * elles. Permet de trier les déclaration par la suite
   *
   * @param declarationDtos la liste des declarations dto
   * @param nirBeneficiaire nir beneficiaire
   */
  public static void setPrioritesBeneficiaire(
      List<DeclarationDto> declarationDtos, String nirBeneficiaire) {
    for (DeclarationDto declarationDto : declarationDtos) {
      if (CollectionUtils.isNotEmpty(declarationDto.getDomaineDroits())) {
        PrioriteDroitDto priorite = declarationDto.getDomaineDroits().getFirst().getPrioriteDroit();
        String prioriteDroit = priorite.getCode();

        if (nirBeneficiaire.equals(priorite.getNirPrio1())) {
          prioriteDroit = priorite.getPrioDroitNir1();
        } else if (nirBeneficiaire.equals(priorite.getNirPrio2())) {
          prioriteDroit = priorite.getPrioDroitNir2();
        }

        priorite.setCode(prioriteDroit);
        priorite.setLibelle(prioriteDroit);
      }
    }
  }

  /**
   * Construit un set unique des segments de recherche, selon les informations fournis dans la
   * requete
   *
   * @param infoBenef l'objet contenant la requete
   * @return un set de String
   */
  public static Set<String> getSegmentRecherche(DemandeInfoBeneficiaire infoBenef) {
    Set<String> segmentsRecherche = new HashSet<>();

    if (infoBenef != null) {
      switch (infoBenef.getTypeRechercheSegment()) {
        case LISTE_SEGMENT:
          for (String seg : infoBenef.getListeSegmentRecherche()) {
            if (StringUtils.isNotBlank(seg)) {
              segmentsRecherche.add(seg);
            }
          }
          break;
        case MONO_SEGMENT:
          if (StringUtils.isNotBlank(infoBenef.getSegmentRecherche())) {
            segmentsRecherche.add(infoBenef.getSegmentRecherche());
          }
          break;
        default:
          break;
      }
    }

    return segmentsRecherche;
  }

  public static String computeDateRenouvellement(Declarant declarant) {
    if (declarant == null) {
      return null;
    }
    List<Pilotage> pilotages = declarant.getPilotages();
    Pilotage rocPilotage =
        pilotages.stream()
            .filter(pilotage -> Constants.SEL_ROC.equals(pilotage.getCodeService()))
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
      return DateUtils.formatDate(c.getTime());
    } else if (StringUtils.isNotBlank(periodeValidite)) {
      Calendar calendar = Calendar.getInstance();
      switch (periodeValidite) {
        case Constants.DEBUT_MOIS:
          calendar.add(Calendar.MONTH, 1);
          calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
          DateUtils.resetTimeInDate(calendar);
          return DateUtils.formatDate(calendar.getTime());
        case Constants.MILIEU_MOIS:
          Date currentDate = new Date();
          calendar.setTime(currentDate);
          calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 15);
          if (currentDate.after(calendar.getTime())) {
            calendar.add(Calendar.MONTH, 1);
          }
          DateUtils.resetTimeInDate(calendar);
          return DateUtils.formatDate(calendar.getTime());
        // finMois
        default:
          calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
          DateUtils.resetTimeInDate(calendar);
          return DateUtils.formatDate(calendar.getTime());
      }
    }
    return null;
  }

  public static String computeDateRenouvellementSansParametrage(
      Date dateFinDroit, Date dateFinInterro) {
    Date now = DateUtils.removeTimeInDate(new Date());
    Date dateFinDroitF = DateUtils.removeTimeInDate(dateFinDroit);
    Date dateFinInterroF = DateUtils.removeTimeInDate(dateFinInterro);
    Date dateCalculee = dateFinDroitF;
    if (dateFinInterroF != null && !dateFinDroitF.before(dateFinInterroF)) {
      if (dateFinInterroF.before(now)) {
        dateCalculee = now;
      } else {
        dateCalculee = dateFinInterroF;
      }
    }

    return DateUtils.formatDate(dateCalculee);
  }
}
