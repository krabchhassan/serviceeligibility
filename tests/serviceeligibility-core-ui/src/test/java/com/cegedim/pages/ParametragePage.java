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

public class ParametragePage extends Page<ParametragePage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public ParametragePage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    // Titre de la page
    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de deplier tous les panels
    public Button getButtonExpandPanels() {
        Selector xPath = SelectorFactory.xPath("//button[@class='byd-bh-menu-item btn btn-outline-default'][1]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Titre de panel
    public BaseWebComponent<?> getPanelTitle(int indicePanel) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(//div[@class='byd-page-layout-content-wrapper']//div[@class='byd-ph-main byd-ph-collapsible'])["
                                + indicePanel + "]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Titre du sous panel
    public BaseWebComponent<?> getUnderPanelTitle(int indicePanel) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(//div[@class='byd-page-layout-content-wrapper']//div[@class='collapse show'])[" + indicePanel
                                + "]//h4");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Titre de colonne
    public BaseWebComponent<?> getColumnTitle(int indicePanel, int indiceColonne) {
        Selector xPath = SelectorFactory
                .xPath(
                        "((//div[@class='byd-page-layout-content-wrapper']//div[@class='collapse show'])[" + indicePanel
                                + "]//div[@class='rt-resizable-header-content'])[" + indiceColonne + "]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Colonne
    public BaseWebComponent<?> getColumn(int indicePanel, int indiceLigne, int indiceColonne) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(((//div[@class='byd-page-layout-content-wrapper']//div[@class='rt-tbody'])[" + indicePanel
                                + "]//div[@role='row'])[" + indiceLigne + "]//div[@role='gridcell'])[" + indiceColonne
                                + "]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton de pagination
    public Button getButtonPaginationPanel(int indicePanel, String texteBouton) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(//div[@class='byd-page-layout-content-wrapper']//div[@class='pagination-bottom'])["
                                + indicePanel + "]//button[contains(text(), '" + texteBouton + "')]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Param activé - Case à coché activée
    public BaseWebComponent<?> getParamActivated(int indicePanel, int indiceLigne, int indiceColonne) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(((//div[@class='byd-page-layout-content-wrapper']//div[@class='rt-tbody'])[" + indicePanel
                                + "]//div[@role='row'])[" + indiceLigne + "]//div[@role='gridcell'])[" + indiceColonne
                                + "]//input[@checked]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de d'agir sur le paramétrage
    public Button getButtonActionParam(int indicePanel, int indiceLigne, int indiceColonne, int indiceBouton) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(((//div[@class='byd-page-layout-content-wrapper']//div[@class='rt-tbody'])[" + indicePanel
                                + "]//div[@role='row'])[" + indiceLigne + "]//div[@role='gridcell'])[" + indiceColonne
                                + "]//button[" + indiceBouton + "]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de d'agir sur le paramétrage
    public Button getCellule(int indicePanel, int indiceLigne, int indiceColonne) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(((//div[@class='byd-page-layout-content-wrapper']//div[@class='rt-tbody'])[" + indicePanel
                                + "]//div[@role='row'])[" + indiceLigne + "]//div[@role='gridcell'])[" + indiceColonne
                                + "]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Message d'alerte Suppression
    public BaseWebComponent<?> getMessageAlerte() {
        Selector xPath = SelectorFactory.xPath("//div[@class='alert-msg']/div");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de supprimer le paramétrage
    public Button getButtonCloseAlert() {
        Selector xPath = SelectorFactory.xPath("//div[@class='alert-danger alert']/button");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de confirmer la suppression de paramétrage
    public Button getButtonConfirmDeleteParam() {
        Selector xPath = SelectorFactory
                .xPath("//div[@class='modal-dialog byd-confirm-modal']//button[@class='ml-auto btn btn-primary']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de créer un nouveau transcodage
    public Button getButtonCreateTranscodage() {
        Selector xPath = SelectorFactory
                .xPath("//div[@id='panel-transcoding-objects-section-0']//button[@id='ph-action-edit']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie Code objet de transcodage
    public TextField getItemCodeTranscodage() {
        Selector selector = SelectorFactory.id("ParamTranscoding-codeObjetTransco");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie du nom objet de transcodage
    public TextField getItemNomObjetTranscodage() {
        Selector selector = SelectorFactory.id("ParamTranscoding-nomObjetTransco");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie des colonnes de l'objet de transcodage
    public TextField getItemColonnesObjetTranscodage() {
        Selector selector = SelectorFactory.id("ParamTranscoding-colNames");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Bouton permettant de confirmer la création de paramétrage
    public Button getButtonConfirmCreateTransco() {
        Selector xPath = SelectorFactory
                .xPath(
                        "//div[@class='ParamObjetTransco__transco-object-line-actions']/button[@class='btn btn-primary']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de créer un nouveau service
    public Button getButtonCreateService() {
        Selector selector = SelectorFactory.id("ph-action-addService");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Item pour la saisie du Code service
    public TextField getItemCodeService() {
        Selector selector = SelectorFactory.id("ParamServiceTransco-serviceCode");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Combo sélection objetsService
    public ComboField getComboObjetService(boolean isVide) {
        Selector selector = null;
        if (isVide) {
            // selector = SelectorFactory.xPath("//div[@class='Select
            // form-control Select--multi']");
            selector = SelectorFactory.xPath("//div[@class='Select-control']/..");
        }
        else {
            // selector = SelectorFactory.xPath("//div[@class='Select
            // form-control has-value Select--multi']");
            selector = SelectorFactory.xPath("//div[@class='Select-control']/..");
        }
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Bouton permettant de confirmer la création/modification de service
    public Button getButtonConfirmActionService() {
        Selector xPath = SelectorFactory
                .xPath("//div[@class='ParamServiceTransco__service-line-actions']/button[@class='btn btn-primary']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant d'enlever un choix d'une combo multiple
    public Button getButtonSupprimeChoixCombo(int indicePanel, int indiceLigne, int indiceColonne, int indiceChoix) {
        Selector xPath = SelectorFactory
                .xPath(
                        "((((//div[@class='byd-page-layout-content-wrapper']//div[@class='rt-tbody'])[" + indicePanel
                                + "]//div[@role='row'])[" + indiceLigne + "]//div[@role='gridcell'])[" + indiceColonne
                                + "]//span[@class='Select-value-icon'])[" + indiceChoix + "]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Message d'alerte Suppression
    public BaseWebComponent<?> getContenuDetailRejet(int indiceInfoRejet) {
        Selector xPath = SelectorFactory
                .xPath(
                        "(((//div[@class='byd-page-layout-content-wrapper']//div[@class='rt-tbody'])[9]//div[@role='rowgroup'])[2]//div[@class='cgd-comment mr-2'])["
                                + indiceInfoRejet + "]/../../div[@class='col-9']/div");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

}
