package com.cegedim.beyond.blb.recalcul.service;

import static com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus.FAILED;
import static com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus.NO_RESPONSE;
import static com.cegedim.next.serviceeligibility.core.model.enumeration.BddsToBlbStatus.SUCCESS;

import com.cegedim.beyond.blb.recalcul.kafka.model.BeneficiaryFeedingDto;
import com.cegedim.beyond.blb.recalcul.kafka.model.BlbLogicalDeletionResponseDto;
import com.cegedim.beyond.blb.recalcul.kafka.publisher.LogicalDeletionMessagePublisher;
import com.cegedim.beyond.blb.recalcul.util.CrexUtils;
import com.cegedim.beyond.messaging.api.handler.MessageConsumersHandler;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbServicePrestationRepo;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbTrackingRepo;
import com.cegedim.next.serviceeligibility.core.kafka.common.KafkaSendingException;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.model.crex.JobRecalculBlbEndEventDto;
import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbServicePrestation;
import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbServicePrestation.Assure.Identite;
import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbTracking;
import com.cegedim.next.serviceeligibility.core.model.kafka.benef.BeneficiaireId;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import io.micrometer.tracing.annotation.ContinueSpan;
import io.micrometer.tracing.annotation.NewSpan;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlbRecalculService {

  private static final String SUCCESS_MSG = "OK";

  private final BddsToBlbTrackingRepo blbTrackingDao;
  private final BddsToBlbServicePrestationRepo servicePrestationDao;
  private final EventService eventPublisherService;
  private final ExtractContractProducer extractContractProducer;
  private final LogicalDeletionMessagePublisher logicalDeletionMessagePublisher;
  private final MessageConsumersHandler messageConsumersHandler;

  @Value("${spring.kafka.consumer.group-id}")
  private String groupId;

  @Value("${logical-deletion-timeout-minutes:10}")
  private Long deletionTimeout;

  private CountDownLatch deletionBarrier;
  private AtomicBoolean logicalDeletionError;

  @Value("${feeding-timeout-minutes:10}")
  private Long feedingTimeout;

  private CountDownLatch feedingBarrier;

  private BlbEndEventFactory endEventFactory;

  // --------------------
  // UTILS
  // --------------------
  private void beforeDeletion() {
    this.deletionBarrier = new CountDownLatch(1);
    this.logicalDeletionError = new AtomicBoolean(false);
  }

  /** Return true receive response before timeout */
  private boolean waitingDeletion(final Long timeout) throws InterruptedException {
    log.info("=======> Waiting message logical deletion from BLB");
    return this.deletionBarrier.await(timeout, TimeUnit.MINUTES);
  }

  private void beforeFeeding() {
    this.feedingBarrier = new CountDownLatch(1);
    if (this.endEventFactory == null)
      this.endEventFactory = BlbEndEventFactory.init(this.blbTrackingDao);
  }

  /**
   * Return false if we don't receive any response after timeout. <br>
   * Else wait until still pending and receive message
   */
  private boolean waitingBlbFeeding(final Long timeout) throws InterruptedException {
    var noResponseCount = this.blbTrackingDao.countByStatus(NO_RESPONSE).intValue();
    while (noResponseCount > 0) {
      log.info("=======> Waiting for blb eventing messages : {} pending messages", noResponseCount);
      // sleep until process msg
      final boolean allMsgProcessed = this.feedingBarrier.await(timeout, TimeUnit.MINUTES);
      if (allMsgProcessed) return true; // early exit

      final var currentNoResponseCount = this.blbTrackingDao.countByStatus(NO_RESPONSE).intValue();
      if (currentNoResponseCount == noResponseCount) return false; // exit after timeout

      noResponseCount = currentNoResponseCount;
    }

    return true; // false if stillPending (noResponseCount > 0)
  }

  // --------------------
  // METHODS
  // --------------------
  @NewSpan
  public int processStart() throws InterruptedException, KafkaSendingException {
    log.info("=======> Wait for {}-beneficiary", groupId);
    while (!messageConsumersHandler.isStarted("%s-beneficiary".formatted(groupId))) {
      log.info("Waiting 1 second for {}-beneficiary...", groupId);
      Thread.sleep(1000);
    }
    log.info("=======> Wait for {}-deletion", groupId);
    while (!messageConsumersHandler.isStarted("%s-deletion".formatted(groupId))) {
      log.info("Waiting 1 second for {}-deletion...", groupId);
      Thread.sleep(1000);
    }

    log.info("=======> Start BLB recalcul database");
    this.eventPublisherService.sendObservabilityEventStartRecalculBlb();
    this.blbTrackingDao.deleteAll();
    this.endEventFactory = BlbEndEventFactory.init(this.blbTrackingDao);

    // LOGICAL DELETION (SEND & WAIT RESPONSE)
    log.info("=======> Send message logical deletion to BLB");
    this.beforeDeletion();
    this.logicalDeletionMessagePublisher.publishDeletion();
    final boolean hasDeletionResponse = this.waitingDeletion(this.deletionTimeout);
    if (!hasDeletionResponse) {
      log.error("=======> Waiting message logical deletion from BLB timeout");
      this.endEventFactory.setDeletionBeforeReinit("Pas de réponse");
      return this.exit(this.endEventFactory);
    }
    if (this.logicalDeletionError.get()) {
      log.error("=======> Waiting message logical deletion from BLB in error");
      return this.exit(this.endEventFactory);
    }
    log.info("=======> Receive message logical deletion from BLB");

    // EXTRACT CONTRACT / BLB FEEDING (SEND & WAIT RESPONSE)
    log.info("=======> Start functional Core method");
    this.functionalCoreJob();
    log.info("=======> Start waiting eventing BLB");
    final boolean hasFeedingResponse = this.waitingBlbFeeding(this.feedingTimeout);
    if (!hasFeedingResponse) log.error("=======> Waiting eventing BLB timeout");
    return this.exit(this.endEventFactory);
  }

  @ContinueSpan(log = "functionalCoreJob")
  public void functionalCoreJob() {
    this.beforeFeeding();
    var paging = Pageable.ofSize(1000);
    do {
      // Retrieve ServicePrestation
      final Page<BddsToBlbServicePrestation> page =
          this.servicePrestationDao.findAllContrat(paging);
      // Extract Identity & Send
      page.stream()
          .map(BddsToBlbServicePrestation::getAssures)
          .flatMap(List::stream)
          .map(BddsToBlbServicePrestation.Assure::getIdentite)
          .forEach(this::identityTreatment);
      // page++
      paging = page.nextPageable();
    } while (paging.isPaged());
  }

  @ContinueSpan(log = "identityTreatment")
  private void identityTreatment(final Identite identite) {
    final var extracted = this.extractNir(identite);
    extracted.forEach(
        (nir, nirDateRang) ->
            // Send to kafka & save to tracking
            this.storeAndSend(
                nir, identite.getDateNaissance(), identite.getRangNaissance(), nirDateRang));
  }

  /**
   * Retourne la {@code Map<Nir, NirDateRang>} pour l'Identité et toutes ses AffiliationsRO
   *
   * <ul>
   *   <li>{@code Nir} : nir.getCode() de l'Identité ou de ses * AffiliationsRO
   *   <li>{@code NirDateRang} : nirCode+dateNaissance+rangNaissance
   * </ul>
   */
  public Map<String, String> extractNir(final Identite identite) {
    final Map<String, String> extracted = new HashMap<>();
    // principale
    if (identite.nirCode() != null) extracted.put(identite.nirCode(), identite.key());

    // affiliations
    if (!CollectionUtils.isEmpty(identite.getAffiliationsRO()))
      identite.getAffiliationsRO().stream()
          .filter(affRO -> Objects.nonNull(affRO.nirCode()))
          .forEach(affRO -> extracted.put(affRO.nirCode(), identite.key(affRO)));

    return extracted;
  }

  @ContinueSpan(log = "storeAndSend")
  private void storeAndSend(
      final String nir, final String dateNaissance, final String rang, final String key) {
    if (this.blbTrackingDao.exist(nir, dateNaissance, rang)) return; // early exit

    log.debug("=======> Send and store key {}", key);
    this.endEventFactory.addBeneficiary();
    final var tracking =
        this.blbTrackingDao.insert(
            BddsToBlbTracking.builder()
                .nir(nir)
                .dateNaissance(dateNaissance)
                .rangNaissance(rang)
                .date(LocalDateTime.now(ZoneOffset.UTC))
                .status(NO_RESPONSE)
                .build());

    final BeneficiaireId beneficiaireId =
        BeneficiaireId.builder()
            .nir(nir)
            .dateNaissance(dateNaissance)
            .rangNaissance(rang)
            .trackingId(tracking.idHex())
            .build();
    try {
      this.extractContractProducer.send(beneficiaireId, key);
    } catch (final KafkaSendingException e) {
      log.error("=======> Kafka error", e);
    }
  }

  @ContinueSpan(log = "processBeneficiaryFeedingResponse")
  public void processBeneficiaryFeedingResponse(final BeneficiaryFeedingDto feedingResponse) {
    log.debug("consume response TrackingId {}", feedingResponse.getTrackingId());
    final var document = this.blbTrackingDao.findById(feedingResponse.getTrackingId()).orElse(null);
    if (document == null) {
      log.warn("mismatch trackingId {} ", feedingResponse.getTrackingId());
      return; // early exit
    }
    if (document.getStatus() != NO_RESPONSE) {
      log.warn("ERROR : consume twice response TrackingId {}", feedingResponse.getTrackingId());
      return; // early exit
    }

    // Update tracking status
    document.setErrorCode(feedingResponse.getErrorCode());
    document.setErrorLabel(feedingResponse.getErrorLabel());
    final var status = !StringUtils.hasLength(feedingResponse.getErrorCode()) ? SUCCESS : FAILED;
    log.debug("Status tracking info {}", status);
    document.setStatus(status);
    this.blbTrackingDao.save(document);

    if (!this.blbTrackingDao.existsByStatus(NO_RESPONSE))
      this.feedingBarrier.countDown(); // all msg processed
  }

  @ContinueSpan(log = "processLogicalDeletionResponse")
  public void processLogicalDeletionResponse(final BlbLogicalDeletionResponseDto deletionResponse) {
    final String msg = deletionResponse.errorMsg(SUCCESS_MSG);
    if (!SUCCESS_MSG.equals(msg)) {
      log.info(
          "=======> Message de suppression logique en base BLB reçu total deleted : {}, error : {}",
          deletionResponse.getTotal(),
          msg);
      this.logicalDeletionError.set(true);
    }

    this.endEventFactory.setDeletionBeforeReinit(msg);
    this.deletionBarrier.countDown();
  }

  @ContinueSpan(log = "exit")
  private int exit(final BlbEndEventFactory endEventFactory) {
    log.info("=======> End BLB recalcul database");
    final var event = endEventFactory.build();
    this.eventPublisherService.sendObservabilityEventEndRecalculBlb(event);
    CrexUtils.updateOmuCrex(event);

    return this.exitCode(event);
  }

  private int exitCode(final JobRecalculBlbEndEventDto event) {
    final boolean deletionFail = !SUCCESS_MSG.equals(event.getDeletionBeforeReinit());
    final boolean stillPending = event.getNbTrackedBeneficiariesNoResponse() > 0;

    return (deletionFail || stillPending) ? -1 : 0;
  }
}
