package com.cegedim.utils;

import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.pages.HomePage;

/**
 * The Class AuthUtils.
 */
public class AuthUtils {
    private AuthUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Login.
     *
     * @param scenario
     *            the scenario
     */
    public static void login(Scenario scenario) {
        HomePage searchForm = new HomePage(scenario.getWebDriver(), scenario.getScenarioReport());
        scenario.enterScenarioSection("Authentication");
        scenario.enterScenarioItem("Authentication with " + ConfigurationUtils.getAuthUser() + " user");
        searchForm.login(ConfigurationUtils.getAuthUser(), ConfigurationUtils.getAuthPass());
    }
}
