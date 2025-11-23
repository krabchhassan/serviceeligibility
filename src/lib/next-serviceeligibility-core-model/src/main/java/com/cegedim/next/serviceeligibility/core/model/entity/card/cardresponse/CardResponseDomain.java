package com.cegedim.next.serviceeligibility.core.model.entity.card.cardresponse;

import java.util.List;
import lombok.Data;

@Data
public class CardResponseDomain {
  private String code;
  private String libelleDomaine;
  private Integer rang;
  private List<CardResponseConvention> conventions;
}
