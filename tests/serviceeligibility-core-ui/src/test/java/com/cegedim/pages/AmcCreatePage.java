package com.cegedim.pages;

import com.cegedim.next.e2e.component.BaseWebComponent;
import com.cegedim.next.e2e.component.button.Button;
import com.cegedim.next.e2e.component.combo.ComboField;
import com.cegedim.next.e2e.component.date.DateField;
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
public class AmcCreatePage extends Page<AmcCreatePage> {

    public static final String INPUT_ID_DECLARANT_FORM = "//input[@id='declarantForm-";
    public static final String INPUT_NAME_PILOTAGES = "//input[@name='pilotages[";
    public static final String DIV_NAME_PILOTAGES = "//div[@name='pilotages[";
    public static final String DECLARANT_FORM = "declarantForm-";

    /**
     * Constructor.
     *
     * @param webDriver
     *            the web driver
     * @param scenarioReport
     *            the scenario report
     */
    public AmcCreatePage(NextWebDriver webDriver, ScenarioReport scenarioReport) {
        super(webDriver, scenarioReport);
    }

    public BaseWebComponent<?> getPageTitle() {
        Selector xPath = SelectorFactory.xPath("//h1[@class='byd-bh-title']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    //
    // BOUTONS - RECHERCHE/CREATION
    //
    // Bouton permettant d'annuler la création
    public Button getButtonCancel() {
        Selector selector = SelectorFactory.id("cancel-form");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de valider la création
    public Button getButtonSubmit() {
        Selector selector = SelectorFactory.id("submit-declarant-form");
        return new Button(getWebDriver(), selector, getScenarioReport());
    }

    // Bouton permettant de confirmer l'annulation de la création
    public Button getButtonConfirmeAnnulation() {
        Selector xPath = SelectorFactory.xPath("//button[@class='btn btn-outline-default']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Bouton permettant de rester sur la page de création
    public Button getButtonResterPageCreation() {
        Selector xPath = SelectorFactory.xPath("//button[@class='ml-auto btn btn-primary']");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    //
    // ITEMS - CREATION
    //

    // Item pour la saisie du n° d'AMC
    public TextField getItemNumeroAMC(String mode) {
        Selector selector = SelectorFactory.id(DECLARANT_FORM + mode + "-numero");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Label informant que le n° d'AMC est requis
    public BaseWebComponent<?> getMsgValidationNumAMC(String mode) {
        Selector xPath = SelectorFactory
                .xPath(
                        INPUT_ID_DECLARANT_FORM + mode
                                + "-numero']/ancestor::div[@class='mb-2 has-danger form-group']//div[@class='mt-0 invalid-feedback']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie du libellé court de l'AMC
    public TextField getItemLibCourt(String mode) {
        Selector selector = SelectorFactory.id(DECLARANT_FORM + mode + "-nom");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Label informant que le libellé court est requis
    public BaseWebComponent<?> getMsgValidationLibCourt(String mode) {
        Selector xPath = SelectorFactory
                .xPath(
                        INPUT_ID_DECLARANT_FORM + mode
                                + "-nom']/ancestor::div[@class='mb-2 has-danger form-group']//div[@class='mt-0 invalid-feedback']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie du libellé long de l'AMC
    public TextField getItemLibLong(String mode) {
        Selector selector = SelectorFactory.id(DECLARANT_FORM + mode + "-libelle");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie du partenaire de l'AMC
    public TextField getItemCodePartenaire(String mode) {
        Selector selector = SelectorFactory.id(DECLARANT_FORM + mode + "-codePartenaire");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Label informant que le code partenaire est requis
    public BaseWebComponent<?> getMsgValidationCodePartenaire(String mode) {
        Selector xPath = SelectorFactory
                .xPath(
                        INPUT_ID_DECLARANT_FORM + mode
                                + "-codePartenaire']/ancestor::div[@class='mb-2 has-danger form-group']//div[@class='mt-0 invalid-feedback']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Item pour la saisie de l'emetteur de Droits de l'AMC
    public TextField getItemEmetteurDroits(String mode) {
        Selector selector = SelectorFactory.id(DECLARANT_FORM + mode + "-emetteurDroits");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Item pour la saisie du SIRET de l'AMC
    public TextField getItemSiret(String mode) {
        Selector selector = SelectorFactory.id(DECLARANT_FORM + mode + "-siret");
        return new TextField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Combo pour la saisie du circuit de l'AMC
    public ComboField getComboCircuit() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Code circuit')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Label informant que le circuit est requis
    public BaseWebComponent<?> getMsgValidationCircuitAMC() {
        Selector xPath = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Code circuit')]/../following-sibling::div//div[contains(@class,  'Select ')]/ancestor::div[@class='mb-2 has-danger form-group']//div[@class='mt-0 invalid-feedback']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // Combo pour la saisie de l'opérateur principal de l'AMC
    public ComboField getComboOperateurPrincipal() {
        Selector selector = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Opérateur principal')]/../following-sibling::div//div[contains(@class,  'Select ')]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // Label informant que le circuit est requis
    public BaseWebComponent<?> getMsgValidationOperateurPrincipal() {
        Selector xPath = SelectorFactory
                .xPath(
                        "//label[contains(text(), 'Opérateur principal')]/../following-sibling::div//div[contains(@class,  'Select ')]/ancestor::div[@class='mb-2 has-danger form-group']//div[@class='mt-0 invalid-feedback']");
        return new BaseWebComponent<>(getWebDriver(), xPath, getScenarioReport());
    }

    // RadioGroup pour l'activation du service de l'AMC
    public RadioGroupField getRadioActiveService(int indexService) {
        Selector xPath = SelectorFactory.xPath(INPUT_NAME_PILOTAGES + indexService + "].serviceOuvert']/..");
        return new RadioGroupField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de période d'ouverture du service
    public DateField getItemPeriodeOuvertureService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(DIV_NAME_PILOTAGES + indexService + "].regroupements[0].dateOuverture']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de période de synchronisation du service
    public DateField getItemPeriodeSynchroService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(DIV_NAME_PILOTAGES + indexService + "].regroupements[0].dateSynchronisation']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du couloir client du service
    public TextField getItemCouloirClientService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].couloirClient']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // RadioGroup pour la génération de fichier
    public RadioGroupField getRadioActiveFichierService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].generateFichier']/..");
        return new RadioGroupField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du N° de fichier début
    public TextField getItemNumDebutFichierService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].numDebutFichier']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du N° d'emetteur
    public TextField getItemNumEmetteurService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].numEmetteur']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de la version de norme
    public TextField getItemVersionNormeService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].versionNorme']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de la version de norme
    public TextField getItemCodeClientService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].codeClient']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du code comptable du service
    public TextField getItemCodeComptableService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].codeComptable']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Combo pour la saisie du circuit de l'AMC
    public ComboField getComboTypeConvention(String service) {
        Selector selector = SelectorFactory
                .xPath(
                        "//div[@id='" + service
                                + "-section-1']//label[contains(text(), 'Type de convention')]/../following-sibling::div//div[contains(@class,  'Select ')][1]");
        return new ComboField(getWebDriver(), selector, null, getScenarioReport());
    }

    // RadioGroup pour la sélection de tous les domaines
    public RadioGroupField getRadioTousDomaineService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].filtreDomaine']/..");
        return new RadioGroupField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Bouton d'ajout de regroupement
    public Button getButtonAjoutRegroupement() {
        Selector xPath = SelectorFactory
                .xPath("//button[@class='btn btn-default'][contains(text(), 'Ajouter un regroupement')]");
        return new Button(getWebDriver(), xPath, getScenarioReport());
    }

    // Input pour la saisie du regroupement detaille
    public TextField getItemRegroupementDetailleService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].critereRegroupementDetaille']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du regroupement
    public TextField getItemRegroupementService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].critereRegroupement']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du regroupement
    public TextField getItemNumClientService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].numClient']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du nom du client
    public TextField getItemNomClientService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].nomClient']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du type de fichier
    public TextField getItemTypeFichierService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].typeFichier']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du code périmetre
    public TextField getItemCodePerimetreService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].codePerimetre']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du périmetre
    public TextField getItemNomPerimetreService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].nomPerimetre']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du Gestionnaire de contrat
    public TextField getItemTypeGestionnaireContratService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].typeGestionnaireBO']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie du Gestionnaire de contrat
    public TextField getItemLibGestionnaireContratService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(INPUT_NAME_PILOTAGES + indexService + "].regroupements[0].libelleGestionnaireBO']");
        return new TextField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // RadioGroup pour la sélection du n° de contrat individuel
    public RadioGroupField getRadioContratIndividuelService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(
                        INPUT_NAME_PILOTAGES + indexService
                                + "].regroupements[0].numExterneContratIndividuel']/..");
        return new RadioGroupField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // RadioGroup pour la sélection du n° de contrat collectif
    public RadioGroupField getRadioContratCollectifService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(
                        INPUT_NAME_PILOTAGES + indexService
                                + "].regroupements[0].numExterneContratCollectif']/..");
        return new RadioGroupField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de période d'ouverture du service
    public DateField getItemDebutPeriodeReferenceService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(DIV_NAME_PILOTAGES + indexService + "].regroupements[0].periodeReferenceDebut']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

    // Input pour la saisie de période d'ouverture du service
    public DateField getItemFinPeriodeReferenceService(int indexService) {
        Selector xPath = SelectorFactory
                .xPath(DIV_NAME_PILOTAGES + indexService + "].regroupements[0].periodeReferenceFin']//input");
        return new DateField(getWebDriver(), xPath, null, getScenarioReport());
    }

}
