package com.cegedim.pages;

import com.cegedim.next.e2e.component.BaseWebComponent;
import com.cegedim.next.e2e.component.button.Button;
import com.cegedim.next.e2e.component.combo.ComboField;
import com.cegedim.next.e2e.component.page.Page;
import com.cegedim.next.e2e.component.text.TextField;
import com.cegedim.next.e2e.core.driver.NextWebDriver;
import com.cegedim.next.e2e.core.element.selector.Selector;
import com.cegedim.next.e2e.core.element.selector.impl.SelectorFactory;
import com.cegedim.next.e2e.core.report.scenario.ScenarioReport;

/**
 * The Class SearchForm.
 */
public class AmcSearchPage extends Page<AmcSearchPage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public AmcSearchPage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    //
    // BOUTONS - RECHERCHE/CREATION
    //
    // Bouton permettant la recherche d'AMC
    public Button getButtonSearch() {
        Selector selector = SelectorFactory.id("search-customer");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de reinitialiser la recherche
    public Button getButtonClearSearch() {
        Selector selector = SelectorFactory.id("clear-search-customer");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant la création d'une nouvelle AMC
    public Button getButtonCreateAMC() {
        Selector xPath = SelectorFactory
                .xPath("//button[@class=\"btn btn-secondary\"][contains(text(), 'Créer une nouvelle AMC')]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant d'ouvrir une AMC du résultat de recherche
    public Button getButtonUpdateAMC(String numeroAMC) {
        Selector selector = SelectorFactory.id(numeroAMC + "-header-ph-action-open");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant d'ouvrir un AMC recherché
    public Button getButtonOpenAMC(String numeroAMC) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//h4[@class='byd-ph-title'][contains(text(), 'Résultat(s) de recherche')]/../ancestor::div[@class='byd-panel byd-panel-theme-main byd-panel-border byd-panel-header-border byd-panel-separators']//button[@id='"
                                + numeroAMC + "-header-ph-action-open']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    //
    // ITEMS - RECHERCHE
    //

    // Item pour la saisie de recherche par n° d'AMC
    public ComboField getComboSearchNumeroAMC() {
        Selector selector = SelectorFactory
                .xPath("//label[contains(text(), 'AMC')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Libellé Court
    public ComboField getComboSearchLibelleCourt() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Libellé court')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Couloir - homeSearchForm-couloir
    public TextField getItemSearchCouloir() {
        Selector selector = SelectorFactory.id("homeSearchForm-couloir");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Service
    public ComboField getComboSearchService() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Service')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    //
    // ITEMS - RESULTAT
    //

    // Nb de résultat de la recherche
    public BaseWebComponent<?> getNbResultSearch() {
        String xPath = "//h4[@class=\"byd-ph-title\"][contains(text(), 'Résultat(s) de recherche')]/../../../../../div[@class=\"byd-panel-header-container\"]/div/div/span";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    // Critères de recherche et sa valeur
    public BaseWebComponent<?> getCritereRecherche(int indiceCritere) {
        String xPath = "//h4[@class=\"byd-ph-title\"][contains(text(), 'Résultat(s) de recherche')]/../../../../../div[2]/div/div/div["
                + indiceCritere + "]/span";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    // Valeur du 1er critère de recherche
    public BaseWebComponent<?> getValeurRecherche(int indiceCritere) {
        String xPath = "//h4[@class=\"byd-ph-title\"][contains(text(), 'Résultat(s) de recherche')]/../../../../../div[2]/div/div/div["
                + indiceCritere + "]/span[2]";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    // Résultat du 1er AMC
    public BaseWebComponent<?> getValeurPremierAMC() {
        String xPath = "//div[@class=\"row\"]/div[@class=\"col-12 col-lg-6\"][2]/div//div[@class=\"byd-panel-section\"][2]/div/div/div/div/div/div/div[2]/h4";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }
}
