package com.cegedim.pages;

import com.cegedim.next.e2e.component.BaseWebComponent;
import com.cegedim.next.e2e.component.button.Button;
import com.cegedim.next.e2e.component.page.Page;
import com.cegedim.next.e2e.core.driver.NextWebDriver;
import com.cegedim.next.e2e.core.element.selector.Selector;
import com.cegedim.next.e2e.core.element.selector.impl.SelectorFactory;
import com.cegedim.next.e2e.core.report.scenario.ScenarioReport;

/**
 * The Class SearchForm.
 */
public class BeneficiaryRightTrackingPage extends Page<BeneficiaryRightTrackingPage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public BeneficiaryRightTrackingPage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

}
