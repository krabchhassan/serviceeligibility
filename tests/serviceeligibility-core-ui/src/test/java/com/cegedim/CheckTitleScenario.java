package com.cegedim;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.pages.HomePage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;

/**
 * Published service detailed test scenario.
 */
@E2EScenario(name = "Check title")
public class CheckTitleScenario extends Scenario {

    @Override
    public void doScenario() {
        AuthUtils.login(this);

        HomePage homePage = new HomePage(getWebDriver(), getScenarioReport());

        enterScenarioSection("Check page title");
        enterScenarioItem("Check home page title");

        homePage.getPageTitle().isVisible();

    }

}
