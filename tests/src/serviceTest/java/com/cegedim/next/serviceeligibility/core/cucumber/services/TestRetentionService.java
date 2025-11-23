package com.cegedim.next.serviceeligibility.core.cucumber.services;

import com.cegedim.next.serviceeligibility.core.model.entity.Retention;
import com.cegedim.next.serviceeligibility.core.model.entity.RetentionStatus;
import com.cegedim.next.serviceeligibility.core.services.bdd.RetentionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestRetentionService {

  private final RetentionService retentionService;

  public List<Retention> getAllRetentions() {
    return retentionService.getAll();
  }

  public void updateStatusRetention(Retention retention, String status) {
    retentionService.updateStatus(retention, RetentionStatus.valueOf(status));
  }
}
