package com.cegedim.next.serviceeligibility.core.model.entity;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cache")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cache extends DocumentEntity implements GenericDomain<Cache> {
  private String cacheName;
  private Integer version;
  private Date changeDate;
  private String userId;

  @Override
  public int compareTo(Cache cache) {
    CompareToBuilder compareToBuilder = new CompareToBuilder();
    compareToBuilder.append(this.get_id(), cache.get_id());
    return compareToBuilder.toComparison();
  }
}
