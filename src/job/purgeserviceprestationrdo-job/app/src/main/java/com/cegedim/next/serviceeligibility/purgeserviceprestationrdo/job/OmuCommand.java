package com.cegedim.next.serviceeligibility.purgeserviceprestationrdo.job;

import java.util.concurrent.Callable;
import lombok.Data;
import picocli.CommandLine;

@Data
@CommandLine.Command(description = {"Commande du batch de purge de servicePrestationRdo"})
public class OmuCommand implements Callable<Integer> {

  @Override
  public Integer call() {
    return 1;
  }
}
