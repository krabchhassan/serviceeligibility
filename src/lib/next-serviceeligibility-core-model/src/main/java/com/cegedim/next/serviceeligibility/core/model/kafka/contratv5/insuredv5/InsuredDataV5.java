package com.cegedim.next.serviceeligibility.core.model.kafka.contratv5.insuredv5;

import com.cegedim.next.serviceeligibility.core.model.kafka.Nir;
import com.cegedim.next.serviceeligibility.core.model.kafka.NirRattachementRO;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DataAssure;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InsuredDataV5 extends DataAssure {

  private String dateNaissance;

  private String rangNaissance;

  private Nir nir;

  private List<NirRattachementRO> affiliationsRO;
}
