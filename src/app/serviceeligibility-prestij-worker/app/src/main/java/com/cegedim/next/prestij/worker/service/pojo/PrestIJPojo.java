package com.cegedim.next.prestij.worker.service.pojo;

import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.ContratIJ;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Entreprise;
import com.cegedim.next.serviceeligibility.core.model.kafka.prestij.Oc;
import java.util.List;
import lombok.Data;

@Data
public class PrestIJPojo {
  private String traceId;

  private ContratIJ contrat;

  private Oc oc;

  private Entreprise entreprise;

  private List<Assure> assures;
}
