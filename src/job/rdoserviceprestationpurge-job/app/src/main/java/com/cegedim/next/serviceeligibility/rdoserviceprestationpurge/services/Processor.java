package com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.services;

import static com.cegedim.next.serviceeligibility.rdoserviceprestationpurge.constants.Constants.*;

import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduPurgeRdo;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Processor {

  private final Logger logger = LoggerFactory.getLogger(Processor.class);

  private final FileService fileService;

  @Autowired private CrexProducer crexProducer;

  public Processor(@Autowired FileService fileService) {
    this.fileService = fileService;
  }

  public int readFolders(int searchType, String input) {
    int status = -1;
    if (searchType == INVALID_ARGUMENT) {
      logger.error("error processing batch, contains too many or no arguments");
      crexProducer.generateCrex(new CompteRenduPurgeRdo());
      status = 1;
    } else {
      boolean problemWhileProcessing = fileService.processFile(searchType, input);
      if (!problemWhileProcessing) {
        logger.info("Job Success");
        status = 0;
      } else {
        logger.error("Error while processing batch");
        status = 1;
      }
    }

    return status;
  }
}
