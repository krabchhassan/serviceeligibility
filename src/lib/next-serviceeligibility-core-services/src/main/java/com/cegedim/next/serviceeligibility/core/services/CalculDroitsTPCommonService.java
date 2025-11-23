package com.cegedim.next.serviceeligibility.core.services;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import com.cegedim.next.serviceeligibility.core.model.domain.carence.ParametrageCarence;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.Anomaly;
import com.cegedim.next.serviceeligibility.core.model.domain.trigger.TriggeredBeneficiaryAnomaly;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.PeriodeCarence;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.CarenceDroit;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.services.pojo.*;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.CarenceException;
import io.micrometer.tracing.annotation.ContinueSpan;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public abstract class CalculDroitsTPCommonService {

  protected final IPwService pwService;
  protected final ContractElementService contractElementService;
  protected final CarenceService carenceService;

  protected static boolean calculDroitsTpWorkingExtendedList(
      boolean erreurParametrage,
      List<DroitsTPExtended> listeDroits,
      List<DroitsTPExtended> standardExtendedList,
      List<DroitsTPExtended> workingExtendedList,
      int countSettingsParameterInError) {
    if (CollectionUtils.isEmpty(workingExtendedList)
        && CollectionUtils.isNotEmpty(standardExtendedList)) {
      erreurParametrage = true;
    } else {
      if (countSettingsParameterInError > 0) {
        workingExtendedList.forEach(item -> item.setPapNatureTags(null));
      }
      listeDroits.addAll(workingExtendedList);
    }
    return erreurParametrage;
  }

  protected boolean requestPeriodNotIncludedInPeriodPw(
      List<ParametrageAtelierProduit> parametrageAtelierProduitList,
      LocalDate dateDebutDroit,
      LocalDate dateFinDroit) {
    List<String> dateDebutPw =
        parametrageAtelierProduitList.stream()
            .filter(Objects::nonNull)
            .map(ParametrageAtelierProduit::getPwValidityDate)
            .toList();
    Optional<String> optionalDebut =
        dateDebutPw.stream().filter(Objects::nonNull).min(String::compareTo);
    List<String> dateFinPw =
        parametrageAtelierProduitList.stream()
            .filter(Objects::nonNull)
            .map(ParametrageAtelierProduit::getPwEndValidityDate)
            .toList();
    LocalDate dateFinPW = null;
    if (dateFinPw.stream().noneMatch(Objects::isNull)) {
      Optional<String> optionalFin =
          dateFinPw.stream().filter(Objects::nonNull).max(String::compareTo);
      dateFinPW = DateUtils.parse(optionalFin.orElse(null), DateUtils.FORMATTER);
    }
    LocalDate dateDebutPW = DateUtils.parse(optionalDebut.get(), DateUtils.FORMATTER); // NOSONAR

    java.time.Period periodD = java.time.Period.between(dateDebutPW, dateDebutDroit);
    java.time.Period periodF = null;
    if (dateFinPW != null
        && dateFinDroit == null) { // si y a une date de fin sur PW alors que les droits non,
      // c'est que ce n'est pas couvert
      return true;
    }
    if (dateFinPW != null) {
      periodF = java.time.Period.between(dateFinDroit, dateFinPW);
    }
    return periodD.isNegative() || (periodF != null && periodF.isNegative());
  }

  @ContinueSpan(log = "getParametrageCarenceList")
  public List<ParametrageCarence> getParametrageCarenceList(
      String codeAssureur,
      String codeOffre,
      String codeProduit,
      String codeCarence,
      String dateDebut,
      String dateFin,
      boolean silentException)
      throws CarenceException {
    List<ParametrageCarence> parametrageCarenceList =
        callCarence(codeAssureur, codeOffre, codeProduit, codeCarence, silentException);

    if (CollectionUtils.isEmpty(parametrageCarenceList) && !silentException) {
      TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
          TriggeredBeneficiaryAnomaly.create(
              Anomaly.WAITINGS_PERIODES_SETTINGS_NOT_FOUND,
              codeCarence,
              dateDebut,
              dateFin,
              codeAssureur,
              codeOffre,
              codeProduit);
      throw new CarenceException(triggeredBeneficiaryAnomaly);
    }
    if (errorParametrageDatesCarence(dateDebut, dateFin, parametrageCarenceList)) {
      if (!silentException) {
        TriggeredBeneficiaryAnomaly triggeredBeneficiaryAnomaly =
            TriggeredBeneficiaryAnomaly.create(
                Anomaly.WAITINGS_PERIODES_SETTINGS_NOT_FOUND,
                codeCarence,
                dateDebut,
                dateFin,
                codeAssureur,
                codeOffre,
                codeProduit);
        throw new CarenceException(triggeredBeneficiaryAnomaly);
      } else {
        return Collections.emptyList();
      }
    }
    return parametrageCarenceList;
  }

  /**
   * Controle que la liste de parametrage couvre entierement la periode en entrée
   *
   * @param dateDebutCarence
   * @param dateFinCarence
   * @param parametrageCarenceList
   * @return
   */
  protected boolean errorParametrageDatesCarence(
      String dateDebutCarence,
      String dateFinCarence,
      List<ParametrageCarence> parametrageCarenceList) {
    parametrageCarenceList.sort(Comparator.comparing(ParametrageCarence::getDateDebutParametrage));
    boolean isContinuous = checkContinuousPeriod(parametrageCarenceList);
    if (!isContinuous) {
      return false;
    }

    LocalDate dateDebut = DateUtils.stringToDate(dateDebutCarence);
    LocalDate dateFin = DateUtils.stringToDate(dateFinCarence);
    int nbParameterError = parametrageCarenceList.size();
    for (ParametrageCarence parametrageCarence : parametrageCarenceList) {
      LocalDate dateDebutParametrage =
          DateUtils.stringToDate(parametrageCarence.getDateDebutParametrage());
      LocalDate dateFinParametrage =
          DateUtils.stringToDate(parametrageCarence.getDateFinParametrage());
      if (DateUtils.betweenLocalDate(dateDebut, dateDebutParametrage, dateFinParametrage)
          && dateFin != null
          && DateUtils.betweenLocalDate(dateFin, dateDebutParametrage, dateFinParametrage)) {
        nbParameterError--;
      }
    }

    return nbParameterError == parametrageCarenceList.size();
  }

  protected boolean checkContinuousPeriod(List<ParametrageCarence> parametrageCarenceList) {
    // check if the period is continuous
    String dateFin = null;
    for (ParametrageCarence parametrageCarence : parametrageCarenceList) {
      if (parametrageCarence.getDateFinParametrage() != null) {
        dateFin = parametrageCarence.getDateFinParametrage();
      } else {
        if (dateFin != null) {
          String dateDebutPlus1 =
              DateUtils.getStringDatePlusDays(
                  parametrageCarence.getDateDebutParametrage(), 1, DateUtils.FORMATTER);
          if (!dateDebutPlus1.equals(dateFin)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Filtre la reponse du product workshop pour ne garder que les parametrage en lien avec la
   * naturePrestation demande. Si nature vide ou null alors on accepte toutes les natures
   */
  protected void filterPWResponseNature(
      String naturePrestation, List<ParametrageAtelierProduit> pwRes) {
    if (StringUtils.isNotBlank(naturePrestation)) {
      Iterator<ParametrageAtelierProduit> iterator = pwRes.iterator();
      while (iterator.hasNext()) {
        ParametrageAtelierProduit param = iterator.next();
        param.getNaturesTags().removeIf(pap -> !naturePrestation.equals(pap.getNature()));
        if (param.getNaturesTags().isEmpty()) {
          iterator.remove();
        }
      }
    }
  }

  protected static ParametrageBobb fillBobbParameters(
      Periode periode, ContractElement contractElement) {
    ParametrageBobb parametrageBobb = null;
    if (contractElement.getProductElements() != null) {
      parametrageBobb = new ParametrageBobb();
      parametrageBobb.setCodeAssureur(contractElement.getCodeInsurer());
      parametrageBobb.setCodeGarantie(contractElement.getCodeAMC());
      parametrageBobb.setParametrageBobbProductElements(new ArrayList<>());
      List<ProductElement> productElements = contractElement.getProductElements();
      for (ProductElement productElement : productElements) {
        if (DateUtils.compareDate(periode.getDebut(), periode.getFin()) <= 0
            && DateUtils.compareDate(
                    periode.getDebut(), DateUtils.formatDateTime(productElement.getTo()))
                <= 0
            && DateUtils.compareDate(
                    periode.getFin(), DateUtils.formatDateTime(productElement.getFrom()))
                >= 0) {
          ParametrageBobbProductElement parametrageBobbProductElement1 =
              parametrageBobb.getParametrageBobbProductElements().stream()
                  .filter(
                      parametrageBobbProductElement ->
                          parametrageBobbProductElement
                                  .getCodeProduit()
                                  .equals(productElement.getCodeProduct())
                              && parametrageBobbProductElement
                                  .getCodeOffre()
                                  .equals(productElement.getCodeOffer())
                              && parametrageBobbProductElement
                                  .getCodeOc()
                                  .equals(productElement.getCodeAmc()))
                  .findFirst()
                  .orElse(null);
          if (parametrageBobbProductElement1 == null) {
            parametrageBobbProductElement1 = new ParametrageBobbProductElement();
            parametrageBobb.getParametrageBobbProductElements().add(parametrageBobbProductElement1);
            parametrageBobbProductElement1.setCodeOc(productElement.getCodeAmc());
            parametrageBobbProductElement1.setCodeOffre(productElement.getCodeOffer());
            parametrageBobbProductElement1.setCodeProduit(productElement.getCodeProduct());
          }
          ParametrageBobbNaturePrestation parametrageBobbNaturePrestation =
              new ParametrageBobbNaturePrestation();
          parametrageBobbNaturePrestation.setNaturePrestation(
              productElement.getCodeBenefitNature());
          parametrageBobbNaturePrestation.setDateDebutBobb(
              DateUtils.formatDateTime(productElement.getFrom()));
          parametrageBobbNaturePrestation.setDateFinBobb(
              DateUtils.formatDateTime(productElement.getTo()));
          parametrageBobbNaturePrestation.setDateDebut(
              DateUtils.getMaxDate(
                  DateUtils.formatDateTime(productElement.getFrom()),
                  periode.getDebut(),
                  DateUtils.FORMATTER));
          parametrageBobbNaturePrestation.setDateFin(
              DateUtils.getMinDate(
                  DateUtils.formatDateTime(productElement.getTo()),
                  periode.getFin(),
                  DateUtils.FORMATTER));
          parametrageBobbProductElement1.getNaturePrestation().add(parametrageBobbNaturePrestation);
        }
      }
    }
    return parametrageBobb;
  }

  @ContinueSpan(log = "callCarence")
  protected List<ParametrageCarence> callCarence(
      String codeAssureur,
      String codeOffre,
      String codeProduit,
      String codeCarence,
      boolean silentException)
      throws CarenceException {
    try {
      List<ParametrageCarence> parametrageCarenceList =
          carenceService.getParametragesCarence(codeAssureur, codeOffre, codeProduit);
      if (CollectionUtils.isNotEmpty(parametrageCarenceList)) {
        return parametrageCarenceList.stream()
            .filter(parametrageCarence -> parametrageCarence.getCodeCarence().equals(codeCarence))
            .collect(Collectors.toList()); // NOSONAR ImmutableCollections after sort.
      }
    } catch (CarenceException e) {
      if (!silentException) {
        throw e;
      }
    }

    return Collections.emptyList();
  }

  protected List<String> getNaturesPrestation(
      List<ParametrageAtelierProduit> parametragesAtelierProduitOffline) {
    return parametragesAtelierProduitOffline.stream()
        .flatMap(
            parametrageAtelierProduit ->
                parametrageAtelierProduit.getNaturesTags().stream().map(PAPNatureTags::getNature))
        .toList();
  }

  protected List<String> getNaturesPrestationInSettings(
      List<ParametrageCarence> parametrageCarence) {
    return parametrageCarence.stream().map(ParametrageCarence::getNaturePrestation).toList();
  }

  /**
   * Si la {@link CarenceDroit} est non nulle ecrase la dateFinOnline du {@link DroitsTPExtended}
   * par la plus petite des dates entre dateFinParametrage et carence.getPeriode().getFin().
   * Complete le {@link DroitsTPExtended} avec les donnees presentes dans la {@link CarenceDroit}
   */
  protected void completeFinOnlineAndDroitsWithCarence(
      @Nullable CarenceDroit carence,
      DroitsTPExtended droitsTPExtended,
      String dateFinParametrage,
      PeriodeCarence periodeCarence) {
    if (carence != null) {
      String finOnline =
          DateUtils.getMinDate(
              dateFinParametrage, carence.getPeriode().getFin(), DateUtils.FORMATTER);
      droitsTPExtended.setDateFinOnline(finOnline);

      // BLUE-4652 Lors de la génération des
      // droits TP, les droit liés à la
      // garantie de remplacement ont une
      // date de fin ONLINE au 31/12/2023,
      // alors que la carence se termine le
      // 31/01/2024

      completeDroitsTPExtendedCarence(droitsTPExtended, carence);

      droitsTPExtended.setCarencePeriode(periodeCarence);
    }
  }

  /**
   * Initialise dateDebutForWorking a la plus grande date entre dateDebut et
   * parametrageAtelierProduit.getValidityDate() && Initialise dateFinForWorking a la plus petite
   * date entre dateFin et parametrageAtelierProduit.getEndValidityDate()
   */
  protected void completeDateBufferWithAtelierProd(
      DateBuffer dateBuffer, ParametrageAtelierProduit parametrageAtelierProduit) {
    dateBuffer.dateDebutForWorking =
        DateUtils.getMaxDate(
            dateBuffer.dateDebut, parametrageAtelierProduit.getValidityDate(), DateUtils.FORMATTER);
    dateBuffer.dateFinForWorking =
        DateUtils.getMinDate(
            dateBuffer.dateFin,
            parametrageAtelierProduit.getEndValidityDate(),
            DateUtils.FORMATTER);
    dateBuffer.dateFinOnlineForWorking =
        DateUtils.getMinDate(
            dateBuffer.dateFinOnline,
            parametrageAtelierProduit.getEndValidityDate(),
            DateUtils.FORMATTER);
  }

  /**
   * Remplace la dateDebutForWorking par parametrageBobbNaturePrestation.getDateDebut() si celle ci
   * existe et est plus grande && Remplace la dateFinForWorking par
   * parametrageBobbNaturePrestation.getDateFin() si celle ci existe et est plus petite
   */
  protected void completeDateBufferWithBobbParams(
      DateBuffer dateBuffer, ParametrageBobbNaturePrestation parametrageBobbNaturePrestation) {
    dateBuffer.dateDebutForWorking =
        DateUtils.getMaxDate(
            dateBuffer.dateDebutForWorking,
            parametrageBobbNaturePrestation.getDateDebut(),
            DateUtils.FORMATTER);
    dateBuffer.dateFinForWorking =
        DateUtils.getMinDate(
            dateBuffer.dateFinForWorking,
            parametrageBobbNaturePrestation.getDateFin(),
            DateUtils.FORMATTER);
  }

  /**
   * Parcourt la liste de carence ayant les meme naturePrestation que l atelierProduit && Remplace
   * dateDebutForWorking par la plus grande date parametrageCarence.getDateDebutParametrage() des
   * carences && Remplace dateFinForWorking par la plus petite date
   * parametrageCarence.getDateFinParametrage() des carences && Initialise dateFinParametrage par la
   * plus petite date parametrageCarence.getDateFinParametrage() des carences
   */
  protected PeriodeCarence completeDateBufferWithCarences(
      DateBuffer dateBuffer,
      ParametrageAtelierProduit parametrageAtelierProduit,
      List<ParametrageCarence> parametrageCarenceList) {
    List<ParametrageCarence> parametrageCarenceSubList =
        parametrageCarenceList.stream()
            .filter(
                parametrageCarence ->
                    parametrageAtelierProduit.getNaturesTags().stream()
                        .anyMatch(
                            papNatureTags ->
                                papNatureTags
                                    .getNature()
                                    .equals(parametrageCarence.getNaturePrestation())))
            .toList();
    PeriodeCarence periodeCarence = new PeriodeCarence();
    for (ParametrageCarence parametrageCarence : parametrageCarenceSubList) {
      dateBuffer.dateDebutForWorking =
          DateUtils.getMaxDate(
              dateBuffer.dateDebutForWorking,
              parametrageCarence.getDateDebutParametrage(),
              DateUtils.FORMATTER);
      dateBuffer.dateFinForWorking =
          DateUtils.getMinDate(
              dateBuffer.dateFinForWorking,
              parametrageCarence.getDateFinParametrage(),
              DateUtils.FORMATTER);

      dateBuffer.dateFinParametrage =
          DateUtils.getMinDate(
              dateBuffer.dateFinParametrage,
              parametrageCarence.getDateFinParametrage(),
              DateUtils.FORMATTER);
      periodeCarence.setDebut(parametrageCarence.getDateDebutParametrage());
      periodeCarence.setFin(parametrageCarence.getDateFinParametrage());
    }
    return periodeCarence;
  }

  /**
   * Recupere la liste de paramétrage Bobb ayant soit le meme tag de nature, soit la nature vide ""
   * (qui veut dire toutes les natures) sinon renvoie une liste vide
   */
  protected List<ParametrageBobbNaturePrestation> getParametrageBobbNaturePrestations(
      String nature, @NonNull Map<String, List<ParametrageBobbNaturePrestation>> bobbNatureFilter) {
    if (bobbNatureFilter.containsKey(nature)) {
      return bobbNatureFilter.get(nature);
    } else {
      return bobbNatureFilter.getOrDefault(
          Constants.NATURE_PRESTATION_VIDE_BOBB, Collections.emptyList());
    }
  }

  /**
   * Complete un {@link DroitsTPExtended} avec les donnes presentes dans {@link
   * ParametrageAtelierProduit}
   */
  protected void completeDroitsTPExtendedAtelierProd(
      DroitsTPExtended droitTPExtended, ParametrageAtelierProduit parametrageAtelierProduit) {
    droitTPExtended.setDetailsOffline(parametrageAtelierProduit.getDetailsOffline());
    droitTPExtended.setDetailsOnline(parametrageAtelierProduit.getDetailsOnline());
    droitTPExtended.setCodeDomaine(parametrageAtelierProduit.getDomaine());
    droitTPExtended.setCodeOffre(parametrageAtelierProduit.getCodeOffre());
    droitTPExtended.setCodeProduit(parametrageAtelierProduit.getCodeProduit());
    droitTPExtended.setVersionOffre(parametrageAtelierProduit.getVersion());
    droitTPExtended.setCodeOc(parametrageAtelierProduit.getCodeOc());
    droitTPExtended.setModeAssemblage(parametrageAtelierProduit.getModeAssemblage());
  }

  /**
   * Cree un {@link DroitsTPExtended} et initialise ses donnees avec celle du {@link DroitAssure}
   *
   * @return {@link DroitsTPExtended} initialise
   */
  protected DroitsTPExtended createDroitsTPExtendedBase(
      DroitAssure droitHTP, Periode periodeDroitCalcule) {
    DroitsTPExtended droitTPExtended = new DroitsTPExtended();
    droitTPExtended.setCodeGarantie(droitHTP.getCode());
    droitTPExtended.setInsurerCode(droitHTP.getCodeAssureur());
    droitTPExtended.setType(droitHTP.getType());
    droitTPExtended.setOrdrePriorisation(droitHTP.getOrdrePriorisation());
    droitTPExtended.setDateAncienneteGarantie(droitHTP.getDateAncienneteGarantie());
    droitTPExtended.setLibelle(droitHTP.getLibelle());
    droitTPExtended.setDateDebut(periodeDroitCalcule.getDebut());
    droitTPExtended.setDateFin(periodeDroitCalcule.getFin());

    return droitTPExtended;
  }

  protected void completeDroitsTPExtendedCarence(
      DroitsTPExtended droitTPExtended, CarenceDroit carence) {
    // Settings of original values
    droitTPExtended.setOriginCode(droitTPExtended.getCodeGarantie());
    droitTPExtended.setOriginInsurerCode(droitTPExtended.getInsurerCode());

    // Values from carence
    droitTPExtended.setCodeGarantie(carence.getDroitRemplacement().getCode());
    droitTPExtended.setInsurerCode(carence.getDroitRemplacement().getCodeAssureur());
    droitTPExtended.setCarenceCode(carence.getCode());
  }

  // ----- update listeDroits : pour chaque, si naturePrestation présente dans
  // parametrageCarenceList si dateDebutCarence > dateDebut, alors duplication du
  // droit avec dateFin = dateDebutCarence-1 puis update droit déjà présent avec
  // dateDebut = dateFinCarence+1

  /**
   * Applique les paramétrages d'une carence sur les droits
   *
   * @param listeDroits
   * @param parametrageCarenceList
   * @param dateFinCarence
   */
  protected void carencing(
      List<DroitsTPExtended> listeDroits,
      List<ParametrageCarence> parametrageCarenceList,
      String dateFinCarence) {
    parametrageCarenceList.sort(
        Comparator.comparing(ParametrageCarence::getDateDebutParametrage).reversed());
    List<DroitsTPExtended> listeDroitsToAdd = new ArrayList<>();
    String dateFinCarenceForWorking;
    for (DroitsTPExtended droitsTPExtended : listeDroits) {
      dateFinCarenceForWorking = dateFinCarence;
      DroitsTPExtended droitsTPExtendedOriginal = droitsTPExtended;
      List<ParametrageCarence> parametrageCarenceSubList =
          parametrageCarenceList.stream()
              .filter(
                  parametrageCarence ->
                      droitsTPExtended
                          .getPapNatureTags()
                          .getNature()
                          .equals(parametrageCarence.getNaturePrestation()))
              .toList();
      for (ParametrageCarence parametrageCarence : parametrageCarenceSubList) {
        dateFinCarenceForWorking =
            DateUtils.getMinDate(
                Objects.requireNonNullElse(
                    parametrageCarence.getDateFinParametrage(), dateFinCarenceForWorking),
                dateFinCarenceForWorking,
                DateUtils.FORMATTER);

        droitsTPExtendedOriginal =
            getDroitsTPExtended(
                listeDroitsToAdd,
                dateFinCarenceForWorking,
                droitsTPExtendedOriginal,
                parametrageCarence);
      }
    }

    listeDroits.removeIf(
        droitsTP ->
            droitsTP.getDateFin() != null
                && DateUtils.before(droitsTP.getDateFin(), droitsTP.getDateDebut()));

    for (DroitsTPExtended droitsToAdd : listeDroitsToAdd) {
      if (droitsToAdd.getDateFin() == null
          || !DateUtils.before(droitsToAdd.getDateFin(), droitsToAdd.getDateDebut())) {
        listeDroits.add(droitsToAdd);
      }
    }
  }

  protected DroitsTPExtended getDroitsTPExtended(
      List<DroitsTPExtended> listeDroitsToAdd,
      String dateFinCarenceForWorking,
      DroitsTPExtended droitsTPExtendedOriginal,
      ParametrageCarence parametrageCarence) {
    if (DateUtils.before(
        droitsTPExtendedOriginal.getDateDebut(), parametrageCarence.getDateDebutParametrage())) {
      DroitsTPExtended droitsTPExtendedClone = new DroitsTPExtended(droitsTPExtendedOriginal);
      droitsTPExtendedOriginal.setDateDebut(
          DateUtils.getStringDatePlusDays(dateFinCarenceForWorking, 1, DateUtils.FORMATTER));
      droitsTPExtendedClone.setDateFin(
          DateUtils.getMinDate(
              droitsTPExtendedOriginal.getDateFin(),
              DateUtils.getStringDatePlusDays(
                  parametrageCarence.getDateDebutParametrage(), -1, DateUtils.FORMATTER),
              DateUtils.FORMATTER));
      if (parametrageCarence.getDateDebutParametrage() != null
          && DateUtils.before(
              parametrageCarence.getDateDebutParametrage(),
              droitsTPExtendedOriginal.getDateFin())) {
        droitsTPExtendedClone.setDateFinOnline(droitsTPExtendedClone.getDateFin());
      }
      droitsTPExtendedOriginal = droitsTPExtendedClone;
      droitsTPExtendedOriginal.setCarencePeriode(
          new PeriodeCarence(
              parametrageCarence.getDateDebutParametrage(),
              parametrageCarence.getDateFinParametrage()));
      listeDroitsToAdd.add(droitsTPExtendedOriginal);
    } else {
      String newStartDate =
          DateUtils.getStringDatePlusDays(dateFinCarenceForWorking, 1, DateUtils.FORMATTER);
      // gestion du cas où la date de début requêté par le PAU est supérieur à la fin
      // de la carence.
      if (!DateUtils.before(newStartDate, droitsTPExtendedOriginal.getDateDebut())) {
        droitsTPExtendedOriginal.setDateDebut(newStartDate);
        droitsTPExtendedOriginal.setCarencePeriode(
            new PeriodeCarence(
                parametrageCarence.getDateDebutParametrage(),
                parametrageCarence.getDateFinParametrage()));
      }
    }
    return droitsTPExtendedOriginal;
  }

  protected ContractElement getContractElement(
      @NotNull final String codeContractElement, @NotNull final String codeInsurer) {
    return contractElementService.get(codeContractElement, codeInsurer, false);
  }

  protected ContractElement getContractElementIncludingIgnored(
      @NotNull final String codeContractElement, @NotNull final String codeInsurer) {
    return contractElementService.get(codeContractElement, codeInsurer, true);
  }

  /** Classe buffer de multiple dates qui servent au parametrage des droits */
  protected static class DateBuffer {
    String dateDebut;
    String dateFin;
    String dateDebutForWorking;
    String dateFinForWorking;
    String dateFinParametrage;
    String dateFinOnlineForWorking;
    String dateFinOnline;

    DateBuffer copy() {
      DateBuffer copyBuffer = new DateBuffer();
      copyBuffer.dateDebut = dateDebut;
      copyBuffer.dateFin = dateFin;
      copyBuffer.dateDebutForWorking = dateDebutForWorking;
      copyBuffer.dateFinForWorking = dateFinForWorking;
      copyBuffer.dateFinParametrage = dateFinParametrage;
      copyBuffer.dateFinOnline = dateFinOnline;
      copyBuffer.dateFinOnlineForWorking = dateFinOnlineForWorking;

      return copyBuffer;
    }
  }

  protected static boolean bufferDatesInverse(DateBuffer dateBufferPerBobbParam) {
    return dateBufferPerBobbParam.dateFinForWorking != null
        && DateUtils.before(
            dateBufferPerBobbParam.dateFinForWorking, dateBufferPerBobbParam.dateDebutForWorking);
  }
}
