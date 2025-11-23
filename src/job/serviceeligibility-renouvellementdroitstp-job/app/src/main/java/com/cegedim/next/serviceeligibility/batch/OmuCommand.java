package com.cegedim.next.serviceeligibility.batch;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.BATCH_MODE_NO_RDO;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(description = {"Commande du batch de purge"})
public class OmuCommand implements Callable<Integer> {
  @CommandLine.Option(
      names = {"--BATCH_MODE"},
      required = false)
  private String batchMode = BATCH_MODE_NO_RDO;

  public String getBatchMode() {
    return batchMode;
  }

  @Override
  public Integer call() throws Exception {
    return 1;
  }
}
