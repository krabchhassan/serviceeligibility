package com.cegedim.next.serviceeligibility.batch635.job.domain.repository;

import static org.mockito.Mockito.*;

import com.cegedim.next.serviceeligibility.batch635.job.domain.repository.projection.PeriodeDroitProjection;
import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

@ExtendWith(MockitoExtension.class)
class CustomContratsRepositoryTest {
  @Mock MongoTemplate mongoTemplate;

  CustomContratsRepositoryImpl customContratsRepository;

  @BeforeEach
  public void setUp() {
    customContratsRepository = new CustomContratsRepositoryImpl(2000, mongoTemplate);
  }

  @Test
  void shouldCheckIfAmcExists_whenCallToServiceIsOk() {
    String amc = "5239893746";
    String referenceDate = "20211212";
    AggregationResults expectedRes = getExpectedResult();

    when(mongoTemplate.aggregate(any(Aggregation.class), anyString(), any()))
        .thenReturn(expectedRes);

    List<PeriodeDroitProjection> res =
        customContratsRepository.extractPeriodesDroit(amc, referenceDate, 0);

    Assertions.assertEquals(1, res.size());
    verify(mongoTemplate, times(1)).aggregate(any(Aggregation.class), anyString(), any());
  }

  private AggregationResults<PeriodeDroitProjection> getExpectedResult() {
    PeriodeDroitProjection periodeDroitProjection = new PeriodeDroitProjection();
    List<PeriodeDroitProjection> mappedResults = List.of(periodeDroitProjection);
    Document rawResults = new Document();
    return new AggregationResults<>(mappedResults, rawResults);
  }
}
