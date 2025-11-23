package com.cegedim.next.serviceeligibility.rdoserviceprestation.pojo;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduRdo;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.ContratAICommun;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RequestFileTask {

  private String shortFileName;

  private String fileName;

  private Class<? extends ContratAICommun> contratVersion;

  private boolean generationDroit;

  private CompteRenduRdo compteRenduRdo;

  private String arlFolder;

  private String batchExecutionTime;

  private boolean isUnitTest;

  private boolean controlMeta;
}
