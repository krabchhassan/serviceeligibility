package com.cegedim.next.serviceeligibility.rdoserviceprestationpurge;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(description = {"Commande du batch de purge"})
public class OmuCommand implements Callable<Integer> {
  @CommandLine.Option(
      names = {"--NOM_FICHIER"},
      required = false)
  private String nomFichier = null;

  public String getNomFichier() {
    return nomFichier;
  }

  @CommandLine.Option(
      names = {"--NUM_AMC"},
      required = false)
  private String numAmc = null;

  public String getNumAmc() {
    return numAmc;
  }

  @Override
  public Integer call() throws Exception {
    return 1;
  }
}
