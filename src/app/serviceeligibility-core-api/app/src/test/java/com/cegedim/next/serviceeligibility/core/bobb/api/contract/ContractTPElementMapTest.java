package com.cegedim.next.serviceeligibility.core.bobb.api.contract;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobb.ProductElement;
import com.cegedim.next.serviceeligibility.core.bobb.dao.ContractElementRepository;
import com.cegedim.next.serviceeligibility.core.bobb.services.ContractElementService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

class ContractTPElementMapTest {

  @InjectMocks private ContractElementService service;

  @Mock private ContractElementRepository repository;

  private final ContractElement existingContractElement = new ContractElement();

  private final ProductElement newProductElement = new ProductElement();

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);

    // New product element to map
    newProductElement.setCodeOffer("OFFER");
    newProductElement.setCodeProduct("PRODUCT");
    newProductElement.setCodeBenefitNature("NATURE");
    newProductElement.setEffectiveDate(LocalDateTime.of(2020, 2, 2, 0, 0, 0));
    newProductElement.setFrom(LocalDateTime.of(2020, 1, 1, 0, 0, 0));
    newProductElement.setTo(LocalDateTime.of(2020, 12, 31, 0, 0, 0));

    // expected alert id set on creation
    existingContractElement.setAlertId("ALERT_ID");
    Optional<ContractElement> existing = Optional.of(existingContractElement);
    Mockito.when(repository.findOneById("ID")).thenReturn(existing);
  }

  @Test
  void testGetContractElements() {
    Collection<ContractElement> elements = new ArrayList<>();
    Mockito.when(repository.get("CODE_AMC")).thenReturn(elements);

    Collection<ContractElement> result = service.get("CODE_AMC");

    Assertions.assertEquals(elements, result);
  }

  @Test
  void testMapExistingElementAndMapping() {
    // Existing mapping to same product element
    ProductElement mappedProductElement = new ProductElement();
    mappedProductElement.setCodeOffer("OFFER");
    mappedProductElement.setCodeProduct("PRODUCT");
    mappedProductElement.setCodeBenefitNature("NATURE");
    mappedProductElement.setFrom(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
    mappedProductElement.setTo(LocalDateTime.of(2019, 12, 31, 0, 0, 0));
    mappedProductElement.setEffectiveDate(LocalDateTime.of(2019, 2, 1, 0, 0, 0));
    existingContractElement.getProductElements().add(mappedProductElement);

    service.map("ID", newProductElement);

    Mockito.verify(repository).save(existingContractElement);
    Assertions.assertFalse(existingContractElement.getProductElements().isEmpty());
    // Previous mapping is updated
    Assertions.assertEquals(LocalDateTime.of(2020, 1, 1, 0, 0, 0), mappedProductElement.getFrom());
    Assertions.assertEquals(LocalDateTime.of(2020, 12, 31, 0, 0, 0), mappedProductElement.getTo());
    Assertions.assertEquals(
        LocalDateTime.of(2020, 2, 2, 0, 0, 0), mappedProductElement.getEffectiveDate());
  }

  @Test
  void testMapExistingElementOtherProduct() {
    // Existing mapping to other product element
    ProductElement mappedProductElement = new ProductElement();
    mappedProductElement.setCodeOffer("OFFER");
    mappedProductElement.setCodeProduct("PRODUCT_2");
    mappedProductElement.setCodeBenefitNature("NATURE");
    mappedProductElement.setEffectiveDate(LocalDateTime.of(2019, 1, 1, 0, 0, 0));
    existingContractElement.getProductElements().add(mappedProductElement);

    service.map("ID", newProductElement);

    Mockito.verify(repository).save(existingContractElement);
    // date TO not updated
    Assertions.assertNull(mappedProductElement.getTo());
    // but new mapping added
    Assertions.assertEquals(2, existingContractElement.getProductElements().size());
  }

  @Test
  void testMapUnknownElement() {
    Mockito.when(repository.findOneById("ID")).thenThrow(IllegalArgumentException.class);

    Assertions.assertThrows(
        IllegalArgumentException.class, () -> service.map("ID", newProductElement));

    // Expect InvalidParameterException
  }
}
