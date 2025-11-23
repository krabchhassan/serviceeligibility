package com.cegedim.next.serviceeligibility.rdoserviceprestation;

import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(description = {"Commande du batch de rdo"})
public class OmuCommand implements Callable<Integer> {
  @CommandLine.Option(
      names = {"--CONTRACT_VERSION"},
      required = false)
  private String contractVersion = Constants.CONTRACT_VERSION_V6;

  public String getContractVersion() {
    return contractVersion;
  }

  @Override
  public Integer call() {
    return 1;
  }
}
