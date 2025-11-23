package com.cegedim.beyond.blb.recalcul.service;

import com.cegedim.beyond.blb.recalcul.kafka.publisher.LogicalDeletionMessagePublisher;
import com.cegedim.beyond.blb.recalcul.utils.Utils;
import com.cegedim.beyond.messaging.api.handler.MessageConsumersHandler;
import com.cegedim.next.serviceeligibility.core.config.mongo.BddsMongoConfig;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbServicePrestationRepo;
import com.cegedim.next.serviceeligibility.core.dao.BddsToBlbTrackingRepo;
import com.cegedim.next.serviceeligibility.core.kafka.serviceprestation.ExtractContractProducer;
import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbServicePrestation;
import com.cegedim.next.serviceeligibility.core.model.entity.BddsToBlbTracking;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DataMongoTest
@ExtendWith(SpringExtension.class)
@SpringBootConfiguration
@EnableAutoConfiguration()
@Import({BddsMongoConfig.class})
@ActiveProfiles("test")
class RecalculBlbTest {

  @Autowired private MongoTemplate mongoTemplate;
  @Autowired private BddsToBlbTrackingRepo blbTrackingDao;

  @Autowired private BddsToBlbServicePrestationRepo servicePrestationDao;

  @Mock private EventService eventPublisherService;

  @Mock private ExtractContractProducer extractContractProducer;

  @Mock private LogicalDeletionMessagePublisher logicalDeletionProducer;

  private List<BddsToBlbServicePrestation> servicePrestations;

  private List<BddsToBlbTracking> trackingDocuments;

  private BlbRecalculService blbRecalculService;

  // --------------------
  // INIT / CLEAN
  // --------------------
  @BeforeEach
  void setup() throws IOException {
    // instantiate service
    this.blbRecalculService =
        new BlbRecalculService(
            this.blbTrackingDao,
            this.servicePrestationDao,
            this.eventPublisherService,
            this.extractContractProducer,
            this.logicalDeletionProducer,
            new MessageConsumersHandler() {
              @Override
              public void start(String container) {}

              @Override
              public void stop(String container) {}

              @Override
              public void startAll() {}

              @Override
              public void stopAll() {}

              @Override
              public boolean isStarted(String consumerGroupId) {
                return true;
              }
            });
    // insert data
    Utils.setupEmbeddedMongo(this.mongoTemplate);
    // run & get results
    this.blbRecalculService.functionalCoreJob();
    this.servicePrestations = servicePrestationDao.findAll();
    this.trackingDocuments = blbTrackingDao.findAll();
  }

  @AfterEach
  void afterEach() {
    // clean data
    servicePrestationDao.deleteAll();
    blbTrackingDao.deleteAll();
  }

  // --------------------
  // TESTS
  // --------------------
  // TODO @Test
  @DisplayName(
      "Test case : seven servicePrestation documents must produce 15 triplet send and tracked")
  void testGoodNumberOfTriplet() {
    Assertions.assertEquals(7, this.servicePrestations.size());
    final Set<String> tripletSet =
        this.servicePrestations.stream()
            .map(BddsToBlbServicePrestation::getAssures)
            .flatMap(List::stream)
            .map(BddsToBlbServicePrestation.Assure::getIdentite)
            .flatMap(identite -> blbRecalculService.extractNir(identite).values().stream())
            .collect(Collectors.toSet());
    Assertions.assertEquals(tripletSet.size(), this.trackingDocuments.size());
    Assertions.assertTrue(this.trackingDocuments.stream().allMatch(Utils::checkEntity));
  }

  // TODO @Test
  @DisplayName("Test case : no duplicate triplet send and tracked")
  void testNoDuplicateTripletTracked() {
    var allKey = trackingDocuments.stream().map(BddsToBlbTracking::key).toList();
    var allUniqueKey = new HashSet<>(allKey);

    Assertions.assertEquals(allUniqueKey.size(), allKey.size());
  }
}
