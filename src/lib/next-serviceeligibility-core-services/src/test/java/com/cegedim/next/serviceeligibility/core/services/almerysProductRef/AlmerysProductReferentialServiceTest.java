package com.cegedim.next.serviceeligibility.core.services.almerysProductRef;

import static org.junit.jupiter.api.Assertions.*;

import com.cegedim.next.serviceeligibility.core.almerysProductRef.LotAlmerys;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.dao.AlmerysProductReferentialRepository;
import com.cegedim.next.serviceeligibility.core.dao.LotDao;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ParametrageAlmerysResponseDto;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ProductCombination;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlmerysProductReferentialServiceTest {
  public static final String LOT_1 = "LOT1";
  public static final String LOT_2 = "LOT2";
  public static final String LOT_3 = "LOT3";
  public static final String LOT_DUP = "LOT_DUP";
  public static final String CODE_GARANTIE = "GTX";
  public static final String CODE_ASSUREUR = "ASSX";

  @Mock private AlmerysProductReferentialRepository almerysProductReferentialRepository;

  @Mock private LotDao lotDao;

  @InjectMocks private AlmerysProductReferentialService almerysProductReferentialService;

  @Test
  void should_return_only_active_lots_linked_to_GT_and_present_in_almerys() {
    GarantieTechnique gtActive = new GarantieTechnique();
    gtActive.setCodeGarantie(CODE_GARANTIE);
    gtActive.setCodeAssureur(CODE_ASSUREUR);
    gtActive.setDateSuppressionLogique(null);

    GarantieTechnique gtDeleted = new GarantieTechnique();
    gtDeleted.setCodeGarantie(CODE_GARANTIE);
    gtDeleted.setCodeAssureur(CODE_ASSUREUR);
    gtDeleted.setDateSuppressionLogique("2025-01-01");

    Lot lot1 = new Lot();
    lot1.setCode(LOT_1);
    lot1.setGarantieTechniques(List.of(gtActive));

    Lot lot2 = new Lot();
    lot2.setCode(LOT_2);
    lot2.setGarantieTechniques(List.of(gtDeleted));

    Lot lot3 = new Lot();
    lot3.setCode(LOT_3);
    lot3.setGarantieTechniques(null);

    LotAlmerys lotAlmerys1 = new LotAlmerys();
    lotAlmerys1.setCode(LOT_1);

    LotAlmerys lotAlmerys2 = new LotAlmerys();
    lotAlmerys2.setCode(LOT_2);

    ProductCombination pc = new ProductCombination();
    pc.setLotAlmerysList(List.of(lotAlmerys1, lotAlmerys2));

    AlmerysProduct almerysProduct = new AlmerysProduct();
    almerysProduct.setProductCombinations(List.of(pc));

    Mockito.when(
            almerysProductReferentialRepository.findByGuaranteeCodeAndInsurerCode(
                CODE_GARANTIE, CODE_ASSUREUR))
        .thenReturn(List.of(almerysProduct));
    Mockito.when(lotDao.findByGT(CODE_GARANTIE, CODE_ASSUREUR)).thenReturn(List.of(lot1, lot3));

    ParametrageAlmerysResponseDto result =
        almerysProductReferentialService.getByGuaranteeCodeAndInsurerCode(
            CODE_GARANTIE, CODE_ASSUREUR);

    assertNotNull(result);
    assertEquals(1, result.getParametragesAlmerys().size());

    List<String> lotCodes = result.getLots().stream().map(Lot::getCode).toList();

    assertEquals(1, lotCodes.size());
    assertTrue(lotCodes.contains(LOT_1));
    assertFalse(lotCodes.contains(LOT_2));
    assertFalse(lotCodes.contains(LOT_3));
  }

  @Test
  void should_return_empty_response_when_no_parametrages_almerys_found() {
    Mockito.when(
            almerysProductReferentialRepository.findByGuaranteeCodeAndInsurerCode(
                CODE_GARANTIE, CODE_ASSUREUR))
        .thenReturn(Collections.emptyList());

    ParametrageAlmerysResponseDto result =
        almerysProductReferentialService.getByGuaranteeCodeAndInsurerCode(
            CODE_GARANTIE, CODE_ASSUREUR);

    assertNotNull(result);
    assertTrue(result.getLots().isEmpty());
    assertTrue(result.getParametragesAlmerys().isEmpty());
  }

  @Test
  void should_handle_duplicate_lot_codes() {
    String codeGarantie = "GT_DUP";
    String codeAssureur = "ASS_DUP";

    LotAlmerys lotAlmerys = new LotAlmerys();
    lotAlmerys.setCode(LOT_DUP);

    ProductCombination productCombination1 = new ProductCombination();
    productCombination1.setLotAlmerysList(List.of(lotAlmerys));

    ProductCombination productCombination2 = new ProductCombination();
    productCombination2.setLotAlmerysList(List.of(lotAlmerys));

    AlmerysProduct product = new AlmerysProduct();
    product.setProductCombinations(List.of(productCombination1, productCombination2));

    Mockito.when(
            almerysProductReferentialRepository.findByGuaranteeCodeAndInsurerCode(
                codeGarantie, codeAssureur))
        .thenReturn(List.of(product));

    Lot lot = new Lot();
    lot.setCode(LOT_DUP);
    Mockito.when(lotDao.findByGT(codeGarantie, codeAssureur)).thenReturn(List.of(lot));

    ParametrageAlmerysResponseDto result =
        almerysProductReferentialService.getByGuaranteeCodeAndInsurerCode(
            codeGarantie, codeAssureur);

    assertNotNull(result);
    assertEquals(1, result.getLots().size());
    assertEquals(LOT_DUP, result.getLots().getFirst().getCode());
  }
}
