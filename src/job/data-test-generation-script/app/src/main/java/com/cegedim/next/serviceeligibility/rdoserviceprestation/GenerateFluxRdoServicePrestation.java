package com.cegedim.next.serviceeligibility.rdoserviceprestation;

import com.cegedim.next.serviceeligibility.rdoserviceprestation.services.FluxRdoServicePrestationService;
import java.io.IOException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
    value = {
      "com.cegedim.next.serviceeligibility.core.model.kafka",
      "com.cegedim.next.serviceeligibility.rdoserviceprestation.services"
    })
public class GenerateFluxRdoServicePrestation {

  public static void main(String[] args) throws IOException {
    ConfigurableApplicationContext context =
        new SpringApplication(GenerateFluxRdoServicePrestation.class).run();
    /*
     * Création d'un flux de RDO Param : nbContrat : Nb de contrat à créer dans le
     * flux idDeclarant : n° de l'AMC, elle doit exister en base cible avec un
     * idClientBO de paramétrer getContratFromBaseForUpdate : Booléen permettant
     * d'indiquer si le flux doit contenir des contrat existant en base pour leur
     * mise à jour
     */
    System.exit(
        context.getBean(FluxRdoServicePrestationService.class).createFile(10, "0000452433"));
  }
}
