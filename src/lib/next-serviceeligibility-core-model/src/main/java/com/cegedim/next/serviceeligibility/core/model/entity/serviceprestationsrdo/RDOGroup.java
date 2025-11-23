package com.cegedim.next.serviceeligibility.core.model.entity.serviceprestationsrdo;

import com.cegedim.next.serviceeligibility.core.model.domain.generic.GenericDomain;
import com.cegedim.next.serviceeligibility.core.mongo.DocumentEntity;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = false)
@Document(collection = "rdoServicePrestation")
public class RDOGroup extends DocumentEntity implements GenericDomain<RDOGroup> {

  private List<ServicePrestationsRdo> servicePrestationsRdo;

  @Override
  public int compareTo(RDOGroup source) {
    CompareToBuilder compare = new CompareToBuilder();
    compare.append(this.get_id(), source.get_id());
    compare.append(this.getServicePrestationsRdo(), source.getServicePrestationsRdo());
    return compare.toComparison();
  }
}
