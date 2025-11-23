package com.cegedim.next.serviceeligibility.core.bobb.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb.TechnicalGuaranteeRequest;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.utils.exceptions.GuaranteeException;
import com.mongodb.client.result.UpdateResult;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class LotRepositoryTest {
  public static final String LOT_1 = "LOT1";
  public static final String LOT_2 = "LOT2";
  public static final String GUARANTEE_CODE = "GT123";
  public static final String INSURER_CODE = "ASSU0001";
  @Mock private MongoTemplate mongoTemplate;

  @InjectMocks private LotRepository lotRepository;

  private Lot getLot(String code) {
    Lot lot = new Lot();
    lot.setCode(code);
    lot.setLibelle("Libelle " + code);
    lot.setGarantieTechniques(Collections.emptyList());
    return lot;
  }

  @Test
  void should_return_empty_collection_when_empty_aggregation() {
    List<Lot> result = lotRepository.getLotsAlmerysValides(List.of());

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(mongoTemplate, never()).find(any(Query.class), eq(Lot.class));
  }

  @Test
  void should_return_single_lot() {
    Lot lot = getLot(LOT_1);
    when(mongoTemplate.find(any(Query.class), eq(Lot.class))).thenReturn(List.of(lot));

    List<Lot> result = lotRepository.getLotsAlmerysValides(List.of(LOT_1));

    assertEquals(1, result.size());
    assertEquals(LOT_1, result.get(0).getCode());
  }

  @Test
  void should_return_multiple_lot() {
    Lot lot1 = getLot(LOT_1);
    Lot lot2 = getLot(LOT_2);
    when(mongoTemplate.find(any(Query.class), eq(Lot.class))).thenReturn(List.of(lot1, lot2));

    List<Lot> result = lotRepository.getLotsAlmerysValides(List.of(LOT_1, LOT_2));

    assertEquals(2, result.size());
    assertTrue(result.stream().anyMatch(l -> l.getCode().equals(LOT_1)));
    assertTrue(result.stream().anyMatch(l -> l.getCode().equals(LOT_2)));
  }

  @Test
  void should_return_unique_result_when_duplicate_lot() {
    Lot lot1 = getLot(LOT_1);
    when(mongoTemplate.find(any(Query.class), eq(Lot.class))).thenReturn(List.of(lot1));

    List<Lot> result = lotRepository.getLotsAlmerysValides(List.of(LOT_1, LOT_2));

    assertEquals(1, result.size());
    assertEquals(LOT_1, result.get(0).getCode());
  }

  @Test
  void should_return_only_existing_lot_in_mongodb() {
    Lot lot1 = getLot(LOT_1);
    when(mongoTemplate.find(any(Query.class), eq(Lot.class))).thenReturn(List.of(lot1));

    List<Lot> result = lotRepository.getLotsAlmerysValides(List.of(LOT_1, LOT_2));

    assertEquals(1, result.size());
    assertEquals(LOT_1, result.get(0).getCode());
  }

  @Test
  void should_add_garantie_technique_when_not_associate_to_lot() {
    TechnicalGuaranteeRequest req = new TechnicalGuaranteeRequest();
    req.setGuaranteeCode(GUARANTEE_CODE);
    req.setInsurerCode(INSURER_CODE);
    UpdateResult mockResult = mock(UpdateResult.class);

    when(mockResult.getMatchedCount()).thenReturn(1L);
    when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Lot.class)))
        .thenReturn(mockResult);

    assertDoesNotThrow(() -> lotRepository.addGarantieTechnique(LOT_1, req));
    verify(mongoTemplate, times(1)).updateFirst(any(Query.class), any(Update.class), eq(Lot.class));
  }

  @Test
  void should_throw_exception_when_guarantee_already_associate_to_lot() {
    TechnicalGuaranteeRequest req = new TechnicalGuaranteeRequest();
    req.setGuaranteeCode(GUARANTEE_CODE);
    req.setInsurerCode(INSURER_CODE);
    UpdateResult mockResult = mock(UpdateResult.class);

    when(mockResult.getMatchedCount()).thenReturn(0L);
    when(mongoTemplate.updateFirst(any(Query.class), any(Update.class), eq(Lot.class)))
        .thenReturn(mockResult);

    GuaranteeException ex =
        assertThrows(
            GuaranteeException.class, () -> lotRepository.addGarantieTechnique(LOT_1, req));

    assertEquals("Cette garantie est déjà associée au lot LOT1", ex.getMessage());
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void should_update_date_suppression_logique() {
    String lotCode = "LOT001";
    TechnicalGuaranteeRequest request = new TechnicalGuaranteeRequest();
    request.setGuaranteeCode(GUARANTEE_CODE);
    request.setInsurerCode(INSURER_CODE);

    lotRepository.updateDateSuppressionLogiqueGT(lotCode, request);

    ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
    ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

    verify(mongoTemplate, times(1))
        .updateFirst(queryCaptor.capture(), updateCaptor.capture(), eq(Lot.class));

    Query capturedQuery = queryCaptor.getValue();
    Update capturedUpdate = updateCaptor.getValue();
    String updateJson = capturedUpdate.getUpdateObject().toJson();
    String queryJson = capturedQuery.getQueryObject().toJson();

    assertThat(queryJson).contains("LOT001");
    assertThat(queryJson).contains(INSURER_CODE);
    assertThat(queryJson).contains(GUARANTEE_CODE);
    assertThat(updateJson).contains("garantieTechniques.$.dateSuppressionLogique");
  }
}
