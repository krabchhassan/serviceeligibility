package com.cegedim.pages;

import com.cegedim.next.e2e.component.BaseWebComponent;
import com.cegedim.next.e2e.component.button.Button;
import com.cegedim.next.e2e.component.page.Page;
import com.cegedim.next.e2e.component.text.TextField;
import com.cegedim.next.e2e.core.driver.NextWebDriver;
import com.cegedim.next.e2e.core.element.selector.Selector;
import com.cegedim.next.e2e.core.element.selector.impl.SelectorFactory;
import com.cegedim.next.e2e.core.report.scenario.ScenarioReport;

public class VolumetriePage extends Page<VolumetriePage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public VolumetriePage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    // Titre de la page
    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie de recherche par AMC
    public TextField getSearchAMC() {
        Selector selector = SelectorFactory.id("volumetryForm-amc");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par partenaire
    public TextField getSearchPartenaire() {
        Selector selector = SelectorFactory.id("volumetryForm-codePartenaire");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Bouton de recherche
    public Button getButtonRechercher() {
        Selector selector = SelectorFactory.id("volumetry-search-submit");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Contenu de la cellule de résultat
    public BaseWebComponent<?> getContenuCelluleResultat(int numLigne, int numColonne) {
        Selector xPath = SelectorFactory
                .xPath("//div[@class='rt-tr-group'][" + numLigne + "]//div[@role='gridcell'][" + numColonne + "]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Contenu de la cellule de total
    public BaseWebComponent<?> getContenuCelluleTotal(int numColonne) {
        Selector xPath = SelectorFactory.xPath("//div[@class='rt-tfoot']//div[@role='gridcell'][" + numColonne + "]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    public Button getButtonTri(String nomColonne) {
        Selector xPath = SelectorFactory
                .xPath("//div[@class='rt-resizable-header-content'][contains(text(), '" + nomColonne + "')]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

}
