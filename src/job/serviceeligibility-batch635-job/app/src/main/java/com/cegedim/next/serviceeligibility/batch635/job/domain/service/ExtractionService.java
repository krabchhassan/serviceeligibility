package com.cegedim.next.serviceeligibility.batch635.job.domain.service;

import com.cegedim.next.serviceeligibility.batch635.job.helpers.AmcReferenceDateLineEligible;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public interface ExtractionService {
  Runnable extract(
      AmcReferenceDateLineEligible amcReferenceDateLineEligible,
      String fileName,
      PrintWriter printWriter,
      List<String> extractionFileNames)
      throws IOException;
}
