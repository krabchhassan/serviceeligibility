package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestServicePrestationService;
import com.cegedim.next.serviceeligibility.core.cucumber.services.TestTraceService;
import com.cegedim.next.serviceeligibility.core.cucumber.utils.RecursiveUtils;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceConsolidation;
import com.cegedim.next.serviceeligibility.core.job.batch.TraceExtractionConso;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.springframework.util.CollectionUtils;

@RequiredArgsConstructor
public class TraceStep {

  public final TestTraceService testTraceService;
  public final TestServicePrestationService testServicePrestationService;

  public List<TraceExtractionConso> traceExtractionConsoList;
  public List<TraceConsolidation> traceConsoList;

  //  ici "the trace" = "TraceConsolidation"
  @Then("^the trace at index (\\d+) has this values")
  public void theTraceAtIndex(Integer index, DataTable expected) throws Exception {
    TraceConsolidation trace = traceConsoList.get(index);
    Map<String, String> expectedTrace = expected.asMap(String.class, String.class);

    Assertions.assertEquals("620", trace.getBatch());

    if (expectedTrace.get("idDeclaration") != null) {
      Assertions.assertEquals(expectedTrace.get("idDeclaration"), trace.getIdDeclaration());
    }

    if (expectedTrace.get("idDeclarationConsolidee") != null) {
      Assertions.assertEquals(
          expectedTrace.get("idDeclarationConsolidee"), trace.getIdDeclarationConsolidee());
    }

    if (expectedTrace.get("idDeclarant") != null) {
      Assertions.assertEquals(expectedTrace.get("idDeclarant"), trace.getIdDeclarant());
    }

    if (expectedTrace.get("codeService") != null) {
      Assertions.assertEquals(expectedTrace.get("codeService"), trace.getCodeService());
    }

    if (expectedTrace.get("codeRejet") != null) {
      Assertions.assertEquals(expectedTrace.get("codeRejet"), trace.getCodeRejet());
    }

    if (expectedTrace.get("nomFichierARL") != null) {
      Assertions.assertEquals(expectedTrace.get("nomFichierARL"), trace.getNomFichierARL());
    }

    if (expectedTrace.get("codeClient") != null) {
      Assertions.assertEquals(expectedTrace.get("codeClient"), trace.getCodeClient());
    }

    if (expectedTrace.get("collectionConsolidee") != null) {
      Assertions.assertEquals(
          expectedTrace.get("collectionConsolidee"), trace.getCollectionConsolidee());
    }
  }

  @When("^I wait for (\\d+) traceConsolidation(?:s)?$")
  public void iWaitFortraceConsolidation(Integer number) throws Exception {
    Callable<List<TraceConsolidation>> call = testTraceService::getAllTracesConso;
    // appel sur le service autant de fois que le nombre de résultats soit égal à number
    Predicate<List<TraceConsolidation>> validation =
        response -> number == 0 && CollectionUtils.isEmpty(response) || number == response.size();
    traceConsoList = RecursiveUtils.defaultRecursive(call, validation);
  }

  @Then("^the extraction conso trace at index (\\d+) has this values")
  public void theTraceExtraAtIndex(Integer index, DataTable expected) throws Exception {
    Map<String, String> expectedTrace = expected.asMap();
    TraceExtractionConso trace = traceExtractionConsoList.get(index);

    Assertions.assertEquals("620", trace.getBatch());

    if (expectedTrace.get("idDeclaration") != null) {
      Assertions.assertEquals(expectedTrace.get("idDeclaration"), trace.getIdDeclaration());
    }

    if (expectedTrace.get("idDeclarationConsolidee") != null) {
      Assertions.assertEquals(
          expectedTrace.get("idDeclarationConsolidee"), trace.getIdDeclarationConsolidee());
    }

    if (expectedTrace.get("idDeclarant") != null) {
      Assertions.assertEquals(expectedTrace.get("idDeclarant"), trace.getIdDeclarant());
    }

    if (expectedTrace.get("codeService") != null) {
      Assertions.assertEquals(expectedTrace.get("codeService"), trace.getCodeService());
    }

    if (expectedTrace.get("codeRejet") != null) {
      Assertions.assertEquals(expectedTrace.get("codeRejet"), trace.getCodeRejet());
    }

    if (expectedTrace.get("nomFichierARL") != null) {
      Assertions.assertEquals(expectedTrace.get("nomFichierARL"), trace.getNomFichierARL());
    } else {
      Assertions.assertNotNull(trace.getNomFichierARL());
    }

    if (expectedTrace.get("codeClient") != null) {
      Assertions.assertEquals(expectedTrace.get("codeClient"), trace.getCodeClient());
    }

    if (expectedTrace.get("collectionConsolidee") != null) {
      Assertions.assertEquals(
          expectedTrace.get("collectionConsolidee"), trace.getCollectionConsolidee());
    }
  }

  @When("^I wait for (\\d+) traceExtraConsolidation(?:s)?$")
  public void iWaitFortraceExtraConsolidation(Integer number) throws Exception {
    Callable<List<TraceExtractionConso>> call = testTraceService::getAllTracesExtractionConso;
    // appel sur le service autant de fois que le nombre de résultats soit égal à number
    Predicate<List<TraceExtractionConso>> validation =
        response -> number == 0 && CollectionUtils.isEmpty(response) || number == response.size();
    traceExtractionConsoList = RecursiveUtils.defaultRecursive(call, validation);
  }
}
