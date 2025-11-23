package com.cegedim.next.serviceeligibility.core.model.domain.pw;

import java.util.Map;
import lombok.Data;

@Data
public class Domain {
  String domainCode;
  Map<String, DetailsByDomain> detailsByDomain;
}
