package com.cegedim.next.serviceeligibility.core.services.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.almerysProductRef.AlmerysProductRequest;
import com.cegedim.next.serviceeligibility.core.almerysProductRef.AlmerysProductResponse;
import com.cegedim.next.serviceeligibility.core.almerysProductRef.LotAlmerys;
import com.cegedim.next.serviceeligibility.core.bobb.Lot;
import com.cegedim.next.serviceeligibility.core.dao.AlmerysProductReferentialRepository;
import com.cegedim.next.serviceeligibility.core.dao.LotDao;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ParametrageAlmerysResponseDto;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlmerysProductReferentialService {
  private final AlmerysProductReferentialRepository repository;
  private final LotDao lotDao;

  public Collection<AlmerysProduct> getAlmerysProductReferential() {
    return repository.getAlmerysProductReferential();
  }

  public AlmerysProductResponse getAlmerysProduct(AlmerysProductRequest request) {
    return repository.getAlmerysProduct(request);
  }

  public AlmerysProduct getAlmerysProductByCode(String code) {
    return repository.findByCode(code);
  }

  public void sortAlmerysProduct(AlmerysProduct almerysProduct) {
    repository.sortAlmerysProduct(almerysProduct);
  }

  public AlmerysProduct createAlmerysProduct(AlmerysProduct almerysProduct) {
    return repository.createAlmerysProduct(almerysProduct);
  }

  public AlmerysProduct[] updateAlmerysProduct(AlmerysProduct almerysProduct) {
    AlmerysProduct[] almerysProducts = new AlmerysProduct[2];
    AlmerysProductRequest request = new AlmerysProductRequest();
    request.setCode(almerysProduct.getCode());
    AlmerysProductResponse almerysProductResponse = this.getAlmerysProduct(request);
    if (almerysProductResponse != null
        && CollectionUtils.isNotEmpty(almerysProductResponse.getAlmerysProductList())) {
      almerysProducts[0] = almerysProductResponse.getAlmerysProductList().get(0);
    }
    repository.updateAlmerysProduct(almerysProduct);
    almerysProductResponse = this.getAlmerysProduct(request);
    if (almerysProductResponse != null
        && CollectionUtils.isNotEmpty(almerysProductResponse.getAlmerysProductList())) {
      almerysProducts[1] = almerysProductResponse.getAlmerysProductList().get(0);
    }
    return almerysProducts;
  }

  public ParametrageAlmerysResponseDto getByGuaranteeCodeAndInsurerCode(
      String guaranteeCode, String insurerCode) {
    List<AlmerysProduct> almerysProducts =
        repository.findByGuaranteeCodeAndInsurerCode(guaranteeCode, insurerCode);
    List<String> codesLotsAlmerys =
        almerysProducts.stream()
            .flatMap(p -> p.getProductCombinations().stream())
            .flatMap(pc -> pc.getLotAlmerysList().stream())
            .map(LotAlmerys::getCode)
            .distinct()
            .toList();
    List<Lot> lotsAssociesAuxGT = lotDao.findByGT(guaranteeCode, insurerCode);
    List<Lot> lots =
        lotsAssociesAuxGT.stream().filter(l -> codesLotsAlmerys.contains(l.getCode())).toList();

    return ParametrageAlmerysResponseDto.builder()
        .parametragesAlmerys(almerysProducts)
        .lots(lots)
        .build();
  }
}
