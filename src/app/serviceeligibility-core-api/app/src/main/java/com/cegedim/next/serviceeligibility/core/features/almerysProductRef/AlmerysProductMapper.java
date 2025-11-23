package com.cegedim.next.serviceeligibility.core.features.almerysProductRef;

import com.cegedim.next.serviceeligibility.core.almerysProductRef.LotAlmerys;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef.AlmerysProductDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef.LotAlmerysDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.almerysProductRef.ProductCombinationDto;
import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.bobb.GTDto;
import com.cegedim.next.serviceeligibility.core.bobb.GarantieTechnique;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.AlmerysProduct;
import com.cegedim.next.serviceeligibility.core.model.domain.almerysProductRef.ProductCombination;
import org.mapstruct.Mapper;

@Mapper
public interface AlmerysProductMapper {

  AlmerysProduct productDtoToProduct(AlmerysProductDto almerysProductDto);

  AlmerysProductDto productToProductDto(AlmerysProduct almerysProduct);

  ProductCombination combinationToCombinationDto(ProductCombinationDto productCombinationDto);

  ProductCombinationDto combinationDtoToCombination(ProductCombination productCombination);

  GarantieTechnique warrantyDtoToWarranty(GTDto garantieTechniqueDto);

  GTDto warrantyToWarrantyDto(GarantieTechnique garantieTechnique);

  LotAlmerys lotDtoToLot(LotAlmerysDto lotDto);

  LotAlmerysDto lotToLotDto(LotAlmerys lot);
}
