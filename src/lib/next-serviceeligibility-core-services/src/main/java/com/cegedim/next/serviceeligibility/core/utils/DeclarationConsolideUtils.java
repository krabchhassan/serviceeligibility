package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.Declaration;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DeclarationConsolideUtils {

  public static boolean isDroitsOuverts(
      List<DomaineDroit> domainesDroit, String dateJour, boolean isInsurer) {
    for (DomaineDroit domaine : domainesDroit) {
      String dateFin = DateUtils.getDateFin(domaine, isInsurer);
      if (isDroitOuvert(domaine.getPeriodeDroit().getPeriodeDebut(), dateFin, dateJour)) {
        return true;
      }
    }
    return false;
  }

  private static boolean isDroitOuvert(String debut, String fin, String dateJour) {
    return (debut.compareTo(dateJour) <= 0 && fin.compareTo(dateJour) >= 0)
        || (debut.compareTo(dateJour) >= 0 && debut.compareTo(fin) <= 0);
  }

  public static List<DomaineDroit> decoupageDomainesPeriodes(
      List<DomaineDroit> domaines, String dateJour, boolean isInsurer) {
    List<DomaineDroit> listeDecoupee = new ArrayList<>();
    String dateDebutTmp = getDateDebutDeclaration(domaines);
    if (dateDebutTmp != null) {
      List<String> datesFin = getDatesFinDeclaration(domaines, isInsurer);
      Collections.sort(datesFin);
      for (String dateFin : datesFin) {
        for (DomaineDroit domaine : domaines) {
          DomaineDroit decoupe = decoupageDomainePeriode(domaine, dateDebutTmp, dateFin, isInsurer);
          if (decoupe != null
              && isDroitOuvert(
                  decoupe.getPeriodeDroit().getPeriodeDebut(),
                  decoupe.getPeriodeDroit().getPeriodeFin(),
                  dateJour)) {
            listeDecoupee.add(decoupe);
          }
        }
        String parsed = DateUtils.parseDateAndNumberOfDays(dateFin, 1);
        dateDebutTmp = parsed != null ? parsed : dateDebutTmp;
      }
    }
    return !listeDecoupee.isEmpty() ? listeDecoupee : null;
  }

  public static DomaineDroit decoupageDomainePeriode(
      DomaineDroit domaine, String dateDebut, String dateFin, boolean isInsurer) {
    if (Boolean.TRUE.equals(domaine.getIsEditable())) {
      String periodeDebut = domaine.getPeriodeDroit().getPeriodeDebut();
      String periodeFin = DateUtils.getDateFin(domaine, isInsurer);
      if (periodeFin.compareTo(dateFin) >= 0 && periodeDebut.compareTo(dateFin) <= 0) {
        PeriodeDroit periodeDroit = new PeriodeDroit(domaine.getPeriodeDroit());
        periodeDroit.setPeriodeFin(dateFin);
        if (periodeDebut.compareTo(dateDebut) <= 0) {
          periodeDroit.setPeriodeDebut(dateDebut);
        }
        DomaineDroit domaineDroit = new DomaineDroit(domaine);
        domaineDroit.setPeriodeDroit(periodeDroit);
        return domaineDroit;
      }
    }
    return null;
  }

  /** Creer une declaration consolidee */
  public static DeclarationConsolide createDeclarationConsolidee(
      Declaration declaration,
      Date dateConsolidation,
      List<DomaineDroit> domaineDroits,
      List<String> servicesWanted,
      List<DomaineConvention> listConvention,
      String periodeDebut,
      String periodeFin,
      Boolean declarationValide,
      DomaineDroit produit) {
    DeclarationConsolide declarationConsolide = new DeclarationConsolide();
    declarationConsolide.setIdDeclarant(declaration.getIdDeclarant());
    declarationConsolide.setEffetDebut(declaration.getEffetDebut());
    declarationConsolide.setDateCreation(dateConsolidation);
    declarationConsolide.setUserCreation(Constants.JOB_620);
    declarationConsolide.setDateModification(dateConsolidation);
    declarationConsolide.setUserModification(Constants.JOB_620);
    declarationConsolide.setBeneficiaire(mapBenefToV1(declaration.getBeneficiaire()));
    List<Adresse> adresses = new ArrayList<>(declarationConsolide.getBeneficiaire().getAdresses());
    declarationConsolide.getBeneficiaire().setAdresses(adresses);
    declarationConsolide.setContrat(declaration.getContrat());
    if (domaineDroits != null) {
      domaineDroits.sort(Comparator.comparingInt(DomaineDroit::getNoOrdreDroit));
      declarationConsolide.setDomaineDroits(domaineDroits);
    }
    if (produit != null) {
      declarationConsolide.setProduit(produit);
    }
    declarationConsolide.setCodeServices(servicesWanted);
    declarationConsolide.setAMC_contrat(
        declaration.getIdDeclarant() + "-" + declaration.getContrat().getNumero());
    declarationConsolide.setIdDeclarations(declaration.get_id());
    declarationConsolide.setDateConsolidation(dateConsolidation);
    if (listConvention != null) {
      declarationConsolide.setListeDomainesConventions(listConvention);
    }
    declarationConsolide.setPeriodeDebut(periodeDebut);
    declarationConsolide.setPeriodeFin(periodeFin);
    declarationConsolide.setDeclarationValide(declarationValide);
    declarationConsolide.setNumAMCEchange(declaration.getContrat().getNumAMCEchange());
    return declarationConsolide;
  }

  private static Beneficiaire mapBenefToV1(BeneficiaireV2 beneficiaire) {
    Beneficiaire benefV1 = new Beneficiaire(beneficiaire);
    List<Adresse> adresses = new ArrayList<>();
    if (!CollectionUtils.isEmpty(beneficiaire.getAdresses())) {
      for (Adresse add : beneficiaire.getAdresses()) {
        adresses.add(new Adresse(add));
      }
    }
    benefV1.setAdresses(adresses);
    return benefV1;
  }

  /** Recupere une liste distincte de datePeriodeFin au format {@link String} */
  private static List<String> getDatesFinDeclaration(
      List<DomaineDroit> domaines, boolean isInsurer) {
    return domaines.stream()
        .filter(DomaineDroit::getIsEditable)
        .map(domaine -> DateUtils.getDateFin(domaine, isInsurer))
        .distinct()
        .collect(Collectors.toList());
  }

  /** Recupere la plus petite dateDebutPeriode des {@link DomaineDroit}, au format {@link String} */
  private static String getDateDebutDeclaration(List<DomaineDroit> domaines) {
    return domaines.stream()
        .filter(DomaineDroit::getIsEditable)
        .map(domaine -> domaine.getPeriodeDroit().getPeriodeDebut())
        .sorted()
        .findFirst()
        .orElse(null);
  }

  public static List<String> consolidateServicesDeclarations(
      List<Declaration> declarations, Declarant declarant) {
    Set<String> services = new HashSet<>();
    if (CollectionUtils.isNotEmpty(declarations)) {
      declarations.sort(Comparator.comparing(Declaration::getEffetDebut).reversed());
      // BLUE-6279 : on ne prend que la dernière déclaration pour connaitre les
      // services voulus.

      for (Declaration declaration : declarations) {
        fillServices(declaration, services);
      }

      List<Pilotage> pilotages = declarant.getPilotages();
      checkSynchroDateIfNotEditable(pilotages, services);

      return pilotages.stream()
          .map(Pilotage::getCodeService)
          .filter(services::contains)
          .distinct()
          .collect(Collectors.toCollection(ArrayList::new));
    }
    return new ArrayList<>();
  }

  public static void fillServices(Declaration declaration, Set<String> services) {
    if (Constants.V_3_1.equals(declaration.getVersionDeclaration())) {
      switch (declaration.getCarteTPaEditerOuDigitale()) {
        case "1":
          services.add(Constants.CARTE_TP);
          break;
        case "2", "4":
          services.add(Constants.CARTE_DEMATERIALISEE);
          break;
        case "3":
          services.add(Constants.CARTE_DEMATERIALISEE);
          services.add(Constants.CARTE_TP);
          break;
        default:
          break;
      }
    } else {
      services.add(Constants.CARTE_DEMATERIALISEE);
      services.add(Constants.CARTE_TP);
    }
  }

  private static void checkSynchroDateIfNotEditable(
      List<Pilotage> pilotages, Set<String> services) {
    if (services.contains(Constants.CARTE_TP) && CollectionUtils.isNotEmpty(pilotages)) {
      Optional<Pilotage> pilotageCartePapierOptional =
          pilotages.stream()
              .filter(pilotage -> Constants.CARTE_TP.equals(pilotage.getCodeService()))
              .findFirst();
      if (pilotageCartePapierOptional.isPresent()
          && pilotageCartePapierOptional.get().getDateSynchronisation() != null
          && Boolean.FALSE.equals(pilotageCartePapierOptional.get().getIsCarteEditable())) {
        services.remove(Constants.CARTE_TP);
      }
    }
  }

  public static String getDateDebutMinimumOnDomaineDroits(List<DomaineDroit> domaineDroits) {
    return domaineDroits.stream()
        .map(domaineDroit -> domaineDroit.getPeriodeDroit().getPeriodeDebut())
        .filter(Objects::nonNull)
        .min(String::compareTo)
        .orElse(null);
  }

  public static String getDateDebutMinimum(List<Declaration> declarations) {
    return getDateDebutMinimumOnDomaineDroits(
        declarations.stream()
            .flatMap(declaration -> declaration.getDomaineDroits().stream())
            .toList());
  }

  public static String getDateFinMinimumOnDomaineDroits(
      List<DomaineDroit> domaineDroits, String clientType) {
    boolean isInsurer = Constants.CLIENT_TYPE_INSURER.equals(clientType);
    return domaineDroits.stream()
        .map(domaine -> DateUtils.getDateFin(domaine, isInsurer))
        .min(String::compareTo)
        .orElse(null);
  }

  public static String getDateFinFermetureMaxOnDomaineDroits(List<DomaineDroit> domaineDroits) {
    return domaineDroits.stream()
        .map(
            domaine ->
                StringUtils.defaultIfBlank(
                    domaine.getPeriodeDroit().getPeriodeFermetureFin(),
                    domaine.getPeriodeDroit().getPeriodeFin()))
        .max(String::compareTo)
        .orElse(null);
  }

  public static String getDateFinMinimum(List<Declaration> declarations, String clientType) {
    return getDateFinMinimumOnDomaineDroits(
        declarations.stream()
            .flatMap(declaration -> declaration.getDomaineDroits().stream())
            .toList(),
        clientType);
  }

  public static PeriodeDroit getMinMaxPeriodesDomaineDroit(
      Declaration declaration, boolean isInsurer) {
    String minPeriodeDebut = null;
    String maxPeriodeFin = null;
    boolean isR = Constants.CODE_ETAT_INVALIDE.equals(declaration.getCodeEtat());
    Function<DomaineDroit, String> getFin =
        isR
            ? dom -> dom.getPeriodeDroit().getPeriodeFermetureFin()
            : dom -> DateUtils.getDateFin(dom, isInsurer);
    for (int i = 0; i < declaration.getDomaineDroits().size(); i++) {
      DomaineDroit domaine = declaration.getDomaineDroits().get(i);
      if (i == 0) {
        minPeriodeDebut = domaine.getPeriodeDroit().getPeriodeDebut();
        maxPeriodeFin = getFin.apply(domaine);
      } else {
        minPeriodeDebut =
            DateUtils.getMinDate(
                minPeriodeDebut,
                domaine.getPeriodeDroit().getPeriodeDebut(),
                DateUtils.SLASHED_FORMATTER);
        maxPeriodeFin =
            DateUtils.getMaxDate(maxPeriodeFin, getFin.apply(domaine), DateUtils.SLASHED_FORMATTER);
      }
    }

    PeriodeDroit periodeDroit = new PeriodeDroit();
    periodeDroit.setPeriodeDebut(minPeriodeDebut);
    periodeDroit.setPeriodeFin(maxPeriodeFin);
    return periodeDroit;
  }

  public static List<Periode> computePeriodesWithoutSuspensions(
      List<Periode> periodesSuspension, Periode periodeDeclaration) {
    List<Periode> periodeList = new ArrayList<>();
    periodeList.add(periodeDeclaration);

    for (Periode periodeSuspension : periodesSuspension) {
      List<Periode> iterateList = new ArrayList<>(periodeList);
      periodeList.clear();

      for (Periode periodeDecl : iterateList) {
        LocalDate debutPeriodeDecl =
            LocalDate.parse(periodeDecl.getDebut(), DateUtils.SLASHED_FORMATTER);
        LocalDate finPeriodeDecl =
            LocalDate.parse(periodeDecl.getFin(), DateUtils.SLASHED_FORMATTER);
        LocalDate debutSuspension = DateUtils.parseAnyFormat(periodeSuspension.getDebut());
        LocalDate finSuspension = DateUtils.parseAnyFormat(periodeSuspension.getFin());

        if (finSuspension == null && debutSuspension.isBefore(debutPeriodeDecl)) {
          return Collections.emptyList();
        }

        handlePeriod(
            debutPeriodeDecl,
            finPeriodeDecl,
            debutSuspension,
            finSuspension,
            periodeDecl,
            periodeList);
      }
    }
    return periodeList;
  }

  private static void handlePeriod(
      LocalDate debutPeriodeDecl,
      LocalDate finPeriodeDecl,
      LocalDate debutSuspension,
      LocalDate finSuspension,
      Periode periodeDecl,
      List<Periode> periodeList) {
    if (!DateUtils.isPeriodeValide(debutSuspension, finSuspension)) {
      periodeList.add(periodeDecl);
    } else if (debutPeriodeDecl.isBefore(debutSuspension)
        && (finSuspension != null && finPeriodeDecl.isAfter(finSuspension))) {
      createPeriodBeforeSuspension(debutSuspension, periodeDecl, periodeList);
      createPeriodAfterSuspension(finSuspension, periodeDecl, periodeList);
    } else if (!debutPeriodeDecl.isBefore(debutSuspension)
        && (finSuspension != null && !finSuspension.isBefore(debutPeriodeDecl))) {
      if (finSuspension.isBefore(finPeriodeDecl)) {
        createPeriodAfterSuspension(finSuspension, periodeDecl, periodeList);
      }
    } else if (!finPeriodeDecl.isBefore(debutSuspension)
        && !debutPeriodeDecl.isAfter(debutSuspension)) {
      if (finSuspension != null || !debutSuspension.equals(debutPeriodeDecl)) {
        createPeriodBeforeSuspension(debutSuspension, periodeDecl, periodeList);
      }
    } else {
      periodeList.add(periodeDecl);
    }
  }

  private static void createPeriodAfterSuspension(
      LocalDate finSuspension, Periode periodeDecl, List<Periode> periodeList) {
    periodeList.add(
        new Periode(
            DateUtils.formatDate(finSuspension.plusDays(1), DateUtils.SLASHED_FORMATTER),
            periodeDecl.getFin()));
  }

  private static void createPeriodBeforeSuspension(
      LocalDate debutSuspension, Periode periodeDecl, List<Periode> periodeList) {
    periodeList.add(
        new Periode(
            periodeDecl.getDebut(),
            DateUtils.formatDate(debutSuspension.minusDays(1), DateUtils.SLASHED_FORMATTER)));
  }

  public static List<Periode> getPeriodeSuspensionList(List<Declaration> declarations) {
    List<Periode> suspensionList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(declarations)) {
      Optional<Declaration> mostRecentDeclaration =
          declarations.stream().max((Comparator.comparing(Declaration::getEffetDebut)));
      if (mostRecentDeclaration.isPresent()
          && CollectionUtils.isNotEmpty(
              mostRecentDeclaration.get().getContrat().getPeriodeSuspensions())) {
        return mostRecentDeclaration
            .map(
                declaration ->
                    declaration.getContrat().getPeriodeSuspensions().stream()
                        .map(
                            periodeSuspensionDeclaration ->
                                new Periode(
                                    periodeSuspensionDeclaration.getDebut(),
                                    periodeSuspensionDeclaration.getFin()))
                        .toList())
            .orElse(suspensionList);
      }
    }
    return suspensionList;
  }
}
