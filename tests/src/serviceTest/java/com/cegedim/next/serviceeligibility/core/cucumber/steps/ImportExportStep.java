package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.bdd.backend.dto.importexport.RestImportDto;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestImportExportService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.FileUtils;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.TryToUtils;
import io.cucumber.java.en.Given;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImportExportStep {

  private final TestImportExportService testImportExportService;

  @Given("^I (try to )?import (an empty|a complete) file( with invalid data)?( for parametrage)?")
  public void iTryToImportFileForParametrage(
      Boolean tryTo, String fileCharacteristic, Boolean withInvalidData, Boolean forParam) {
    if (forParam == null) {
      importFile(fileCharacteristic, tryTo, withInvalidData, "importExport/nominalImport.json");
    } else {
      importFile(
          fileCharacteristic, tryTo, withInvalidData, "importExport/nominalImportParametrage.json");
    }
  }

  @Given("I import the file {string} for parametrage")
  public void importSpecificFileForParametrage(String filePath) {
    importFile("", false, null, "importExport/" + filePath + ".json");
  }

  private void importFile(
      String fileCharacteristic, Boolean tryTo, Boolean withInvalidData, String filePath) {
    if (fileCharacteristic.equals("an empty")) {
      TryToUtils.tryTo(
          () ->
              testImportExportService.importAll(
                  FileUtils.readRequestFile("importExport/emptyImport.json", RestImportDto.class)),
          tryTo);
    } else if (fileCharacteristic.equals("a complete")) {
      if (withInvalidData != null) {
        TryToUtils.tryTo(
            () ->
                testImportExportService.importAll(
                    FileUtils.readRequestFile(
                        "importExport/notValidDataImport.json", RestImportDto.class)),
            tryTo);
      } else {
        TryToUtils.tryTo(
            () ->
                testImportExportService.importAll(
                    FileUtils.readRequestFile(filePath, RestImportDto.class)),
            tryTo);
      }
    } else {
      TryToUtils.tryTo(
          () ->
              testImportExportService.importAll(
                  FileUtils.readRequestFile(filePath, RestImportDto.class)),
          tryTo);
    }
  }
}
