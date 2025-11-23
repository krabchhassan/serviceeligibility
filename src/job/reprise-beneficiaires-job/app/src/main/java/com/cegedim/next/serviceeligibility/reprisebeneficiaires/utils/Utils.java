package com.cegedim.next.serviceeligibility.reprisebeneficiaires.utils;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class Utils {
  @Value("${MAXIMUM_BATCH_SIZE:5000}")
  private int maximumBatchSize;

  public Aggregation getAggregation(int page) {
    List<AggregationOperation> steps = new ArrayList<>();

    // Matches all documents
    steps.add(match(Criteria.where("_id").exists(true)));

    // Skips first X results
    steps.add(skip((long) page * maximumBatchSize));

    // Get up to X results
    steps.add(limit(maximumBatchSize));

    return Aggregation.newAggregation(steps)
        .withOptions(Aggregation.newAggregationOptions().allowDiskUse(true).build());
  }
}
