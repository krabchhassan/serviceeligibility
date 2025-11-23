package com.cegedim.next.serviceeligibility.almerys608;

import java.util.concurrent.Callable;
import lombok.Data;
import picocli.CommandLine;

@Data
@CommandLine.Command(description = {"Commande pour la constitution du flux Almerys"})
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
