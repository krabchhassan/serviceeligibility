package com.cegedim.next.serviceeligibility.core.model.domain.parametragecartetp;

import com.cegedim.next.serviceeligibility.core.model.enumeration.ModeDeclenchementCarteTP;
import java.util.List;
import lombok.Data;

@Data
public class ParametrageCarteTPRequest {
  private List<String> amcs;
  private boolean status;
  private List<ModeDeclenchementCarteTP> triggerMode;
  private int perPage;
  private int page;
  private String sortBy;
  private String direction;
  private List<String> lots;
  private List<String> gts;
}
