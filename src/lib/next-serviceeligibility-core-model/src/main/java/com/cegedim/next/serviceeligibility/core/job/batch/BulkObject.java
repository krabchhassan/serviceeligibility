package com.cegedim.next.serviceeligibility.core.job.batch;

import org.springframework.data.mongodb.core.BulkOperations;

public abstract class BulkObject {
  public void bulk(BulkOperations bulkOperation) {
    bulkOperation.insert(this);
  }
}
