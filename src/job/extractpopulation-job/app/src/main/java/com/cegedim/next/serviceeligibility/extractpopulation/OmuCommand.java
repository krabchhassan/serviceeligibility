package com.cegedim.next.serviceeligibility.extractpopulation;

import java.util.concurrent.Callable;
import lombok.Data;
import picocli.CommandLine;

@Data
@CommandLine.Command(
    description = {"Commande pour l'extraction des contrats, bénéficiaires et garanties"})
public class OmuCommand implements Callable<Integer> {

  @CommandLine.Option(
      names = {"--FORMAT"},
      description = "Le format de sortie des flux de comparaison (CSV ou JSON)")
  private String format;

  @Override
  public Integer call() {
    return 1;
  }
}
