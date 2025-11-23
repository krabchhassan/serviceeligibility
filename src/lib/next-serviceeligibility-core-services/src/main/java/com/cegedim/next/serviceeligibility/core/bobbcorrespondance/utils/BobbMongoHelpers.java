package com.cegedim.next.serviceeligibility.core.bobbcorrespondance.utils;

import static com.cegedim.next.serviceeligibility.core.bobbcorrespondance.constants.CommonConstants.*;

import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.PeriodSelector;
import com.cegedim.next.serviceeligibility.core.bobbcorrespondance.dto.ProductSelector;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.query.Criteria;

public final class BobbMongoHelpers {

  private BobbMongoHelpers() {}

  public static Date toUtcDate(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return Date.from(dateTime.toInstant(ZoneOffset.UTC));
  }

  public static Criteria productElementFilters(ProductSelector sel) {
    List<Criteria> filters = new ArrayList<>();
    filters.add(Criteria.where("pe." + CODE_OFFER).is(sel.codeOffer()));
    filters.add(Criteria.where("pe." + CODE_PRODUCT).is(sel.codeProduct()));
    filters.add(Criteria.where("pe." + CODE_BENEFIT_NATURE).is(sel.codeBenefitNature()));
    filters.add(Criteria.where("pe." + CODE_AMC).is(sel.codeAmc()));

    if (sel.from() != null) {
      filters.add(Criteria.where("pe." + FROM).is(toUtcDate(sel.from().atStartOfDay())));
    }

    return combineAnd(filters);
  }

  public static Criteria periodFilters(PeriodSelector period) {
    List<Criteria> filters = new ArrayList<>();

    filters.add(Criteria.where("pe." + FROM).is(toUtcDate(period.from().atStartOfDay())));

    if (period.to() == null) {
      filters.add(
          new Criteria()
              .orOperator(
                  Criteria.where("pe." + TO).exists(false), Criteria.where("pe." + TO).is(null)));
    } else {
      filters.add(Criteria.where("pe." + TO).is(toUtcDate(period.to().atStartOfDay())));
    }
    return combineAnd(filters);
  }

  private static Criteria combineAnd(List<Criteria> filters) {
    if (filters == null || filters.isEmpty()) {
      return new Criteria().andOperator(new Criteria().is(false));
    }
    if (filters.size() == 1) {
      return filters.get(0);
    }
    return new Criteria().andOperator(filters.toArray(new Criteria[0]));
  }
}
