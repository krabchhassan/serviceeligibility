package com.cegedim.next.serviceeligibility.core.utils;

import com.cegedim.common.omu.helper.OmuHelper;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduGeneric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CrexProducer {
  @Autowired private OmuHelper omuHelper;

  private static final Logger logger = LoggerFactory.getLogger(CrexProducer.class);

  public void generateCrex(CompteRenduGeneric compteRendu) {
    logger.info("=== Generation du CREX ===");

    try {
      if (compteRendu == null) {
        throw new IllegalArgumentException("le compte rendu passé en paramètre est null");
      }

      omuHelper.produceOutputParameters(compteRendu.asMap());
      logger.info("CREX genere avec succes");
    } catch (IllegalArgumentException e) {
      logger.error("Erreur lors de la genération du CREX : {}", e.getMessage());
    }
  }
}
