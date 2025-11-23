package com.cegedim.pages;

import com.cegedim.next.e2e.component.BaseWebComponent;
import com.cegedim.next.e2e.component.button.Button;
import com.cegedim.next.e2e.component.date.DateField;
import com.cegedim.next.e2e.component.link.Link;
import com.cegedim.next.e2e.component.page.Page;
import com.cegedim.next.e2e.component.radio.RadioGroupField;
import com.cegedim.next.e2e.component.text.TextField;
import com.cegedim.next.e2e.core.driver.NextWebDriver;
import com.cegedim.next.e2e.core.element.selector.Selector;
import com.cegedim.next.e2e.core.element.selector.impl.SelectorFactory;
import com.cegedim.next.e2e.core.report.scenario.ScenarioReport;

/**
 * The Class SearchForm.
 */
public class AmcConsultPage extends Page<AmcConsultPage> {

    public static final String INPUT_NAME_PILOTAGES = "//input[@name='pilotages[";

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public AmcConsultPage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    public BaseWebComponent<?> getContenu(String label) {
        Selector xPath = SelectorFactory.xPath("//div[contains(text(), '" + label + "')]/../..//div[@class='col-6']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    public BaseWebComponent<?> getStatutService(String service) {
        Selector xPath = SelectorFactory
                .xPath(
                        "//h4[@class='byd-ph-title'][contains(text(), '" + service
                                + "')]/../..//div[@class='byd-status-text byd-status-item']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie du libellé long
    public TextField getLibLongAMC() {
        Selector selector = SelectorFactory.id("declarantForm-Edit-libelle");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Bouton permettant de modifier l'AMC
    public Button getButtonUpdate() {
        Selector selector = SelectorFactory.id("ph-action-edit");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de dupliquer l'AMC
    public Button getButtonDuplicate() {
        Selector selector = SelectorFactory.id("ph-action-duplicate");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de dupliquer l'AMC
    public Button getButtonValidate() {
        Selector selector = SelectorFactory.id("submit-declarant-form");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Case à cocher permettant d'activer les cartes dematérialisés
    public RadioGroupField getRadioActiveService(int indexService) {
        Selector xPath = SelectorFactory.xPath(INPUT_NAME_PILOTAGES + indexService + "].serviceOuvert']/..");
        return new RadioGroupField(getWebDriver(), xPath, null, getScenarioReport());
    }

    public RadioGroupField getRadioDesactiveService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(
                        INPUT_NAME_PILOTAGES + indexService
                                + "].serviceOuvert']/../..//div[@class='form-check'][2]");
        return new RadioGroupField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de période d'ouverture du service
    public DateField getItemPeriodeOuvertureService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath("//div[@name='pilotages[" + indexService + "].regroupements[0].dateOuverture']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de période de synchronisation du service
    public DateField getItemPeriodeSynchroService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath("//div[@name='pilotages[" + indexService + "].regroupements[0].dateSynchronisation']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du couloir client du service
    public TextField getItemCouloirClientService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].couloirClient']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du libellé court
    public TextField getLibCourtAMC() {
        Selector selector = SelectorFactory.id("declarantForm--nom");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Bouton permettant de modifier l'AMC
    public Button getPanelService(String nomService) {
        Selector xPath = SelectorFactory.xPath("//h4[@class='byd-ph-title'][contains(text(), '" + nomService + "')]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Url de retour au module de recherche
    public Link getUrlRetour() {
        Selector xPath = SelectorFactory.xPath("//li[@class='breadcrumb-item']/a");
        return new Link(getWebDriver(), xPath, getScenarioReport());
    }

}
