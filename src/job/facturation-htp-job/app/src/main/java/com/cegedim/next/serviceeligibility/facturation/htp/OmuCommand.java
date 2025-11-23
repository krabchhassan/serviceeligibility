package com.cegedim.next.serviceeligibility.facturation.htp;

import java.util.concurrent.Callable;
import lombok.Data;
import picocli.CommandLine;

@Data
@CommandLine.Command(description = {"Commande de facturation des clients HTP vers le CETIP"})
public class OmuCommand implements Callable<Integer> {

  @CommandLine.Option(
      names = {"--MOIS"},
      description = "Le mois cible pour le calcul (MM)")
  private String moisCalcul;

  @CommandLine.Option(
      names = {"--ANNEE"},
      description = "L annee cible pour le calcul (yyyy)")
  private String anneeCalcul;

  @CommandLine.Option(
      names = {"--AMC_LIST"},
      description =
          "La liste d AMC separee par des virgules (utile uniquement pour les clients OTP)")
  private String amcList;

  @Override
  public Integer call() {
    return 1;
  }
}
