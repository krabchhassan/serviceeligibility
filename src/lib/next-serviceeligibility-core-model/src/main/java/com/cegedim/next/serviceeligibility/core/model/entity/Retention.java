package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.AffiliationRO;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "retention")
@EqualsAndHashCode(callSuper = true)
public class Retention extends DocumentEntity {
  private static final long serialVersionUID = 1L;

  private String insurerId;
  private String issuingCompanyCode;
  private String subscriberNumber;
  private String contractNumber;
  private String personNumber;
  private String nir;
  private List<AffiliationRO> affiliationsRO;
  private String birthDate;
  private String birthRank;
  private String originalEndDate;
  private String currentEndDate;
  private LocalDateTime receptionDate;
  private RetentionStatus status;
  private LinkedList<RetentionHistorique> historique;
}
