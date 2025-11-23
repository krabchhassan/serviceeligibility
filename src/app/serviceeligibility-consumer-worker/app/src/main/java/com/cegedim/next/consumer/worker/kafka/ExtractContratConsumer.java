package com.cegedim.next.consumer.worker.kafka;

import com.cegedim.beyond.messaging.api.annotation.MessageHandler;
import com.cegedim.beyond.messaging.api.annotation.MessageListener;
import com.cegedim.next.consumer.worker.exception.AssureNotFoundException;
import com.cegedim.next.consumer.worker.exception.ExtractContractException;
import com.cegedim.next.consumer.worker.model.BeneficiaireBLB;
import com.cegedim.next.consumer.worker.model.ContratBLB;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbTrackingRepo;
import com.cegedim.next.serviceeligibility.core.dao.ServicePrestationDao;
import com.cegedim.next.serviceeligibility.core.kafka.observability.MessageType;
import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbTracking;
import com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.Periode;
import com.cegedim.next.serviceeligibility.core.model.kafka.benef.BeneficiaireId;
import com.cegedim.next.serviceeligibility.core.model.kafka.contract.IdentiteContrat;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssureCommun;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.pau.UniqueAccessPointServiceHTPImpl;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.core.utils.ContextConstants;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.RequestValidationException;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.UAPFunctionalException;
import com.cegedim.next.serviceeligibility.core.webservices.UniqueAccessPointTriHTP;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.GenericRightDto;
import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.UniqueAccessPointRequestV5;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component("ExtractContractConsumer")
@MessageListener(topics = "${kafka.topic.bdd-extract-contract}")
public class ExtractContratConsumer {

  @Autowired BlbProducer blbProducer;

  @Autowired UniqueAccessPointTriHTP uniqueAccessPointTriHTP;

  @Qualifier("uniqueAccessPointServiceHTPV5")
  @Autowired
  UniqueAccessPointServiceHTPImpl uniqueAccessPointServiceV5HTP;

  @Autowired ServicePrestationDao servicePrestationDao;

  @Autowired BddsToBlbTrackingRepo blbTrackingDao;

  @Autowired EventService eventService;

  public ExtractContratConsumer(@Lazy final BlbProducer blbProducer) {
    this.blbProducer = blbProducer;
  }

  public ExtractContratConsumer() {}

  @MessageHandler
  public void processMessageExtractContract(@Payload final BeneficiaireId beneficiaryId)
      throws ExtractContractException, AssureNotFoundException {

    log.info(
        "In extract contract consumer - Processing beneficiary {} with trackingId {}",
        beneficiaryId.getKey(),
        beneficiaryId.getTrackingId());

    try {
      final BeneficiaireBLB beneficiaryBlb = new BeneficiaireBLB();
      beneficiaryBlb.setNir(beneficiaryId.getNir());
      beneficiaryBlb.setDateNaissance(beneficiaryId.getDateNaissance());
      beneficiaryBlb.setRangNaissance(beneficiaryId.getRangNaissance());
      beneficiaryBlb.setTrackingId(beneficiaryId.getTrackingId());

      final List<ContratAIV6> contrats =
          this.servicePrestationDao.findContratAIV6ForBlb(
              beneficiaryId.getDateNaissance(),
              beneficiaryId.getRangNaissance(),
              beneficiaryId.getNir());

      final List<GenericRightDto> orderContracts = new ArrayList<>();

      final UniqueAccessPointRequestV5 request =
          new UniqueAccessPointRequestV5(
              beneficiaryId.getNir(),
              beneficiaryId.getDateNaissance(),
              beneficiaryId.getRangNaissance(),
              "0001-01-01",
              null,
              null,
              null,
              null,
              ContextConstants.HTP,
              null,
              null,
              null,
              Constants.CLIENT_TYPE_INSURER,
              false,
              false);

      this.uniqueAccessPointTriHTP.triHTP(contrats, request);
      contrats.forEach(
          contratAIV6 -> {
            final ImmutablePair<String, String> dates;

            dates = this.calculatePeriod(contratAIV6, beneficiaryId, new ArrayList<>());
            if (dates.getLeft() == null) {
              return;
            }

            request.setStartDate(dates.getLeft());
            request.setEndDate(
                StringUtils.isNotEmpty(dates.getRight()) ? dates.getRight() : dates.getLeft());
            request.setContractNumber(contratAIV6.getNumero());

            orderContracts.addAll(
                this.uniqueAccessPointServiceV5HTP.mapContratToGenericRights(
                    contratAIV6,
                    request,
                    contratAIV6.getAssures().stream()
                        .map(assure -> assure.getIdentite().getNumeroPersonne())
                        .toList(),
                    false));
          });
      request.setContractNumber(null);

      if (!orderContracts.isEmpty()) {
        final List<ContratBLB> contratsBlb = new ArrayList<>();
        orderContracts.forEach(
            contrat -> {
              final String numContrat = contrat.getNumber();
              final ContratAIV6 contratAIV6 =
                  contrats.stream()
                      .filter(currContrat -> currContrat.getNumero().equals(numContrat))
                      .findFirst()
                      .get();
              try {
                this.buildBLBContracts(
                    contratAIV6,
                    contratsBlb,
                    this.calculatePeriod(contratAIV6, beneficiaryId, contratsBlb));
              } catch (CloneNotSupportedException e) {
                throw new ExtractContractException(e);
              }
            });
        beneficiaryBlb.setContrats(this.extractContracts(contratsBlb));
      }

      publishMessage(beneficiaryBlb);
    } catch (final RequestValidationException e) {
      logError(e, beneficiaryId);
      this.updateBlbTracking(beneficiaryId, e.getMessage(), e.getCustomErrorCode());
    } catch (
        final UAPFunctionalException
            e) { // NOSONAR : CustomErrorCode n'est pas dans RuntimeException
      logError(e, beneficiaryId);
      this.updateBlbTracking(beneficiaryId, e.getMessage(), e.getCustomErrorCode());
    } catch (final Exception e) {
      logError(e, beneficiaryId);
      this.updateBlbTracking(beneficiaryId, e.toString(), "TechnicalError");
    }
  }

  private void publishMessage(BeneficiaireBLB beneficiaryBlb) {
    try {
      this.blbProducer.publishMessage(
          MessageType.BLB_BENEFICIARY_FEEDING, beneficiaryBlb, beneficiaryBlb.getKey());
    } catch (final Exception e) {
      throw new ExtractContractException(e);
    }
  }

  ImmutablePair<String, String> calculatePeriod(
      final ContratAIV6 contrat,
      final BeneficiaireId beneficiaire,
      final List<ContratBLB> contratsBlb)
      throws AssureNotFoundException, ExtractContractException {
    final TreeSet<LocalDate> debutDates = new TreeSet<>();
    final TreeSet<LocalDate> finDates = new TreeSet<>();

    // Dates de début et fin de contrat
    final LocalDate debutDateMin = this.parseDate(contrat.getDateSouscription());
    final LocalDate finDateMax =
        contrat.getDateResiliation() != null ? this.parseDate(contrat.getDateResiliation()) : null;

    final var nir = beneficiaire.getNir();
    final var dateNaissance = beneficiaire.getDateNaissance();
    final var rang = beneficiaire.getRangNaissance();

    final Optional<Assure> optionalAssure =
        contrat.getAssures().stream()
            .filter(
                currAssure -> {
                  final var identite = currAssure.getIdentite();
                  return dateNaissance.equals(identite.getDateNaissance()) // dateNaissance
                      && rang.equals(identite.getRangNaissance()) // rangNaissance
                      && (nir.equals(identite.nirCode())
                          || this.matchAffiliation(identite, nir)); // nir
                })
            .findFirst();
    if (optionalAssure.isEmpty()) {
      final String message = String.format("Aucun assuré dans le contrat %s", contrat.getNumero());
      throw new AssureNotFoundException(message);
    }

    final Assure assure = optionalAssure.get();
    NirRattachementRO affiliationRO = null;
    if (assure.getIdentite().getAffiliationsRO() != null) {
      affiliationRO =
          assure.getIdentite().getAffiliationsRO().stream()
              .filter(affiliation -> matchAffiliation(affiliation, nir))
              .findFirst()
              .orElse(null);
    }
    if (affiliationRO != null
        && !nir.equals(affiliationRO.nirCode())
        && finDateMax != null
        && !this.parseDate(affiliationRO.getPeriode().getDebut()).isBefore(finDateMax)) {
      return new ImmutablePair<>(null, null);
    }

    // Dates de rattachement
    concatenatePeriodes(
        assure.getPeriodes(), debutDates, finDates, contrat, contratsBlb, debutDateMin, finDateMax);

    // Dates des droits
    concatenatePeriodes(
        assure.getDroits().stream().map(DroitAssureCommun::getPeriode).toList(),
        debutDates,
        finDates,
        contrat,
        contratsBlb,
        debutDateMin,
        finDateMax);

    // Dates d'affiliation RO
    if (affiliationRO != null && !nir.equals(affiliationRO.nirCode()))
      concatenatePeriodes(
          assure.getIdentite().getAffiliationsRO().stream()
              .filter(aff -> matchAffiliation(aff, nir))
              .map(NirRattachementRO::getPeriode)
              .toList(),
          debutDates,
          finDates,
          contrat,
          contratsBlb,
          debutDateMin,
          finDateMax);

    // Date d'adhésion et radiation
    this.addDate(debutDates, assure.getDateAdhesionMutuelle());
    this.addDate(finDates, assure.getDateRadiation());

    // Recherche de la plus petite période
    final String finalDebutDate = getFinalDebutDate(debutDates, finDateMax, debutDateMin);
    final String finalFinDate = getFinalFinDate(finDates, finDateMax, debutDateMin);

    return new ImmutablePair<>(finalDebutDate, finalFinDate);
  }

  private boolean matchAffiliation(final NirRattachementRO affiliation, final String nir) {
    return nir.equals(affiliation.nirCode());
  }

  private boolean matchAffiliation(final IdentiteContrat identite, final String nir) {
    if (CollectionUtils.isEmpty(identite.getAffiliationsRO())) return false; // early exit
    return identite.getAffiliationsRO().stream().anyMatch(aff -> matchAffiliation(aff, nir));
  }

  private String getFinalFinDate(
      TreeSet<LocalDate> finDates, LocalDate finDateMax, LocalDate debutDateMin) {
    final var finDateFilter =
        finDates.stream()
            .filter(
                date ->
                    (finDateMax == null || date.isBefore(finDateMax)) && date.isAfter(debutDateMin))
            .findFirst()
            .orElse(finDateMax);

    return finDateFilter != null ? DateUtils.formatDate(finDateFilter) : null;
  }

  private String getFinalDebutDate(
      TreeSet<LocalDate> debutDates, LocalDate finDateMax, LocalDate debutDateMin) {
    final var debutDatesFilter =
        debutDates.descendingSet().stream()
            .filter(
                date ->
                    date.isAfter(debutDateMin) && (finDateMax == null || date.isBefore(finDateMax)))
            .findFirst()
            .orElse(debutDateMin);
    return DateUtils.formatDate(debutDatesFilter);
  }

  private List<ContratBLB> extractContracts(final List<ContratBLB> contracts) {
    if (CollectionUtils.isEmpty(contracts)) return Collections.emptyList();
    return contracts.stream()
        .distinct()
        .collect(Collectors.groupingBy(this::periodGroup))
        .values()
        .stream()
        .map(List::getFirst)
        .toList();
  }

  private Pair<String, String> periodGroup(final ContratBLB contract) {
    return new ImmutablePair<>(contract.getDebut(), contract.getFin());
  }

  private void addDate(final Set<LocalDate> datesSet, final String date) {

    if (StringUtils.isNotBlank(date)) {
      datesSet.add(this.parseDate(date));
    }
  }

  void buildBLBContracts(
      final ContratAIV6 contractToCompare,
      List<ContratBLB> contratsBLB,
      ImmutablePair<String, String> dates)
      throws CloneNotSupportedException {
    final List<ContratBLB> contractsToAdd = new ArrayList<>();
    final ContratBLB contractToAdd = new ContratBLB(contractToCompare);
    contractToAdd.setDebut(dates.getLeft());
    contractToAdd.setFin(dates.getRight());
    if (StringUtils.isNotBlank(contractToAdd.getFin())
        && this.parseDate(contractToAdd.getFin())
            .isBefore(this.parseDate(contractToAdd.getDebut()))) {
      return;
    }

    if (contratsBLB.isEmpty()) {
      this.addContractBLB(dates.getLeft(), dates.getRight(), contractToAdd, contractsToAdd);
    } else {
      contractsToAdd.add(contractToAdd);
      contratsBLB.forEach(contratBLB -> this.processContrats(contratBLB, contractsToAdd));
    }
    contratsBLB.addAll(contractsToAdd);
  }

  private void processContrats(final ContratBLB contratBLB, final List<ContratBLB> contractsToAdd) {
    final List<ContratBLB> newContractsToAdd = new ArrayList<>();
    contractsToAdd.forEach(
        contractToAdd -> {
          try {
            final LocalDate startDateContractToAdd = this.parseDate(contractToAdd.getDebut());
            final LocalDate endDateContractToAdd =
                StringUtils.isNotBlank(contractToAdd.getFin())
                    ? this.parseDate(contractToAdd.getFin())
                    : null;
            final boolean endDateContractToAddExists = endDateContractToAdd != null;

            final LocalDate startDateBLBContract = this.parseDate((contratBLB.getDebut()));
            final LocalDate endDateBLBContract =
                StringUtils.isNotBlank(contratBLB.getFin())
                    ? this.parseDate((contratBLB.getFin()))
                    : null;
            final boolean endDateBLBContractExists = endDateBLBContract != null;

            // Le contrat courant commence après le contrat prioritaire
            if (startDateContractToAdd.isAfter(startDateBLBContract)) {
              this.handleCurrentContractAfterPriorityContract(
                  contractToAdd,
                  contractToAdd.getDebut(),
                  contractToAdd.getFin(),
                  contratBLB,
                  newContractsToAdd,
                  startDateContractToAdd,
                  endDateContractToAdd,
                  endDateContractToAddExists,
                  endDateBLBContract,
                  endDateBLBContractExists);
            }

            // Le contrat courant commence avant le contrat prioritaire
            else if (startDateContractToAdd.isBefore(startDateBLBContract)) {
              this.handleCurrentContractisBeforePriorityContract(
                  contractToAdd,
                  contractToAdd.getDebut(),
                  contractToAdd.getFin(),
                  contratBLB,
                  newContractsToAdd,
                  endDateContractToAdd,
                  endDateContractToAddExists,
                  startDateBLBContract,
                  endDateBLBContract,
                  endDateBLBContractExists);
            }

            // Le contrat courant commence le même jour que le contrat prioritaire
            else if ((endDateBLBContractExists
                    && endDateContractToAddExists
                    && endDateBLBContract.isBefore(endDateContractToAdd))
                || (endDateBLBContractExists && !endDateContractToAddExists)) {
              this.addContractBLB(
                  this.addOneDay(contratBLB.getFin()),
                  contractToAdd.getFin(),
                  contractToAdd,
                  newContractsToAdd);
            }
          } catch (final CloneNotSupportedException e) {
            throw new ExtractContractException(e);
          }
        });
    contractsToAdd.clear();
    contractsToAdd.addAll(newContractsToAdd);
  }

  private void handleCurrentContractisBeforePriorityContract(
      final ContratBLB contractToAdd,
      final String newStartDate,
      final String newEndDate,
      final ContratBLB contratBLB,
      final List<ContratBLB> contractsToAdd,
      final LocalDate endDateContractToAdd,
      final boolean endDateContractToAddExists,
      final LocalDate startDateBLBContract,
      final LocalDate endDateBLBContract,
      final boolean endDateBLBContractExists)
      throws CloneNotSupportedException {
    if ((endDateContractToAddExists
                && endDateBLBContractExists
                && (endDateBLBContract.isAfter(endDateContractToAdd)
                    || endDateBLBContract.equals(endDateContractToAdd))
            || !endDateBLBContractExists && endDateContractToAddExists)
        && !endDateContractToAdd.isBefore(startDateBLBContract)) {
      this.addContractBLB(
          newStartDate, this.removeOneDay(contratBLB.getDebut()), contractToAdd, contractsToAdd);
    } else if (endDateContractToAddExists
            && endDateBLBContractExists
            && endDateBLBContract.isBefore(endDateContractToAdd)
        || endDateBLBContractExists && !endDateContractToAddExists) {
      this.duplicateContractBLB(
          newStartDate,
          this.removeOneDay(contratBLB.getDebut()),
          this.addOneDay(contratBLB.getFin()),
          newEndDate,
          contractToAdd,
          contractsToAdd);
    } else if (!endDateContractToAddExists) {
      this.addContractBLB(
          newStartDate, this.removeOneDay(contratBLB.getDebut()), contractToAdd, contractsToAdd);
    } else {
      this.addContractBLB(newStartDate, newEndDate, contractToAdd, contractsToAdd);
    }
  }

  private void handleCurrentContractAfterPriorityContract(
      final ContratBLB contractToAdd,
      final String newStartDate,
      final String newEndDate,
      final ContratBLB contratBLB,
      final List<ContratBLB> contractsToAdd,
      final LocalDate startDateContractToAdd,
      final LocalDate endDateContractToAdd,
      final boolean endDateContractToAddExists,
      final LocalDate endDateBLBContract,
      final boolean endDateBLBContractExists)
      throws CloneNotSupportedException {
    if (endDateBLBContractExists && startDateContractToAdd.isAfter(endDateBLBContract)) {
      this.addContractBLB(newStartDate, newEndDate, contractToAdd, contractsToAdd);
    } else if ((endDateBLBContractExists
            && endDateContractToAddExists
            && endDateContractToAdd.isBefore(endDateBLBContract))
        || (!endDateBLBContractExists && endDateContractToAddExists)) {
      // ne pas ajouter le contrat courant
    } else if ((endDateBLBContractExists
            && endDateContractToAddExists
            && endDateContractToAdd.isAfter(endDateBLBContract))
        || (!endDateContractToAddExists && endDateBLBContractExists)) {
      this.addContractBLB(
          this.addOneDay(contratBLB.getFin()), newEndDate, contractToAdd, contractsToAdd);
    }
  }

  private void addContractBLB(
      final String debut,
      final String fin,
      final ContratBLB contractToAdd,
      final List<ContratBLB> contractstoAdd)
      throws CloneNotSupportedException {
    final ContratBLB contractToAddCopy = (ContratBLB) contractToAdd.clone();
    contractToAddCopy.setDebut(debut);
    contractToAddCopy.setFin(fin);
    contractstoAdd.add(contractToAddCopy);
  }

  private void duplicateContractBLB(
      final String debut1,
      final String fin1,
      final String debut2,
      final String fin2,
      final ContratBLB contractToAdd,
      final List<ContratBLB> contratsBLB)
      throws CloneNotSupportedException {
    this.addContractBLB(debut1, fin1, contractToAdd, contratsBLB);
    this.addContractBLB(debut2, fin2, contractToAdd, contratsBLB);
  }

  private LocalDate parseDate(final String dateString) {
    try {
      return DateUtils.stringToDate(dateString);
    } catch (final DateTimeParseException e) {
      throw new ExtractContractException(e);
    }
  }

  private String removeOneDay(final String date) {
    return DateUtils.formatDate(LocalDate.parse(date, DateUtils.FORMATTER).minusDays(1));
  }

  private String addOneDay(final String date) {
    return DateUtils.formatDate(LocalDate.parse(date, DateUtils.FORMATTER).plusDays(1));
  }

  private void updateBlbTracking(
      final BeneficiaireId beneficiaireId, final String message, final String errorCode) {
    final BddsToBlbTracking blbTracking =
        BddsToBlbTracking.builder2()
            .idHex(beneficiaireId.getTrackingId())
            .nir(beneficiaireId.getNir())
            .dateNaissance(beneficiaireId.getDateNaissance())
            .rangNaissance(beneficiaireId.getRangNaissance())
            .errorCode(errorCode)
            .errorLabel(message)
            .status(BddsToBlbStatus.NO_SEND)
            .build();
    this.blbTrackingDao.save(blbTracking);
    this.eventService.sendObservabilityEventBeneficiaryNoSend(blbTracking);
  }

  private void logError(final Exception e, final BeneficiaireId benef) {
    log.error(
        "Error consuming benef {} with trackingId {}", benef.getKey(), benef.getTrackingId(), e);
  }

  public void concatenatePeriodes(
      List<Periode> periodes,
      Set<LocalDate> debutDates,
      Set<LocalDate> finDates,
      ContratAIV6 contrat,
      List<ContratBLB> contrats,
      LocalDate minDate,
      LocalDate maxDate) {
    if (periodes.isEmpty()) {
      return;
    }

    List<Periode> periodesTrie = new ArrayList<>(periodes);
    periodesTrie.sort(Comparator.comparing(Periode::getDebut));

    // On fusionne les périodes qui se chevauchent
    List<Periode> result = this.mergeOverlapsPeriods(periodesTrie);

    result.stream()
        .skip(1)
        .forEach(
            period -> {
              TreeSet<LocalDate> newDebutDates = new TreeSet<>();
              newDebutDates.add(this.parseDate(period.getDebut()));
              TreeSet<LocalDate> newFinDates = new TreeSet<>();
              if (period.getFin() != null) {
                newFinDates.add(this.parseDate(period.getFin()));
              }
              try {
                this.buildBLBContracts(
                    contrat,
                    contrats,
                    new ImmutablePair<>(
                        this.getFinalDebutDate(newDebutDates, maxDate, minDate),
                        this.getFinalFinDate(newFinDates, maxDate, minDate)));
              } catch (CloneNotSupportedException e) {
                throw new ExtractContractException(e);
              }
            });
    this.addDate(debutDates, result.getFirst().getDebut());
    this.addDate(finDates, result.getFirst().getFin());
  }

  /**
   * Voir <a href=
   * "https://cegedim-insurance.atlassian.net/browse/GREEN-8461?focusedCommentId=139758">GREEN-8461</a>
   */
  private List<Periode> mergeOverlapsPeriods(List<Periode> sorted) {
    List<Periode> merged = new ArrayList<>();

    for (int i = 0; i < sorted.size(); i++) {
      Periode current = sorted.get(i);
      Periode next = this.next(sorted, i);

      // Tant que les périodes 'current' et 'next' se chevauchent
      while (next != null && current.overlaps(this.removeOneDay(next.getDebut()), next.getFin())) {
        current = this.mergePeriods(current, next); // on prend (p.debut min) et (p.fin max)
        next = this.next(sorted, ++i); // next++ (et on skip le prochain current grâce au ++i)
      }

      merged.add(current);
    }
    return merged;
  }

  private Periode next(List<Periode> periods, int i) {
    return (i + 1 < periods.size()) ? periods.get(i + 1) : null;
  }

  /** Retourne une nouvelle Periode avec (p.debut min) et (p.fin max) */
  private Periode mergePeriods(Periode p1, Periode p2) {
    var debut = ObjectUtils.min(p1.getDebut(), p2.getDebut());
    var fin =
        ObjectUtils.anyNull(p1.getFin(), p2.getFin())
            ? null
            : ObjectUtils.max(p1.getFin(), p2.getFin());
    return new Periode(debut, fin);
  }
}
