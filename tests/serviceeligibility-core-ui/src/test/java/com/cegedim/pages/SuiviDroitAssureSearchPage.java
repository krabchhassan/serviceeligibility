package com.cegedim.pages;

import com.cegedim.next.e2e.component.BaseWebComponent;
import com.cegedim.next.e2e.component.button.Button;
import com.cegedim.next.e2e.component.combo.ComboField;
import com.cegedim.next.e2e.component.date.DateField;
import com.cegedim.next.e2e.component.page.Page;
import com.cegedim.next.e2e.component.text.TextField;
import com.cegedim.next.e2e.core.driver.NextWebDriver;
import com.cegedim.next.e2e.core.element.selector.Selector;
import com.cegedim.next.e2e.core.element.selector.impl.SelectorFactory;
import com.cegedim.next.e2e.core.report.scenario.ScenarioReport;

public class SuiviDroitAssureSearchPage extends Page<SuiviDroitAssureSearchPage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public SuiviDroitAssureSearchPage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    // Titre de la page
    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    //
    // Boutons de recherche
    //
    public Button getButtonSearch() {
        Selector selector = SelectorFactory.id("submit-search-beneficiary-rights");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de reinitialiser la recherche
    public Button getButtonClearSearch() {
        Selector selector = SelectorFactory.id("clear-search-beneficiary-rights");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant d'ouvrir le résultat de recherche
    public Button getButtonOpenAssure(int indexAssure) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//h4[@class='byd-ph-title'][contains(text(), 'Résultats de recherche')]/ancestor::div[5]//div[@class='byd-panel-section'][2]//div[@class='byd-panel-wrapper']["
                                + indexAssure + "]//div[@class='byd-ph-actions']/button");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    //
    // ITEMS - RECHERCHE
    //

    // Item pour la saisie de recherche par n° d'AMC
    public ComboField getComboSearchNumeroAMC() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Nom ou numéro AMC')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Numéro AMC Echanges
    public TextField getItemSearchNumeroAMCEchange() {
        Selector selector = SelectorFactory.id("beneficiary-rights-search-form-amc-number-exchange");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Numéro Adherent
    public TextField getItemSearchNumeroAdherent() {
        Selector selector = SelectorFactory.id("beneficiary-rights-search-form-member-number");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Numéro Contrat
    public TextField getItemSearchNumeroContrat() {
        Selector selector = SelectorFactory.id("beneficiary-rights-search-form-contract-number");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Numéro Contrat
    public TextField getItemSearchNIR() {
        Selector selector = SelectorFactory.id("beneficiary-rights-search-form-ro-number");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Input pour la saisie de recherche par date de naissance
    public DateField getItemSearchDateNaissance() {
        Selector xPath = SelectorFactory.xPath("//div[@name='dateNaissance']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Rang gemellaire
    public TextField getItemSearchRang() {
        Selector selector = SelectorFactory.id("beneficiary-rights-search-form-rank-number");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    //
    // Résultat de recherche
    //
    public BaseWebComponent<?> getNbResultSearch() {
        String xPath = "//h4[@class='byd-ph-title'][contains(text(), 'Résultats de recherche')]/../../../../../div[@class='byd-panel-header-container']/div/div/span";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    public BaseWebComponent<?> getResultSearch(String critereRecherche) {
        String xPath = "//div[@id='search-criteria-" + critereRecherche + "']";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    public BaseWebComponent<?> getValueResultSearch(String critereRecherche) {
        String xPath = "//div[@id='search-criteria-" + critereRecherche + "']/div[2]";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

}
