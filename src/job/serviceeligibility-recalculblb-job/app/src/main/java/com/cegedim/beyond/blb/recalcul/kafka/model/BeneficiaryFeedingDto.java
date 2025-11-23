package com.cegedim.beyond.blb.recalcul.kafka.model;

import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;

@Data
@Builder
public class BeneficiaryFeedingDto {
  private ObjectId trackingId;
  private String nir;
  private String dateNaissance;
  private String rangNaissance;
  private String errorCode;
  private String errorLabel;
}
