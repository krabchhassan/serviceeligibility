package com.cegedim.next.serviceeligibility.reprisebeneficiaires;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(description = {"Commande du batch de purge"})
public class OmuCommand implements Callable<Integer> {
  @CommandLine.Option(
      names = {"--CONTRACT_TYPES"},
      required = false)
  private String contractTypes = null;

  public String getContractTypes() {
    return contractTypes;
  }

  @CommandLine.Option(
      names = {"--DATE_REPRISE"},
      required = false)
  private String dateReprise = null;

  public String getDateReprise() {
    return dateReprise;
  }

  @Override
  public Integer call() throws Exception {
    return 1;
  }
}
