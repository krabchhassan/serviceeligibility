package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkActions;
import com.cegedim.next.serviceeligibility.core.job.batch.Rejection;
import com.cegedim.next.serviceeligibility.core.model.domain.*;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.BenefCarteDemat;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.Convention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.DomaineConvention;
import com.cegedim.next.serviceeligibility.core.model.domain.cartedemat.LienContrat;
import com.cegedim.next.serviceeligibility.core.model.entity.Declarant;
import com.cegedim.next.serviceeligibility.core.model.entity.DeclarationConsolide;
import com.cegedim.next.serviceeligibility.core.model.entity.DomaineCarte;
import com.cegedim.next.serviceeligibility.core.model.entity.card.CarteDemat;
import com.cegedim.next.serviceeligibility.core.model.enumeration.ConstantesRejetsConsolidations;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RejetException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.math.NumberUtils;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperCartesUtils {

  public static List<DeclarationConsolide> splitAllConsosPeriodesByEndDate(
      List<DeclarationConsolide> consos,
      String dateDebut,
      String dateJour,
      boolean isInsurer,
      Periode previousSuspensionPeriod) {
    List<String> datesFin =
        consos.stream()
            .map(DeclarationConsolide::getPeriodeFin)
            .distinct()
            .collect(Collectors.toList());

    List<DeclarationConsolide> listeDecoupee = new ArrayList<>();
    String dateDebutTmp = dateDebut;
    Collections.sort(datesFin);

    for (String dateFin : datesFin) {
      for (DeclarationConsolide declarationConso : consos) {
        DeclarationConsolide decoupe =
            splitConsoPeriodeByEndDate(declarationConso, dateDebutTmp, dateFin);
        boolean isDroitsOuverts =
            decoupe != null
                && DeclarationConsolideUtils.isDroitsOuverts(
                    decoupe.getDomaineDroits(), dateJour, isInsurer);

        if (CollectionUtils.isNotEmpty(declarationConso.getContrat().getPeriodeSuspensions())
            && isInsurer) {
          List<Periode> listPeriodeSuspension =
              declarationConso.getContrat().getPeriodeSuspensions().stream()
                  .map(suspension -> new Periode(suspension.getDebut(), suspension.getFin()))
                  .toList();
          processWithSuspensions(
              listeDecoupee,
              dateDebutTmp,
              dateFin,
              decoupe,
              isDroitsOuverts,
              listPeriodeSuspension);
        } else {
          processWithoutSuspensions(
              dateJour,
              previousSuspensionPeriod,
              listeDecoupee,
              dateDebutTmp,
              dateFin,
              decoupe,
              isDroitsOuverts);
        }
      }
      String parsed = DateUtils.parseDateAndNumberOfDays(dateFin, 1);
      dateDebutTmp = parsed != null ? parsed : dateDebutTmp;
    }
    return listeDecoupee;
  }

  private static void processWithSuspensions(
      List<DeclarationConsolide> listeDecoupee,
      String dateDebutTmp,
      String dateFin,
      DeclarationConsolide decoupe,
      boolean isDroitsOuverts,
      List<Periode> listPeriodeSuspension) {
    List<Periode> periodesWithoutSuspensions =
        DeclarationConsolideUtils.computePeriodesWithoutSuspensions(
            listPeriodeSuspension, new Periode(dateDebutTmp, dateFin));
    if (CollectionUtils.isNotEmpty(periodesWithoutSuspensions)) {
      for (Periode periodeWithoutSuspensions : periodesWithoutSuspensions) {
        if (isDroitsOuverts
            && DateUtils.isOverlapping(
                dateDebutTmp,
                dateFin,
                periodeWithoutSuspensions.getDebut(),
                periodeWithoutSuspensions.getFin())) {
          DeclarationConsolide newDecoupe = new DeclarationConsolide(decoupe);
          newDecoupe.setPeriodeDebut(periodeWithoutSuspensions.getDebut());
          newDecoupe.setPeriodeFin(periodeWithoutSuspensions.getFin());
          listeDecoupee.add(newDecoupe);
        }
      }
    }
  }

  private static void processWithoutSuspensions(
      String dateJour,
      Periode previousSuspensionPeriod,
      List<DeclarationConsolide> listeDecoupee,
      String dateDebutTmp,
      String dateFin,
      DeclarationConsolide decoupe,
      boolean isDroitsOuverts) {
    if (isDroitsOuverts) {
      if (isLeveeSuspensionApplicable(dateJour, previousSuspensionPeriod, dateDebutTmp)) {
        processWithSuspensions(
            listeDecoupee, dateDebutTmp, dateFin, decoupe, true, List.of(previousSuspensionPeriod));
      }
      if (DateUtils.betweenString(
          dateJour, previousSuspensionPeriod.getDebut(), previousSuspensionPeriod.getFin())) {
        DeclarationConsolide newDecoupe = new DeclarationConsolide(decoupe);
        newDecoupe.setPeriodeDebut(DateUtils.datePlusOneDay(dateJour, DateUtils.SLASHED_FORMATTER));
        newDecoupe.setPeriodeFin(dateFin);
        listeDecoupee.add(newDecoupe);
      } else {
        listeDecoupee.add(decoupe);
      }
    }
  }

  private static boolean isLeveeSuspensionApplicable(
      String dateJour, Periode previousSuspensionPeriod, String dateDebutTmp) {
    return previousSuspensionPeriod.getDebut() != null
        && DateUtils.before(
            dateDebutTmp,
            previousSuspensionPeriod.getDebut().replace("-", "/"),
            DateUtils.SLASHED_FORMATTER)
        && !DateUtils.before(
            dateJour,
            previousSuspensionPeriod.getDebut().replace("-", "/"),
            DateUtils.SLASHED_FORMATTER);
  }

  public static List<DeclarationConsolide> splitAllConsosPeriodesByStartDate(
      List<DeclarationConsolide> consos, String dateFin) {
    List<String> datesDebut =
        consos.stream()
            .map(DeclarationConsolide::getPeriodeDebut)
            .distinct()
            .collect(Collectors.toList());
    List<DeclarationConsolide> listeDecoupee = new ArrayList<>();
    String dateFinTmp = dateFin;
    datesDebut.sort(Collections.reverseOrder());
    for (String dateDebut : datesDebut) {
      for (DeclarationConsolide declarationConso : consos) {
        DeclarationConsolide decoupe =
            splitConsoPeriodeByStartDate(declarationConso, dateDebut, dateFinTmp);
        if (decoupe != null) {
          listeDecoupee.add(decoupe);
        }
      }
      String parsed = DateUtils.parseDateAndNumberOfDays(dateDebut, -1);
      dateFinTmp = parsed != null ? parsed : dateFinTmp;
    }
    return listeDecoupee;
  }

  static DeclarationConsolide splitConsoPeriodeByEndDate(
      DeclarationConsolide conso, String dateDebut, String dateFin) {
    String periodeDebut = conso.getPeriodeDebut();
    String periodeFin = conso.getPeriodeFin();
    if (periodeFin.compareTo(dateFin) >= 0 && periodeDebut.compareTo(dateFin) <= 0) {
      DeclarationConsolide newConso = new DeclarationConsolide(conso);
      newConso.setPeriodeFin(dateFin);
      if (periodeDebut.compareTo(dateDebut) <= 0) {
        newConso.setPeriodeDebut(dateDebut);
      }
      return newConso;
    }
    return null;
  }

  static DeclarationConsolide splitConsoPeriodeByStartDate(
      DeclarationConsolide conso, String dateDebut, String dateFin) {
    String periodeDebut = conso.getPeriodeDebut();
    String periodeFin = conso.getPeriodeFin();
    if (periodeDebut.compareTo(dateDebut) <= 0 && periodeFin.compareTo(dateDebut) >= 0) {
      DeclarationConsolide newConso = new DeclarationConsolide(conso);
      newConso.setPeriodeDebut(dateDebut);
      if (periodeFin.compareTo(dateFin) >= 0) {
        newConso.setPeriodeFin(dateFin);
      }
      return newConso;
    }
    return null;
  }

  private static void createRejectionsFromDeclarationConsolideeList(
      List<DeclarationConsolide> consoByContrat,
      Date today,
      String rejet,
      BulkActions bulkActions) {
    for (DeclarationConsolide declarationConsolide : consoByContrat) {
      for (String codeService : declarationConsolide.getCodeServices()) {
        bulkActions.reject(new Rejection(rejet, declarationConsolide, today, codeService));
        log.debug(rejet);
      }
    }
  }

  /** Extrait une liste de {@link RegroupementDomainesTP} valide à la date de début des droits */
  private static List<RegroupementDomainesTP> getRegroupParamsValid(
      Declarant declarant, String dateDebutCarte) {
    List<RegroupementDomainesTP> regroupValid = new ArrayList<>();
    if (!CollectionUtils.isEmpty(declarant.getRegroupementDomainesTP())) {
      regroupValid =
          declarant.getRegroupementDomainesTP().stream()
              .filter(
                  regroupementDomainesTP -> {
                    if (!DateUtils.isPeriodeValide(
                        regroupementDomainesTP.getDateDebut(),
                        regroupementDomainesTP.getDateFin())) {
                      return false;
                    }
                    LocalDate debutCarte =
                        DateUtils.parse(dateDebutCarte, DateUtils.SLASHED_FORMATTER);
                    LocalDate regroupDateDebut =
                        regroupementDomainesTP.getDateDebut() != null
                            ? regroupementDomainesTP.getDateDebut().toLocalDate()
                            : null;
                    LocalDate regroupDateFin =
                        regroupementDomainesTP.getDateFin() != null
                            ? regroupementDomainesTP.getDateFin().toLocalDate()
                            : null;
                    return DateUtils.betweenLocalDate(debutCarte, regroupDateDebut, regroupDateFin);
                  })
              .toList();
    }
    return regroupValid;
  }

  private static void checkSameItelisCodeForBenefs(
      List<DeclarationConsolide> declarationConsolideList) throws RejetException {
    String previousItelisCode = null;
    for (DeclarationConsolide declarationConsolide : declarationConsolideList) {
      String currentItelisCode =
          declarationConsolide.getContrat().getCodeItelis() == null
              ? ""
              : declarationConsolide.getContrat().getCodeItelis();
      if (previousItelisCode != null && !currentItelisCode.equals(previousItelisCode)) {
        throw new RejetException(
            "Le code offre ITELIS n'est pas le même pour tous les bénéficiaires du contrat",
            ConstantesRejetsConsolidations.REJET_C25.getCode());
      }
      previousItelisCode = currentItelisCode;
    }
  }

  /**
   * Extrait une map de string "DomaineRegroupementTP" - liste des codes des {@link DomaineDroit} de
   * chaque bénéficiaire ayant NiveauRemboursementIdentique à true
   */
  private static Map<String, List<List<String>>> getRegroupementWithNiveauRemboursementIdentique(
      List<RegroupementDomainesTP> regroupementDomainesTPList,
      List<List<DomaineDroit>> domainesAllBenef) {
    Map<String, List<List<String>>> domainesDroitWithRmbtIdentique = new HashMap<>();
    if (!CollectionUtils.isEmpty(regroupementDomainesTPList)) {
      for (RegroupementDomainesTP regroupementDomainesTP : regroupementDomainesTPList) {
        if (regroupementDomainesTP.isNiveauRemboursementIdentique()) {
          domainesDroitWithRmbtIdentique.put(
              regroupementDomainesTP.getDomaineRegroupementTP(), new ArrayList<>());
          for (List<DomaineDroit> domaines : domainesAllBenef) {
            List<String> domaineToRegroup =
                domaines.stream()
                    .map(DomaineDroit::getCode)
                    .filter(code -> regroupementDomainesTP.getCodesDomainesTP().contains(code))
                    .toList();
            if (!CollectionUtils.isEmpty(domaineToRegroup)) {
              domainesDroitWithRmbtIdentique
                  .get(regroupementDomainesTP.getDomaineRegroupementTP())
                  .add(domaineToRegroup);
            }
          }
        }
      }
    }
    return domainesDroitWithRmbtIdentique;
  }

  /**
   * Vérifie si les bénéficiaires ont les mêmes domaines (pour le cas du
   * niveauRembousementIdentique)
   */
  private static void checkDomainesBenefIdentiques(
      Map<String, List<List<String>>> domainesDroitWithRmbtIdentique) throws RejetException {
    for (Map.Entry<String, List<List<String>>> entry : domainesDroitWithRmbtIdentique.entrySet()) {
      List<String> previous = null;
      for (List<String> current : entry.getValue()) {
        if (previous != null) {
          if (current.size() != previous.size()) {
            throw new RejetException(
                "Les domaines TP ne sont pas les memes pour tous les bénéficiaires pour le regroupement "
                    + entry.getKey(),
                ConstantesRejetsConsolidations.REJET_C24.getCode());
          } else {
            if (previous.stream().anyMatch(codeDomaine -> !current.contains(codeDomaine))) {
              throw new RejetException(
                  "Les domaines TP ne sont pas les memes pour tous les bénéficiaires pour le regroupement "
                      + entry.getKey(),
                  ConstantesRejetsConsolidations.REJET_C24.getCode());
            }
          }
        }
        previous = current;
      }
    }
  }

  /**
   * Vérifie la cohérence entre le paramétrage de regroupement des domaines et les domaines des
   * bénéficiaires (s'il y a des regroupements de niveau de remboursement identique)
   */
  private static void checkRegroupParamsAndBenefsConsistency(
      List<DeclarationConsolide> declarationConsolideList,
      List<RegroupementDomainesTP> regroupementDomainesTPValidList)
      throws RejetException {
    List<List<DomaineDroit>> domainesAllBenefs =
        declarationConsolideList.stream().map(DeclarationConsolide::getDomaineDroits).toList();
    Map<String, List<List<String>>> domainesRbtIdentique =
        getRegroupementWithNiveauRemboursementIdentique(
            regroupementDomainesTPValidList, domainesAllBenefs);
    if (!domainesRbtIdentique.isEmpty()) {
      checkDomainesBenefIdentiques(domainesRbtIdentique);
    }
  }

  public static CarteDemat createCarteConsolidee(
      List<DeclarationConsolide> consoGroup,
      Date dateExec,
      Declarant declarant,
      BulkActions bulkActions) {
    CarteDemat carteDemat = null;
    boolean errorBenef = false;
    MapperCartesUtils.sortByAssurePrincipale(consoGroup);
    if (!CollectionUtils.isEmpty(consoGroup)) {
      List<RegroupementDomainesTP> regroupementDomainesTPValidList =
          getRegroupParamsValid(declarant, consoGroup.get(0).getPeriodeDebut());
      try {
        checkRegroupParamsAndBenefsConsistency(consoGroup, regroupementDomainesTPValidList);
        checkSameItelisCodeForBenefs(consoGroup);
      } catch (RejetException rejetException) {
        createRejectionsFromDeclarationConsolideeList(
            consoGroup, dateExec, rejetException.getCodeRejet(), bulkActions);
        return null;
      }

      for (DeclarationConsolide conso : consoGroup) {
        if (carteDemat == null) {
          carteDemat = new CarteDemat();
          carteDemat.setIdDeclarant(conso.getIdDeclarant());
          Contrat contrat = new Contrat(conso.getContrat());
          // on ne souhaite pas avoir d'objet periodeSuspensions dans la carte
          contrat.setPeriodeSuspensions(null);
          carteDemat.setContrat(contrat);

          carteDemat.setPeriodeDebut(conso.getPeriodeDebut());
          carteDemat.setPeriodeFin(conso.getPeriodeFin());
          carteDemat.setAMC_contrat(conso.getAMC_contrat());
          carteDemat.setUserCreation(Constants.JOB_620);
          carteDemat.setUserModification(Constants.JOB_620);
          carteDemat.setDateCreation(dateExec);
          carteDemat.setDateModification(dateExec);
          carteDemat.setDateConsolidation(dateExec);
          carteDemat.setDomainesConventions(new ArrayList<>());
          carteDemat.setIdDeclarations(new ArrayList<>());
          carteDemat.setIdDeclarationsConsolides(new ArrayList<>());
          carteDemat.setBeneficiaires(new ArrayList<>());
          carteDemat.setCodeServices(conso.getCodeServices());
          carteDemat.setIdentifiant(conso.getIdentifiant());
        }

        try {
          carteDemat
              .getBeneficiaires()
              .add(benefToBenefCarteDemat(conso, regroupementDomainesTPValidList));
          carteDemat.getIdDeclarations().add(conso.getIdDeclarations());

          List<String> codeServicesInConso = conso.getCodeServices();
          List<String> codeServicesInCarte = carteDemat.getCodeServices();
          List<String> newCodeServices =
              codeServicesInConso.stream()
                  .filter(codeService -> !codeServicesInCarte.contains(codeService))
                  .toList();
          if (CollectionUtils.isNotEmpty(newCodeServices)) {
            codeServicesInCarte.addAll(newCodeServices);
          }
        } catch (RejetException rejetException) {
          errorBenef = true;
          for (String codeService : conso.getCodeServices()) {
            bulkActions.reject(
                new Rejection(rejetException.getCodeRejet(), conso, dateExec, codeService));
            log.debug(rejetException.getMessage());
          }
        }
      }
    }

    if (errorBenef) {
      carteDemat = null;
    }

    if (carteDemat != null) {
      List<DomaineConvention> convConsolides =
          createDomainesConventionHeaders(carteDemat.getBeneficiaires());
      convConsolides.sort(Comparator.comparingInt(DomaineConvention::getRang));
      carteDemat.setDomainesConventions(convConsolides);

      List<String> serviceConsos = consolidateCodeServices(carteDemat.getCodeServices());
      carteDemat.setCodeServices(serviceConsos);
      carteDemat.setIsLastCarteDemat(serviceConsos.contains(Constants.CARTE_DEMATERIALISEE));

      carteDemat.setCodeClient(declarant.getCodePartenaire());
    }

    return carteDemat;
  }

  public static void deleteExcessiveInformations(CarteDemat carteDemat) {
    // ces données sont sur le benef pas sur le contrat
    carteDemat.getContrat().setRangAdministratif(null);
    carteDemat.getContrat().setLienFamilial(null);
    carteDemat.getContrat().setModePaiementPrestations(null);
  }

  private static List<DomaineConvention> createDomainesConventionHeaders(
      List<BenefCarteDemat> beneficiaires) {
    List<DomaineConvention> domaineConventions = new ArrayList<>();

    for (BenefCarteDemat beneficiaire : beneficiaires) {
      for (DomaineCarte domaineCarte : beneficiaire.getDomainesRegroup()) {
        DomaineConvention domaineConvention = new DomaineConvention();
        domaineConvention.setCode(domaineCarte.getCode());
        domaineConvention.setRang(domaineCarte.getRang());
        domaineConvention.setConventions(new ArrayList<>());
        for (Conventionnement conventionnement : domaineCarte.getConventionnements()) {
          Convention convention = new Convention();
          convention.setCode(conventionnement.getTypeConventionnement().getCode());
          convention.setPriorite(conventionnement.getPriorite());
          domaineConvention.getConventions().add(convention);
        }

        if (domaineConventions.stream()
            .noneMatch(domC -> domC.getCode().equals(domaineConvention.getCode()))) {
          domaineConventions.add(domaineConvention);
        }
      }
    }

    return domaineConventions;
  }

  static BenefCarteDemat benefToBenefCarteDemat(
      DeclarationConsolide declarationConsolide,
      List<RegroupementDomainesTP> regroupementDomainesTPValidList)
      throws RejetException {
    BenefCarteDemat benefCarteDemat = new BenefCarteDemat();

    LienContrat lienContrat = new LienContrat();
    lienContrat.setLienFamilial(declarationConsolide.getContrat().getLienFamilial());
    lienContrat.setRangAdministratif(declarationConsolide.getContrat().getRangAdministratif());
    lienContrat.setModePaiementPrestations(
        declarationConsolide.getContrat().getModePaiementPrestations());
    benefCarteDemat.setLienContrat(lienContrat);

    benefCarteDemat.setBeneficiaire(declarationConsolide.getBeneficiaire());

    List<DomaineDroit> domainesCouverture = declarationConsolide.getDomaineDroits();
    domainesCouverture.sort(Comparator.comparing(DomaineDroit::getNoOrdreDroit));
    benefCarteDemat.setDomainesCouverture(domainesCouverture);
    benefCarteDemat.setDomainesRegroup(
        createDomainesCarte(
            benefCarteDemat.getDomainesCouverture(), regroupementDomainesTPValidList));

    return benefCarteDemat;
  }

  public static void sortByFinLienFamilialNaissance(List<DeclarationConsolide> consosSplit) {
    Comparator<DeclarationConsolide> comparator =
        Comparator.comparing(DeclarationConsolide::getPeriodeFin)
            .thenComparing(conso -> conso.getContrat().getLienFamilial())
            .thenComparing(conso -> conso.getBeneficiaire().getDateNaissance())
            .thenComparing(conso -> conso.getBeneficiaire().getRangNaissance());
    consosSplit.sort(comparator);
  }

  private static List<String> consolidateCodeServices(List<String> codeServices) {
    return codeServices.stream().distinct().collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * A partir d une liste de {@link DomaineDroit} et de parametrage regroupement dans un {@link
   * Declarant}, concatene ou merge les {@link DomaineDroit} en {@link DomaineCarte}
   */
  public static List<DomaineCarte> createDomainesCarte(
      List<DomaineDroit> domaines, List<RegroupementDomainesTP> regroupementDomainesTPList)
      throws RejetException {
    List<DomaineDroit> notMerged = new ArrayList<>(domaines);
    List<DomaineCarte> domaineCartes = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(regroupementDomainesTPList)) {
      for (RegroupementDomainesTP regroupParam : regroupementDomainesTPList) {
        Map<String, DomaineDroit> domainePerCode =
            extractMapDomainesByRegroupement(regroupParam, domaines);

        if (domainePerCode.isEmpty()) {
          continue;
        }

        invalidRegroup(domainePerCode.values());

        DomaineCarte domaineCarte = initMainDomaineCarte(domainePerCode, regroupParam);
        String nonCouvert = getNonCouvertTaux(domaineCarte.getUnite());

        boolean canRegroup = true;
        for (int i = 0; canRegroup && i < regroupParam.getCodesDomainesTP().size(); i++) {
          String codeToGroup = regroupParam.getCodesDomainesTP().get(i);
          DomaineDroit domaine = domainePerCode.get(codeToGroup);
          DomaineCarte subDomaine = new DomaineCarte();
          subDomaine.setCode(codeToGroup);
          subDomaine.setTaux(nonCouvert);
          canRegroup = completeRegroupement(domaine, regroupParam, domaineCarte, subDomaine);
          domaineCarte.getRegroupement().add(subDomaine);
        }

        if (canRegroup) {
          domaineCarte.getRegroupement().sort(Comparator.comparing(DomaineCarte::getRang));
          domaineCartes.add(domaineCarte);
          notMerged.removeAll(domainePerCode.values());
        }
      }
    }

    // Domaines qui ne sont pas impactes par les regroupements
    for (DomaineDroit domaine : notMerged) {
      DomaineCarte domaineCarte = new DomaineCarte();
      domaineCarte.setCode(domaine.getCode());
      domaineCarte.setLibelle(domaine.getLibelle());
      domaineCarte.setTaux(domaine.getTauxRemboursement());
      domaineCarte.setRang(domaine.getNoOrdreDroit());
      domaineCarte.setConventionnements(domaine.getConventionnements());
      domaineCarte.setUnite(domaine.getUniteTauxRemboursement());
      domaineCarte.setCodeExterne(domaine.getCodeExterne());
      domaineCarte.setLibelleExterne(domaine.getLibelleExterne());
      domaineCarte.setCodeExterneProduit(domaine.getCodeExterneProduit());
      domaineCarte.setCodeOptionMutualiste(domaine.getCodeOptionMutualiste());
      domaineCarte.setLibelleOptionMutualiste(domaine.getLibelleOptionMutualiste());
      domaineCarte.setCodeProduit(domaine.getCodeProduit());
      domaineCarte.setLibelleProduit(domaine.getLibelleProduit());
      domaineCarte.setCodeGarantie(domaine.getCodeGarantie());
      domaineCarte.setLibelleGarantie(domaine.getLibelleGarantie());
      domaineCarte.setPrioriteDroits(domaine.getPrioriteDroit());
      domaineCarte.setCodeRenvoi(domaine.getCodeRenvoi());
      domaineCarte.setLibelleCodeRenvoi(domaine.getLibelleCodeRenvoi());
      domaineCarte.setCodeRenvoiAdditionnel(domaine.getCodeRenvoiAdditionnel());
      domaineCarte.setLibelleCodeRenvoiAdditionnel(domaine.getLibelleCodeRenvoiAdditionnel());
      domaineCarte.setCategorieDomaine(domaine.getCategorie());
      domaineCarte.setPeriodeDebut(domaine.getPeriodeDroit().getPeriodeDebut());
      domaineCarte.setPeriodeFin(domaine.getPeriodeDroit().getPeriodeFin());
      domaineCarte.setReferenceCouverture(domaine.getReferenceCouverture());
      domaineCarte.setNoOrdreDroit(domaine.getNoOrdreDroit());
      domaineCarte.setFormulaMask(domaine.getFormulaMask());
      domaineCarte.setIsEditable(domaine.getIsEditable());
      domaineCartes.add(domaineCarte);
    }

    domaineCartes.sort(Comparator.comparing(DomaineCarte::getRang));
    return domaineCartes;
  }

  private static DomaineCarte initMainDomaineCarte(
      Map<String, DomaineDroit> domainePerCode, RegroupementDomainesTP regroupDomainesTP) {
    List<String> codesDomainesTP = regroupDomainesTP.getCodesDomainesTP();
    String regroupDomaine = regroupDomainesTP.getDomaineRegroupementTP();
    Collection<DomaineDroit> domaineDroits = domainePerCode.values();
    DomaineCarte domaineCarte = new DomaineCarte();
    domaineCarte.setCode(regroupDomaine);
    domaineCarte.setRegroupement(new ArrayList<>());
    domaineCarte.setConventionnements(new ArrayList<>());
    domaineCarte.setRang(Integer.MAX_VALUE);

    if (CollectionUtils.isNotEmpty(codesDomainesTP)) {
      if (domainePerCode.containsKey(codesDomainesTP.get(0))) {
        DomaineDroit firstDomainRegroupement = domainePerCode.get(codesDomainesTP.get(0));
        mapMainDomaineCarte(domaineCarte, firstDomainRegroupement);
      } else {
        domaineDroits.stream()
            .findFirst()
            .ifPresent(domaineDroit -> mapMainDomaineCarte(domaineCarte, domaineDroit));
      }
    }

    return domaineCarte;
  }

  private static void mapMainDomaineCarte(
      DomaineCarte domaineCarte, DomaineDroit firstDomainRegroupement) {
    domaineCarte.setUnite(firstDomainRegroupement.getUniteTauxRemboursement());
    domaineCarte.setCodeRenvoi(firstDomainRegroupement.getCodeRenvoi());
    domaineCarte.setLibelleCodeRenvoi(firstDomainRegroupement.getLibelleCodeRenvoi());
    domaineCarte.setCodeRenvoiAdditionnel(firstDomainRegroupement.getCodeRenvoiAdditionnel());
    domaineCarte.setLibelleCodeRenvoiAdditionnel(
        firstDomainRegroupement.getLibelleCodeRenvoiAdditionnel());
    domaineCarte.setConventionnements(firstDomainRegroupement.getConventionnements());
    domaineCarte.setLibelle(firstDomainRegroupement.getLibelle());
    domaineCarte.setCodeExterne(firstDomainRegroupement.getCodeExterne());
    domaineCarte.setLibelleExterne(firstDomainRegroupement.getLibelleExterne());
    domaineCarte.setCodeExterneProduit(firstDomainRegroupement.getCodeExterneProduit());
    domaineCarte.setCodeOptionMutualiste(firstDomainRegroupement.getCodeOptionMutualiste());
    domaineCarte.setLibelleOptionMutualiste(firstDomainRegroupement.getLibelleOptionMutualiste());
    domaineCarte.setCodeProduit(firstDomainRegroupement.getCodeProduit());
    domaineCarte.setLibelleProduit(firstDomainRegroupement.getLibelleProduit());
    domaineCarte.setCodeGarantie(firstDomainRegroupement.getCodeGarantie());
    domaineCarte.setLibelleGarantie(firstDomainRegroupement.getLibelleGarantie());
    domaineCarte.setPrioriteDroits(firstDomainRegroupement.getPrioriteDroit());
    domaineCarte.setCodeRenvoi(firstDomainRegroupement.getCodeRenvoi());
    domaineCarte.setLibelleCodeRenvoi(firstDomainRegroupement.getLibelleCodeRenvoi());
    domaineCarte.setCodeRenvoiAdditionnel(firstDomainRegroupement.getCodeRenvoiAdditionnel());
    domaineCarte.setLibelleCodeRenvoiAdditionnel(
        firstDomainRegroupement.getLibelleCodeRenvoiAdditionnel());
    domaineCarte.setCategorieDomaine(firstDomainRegroupement.getCategorie());
    domaineCarte.setPeriodeDebut(firstDomainRegroupement.getPeriodeDroit().getPeriodeDebut());
    domaineCarte.setPeriodeFin(firstDomainRegroupement.getPeriodeDroit().getPeriodeFin());
    domaineCarte.setReferenceCouverture(firstDomainRegroupement.getReferenceCouverture());
    domaineCarte.setNoOrdreDroit(firstDomainRegroupement.getNoOrdreDroit());
    domaineCarte.setFormulaMask(firstDomainRegroupement.getFormulaMask());
    domaineCarte.setIsEditable(firstDomainRegroupement.getIsEditable());
  }

  /**
   * A partir du premier {@link DomaineDroit} retourne le taux de non couverture correspondant a la
   * bonne unite
   */
  private static String getNonCouvertTaux(String uniteTaux) {
    if (TauxConstants.U_TEXT.equals(uniteTaux)) {
      return TauxConstants.T_NON_COUVERT;
    } else {
      return TauxConstants.T_0;
    }
  }

  /**
   * Test si les {@link DomaineDroit} ont des codes renvois, code renvois additionnel, unite de taux
   * ou des conventionnements differents
   */
  private static void invalidRegroup(Collection<DomaineDroit> domaineDroits) throws RejetException {
    DomaineDroit previous = null;
    for (DomaineDroit current : domaineDroits) {
      if (previous != null) {
        if (!Objects.equals(previous.getCodeRenvoi(), current.getCodeRenvoi())) {
          throw new RejetException(
              "CodeRenvoi différents pour les domaines "
                  + previous.getCode()
                  + " "
                  + current.getCode(),
              ConstantesRejetsConsolidations.REJET_C20.getCode());
        }

        if (!Objects.equals(
            previous.getCodeRenvoiAdditionnel(), current.getCodeRenvoiAdditionnel())) {
          throw new RejetException(
              "CodeRenvoiAdditionnel différents pour les domaines "
                  + previous.getCode()
                  + " "
                  + current.getCode(),
              ConstantesRejetsConsolidations.REJET_C20.getCode());
        }

        if (!Objects.equals(
            previous.getUniteTauxRemboursement(), current.getUniteTauxRemboursement())) {
          throw new RejetException(
              "Unite de taux différents pour les domaines "
                  + previous.getCode()
                  + " "
                  + current.getCode(),
              ConstantesRejetsConsolidations.REJET_C20.getCode());
        }

        CompareToBuilder comparator = new CompareToBuilder();
        List<Conventionnement> prevConv =
            Objects.requireNonNullElse(previous.getConventionnements(), Collections.emptyList());
        List<Conventionnement> currConv =
            Objects.requireNonNullElse(current.getConventionnements(), Collections.emptyList());
        comparator.append(prevConv.toArray(), currConv.toArray());
        if (comparator.toComparison() != 0) {
          throw new RejetException(
              "Conventionnements différents pour les domaines "
                  + previous.getCode()
                  + " "
                  + current.getCode(),
              ConstantesRejetsConsolidations.REJET_C20.getCode());
        }
      }
      previous = current;
    }
  }

  /**
   * Extrait une map de Code - {@link DomaineDroit} en fonction des codes a regrouper et de la date
   * de validite du parametrage de regroupement
   */
  private static Map<String, DomaineDroit> extractMapDomainesByRegroupement(
      RegroupementDomainesTP regroupParam, Collection<DomaineDroit> domaines) {
    return domaines.stream()
        .filter(
            domaine -> {
              if (!regroupParam.getCodesDomainesTP().contains(domaine.getCode())) {
                return false;
              }
              LocalDate debutDom =
                  DateUtils.parse(
                      domaine.getPeriodeDroit().getPeriodeDebut(), DateUtils.SLASHED_FORMATTER);
              LocalDate regroupDateDebut =
                  regroupParam.getDateDebut() != null
                      ? regroupParam.getDateDebut().toLocalDate()
                      : null;
              LocalDate regroupDateFin =
                  regroupParam.getDateFin() != null
                      ? regroupParam.getDateFin().toLocalDate()
                      : null;
              return DateUtils.betweenLocalDate(debutDom, regroupDateDebut, regroupDateFin);
            })
        .collect(Collectors.toMap(DomaineDroit::getCode, domaine -> domaine));
  }

  private static boolean completeRegroupement(
      DomaineDroit domaine,
      RegroupementDomainesTP regroupParam,
      DomaineCarte domaineCarte,
      DomaineCarte subDomaine)
      throws RejetException {
    if (domaine == null) {
      if (regroupParam.isNiveauRemboursementIdentique()) {
        return false;
      }
      if (domaineCarte.getTaux() != null) {
        domaineCarte.setTaux(domaineCarte.getTaux() + Constants.SLASH + subDomaine.getTaux());
      } else {
        domaineCarte.setTaux(subDomaine.getTaux());
      }
    } else {
      subDomaine.setTaux(domaine.getTauxRemboursement());
      subDomaine.setConventionnements(domaine.getConventionnements());
      subDomaine.setRang(domaine.getNoOrdreDroit());
      subDomaine.setCodeRenvoi(domaine.getCodeRenvoi());
      subDomaine.setLibelleCodeRenvoi(domaine.getLibelleCodeRenvoi());
      subDomaine.setCodeRenvoiAdditionnel(domaine.getCodeRenvoiAdditionnel());
      subDomaine.setLibelleCodeRenvoiAdditionnel(domaine.getLibelleCodeRenvoiAdditionnel());
      subDomaine.setUnite(domaine.getUniteTauxRemboursement());
      if (domaineCarte.getTaux() == null) {
        domaineCarte.setTaux(domaine.getTauxRemboursement());
      } else if (!regroupParam.isNiveauRemboursementIdentique()) {
        domaineCarte.setTaux(domaineCarte.getTaux() + Constants.SLASH + subDomaine.getTaux());
      } else if (!Objects.equals(subDomaine.getTaux(), domaineCarte.getTaux())) {
        throw new RejetException(
            "Les taux "
                + subDomaine.getTaux()
                + " et "
                + domaineCarte.getCode()
                + " ne sont pas identique pour le regroupement "
                + regroupParam.getDomaineRegroupementTP(),
            ConstantesRejetsConsolidations.REJET_C22.getCode());
      }

      domaineCarte.setRang(Math.min(domaineCarte.getRang(), domaine.getNoOrdreDroit()));
    }

    return true;
  }

  /**
   * Ordonne la liste de façon a avoir l assure principal en premier puis ceux ayant le rand
   * adminitratif le plus bas
   */
  public static void sortByAssurePrincipale(List<DeclarationConsolide> declarationConsolides) {
    Comparator<DeclarationConsolide> comparator =
        Comparator.comparingInt(
            conso -> {
              String qualite = conso.getBeneficiaire().getAffiliation().getQualite();
              return Constants.QUALITE_A.equals(qualite) ? 0 : 1;
            });
    comparator =
        comparator.thenComparingInt(
            conso -> {
              String rangAdministratif = conso.getContrat().getRangAdministratif();
              if (NumberUtils.isParsable(rangAdministratif)) {
                return Integer.parseInt(rangAdministratif);
              }
              return Integer.MAX_VALUE;
            });
    declarationConsolides.sort(comparator);
  }
}
