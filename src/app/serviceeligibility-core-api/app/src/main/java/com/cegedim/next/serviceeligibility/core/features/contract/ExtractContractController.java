package com.cegedim.next.serviceeligibility.core.features.contract;

import com.cegedim.next.serviceeligibility.core.model.domain.contract.BulkContracts;
import com.cegedim.next.serviceeligibility.core.model.query.ContractRequest;
import com.cegedim.next.serviceeligibility.core.services.BulkExtractContractService;
import io.micrometer.tracing.annotation.NewSpan;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/contrats/extract")
@RequiredArgsConstructor
public class ExtractContractController {
  // AUTOWIRED
  private final BulkExtractContractService bulkExtractContractService;

  // --------------------
  // METHODS
  // --------------------
  @NewSpan
  @PostMapping
  public BulkContracts extract(@RequestBody List<ContractRequest> requestList) {
    return bulkExtractContractService.extract(requestList);
  }
}
