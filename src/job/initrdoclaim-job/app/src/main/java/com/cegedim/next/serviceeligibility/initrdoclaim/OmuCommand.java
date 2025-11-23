package com.cegedim.next.serviceeligibility.initrdoclaim;

import java.util.concurrent.Callable;
import lombok.Data;
import picocli.CommandLine;

@Data
@CommandLine.Command(description = {"Commande de generation des cartes"})
public class OmuCommand implements Callable<Integer> {

  @CommandLine.Option(names = {"--NUM_AMC"})
  private String numAmc;

  public String getNumAmc() {
    return numAmc;
  }

  @Override
  public Integer call() {
    return 1;
  }
}
