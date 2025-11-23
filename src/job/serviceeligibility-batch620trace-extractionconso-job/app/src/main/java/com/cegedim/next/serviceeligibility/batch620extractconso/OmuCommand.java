package com.cegedim.next.serviceeligibility.batch620extractconso;

import java.util.concurrent.Callable;
import lombok.Data;
import picocli.CommandLine;

@Data
@CommandLine.Command(description = {"Commande de generation des cartes"})
public class OmuCommand implements Callable<Integer> {

  @CommandLine.Option(
      names = {"--COULOIR_CLIENT"},
      description = "Le couloir client")
  private String couloirClient;

  @Override
  public Integer call() {
    return 1;
  }
}
