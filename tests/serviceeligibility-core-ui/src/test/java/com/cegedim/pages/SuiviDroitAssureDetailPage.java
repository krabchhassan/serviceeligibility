package com.cegedim.pages;

import com.cegedim.next.e2e.component.BaseWebComponent;
import com.cegedim.next.e2e.component.button.Button;
import com.cegedim.next.e2e.component.link.Link;
import com.cegedim.next.e2e.component.page.Page;
import com.cegedim.next.e2e.core.driver.NextWebDriver;
import com.cegedim.next.e2e.core.element.selector.Selector;
import com.cegedim.next.e2e.core.element.selector.impl.SelectorFactory;
import com.cegedim.next.e2e.core.report.scenario.ScenarioReport;

public class SuiviDroitAssureDetailPage extends Page<SuiviDroitAssureDetailPage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public SuiviDroitAssureDetailPage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    // Titre de la page
    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Commentaire de panel
    public BaseWebComponent<?> getPanelComment(String panelId) {
        Selector xPath = SelectorFactory.xPath("//div[@id='" + panelId + "']//div[@class='cgd-comment']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de deplier tous les panels
    public Button getButtonExpandPanels() {
        Selector xPath = SelectorFactory.xPath("//button[@class='byd-bh-menu-item btn btn-outline-default'][1]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // détail du sous-panel
    public BaseWebComponent<?> getSousPanelInfo(String sousPanelId) {
        Selector xPath = SelectorFactory.xPath("//div[@id='" + sousPanelId + "']//div[2]/div");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Début de garantie
    public BaseWebComponent<?> getDateDebutGarantie(String codeGarantie) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//div[@id='right-item-" + codeGarantie
                                + "-rate']/../../div/div[@class='d-flex justify-item-center align-items-center pr-2']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // fin de garantie
    public BaseWebComponent<?> getDateFinGarantie(String codeGarantie) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//div[@id='right-item-" + codeGarantie
                                + "-rate']/../../div/div[@class='d-flex justify-item-center align-items-center']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant d'accéder aux prestations de la garantie
    public Button getButtonPrestations(String codeGarantie) {
        Selector xPath = SelectorFactory
                .xPath("//h4[@class='byd-ph-title'][contains(text(), '" + codeGarantie + "')]/../../..//button");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    public BaseWebComponent<?> getPrestationCode(int indicePrestation) {
        Selector xPath = SelectorFactory
                .xPath("//div[@class='modal-content']//tbody//tr[" + indicePrestation + "]//td[1]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    public BaseWebComponent<?> getPrestationFormule(int indicePrestation) {
        Selector xPath = SelectorFactory
                .xPath("//div[@class='modal-content']//tbody//tr[" + indicePrestation + "]//td[2]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    public BaseWebComponent<?> getPrestationParametre(int indicePrestation) {
        Selector xPath = SelectorFactory
                .xPath("//div[@class='modal-content']//tbody//tr[" + indicePrestation + "]//td[3]//div[@class='col']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de fermer les prestations de la garantie
    public Button getButtonClosePrestations() {
        Selector xPath = SelectorFactory.xPath("//div[@class='modal-content']//button[@class='btn btn-secondary']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    public Link getUrlRetour() {
        Selector xPath = SelectorFactory.xPath("//li[@class='breadcrumb-item']/a");
        return new Link(getWebDriver(), xPath, getScenarioReport());
    }

}
