package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.services;

import com.cegedim.next.serviceeligibility.core.bobb.ContractElement;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.ClosePeriodRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CloseProductElementRequest;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.CorrespondenceResponseDto;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.GroupBy;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.model.SearchCriteria;
import com.cegedim.next.serviceeligibility.core.dto.GuaranteeSearchResultDto;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BobbCorrespondanceService {
  ContractElement findCorrespondance(
      @NotNull String codeContractElement, @NotNull String codeInsurer);

  boolean isCorrespondenceExist(@NotNull String codeContractElement, @NotNull String codeInsurer);

  CorrespondenceResponseDto getProductsByGtId(
      String gtId, GroupBy groupBy, @Nullable LocalDate appliedDate);

  CorrespondenceResponseDto getProductsByCodes(
      String codeContractElement,
      String codeInsurer,
      GroupBy groupBy,
      @Nullable LocalDate appliedDate);

  Page<GuaranteeSearchResultDto> search(SearchCriteria criteria, Pageable pageable);

  int closeOneProductElementByGt(String gtId, CloseProductElementRequest body);

  int closePeriodByGt(String gtId, ClosePeriodRequest body);

  List<String> findGuaranteeCodes(String codeOffer, String codeAmc);

  List<String> findOfferCodes(String codeContractElement, String codeAmc);

  List<String> findAmcCodes(String codeOffer, String codeContractElement);

  void checkParams(Map<String, String> allParams, Set<String> allowed);
}
