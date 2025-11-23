package com.cegedim.scenario;

import com.cegedim.next.e2e.core.annotation.E2EScenario;
import com.cegedim.next.e2e.core.scenario.Scenario;
import com.cegedim.pages.ParametragePage;
import com.cegedim.utils.AuthUtils;
import com.cegedim.utils.ImportUtils;

@E2EScenario(name = "Paramétrage Métier")
public class ParametrageScenario extends Scenario {
    @Override
    public void doScenario() {
        ImportUtils.doDeclarantsImport();

        AuthUtils.login(this);

        goTo(initialUrl + "/#/parameters");

        enterScenarioSection("Visualisation du paramétrage");
        /*
         * // Test du titre de la page enterScenarioItem("Titre de la page");
         * scenarioTitrePage();
         */
        // On affiche tous les panels
        enterScenarioItem("Affichage de tous les panels");
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());
        paramPage.getButtonExpandPanels().click();
        /*
         * // Conventionnement enterScenarioItem("Paramétrage de conventionnement");
         * scenarioTestPanelConventionnement();
         *
         * // Domaines enterScenarioItem("Paramétrage des domaines");
         * scenarioTestPanelDomaines();
         *
         * // Domaines ISanté enterScenarioItem("Paramétrage des domaines ISanté");
         * scenarioTestPanelISante();
         *
         * // Domaines SPSanté enterScenarioItem("Paramétrage des domaines SPSanté");
         * scenarioTestPanelSPSante();
         *
         * // Formules enterScenarioItem("Paramétrage des Formules");
         * scenarioTestPanelFormule();
         */
        // Objet transcodage
        enterScenarioItem("Paramétrage des Objets de transcodage");
        scenarioTestPanelTranscodage();
        /*
         * // Prestations enterScenarioItem("Paramétrage des Prestations");
         * scenarioTestPanelPrestations();
         *
         * // Processus enterScenarioItem("Paramétrage des Processus");
         * scenarioTestPanelProcessus();
         *
         * // Rejets enterScenarioItem("Paramétrage des Rejets");
         * scenarioTestPanelRejets();
         *
         * // ServicesComponent enterScenarioItem("Paramétrage des ServicesComponent");
         * scenarioTestPanelService();
         *
         * // Type de fichiers enterScenarioItem("Paramétrage des Types de fichiers");
         * scenarioTestPanelTypesFichiers();
         */
    }

    // Test du titre de la page
    public void scenarioTitrePage() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());
        paramPage.getPageTitle().hasContent("Paramétrage métier");
    }

    // Test du panel de Conventionnement
    public void scenarioTestPanelConventionnement() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 1;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Conventionnement");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des conventions");

        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("AC");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("ACTIL");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("AL");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("Almerys");
        /*
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 1).hasContent("IT/SP");
         * paramPage.getColumn(indicePanel, 1, 2).hasContent("IT/SP");
         *
         * paramPage.getColumn(indicePanel, 2, 1).hasContent("KA");
         * paramPage.getColumn(indicePanel, 2, 2).hasContent("KALIVIA");
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }

    // Test du panel de Domaines
    public void scenarioTestPanelDomaines() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 2;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Domaines");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des domaines");
        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        paramPage.getColumnTitle(indicePanel, 3).hasContent("Valeur transcodée");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("ALMER");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("Almérys");
        paramPage.getColumn(indicePanel, 1, 3).hasContent("ALMER");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("AMI");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("Auxiliaire Médical Infirmier");
        paramPage.getColumn(indicePanel, 2, 3).hasContent("AMI");
        /*
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 1).hasContent("CHAM");
         * paramPage.getColumn(indicePanel, 1, 2).hasContent("Chambre Particulière");
         * paramPage.getColumn(indicePanel, 1, 3).hasContent("CHAM");
         *
         * paramPage.getColumn(indicePanel, 2, 1).hasContent("CMU");
         * paramPage.getColumn(indicePanel, 2,
         * 2).hasContent("Couverture Maladie Universelle");
         * paramPage.getColumn(indicePanel, 2, 3).hasContent("CMU");
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }

    // Test du panel de Domaines ISanté
    public void scenarioTestPanelISante() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 3;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Domaines ISANTE");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des domaines ISANTE");
        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        paramPage.getColumnTitle(indicePanel, 3).hasContent("Valeur transcodée");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("AMO");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("Orthophonie");
        paramPage.getColumn(indicePanel, 1, 3).hasContent("AMO");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("AMP");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("Pédicurie podologue");
        paramPage.getColumn(indicePanel, 2, 3).hasContent("AMP");
        /*
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 1).hasContent("CURE");
         * paramPage.getColumn(indicePanel, 1, 2).hasContent("Cure Thermale");
         * paramPage.getColumn(indicePanel, 1, 3).hasContent("CURE");
         *
         * paramPage.getColumn(indicePanel, 2, 1).hasContent("DENS");
         * paramPage.getColumn(indicePanel, 2, 2).hasContent("Soins Dentaires");
         * paramPage.getColumn(indicePanel, 2, 3).hasContent("DENS");
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }

    // Test du panel de Domaines SPSanté
    public void scenarioTestPanelSPSante() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 4;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Domaines SPSANTE");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des domaines SPSANTE");
        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        paramPage.getColumnTitle(indicePanel, 3).hasContent("Valeur transcodée");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("AUDI");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("Audioprothèse");
        paramPage.getColumn(indicePanel, 1, 3).hasContent("AUDI");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("AUXM");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("Auxiliaire Médical");
        paramPage.getColumn(indicePanel, 2, 3).hasContent("AUXM");
        /*
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 1).hasContent("LABO");
         * paramPage.getColumn(indicePanel, 1, 2).hasContent("Laboratoire");
         * paramPage.getColumn(indicePanel, 1, 3).hasContent("LABO");
         *
         * paramPage.getColumn(indicePanel, 2, 1).hasContent("LARA");
         * paramPage.getColumn(indicePanel, 2, 2).hasContent("LABO + RADL");
         * paramPage.getColumn(indicePanel, 2, 3).hasContent("LARA");
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }

    // Test du panel de Formules
    public void scenarioTestPanelFormule() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 5;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Formules");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des formules");
        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Param. 1");
        paramPage.getColumnTitle(indicePanel, 3).hasContent("Param. 2");
        paramPage.getColumnTitle(indicePanel, 4).hasContent("Param. 3");
        paramPage.getColumnTitle(indicePanel, 5).hasContent("Param. 4");
        paramPage.getColumnTitle(indicePanel, 6).hasContent("Param. 5");
        paramPage.getColumnTitle(indicePanel, 7).hasContent("Param. 6");
        paramPage.getColumnTitle(indicePanel, 8).hasContent("Param. 7");
        paramPage.getColumnTitle(indicePanel, 9).hasContent("Param. 8");
        paramPage.getColumnTitle(indicePanel, 10).hasContent("Param. 9");
        paramPage.getColumnTitle(indicePanel, 11).hasContent("Param. 10");

        /*
         * // Contenu du panel paramPage.getColumn(indicePanel, 1, 1).hasContent("010");
         *
         * paramPage.getColumn(indicePanel, 2, 1).hasContent("011");
         * paramPage.getParamActivated(indicePanel, 2, 2).isVisible();
         *
         * paramPage.getColumn(indicePanel, 3, 1).hasContent("012");
         * paramPage.getParamActivated(indicePanel, 3, 3).isVisible();
         *
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 1).hasContent("025");
         * paramPage.getParamActivated(indicePanel, 1, 4).isVisible();
         * paramPage.getParamActivated(indicePanel, 1, 8).isVisible();
         *
         * paramPage.getColumn(indicePanel, 2, 1).hasContent("026");
         * paramPage.getParamActivated(indicePanel, 2, 2).isVisible();
         * paramPage.getParamActivated(indicePanel, 2, 4).isVisible();
         * paramPage.getParamActivated(indicePanel, 2, 8).isVisible();
         *
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }

    // Test du panel de Objet transcodage
    public void scenarioTestPanelTranscodage() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 6;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Objet transcodage");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des objets transcodés");
        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code objet");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Nom objet");
        paramPage.getColumnTitle(indicePanel, 3).hasContent("Noms des colonnes (séparés par des virgules)");

        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("Code_Mouvement");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("Code Mouvement");
        paramPage.getColumn(indicePanel, 1, 3).hasContent("Motif_evenement,Type_mouvement,Carte");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("Domaine_Droits");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("Domaines de droits");
        paramPage.getColumn(indicePanel, 2, 3).hasContent("Domaine_Droits");

        paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
        paramPage.getButtonPaginationPanel(indicePanel, "Suivant").isNotEnabled();

        // Création de transcodage
        paramPage.getButtonCreateTranscodage().click();
        paramPage.getItemCodeTranscodage().fill("Test_E2E");
        paramPage.getItemNomObjetTranscodage().fill("Test_UI");
        paramPage.getItemColonnesObjetTranscodage().fill("Code_Test,LibTest");
        paramPage.getButtonConfirmCreateTransco().click();

        // Suppression du transcodage
        paramPage.getButtonActionParam(indicePanel, 1, 4, 1).click();
        paramPage
                .getMessageAlerte()
                    .hasContent("Cet objet de transcodage ne peut être supprimé car il est utilisé dans un service.");
        paramPage.getButtonCloseAlert().click();

        paramPage.getButtonActionParam(indicePanel, 5, 4, 1).click();
        paramPage.getButtonConfirmDeleteParam().click();

        paramPage.getColumn(indicePanel, 5, 1).hasContent("Transco_Emetteur");
        paramPage.getColumn(indicePanel, 5, 2).hasContent("Partenaire");
        paramPage.getColumn(indicePanel, 5, 3).hasContent("Partenaire");
    }

    // Test du panel de Prestations
    public void scenarioTestPanelPrestations() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 7;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Prestations");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des prestations");

        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("160");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("Acte en B");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("300");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("acte non remboursable");
        /*
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 1).hasContent("314");
         * paramPage.getColumn(indicePanel, 1, 2).hasContent("*");
         *
         * paramPage.getColumn(indicePanel, 2, 1).hasContent("315");
         * paramPage.getColumn(indicePanel, 2, 2).hasContent("*");
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }

    // Test du panel de Processus
    public void scenarioTestPanelProcessus() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 8;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Processus");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des processus");

        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("Aiguillage");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("Aiguillage");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("Almerys");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("Almerys");

        paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
        paramPage.getButtonPaginationPanel(indicePanel, "Suivant").isNotEnabled();
    }

    // Test du panel de Rejets
    public void scenarioTestPanelRejets() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 9;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Rejets");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des rejets");

        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 2).hasContent("A03");
        paramPage.getColumn(indicePanel, 1, 3).hasContent("CONTRAT SANS ASSURE PRINCIPAL");

        paramPage.getColumn(indicePanel, 2, 2).hasContent("A04");
        paramPage.getColumn(indicePanel, 2, 3).hasContent("TYPE BENEFICIAIRE INCORRECT");
        /*
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 2).hasContent("B10");
         * paramPage.getColumn(indicePanel, 1,
         * 3).hasContent("PRENOM BENEFICIAIRE INVALIDE");
         *
         * paramPage.getColumn(indicePanel, 2, 2).hasContent("B11");
         * paramPage.getColumn(indicePanel, 2,
         * 3).hasContent("DATE NAISSANCE BENEFICIAIRE ABSENT");
         * paramPage.getCellule(indicePanel, 2, 1).click();
         * paramPage.getContenuDetailRejet(1).hasContent("Donnée non renseignée" );
         * paramPage.getContenuDetailRejet(2).hasContent("Bénéficiaire");
         * paramPage.getContenuDetailRejet(3).hasContent("Structure");
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }

    // Test du panel Service
    public void scenarioTestPanelService() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 10;

        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Service");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des services de transcodage");
        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code Service");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Objets");

        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("ALMV3");
        paramPage
                .getColumn(indicePanel, 1, 2)
                    .hasContent("Domaines de droits,Type bénéficiaire,Code Mouvement,Mode paiement");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("CARTE-DEMATERIALISEE");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("");

        paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
        paramPage.getButtonPaginationPanel(indicePanel, "Suivant").isNotEnabled();

        // Création d'un service
        paramPage.getButtonCreateService().click();
        paramPage.getItemCodeService().fill("TESTE2E");
        System.out.println("Selection de Domaines de droits");
        paramPage.getComboObjetService(true).select("Domaines de droits");
        System.out.println("Selection de Mode paiement");
        paramPage.getComboObjetService(false).select("Mode paiement");
        paramPage.getButtonConfirmActionService().click();
        paramPage.getColumn(indicePanel, 7, 1).hasContent("TESTE2E");
        paramPage.getColumn(indicePanel, 7, 2).hasContent("Domaines de droits,Mode paiement");

        // Modification de service
        paramPage.getButtonActionParam(indicePanel, 7, 3, 1).click();
        paramPage.getButtonSupprimeChoixCombo(indicePanel, 1, 2, 2).click(); // Enleve
                                                                             // le
                                                                             // 2ème
                                                                             // choix
        paramPage.getButtonConfirmActionService().click();
        paramPage.getColumn(indicePanel, 7, 1).hasContent("TESTE2E");
        paramPage.getColumn(indicePanel, 7, 2).hasContent("Domaines de droits");

        // Suppression du service
        paramPage.getButtonActionParam(indicePanel, 7, 3, 2).click();
        paramPage.getButtonConfirmDeleteParam().click();

        paramPage.getColumn(indicePanel, 7, 1).hasContent("TPG-IS");
        paramPage.getColumn(indicePanel, 7, 2).hasContent("Domaines de droits");
    }

    // Test du panel de Types fichiers
    public void scenarioTestPanelTypesFichiers() {
        ParametragePage paramPage = new ParametragePage(getWebDriver(), getScenarioReport());

        int indicePanel = 11;
        // Titre de panel
        paramPage.getPanelTitle(indicePanel).hasContent("Types fichiers");
        paramPage.getUnderPanelTitle(indicePanel).hasContent("Liste des types de fichiers");

        // Titre de colonne
        paramPage.getColumnTitle(indicePanel, 1).hasContent("Code");
        paramPage.getColumnTitle(indicePanel, 2).hasContent("Libellé");
        // Contenu du panel
        paramPage.getColumn(indicePanel, 1, 1).hasContent("ADB");
        paramPage.getColumn(indicePanel, 1, 2).hasContent("ADB");

        paramPage.getColumn(indicePanel, 2, 1).hasContent("AFD");
        paramPage.getColumn(indicePanel, 2, 2).hasContent("AFD");
        /*
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isNotEnabled();
         * paramPage.getButtonPaginationPanel(indicePanel, "Suivant").click();
         *
         * paramPage.getColumn(indicePanel, 1, 1).hasContent("TFD");
         * paramPage.getColumn(indicePanel, 1, 2).hasContent("TFD");
         *
         * paramPage.getButtonPaginationPanel(indicePanel, "Précédent").isEnabled();
         */
    }
}
