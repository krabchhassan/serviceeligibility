package com.cegedim.next.serviceeligibility.batch620extractconso;

import com.cegedim.common.omu.helper.configuration.OmuHelperConfiguration;
import com.cegedim.next.serviceeligibility.core.dao.*;
import com.cegedim.next.serviceeligibility.core.dao.traces.TraceExtractionConsoDaoImpl;
import com.cegedim.next.serviceeligibility.core.services.cartedemat.cartepapier.CartesPapierService;
import com.cegedim.next.serviceeligibility.core.services.event.EventService;
import com.cegedim.next.serviceeligibility.core.services.trace.TraceExtractionConsoService;
import com.cegedim.next.serviceeligibility.core.utils.CrexProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(
    value = {
      CrexProducer.class,
      OmuHelperConfiguration.class,
      CartesPapierService.class,
      CartePapierDaoImpl.class,
      TraceExtractionConsoService.class,
      EventService.class,
      HistoriqueExecutionsDaoImpl.class,
      DeclarationDaoImpl.class,
      TraceExtractionConsoDaoImpl.class
    })
public class Batch620TraceExtractionConsoJob {
  public static void main(final String[] args) {
    new SpringApplication(Batch620TraceExtractionConsoJob.class).run(args).close();
  }
}
