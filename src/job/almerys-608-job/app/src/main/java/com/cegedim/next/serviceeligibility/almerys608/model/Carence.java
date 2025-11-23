package com.cegedim.next.serviceeligibility.almerys608.model;

import com.cegedim.next.serviceeligibility.core.job.batch.BulkObject;
import java.util.List;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/** infoCadreExercice dans le xsd */
@Data
@Document(collection = "almv3_Carence")
public class Carence extends BulkObject {

  private String numeroContrat;
  private String refInterneOS;
  private String refProduit;
  private List<CarenceInfo> carenceInfos;
}
