package com.cegedim.next.serviceeligibility.facturation.htp.services;

import static com.cegedim.next.serviceeligibility.core.utils.Constants.QUOTE;

import com.cegedim.common.base.pefb.services.MetaService;
import com.cegedim.next.serviceeligibility.core.model.crex.CompteRenduFacturationHTP;
import com.cegedim.next.serviceeligibility.core.services.bdd.DeclarantService;
import com.cegedim.next.serviceeligibility.core.services.contracttp.ContractService;
import com.cegedim.next.serviceeligibility.core.services.pojo.BillingResult;
import com.cegedim.next.serviceeligibility.facturation.htp.constants.Constants;
import io.micrometer.tracing.annotation.NewSpan;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Processor {

  private final ContractService contractService;

  private final DeclarantService declarantService;

  private static final String FACTURATION_CETIP = "facturation_Cetip_";

  @NewSpan
  public int calcul(
      LocalDate dateCalcul,
      String csvDelemiter,
      String csvDirectory,
      CompteRenduFacturationHTP compteRenduFacturationHTP) {
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;

    final List<BillingResult> result = contractService.getContractsForBillingJob(dateCalcul);

    final Map<String, List<BillingResult>> billingResultPerDeclarant =
        result.stream().collect(Collectors.groupingBy(BillingResult::getIdDeclarant));

    Timestamp finTraitement = new Timestamp(System.currentTimeMillis());

    for (String idDeclarant : declarantService.getAllDeclarantIDs()) {

      final List<BillingResult> billingResultsOfIdDeclarant =
          billingResultPerDeclarant.getOrDefault(idDeclarant, Collections.emptyList());

      int csvReturnCode =
          generateFiles(
              idDeclarant,
              billingResultsOfIdDeclarant,
              dateCalcul,
              csvDelemiter,
              csvDirectory,
              finTraitement,
              compteRenduFacturationHTP);

      if (Constants.PROCESSED_WITH_ERRORS == csvReturnCode) {
        processReturnCode = Constants.PROCESSED_WITH_ERRORS;
      }
    }

    return processReturnCode;
  }

  public int calculOTP(
      LocalDate dateCalcul,
      String csvDelemiter,
      String csvDirectory,
      CompteRenduFacturationHTP compteRenduFacturationHTP,
      List<String> amcList) {
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;

    List<BillingResult> result = new ArrayList<>();
    for (String amcId : amcList) {
      result.addAll(contractService.getContractsForBillingOTPJob(dateCalcul, amcId));
    }

    Timestamp finTraitement = new Timestamp(System.currentTimeMillis());

    int csvReturnCode =
        generateOTPFiles(
            result,
            dateCalcul,
            amcList,
            csvDelemiter,
            csvDirectory,
            finTraitement,
            compteRenduFacturationHTP);

    if (Constants.PROCESSED_WITH_ERRORS == csvReturnCode) {
      processReturnCode = Constants.PROCESSED_WITH_ERRORS;
    }

    return processReturnCode;
  }

  private int generateFiles(
      String idDeclarant,
      List<BillingResult> billingResults,
      LocalDate dateCalcul,
      String csvDelemiter,
      String csvDirectory,
      Timestamp finTraitement,
      CompteRenduFacturationHTP compteRenduFacturationHTP) {
    final String csvFileName =
        FACTURATION_CETIP + idDeclarant + "_" + finTraitement.getTime() + ".csv";
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;

    try {
      String csvFilling =
          prepareCSVFilling(
              billingResults,
              csvDelemiter,
              dateCalcul,
              com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER);
      writeToFile(csvDirectory + csvFileName, csvFilling);
      writeMeta(csvDirectory + csvFileName);
      compteRenduFacturationHTP.incExportedFilesCount();
      compteRenduFacturationHTP.addCompteRenduValue("declarantCree", idDeclarant);
    } catch (IOException e) {
      log.error("Erreur lors de la création du csv " + csvFileName);
      processReturnCode = Constants.PROCESSED_WITH_ERRORS;
      compteRenduFacturationHTP.incErrorFilesCount();
      compteRenduFacturationHTP.addCompteRenduValue("declarantKO", idDeclarant);
    }

    return processReturnCode;
  }

  private int generateOTPFiles(
      List<BillingResult> billingResults,
      LocalDate dateCalcul,
      List<String> amcList,
      String csvDelemiter,
      String csvDirectory,
      Timestamp finTraitement,
      CompteRenduFacturationHTP compteRenduFacturationHTP) {
    final String csvFileName = FACTURATION_CETIP + finTraitement.getTime() + ".csv";
    int processReturnCode = Constants.PROCESSED_WITHOUT_ERRORS;

    try {
      String csvFilling =
          prepareCSVFilling(
              billingResults,
              csvDelemiter,
              dateCalcul,
              com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_OTP);
      writeToFile(csvDirectory + csvFileName, csvFilling);
      writeMeta(csvDirectory + csvFileName);
      compteRenduFacturationHTP.incExportedFilesCount();
      addCompteRenduValueForAmcList(compteRenduFacturationHTP, "declarantCree", amcList);
    } catch (IOException e) {
      log.error("Erreur lors de la création du csv " + csvFileName);
      processReturnCode = Constants.PROCESSED_WITH_ERRORS;
      compteRenduFacturationHTP.incErrorFilesCount();
      addCompteRenduValueForAmcList(compteRenduFacturationHTP, "declarantKO", amcList);
    }

    return processReturnCode;
  }

  private void writeMeta(String csvPath) throws IOException {
    File file = new File(csvPath);
    String meta = MetaService.createMeta(file, "UTF-8", LocalDateTime.now(), "facturation-htp-job");
    log.debug("meta: " + meta);
    writeToFile(csvPath + ".meta", meta);
  }

  private String prepareCSVFilling(
      List<BillingResult> billingResults,
      String csvDelemiter,
      LocalDate dateCalcul,
      String clientType) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(getHeader(csvDelemiter, clientType));
    for (BillingResult billingResult : billingResults) {
      stringBuilder.append(
          getContent(billingResult, csvDelemiter, dateCalcul.toString(), clientType));
    }
    return stringBuilder.toString();
  }

  void writeToFile(String filePath, String fileFilling) throws IOException {
    try (FileWriter fileWriter = new FileWriter(filePath, StandardCharsets.UTF_8)) {
      fileWriter.write(fileFilling);
    }
  }

  private String toCSVQuotedVariables(String delemiter, String... variables) {
    final StringJoiner stringJoiner = new StringJoiner(delemiter, "", System.lineSeparator());

    for (String variable : variables) {
      stringJoiner.add(QUOTE + variable + QUOTE);
    }

    return stringJoiner.toString();
  }

  private String getHeader(String csvDelemiter, String clientType) {
    if (com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER.equals(
        clientType)) {
      return toCSVQuotedVariables(
          csvDelemiter,
          "idDeclarant",
          "Société émettrice",
          "Nombre de bénéficiaires",
          "Mois de facturation");
    } else {
      return toCSVQuotedVariables(
          csvDelemiter, "idDeclarant", "Nombre de bénéficiaires", "Mois de facturation");
    }
  }

  private String getContent(
      BillingResult billingResult, String csvDelemiter, String dateCalcul, String clientType) {
    if (com.cegedim.next.serviceeligibility.core.utils.Constants.CLIENT_TYPE_INSURER.equals(
        clientType)) {
      return toCSVQuotedVariables(
          csvDelemiter,
          billingResult.getIdDeclarant(),
          billingResult.getGestionnaire(),
          Long.toString(billingResult.getCount()),
          dateCalcul);
    } else {
      return toCSVQuotedVariables(
          csvDelemiter,
          billingResult.getIdDeclarant(),
          Long.toString(billingResult.getCount()),
          dateCalcul);
    }
  }

  private void addCompteRenduValueForAmcList(
      CompteRenduFacturationHTP compteRenduFacturationHTP,
      String compteRenduValue,
      List<String> amcList) {
    for (String amc : amcList) {
      compteRenduFacturationHTP.addCompteRenduValue(compteRenduValue, amc);
    }
  }
}
