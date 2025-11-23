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

public class SuiviFluxPage extends Page<SuiviFluxPage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public SuiviFluxPage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    // Titre de la page
    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Input pour la saisie de la date de but de sélection
    public DateField getSearchDateDebut() {
        Selector xPath = SelectorFactory.xPath("//div[@name='dateDebut']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Bouton permettant de d'effacer la date de début
    public Button getButtonClearDateDebut() {
        Selector xPath = SelectorFactory.xPath("//div[@name='dateDebut']//div[2]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Input pour la saisie de la date de but de sélection
    public DateField getSearchDateFin() {
        Selector xPath = SelectorFactory.xPath("//div[@name='dateFin']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Bouton permettant de d'effacer la date de fin
    public Button getButtonClearDateFin() {
        Selector xPath = SelectorFactory.xPath("//div[@name='dateFin']//div[2]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie de recherche par n° d'AMC
    public ComboField getSearchNumeroAMC() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'N° AMC')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    public Button getButtonClearCombo(String libelleCombo) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//label[contains(text(), '" + libelleCombo
                                + "')]/../following-sibling::div//div[contains(@class,  'Select ')]//span[@class='Select-clear-zone']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie de recherche par Processus
    public ComboField getSearchProcessus() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Processus')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Partenaire
    public TextField getSearchPartenaire() {
        Selector selector = SelectorFactory.id("file-tracking-search-partner");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Emetteur
    public ComboField getSearchEmetteur() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Emetteur')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par Nom d'AMC
    public ComboField getSearchNomAMC() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Nom AMC')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par N° de fichier
    public TextField getSearchNumeroFichier() {
        Selector selector = SelectorFactory.id("file-tracking-search-file-number");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par type de fichier
    public ComboField getSearchTypeFichier() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Type de fichier')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par circuit
    public ComboField getSearchCircuit() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Circuit')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie de recherche par nom de fichier
    public TextField getSearchNomFichier() {
        Selector selector = SelectorFactory.id("file-tracking-search-file-name");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Bouton permettant d'afficher plus de critères de recherche
    public Button getButtonMoreLessCriteria() {
        Selector selector = SelectorFactory.id("file-tracking-search-search-more-less-criteria");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de lancer la rechercher
    public Button getButtonSearch() {
        Selector selector = SelectorFactory.id("file-tracking-search-search-submit");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Nb de résultat de la recherche - Fichiers recus
    public BaseWebComponent<?> getFichiersRecusNbResultSearch() {
        String xPath = "//div[@class='byd-panel-wrapper flex-auto w-100'][1]//h6";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    // Nb de résultat de la recherche - Fichiers emis
    public BaseWebComponent<?> getFichiersEmisNbResultSearch() {
        String xPath = "//div[@class='byd-panel-wrapper flex-auto w-100'][2]//h6";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    public Button getButtonExpandFichierRecu(int ligneFichierRecu) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//div[@class='byd-panel-wrapper flex-auto w-100'][1]//div[@class='rt-td  rt-expandable']["
                                + ligneFichierRecu + "]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    public Button getButtonExpandFichierEmis(int ligneFichierRecu) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//div[@class='byd-panel-wrapper flex-auto w-100'][2]//div[@class='rt-td  rt-expandable']["
                                + ligneFichierRecu + "]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    public BaseWebComponent<?> getContenuCelluleFichierRecu(int ligne, int colonne) {
        String xPath = "//div[@class='byd-panel-wrapper flex-auto w-100'][1]//div[@role='rowgroup'][" + ligne
                + "]//div[@role='gridcell'][" + colonne + "]";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    public BaseWebComponent<?> getContenuCelluleFichierEmis(int ligne, int colonne) {
        String xPath = "//div[@class='byd-panel-wrapper flex-auto w-100'][2]//div[@role='rowgroup'][" + ligne
                + "]//div[@role='gridcell'][" + colonne + "]";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    public BaseWebComponent<?> getDetailInfoFichierRecu(int ligne, String info) {
        String xPath = "//div[@class='byd-panel-wrapper flex-auto w-100'][1]//div[@role='rowgroup'][" + ligne
                + "]//div[@class='byd-panel-body-container']//div[@class='cgd-comment mr-2'][contains(text(), '" + info
                + "')]/../../div[@class='col-6']";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

    public BaseWebComponent<?> getDetailInfoFichierEmis(int ligne, String info) {
        String xPath = "//div[@class='byd-panel-wrapper flex-auto w-100'][2]//div[@role='rowgroup'][" + ligne
                + "]//div[@class='byd-panel-body-container']//div[@class='cgd-comment mr-2'][contains(text(), '" + info
                + "')]/../../div[@class='col-6']";
        return new BaseWebComponent<>(getWebDriver(), SelectorFactory.xPath(xPath), getScenarioReport());
    }

}
