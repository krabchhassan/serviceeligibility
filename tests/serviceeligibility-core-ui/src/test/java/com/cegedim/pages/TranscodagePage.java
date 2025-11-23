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

public class TranscodagePage extends Page<TranscodagePage> {

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public TranscodagePage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    // Titre de la page
    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Combo pour la sélection du service
    public ComboField getComboService() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Service')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Combo pour la sélection de l'objet
    public ComboField getComboObjet() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Objet')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Label transcodée
    public BaseWebComponent<?> getColTableauTransco(int numLigne, int numColonne) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//table[@class='table table-striped table-sm table-bordered my-1']//tbody/tr[" + numLigne
                                + "]/td[" + numColonne + "]");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de supprimer le transcodage d'une ligne
    public Button getButtonDeleteTransco(int numLigne) {
        Selector xPath = SelectorFactory
                .xPath("(//button[@class='btn btn-outline-default btn-outline-no-border'])[" + numLigne + "]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de supprimer le transcodage liee à une valeur
    // transcodée
    public Button getButtonDeleteViaValeurTransco(String valeurTranscodee) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//table[@class='table table-striped table-sm table-bordered my-1']//tbody//tr/td[contains(text(), '"
                                + valeurTranscodee
                                + "')]/..//button[@class='btn btn-outline-default btn-outline-no-border']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant d'ajouter un transcodage
    public Button getButtonAdd() {
        Selector selector = SelectorFactory.id("ph-action-edit");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de valider le transcodage
    public Button getButtonValidateTransco() {
        Selector xPath = SelectorFactory.xPath("//button[@class='btn btn-primary']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de valider le transcodage
    public Button getButtonConfirmDelete() {
        Selector xPath = SelectorFactory.xPath("//button[@class='ml-auto btn btn-primary']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie d'une valeur à transcoder
    public TextField getValeurATranscoder(String itemName) {
        Selector selector = SelectorFactory.id("Transcoding-cle." + itemName);
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie d'une valeur transcodée
    public TextField getValeurTranscodee() {
        Selector selector = SelectorFactory.id("Transcoding-codeTransco");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

}
