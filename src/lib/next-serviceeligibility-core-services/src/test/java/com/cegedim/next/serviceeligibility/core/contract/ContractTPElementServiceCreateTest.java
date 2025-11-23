package com.cegedim.next.serviceeligibility.core.contract;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class ContractTPElementServiceCreateTest {

  @InjectMocks private ContractElementService service;

  @Mock private ContractElementRepository repository;

  private final ContractElement newElement = new ContractElement();

  private final ContractElement existingElement = new ContractElement();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    // New contract element to create
    newElement.setCodeAMC("CODE_AMC");
    newElement.setCodeContractElement("CODE_CONTRACT_ELEMENT");
    newElement.setCodeInsurer("CODE_INSURER");
    // By default, return an existing contract element for this key
    existingElement.setCodeAMC("CODE_AMC");
    existingElement.setCodeContractElement("CODE_CONTRACT_ELEMENT");
    existingElement.setCodeInsurer("CODE_INSURER");
    existingElement.setAlertId("ALERT_ID");

    Optional<ContractElement> existing = Optional.of(existingElement);
    Mockito.when(repository.findByKey("CODE_CONTRACT_ELEMENT", "CODE_INSURER"))
        .thenReturn(existing);
  }

  @Test
  void testInitializeData() {
    List<ContractElement> elements = new ArrayList<>();
    ContractElement element1 = new ContractElement();
    element1.setCodeContractElement("GT1");
    element1.setCodeInsurer("ASR");
    elements.add(element1);
    ContractElement element2 = new ContractElement();
    element2.setCodeContractElement("GT2");
    element2.setCodeInsurer("ASR");
    elements.add(element2);

    service.initializeData(elements);

    Mockito.verify(repository).removeAll();
    Mockito.verify(repository).save(element1);
    Mockito.verify(repository).save(element2);
  }

  @Test
  void testCreateNew() {
    // No existing element
    Optional<ContractElement> existing = Optional.empty();
    Mockito.when(repository.findByKey("CODE_CONTRACT_ELEMENT", "CODE_INSURER"))
        .thenReturn(existing);

    service.create(newElement);

    Mockito.verify(repository).save(newElement);
    // Newly generated contract element UUID
    Assertions.assertNotNull(newElement.getId());
  }

  @Test
  void testCreateExistingNoDeadline() {
    existingElement.setDeadline(null);

    service.create(newElement);

    // No database update
    Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
  }

  @Test
  void testCreateExistingShorterDeadline() {
    existingElement.setDeadline(LocalDate.of(2020, 2, 1));
    // Deadline is shorter than existing deadline
    newElement.setDeadline(LocalDate.of(2020, 1, 1));

    service.create(newElement);

    // Existing element is updated with this new deadline
    Mockito.verify(repository, Mockito.times(1)).save(existingElement);
    Assertions.assertEquals(LocalDate.of(2020, 1, 1), existingElement.getDeadline());
  }

  @Test
  void testCreateExistingLongerDeadline() {
    existingElement.setDeadline(LocalDate.of(2020, 2, 1));
    // Deadline is longer than existing deadline
    newElement.setDeadline(LocalDate.of(2020, 3, 1));

    service.create(newElement);

    // No database update
    Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
    Assertions.assertEquals(LocalDate.of(2020, 2, 1), existingElement.getDeadline());
  }

  @Test
  void testCreateAlreadyMappedActiveNoBound() {
    ProductElement productElement = new ProductElement();
    // no bound to this mapping
    productElement.setEffectiveDate(LocalDateTime.of(2000, 1, 1, 0, 0, 0));
    existingElement.getProductElements().add(productElement);
    // already mapped means previous alert have been resolved
    existingElement.setAlertId(null);

    service.create(newElement);

    // No database update
    Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
  }

  @Test
  void testCreateAlreadyMappedActiveBounded() {
    ProductElement productElement = new ProductElement();
    // bound from today to today and effective since today (shortest
    // possible bounds)
    productElement.setFrom(LocalDateTime.now(ZoneOffset.UTC));
    productElement.setTo(LocalDateTime.now(ZoneOffset.UTC));
    productElement.setEffectiveDate(LocalDateTime.now(ZoneOffset.UTC));
    existingElement.getProductElements().add(productElement);
    // already mapped means previous alert have been resolved
    existingElement.setAlertId(null);

    service.create(newElement);

    // No database update
    Mockito.verify(repository, Mockito.times(0)).save(Mockito.any());
  }

  @Test
  void testCreateAlreadyMappedInactiveEffectiveDate() {
    ProductElement productElement = new ProductElement();
    // no bound but effective from tomorrow
    productElement.setEffectiveDate(LocalDateTime.now(ZoneOffset.UTC).plusDays(1));
    existingElement.getProductElements().add(productElement);
    // already mapped means previous alert have been resolved
    existingElement.setAlertId(null);

    service.create(newElement);
  }

  @Test
  void testCreateAlreadyMappedInactiveFrom() {
    ProductElement productElement = new ProductElement();
    // from tomorrow
    productElement.setFrom(LocalDateTime.now(ZoneOffset.UTC).plusDays(1));
    productElement.setEffectiveDate(LocalDateTime.of(2018, 1, 1, 0, 0, 0));
    existingElement.getProductElements().add(productElement);
    // already mapped means previous alert have been resolved
    existingElement.setAlertId(null);
    service.create(newElement);
  }
}
