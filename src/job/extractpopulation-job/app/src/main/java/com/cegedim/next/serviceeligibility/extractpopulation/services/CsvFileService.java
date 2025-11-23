package com.cegedim.next.serviceeligibility.extractpopulation.services;

import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.Assure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratcommun.DroitAssure;
import com.cegedim.next.serviceeligibility.core.model.kafka.contratv6.ContratAIV6;
import com.cegedim.next.serviceeligibility.core.utils.Constants;
import com.cegedim.next.serviceeligibility.extractpopulation.constants.ConstantsExtractPop;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringJoiner;
import java.util.stream.Stream;
import org.apache.commons.collections4.ListUtils;

public class CsvFileService {

  public void fillCsvFiles(
      String csvDelemiter,
      Stream<ContratAIV6> stream,
      FileWriter contratsWriter,
      FileWriter assuresWriter,
      FileWriter garantiesWriter)
      throws IOException {
    contratsWriter.append(
        toCSVQuotedVariables(
            csvDelemiter,
            ConstantsExtractPop.NUM_AMC,
            ConstantsExtractPop.NUM_ADHERENT,
            ConstantsExtractPop.NUM_CONTRAT));
    assuresWriter.append(
        toCSVQuotedVariables(
            csvDelemiter,
            ConstantsExtractPop.NUM_AMC,
            ConstantsExtractPop.NUM_ADHERENT,
            ConstantsExtractPop.NUM_CONTRAT,
            ConstantsExtractPop.NUM_PERSONNE,
            "isSouscripteur",
            "Nir principal",
            "Date de naissance",
            "Rang de naissance",
            "Nom de famille",
            "Nom d’usage",
            "Prénom"));
    garantiesWriter.append(
        toCSVQuotedVariables(
            csvDelemiter,
            ConstantsExtractPop.NUM_AMC,
            ConstantsExtractPop.NUM_ADHERENT,
            ConstantsExtractPop.NUM_CONTRAT,
            ConstantsExtractPop.NUM_PERSONNE,
            "Code assureur",
            "Code GT",
            "Date début",
            "Date fin"));
    Iterator<ContratAIV6> iterator = stream.iterator();
    while (iterator.hasNext()) {
      ContratAIV6 contract = iterator.next();
      contratsWriter.append(getContractLine(csvDelemiter, contract));
      assuresWriter.append(getAssureLine(csvDelemiter, contract));
      garantiesWriter.append(getGarantieLine(csvDelemiter, contract));

      contratsWriter.flush();
      assuresWriter.flush();
      garantiesWriter.flush();
    }
  }

  private String getContractLine(String csvDelemiter, ContratAIV6 contract) {
    return toCSVQuotedVariables(
        csvDelemiter,
        contract.getIdDeclarant(),
        contract.getNumeroAdherent(),
        contract.getNumero());
  }

  private String getAssureLine(String csvDelemiter, ContratAIV6 contract) {
    StringBuilder res = new StringBuilder();
    for (Assure assure : ListUtils.emptyIfNull(contract.getAssures())) {
      String line =
          toCSVQuotedVariables(
              csvDelemiter,
              contract.getIdDeclarant(),
              contract.getNumeroAdherent(),
              contract.getNumero(),
              assure.getIdentite().getNumeroPersonne(),
              assure.getIsSouscripteur() + "",
              assure.getIdentite().getNir() != null
                  ? assure.getIdentite().getNir().getCode()
                  : null,
              assure.getIdentite().getDateNaissance(),
              assure.getIdentite().getRangNaissance(),
              assure.getData().getNom().getNomFamille(),
              assure.getData().getNom().getNomUsage(),
              assure.getData().getNom().getPrenom());
      res.append(line);
    }
    return res.toString();
  }

  private String getGarantieLine(String csvDelemiter, ContratAIV6 contract) {
    StringBuilder res = new StringBuilder();
    for (Assure assure : ListUtils.emptyIfNull(contract.getAssures())) {
      for (DroitAssure droit : ListUtils.emptyIfNull(assure.getDroits())) {
        String line =
            toCSVQuotedVariables(
                csvDelemiter,
                contract.getIdDeclarant(),
                contract.getNumeroAdherent(),
                contract.getNumero(),
                assure.getIdentite().getNumeroPersonne(),
                droit.getCodeAssureur(),
                droit.getCode(),
                droit.getPeriode().getDebut(),
                droit.getPeriode().getFin());
        res.append(line);
      }
    }

    return res.toString();
  }

  private String toCSVQuotedVariables(String delemiter, String... variables) {
    final StringJoiner stringJoiner = new StringJoiner(delemiter, "", System.lineSeparator());
    for (String variable : variables) {
      stringJoiner.add(variable == null ? "" : Constants.QUOTE + variable + Constants.QUOTE);
    }

    return stringJoiner.toString();
  }
}
