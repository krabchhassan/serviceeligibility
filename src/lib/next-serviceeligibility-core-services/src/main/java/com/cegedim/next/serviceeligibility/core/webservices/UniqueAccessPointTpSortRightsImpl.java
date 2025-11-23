package com.cegedim.next.serviceeligibility.core.webservices;

import com.cegedim.next.serviceeligibility.core.webservices.dto.droitsmultiples.Right;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public abstract class UniqueAccessPointTpSortRightsImpl implements UniqueAccessPointTpSortRights {

  public void sort(List<Right> rights) {
    Comparator<Right> comparator = this::sortPrioriteGarantie;
    rights.sort(comparator);
  }

  private int sortPrioriteGarantie(Right right1, Right right2) {
    int compare =
        StringUtils.compare(right1.getPrioritizationOrder(), right2.getPrioritizationOrder());
    if (compare == 0) {
      return sortGarantieCode(right1, right2);
    }
    return compare;
  }

  private int sortGarantieCode(Right right1, Right right2) {
    String smallestGarantieCode1 = right1.getCode();
    String smallestGarantieCode2 = right2.getCode();
    return StringUtils.compare(smallestGarantieCode1, smallestGarantieCode2);
  }
}
