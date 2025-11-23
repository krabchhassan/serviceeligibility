package com.cegedim.beyond.undue.retention;

import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(description = {"Commande du batch de undue retention"})
public class OmuCommand implements Callable<Integer> {

  @Override
  public Integer call() {
    return 1;
  }
}
