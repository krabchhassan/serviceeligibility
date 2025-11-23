package com.cegedim.next.serviceeligibility.core.features.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.almerysProductRef.AlmerysProductRequest;
import com.cegedim.next.serviceeligibility.core.almerysProductRef.AlmerysProductResponse;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef.AlmerysProductDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef.ProductCombinationDto;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ParametrageAlmerysResponseDto;
import com.cegedim.next.serviceeligibility.core.services.almerysProductRef.AlmerysProductReferentialService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.utils.AuthenticationFacade;
import io.micrometer.tracing.annotation.NewSpan;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/v1/almerysProductReferential")
@RequiredArgsConstructor
@Slf4j
public class AlmerysProductReferentialController {

  private final AlmerysProductReferentialService service;
  private final EventService eventService;
  private final AlmerysProductMapper mapper;

  @Qualifier("bddAuth")
  private final AuthenticationFacade authenticationFacade;

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<List<AlmerysProductDto>> getAlmerysProductReferential() {
    List<AlmerysProductDto> almerysProductDtoList = new ArrayList<>();
    Collection<AlmerysProduct> almerysProductReferential = service.getAlmerysProductReferential();
    for (AlmerysProduct product : almerysProductReferential) {
      almerysProductDtoList.add(mapper.productToProductDto(product));
    }
    return new ResponseEntity<>(almerysProductDtoList, HttpStatus.OK);
  }

  @GetMapping(value = "/getAlmerysProduct")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<AlmerysProductDtoResponse> getAlmerysProduct(
      AlmerysProductRequest request) {
    AlmerysProductDtoResponse almerysProductDtoResponse = new AlmerysProductDtoResponse();
    AlmerysProductResponse almerysProductResponse = service.getAlmerysProduct(request);
    almerysProductDtoResponse.setPaging(almerysProductResponse.getPaging());
    List<AlmerysProduct> almerysProductList = almerysProductResponse.getAlmerysProductList();
    if (CollectionUtils.isNotEmpty(almerysProductList)) {
      List<AlmerysProductDto> almerysProductDtoList = new ArrayList<>();
      for (AlmerysProduct product : almerysProductList) {
        almerysProductDtoList.add(mapper.productToProductDto(product));
      }
      almerysProductDtoResponse.setAlmerysProductList(almerysProductDtoList);
    }
    return new ResponseEntity<>(almerysProductDtoResponse, HttpStatus.OK);
  }

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  @NewSpan
  public ResponseEntity<Void> createAlmerysProduct(
      @Valid @RequestBody AlmerysProductDto almerysProductDto) {
    try {
      if (almerysProductDto.getCode() == null) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST, "A combination should have a code");
      }
      checkIfProductAlreadyExists(almerysProductDto);
      checkProductCombinations(almerysProductDto);
      AlmerysProduct result =
          service.createAlmerysProduct(mapper.productDtoToProduct(almerysProductDto));
      eventService.sendObservabilityEventAlmerysProductCreation(
          result, authenticationFacade.getAuthenticationUserName());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  private void checkIfProductAlreadyExists(AlmerysProductDto almerysProductDto) {
    AlmerysProductRequest request = new AlmerysProductRequest();
    request.setCode(almerysProductDto.getCode());
    AlmerysProduct almerysProduct = service.getAlmerysProductByCode(request.getCode());
    if (almerysProduct != null) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST, "Already existing Almerys product code");
    }
  }

  @PostMapping("/update")
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<AlmerysProductDto> updateAlmerysProduct(
      @Valid @RequestBody AlmerysProductDto almerysProductDto) {
    try {
      checkProductCombinations(almerysProductDto);
      AlmerysProduct existingProduct = getProduct(almerysProductDto.getCode());
      service.updateAlmerysProduct(mapper.productDtoToProduct(almerysProductDto));
      AlmerysProduct updatedProduct = getProduct(almerysProductDto.getCode());
      service.sortAlmerysProduct(updatedProduct);
      if (updatedProduct != null) {
        eventService.sendObservabilityEventAlmerysProductModification(
            existingProduct, updatedProduct, authenticationFacade.getAuthenticationUserName());
      }

      return new ResponseEntity<>(mapper.productToProductDto(updatedProduct), HttpStatus.OK);
    } catch (Exception ex) {
      log.error(ex.getMessage(), ex);
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
  }

  @GetMapping(
      path = "guarantees/{guaranteeCode}/insurers/{insurerCode}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  @NewSpan
  public ResponseEntity<ParametrageAlmerysResponseDto>
      getParametrageAlmerysByGuaranteeCodeAndInsurerCode(
          @PathVariable("guaranteeCode") @NonNull final String guaranteeCode,
          @PathVariable("insurerCode") @NonNull final String insurerCode) {
    ParametrageAlmerysResponseDto response =
        service.getByGuaranteeCodeAndInsurerCode(guaranteeCode, insurerCode);
    return new ResponseEntity<>(response, HttpStatus.OK);
  }

  @Nullable
  private AlmerysProduct getProduct(String code) {
    return service.getAlmerysProductByCode(code);
  }

  private static void checkProductCombinations(AlmerysProductDto almerysProductDto) {
    List<ProductCombinationDto> productCombinations = almerysProductDto.getProductCombinations();
    if (CollectionUtils.isNotEmpty(productCombinations)) {
      for (ProductCombinationDto productCombinationDto : productCombinations) {
        if (CollectionUtils.isEmpty(productCombinationDto.getGarantieTechniqueList())
            && CollectionUtils.isEmpty(productCombinationDto.getLotAlmerysList())) {
          throw new ResponseStatusException(
              HttpStatus.BAD_REQUEST,
              "A combination should contain at least one technical warranty or one lot");
        }
      }
    }
  }
}
