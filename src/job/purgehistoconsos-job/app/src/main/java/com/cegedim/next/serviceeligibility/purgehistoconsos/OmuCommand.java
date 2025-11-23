package com.cegedim.next.serviceeligibility.purgehistoconsos;

import com.cegedim.next.serviceeligibility.core.utils.Constants;
import java.util.concurrent.Callable;
import lombok.Getter;
import picocli.CommandLine;

@Getter
@CommandLine.Command(description = {"Commande du batch de rdo"})
public class OmuCommand implements Callable<Integer> {
  @CommandLine.Option(names = {"--DAYS"})
  private String days = Constants.ONE_YEAR_DAYS;

  @Override
  public Integer call() {
    return 1;
  }
}
