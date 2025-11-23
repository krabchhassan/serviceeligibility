package com.cegedim.next.serviceeligibility.core.bdd.backend.dto.benefitrecipients;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.generic.GenericDto;
import com.cegedim.next.serviceeligibility.core.utils.DateUtils;
import lombok.Data;

@Data
public class BenefitRecipientsDto implements GenericDto, Comparable<BenefitRecipientsDto> {

  private String idBeyond;
  private String validityStartDate;
  private String validityEndDate;

  @Override
  public int compareTo(BenefitRecipientsDto benefitRecipientsDto) {
    return DateUtils.compareDate(
        getValidityStartDate(), benefitRecipientsDto.getValidityStartDate(), DateUtils.FORMATTER);
  }
}
