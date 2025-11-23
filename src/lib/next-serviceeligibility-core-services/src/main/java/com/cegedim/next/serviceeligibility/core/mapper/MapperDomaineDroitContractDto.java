package com.cegedim.next.serviceeligibility.core.mapper;

import static com.cegedim.next.serviceeligibility.core.utils.DateUtils.SLASHED_FORMATTER;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.beneficiarydetails.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.*;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.BeneficiaireMailleDomaine;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailledomaine.MailleDomaineDroit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.BeneficiaireMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.DomaineDroitMailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillegarantie.MailleGarantie;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.BeneficiaireMailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.DomaineDroitMailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.GarantieMailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.mailleproduit.MailleProduit;
import com.cegedim.next.serviceeligibility.core.model.domain.contract.maillerefcouverture.*;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.Util;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor
public class MapperDomaineDroitContractDto {

  private static String dateFinOnline = null;

  protected static List<DomaineDroitContractDto> mapDomaineDroits(
      BeneficiaireMailleDomaine beneficiaireMailleDomaine,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitContractDto> domaineDroitContractDtoList = new ArrayList<>();
    if (beneficiaireMailleDomaine != null
        && CollectionUtils.isNotEmpty(beneficiaireMailleDomaine.getDomaineDroits())) {
      for (MailleDomaineDroit domaineDroit : beneficiaireMailleDomaine.getDomaineDroits()) {
        if (domaineDroit != null) {
          List<PeriodeDroitContractTP> periodesDroit =
              Boolean.TRUE.equals(isHTP)
                  ? sortPeriodesDroit(domaineDroit.getPeriodesDroit())
                  : domaineDroit.getPeriodesDroit();
          for (PeriodeDroitContractTP periodeDroitContractTP : periodesDroit) {
            if (periodeDroitContractTP != null) {
              DomaineDroitContractDto domaineDroitDto = new DomaineDroitContractDto();
              domaineDroitDto.setCode(domaineDroit.getCode());
              PeriodeDroitContractDto periodeDroitContractDto =
                  getPeriodeDroitContractDto(
                      dateRestitution, isExclusiviteCarteDematerialise, periodeDroitContractTP);
              if (Boolean.TRUE.equals(isHTP)
                  && TypePeriode.ONLINE.equals(periodeDroitContractDto.getTypePeriode())) {
                dateFinOnline = periodeDroitContractDto.getPeriodeFin();
              }
              periodeDroitContractDto.setRemboursements(
                  mapRemboursements(
                      domaineDroit.getRemboursements(),
                      periodeDroitContractTP,
                      periodeDroitContractDto,
                      dateFinOnline,
                      isHTP));
              periodeDroitContractDto.setPrioritesDroit(
                  mapPrioritesDroit(
                      domaineDroit.getPrioritesDroit(),
                      periodeDroitContractTP,
                      periodeDroitContractDto,
                      dateFinOnline,
                      isHTP));
              periodeDroitContractDto.setConventionnements(
                  mapConventionnements(
                      ajusterPeriode(
                          domaineDroit.getConventionnements(),
                          periodeDroitContractDto,
                          dateFinOnline,
                          isHTP),
                      periodeDroitContractTP));
              periodeDroitContractDto.setPrestations(
                  mapPrestations(
                      domaineDroit.getPrestations(),
                      periodeDroitContractTP,
                      periodeDroitContractDto,
                      dateFinOnline,
                      isHTP));
              domaineDroitDto.setPeriodesDroit(List.of(periodeDroitContractDto));
              domaineDroitContractDtoList.add(domaineDroitDto);
            }
          }
        }
      }
    }
    return domaineDroitContractDtoList;
  }

  private static List<PeriodeDroitContractTP> sortPeriodesDroit(
      List<PeriodeDroitContractTP> periodesDroit) {
    return periodesDroit.stream()
        .sorted(
            Comparator.comparing(
                periode -> TypePeriode.ONLINE.equals(periode.getTypePeriode()) ? 0 : 1))
        .toList();
  }

  private static @NotNull PeriodeDroitContractDto getPeriodeDroitContractDto(
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      PeriodeDroitContractTP periodeDroitContractTP) {
    PeriodeDroitContractDto periodeDroitContractDto = new PeriodeDroitContractDto();
    periodeDroitContractDto.setTypePeriode(periodeDroitContractTP.getTypePeriode());
    periodeDroitContractDto.setPeriodeDebut(
        formatEmptyDate(periodeDroitContractTP.getPeriodeDebut()));
    periodeDroitContractDto.setPeriodeFin(formatEmptyDate(periodeDroitContractTP.getPeriodeFin()));
    periodeDroitContractDto.setPeriodeFinFermeture(
        formatEmptyDate(periodeDroitContractTP.getPeriodeFinFermeture()));
    periodeDroitContractDto.setPeriodeFinOffline(
        Util.getDateFinOffline(
            periodeDroitContractTP.getPeriodeFin(),
            periodeDroitContractTP.getPeriodeFinFermeture(),
            dateRestitution,
            isExclusiviteCarteDematerialise));
    periodeDroitContractDto.setLibelleEvenement(periodeDroitContractTP.getLibelleEvenement());
    periodeDroitContractDto.setModeObtention(periodeDroitContractTP.getModeObtention());
    periodeDroitContractDto.setMotifEvenement(periodeDroitContractTP.getMotifEvenement());
    return periodeDroitContractDto;
  }

  protected static List<DomaineDroitContractDto> mapDomaineDroits(
      BeneficiaireMailleGarantie beneficiaire,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitMailleGarantie> domaineDroits = beneficiaire.getDomaineDroits();
    List<DomaineDroitContractDto> domaineDroitDtoList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(domaineDroits)) {
      domaineDroits.forEach(
          domaineDroit -> {
            List<DomaineDroitContractDto> domainesDroitDto =
                mapDomaineDroit(
                    domaineDroit, dateRestitution, isExclusiviteCarteDematerialise, isHTP);
            if (CollectionUtils.isNotEmpty(domainesDroitDto)) {
              domaineDroitDtoList.addAll(domainesDroitDto);
            }
          });
    }
    return domaineDroitDtoList;
  }

  protected static List<DomaineDroitContractDto> mapDomaineDroits(
      BeneficiaireMailleProduit beneficiaire,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitMailleProduit> domaineDroits = beneficiaire.getDomaineDroits();
    List<DomaineDroitContractDto> domaineDroitDtoList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(domaineDroits)) {
      domaineDroits.forEach(
          domaineDroit -> {
            List<DomaineDroitContractDto> domainesDroitDto =
                mapDomaineDroit(
                    domaineDroit, dateRestitution, isExclusiviteCarteDematerialise, isHTP);
            if (CollectionUtils.isNotEmpty(domainesDroitDto)) {
              domaineDroitDtoList.addAll(domainesDroitDto);
            }
          });
    }
    return domaineDroitDtoList;
  }

  protected static List<DomaineDroitContractDto> mapDomaineDroits(
      BeneficiaireMailleRefCouv beneficiaire,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitMailleRefCouv> domaineDroits = beneficiaire.getDomaineDroits();
    List<DomaineDroitContractDto> domaineDroitDtoList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(domaineDroits)) {
      domaineDroits.forEach(
          domaineDroit -> {
            List<DomaineDroitContractDto> domainesDroitDto =
                mapDomaineDroit(
                    domaineDroit, dateRestitution, isExclusiviteCarteDematerialise, isHTP);
            if (CollectionUtils.isNotEmpty(domainesDroitDto)) {
              domaineDroitDtoList.addAll(domainesDroitDto);
            }
          });
    }
    return domaineDroitDtoList;
  }

  protected static List<DomaineDroitContractDto> mapDomaineDroits(
      BeneficiaireContractTP beneficiaire,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitContractTP> domaineDroits = beneficiaire.getDomaineDroits();
    List<DomaineDroitContractDto> domaineDroitDtoList = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(domaineDroits)) {
      domaineDroits.forEach(
          domaineDroit -> {
            List<DomaineDroitContractDto> domainesDroitDto =
                mapDomaineDroit(
                    domaineDroit, dateRestitution, isExclusiviteCarteDematerialise, isHTP);
            if (CollectionUtils.isNotEmpty(domainesDroitDto)) {
              domaineDroitDtoList.addAll(domainesDroitDto);
            }
          });
    }
    return domaineDroitDtoList;
  }

  private static List<RemboursementContract> mapRemboursements(
      List<RemboursementContrat> remboursementContrats,
      PeriodeDroitContractTP periodeDroitContractTP,
      PeriodeDroitContractDto periodeDroitContractDto,
      String dateFinOnline,
      Boolean isHTP) {
    List<RemboursementContract> remboursementContractList = new ArrayList<>();
    Map<PeriodeContractDto, List<Remboursement>> remboursementsParPeriode =
        mapElementsByPeriode(
            remboursementContrats,
            remboursementContrat ->
                mapPeriodesContract(remboursementContrat.getPeriodes(), periodeDroitContractTP),
            MapperDomaineDroitContractDto::mapRemboursement);

    for (Map.Entry<PeriodeContractDto, List<Remboursement>> entry :
        remboursementsParPeriode.entrySet()) {
      RemboursementContract remboursement = new RemboursementContract();
      remboursement.setPeriode(
          ajusterPeriode(entry.getKey(), periodeDroitContractDto, dateFinOnline, isHTP));
      remboursement.setRemboursements(entry.getValue());
      remboursementContractList.add(remboursement);
    }
    return remboursementContractList;
  }

  private static Remboursement mapRemboursement(RemboursementContrat remboursementContrat) {
    Remboursement remboursement = new Remboursement();
    if (remboursementContrat.getTauxRemboursement() != null) {
      remboursement.setTauxRemboursement(remboursementContrat.getTauxRemboursement());
    }
    if (remboursementContrat.getUniteTauxRemboursement() != null) {
      remboursement.setUniteTauxRemboursement(remboursementContrat.getUniteTauxRemboursement());
    }
    return remboursement;
  }

  private static List<PrioriteDroitContract> mapPrioritesDroit(
      List<PrioriteDroitContrat> prioritesDroit,
      PeriodeDroitContractTP periodeDroitContractTP,
      PeriodeDroitContractDto periodeDroitContractDto,
      String dateFinOnline,
      Boolean isHTP) {
    List<PrioriteDroitContract> prioriteDroitContractList = new ArrayList<>();
    Map<PeriodeContractDto, List<PrioriteDroit>> prioritesParPeriode =
        mapElementsByPeriode(
            prioritesDroit,
            prioriteDroitContrat ->
                mapPeriodesContract(prioriteDroitContrat.getPeriodes(), periodeDroitContractTP),
            MapperDomaineDroitContractDto::mapPrioriteDroit);

    for (Map.Entry<PeriodeContractDto, List<PrioriteDroit>> entry :
        prioritesParPeriode.entrySet()) {
      PrioriteDroitContract prioriteDroit = new PrioriteDroitContract();
      prioriteDroit.setPeriode(
          ajusterPeriode(entry.getKey(), periodeDroitContractDto, dateFinOnline, isHTP));
      prioriteDroit.setPriorites(entry.getValue());
      prioriteDroitContractList.add(prioriteDroit);
    }
    return prioriteDroitContractList;
  }

  private static PrioriteDroit mapPrioriteDroit(PrioriteDroitContrat prioriteDroitContrat) {
    PrioriteDroit prioriteDroit = new PrioriteDroit();
    if (prioriteDroitContrat.getCode() != null) {
      prioriteDroit.setCode(prioriteDroitContrat.getCode());
    }
    if (prioriteDroitContrat.getLibelle() != null) {
      prioriteDroit.setLibelle(prioriteDroitContrat.getLibelle());
    }
    if (prioriteDroitContrat.getTypeDroit() != null) {
      prioriteDroit.setTypeDroit(prioriteDroitContrat.getTypeDroit());
    }
    if (prioriteDroitContrat.getPrioriteBO() != null) {
      prioriteDroit.setPrioriteBO(prioriteDroitContrat.getPrioriteBO());
    }
    if (prioriteDroitContrat.getNirPrio1() != null) {
      prioriteDroit.setNirPrio1(prioriteDroitContrat.getNirPrio1());
    }
    if (prioriteDroitContrat.getNirPrio2() != null) {
      prioriteDroit.setNirPrio2(prioriteDroitContrat.getNirPrio2());
    }
    if (prioriteDroitContrat.getPrioDroitNir1() != null) {
      prioriteDroit.setPrioDroitNir1(prioriteDroitContrat.getPrioDroitNir1());
    }
    if (prioriteDroitContrat.getPrioDroitNir2() != null) {
      prioriteDroit.setPrioDroitNir2(prioriteDroitContrat.getPrioDroitNir2());
    }
    if (prioriteDroitContrat.getPrioContratNir1() != null) {
      prioriteDroit.setPrioContratNir1(prioriteDroitContrat.getPrioContratNir1());
    }
    if (prioriteDroitContrat.getPrioContratNir2() != null) {
      prioriteDroit.setPrioContratNir2(prioriteDroitContrat.getPrioContratNir2());
    }
    return prioriteDroit;
  }

  private static PeriodeContractDto ajusterPeriode(
      PeriodeContractDto periode,
      PeriodeDroitContractDto periodeDroit,
      String dateFinOnline,
      Boolean isHTP) {
    if (Boolean.FALSE.equals(isHTP) || TypePeriode.ONLINE.equals(periodeDroit.getTypePeriode())) {
      return periode;
    }
    if (periode.getFin() != null && periode.getFin().equals(dateFinOnline)) {
      periode.setFin(
          periodeDroit.getPeriodeFinFermeture() != null
                  && !Constants.EMPTY_DATE.equals(periodeDroit.getPeriodeFinFermeture())
              ? periodeDroit.getPeriodeFinFermeture()
              : periodeDroit.getPeriodeFinOffline());
    }

    return periode;
  }

  private static List<ConventionnementContrat> ajusterPeriode(
      List<ConventionnementContrat> conventionnementContrats,
      PeriodeDroitContractDto periodeDroit,
      String dateFinOnline,
      Boolean isHTP) {
    if (Boolean.FALSE.equals(isHTP) || TypePeriode.ONLINE.equals(periodeDroit.getTypePeriode())) {
      return conventionnementContrats;
    }
    conventionnementContrats.forEach(
        conventionnementContrat ->
            conventionnementContrat
                .getPeriodes()
                .forEach(
                    periode -> {
                      if (periode.getFin() != null && periode.getFin().equals(dateFinOnline)) {
                        periode.setFin(
                            (periodeDroit.getPeriodeFinFermeture() != null
                                    && !Constants.EMPTY_DATE.equals(
                                        periodeDroit.getPeriodeFinFermeture()))
                                ? periodeDroit.getPeriodeFinFermeture()
                                : periodeDroit.getPeriodeFinOffline());
                      }
                    }));
    return conventionnementContrats;
  }

  public static List<ConventionnementContract> mapConventionnements(
      List<ConventionnementContrat> conventionnementContrats,
      PeriodeDroitContractTP periodeDroitContractTP) {
    Map<PeriodeContractDto, List<TypeConventionnements>> resultConventionnements = new HashMap<>();

    // Étape 1 : Groupement des conventionnements par priorité
    Map<Integer, List<ConventionnementContrat>> groupedByPriorite =
        conventionnementContrats.stream()
            .collect(
                Collectors.groupingBy(
                    ConventionnementContrat::getPriorite,
                    TreeMap::new, // permet de trier les clés par ordre croissants
                    Collectors.toList()));

    // Étape 2 : Processus par priorité croissante
    for (Map.Entry<Integer, List<ConventionnementContrat>> entry : groupedByPriorite.entrySet()) {
      Integer prioriteActuelle = entry.getKey();
      List<ConventionnementContrat> conventionnementsActuels = entry.getValue();

      // Ajuster les périodes si des priorités supérieures existent déjà
      List<ConventionnementContrat> conventionnementsDecoupes =
          handleConventionnementsPrioInferieurs(conventionnementsActuels, resultConventionnements);

      // Fusionner les résultats pour cette priorité
      fusionnerConventionnements(
          resultConventionnements,
          conventionnementsDecoupes,
          periodeDroitContractTP,
          prioriteActuelle);
    }

    // Étape 3 : Conversion des résultats en List<ConventionnementContract> + tri
    return resultConventionnements.entrySet().stream()
        .map(entry -> buildConventionnementContract(entry.getKey(), entry.getValue()))
        .sorted(
            Comparator.comparing(
                contract -> contract.getPeriode().getDebut(), Comparator.reverseOrder()))
        .toList();
  }

  private static ConventionnementContract buildConventionnementContract(
      PeriodeContractDto periode, List<TypeConventionnements> types) {
    ConventionnementContract conventionnementContract = new ConventionnementContract();
    conventionnementContract.setPeriode(periode);
    conventionnementContract.setTypeConventionnements(types);
    return conventionnementContract;
  }

  private static List<ConventionnementContrat> handleConventionnementsPrioInferieurs(
      List<ConventionnementContrat> conventionnementsActuels,
      Map<PeriodeContractDto, List<TypeConventionnements>> resultConventionnements) {
    List<ConventionnementContrat> conventionsAvecPrioInferieurs = new ArrayList<>();

    if (resultConventionnements.isEmpty()) {
      return conventionnementsActuels;
    }

    for (ConventionnementContrat convInferieur : conventionnementsActuels) {
      // Découper les périodes des priorités inférieurs en fonction des périodes des
      // priorités supérieurs
      List<Periode> periodesDecoupees =
          decouperPeriodes(convInferieur.getPeriodes(), resultConventionnements);

      // Ajouter les périodes découpées au resultConventionnements
      for (Periode periode : periodesDecoupees) {
        ConventionnementContrat conventionnementContrat = new ConventionnementContrat();
        conventionnementContrat.setPriorite(convInferieur.getPriorite());
        conventionnementContrat.setTypeConventionnement(convInferieur.getTypeConventionnement());
        conventionnementContrat.setPeriodes(Collections.singletonList(periode));
        conventionsAvecPrioInferieurs.add(conventionnementContrat);
      }
    }
    return conventionsAvecPrioInferieurs;
  }

  private static List<Periode> decouperPeriodes(
      List<Periode> periodesInferieures,
      Map<PeriodeContractDto, List<TypeConventionnements>> resultConventionnements) {
    List<Periode> periodesResult = new ArrayList<>();

    for (Periode periodeInferieure : periodesInferieures) {
      Map<PeriodeContractDto, List<TypeConventionnements>> periodesMiseAJour = new HashMap<>();
      resultConventionnements.forEach(
          (periodeSuperieure, conventions) -> {
            if (DateUtils.isOverlapping(
                periodeInferieure.getDebut(),
                periodeInferieure.getFin(),
                periodeSuperieure.getDebut(),
                periodeSuperieure.getFin())) {

              // Intersection entre les périodes
              String intersectionDebut =
                  DateUtils.getMaxDate(periodeInferieure.getDebut(), periodeSuperieure.getDebut());
              String intersectionFin =
                  DateUtils.getMinDate(periodeInferieure.getFin(), periodeSuperieure.getFin());

              // Ajout de l'intersection au résultat
              periodesResult.add(new Periode(intersectionDebut, intersectionFin));

              // Découper et mettre à jour les périodes existantes
              decouperEtMettreAJour(
                  periodeSuperieure,
                  intersectionDebut,
                  intersectionFin,
                  conventions,
                  periodesMiseAJour);
            } else {
              // Aucune intersection, on conserve la période existante
              periodesMiseAJour.put(periodeSuperieure, new ArrayList<>(conventions));
            }
          });

      // Mise à jour des résultats avec les nouvelles périodes découpées
      resultConventionnements.clear();
      resultConventionnements.putAll(periodesMiseAJour);
    }

    return periodesResult;
  }

  private static void decouperEtMettreAJour(
      PeriodeContractDto periodeSuperieure,
      String intersectionDebut,
      String intersectionFin,
      List<TypeConventionnements> conventions,
      Map<PeriodeContractDto, List<TypeConventionnements>> periodesMiseAJour) {

    // Période avant l'intersection
    if (DateUtils.before(periodeSuperieure.getDebut(), intersectionDebut, SLASHED_FORMATTER)) {
      addNewPeriod(
          periodeSuperieure.getDebut(),
          DateUtils.dateMinusOneDay(intersectionDebut, SLASHED_FORMATTER),
          conventions,
          periodesMiseAJour);
    }

    // Période correspondant à l'intersection
    addNewPeriod(intersectionDebut, intersectionFin, conventions, periodesMiseAJour);

    // Période après l'intersection
    if (intersectionFin != null
        && (periodeSuperieure.getFin() == null
            || DateUtils.after(periodeSuperieure.getFin(), intersectionFin, SLASHED_FORMATTER))) {
      addNewPeriod(
          DateUtils.datePlusOneDay(intersectionFin, SLASHED_FORMATTER),
          periodeSuperieure.getFin(),
          conventions,
          periodesMiseAJour);
    }
  }

  private static void addNewPeriod(
      String debut,
      String fin,
      List<TypeConventionnements> conventions,
      Map<PeriodeContractDto, List<TypeConventionnements>> periodesMiseAJour) {
    PeriodeContractDto newPeriodContract = new PeriodeContractDto();
    newPeriodContract.setDebut(debut);
    newPeriodContract.setFin(fin);
    periodesMiseAJour.put(newPeriodContract, new ArrayList<>(conventions));
  }

  private static void fusionnerConventionnements(
      Map<PeriodeContractDto, List<TypeConventionnements>> result,
      List<ConventionnementContrat> conventionnements,
      PeriodeDroitContractTP periodeDroitContractTP,
      int priorite) {

    for (ConventionnementContrat conventionnement : conventionnements) {
      TypeConventionnements typeConventionnements = new TypeConventionnements();
      typeConventionnements.setPriorite(priorite);
      typeConventionnements.setTypeConventionnement(conventionnement.getTypeConventionnement());

      List<PeriodeContractDto> periodeContract =
          mapPeriodesContract(conventionnement.getPeriodes(), periodeDroitContractTP);
      periodeContract.forEach(
          periode ->
              result.computeIfAbsent(periode, k -> new ArrayList<>()).add(typeConventionnements));
    }
  }

  private static List<PrestationContract> mapPrestations(
      List<PrestationContrat> prestationContrats,
      PeriodeDroitContractTP periodeDroitContractTP,
      PeriodeDroitContractDto periodeDroitContractDto,
      String dateFinOnline,
      Boolean isHTP) {
    List<PrestationContract> prestationContractList = new ArrayList<>();
    Map<PeriodeContractDto, List<Prestation>> prestationsParPeriode =
        mapElementsByPeriode(
            prestationContrats,
            prestationContrat ->
                mapPeriodesContract(prestationContrat.getPeriodes(), periodeDroitContractTP),
            MapperDomaineDroitContractDto::mapPrestation);

    for (Map.Entry<PeriodeContractDto, List<Prestation>> entry : prestationsParPeriode.entrySet()) {
      PrestationContract prestationContract = new PrestationContract();
      prestationContract.setPeriode(
          ajusterPeriode(entry.getKey(), periodeDroitContractDto, dateFinOnline, isHTP));
      prestationContract.setPrestations(entry.getValue());
      prestationContractList.add(prestationContract);
    }
    return prestationContractList;
  }

  private static Prestation mapPrestation(PrestationContrat prestationContrat) {
    Prestation prestation = new Prestation();
    prestation.setCode(prestationContrat.getCode());
    prestation.setCodeRegroupement(prestationContrat.getCodeRegroupement());
    prestation.setLibelle(prestationContrat.getLibelle());
    prestation.setIsEditionRisqueCarte(prestationContrat.getIsEditionRisqueCarte());
    prestation.setDateEffet(prestationContrat.getDateEffet());
    prestation.setFormule(prestationContrat.getFormule());
    prestation.setFormuleMetier(prestationContrat.getFormuleMetier());
    return prestation;
  }

  private static List<PeriodeContractDto> mapPeriodesContract(
      List<Periode> periodes, PeriodeDroitContractTP periodeDroitContractTP) {
    List<PeriodeContractDto> periodeList = new ArrayList<>();
    for (Periode periode : periodes) {
      if (DateUtils.isOverlapping(
          periode.getDebut(),
          periode.getFin(),
          periodeDroitContractTP.getPeriodeDebut(),
          periodeDroitContractTP.getPeriodeFin())) {
        PeriodeContractDto newPeriode = new PeriodeContractDto();
        newPeriode.setDebut(periode.getDebut());
        newPeriode.setFin(periode.getFin());
        periodeList.add(newPeriode);
      }
    }
    return periodeList;
  }

  private static <T, U> Map<PeriodeContractDto, List<U>> mapElementsByPeriode(
      List<T> elements,
      Function<T, List<PeriodeContractDto>> periodesMapper,
      Function<T, U> elementMapper) {
    // Utilisateion de TreeMap pour faire un tri décroissant sur la date de début
    Map<PeriodeContractDto, List<U>> elementsParPeriode =
        new TreeMap<>(
            Comparator.comparing(PeriodeContractDto::getDebut, Comparator.reverseOrder()));

    for (T element : elements) {
      List<PeriodeContractDto> periodes = periodesMapper.apply(element);
      if (CollectionUtils.isNotEmpty(periodes)) {
        periodes.forEach(
            periode ->
                elementsParPeriode
                    .computeIfAbsent(periode, p -> new ArrayList<>())
                    .add(elementMapper.apply(element)));
      }
    }

    return elementsParPeriode;
  }

  private static String formatEmptyDate(String date) {
    if ((StringUtils.isBlank(date))) {
      return Constants.EMPTY_DATE;
    }
    return date;
  }

  private static List<DomaineDroitContractDto> mapDomaineDroit(
      DomaineDroitMailleGarantie domaineDroit,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitContractDto> domaineDroitContractDtoList = new ArrayList<>();
    if (domaineDroit != null && CollectionUtils.isNotEmpty(domaineDroit.getGaranties())) {
      for (MailleGarantie garantie : domaineDroit.getGaranties()) {
        if (garantie != null) {
          List<PeriodeDroitContractTP> periodesDroit =
              Boolean.TRUE.equals(isHTP)
                  ? sortPeriodesDroit(garantie.getPeriodesDroit())
                  : garantie.getPeriodesDroit();
          for (PeriodeDroitContractTP periodeDroitContractTP : periodesDroit) {
            if (periodeDroitContractTP != null) {
              DomaineDroitContractDto domaineDroitDto = new DomaineDroitContractDto();
              domaineDroitDto.setCode(domaineDroit.getCode());
              PeriodeDroitContractDto periodeDroitContractDto =
                  getPeriodeDroitContractDto(
                      dateRestitution, isExclusiviteCarteDematerialise, periodeDroitContractTP);
              completePeriodWithWarranty(garantie, periodeDroitContractDto);
              if (Boolean.TRUE.equals(isHTP)
                  && TypePeriode.ONLINE.equals(periodeDroitContractDto.getTypePeriode())) {
                dateFinOnline = periodeDroitContractDto.getPeriodeFin();
              }
              periodeDroitContractDto.setRemboursements(
                  mapRemboursements(
                      garantie.getRemboursements(),
                      periodeDroitContractTP,
                      periodeDroitContractDto,
                      dateFinOnline,
                      isHTP));
              periodeDroitContractDto.setPrioritesDroit(
                  mapPrioritesDroit(
                      garantie.getPrioritesDroit(),
                      periodeDroitContractTP,
                      periodeDroitContractDto,
                      dateFinOnline,
                      isHTP));
              periodeDroitContractDto.setConventionnements(
                  mapConventionnements(
                      ajusterPeriode(
                          garantie.getConventionnements(),
                          periodeDroitContractDto,
                          dateFinOnline,
                          isHTP),
                      periodeDroitContractTP));
              periodeDroitContractDto.setPrestations(
                  mapPrestations(
                      garantie.getPrestations(),
                      periodeDroitContractTP,
                      periodeDroitContractDto,
                      dateFinOnline,
                      isHTP));
              domaineDroitDto.setPeriodesDroit(List.of(periodeDroitContractDto));
              domaineDroitContractDtoList.add(domaineDroitDto);
            }
          }
        }
      }
    }
    return domaineDroitContractDtoList;
  }

  private static void completePeriodWithWarranty(
      GarantieCommun garantie, PeriodeDroitContractDto periodeDroitContractDto) {
    periodeDroitContractDto.setCodeGarantie(garantie.getCodeGarantie());
    periodeDroitContractDto.setLibelleGarantie(garantie.getLibelleGarantie());
    periodeDroitContractDto.setCodeAssureurGarantie(garantie.getCodeAssureurGarantie());
    periodeDroitContractDto.setDateAdhesionCouverture(garantie.getDateAdhesionCouverture());
  }

  private static List<DomaineDroitContractDto> mapDomaineDroit(
      DomaineDroitMailleProduit domaineDroit,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitContractDto> domaineDroitContractDtoList = new ArrayList<>();
    if (domaineDroit != null && CollectionUtils.isNotEmpty(domaineDroit.getGaranties())) {
      for (GarantieMailleProduit garantie : domaineDroit.getGaranties()) {
        if (CollectionUtils.isNotEmpty(garantie.getProduits())) {
          for (MailleProduit produit : garantie.getProduits()) {
            if (produit != null) {
              List<PeriodeDroitContractTP> periodesDroit =
                  Boolean.TRUE.equals(isHTP)
                      ? sortPeriodesDroit(produit.getPeriodesDroit())
                      : produit.getPeriodesDroit();
              for (PeriodeDroitContractTP periodeDroitContractTP : periodesDroit) {
                if (periodeDroitContractTP != null) {
                  DomaineDroitContractDto domaineDroitDto = new DomaineDroitContractDto();
                  domaineDroitDto.setCode(domaineDroit.getCode());
                  PeriodeDroitContractDto periodeDroitContractDto =
                      getPeriodeDroitContractDto(
                          dateRestitution, isExclusiviteCarteDematerialise, periodeDroitContractTP);
                  completePeriodWithWarranty(garantie, periodeDroitContractDto);
                  completePeriodeWithProduct(produit, periodeDroitContractDto);
                  if (Boolean.TRUE.equals(isHTP)
                      && TypePeriode.ONLINE.equals(periodeDroitContractDto.getTypePeriode())) {
                    dateFinOnline = periodeDroitContractDto.getPeriodeFin();
                  }
                  periodeDroitContractDto.setRemboursements(
                      mapRemboursements(
                          produit.getRemboursements(),
                          periodeDroitContractTP,
                          periodeDroitContractDto,
                          dateFinOnline,
                          isHTP));
                  periodeDroitContractDto.setPrioritesDroit(
                      mapPrioritesDroit(
                          produit.getPrioritesDroit(),
                          periodeDroitContractTP,
                          periodeDroitContractDto,
                          dateFinOnline,
                          isHTP));
                  periodeDroitContractDto.setConventionnements(
                      mapConventionnements(
                          ajusterPeriode(
                              produit.getConventionnements(),
                              periodeDroitContractDto,
                              dateFinOnline,
                              isHTP),
                          periodeDroitContractTP));
                  periodeDroitContractDto.setPrestations(
                      mapPrestations(
                          produit.getPrestations(),
                          periodeDroitContractTP,
                          periodeDroitContractDto,
                          dateFinOnline,
                          isHTP));
                  domaineDroitDto.setPeriodesDroit(List.of(periodeDroitContractDto));
                  domaineDroitContractDtoList.add(domaineDroitDto);
                }
              }
            }
          }
        }
      }
    }
    return domaineDroitContractDtoList;
  }

  private static void completePeriodeWithProduct(
      ProduitCommun produit, PeriodeDroitContractDto periodeDroitContractDto) {
    periodeDroitContractDto.setCodeProduit(produit.getCodeProduit());
    periodeDroitContractDto.setLibelleProduit(produit.getLibelleProduit());
    periodeDroitContractDto.setCodeExterneProduit(produit.getCodeExterneProduit());
    periodeDroitContractDto.setLibelleExterneProduit(produit.getLibelleExterneProduit());
    periodeDroitContractDto.setCodeOc(produit.getCodeOc());
    periodeDroitContractDto.setCodeOffre(produit.getCodeOffre());
  }

  private static List<DomaineDroitContractDto> mapDomaineDroit(
      DomaineDroitMailleRefCouv domaineDroit,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitContractDto> domaineDroitContractDtoList = new ArrayList<>();
    if (domaineDroit != null && CollectionUtils.isNotEmpty(domaineDroit.getGaranties())) {
      for (GarantieMailleRefCouv garantie : domaineDroit.getGaranties()) {
        if (CollectionUtils.isNotEmpty(garantie.getProduits())) {
          for (ProduitMailleRefCouv produit : garantie.getProduits()) {
            if (produit != null && CollectionUtils.isNotEmpty(produit.getReferencesCouverture())) {
              for (MailleReferenceCouverture referenceCouverture :
                  produit.getReferencesCouverture()) {
                if (referenceCouverture != null) {
                  List<PeriodeDroitContractTP> periodesDroit =
                      Boolean.TRUE.equals(isHTP)
                          ? sortPeriodesDroit(referenceCouverture.getPeriodesDroit())
                          : referenceCouverture.getPeriodesDroit();
                  for (PeriodeDroitContractTP periodeDroitContractTP : periodesDroit) {
                    if (periodeDroitContractTP != null) {
                      DomaineDroitContractDto domaineDroitDto = new DomaineDroitContractDto();
                      domaineDroitDto.setCode(domaineDroit.getCode());
                      PeriodeDroitContractDto periodeDroitContractDto =
                          getPeriodeDroitContractDto(
                              dateRestitution,
                              isExclusiviteCarteDematerialise,
                              periodeDroitContractTP);
                      completePeriodWithWarranty(garantie, periodeDroitContractDto);
                      completePeriodeWithProduct(produit, periodeDroitContractDto);
                      periodeDroitContractDto.setMasqueFormule(
                          referenceCouverture.getFormulaMask());
                      periodeDroitContractDto.setReferenceCouverture(
                          referenceCouverture.getReferenceCouverture());
                      if (Boolean.TRUE.equals(isHTP)
                          && TypePeriode.ONLINE.equals(periodeDroitContractDto.getTypePeriode())) {
                        dateFinOnline = periodeDroitContractDto.getPeriodeFin();
                      }
                      periodeDroitContractDto.setRemboursements(
                          mapRemboursements(
                              referenceCouverture.getRemboursements(),
                              periodeDroitContractTP,
                              periodeDroitContractDto,
                              dateFinOnline,
                              isHTP));
                      periodeDroitContractDto.setPrioritesDroit(
                          mapPrioritesDroit(
                              referenceCouverture.getPrioritesDroit(),
                              periodeDroitContractTP,
                              periodeDroitContractDto,
                              dateFinOnline,
                              isHTP));
                      periodeDroitContractDto.setConventionnements(
                          mapConventionnements(
                              ajusterPeriode(
                                  referenceCouverture.getConventionnements(),
                                  periodeDroitContractDto,
                                  dateFinOnline,
                                  isHTP),
                              periodeDroitContractTP));
                      periodeDroitContractDto.setPrestations(
                          mapPrestations(
                              referenceCouverture.getPrestations(),
                              periodeDroitContractTP,
                              periodeDroitContractDto,
                              dateFinOnline,
                              isHTP));
                      domaineDroitDto.setPeriodesDroit(List.of(periodeDroitContractDto));
                      domaineDroitContractDtoList.add(domaineDroitDto);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return domaineDroitContractDtoList;
  }

  private static List<DomaineDroitContractDto> mapDomaineDroit(
      DomaineDroitContractTP domaineDroit,
      String dateRestitution,
      Boolean isExclusiviteCarteDematerialise,
      Boolean isHTP) {
    List<DomaineDroitContractDto> domaineDroitContractDtoList = new ArrayList<>();
    if (domaineDroit != null && CollectionUtils.isNotEmpty(domaineDroit.getGaranties())) {
      for (Garantie garantie : domaineDroit.getGaranties()) {
        if (CollectionUtils.isNotEmpty(garantie.getProduits())) {
          for (Produit produit : garantie.getProduits()) {
            if (produit != null && CollectionUtils.isNotEmpty(produit.getReferencesCouverture())) {
              for (ReferenceCouverture referenceCouverture : produit.getReferencesCouverture()) {
                if (referenceCouverture != null
                    && CollectionUtils.isNotEmpty(referenceCouverture.getNaturesPrestation())) {
                  for (NaturePrestation naturePrestation :
                      referenceCouverture.getNaturesPrestation()) {
                    if (naturePrestation != null
                        && CollectionUtils.isNotEmpty(naturePrestation.getPeriodesDroit())) {
                      List<PeriodeDroitContractTP> periodesDroit =
                          Boolean.TRUE.equals(isHTP)
                              ? sortPeriodesDroit(naturePrestation.getPeriodesDroit())
                              : naturePrestation.getPeriodesDroit();
                      for (PeriodeDroitContractTP periodeDroitContractTP : periodesDroit) {
                        if (periodeDroitContractTP != null) {
                          DomaineDroitContractDto domaineDroitDto = new DomaineDroitContractDto();
                          domaineDroitDto.setCode(domaineDroit.getCode());
                          PeriodeDroitContractDto periodeDroitContractDto =
                              getPeriodeDroitContractDto(
                                  dateRestitution,
                                  isExclusiviteCarteDematerialise,
                                  periodeDroitContractTP);
                          completePeriodWithWarranty(garantie, periodeDroitContractDto);
                          completePeriodeWithProduct(produit, periodeDroitContractDto);
                          periodeDroitContractDto.setReferenceCouverture(
                              referenceCouverture.getReferenceCouverture());
                          periodeDroitContractDto.setMasqueFormule(
                              referenceCouverture.getFormulaMask());
                          periodeDroitContractDto.setCodeOc(produit.getCodeOc());
                          periodeDroitContractDto.setNaturePrestation(
                              naturePrestation.getNaturePrestation());
                          periodeDroitContractDto.setCodeOffre(produit.getCodeOffre());
                          periodeDroitContractDto.setDateAdhesionCouverture(
                              garantie.getDateAdhesionCouverture());
                          if (Boolean.TRUE.equals(isHTP)
                              && TypePeriode.ONLINE.equals(
                                  periodeDroitContractDto.getTypePeriode())) {
                            dateFinOnline = periodeDroitContractDto.getPeriodeFin();
                          }
                          periodeDroitContractDto.setRemboursements(
                              mapRemboursements(
                                  naturePrestation.getRemboursements(),
                                  periodeDroitContractTP,
                                  periodeDroitContractDto,
                                  dateFinOnline,
                                  isHTP));
                          periodeDroitContractDto.setPrioritesDroit(
                              mapPrioritesDroit(
                                  naturePrestation.getPrioritesDroit(),
                                  periodeDroitContractTP,
                                  periodeDroitContractDto,
                                  dateFinOnline,
                                  isHTP));
                          periodeDroitContractDto.setConventionnements(
                              mapConventionnements(
                                  ajusterPeriode(
                                      naturePrestation.getConventionnements(),
                                      periodeDroitContractDto,
                                      dateFinOnline,
                                      isHTP),
                                  periodeDroitContractTP));
                          periodeDroitContractDto.setPrestations(
                              mapPrestations(
                                  naturePrestation.getPrestations(),
                                  periodeDroitContractTP,
                                  periodeDroitContractDto,
                                  dateFinOnline,
                                  isHTP));
                          domaineDroitDto.setPeriodesDroit(List.of(periodeDroitContractDto));
                          domaineDroitContractDtoList.add(domaineDroitDto);
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    return domaineDroitContractDtoList;
  }
}
