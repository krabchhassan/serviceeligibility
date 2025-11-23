package com.cegedim.next.serviceeligibility.core.cucumber.steps;

import com.cegedim.next.serviceeligibility.core.cucumber.services.TestCommonStoreService;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Value;

@RequiredArgsConstructor
@Slf4j
public class CommonStep {

  private final TestCommonStoreService testCommonStoreService;

  private static boolean skipTest = false;

  @Value("${FAIL_FAST:true}")
  private boolean failFast;

  @After
  public void after(Scenario scen) {
    TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
    if (scen.isFailed()) {
      log.error("failed scenario : {}", scen.getName());
      skipTest = true;
    }
  }

  @Before
  public void beforeEach(Scenario scen) throws InterruptedException {
    log.info("before each : {}", scen.getName());
    if (failFast && skipTest) {
      Assumptions.assumeTrue(false, "Un test a planté");
    } else {
      Assertions.assertTrue(testCommonStoreService.dropCollections());
    }
  }

  @Given("I change GMT TimeZone")
  public void ichangeGmtTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone("Europe/Paris"));
  }

  @Given("^I wait \"(.*)\" seconds in order to consume the data$")
  public void waitSecondsInOrderToConsumeTheData(int seconds) throws InterruptedException {
    TimeUnit.SECONDS.sleep(seconds);
  }
}
